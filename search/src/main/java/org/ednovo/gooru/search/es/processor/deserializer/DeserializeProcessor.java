/**
 *
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.constant.IndexFields;
import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.Code;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessor;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author SearchTeam
 *
 */

public abstract class DeserializeProcessor<O, S> extends SearchProcessor<SearchData, O> implements Constants {

	abstract O deserialize(Map<String, Object> model, SearchData input, O output);

	abstract S collect(Map<String, Object> model, SearchData input, S output);

	@Override
	public final void process(SearchData searchData, SearchResponse<O> response) {
		try {
			Map<String, Object> responseAsMap = (Map<String, Object>) SERIAILIZER.readValue(searchData.getSearchResultText(),
					new TypeReference<Map<String, Object>>() {
					});
			O searchResult = deserialize(responseAsMap, searchData, null);
			response.setSearchResults(searchResult);			
		   if(responseAsMap.get(SEARCH_HITS) !=null) {
			   Map<String, Object> hit = (Map<String, Object>) responseAsMap.get(SEARCH_HITS);
			   if(((List<Map<String, Object>>) (hit).get(SEARCH_HITS) )!= null) {
					Map<String,Object> stats = new HashMap<String,Object>(3);
					 stats.put("totalHitCount",((Integer) hit.get(SEARCH_TOTAL)).longValue());
					 stats.put("pageSize",searchData.getSize());
					 stats.put("pageNumber", searchData.getPageNum());
					 
					 Map<String,Object> query = new HashMap <String,Object>(4);
					 query.put("userQueryString", searchData.getUserQueryString());
					 query.put("rewrittenQueryString", searchData.getQueryString());
					 if(searchData.getSpellCheckQueryString()!= null && !searchData.getSpellCheckQueryString().isEmpty()) {
						  query.put("current",searchData.getSpellCheckQueryString());
						  query.put("rewriteType",SPELLCHECKER);
					 }	 
					 response.setQuery(query);
					 response.setStats(stats);
		
					List<Map<String, Object>> hits = (List<Map<String, Object>>) (hit).get(SEARCH_HITS);
					response.setTotalHitCount(((Integer) hit.get(SEARCH_TOTAL)).longValue());
					response.setResultCount(hits.size());
					//response.setSpellCheckQueryString(searchData.getSpellCheckQueryString()); 
				}

			}
			/*if(searchData.getFacet() !=  null && searchData.getFacet().trim().length() > 0 && responseAsMap.get(FACETS) !=null){
				response.setFacets(responseAsMap.get(FACETS));
			}*/

		} catch (Exception e) {
			LOG.error("Search Error", e);
			throw new SearchException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
    protected String updateTeksCode(Map<String, Object> taxonomyMap, String userTaxonomyPreference) {/*
    	   String taxonomyDataSet = (String) taxonomyMap.get(TAXONOMYDATASET);
           String rootNodeId = null;
           if (taxonomyDataSet != null) {
               try {
                   JSONObject taxonomyDataSetObject = new JSONObject(taxonomyDataSet);
                   JSONObject curriculumObject = taxonomyDataSetObject.getJSONObject(SEARCH_CURRICULUM);
                   if (curriculumObject != null) {
                       JSONArray userPreferredTaxCode = new JSONArray();
                       JSONArray userPreferredTaxDesc = new JSONArray();
                       if(userTaxonomyPreference != null && userTaxonomyPreference.trim().length() > 0){
                           String taxonomyRootCodes = taxonomyRespository.getFindTaxonomyCodeList(userTaxonomyPreference);
                           if(taxonomyRootCodes != null && taxonomyRootCodes.trim().length() > 0){
                               List<String> taxonomyRootCodeList = Arrays.asList(taxonomyRootCodes.split(","));
                               JSONArray curriculumCodeArray = curriculumObject.getJSONArray(SEARCH_CURRICULUM_CODE);
                               JSONArray curriculumDescArray = curriculumObject.getJSONArray(SEARCH_CURRICULUM_DESC);
                               List<String> preferenceTax = Arrays.asList(userTaxonomyPreference.split(","));
                               if (curriculumCodeArray != null) {
                                   for (int i = 0; i < curriculumCodeArray.length(); i++) {
                                       for(String taxonomyRootCode : taxonomyRootCodeList){
                                           // temp fix for #DO-5256
                                           String code = (String) curriculumCodeArray.get(i);
                                           if(code.startsWith("CA")){
                                               rootNodeId = taxonomyRespository.findTaxonomyRootCode(code);
                                               if(preferenceTax.contains(rootNodeId) && !stdValueExists(userPreferredTaxCode,code)){
                                            	   userPreferredTaxCode.put((String) curriculumCodeArray.get(i));
                                            	   userPreferredTaxDesc.put((String) curriculumDescArray.get(i));
                                               }
                                           } else if(((String) curriculumCodeArray.get(i)).startsWith(taxonomyRootCode) && !stdValueExists(userPreferredTaxCode,code)){
                                        	   userPreferredTaxCode.put((String) curriculumCodeArray.get(i));
                                        	   userPreferredTaxDesc.put((String) curriculumDescArray.get(i));
                                               break;
                                           }
                                       }
                                       
                                   }
                               }
                           }
                   }
                       curriculumObject.put("curriculumCode", userPreferredTaxCode);
                       curriculumObject.put("curriculumDesc", userPreferredTaxDesc);
                       if(taxonomyDataSetObject.isNull("subject")) {
                    	   taxonomyDataSetObject.put("subject", new JSONArray());
                       }
                       if(taxonomyDataSetObject.isNull("course")) {
                    	   taxonomyDataSetObject.put("course", new JSONArray());
                       }
                       taxonomyDataSetObject.put("curriculum", curriculumObject);
               }
               return taxonomyDataSetObject.toString();
               } catch (JSONException e) {
                   LOG.error("taxonomyDataSet parsing: " + e.getMessage());
               }
           }
           return taxonomyDataSet;
       */return null;}
		
	boolean stdValueExists(JSONArray userTaxPref, String stdCode) throws JSONException{
		for(int stdCodeIndex=0; stdCodeIndex < userTaxPref.length() ; stdCodeIndex++){
			if(userTaxPref.get(stdCodeIndex).equals(stdCode)){
				return true;
			}
		}
		return false;
	}
	
	protected String updateTeksCode(Map<String, Object> taxonomyMap, String userTaxonomyPreference,String rootId) {/*
		String taxonomyDataSet = (String) taxonomyMap.get(TAXONOMYDATASET);
		if (taxonomyDataSet != null) {
			try {
				JSONObject taxonomyDataSetObject = new JSONObject(taxonomyDataSet);
				JSONObject curriculumObject = taxonomyDataSetObject.getJSONObject(SEARCH_CURRICULUM);
				if (curriculumObject != null) {
					JSONArray userPreferenceTaxonomy = new JSONArray();
					JSONArray userPreferredCurriculumDesc = new JSONArray();
					if(userTaxonomyPreference != null && userTaxonomyPreference.trim().length() > 0  && rootId != null){
						JSONArray curriculumCodeArray = curriculumObject.getJSONArray(SEARCH_CURRICULUM_CODE);
						if (curriculumCodeArray != null) {
								List <String> userPreference = Arrays.asList(userTaxonomyPreference.split(","));
								String keyValuePair = rootId.substring(1,rootId.indexOf("}"));
								String [] splitBykeyValue = keyValuePair.split("#");
							for(int temp=0 ;temp < splitBykeyValue.length;temp++){
								String [] splitStandards = splitBykeyValue[temp].split("=");
									if(temp>0){
										  if(userPreference.contains(splitStandards[0].substring(2, splitStandards[0].length()))){
								    	        String [] splitKeyValues = splitStandards[1].split(",");
										     for(int j=0; j< splitKeyValues.length;j++){
											          String []tempStandards=splitKeyValues[j].split("~");
											          userPreferenceTaxonomy.put(tempStandards[0]);
											          if(tempStandards.length > 1 && !tempStandards[1].isEmpty()){
											          userPreferredCurriculumDesc.put(tempStandards[1]);
											          }
										           }
									          }
									 }
									else {
								    	    if(userPreference.contains(splitStandards[0])){
								    	        String [] splitKeyValues = splitStandards[1].split(",");
											       for(int j=0; j< splitKeyValues.length;j++){
												            String []tempStandards=splitKeyValues[j].split("~");
												            userPreferenceTaxonomy.put(tempStandards[0]);
												            if(tempStandards.length > 1 && !tempStandards[1].isEmpty()){
														          userPreferredCurriculumDesc.put(tempStandards[1]);
														     }
											             
									                 }
								             }				     
							         }           
						      

						  }
					}
				}
					   curriculumObject.put("curriculumDesc", userPreferredCurriculumDesc);
					   curriculumObject.put("curriculumCode", userPreferenceTaxonomy);
					   taxonomyDataSetObject.put("curriculum", curriculumObject);
			    }

			List<CodeEo> courseList = (List<CodeEo>) taxonomyMap.get(SEARCH_COURSE);
			List<String> courseCodeIds = new ArrayList<String>();
			for(int courseIndex=0; courseList.size() > courseIndex; courseIndex++){
				Map<String,String> courseMap = (Map<String, String>) courseList.get(courseIndex);
					courseCodeIds.add(courseMap.get(SEARCH_CODE_ID));
			}
				
			JSONArray taxonomyCourseArr = new JSONArray();
				String coursesStr = null;
				if(courseCodeIds.size() > 0){
					coursesStr = taxonomyRespository.findGooruTaxonomyCourse(courseCodeIds);
				}
				if(coursesStr != null){
					for(String courseStr : coursesStr.split(",")){
						taxonomyCourseArr.put(courseStr);
					}
				}
				taxonomyDataSetObject.put("course", taxonomyCourseArr);
 
			return taxonomyDataSetObject.toString();
			} catch (JSONException e) {
				LOG.error("taxonomyDataSet parsing: " + e.getMessage());
			}
			
		}
		return taxonomyDataSet;
	*/return null;}
	
	protected Long getAvgTimeSpent(Object avgTimeSpentObj){
	   	   Long avgTimeSpent = 0L;
    	   try{
    		   Integer avgTSInt = (Integer) avgTimeSpentObj;
    		   avgTimeSpent = avgTSInt.longValue();
    	   }
    	   catch(Exception e){
    		   if(e instanceof ClassCastException){
    			   avgTimeSpent = (Long)avgTimeSpentObj;
    		   }
    		   else{
    			   logger.error("Error in setting avg timespnet " + e);
    		   }
    	   }
    	   
    	   return avgTimeSpent;
	}
	protected String getTaxonomyMetadataLabel(List<Code> taxonomyMetadatas) {
		if (taxonomyMetadatas != null) {
			String label = "";
			for (int i = 0; i < taxonomyMetadatas.size(); i++) {
				if (label.length() > 0) {
					label += ",";
				}
				Map<String, String> taxonomyMetadata = (Map<String, String>) taxonomyMetadatas.get(i);
				label += taxonomyMetadata.get(IndexFields.LABEL);
			}
			return label;
		}
		return null;
	}

}
