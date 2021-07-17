package com.tsys.billings.customerpaymenthistory.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.tsys.billings.customerpaymenthistory.Exception.InvalidDateRangeException;
import com.tsys.billings.customerpaymenthistory.Exception.NotFoundException;
import com.tsys.billings.customerpaymenthistory.model.Error;
import com.tsys.billings.customerpaymenthistory.model.Errors;
import com.tsys.billings.customerpaymenthistory.model.History;
import com.tsys.billings.customerpaymenthistory.model.Output;
import com.tsys.billings.customerpaymenthistory.model.Response;
import com.tsys.billings.customerpaymenthistory.model.Results;

@org.springframework.web.bind.annotation.ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class, InvalidDateRangeException.class })
	protected ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
		Error error = new Error();
		error.setErrorCode("5000");
		error.setErrorMessage(ex.getMessage());
		Response response = buildResponse();
		response.getOutput().getError().getList().add(error);
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);

	}
	
	@ExceptionHandler(value = { NotFoundException.class })
	protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
		Error error = new Error();
		error.setErrorCode("6000");
		error.setErrorMessage(ex.getMessage());
		Response response = buildResponse();
		response.getOutput().getError().getList().add(error);
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

	}
	
	
	@ExceptionHandler(value = { RestClientException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleRestException(Exception ex, WebRequest request) {
		Error error = new Error();
		error.setErrorCode("7000");
		error.setErrorMessage(ex.getMessage());
		Response response = buildResponse();
		response.getOutput().getError().getList().add(error);
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.GATEWAY_TIMEOUT, request);

	}
	
	private Response buildResponse() {
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