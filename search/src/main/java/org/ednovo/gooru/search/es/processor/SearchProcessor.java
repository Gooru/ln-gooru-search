/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.SearchSettingType;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.ednovo.gooru.search.responses.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author SearchTeam Abstract class which will be extended by all processors
 * 
 */
public abstract class SearchProcessor<I extends SearchData, O extends Object> implements Constants {

	protected static final ObjectMapper SERIAILIZER = new ObjectMapper();

	private boolean isTransactional = false;

	private boolean isSessionInfoRequired = false;

	public static Set<String> RESOURCE_FORMAT_LIST = new HashSet<>();
	
	public static Set<Map<String,String>> RESOURCE_FORMAT_VALUE = new HashSet<>();
	
	public static Map<String,String> RESOURCE_FORMAT_KEY_VALUE = new HashMap<String, String>();
	
	public static Map<String,String> INSTRUCTIONAL_USE_KEY_VALUE = new HashMap<String, String>();
	
	protected static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	protected static Pattern pattern;
	
	protected Matcher matcher;
	
	private static final Map<SearchProcessorType, SearchProcessor<SearchData, Object>> processors = new HashMap<SearchProcessorType, SearchProcessor<SearchData, Object>>();
	
	@Autowired
	private SearchSettingService searchSettingService;

	protected static final Logger logger = LoggerFactory.getLogger(SearchProcessor.class);
	
	static {
		SERIAILIZER.setSerializationInclusion(Include.NON_NULL);
	}
	
	
	@PostConstruct
	public void init(){
	}
	
	// Holds all the instance of processors


	protected String getSearchSetting(String name) {
		return SearchSettingService.getByName(name);
	}
	
	protected Float getCassandraSettingAsFloat(String name) {
		String value = SearchSettingService.getByName(name);
		return value != null && value.length() > 0 ? Float.valueOf(value) : null;
	}

	protected int getCassandraSettingAsInt(String name, int defaultValue) {
		Integer value = 300;
		return value != null ? value : defaultValue;
	}

	protected String getSearchSetting(String name,
			String defaultValue) {
		String value = SearchSettingService.getByName(name);
		return value != null ? value : defaultValue;
	}

	protected final String getSetting(String name) {
		SearchSettingType configConstant = SearchSettingType.getType(name);
		return getSetting(configConstant);
	}

	protected final Float getSettingAsFloat(String name) {
		SearchSettingType configConstant = SearchSettingType.getType(name);
		return getSettingAsFloat(configConstant);
	}

	protected final Float getSettingAsList(String name) {
		SearchSettingType configConstant = SearchSettingType.getType(name);
		return getSettingAsFloat(configConstant);
	}

	protected final String getSetting(SearchSettingType settingConstant) {
		if(settingConstant == null) {
			return null;
		}
		String value = getSearchSetting(settingConstant.getName());
		return value != null ? value : (String) settingConstant.getDefaultValue();
	}

	protected final Float getSettingAsFloat(SearchSettingType settingConstant) {
		if(settingConstant == null) {
			return null;
		}
		String value = getSearchSetting(settingConstant.getName());
		return value != null ? Float.valueOf(value) : (Float) settingConstant.getDefaultValue();
	}

	protected final Float getCategorySettingAsFloat(String name) {
		SearchSettingType configConstant = SearchSettingType.getCategoryType(name);
		return getSettingAsFloat(configConstant);
	}
	
	protected final Boolean getSettingAsBoolean(String name){
		SearchSettingType configConstant = SearchSettingType.getType(name);
		return getSettingAsBoolean(configConstant);
	}
	
	protected final Boolean getSettingAsBoolean(SearchSettingType settingConstant) {
		if(settingConstant != null) {
			String value = getSearchSetting(settingConstant.getName());
			return value != null ? Boolean.valueOf(value) : Boolean.valueOf(settingConstant.getDefaultValue().toString());			
		}
		return false;
	}

	
	protected final Float getResourceFormatSettingAsFloat(String name) {
		SearchSettingType configConstant = SearchSettingType.getResourceFormatType(name);
		return getSettingAsFloat(configConstant);
	}
	/**
	 * This method is designed to provide support to implement the underlying
	 * mechanism of processor. This method will be called by
	 * {@link ProcessorChain} to perform the process.
	 * 
	 * @param searchData
	 * @param response
	 */
	public abstract void process(I searchData,
			SearchResponse<O> response);

	/**
	 * Registering of processor. The processors are registered automatically
	 * when they extend this class. This method is called when all the spring
	 * beans of this instance are auto-wired.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	protected final void register() {
		if (processors.containsKey(getType())) {
			throw new RuntimeException("Duplicate Search Procesor with name : " + getType() + this.getClass().getName());
		} else {
			processors.put(getType(), (SearchProcessor<SearchData, Object>) this);
		}
	}

	/**
	 * Fetch processor based on {@link SearchProcessorType}
	 * 
	 * @param processorType
	 * @return
	 */
	public static SearchProcessor<SearchData, Object> get(SearchProcessorType processorType) {
		return processors.get(processorType);
	}

	/**
	 * Type of the processor. This should be unique within processors. This type
	 * is used for registration purpose.
	 */
	protected abstract SearchProcessorType getType();

	public boolean isTransactional() {
		return isTransactional;
	}

	public void setTransactional(boolean isTransactional) {
		this.isTransactional = isTransactional;
	}

	public boolean isSessionInfoRequired() {
		return isSessionInfoRequired;
	}

	public void setSessionInfoRequired(boolean isSessionInfoRequired) {
		this.isSessionInfoRequired = isSessionInfoRequired;
	}

	public SearchSettingService getSearchSettingService() {
		return searchSettingService;
	}

	public void setSearchSettingService(SearchSettingService searchSettingService) {
		this.searchSettingService = searchSettingService;
	}

	public boolean emailValidate(final String hex) {
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
	
	public boolean uuidValidate(final String hex) {
		pattern = Pattern.compile(UUID);
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}

	public boolean expressionValidate(final String query) {
		boolean expressionFound = false;
		if(query.contains(" AND ") || query.contains(" OR ")){
			expressionFound = true;
		}
		return expressionFound;
	}

	public boolean standardValidate(SearchData searchData) {
		long start = System.currentTimeMillis();
		boolean standardFound = false;
		String queryString = searchData.getQueryString().trim();
		int dotsCount = StringUtils.countMatches(queryString, DOT);
		int hyphenCount = StringUtils.countMatches(queryString, HYPHEN);
		if (queryString.contains(HYPHEN)) {
			String subject = queryString.substring(0, queryString.indexOf(HYPHEN));

			if (((dotsCount > 2 && !(dotsCount == queryString.length())) || (hyphenCount > 2 && !(hyphenCount == queryString.length()))) && !searchData.getParameters().containsKey(SEARCH_FLT_STANDARD)
					&& !searchData.getParameters().containsKey(SEARCH_FLT_GUT_CODE)) {
				if (getSearchSetting(SEARCH_TAXONOMY_ROOT_CODE) != null) {
					int dotsCountInSubject = StringUtils.countMatches(subject, DOT);
					if (dotsCountInSubject == 2 && (Arrays.asList(getSearchSetting(SEARCH_TAXONOMY_ROOT_CODE).split(COMMA)).contains(queryString.substring(0, queryString.indexOf(DOT))))) {
						standardFound = true;
						searchData.setTaxFilterType(TYPE_STANDARD);
						searchData.setQueryString(STAR);
						searchData.getParameters().put(SEARCH_FLT_STANDARD, queryString.toLowerCase());
					} else if (dotsCountInSubject == 1
							&& (Arrays.asList(getSearchSetting(SEARCH_TAXONOMY_SUBJECT_CLASSIFICATION).split(COMMA)).contains(queryString.substring(0, queryString.indexOf(DOT))))) {
						standardFound = true;
						searchData.setQueryString(STAR);
						searchData.getParameters().put(SEARCH_FLT_GUT_CODE, queryString.toLowerCase());
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Time taken to detect standards : " + (System.currentTimeMillis() - start) + "ms");
			}
		}
		return standardFound;
	}

    //Loose detection of standards by checking if there are dots or hyphens in the query string
    //Needed because the standard could itself be mis spelt and may not be represented as part of the root code.
	public boolean standardValidateLite(SearchData searchData) {
      long start = System.currentTimeMillis();
      String queryString = searchData.getQueryString();
      boolean possibleStandardFound = matchesStandardsPattern(queryString) && !searchData.getParameters().containsKey(SEARCH_FLT_STANDARD) ;
      logger.info("Time taken to detect standards loosely: " + (System.currentTimeMillis() - start) + "ms, possible standard:" + possibleStandardFound);
      return possibleStandardFound;
    }

	public boolean matchesStandardsPattern(String queryString) {
		long start = System.currentTimeMillis();
		boolean possibleStandardFound = false;
		int dotsCount = StringUtils.countMatches(queryString, DOT);
		int hyphenCount = StringUtils.countMatches(queryString, HYPHEN);
		if (queryString.contains(HYPHEN)) {
			String subject = queryString.substring(0, queryString.indexOf(HYPHEN));
			int dotsCountInSubject = StringUtils.countMatches(subject, DOT);

			if (dotsCountInSubject == 2 && (dotsCount > 2 || hyphenCount > 2)) {
				possibleStandardFound = true;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Time taken to detect standards loosely: " + (System.currentTimeMillis() - start) + "ms");
			}
		}
		return possibleStandardFound;
	}
	
    public boolean chemicalFormulaValidate(SearchData searchData) {
        long start = System.currentTimeMillis();
        boolean chemFormulaFound = false;
        String queryString = searchData.getQueryString();

        String chemFormulaList = getSearchSetting(SEARCH_DICTIONARY_CHEMICAL_FORMULA);
        if (chemFormulaList != null && chemFormulaList.length() > 0) {
          String[] chemFormulas = chemFormulaList.split("\\s*,\\s*");
          if (chemFormulas != null && chemFormulas.length > 0) {
            for (String chemFormula : chemFormulas) {
              if (!chemFormula.equalsIgnoreCase("") && queryString.equalsIgnoreCase(chemFormula)) {
                chemFormulaFound = true;
                break;
              }
            }
          }
        } else {
          if(logger.isDebugEnabled()){	
        	  logger.debug("No chemical formula list found for spell checker lookup, returning as is. Please check cassandra storage or cache version.");
          }
        }
        if(logger.isDebugEnabled()){
        	logger.debug("Time taken to detect chemical formula: " + (System.currentTimeMillis() - start) + "ms");
        }
        return chemFormulaFound;
      }

}
