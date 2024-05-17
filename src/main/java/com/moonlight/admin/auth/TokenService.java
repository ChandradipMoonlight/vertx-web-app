package com.moonlight.admin.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.moonlight.config.ConfigHelper;
import com.moonlight.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private String token(String userName) {
		String token = null;
		try {
			token = JWT.create()
					.withExpiresAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
					.withClaim("userName",userName)
					.sign(Algorithm.HMAC256(ConfigHelper.INSTANCE.getAuthConfig().getString("secret").getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			log.error("Error in generating token : {}", e.getMessage());
		}
		return null;
	}
}
