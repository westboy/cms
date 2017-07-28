package com.zhiliao.component.quartz;

import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**

*设置调度工厂，并返回调度管理器

*/

@Configuration
public class QuartzConfiguration {

//    @Autowired
//    @Qualifier("quartzDataSource")
//    private DataSource dataSource;
//
//    @Autowired
//    @Qualifier("quartzTransactionManager")
//    private DataSourceTransactionManager transactionManager;

    @Bean
    public SchedulerFactoryBean getSchedulerFactoryBean() throws IOException, SchedulerException {
            SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
            schedulerFactoryBean .setSchedulerName("CRMscheduler");
//            schedulerFactoryBean .setDataSource(dataSource);
//            schedulerFactoryBean .setTransactionManager(transactionManager);
            schedulerFactoryBean .setQuartzProperties(this.quartzProperties());
            schedulerFactoryBean .setApplicationContextSchedulerContextKey("applicationContextKey");
            schedulerFactoryBean .setOverwriteExistingJobs(true);
            schedulerFactoryBean .setAutoStartup(true);
            schedulerFactoryBean .setStartupDelay(30);
            return schedulerFactoryBean;
    }



        public Properties quartzProperties() throws IOException {  
            Properties prop = new Properties();  
            prop.put("quartz.scheduler.instanceName", "CRMscheduler");
            prop.put("org.quartz.scheduler.instanceId", "AUTO");  
            prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
            prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");  
            prop.put("org.quartz.threadPool.threadCount", "20");
            prop.put("org.quartz.threadPool.threadPriority","5");
//            prop.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
//            prop.put("org.quartz.jobStore.isClustered","true");
//            prop.put("org.quartz.jobStore.clusterCheckinInterval","10000");
//            prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime","1");
//            prop.put("org.quartz.jobStore.misfireThreshold","120000");
//            prop.put("org.quartz.jobStore.tablePrefix","qrtz_");
            return prop;  
        }

}