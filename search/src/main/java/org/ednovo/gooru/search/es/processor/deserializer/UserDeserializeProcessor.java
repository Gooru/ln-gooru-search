/**
 * 
 */
package org.ednovo.gooru.search.es.processor.deserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.domain.service.UserSearchResult;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.springframework.stereotype.Component;

/**
 * @author SearchTeam
 * 
 */
@Component
public class UserDeserializeProcessor extends DeserializeProcessor<List<UserSearchResult>, UserSearchResult> {

	@Override
	List<UserSearchResult> deserialize(Map<String, Object> model, SearchData searchData, List<UserSearchResult> output) {
		Map<String, Object> hitsMap = (Map<String, Object>) model.get(SEARCH_HITS);
		List<Map<String, Object>> hits = (List<Map<String, Object>>) (hitsMap).get(SEARCH_HITS);
		output = new ArrayList<UserSearchResult>();
		for (Map<String, Object> hit : hits) {
			Map<String, Object> fields = (Map<String, Object>) hit.get(SEARCH_SOURCE);
			output.add(collect(fields, searchData, null));
			
		}
		return output;
	}

	@Override
	UserSearchResult collect(Map<String, Object> model, SearchData input, UserSearchResult searchResult) {

		if (searchResult == null) {
			searchResult = new UserSearchResult();
		}
		
		// DETAILS OF CORRESPONDENCE 
		
		Map<String,String> districts =new HashMap<String, String>();
		
		districts.put("code",(String) model.get(SEARCH_DISTRICTCODE));
		districts.put("Id",(String)model.get(SEARCH_DISTRICTID));
		districts.put("Name",(String)model.get(SEARCH_DISTRICTNAME));
		searchResult.setDistrict(districts);
		
		Map<String,String> stateProvience =(Map<String, String>) model.get(SEARCH_STATE_PROVIENCE);
		searchResult.setStateProvince(stateProvience);
		
		Map<String,String> organization = (Map<String, String>) model.get(SEARCH_ORGANIZATION);
        searchResult.setOrganization(organization);
		// searchResult.setResultUId((String) dataMap.get("resultUid"));
		searchResult.setUserId(((Integer) model.get(SEARCH_USER_ID)) + "");
		searchResult.setFirstName((String) model.get(SEARCH_FIRST_NAME));
		searchResult.setLastName((String) model.get(SEARCH_LAST_NAME));
		searchResult.setUserName((String) model.get(SEARCH_USER_NAME));
		searchResult.setGooruUId((String) model.get(SEARCH_USER_GOORU_UID));
		searchResult.setAccountId((String) model.get(SEARCH_ACCOUNT_ID));
		searchResult.setGrade((String) model.get(SEARCH_GRADE));
		searchResult.setNetwork((String) model.get(SEARCH_NETWORK));
		searchResult.setEmailId((String) model.get(SEARCH_EMAIL_ID));
		searchResult.setCreatedOn((String) model.get(SEARCH_CREATED_ON));
		searchResult.setRoleSet((String) model.get(SEARCH_ROLE_SET));
		searchResult.setConfirmStatus((String) model.get(SEARCH_CONFIRMS_STATUS));
		searchResult.setProfileImageUrl((String) model.get(SEARCH_USER_PROFILE_IMAGE));
		searchResult.setLastLogin((String) model.get(SEARCH_LAST_LOGIN));
		searchResult.setIsDeleted(((String) model.get(SEARCH_IS_DELETED)));
		searchResult.setAccountRegisterType(((String) model.get(SEARCH_ACCOUNT_REGISTER_TYPE)));
		searchResult.setAccountTypeId((Integer) model.get(SEARCH_ACCOUNT_TYPE_ID));
		searchResult.setAboutMe((String) model.get(SEARCH_ABOUT_ME));
		searchResult.setNotes((String) model.get(SEARCH_NOTES));
		searchResult.setProfileVisibility((String) model.get(SEARCH_PROFILE_VISIBILITY));
		searchResult.setMeta((Map<String, Map<String, Object>>) model.get(SEARCH_META));
		String roleSet=(String) model.get(SEARCH_ROLE_SET);
		if(input.isActiveEnable()){
		searchResult.setActive((Integer) model.get(SEARCH_ACTIVE));
		}
		
		if (model.get(SEARCH_PARENT_ACCOUNT_USER_NAME) != null) {
			searchResult.setParentAccountUserName( ((String) model.get(SEARCH_PARENT_ACCOUNT_USER_NAME)) );
		}
		if (model.get(SEARCH_CHILD_ACCOUNT_COUNT) != null) {
			searchResult.setChildAccountCount( (Integer) model.get(SEARCH_CHILD_ACCOUNT_COUNT));
		}
		if(model.get(SEARCH_USER_TYPE) != null){
		searchResult.setUserType((String) model.get(SEARCH_USER_TYPE));
		}
		
		return searchResult;

	}

	@Override
	protected SearchProcessorType getType() {
		return SearchProcessorType.UserDeserializer;
	}

}
