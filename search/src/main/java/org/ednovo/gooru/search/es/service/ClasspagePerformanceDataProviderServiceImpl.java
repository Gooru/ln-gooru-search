package org.ednovo.gooru.search.es.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import org.ednovo.gooru.search.es.processor.util.JsonDeserializer;
import org.ednovo.gooru.search.model.AssignmentUsageDTO;
import org.ednovo.gooru.search.model.AsssignmentResourceUsageDTO;
import org.ednovo.gooru.search.model.ClasspageDataProviderCriteria;
import org.ednovo.gooru.search.model.ResourceUsageData;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class ClasspagePerformanceDataProviderServiceImpl implements ClasspagePerformanceDataProviderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClasspagePerformanceDataProviderServiceImpl.class);

	public Map<String, ResourceUsageData> getUserResourcePerformanceDataMap(ClasspageDataProviderCriteria classpageDataProviderCriteria) {
		Map<String, ResourceUsageData> assignmentResourceUserUsageDataMap = new HashMap<String, ResourceUsageData>();
		try {
			JsonObject dataParam = Json.createObjectBuilder().add("fields", "timeSpent,avgTimeSpent,resourceGooruOId,OE,questionType,category,gooruUId,userName,userData,metaData,reaction,gooruOId,title,description,options,skip")
							.add("filters", Json.createObjectBuilder().add("session", "AS").add("classId", classpageDataProviderCriteria.getClassId()))
							.add("paginate", Json.createObjectBuilder().add("sortBy", "itemSequence").add("sortOrder", "ASC")).build();
			try {
				URI resourceUsageUri = new URI("http://www.goorulearning.org/insights/api/v1/classpage/" + classpageDataProviderCriteria.getAssignmentId() + "/users/usage.json?sessionToken=&data=" + URLEncoder.encode(dataParam.toString(), "UTF-8"));
				
			/*JsonObject dataParam = Json.createObjectBuilder().add("fields", "timeSpent,avgTimeSpent,resourceGooruOId,OE,questionType,category,gooruUId,userName,userData,metaData,reaction,gooruOId,title,description,options,skip").add("filters", Json.createObjectBuilder().add("session", "AS").add("classId", "26ef28da-bde9-42d3-b021-7500ad98576d")).add("paginate", Json.createObjectBuilder().add("sortBy", "itemSequence").add("sortOrder", "ASC")).build();
			try {
				URI resourceUsageUri = new URI("http://www.goorulearning.org/insights/api/v1/classpage/a9b2f9fb-8837-426d-b70b-ca43fa8f0a75/users/usage.json?sessionToken=7f19c2ca-5436-11e4-8d6c-123141016e2a&data=" + URLEncoder.encode(dataParam.toString(), "UTF-8"));
				*/
				ClientResource usageResource = new ClientResource(resourceUsageUri);
				usageResource.get();
				if (usageResource.getStatus() != null && usageResource.getStatus().isSuccess()) {
					AsssignmentResourceUsageDTO asssignmentResourceUsageDTO = new AsssignmentResourceUsageDTO();
					Representation usageRepresentation = usageResource.get();
					asssignmentResourceUsageDTO = JsonDeserializer.deserialize(usageRepresentation.getText(), new TypeReference<AsssignmentResourceUsageDTO>() {});
					List<AssignmentUsageDTO> assignmentUsage = asssignmentResourceUsageDTO.getContent();
					for (AssignmentUsageDTO resource : assignmentUsage) {
						if (resource.getStatus() != null && resource.getStatus() == 0) {
							List<ResourceUsageData> userDataList = resource.getUserData();
							for (ResourceUsageData userData : userDataList) {
								if (userData.getStatus() != null && userData.getStatus() == 0 && userData.getGooruUId().equalsIgnoreCase("6f337b1c-0b0d-49b3-8314-e279181aeddf")) {
									assignmentResourceUserUsageDataMap.put(resource.getResourceGooruOId(), userData);
									break;
								}
							}
						}
					}
					return assignmentResourceUserUsageDataMap;
				}
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error("Insights user usage call URL encoding not supported");
				e1.printStackTrace();
			} catch (URISyntaxException e) {
				LOGGER.error("Insights user usage call URL syntax is incorrectly used in suggest");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assignmentResourceUserUsageDataMap;
	}
	
}
