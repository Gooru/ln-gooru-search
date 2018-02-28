/**
 * 
 */
package org.ednovo.gooru.search.es.processor.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.filter.BoolQuery;
import org.ednovo.gooru.search.es.filter.FilterQuery;
import org.ednovo.gooru.search.es.filter.MissingFilter;
import org.ednovo.gooru.search.es.filter.MustNotQuery;
import org.ednovo.gooru.search.es.filter.NestedFilter;
import org.ednovo.gooru.search.es.filter.NotFilter;
import org.ednovo.gooru.search.es.filter.OrFilter;
import org.ednovo.gooru.search.es.filter.QueryString;
import org.ednovo.gooru.search.es.filter.RangeFilter;
import org.ednovo.gooru.search.es.filter.ShouldQuery;
import org.ednovo.gooru.search.es.filter.TermFilter;
import org.ednovo.gooru.search.es.filter.TermsFilter;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author SearchTeam
 * 
 */
public class FilterBuilderUtils {

	protected static final Logger LOG = LoggerFactory.getLogger(FilterBuilderUtils.class);

	private static final String IS_REVIEWED = "isreviewed";

	private static final String GTE_USEDINSCOLLECTIONCOUNT = "gte-usedinscollectioncount";
	
	private static final String FLT_COURSE_MISSING = "courseMissing";
 

	public static Object buildFilters(Map<String, Object> filterMap) {
		List<Object> filterList = new ArrayList<Object>();
		Iterator<Map.Entry<String, Object>> iterator = filterMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> filterEntry = iterator.next();
			Object value = filterEntry.getValue();
			String key = filterEntry.getKey();
			constructFilters(filterList, key, value);
		}
		FilterQuery filterQuery = new FilterQuery(filterList);
		BoolQuery boolQuery = new BoolQuery();
		boolQuery.setBool(filterQuery);
		return boolQuery;
	}

	public static List<Object> buildFilter(Map<String, Object> filterMap) {
		List<Object> filterList = new ArrayList<Object>(1);

		Iterator<Map.Entry<String, Object>> iterator = filterMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> filterEntry = iterator.next();
			Object value = filterEntry.getValue();
			String key = filterEntry.getKey();
			constructFilter(filterList, key, value);
		}
		return filterList;

	}

	private static void constructFilters(List<Object> filterList, String key, Object value) {

		String[] keys = key.split("\\^");

		String type = null;
		if (keys.length > 1) {
			key = keys[1];
			type = keys[0];
		}

		if (value.getClass().isArray() && ((String[]) value).length == 1) {
			value = ((String[]) value)[0];
		}
		if (value instanceof String && SearchSettingService.isLowerCaseFilter(key)) {
			value = ((String) value).toLowerCase();
		}
		if (value instanceof String && SearchSettingService.isSplitByApproxFilter(key)) {
			value = ((String) value).split("~~");
		}

		// To support bluesky search filters
		if (value instanceof String && SearchSettingService.isSplitBySingleTiltaForSearch(key)) {
			value = ((String) value).split("~");
		}

		Object filter = null;
		key = SearchSettingService.getFilterAlias(key);
		String[] filters = null;
		String[] orFilterTerms = null;
		if (value instanceof BoolQuery) {
			filterList.add(value);
		} else if (value instanceof String && (orFilterTerms = ((String) value).split("\\&\\|")).length > 1) {
			List<Object> orFilter = new ArrayList<Object>();
			for (String orFilterTerm : orFilterTerms) {
				String[] filterElements = ((String) orFilterTerm).split("\\~");
				if (orFilterTerm instanceof String && filterElements.length > 1) {
					orFilter.add(buildFilter(filterElements.length > 1 ? filterElements[1] : "", filterElements[0]));
				}
			}
			if (orFilter.size() > 0) {
				ShouldQuery shouldQuery = new ShouldQuery(orFilter);
				BoolQuery boolQuery = new BoolQuery();
				boolQuery.setBool(shouldQuery);
				filter = boolQuery;
			}
		} else if ((filters = ((String) key).split("\\|")).length > 1) {
			List<Object> orFilter = new ArrayList<Object>();
			for (String orFilterKey : filters) {
				if (StringUtils.trimToNull(orFilterKey) != null) {
					orFilter.add(buildFilter(value, orFilterKey));
				}
			}
			if (orFilter.size() > 0) {
				ShouldQuery shouldQuery = new ShouldQuery(orFilter);
				BoolQuery boolQuery = new BoolQuery();
				boolQuery.setBool(shouldQuery);
				filter = boolQuery;
			}
		} else if (type != null && type.equals("|")) {
			List<Object> orFilter = new ArrayList<Object>();
			filters = ((String) key).split("\\|");
			for (String orFilterKey : filters) {
				orFilter.add(buildFilter(((String) value).toLowerCase(), orFilterKey));
			}
			if (orFilter.size() > 0) {
				ShouldQuery shouldQuery = new ShouldQuery(orFilter);
				BoolQuery boolQuery = new BoolQuery();
				boolQuery.setBool(shouldQuery);
				filter = boolQuery;
			}
		} else if ((filters = ((String) key).split("\\&")).length > 1) {
			for (String andFilterTerm : filters) {
				if (StringUtils.trimToNull(andFilterTerm) != null) {
					filter = buildFilter(value, andFilterTerm);
				}
			}
		} else if (value instanceof String && (filters = ((String) value).split("\\|")).length > 1) {
			List<Object> orFilter = new ArrayList<Object>();
			for (String orFilterTerm : filters) {
				String[] filterElements = orFilterTerm.split("\\:");
				orFilter.add(buildFilter(filterElements.length > 1 ? filterElements[1] : "", filterElements[0]));
			}
			if (orFilter.size() > 0) {
				ShouldQuery shouldQuery = new ShouldQuery(orFilter);
				BoolQuery boolQuery = new BoolQuery();
				boolQuery.setBool(shouldQuery);
				filter = boolQuery;
			}
		} else if (key.equalsIgnoreCase(IS_REVIEWED) || key.equalsIgnoreCase(GTE_USEDINSCOLLECTIONCOUNT)) {
			if (key.equalsIgnoreCase(GTE_USEDINSCOLLECTIONCOUNT)) {
				key = "statistics.usedInCollectionCount";
			}
			filter = new RangeFilter(value.toString(), key);

		} else if(type != null && type.equalsIgnoreCase("#")){
			if(key.equalsIgnoreCase(FLT_COURSE_MISSING)){
				key = IndexFields.COURSE;
			}
			filter = new MissingFilter(key);
		} else if(type != null && type.equalsIgnoreCase("&?")){
			filter = value;
		} else if (type != null && type.equalsIgnoreCase("<>-")) {
			return;
		} else {
			filter = buildFilter(value, key);
		}

		if (filter != null) {
			if (type != null && type.equals("!")) {
				List<Object> notQuery = new ArrayList<Object>();
				notQuery.add(filter);
				MustNotQuery mustNotQuery = new MustNotQuery(notQuery);
				BoolQuery boolQuery = new BoolQuery();
				boolQuery.setBool(mustNotQuery);
				filter = boolQuery;
			}
			filterList.add(filter);
		}
	}
	
	public static Object buildFilter(Object values, String key) {

		String[] keys = key.split("\\^");

		String type = null;
		if (keys.length > 1) {
			key = keys[1];
			type = keys[0];
		}

		if (type != null && type.equals("#")) {
			return new MissingFilter(key);
		}

		if (values instanceof String) {
			String[] valueParts = ((String) values).split("<>");
			if (valueParts.length == 2) {
				return new RangeFilter(key, valueParts[0], valueParts[1]);
			}

		}

		if (values instanceof String) {
			values = ((String) values).split("\\,");
		}
		if (values.getClass().isArray() && ((String[]) values).length == 1) {
			values = ((String[]) values)[0];
		}

		keys = key.split("\\@");
		if (keys.length > 1) {
			key = keys[1];
		}

		Object filter = null;

		if (values.getClass().isArray()) {
			filter = new TermsFilter(key, values);
		} else {
			filter = new TermFilter(key, values);
		}
		if (keys.length > 1) {
			filter = new NestedFilter(keys[0], filter);
		}

		if (type != null && type.equals("!")) {
			List<Object> mustNotFilter = new ArrayList<Object>(1);
			mustNotFilter.add(filter);
			MustNotQuery mustNotQuery = new MustNotQuery(mustNotFilter);
			BoolQuery boolQuery = new BoolQuery();
			boolQuery.setBool(mustNotQuery);
			filter = boolQuery;
		}

		return filter;
	}

	private static void constructFilter(List<Object> filerList, String key, Object value) {

		String[] keys = key.split("\\^");

		String type = null;
		if (keys.length > 1) {
			key = keys[1];
			type = keys[0];
		}

		if (value.getClass().isArray() && ((String[]) value).length == 1) {
			value = ((String[]) value)[0];
		}
		if (value instanceof String && SearchSettingService.isLowerCaseFilter(key)) {
			value = ((String) value).toLowerCase();
		}
		if (value instanceof String && SearchSettingService.isSplitByApproxFilter(key)) {
			value = ((String) value).split("~~");
		}

		// To support bluesky search filters
		if (value instanceof String && SearchSettingService.isSplitBySingleTiltaForSearch(key)) {
			value = ((String) value).split("~");
		}

		Object filter = null;

		key = SearchSettingService.getFilterAlias(key);

		String[] filters = null;
		String[] orFilterTerms = null;
		if (value instanceof String && (orFilterTerms = ((String) value).split("\\&\\|")).length > 1) {
			List<Object> orFilter = new ArrayList<Object>();
			for (String orFilterTerm : orFilterTerms) {
				String[] filterElements = ((String) orFilterTerm).split("\\~");
				if (orFilterTerm instanceof String
						&& (filterElements = ((String) orFilterTerm).split("\\~")).length > 1) {
					orFilter.add(buildFilter(filterElements[1], filterElements[0]));
				}
			}
			if (orFilter.size() > 0) {
				filter = new OrFilter(orFilter);
			}
		} else if ((filters = ((String) key).split("\\|")).length > 1) {
			List<Object> orFilter = new ArrayList<Object>();
			for (String orFilterKey : filters) {
				orFilter.add(buildFilter(value, orFilterKey));
			}
			if (orFilter.size() > 0) {
				filter = new OrFilter(orFilter);
			}

		} else if (type != null && type.equals("|")) {
			List<Object> orFilter = new ArrayList<Object>();
			filters = ((String) key).split("\\|");
			for (String orFilterKey : filters) {
				orFilter.add(buildFilter(((String) value).toLowerCase(), orFilterKey));
			}
			if (orFilter.size() > 0) {
				filter = new OrFilter(orFilter);
			}
		} else if ((filters = ((String) key).split("\\&")).length > 1) {
			for (String andFilterTerm : filters) {
				filter = buildFilter(value, andFilterTerm);
			}
		} else if (value instanceof String && (filters = ((String) value).split("\\|")).length > 1) {
			List<Object> orFilter = new ArrayList<Object>();
			for (String orFilterTerm : filters) {
				String[] filterElements = orFilterTerm.split("\\:");
				orFilter.add(buildFilter(filterElements.length > 1 ? filterElements[1] : "", filterElements[0]));
			}
			if (orFilter.size() > 0) {
				filter = new OrFilter(orFilter);
			}
		} else if (key.equalsIgnoreCase(IS_REVIEWED) || key.equalsIgnoreCase(GTE_USEDINSCOLLECTIONCOUNT)) {
			if (key.equalsIgnoreCase(GTE_USEDINSCOLLECTIONCOUNT)) {
				key = "statistics.usedInSCollectionCountN";
			}
			filter = new RangeFilter(value.toString(), key);

		} else {
			filter = buildFilter(value, key);
		}

		if (filter != null) {
			if (type != null && type.equals("!")) {
				filter = new NotFilter(filter);
			}
			filerList.add(filter);
		}
	}

}
