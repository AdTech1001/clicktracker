package com.outfit7.mb.clicktracker.models;

import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Campaign {
	@Id public Long id;
	public String name;
	public String trackingLink;
	public Date creationDate;
	public Date updateDate;
	@Index public Platform[] enabledPlatforms;

	public Campaign() { }
	
	/**
	 * Initiates an object with info of an existing campaign.
	 * @param name Campaign name
	 * @param creationDate Creation date
	 * @param updateDate Update date
	 * @param trackingLinks List of all tracking links
	 */
	public Campaign(String name, String trackingLink, Date creationDate, Date updateDate,
			Platform[] enabledPlatforms) {
		this.name = name;
		this.trackingLink = trackingLink;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
		this.enabledPlatforms = enabledPlatforms;
	}
	
	/**
	 * Checks if platform is enabled for a given campaign.
	 * @param platform
	 * @return True if platform is enabled on the campaign.
	 */
	public boolean isPlatformEnabled(Platform platform) {
		for (Platform p : enabledPlatforms) {
			if (p == platform)
				return true;
		}
		
		return false;
	}
}
