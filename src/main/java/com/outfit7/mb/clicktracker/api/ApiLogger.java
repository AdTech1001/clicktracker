package com.outfit7.mb.clicktracker.api;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

public class ApiLogger {
	/**
	 * Logs unhandled exceptions to a log file
	 * @param request Servlet request object
	 * @param message Error message
	 */
	public static void LogUnhandledException(HttpServletRequest request, String message) {
		Logger log = Logger.getLogger(ClickTrackerAdminApi.class.getName());
		log.warning(String.format("Unhandled exception from IP: %s. %s", request.getRemoteAddr(), message));
	}
}
