package com.test.tfss.domain;

import org.springframework.util.Assert;

import com.test.tfss.domain.auth.GenerateTokenRequest;

public class RequestValidator {

//	public static void validateCreateEmployeeRequest(CreateEmployeeRequest request) {
//		Assert.notNull(request, "Create Employee Request cannot be null");
//		Assert.hasText(request.getFirstName(), "First name is required");
//		Assert.notNull(request.getSallary(), "Sallary is required");
//	}

	public static void validateGenerateTokenRequest(GenerateTokenRequest request) {
		Assert.notNull(request, "Generate JWS token Request cannot be null");
		Assert.hasText(request.getApiKey(), "API key is required");
		Assert.notNull(request.getAppId(), "Application id required");
		Assert.notNull(request.getCustomerId(), "Customer id required");
	}

}
