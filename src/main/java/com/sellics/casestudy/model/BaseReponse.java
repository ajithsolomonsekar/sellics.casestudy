package com.sellics.casestudy.model;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class BaseReponse {

	@JsonProperty("HttpStatus")
	protected HttpStatus httpStatus;

	@JsonProperty("DeveloperMessage")
	protected String developerMessage;

	public BaseReponse() {
		super();
	}

	public BaseReponse(HttpStatus httpStatus, String developerMessage) {
		super();
		this.httpStatus = httpStatus;
		this.developerMessage = developerMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

}
