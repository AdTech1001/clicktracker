package com.outfit7.mb.clicktracker.models;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
/**
 * Stores information about individual clicks for analytical purposes.
 */
public class Click {
	@Id public Long id;
	@Index public Platform userPlatform;
	public Date dateCreated;
	@Parent private Ref<Campaign> campaign;
	
	public Click(Campaign campaign, Platform userPlatform) {
		this.userPlatform = userPlatform;
		this.dateCreated = new Date();
		this.campaign = Ref.create(campaign);
	}
}
