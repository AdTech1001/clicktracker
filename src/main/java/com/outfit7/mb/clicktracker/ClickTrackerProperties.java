package com.outfit7.mb.clicktracker;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ClickTrackerProperties {
	public String defaultTrackingLink;
	private static final String failsafeUrl = "http://outfit7.com";
	private static final Logger log = Logger.getLogger(ClickTrackerProperties.class.getName());
	
	public ClickTrackerProperties() {
		String filename = "/clicktracker.properties";
		InputStream stream = null;
		stream =  this.getClass().getClassLoader().getResourceAsStream(filename);
		Properties properties = new Properties();
		
		try {
			properties.load(stream);
			defaultTrackingLink = properties.getProperty(defaultTrackingLink);
		}
		catch (Exception e) {
			log.warning("Cannot read properties file "+filename+". Defaulting to failsafe url: "+failsafeUrl+".");
			defaultTrackingLink = failsafeUrl;
		}
	}
}
