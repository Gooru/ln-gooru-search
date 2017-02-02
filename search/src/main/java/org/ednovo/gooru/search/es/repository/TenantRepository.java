package org.ednovo.gooru.search.es.repository;

import java.util.Map;

import org.json.JSONException;

public interface TenantRepository {

	Map<String, Object> getAllDiscoverableTenants();

}
