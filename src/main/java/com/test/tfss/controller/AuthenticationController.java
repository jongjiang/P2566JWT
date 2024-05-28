package com.test.tfss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.tfss.controller.common.HTTPResponseHandler;
import com.test.tfss.controller.common.RequestMappings;
import com.test.tfss.domain.RequestValidator;
import com.test.tfss.domain.auth.GenerateTokenRequest;
import com.test.tfss.domain.auth.GenerateTokenResponse;
import com.test.tfss.service.AuthService;

@RestController
//@RequestMapping(value = "/auth")
@RequestMapping(RequestMappings.AUTH)
public class AuthenticationController extends HTTPResponseHandler {
	
	@Autowired
	private AuthService authService;

	/**
	 * Generate new JWS token.
	 */
//	@RequestMapping(value = "/token")
	@RequestMapping(value = RequestMappings.GENERATE_JWS_TOKEN, method = RequestMethod.POST)
	public @ResponseBody GenerateTokenResponse generateToken(@RequestBody GenerateTokenRequest request) {
		RequestValidator.validateGenerateTokenRequest(request);
		GenerateTokenResponse response = null;
		try {
			response = authService.generateJWSToken(request);
		} catch (Exception e) {
		}
		return response;
	}

}
