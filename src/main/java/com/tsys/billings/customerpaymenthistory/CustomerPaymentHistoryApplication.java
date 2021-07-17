package com.tsys.billings.customerpaymenthistory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableEurekaClient
@EnableAsync
public class CustomerPaymentHistoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerPaymentHistoryApplication.class, args);
	}

}
