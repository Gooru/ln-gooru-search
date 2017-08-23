package org.ednovo.gooru.suggest.v3.data.provider.service;

import org.ednovo.gooru.suggest.v3.data.provider.model.TaxonomyDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.TaxonomyContextData;
import org.json.JSONException;

public interface TaxonomyDataProviderService {

	public TaxonomyContextData getTaxonomyData(TaxonomyDataProviderCriteria taxonomyDataProviderCriteria) throws JSONException;

}
