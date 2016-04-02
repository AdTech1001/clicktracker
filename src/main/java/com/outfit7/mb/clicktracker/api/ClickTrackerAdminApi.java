package com.outfit7.mb.clicktracker.api;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.outfit7.mb.clicktracker.logic.CampaignManager;
import com.outfit7.mb.clicktracker.logic.CampaignManagerFactory;
import com.outfit7.mb.clicktracker.models.Campaign;
import com.outfit7.mb.clicktracker.models.CampaignApiDO;
import com.outfit7.mb.clicktracker.models.Platform;

@Api(
		name = "admin",
		description = "API used by admin users to manage campaigns and check current statistics. HTTP Basic authentication is required to invoke API calls.",
		version="v1")
public class ClickTrackerAdminApi {
	private CampaignManager campaignManager;

	/**
	 * Initialize a production ready CampaignManager object instance
	 */
	public ClickTrackerAdminApi() {
		this.campaignManager = CampaignManagerFactory.GetProductionCampaignManager();
	}
	
	/**
	 * Provide a custom campaignManager object instance
	 */
	public ClickTrackerAdminApi(CampaignManager campaignManager) {
		this.campaignManager = campaignManager;
	}
	
	@ApiMethod(path = "campaign", name = "campaign.new", httpMethod = HttpMethod.POST)
	public Properties newCampaign(
			HttpServletRequest request,
			@Named("apiKey") String apiKey,
			CampaignApiDO campaign) throws UnauthorizedException, BadRequestException, InternalServerErrorException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);
		
		try {
			Campaign newCampaign = campaignManager.newCampaign(campaign.getName(), 
					campaign.getTrackingLink(), 
					campaign.getEnabledPlatforms());
			return SingleRestResult.Get("id", newCampaign.id.toString());
		}
		catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage());
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
	
	@ApiMethod(path = "campaign/{id}", name = "campaign.update", httpMethod = HttpMethod.PUT)
	public void updateCampaing(
			HttpServletRequest request,
			@Named("apiKey") String apiKey,
			@Named("id") long id,
			CampaignApiDO campaign
			) throws UnauthorizedException, BadRequestException, NotFoundException, InternalServerErrorException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);
		
		try {
			String name, trackingLink;
			Platform[] enabledPlatforms;
			
			Campaign currentCampaign = campaignManager.getCampaign(id);
			
			// Only change properties which have been passed. Leave others as is.
			name = (campaign.getName() != null && !campaign.getName().isEmpty() ? 
					campaign.getName() : currentCampaign.name);
			trackingLink = (campaign.getTrackingLink() != null && !campaign.getTrackingLink().isEmpty() ? 
					campaign.getTrackingLink() : currentCampaign.trackingLink);
			enabledPlatforms = (campaign.getEnabledPlatforms() != null && campaign.getEnabledPlatforms().length > 0 ? 
					campaign.getEnabledPlatforms() : currentCampaign.enabledPlatforms);

			campaignManager.updateCampaign(id, name, trackingLink, enabledPlatforms);
		}
		catch (NoSuchElementException e) {
			throw new NotFoundException(e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage()); 
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
	
	@ApiMethod(path = "campaign/{id}", name = "campaign.delete", httpMethod = HttpMethod.DELETE)
	public void deleteCampaign(
			HttpServletRequest request, 
			@Named("apiKey") String apiKey, 
			@Named("id") long id) throws NotFoundException, UnauthorizedException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);
		
		campaignManager.deleteCampaignAndClicks(id);
	}
	
	@ApiMethod(path = "campaign/{id}", name = "campaign.get", httpMethod = HttpMethod.GET)	
	public Campaign getCampaign(
			HttpServletRequest request, 
			@Named("apiKey") String apiKey, 
			@Named("id") long id) throws UnauthorizedException, NotFoundException, InternalServerErrorException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);

		try {
			return campaignManager.getCampaign(id);
		}
		catch (NoSuchElementException e) {
			throw new NotFoundException(e.getMessage());
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
	
	@ApiMethod(path = "campaign", name = "campaign.list", httpMethod = HttpMethod.GET)
	public List<Campaign> listCampaigns(
			HttpServletRequest request,
			@Named("apiKey") String apiKey,
			@Named("platform") Platform platform) throws UnauthorizedException, BadRequestException, InternalServerErrorException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);
		
		try {
			if (platform != null)
				return campaignManager.listCampaigns(platform);
			else
				//return campaignManager.listCampaigns();
				throw new BadRequestException("Please provide platform.");
		}
		catch (BadRequestException e) {
			throw e;
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
	
	@ApiMethod(path = "clicks/campaign/{id}", name = "campaignPlatformClicks.get", httpMethod = HttpMethod.GET)
	public Properties getNumberOfClicksForCampaignOnPlatform(
			HttpServletRequest request,
			@Named("apiKey") String apiKey,
			@Named("id") long id,
			@Named("platform") Platform platform
			) throws UnauthorizedException, NotFoundException, InternalServerErrorException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);
		
		try {
			return SingleRestResult.Get("clicks", campaignManager.getNumberOfClicks(campaignManager.getCampaign(id), platform));
		}
		catch (NoSuchElementException e) {
			throw new NotFoundException(e.getMessage());
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
	
	@ApiMethod(path = "clicks/{platform}", name = "platformClicks.get", httpMethod = HttpMethod.GET)
	public Properties getNumberOfClicksForPlatform(
			HttpServletRequest request,
			@Named("apiKey") String apiKey,
			@Named("platform") Platform platform
			) throws UnauthorizedException, InternalServerErrorException {
		ApiAuthenticator.HTTPAuthenticateKey(request, apiKey);
		
		try {
			return SingleRestResult.Get("clicks", campaignManager.getNumberOfClicks(platform));
		}
		catch (Exception e) {
			ApiLogger.LogUnhandledException(request, e.getMessage());
			throw new InternalServerErrorException("Internal server error.");
		}
	}
}
