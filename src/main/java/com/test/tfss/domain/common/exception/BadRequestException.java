package com.test.tfss.domain.common.exception;

public class BadRequestException extends HTTPException {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String statusMessage, String endUserMessage, String externalMessage) {
		super(statusMessage, endUserMessage, externalMessage);
	}

	public BadRequestException(String statusMessage, String endUserMessage) {
		super(statusMessage, endUserMessage);
	}

	public BadRequestException(String statusMessage) {
		super(statusMessage);
	}

}