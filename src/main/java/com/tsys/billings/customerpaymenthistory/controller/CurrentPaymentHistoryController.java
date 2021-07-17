package com.tsys.billings.customerpaymenthistory.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tsys.billings.customerpaymenthistory.Exception.InvalidDateRangeException;
import com.tsys.billings.customerpaymenthistory.Exception.NotFoundException;
import com.tsys.billings.customerpaymenthistory.model.Response;
import com.tsys.billings.customerpaymenthistory.services.CustomerPaymentHistoryService;

@RestController
public class CurrentPaymentHistoryController {
	
	@Autowired
	CustomerPaymentHistoryService customerPaymentHistoryService;
	
	@GetMapping("/customers/{customerId}/paymentHistories")
	public Response getPaymentHistory(@PathVariable("customerId") @NotBlank @Size(max = 10) String customerId, 
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate, 
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate, 
			@RequestParam String limit, @RequestParam String offset) throws InvalidDateRangeException,NotFoundException,
			SQLException, InterruptedException, ExecutionException {
		return customerPaymentHistoryService.getPaymentHistory(customerId, fromDate, toDate, limit, offset);
	}
	
}
