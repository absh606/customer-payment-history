package com.tsys.billings.customerpaymenthistory.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.tsys.billings.customerpaymenthistory.Exception.InvalidDateRangeException;
import com.tsys.billings.customerpaymenthistory.Exception.NotFoundException;
import com.tsys.billings.customerpaymenthistory.dao.CustomerDao;
import com.tsys.billings.customerpaymenthistory.model.Error;
import com.tsys.billings.customerpaymenthistory.model.Errors;
import com.tsys.billings.customerpaymenthistory.model.History;
import com.tsys.billings.customerpaymenthistory.model.Output;
import com.tsys.billings.customerpaymenthistory.model.Response;
import com.tsys.billings.customerpaymenthistory.model.Results;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CustomerPaymentHistoryService {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CustomerDao paymentHistoryDao;
	
	@Value("${app.currentHistory.url}")
	String currentHistoryUrl;
	
	@Value("${app.previousHistory.url}")
	String previousHistoryUrl;

	public Response getPaymentHistory(String customerId, Date fromDate, Date toDate, String limit, String offset) throws InvalidDateRangeException,NotFoundException,SQLException,
	InterruptedException, ExecutionException, RestClientException {
		Response response = new Response();
		Output output = new Output();
		Results histResult = new Results();
		Errors errors = new Errors();
		List<History> histories = new ArrayList<History>();
		List<Error> errorlist = new ArrayList<Error>();
			Boolean isCustomerExist = paymentHistoryDao.isCustomerExist(customerId);
			if(!isCustomerExist) {
				throw new NotFoundException("Customer does not exist");
			}
			
			
			Calendar cal = Calendar.getInstance();
			Calendar calFrom = Calendar.getInstance();
			Calendar calTo = Calendar.getInstance();

			//set the given date in one of the instance and current date in the other
			calFrom.setTime(fromDate);
			calTo.setTime(toDate);
			Boolean isFromDateInCurrentMonth = Boolean.FALSE;
			Boolean isFromDateInPreviousMonth = Boolean.FALSE;
			Boolean isToDateInCurrentMonth = Boolean.FALSE;
			Boolean isToDateInPreviousMonth = Boolean.FALSE;

			
			if(calFrom.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
			    if(calFrom.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
			    	isFromDateInCurrentMonth = Boolean.TRUE;
			    }
			}
			
			
			if(calTo.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
			    if(calTo.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
			    	isToDateInCurrentMonth = Boolean.TRUE;
			    }
			}
		
			if(calFrom.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
			    if(calFrom.get(Calendar.MONTH) < cal.get(Calendar.MONTH)) {
			    	isFromDateInPreviousMonth = Boolean.TRUE;
			    }
			}
			
			if(calTo.get(Calendar.YEAR) == cal.get(Calendar.YEAR)) {
			    if(calTo.get(Calendar.MONTH) < cal.get(Calendar.MONTH)) {
			    	isToDateInPreviousMonth = Boolean.TRUE;
			    }
			}
			
			if(calTo.get(Calendar.DATE) < cal.get(Calendar.DATE)) {
				throw new InvalidDateRangeException("Incorrect date supplied");
			}
		
			if(isFromDateInCurrentMonth && isToDateInCurrentMonth) {			 
				CompletableFuture<Response> futureCurrentHistoryResponse = getCurrentPaymentHistory(customerId, fromDate, toDate, limit, offset);
				Response  currentHistoryResponse = futureCurrentHistoryResponse.get();
				if(currentHistoryResponse != null && currentHistoryResponse.getOutput() != null && currentHistoryResponse.getOutput().getResult() != null) {
					histories.addAll(currentHistoryResponse.getOutput().getResult().getList());
					if(currentHistoryResponse.getOutput().getError()!= null) {
						errorlist.addAll(currentHistoryResponse.getOutput().getError().getList());
					}
				}		
			}else if(isFromDateInCurrentMonth && isToDateInPreviousMonth) {
				CompletableFuture<Response> futureCurrentHistoryResponse = getCurrentPaymentHistory(customerId, fromDate, toDate, limit, offset);
				CompletableFuture<Response> futurePreviousHistoryResponse = getPreviousPaymentHistory(customerId, fromDate, toDate, limit, offset);
				
				Response currentHistoryResponse = futureCurrentHistoryResponse.get();
				histories.addAll(currentHistoryResponse.getOutput().getResult().getList());
				if(currentHistoryResponse.getOutput().getError()!= null) {
					errorlist.addAll(currentHistoryResponse.getOutput().getError().getList());
				}
				
				Response previousHistoryResponse =  futurePreviousHistoryResponse.get();
				histories.addAll(previousHistoryResponse.getOutput().getResult().getList());
				if(previousHistoryResponse.getOutput().getError()!= null) {
					errorlist.addAll(previousHistoryResponse.getOutput().getError().getList());
				}
				
			}else if(isFromDateInPreviousMonth && isToDateInPreviousMonth) {
				CompletableFuture<Response> futurePreviousHistoryResponse = getPreviousPaymentHistory(customerId, fromDate, toDate, limit, offset);
				Response previousHistoryResponse =  futurePreviousHistoryResponse.get();
				histories.addAll(previousHistoryResponse.getOutput().getResult().getList());
				if(previousHistoryResponse.getOutput().getError()!= null) {
					errorlist.addAll(previousHistoryResponse.getOutput().getError().getList());
				}
			}else {
				
					throw new InvalidDateRangeException("Incorrect date supplied");
			}
		
		histResult.setList(histories);
		output.setResult(histResult);
		errors.setList(errorlist);
		output.setError(errors);
		response.setOutput(output);
		return response;
	}

	
	
	@Async
	@CircuitBreaker(fallbackMethod = "getDefault", name = "customerPaymentService")
	public CompletableFuture<Response> getCurrentPaymentHistory(String customerId, Date fromDate, Date toDate, String limit,
			String offset)  throws RestClientException{
		Response currentHistoryResponse = restTemplate.getForObject(
				currentHistoryUrl + "?customerId="+
			  customerId+"&fromDate="+fromDate+"&toDate="+toDate+"&limit="+limit+"&offset="
			  +offset, Response.class);
		return CompletableFuture.completedFuture(currentHistoryResponse);
	}
	
	@Async
	@CircuitBreaker(fallbackMethod = "getDefault", name = "customerPaymentService")
	public CompletableFuture<Response> getPreviousPaymentHistory(String customerId, Date fromDate, Date toDate, String limit,
			String offset) throws RestClientException{
		Response previousHistoryResponse = restTemplate.getForObject(
			 previousHistoryUrl +"?customerId="+
			  customerId+"&fromDate="+fromDate+"&toDate="+toDate+"&limit="+limit+"&offset="
			  +offset, Response.class);
		return CompletableFuture.completedFuture(previousHistoryResponse);
	}
	
	 public Response getDefault(String customerId, Throwable throwable){
		 
		 	Response response = new Response();
			Output output = new Output();
			Results histResult = new Results();
			Errors errors = new Errors();
			List<History> histories = new ArrayList<History>();
			List<Error> errorlist = new ArrayList<Error>();
			histResult.setList(histories);
			output.setResult(histResult);
			errors.setList(errorlist);
			output.setError(errors);
			response.setOutput(output);
			return response;
	    }

}
