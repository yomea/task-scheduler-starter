package com.xxx.task.hangu;

import com.hanggu.consumer.callback.RpcResponseCallback;
import com.xxx.task.model.ApiResult;
import com.xxx.task.model.TaskContext;

/**
 * Created by wuzhenhong on 10/11/21 10:26 AM
 */
public interface TaskSchedulerWorker {

    /**
     * 用于客户端引入该包时进行异步调用
     * {@link #asyncDealTask(TaskContext)}
     * @param taskContext
     * @param callback
     * @return
     */
    default ApiResult<Long> asyncDealTask(TaskContext taskContext, RpcResponseCallback callback) {
        return null;
    }
    ApiResult<Long> asyncDealTask(TaskContext taskContext);
}
