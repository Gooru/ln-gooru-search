package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.model.UserDataProviderCriteria;
import org.ednovo.gooru.search.model.UserPreferenceData;


public interface UserPreferenceDataProviderService {
	
	public UserPreferenceData getUserPreferenceData(UserDataProviderCriteria userDataProviderCriteria);
}
