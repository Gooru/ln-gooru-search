package org.ednovo.gooru.search.es.repository;

import java.util.Map;

public interface TenantRepository {

	Map<String, Object> getAllDiscoverableTenants();

}
