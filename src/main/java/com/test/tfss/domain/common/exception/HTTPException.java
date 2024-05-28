package com.test.tfss.domain.common.exception;

public class HTTPException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String statusMessage;

	private String externalMessage;

	private String endUserMessage;

	private boolean logException = true;

	public HTTPException(String statusMessage) {
		super();
		this.statusMessage = statusMessage;
	}

	public HTTPException(String statusMessage, String endUserMessage) {
		super();
		this.statusMessage = statusMessage;
		this.endUserMessage = endUserMessage;
	}

	public HTTPException(String statusMessage, String endUserMessage, String externalMessage) {
		super();
		this.statusMessage = statusMessage;
		this.endUserMessage = endUserMessage;
		this.externalMessage = externalMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getExternalMessage() {
		return externalMessage;
	}

	public void setExternalMessage(String externalMessage) {
		this.externalMessage = externalMessage;
	}

	public boolean isLogException() {
		return logException;
	}

	public void setLogException(boolean logException) {
		this.logException = logException;
	}

	public String getEndUserMessage() {
		return endUserMessage;
	}
}
