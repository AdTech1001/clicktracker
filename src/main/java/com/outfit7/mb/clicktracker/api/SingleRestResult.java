package com.outfit7.mb.clicktracker.api;

import java.util.Properties;

public class SingleRestResult {
	/**
	 * @return Properties object with single value. Useful for single return REST JSON values.
	 */
	public static Properties Get(String key, Object value) {
		Properties props = new Properties();
		//props.setProperty(key, value);
		props.put(key, value);
		return props;
	}
}
