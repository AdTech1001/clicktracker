package com.outfit7.mb.clicktracker.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.outfit7.mb.clicktracker.models.*;

public class ClickDAO {
	boolean commitSynchronously;
	
	/**
	 * Initializes ClickDAO
	 */
	public ClickDAO() {
		this(false);
	}
	
	/**
	 * Initializes Campaign DAO.
	 * @param commitSynchronously If set to True runs save and delete operation using now(). Otherwise not.
	 */
	public ClickDAO(boolean commitSynchronously) {
		this.commitSynchronously = commitSynchronously;
	}
	
	public int getNumberOfClicks(Campaign campaign, Platform userPlatform) {
		return ObjectifyService.ofy().load().
				type(Click.class).
				ancestor(campaign).
				filter("userPlatform",userPlatform).
				count();
	}
	
	public int getNumberOfClicks(Platform userPlatform) {
		return ObjectifyService.ofy().load().type(Click.class).filter("userPlatform",userPlatform).count();
	}
	
	public void saveClick(Campaign campaign, Platform userPlatform) {
		Click click = new Click(campaign, userPlatform);
		Result<Key<Click>> result = ObjectifyService.ofy().save().entity(click);
		if (commitSynchronously)
			result.now();
	}
	
	public void deleteCampaignClicks(Campaign campaign) {
		Result<Void> result = ObjectifyService.ofy().delete().keys(
				ObjectifyService.ofy().load().type(Click.class).ancestor(campaign).keys().list()
				);
		if (commitSynchronously)
			result.now();
	}
}
