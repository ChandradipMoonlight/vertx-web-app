package com.moonlight.admin.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moonlight.config.ConfigHelper;
import com.moonlight.config.ConfigManager;
import com.moonlight.exception.RoutingError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public enum TokenService {
	INSTANCE;

	private static final Logger log = LoggerFactory.getLogger(TokenService.class);

	byte[] secret = ConfigHelper.INSTANCE.getAuthConfig().getString("secret").getBytes(StandardCharsets.UTF_8);

	public String generateToken(String userName, Integer id) {
		String token = null;
		try {
			token = JWT.create()
					.withExpiresAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
					.withClaim("userName",userName)
					.withClaim("id", id)
					.sign(Algorithm.HMAC256(secret));
		} catch (Exception e) {
			log.error("Error in generating token : {}", e.getMessage());
		}
		return token;
	}

	public DecodedJWT decodeToken(String token) {
		JWTVerifier verifier = null;
		verifier = JWT.require(Algorithm.HMAC256(secret)).build();
		DecodedJWT decodedJWT = null;
		try {
			decodedJWT = verifier.verify(token);
		} catch (JWTVerificationException e) {
			throw new RoutingError("Token is expired!", 401);
		}
		return decodedJWT;
	}

	public Integer getIdFromToken(String accessToken) {
		return decodeToken(accessToken).getClaim("id").asInt();
	}
	public String getUserNameFromToken(String accessToken) {
		return decodeToken(accessToken).getClaim("userName").asString();
	}

}
