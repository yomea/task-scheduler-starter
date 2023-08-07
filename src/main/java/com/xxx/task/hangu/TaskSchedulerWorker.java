package com.xxx.task.hangu;

import com.xxx.task.model.ApiResult;
import com.xxx.task.model.TaskContext;

/**
 * Created by wuzhenhong on 10/11/21 10:26 AM
 */
public interface TaskSchedulerWorker {

    ApiResult<Long> asyncDealTask(TaskContext taskContext);
}
