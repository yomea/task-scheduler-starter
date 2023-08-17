package com.hangu.task.starter;

import com.hangu.common.entity.ServerInfo;
import com.hangu.consumer.annotation.ReferenceScan;
import com.hangu.consumer.factory.ReferenceFactoryBean;
import com.hangu.provider.factory.ServiceFactoryBean;
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
        if (StringUtils.isEmpty(appServiceName)) {
            throw new RuntimeException("启用定时任务调度时，task.scheduler.appServiceName 的属性不能为空");
        }

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setGroupName(TaskScheduleConstant.WORKER_PROXY_NAME + taskSchedulerProperties.getAppServiceName());
        serverInfo.setInterfaceName(TaskScheduleConstant.WORKER_INTERFACE_NAME);
        serverInfo.setVersion("");
        ServiceFactoryBean<TaskSchedulerWorker> serviceFactoryBean = new ServiceFactoryBean(serverInfo,
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

        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setGroupName("TaskScheduler$TaskSchedulerFacade");
        serverInfo.setInterfaceName("TaskSchedulerFacade");
        serverInfo.setVersion("");
        ReferenceFactoryBean<TaskSchedulerFacade> referenceFactoryBean = new ReferenceFactoryBean(
            serverInfo,
            TaskSchedulerFacade.class
        );

        return referenceFactoryBean;
    }
}
