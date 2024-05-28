package com.test.tfss.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.security.sasl.AuthenticationException;

import com.test.tfss.domain.auth.GenerateTokenRequest;
import com.test.tfss.domain.auth.GenerateTokenResponse;

public interface AuthService {

	/**
	 * Retrieve public key from resource folder.
	 * 
	 * @throws IOException
	 * 
	 * @throws FileNotFoundException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * 
	 */
	PublicKey getAuthPublicKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException;

	/**
	 * Convert string version into 'PublicKey' data type
	 * 
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	PublicKey convertToPublicKey(String publicKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException;

	/**
	 * Validate JWT token using public key,iss,exp,nbf
	 * 
	 * @throws AuthenticationException
	 * 
	 */
	Boolean validateJwtToken(String jwtAuthToken, PublicKey tokenPublicKey) throws AuthenticationException;

	/**
	 * Generate JWS Token
	 *
	 * @throws IOException
	 */
	GenerateTokenResponse generateJWSToken(GenerateTokenRequest request) throws Exception;

	public String generateJWSString() throws Exception;
	
	/**
	 * Retrieve private key
	 *
	 * @throws IOException
	 */
	PrivateKey retrievePrivateKey() throws Exception;

	/**
	 * Convert String type private key into 'PrivateKey' type.
	 *
	 */
	PrivateKey convertToPrivateKey(String privateKeyStr);

}
