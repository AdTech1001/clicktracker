package com.outfit7.mb.clicktracker.api;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.repackaged.com.google.api.client.util.Base64;

public class HTTPAuthHelper {
	private String username;
	private String password;
	private boolean parseInitialized;
	private HttpServletRequest req;
	
	public HTTPAuthHelper(HttpServletRequest req) {
		this.req = req;
		this.parseInitialized = false;
	}
		
	private void parseHeader() throws IllegalArgumentException {
		parseInitialized = true;
		String header = req.getHeader("authorization");
		
		if (header == null) {
			throw new IllegalArgumentException("Basic authentication is required to use the API.");
		}
		else {
			// Header example:
			// Basic dDE23QDEQWDE=
			// Base 64 part decoded:
			// john:doe
			String[] splitHeader = header.split(" ");
			if (splitHeader.length != 2)
				throw new IllegalArgumentException("Invalid authentication header configured. Please use HTTP Basic authentication.");
			
			String credentials = new String(Base64.decodeBase64(splitHeader[1]));
			String[] splitCredentials = credentials.split(":");
			if (splitCredentials.length != 2)
				throw new IllegalArgumentException("Username or password missing. Please supply both!");
			this.username = splitCredentials[0];
			this.password = splitCredentials[1];
		}
	}
	
	public String getUsername() throws IllegalArgumentException {
		if (!parseInitialized)
			parseHeader();
		
		return username;
	}
	
	public String getPassword() throws IllegalArgumentException {
		if (!parseInitialized)
			parseHeader();
		
		return password;
	}
}
