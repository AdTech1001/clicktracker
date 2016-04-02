package com.outfit7.mb.clicktracker.dao;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.outfit7.mb.clicktracker.models.Campaign;
import com.outfit7.mb.clicktracker.models.Platform;

public class CampaignDAO {
	boolean deleteSynchronously;
	
	/**
	 * Initializes Campaign DAO.
	 */
	public CampaignDAO() {
		this(false);
	}
	
	/**
	 * Initializes Campaign DAO.
	 * @param deleteSynchronously If set to True runs delete operation using now(). Otherwise not.
	 */
	public CampaignDAO(boolean deleteSynchronously) {
		this.deleteSynchronously = deleteSynchronously;
	}
	
	public void saveCampaign(Campaign campaign) {
		ObjectifyService.ofy().save().entity(campaign).now();
	}
	
	public Campaign getCampaign(long id) {
		return ObjectifyService.ofy().load().type(Campaign.class).id(id).now();
	}
	
	public List<Campaign> listCampaigns(Platform platform) {
		return ObjectifyService.ofy().load().type(Campaign.class).filter("enabledPlatforms", platform).list();
	}
	
	public List<Campaign> listCampaigns() {
		return ObjectifyService.ofy().load().type(Campaign.class).list();
	}

	public void deleteCampaign(long campaignId) {
		deleteCampaign(getCampaign(campaignId));
	}

	public void deleteCampaign(Campaign campaign) {
		Result<Void> result = ObjectifyService.ofy().delete().entity(campaign);
		if (deleteSynchronously)
			result.now();
	}
}
