package org.ednovo.gooru.search.es.service;

import org.ednovo.gooru.search.model.UserDataProviderCriteria;
import org.ednovo.gooru.search.model.UserProficiencyData;


public interface UserProficiencyDataProviderService {
	
	public UserProficiencyData getUserProficiencyData(UserDataProviderCriteria userDataProviderCriteria);
}
