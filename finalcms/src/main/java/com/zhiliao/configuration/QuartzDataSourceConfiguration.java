package com.zhiliao.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

public class QuartzDataSourceConfiguration {

    @Bean(name = "quartzDataSource")
    @ConfigurationProperties(prefix = "datasource.quartz")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "quartzTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("quartzDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}