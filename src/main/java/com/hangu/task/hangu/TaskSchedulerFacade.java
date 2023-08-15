package com.hangu.task.hangu;

import com.hanggu.consumer.annotation.HangguReference;
import com.hangu.task.model.ApiResult;
import com.hangu.task.model.TimerTaskRequest;

/**
 * Created by wuzhenhong on 10/12/21 5:13 PM
 */
@HangguReference(groupName = "TaskScheduler$TaskSchedulerFacade", interfaceName = "TaskSchedulerFacade")
public interface TaskSchedulerFacade {

    /**
     * 返回任务ID
     * @param timerTaskRequest
     * @return
     */
    ApiResult<Long> submitTimerTask(TimerTaskRequest timerTaskRequest);

    /**
     * 删除任务
     * @param taskId
     * @return
     */
    ApiResult<Void> timerTaskDel(Long taskId);

    ApiResult<Void> timerTaskDel(String appServiceName, String apiServiceName, String apiMethodName, String customerId);

    /**
     * 禁止某定时任务
     * @param taskId
     * @return
     */
    ApiResult<Void> disableTask(Long taskId);

    ApiResult<Void> disableTask(String appServiceName, String apiServiceName, String apiMethodName, String customerId);

    /**
     * 启动某定时任务
     * @param taskId
     * @return
     */
    ApiResult<Void> enableTask(Long taskId);

    ApiResult<Void> enableTask(String appServiceName, String apiServiceName, String apiMethodName, String customerId);

    /**
     * 立即调用某任务
     * @param taskId
     * @return
     */
    ApiResult<Void> execTaskImmediately(Long taskId);

    ApiResult<Void> execTaskImmediately(String appServiceName, String apiServiceName, String apiMethodName, String customerId);
}
