package com.outfit7.mb.clicktracker.logic;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.validator.routines.UrlValidator;

import com.outfit7.mb.clicktracker.models.Platform;;

public class CampaignValidator {
	public static final int minPlatforms= 1;
	public static final int maxPlatforms = Platform.values().length;
	
	/**
	 * Validates size of tracking links.
	 * @param campaign
	 * @param newTrackingLink Tracking link to be validated against current collection.
	 * @throws IllegalArgumentException if validation fails.
	 */
	public static void validateCampaignEntry(String campaignName, String trackingLink, Platform[] enabledPlatforms) throws IllegalArgumentException {
		if (campaignName == null || campaignName.isEmpty())
			throw new IllegalArgumentException("No campaign name provided.");
		
		if (!isUrlValid(trackingLink))
			throw new IllegalArgumentException("Invalid campaign URL.");
		
		if (enabledPlatforms == null || enabledPlatforms.length < minPlatforms)
			throw new IllegalArgumentException("At least "+minPlatforms+" tracking link(s) "+
					"must be specified when creating a campaign.");
		
		if (enabledPlatforms == null || enabledPlatforms.length > maxPlatforms)
			throw new IllegalArgumentException(
					"Too many platforms specified. "+
					"No more than "+maxPlatforms+" platforms can be specified. ");
		
		validatePlatforms(enabledPlatforms);
	}
	
	private static void validatePlatforms(Platform[] enabledPlatforms) {
		List<Platform> usedPlatforms = new ArrayList<>();
		for (Platform p : enabledPlatforms) {
			if (p == null)
				throw new IllegalArgumentException("Platform cannot be null.");
			
			if (usedPlatforms.indexOf(p) >= 0)
				throw new IllegalArgumentException("Duplicate platform types not allowed for campaign.");
			else
				usedPlatforms.add(p);
		}
	}
	
	private static boolean isUrlValid(String url) {
		String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		return urlValidator.isValid(url);
	}
}
