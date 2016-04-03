package com.outfit7.mb.clicktracker;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ClickTrackerProperties {
	private String defaultTrackingLink;
	private static final String failsafeUrl = "http://outfit7.com";
	
	public ClickTrackerProperties() {
		String filename = "clicktracker.properties";
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(filename);
		Properties properties = new Properties();
		
		try {
			this.defaultTrackingLink = properties.getProperty("defaultRedirectUrl");	
			properties.load(stream);
		}
		catch (Exception e) {
			Logger log = Logger.getLogger(ClickTrackerProperties.class.getName());
			log.warning("Cannot read properties file "+filename+". Defaulting to failsafe url: "+failsafeUrl+". "+e.getMessage());
			defaultTrackingLink = failsafeUrl;
		}
	}
	
	public String getDefaultTrackingLink() {
		return defaultTrackingLink;
	}
}
