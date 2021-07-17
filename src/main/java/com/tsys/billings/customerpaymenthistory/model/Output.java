package com.tsys.billings.customerpaymenthistory.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Output {
	
	@JsonInclude(value = Include.NON_NULL)
	Results result;
	
	@JsonInclude(value = Include.NON_NULL)
	Errors error;
	
	public Results getResult() {
		return result;
	}
	
	public void setResult(Results result) {
		this.result = result;
	}
	
	public Errors getError() {
		return error;
	}
	public void setError(Errors error) {
		this.error = error;
	}
}
