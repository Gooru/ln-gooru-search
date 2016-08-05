package org.ednovo.gooru.search.es.service;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.model.SuggestData;
import org.ednovo.gooru.search.model.ActivityStreamRawData;
import org.ednovo.gooru.search.model.UserActivityDataProviderCriteria;

public interface ActivityStreamDataProviderService{

	List<Map<String, Object>> getUserActivityStream(String userUid, String eventName, Integer minutesToRead, Integer eventsToRead);
	List<ActivityStreamRawData> getActivityStreamData(UserActivityDataProviderCriteria userActivityDataProviderCriteria,SuggestData suggestData);
}
