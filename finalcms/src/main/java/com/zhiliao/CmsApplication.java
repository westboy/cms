package com.zhiliao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAsync
public class CmsApplication {

	public static void main(String[] args) throws Exception{
		System.out.println("Hello world!");
		SpringApplication.run(CmsApplication.class, args);
	}
}