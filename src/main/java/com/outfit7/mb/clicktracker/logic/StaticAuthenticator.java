package com.outfit7.mb.clicktracker.logic;

public class StaticAuthenticator {
	static final private String apiKey = "dog2dog"; 

	static public boolean AuthenticateKey(String apiKey) {
		return apiKey.equals(StaticAuthenticator.apiKey);
	}
	
	static public String getApiKey() {
		return apiKey;
	}
}
