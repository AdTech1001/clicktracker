package com.outfit7.mb.clicktracker.models;

public class CampaignApiDO {
	private String name;
	private Platform[] enabledPlatforms;
	private String trackingLink;
	
	public CampaignApiDO() { }
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Platform[] getEnabledPlatforms() {
		return enabledPlatforms;
	}
	public void setEnabledPlatforms(Platform[] enabledPlatforms) {
		this.enabledPlatforms = enabledPlatforms;
	}
	public String getTrackingLink() {
		return trackingLink;
	}
	public void setTrackingLink(String trackingLink) {
		this.trackingLink = trackingLink;
	}
}
