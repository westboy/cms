package com.zhiliao.common.quartz;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**

*设置调度工厂，并返回调度管理器

*/

@Configuration
public class QuartzConfig {

        @Bean
        public SchedulerFactoryBean getSchedulerFactoryBean(@Qualifier("quartzDataSource") DataSource dataSource,
                                                            @Qualifier("quartzTransactionManager")DataSourceTransactionManager transactionManager) throws IOException, SchedulerException {
            SchedulerFactoryBean bean = new SchedulerFactoryBean();
            bean.setSchedulerName("CRMscheduler");
            bean.setDataSource(dataSource);
            bean.setTransactionManager(transactionManager);
            bean.setQuartzProperties(quartzProperties());
            bean.setStartupDelay(10);
            bean.setApplicationContextSchedulerContextKey("applicationContextKey");
            bean.setOverwriteExistingJobs(true);
            bean.setAutoStartup(true);
            return bean;
        } 


        public Properties quartzProperties() throws IOException {  
            Properties prop = new Properties();  
            prop.put("quartz.scheduler.instanceName", "CRMscheduler");
            prop.put("org.quartz.scheduler.instanceId", "AUTO");  
            prop.put("org.quartz.scheduler.skipUpdateCheck", "true");
            prop.put("org.quartz.scheduler.jobFactory.class", "org.quartz.simpl.SimpleJobFactory"); 
            prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");  
            prop.put("org.quartz.threadPool.threadCount", "20");
            prop.put("org.quartz.threadPool.threadPriority","5");
            prop.put("org.quartz.jobStore.class","org.quartz.impl.jdbcjobstore.JobStoreTX");
            prop.put("org.quartz.jobStore.clusterCheckinInterval","10000");
            prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime","1");
            prop.put("org.quartz.jobStore.misfireThreshold","120000");
            prop.put("org.quartz.jobStore.tablePrefix","QRTZ");
            return prop;  
        }  
}