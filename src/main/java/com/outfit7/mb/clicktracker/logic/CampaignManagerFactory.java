package com.outfit7.mb.clicktracker.logic;

import com.outfit7.mb.clicktracker.ClickTrackerProperties;
import com.outfit7.mb.clicktracker.dao.CampaignDAO;
import com.outfit7.mb.clicktracker.dao.ClickDAO;

public class CampaignManagerFactory {
	static private String defaultTrackingLink = "http://test.outfit7.com"; 
	
	/**
	 * Initializes a production instance of campaign manager with caching and most writes
	 * running asynchronously.
	 * @return Production ready CampaignManagerManager object.
	 */
	static public CampaignManager GetProductionCampaignManager() {
		CampaignDAO campaignDAO = new CampaignDAO();
		ClickDAO clickDAO = new ClickDAO();
		ClickTrackerProperties properties = new ClickTrackerProperties();
		
		return new CampaignManager(properties.defaultTrackingLink, campaignDAO, clickDAO);
	}

	/**
	 * Initializes an instance of campaign manager for unit tests. Caching is off and most writes
	 * running synchronously.
	 * @return Unit test ready CampaignManagerManager object.
	 */
	static public CampaignManager GetTestCampaignManager() {
		CampaignDAO campaignDAO = new CampaignDAO(true);
		ClickDAO clickDAO = new ClickDAO(true);
		
		// Do not read .properties file for unit tests
		return new CampaignManager(defaultTrackingLink, campaignDAO, clickDAO);
	}
	
	static public String getDefaultTrackingLink() {
		return defaultTrackingLink;
	}
}
