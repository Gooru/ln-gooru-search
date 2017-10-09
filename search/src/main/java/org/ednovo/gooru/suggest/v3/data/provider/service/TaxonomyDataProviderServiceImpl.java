package org.ednovo.gooru.suggest.v3.data.provider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.handler.SearchHandler;
import org.ednovo.gooru.search.es.handler.SearchHandlerType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.repository.TaxonomyRepository;
import org.ednovo.gooru.suggest.v3.data.provider.model.TaxonomyDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.TaxonomyContextData;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxonomyDataProviderServiceImpl implements TaxonomyDataProviderService {

	protected static final Logger LOG = LoggerFactory.getLogger(TaxonomyDataProviderServiceImpl.class);

	@Autowired
	private TaxonomyRepository taxonomyRepository;
	
	@Override
	public TaxonomyContextData getTaxonomyData(TaxonomyDataProviderCriteria taxonomyDataProviderCriteria) throws JSONException {
		TaxonomyContextData taxonomyContextDo = new TaxonomyContextData();
		if (taxonomyDataProviderCriteria.getCodeIds() != null) {
			List<String> standardInternalCodes = new ArrayList<>();
			List<String> ltInternalCodes = new ArrayList<>();
			List<String> parentStandardInternalCodes = new ArrayList<>();
			List<String> slInternalCodes = new ArrayList<>();
			Set<String> gutStdCodes = new HashSet<>();
			Set<String> gutLtCodes = new HashSet<>();
			Boolean isInternalCode = taxonomyDataProviderCriteria.getIsInternalCode();
			taxonomyDataProviderCriteria.getCodeIds().forEach(code -> {
				Map<String, String> taxonomyAsMap = new HashMap<>();
				if (isInternalCode) {
					taxonomyAsMap = taxonomyRepository.getTaxonomyCodeByInternalCode(code);
				} else {
					taxonomyAsMap = taxonomyRepository.getTaxonomyCodeByDisplayCode(code);
				}
				if (taxonomyAsMap != null && !taxonomyAsMap.isEmpty()) {
					String codeType = taxonomyAsMap.get("code_type");
					if (codeType.equalsIgnoreCase(Constants.LEARNING_TARGET_LEVEL_0)) {
						ltInternalCodes.add(taxonomyAsMap.get(IndexFields.ID));
						slInternalCodes.add(taxonomyAsMap.get(IndexFields.ID).toLowerCase());
					} else if (Constants.STANDARD_MATCH.matcher(codeType).matches()) {
						standardInternalCodes.add(taxonomyAsMap.get(IndexFields.ID));
						slInternalCodes.add(taxonomyAsMap.get(IndexFields.ID).toLowerCase());
						parentStandardInternalCodes.add(taxonomyAsMap.get("parent_taxonomy_code_id"));
					}
				}
			});
			addGutCodes(standardInternalCodes, ltInternalCodes, parentStandardInternalCodes, gutStdCodes, gutLtCodes);
			taxonomyContextDo.setSlInternalCodes(slInternalCodes);
			taxonomyContextDo.setGutLtCodes(gutLtCodes.stream().distinct().collect(Collectors.toList()));
			taxonomyContextDo.setGutStdCodes(gutStdCodes.stream().distinct().collect(Collectors.toList()));
		}
		return taxonomyContextDo;
	}

	private void addGutCodes(List<String> standardInternalCodes, List<String> ltInternalCodes, List<String> parentStandardInternalCodes, Set<String> gutStdCodes, Set<String> gutLtCodes) {
		fetchGutCodesForInternalCodes(ltInternalCodes, gutLtCodes);
		fetchGutCodesForInternalCodes(standardInternalCodes, gutStdCodes);
		Set<String> gutLtParentCodes = new HashSet<>(parentStandardInternalCodes.size());
		fetchGutCodesForInternalCodes(parentStandardInternalCodes, gutLtParentCodes);
		if (!gutLtParentCodes.isEmpty()) gutStdCodes.addAll(gutLtParentCodes);
	}

	@SuppressWarnings("unchecked")
	private void fetchGutCodesForInternalCodes(List<String> codes, Set<String> gutCodes) {
		SearchData crosswalkRequest = new SearchData();
		crosswalkRequest.setIndexType(EsIndex.CROSSWALK);
		crosswalkRequest.putFilter(Constants.AMPERSAND + Constants.CARET_SYMBOL + IndexFields.CROSSWALK_CODES + Constants.DOT + IndexFields.ID, (StringUtils.join(codes, Constants.COMMA)));
		crosswalkRequest.setQueryString(Constants.STAR);
		List<Map<String, Object>> searchResponse = (List<Map<String, Object>>) SearchHandler.getSearcher(SearchHandlerType.CROSSWALK.name()).search(crosswalkRequest).getSearchResults();
		if (searchResponse != null) {
			searchResponse.forEach(map -> {
				Map<String, Object> source = (Map<String, Object>) map.get(Constants.SEARCH_SOURCE);
				gutCodes.add((String) source.get(IndexFields.ID));
			});
		}
	}

}
