package com.hangu.task.starter;

import com.hanggu.consumer.annotation.ReferenceScan;
import com.hanggu.consumer.factory.ReferenceFactoryBean;
import com.hanggu.provider.factory.ServiceFactoryBean;
import com.hangu.task.constant.TaskScheduleConstant;
import com.hangu.task.hangu.TaskSchedulerFacade;
import com.hangu.task.hangu.TaskSchedulerWorker;
import com.hangu.task.hangu.impl.TaskSchedulerWorkerImpl;
import com.hangu.task.properties.TaskSchedulerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Created by wuzhenhong on 10/11/21 10:02 AM
 */
@Configuration
@EnableConfigurationProperties(TaskSchedulerProperties.class)
@ConditionalOnProperty(prefix = "task.scheduler", name = "enable", havingValue = "true", matchIfMissing = false)
@ReferenceScan(basePackages = {"com.hangu.task.hangu"})
public class TaskSchedulerAutoConfiguration {

    private static final int NCPUS = Runtime.getRuntime().availableProcessors() << 3;

    @Autowired
    private TaskSchedulerProperties taskSchedulerProperties;

    @Bean
    public ServiceFactoryBean<TaskSchedulerWorker> taskSchedulerWorker() {

        String appServiceName = taskSchedulerProperties.getAppServiceName();
        if(StringUtils.isEmpty(appServiceName)) {
            throw new RuntimeException("启用定时任务调度时，task.scheduler.appServiceName 的属性不能为空");
        }

        ServiceFactoryBean<TaskSchedulerWorker> serviceFactoryBean = new ServiceFactoryBean(
            TaskScheduleConstant.WORKER_PROXY_NAME + taskSchedulerProperties.getAppServiceName(),
            TaskScheduleConstant.WORKER_INTERFACE_NAME,
            "",
            TaskSchedulerWorker.class,
            new TaskSchedulerWorkerImpl()
        );

        Integer coreThreadNum = taskSchedulerProperties.getTaskDealCoreThreadNum();
        Integer maxThreadNum = taskSchedulerProperties.getTaskDealMaxThreadNum();
        Integer maxQueueSize = taskSchedulerProperties.getTaskDealMaxQueueSize();

        return serviceFactoryBean;
    }

    @Bean
    public ReferenceFactoryBean<TaskSchedulerFacade> taskSchedulerFacade(Environment env) {

        ReferenceFactoryBean<TaskSchedulerFacade> referenceFactoryBean = new ReferenceFactoryBean(
            "TaskScheduler$TaskSchedulerFacade",
            "TaskSchedulerFacade",
            "",
            TaskSchedulerFacade.class
            );

        return referenceFactoryBean;
    }
}
