package org.ednovo.gooru.search.es.repository;

import java.util.List;
import java.util.Map;

public interface TenantRepository {

	Map<String, Object> getAllDiscoverableTenants();

	List<String> getDiscoverableTenantIds();

	List<String> getGlobalTenantIds();

}
