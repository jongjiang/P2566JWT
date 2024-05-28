package com.test.tfss.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.security.sasl.AuthenticationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.test.tfss.domain.auth.GenerateTokenRequest;
import com.test.tfss.domain.auth.GenerateTokenResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class AuthServiceImpl implements AuthService {

	private final Logger logger = LogManager.getLogger(AuthServiceImpl.class);

	private final static String algorithem = "RSA";

	// Get public key REGEX pattern from props.
	@Value("${auth.jwt.header.publickey.regx}")
	private String publickeyPatternRegx;

	// Get issuer name for the JWT token.
	@Value("${auth.jwt.issuer}")
	private String issuerName;

	/** The Constant RSA. */
	private static final String RSA = "RSA";

	/** The header strip regex. */
	private String headerStripRegex = "(-{3,}([\\s\\S]*?)-{3,})|(\\r\\n)";

	/** The token life time. */
	private int tokenLifespan = 360;

	/** The issuer name. */
	private String vuiTokenIssuerName = "test";

	@Override
	public PublicKey getAuthPublicKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		// Retrieve public key file from resource folder.
		String content = new String(Files.readAllBytes(Paths.get(new ClassPathResource("auth-keys/public").getURI())), "utf-8");
		String raw = content.replace("-----BEGIN PUBLIC KEY-----", "")
				            .replace("-----END PUBLIC KEY-----", "")
				            .replaceAll("\\s+", "");
		byte[] encodedKey = Base64.getDecoder().decode(raw);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		EncodedKeySpec spec = new X509EncodedKeySpec(encodedKey);
		PublicKey publicKey = kf.generatePublic(spec);

		return publicKey;
	}

	@Override
	public PublicKey convertToPublicKey(String publicKeyStr) throws InvalidKeySpecException, NoSuchAlgorithmException {
		// Remove public key header values (Ex : -----BEGIN PUBLIC KEY----- )
		publicKeyStr = publicKeyStr.replaceAll(publickeyPatternRegx, "");

		// Get public key as a byte array.
		byte[] publicBytes = Base64.getDecoder().decode(publicKeyStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);

		// Key factories are used to convert keys (opaque cryptographic keys of type
		// Key) into key specifications (transparent representations of the underlying
		// key material), and vice versa.
		KeyFactory kf = KeyFactory.getInstance(algorithem);
		PublicKey publicKey = kf.generatePublic(keySpec);

		return publicKey;
	}

	@Override
	public Boolean validateJwtToken(String jwtAuthToken, PublicKey tokenPublicKey) throws AuthenticationException {
		try {
			// This is ultimately a JSON map and any values can be added to it, but JWT
			// standard names are provided as type-safe getters and setters for convenience.
			Jws<Claims> jws = null;
			jws = Jwts.parserBuilder().setSigningKey(tokenPublicKey).build().parseClaimsJws(jwtAuthToken);
			// Here we are checking "iss" (Issuer) Claim , "exp" (Expiration Time) Claim,
			// "nbf" (Not Before) Claim
			if ((jws.getBody().getIssuer().equals(issuerName))
					&& (jws.getBody().getExpiration().compareTo(new Date()) > 0)
					&& (jws.getBody().getNotBefore().compareTo(new Date()) < 0)) {

				return true;
			}

			throw new AuthenticationException("Invalid JWT token");

		} catch (JwtException e) {
			throw new AuthenticationException("Invalid JWT token signature");
		}
	}

	// Generate JWS token
	@Override
	public GenerateTokenResponse generateJWSToken(GenerateTokenRequest request) throws Exception {
		GenerateTokenResponse response = new GenerateTokenResponse();

		// Retrieve private key
		PrivateKey privateKey = this.retrievePrivateKey();
		Assert.state(privateKey != null, "Private key not configured");

		// Build payload

		// Generate token id.
		String tokenId = "test_app" + UUID.randomUUID().toString();
		// Generate payload subject using application related sensitive values.
		String tokenSubject = populateSubject(request);
		// Generate token issued date.
		Calendar cal = Calendar.getInstance();
		Date tokenIssuedAt = cal.getTime();
		// Generate token expiration time.
		cal.add(Calendar.SECOND, tokenLifespan);
		Date tokenExpirationAt = cal.getTime();

		String jws = Jwts.builder()
						 .setId(tokenId)
						 .setSubject(tokenSubject)
						 .setIssuer(vuiTokenIssuerName)
				         .setIssuedAt(tokenIssuedAt)
				         .setNotBefore(tokenIssuedAt)
				         .setExpiration(tokenExpirationAt)
				         .signWith(privateKey, SignatureAlgorithm.RS256)
				         .compact();

		response.setJwsKey(jws);

		return response;
	}
	
	public String generateJWSString() throws Exception {
		// Retrieve private key
		PrivateKey privateKey = this.retrievePrivateKey();
		Assert.state(privateKey != null, "Private key not configured");

		// Build payload

		// Generate token id.
		String tokenId = "test_app" + UUID.randomUUID().toString();
		// Generate payload subject using application related sensitive values.
		JSONObject subjJson = new JSONObject();
		subjJson.put("API-KEY", "API-KEY");
		subjJson.put("CUSTOMER-ID", "CUSTOMER-ID");
		subjJson.put("APPLICATION-ID", "APPLICATION-ID");
		String tokenSubject = subjJson.toString();
		// Generate token issued date.
		Calendar cal = Calendar.getInstance();
		Date tokenIssuedAt = cal.getTime();
		// Generate token expiration time.
		cal.add(Calendar.SECOND, tokenLifespan);
		Date tokenExpirationAt = cal.getTime();

		String jws = Jwts.builder()
						 .setId(tokenId)
						 .setSubject(tokenSubject)
						 .setIssuer(vuiTokenIssuerName)
				         .setIssuedAt(tokenIssuedAt)
				         .setNotBefore(tokenIssuedAt)
				         .setExpiration(tokenExpirationAt)
				         .signWith(privateKey, SignatureAlgorithm.RS256)
				         .compact();

		return jws;
	}

	// Populate 'subject' field using application sensitive data.
	private String populateSubject(GenerateTokenRequest request) {
		JSONObject subjJson = new JSONObject();
		subjJson.put("API-KEY", request.getApiKey());
		subjJson.put("CUSTOMER-ID", request.getCustomerId());
		subjJson.put("APPLICATION-ID", request.getAppId());

		return subjJson.toString();
	}

	// Retrieve private key
	public PrivateKey retrievePrivateKey() throws Exception {
		// Retrieve private key file from resource folder.
		String content = new String(Files.readAllBytes(Paths.get(new ClassPathResource("auth-keys/private").getURI())), "utf-8");
		String raw = content.replace("-----BEGIN PRIVATE KEY-----", "")
				            .replace("-----END PRIVATE KEY-----", "")
				            .replaceAll("\\s+", "");
		byte[] encodedKey = Base64.getDecoder().decode(raw);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		EncodedKeySpec spec = new PKCS8EncodedKeySpec(encodedKey);
		PrivateKey privateKey = kf.generatePrivate(spec);

		return privateKey;
	}

	// Convert String type private key into 'PrivateKey' type.
	public PrivateKey convertToPrivateKey(String privateKeyStr) {
		if (StringUtils.hasText(privateKeyStr)) {

			// strip the private key headers using REGEX
			privateKeyStr = privateKeyStr.replaceAll(headerStripRegex, "");

			// Generate base64 encoded key byte array.
			byte[] encodedKey = Base64.getDecoder().decode(privateKeyStr);
			// Generate key spec
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);

			KeyFactory kf;
			try {

				// Generate RSA KeyFactory instance.
				kf = KeyFactory.getInstance(RSA);
				// Convert into PrivateKey type.
				PrivateKey privateKey = kf.generatePrivate(keySpec);

				return privateKey;
			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				logger.error("Error occured while converting to Private Key");
			}
		}

		return null;
	}

}
