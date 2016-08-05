package org.ednovo.gooru.search.es.service;

import java.util.Map;

import org.ednovo.gooru.search.model.ClasspageDataProviderCriteria;
import org.ednovo.gooru.search.model.ResourceUsageData;

public interface ClasspagePerformanceDataProviderService {
	
	public Map<String, ResourceUsageData> getUserResourcePerformanceDataMap(ClasspageDataProviderCriteria classpageDataProviderCriteria);

}
