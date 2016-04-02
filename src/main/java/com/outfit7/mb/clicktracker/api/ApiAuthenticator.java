package com.outfit7.mb.clicktracker.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.api.server.spi.response.UnauthorizedException;
import com.outfit7.mb.clicktracker.logic.StaticAuthenticator;

/**
 * Simple apiKey authenticator;
 */
public class ApiAuthenticator {
	static private final String unauthorizedMsg = "Invalid API key.";
	
	static public void HTTPAuthenticateKey(HttpServletRequest request, String apiKey) throws UnauthorizedException {
		if (!StaticAuthenticator.AuthenticateKey(apiKey)) {
			Logger log = Logger.getLogger(ApiAuthenticator.class.getName());
			// Log IP for unauthorized access
			log.warning(String.format("Unauthorized admin access from IP: "+request.getRemoteAddr()));
			throw new UnauthorizedException(unauthorizedMsg);
		}
	}
}
