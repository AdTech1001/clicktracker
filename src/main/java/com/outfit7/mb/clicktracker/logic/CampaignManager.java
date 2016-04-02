package com.outfit7.mb.clicktracker.logic;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import com.google.api.server.spi.response.NotFoundException;
import com.outfit7.mb.clicktracker.dao.CampaignDAO;
import com.outfit7.mb.clicktracker.dao.ClickDAO;
import com.outfit7.mb.clicktracker.models.Campaign;
import com.outfit7.mb.clicktracker.models.Platform;

/**
 * Business logic for a given campaign.
 */
public class CampaignManager {
	private String defaultTrackingLink;
	private CampaignDAO campaignDAO;
	private ClickDAO clickDAO;
	
	/**
	 * Initializes the campaign object.
	 * @param defaultTrackingLink
	 * @param campaignDAO
	 * @param clickDAO
	 */
	public CampaignManager(String defaultTrackingLink, CampaignDAO campaignDAO, ClickDAO clickDAO) {
		this.campaignDAO = campaignDAO;
		this.clickDAO = clickDAO;
		this.defaultTrackingLink = defaultTrackingLink;
	}
	
	/**
	 * Creates new campaign.
	 * @param campaignName	Campaign name.
	 * @param trackingLink	Tracking link.
	 * @param enabledPlatforms	End user platforms for which the campaign is enabled.
	 * @return Campaign object with id.
	 */
	public Campaign newCampaign(String campaignName, String trackingLink, Platform[] enabledPlatforms) {
		CampaignValidator.validateCampaignEntry(campaignName, trackingLink, enabledPlatforms);
		Campaign campaign = new Campaign(campaignName, trackingLink, new Date(), new Date(), enabledPlatforms); 
		campaignDAO.saveCampaign(campaign);
		return campaign;
	}
	
	/**
	 * Retrieves campaign by id.
	 * @param id Campaign id.
	 * @return Campaign object.
	 * @throws NoSuchElementException if campaign is not found
	 */
	public Campaign getCampaign(long id) throws NoSuchElementException {
		Campaign campaign = campaignDAO.getCampaign(id);
		if (campaign == null)
			throw new NoSuchElementException("Campaign not found.");
		return campaign;
	}

	/**
	 * Updates existing campaign.
	 * @param campaignId Campaign id.
	 * @param campaignName Campaign name.
	 * @param trackingLink Campaign tracking link.
	 * @param enabledPlatforms	End user platforms for which the campaign is enabled.
	 */
	public void updateCampaign(long campaignId, String campaignName, String trackingLink, 
			Platform[] enabledPlatforms) throws NoSuchElementException, IllegalArgumentException {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		if (campaign == null)
			throw new NoSuchElementException("Campaign not found");
		updateCampaign(campaign, campaignName, trackingLink, enabledPlatforms);
	}
	
	/**
	 * Updates existing campaign.
	 * @param campaign Campaign object to update.
	 * @param campaignName Campaign name.
	 * @param trackingLink Campaign tracking link.
	 * @param enabledPlatforms End user platforms for which the campaign is enabled.
	 */
	public void updateCampaign(Campaign campaign, String campaignName, String trackingLink, 
			Platform[] enabledPlatforms) throws IllegalArgumentException {
		CampaignValidator.validateCampaignEntry(campaignName, trackingLink, enabledPlatforms);
		campaign.name = campaignName;
		campaign.trackingLink = trackingLink;
		campaign.enabledPlatforms = enabledPlatforms;
		campaign.updateDate = new Date();
		campaignDAO.saveCampaign(campaign);
	}
	
	/**
	 * Removes campaign and associated clicks.
	 * @param campaignId
	 * @throws NotFoundException If campaign not found.
	 */
	public void deleteCampaignAndClicks(long campaignId) throws NotFoundException {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		if (campaign == null)
			throw new NotFoundException("Campaign not found.");
		clickDAO.deleteCampaignClicks(campaign);
		campaignDAO.deleteCampaign(campaign);
	}
	
	/**
	 * Stores each click for analytical purposed. 
	 * @param campaignId Id of the campaign.
	 * @param userPlatform End user platform.
	 * @return Campaign tracking link.
	 */
	public String sendClick(long campaignId, Platform userPlatform) {
		Campaign campaign = campaignDAO.getCampaign(campaignId);
		if (campaign != null && campaign.isPlatformEnabled(userPlatform)) {
			clickDAO.saveClick(campaign, userPlatform);
			if (campaign.isPlatformEnabled(userPlatform))
				return campaign.trackingLink;
		}
			
		// Return default URL if campaign not found, or platform not enabled
		return defaultTrackingLink;
	}
	
	/**
	 * List campaigns for platform.
	 * @param platform End User platform.
	 * @return List of campaigns.
	 */
	public List<Campaign> listCampaigns(Platform platform) {
		return campaignDAO.listCampaigns(platform);
	}
	
	/**
	 * List all active campaigns.
	 * @return All active campaigns.
	 */
	public List<Campaign> listCampaigns() {
		return campaignDAO.listCampaigns();
	}
	
	/**
	 * @param campaign Campaign.
	 * @param platform End user platform.
	 * @return Number number of clicks for given campaign on the given platform.
	 */
	public int getNumberOfClicks(Campaign campaign, Platform platform) {
		return clickDAO.getNumberOfClicks(campaign, platform);
	}

	/**
	 * @param platform End user platform.
	 * @return Number of clicks on the given platform.
	 */
	public int getNumberOfClicks(Platform platform) {
		return clickDAO.getNumberOfClicks(platform);
	}
	
	/**
	 * @return Redirect URL which is sent to user if campaign or platform is not found.
	 */
	public String getDefaultTrackingLink() {
		return defaultTrackingLink;
	}
}
