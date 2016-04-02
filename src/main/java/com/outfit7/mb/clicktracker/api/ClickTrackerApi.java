package com.outfit7.mb.clicktracker.api;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.outfit7.mb.clicktracker.logic.CampaignManager;
import com.outfit7.mb.clicktracker.logic.CampaignManagerFactory;
import com.outfit7.mb.clicktracker.models.Platform;

@Api(
		name = "main",
		description = "Main API through which mobile clients interact with the backend application.",
		version="v1")
public class ClickTrackerApi {
	private CampaignManager campaignManager;

	/**
	 * Initialize a production ready CampaignManager object instance
	 */
	public ClickTrackerApi() {
		this.campaignManager = CampaignManagerFactory.GetProductionCampaignManager();
	}
	
	/**
	 * Provide a custom campaignManager object instance
	 */
	public ClickTrackerApi(CampaignManager campaignManager) {
		this.campaignManager = campaignManager;
	}
	
	/**
	 * 
	 * @param request Low level header information
	 * @param campaignId Campaign id
	 * @param platform End user device platform
	 * @return
	 * @throws UnauthorizedException Invalid API key provided
	 * @throws InternalServerErrorException Internal server error because caused by an unhandled exception
	 */
	@ApiMethod(path = "campaign/{campaignId}", name = "click.post", httpMethod = HttpMethod.POST)
	public Properties sendClick(HttpServletRequest request, 
			@Named("campaignId") long campaignId, 
			@Named("platform") Platform platform) throws InternalServerErrorException {
		
		try {
			return SingleRestResult.Get("href", campaignManager.sendClick(campaignId, platform));
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
}

