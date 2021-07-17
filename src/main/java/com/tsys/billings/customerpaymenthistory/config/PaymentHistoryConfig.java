package com.tsys.billings.customerpaymenthistory.config;

import java.util.concurrent.Executor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;


  @Configuration 
  public class PaymentHistoryConfig {
  
		@Bean
		@LoadBalanced
		public RestTemplate getRestTemplate() {
			return new RestTemplate();
		}
		
		@Bean
	    public Executor executor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(10);
	        executor.setMaxPoolSize(10);
	        executor.setQueueCapacity(1000);
	        executor.initialize();
	        return executor;
	    }
}

