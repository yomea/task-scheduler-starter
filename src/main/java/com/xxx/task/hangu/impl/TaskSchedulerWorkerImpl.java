package com.xxx.task.hangu.impl;

import com.hanggu.provider.annotation.HangguService;
import com.xxx.task.constant.TaskScheduleConstant;
import com.xxx.task.context.TaskScheduleMetaContext;
import com.xxx.task.hangu.TaskSchedulerWorker;
import com.xxx.task.model.ApiResult;
import com.xxx.task.model.MethodHolder;
import com.xxx.task.model.TaskContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wuzhenhong on 10/11/21 11:14 AM
 */
@Slf4j
public class TaskSchedulerWorkerImpl implements TaskSchedulerWorker {

    @Override
    public ApiResult<Long> asyncDealTask(TaskContext taskContext) {

        String apiServiceName = taskContext.getApiServiceName();
        String methodName = taskContext.getMethodName();
        Long taskId = taskContext.getTaskId();

        MethodHolder methodHolder = TaskScheduleMetaContext.get(apiServiceName, methodName);

        if (methodHolder == null) {
            log.error("定时任务处理逻辑不存在 apiServiceName -》{}，methodName -》{}", apiServiceName, methodName);
            return ApiResult.fail(String.format("定时任务处理逻辑不存在 apiServiceName -》%s，methodName -》%s", apiServiceName, methodName), 500);
        }

        Object bean = methodHolder.getBean();
        Method method = methodHolder.getMethod();
        Class<?> clazz = methodHolder.getReturnType();
        boolean needTaskContext = methodHolder.isNeedTaskContext();

        try {
            Object returnVal = method.invoke(bean, needTaskContext ? new Object[] {taskContext} : new Object[]{});
            if (clazz == int.class || clazz == Integer.class) {
                Integer retCode = (Integer) returnVal;
                return retCode == 0 ? ApiResult.success(taskId) : ApiResult.fail("任务处理失败！", retCode);
            } else if (ApiResult.class.isAssignableFrom(clazz)) {
                ApiResult apiResult = (ApiResult) returnVal;
                return apiResult.isSuccess() ? ApiResult.success(taskId) : ApiResult.fail(apiResult.getMsg(), apiResult.getCode());
            } else {
                return ApiResult.success(taskId);
            }

        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
            return ApiResult.fail("IllegalAccessException: " + e.getMessage(), 500);
        } catch (InvocationTargetException e) {
            log.error("InvocationTargetException", e);
            return ApiResult.fail("InvocationTargetException" + e.getTargetException() != null ? e.getTargetException().getMessage() :e.getMessage(), 500);
        }
    }
}
