package com.outfit7.mb.clicktracker.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class ApiLogger {
	public static void LogUnhandledException(HttpServletRequest request, String message) {
		Logger log = Logger.getLogger(ClickTrackerAdminApi.class.getName());
		log.warning(String.format("Unhandled exception from IP: %s. %s", request.getRemoteAddr(), message));
	}
}
