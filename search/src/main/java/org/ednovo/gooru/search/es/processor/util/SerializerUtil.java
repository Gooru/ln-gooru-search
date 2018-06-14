/////////////////////////////////////////////////////////////
// SerializerUtil.java
// gooru-api
// Created by Gooru on 2014
// Copyright (c) 2014 Gooru. All rights reserved.
// http://www.goorulearning.org/
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
/////////////////////////////////////////////////////////////
package org.ednovo.gooru.search.es.processor.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.ednovo.gooru.search.es.constant.Constants;
import org.ednovo.gooru.search.es.exception.MethodFailureException;
import org.ednovo.gooru.search.es.model.ContentSearchResult;
import org.ednovo.gooru.search.es.model.SessionContextSupport;
import org.ednovo.gooru.search.es.model.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;

import flexjson.JSONSerializer;

public class SerializerUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SerializerUtil.class);

	private static final String[] EXCLUDES = { "*.class", "*.userRole", "*.organization", "*.primaryOrganization", "organization", "*.codeType.organization.*" };

	public static ModelAndView toModelAndView(final Object object) {
		ModelAndView jsonmodel = new ModelAndView(Constants.REST_MODEL);
		jsonmodel.addObject(Constants.MODEL, object);
		return jsonmodel;
	}

	public static ModelAndView toJsonModelAndView(final Object model, final boolean deepSerialize) {
		return toModelAndView(serializeToJson(model, deepSerialize));
	}

	public static ModelAndView toModelAndView(final Object obj, final String type) {
		return toModelAndView(serialize(obj, type));
	}

	public static ModelAndView toModelAndViewWithInFilter(final Object obj, final String type, final String... includes) {
		return toModelAndView(serialize(obj, type, null, includes));
	}

	public static ModelAndView toModelAndViewWithIoFilter(final Object obj, final String type, final String[] excludes, final String... includes) {
		return toModelAndView(serialize(obj, type, excludes, includes));
	}

	public static ModelAndView toModelAndViewWithIoFilter(final Object obj, final String type, final String[] excludes, final boolean excludeNullObject, final String... includes) {
		return toModelAndView(serialize(obj, type, excludes, false, excludeNullObject, includes));
	}

	// need to improve logic
	public static ModelAndView toModelAndViewWithErrorObject(final Object obj, final String type, final String entityName, final Errors errors, String[] excludes, final String... includes) {
		return toModelAndView(serialize(obj, type, excludes, includes));
	}

	/**
	 * @param model
	 * @param excludes
	 * @param includes
	 * @return
	 */
	public static String serializeToJsonWithExcludes(final Object model, final String[] excludes, final String... includes) {
		return serialize(model, Constants.JSON, excludes, includes);
	}

	public static String serialize(final Object model, final String type) {
		return serialize(model, type, null);
	}

	public static String serializeToJson(final Object model, final String... includes) {
		return serialize(model, Constants.JSON, null, includes);
	}

	public static JSONObject serializeToJsonObjectWithExcludes(final Object model, final String[] excludes, final String... includes) throws Exception {
		return new JSONObject(serialize(model, Constants.JSON, excludes, includes));
	}

	public static JSONObject serializeToJsonObject(final Object model, final String... includes) throws Exception {
		return new JSONObject(serialize(model, Constants.JSON, null, includes));
	}

	public static String serializeToJsonWithExcludes(final Object model, final String[] excludes, final boolean deepSerialize, final String... includes) {
		return serialize(model, Constants.JSON, excludes, deepSerialize, true, includes);
	}

	public static String serializeToJsonWithExcludes(final Object model, final String[] excludes, final boolean deepSerialize, final boolean excludeNullObject, final String... includes) {
		return serialize(model, Constants.JSON, excludes, deepSerialize, excludeNullObject, includes);
	}

	public static String serializeToJson(final Object model, final boolean deepSerialize, final String... includes) {
		return serialize(model, Constants.JSON, null, deepSerialize, includes);
	}

	public static String serializeToJson(final Object model, final boolean deepSerialize, final boolean excludeNullObject) {
		return serialize(model, Constants.JSON, null, deepSerialize, false, excludeNullObject);
	}

	public static String serializeToJson(final Object model, final String[] excludes, final boolean deepSerialize, final boolean excludeNullObject) {
		return serialize(model, Constants.JSON, excludes, deepSerialize, false, excludeNullObject);
	}

	public static JSONObject serializeToJsonObjectWithExcludes(final Object model, final String[] excludes, final boolean deepSerialize, final String... includes) throws Exception {
		return new JSONObject(serialize(model, Constants.JSON, excludes, deepSerialize, includes));
	}

	public static JSONObject serializeToJsonObject(final Object model, final boolean deepSerialize, final String... includes) throws Exception {
		return new JSONObject(serialize(model, Constants.JSON, null, deepSerialize, includes));
	}

	public static String serialize(final Object model, final String type, final String[] excludes, final boolean deepSerialize, final String... includes) {
		return serialize(model, type, excludes, deepSerialize, true, false, includes);
	}

	public static String serialize(final Object model, final String type, final String[] excludes, final boolean deepSerialize, final boolean excludeNullObject, final String... includes) {
		return serialize(model, type, excludes, deepSerialize, true, excludeNullObject, includes);
	}

	public static String serialize(final Object model, final String type, final String[] excludes, final String... includes) {
		return serialize(model, type, excludes, false, includes);
	}
	public static JSONSerializer appendTransformers(final JSONSerializer serializer, final boolean excludeNullObject) {
		//serializer.transform(new UserTransformer(false), User.class).transform(new OrganizationTransformer(), Organization.class).transform(new ContentPermissionTransformer(), ContentPermission.class);
		if (excludeNullObject) {
			serializer.transform(new ExcludeNullTransformer(), void.class);
		}
		return serializer;

	}
	/**
	 * @param model
	 * @param type
	 * @param excludes
	 * @param includes
	 * @return
	 */
	public static String serialize(Object model, final String type, String[] excludes, boolean deepSerialize, final boolean useBaseExcludes, final boolean excludeNullObject, String... includes) {
		if (model == null) {
			return "";
		}
		String serializedData = null;
		JSONSerializer serializer = new JSONSerializer();
		//boolean handlingAssessmentQuestion = willSerializeAssessmentQuestion(model);

		if (type == null || type.equals(Constants.JSON)) {

			if (includes != null) {
				includes = (String[]) ArrayUtils.add(includes, "*.contentPermissions");
				serializer.include(includes);
			} else {
				serializer.include("*.contentPermissions");
			}
			serializer.include(includes);

			if (useBaseExcludes) {
				if (excludes != null) {
					excludes = (String[]) ArrayUtils.addAll(excludes, EXCLUDES);
				} else {
					excludes = EXCLUDES;
				}
			}

			if (model instanceof User) {
				deepSerialize = true;
			}
			//disabled while removing api-jar dependency
			if (model != null) {
				serializer = appendTransformers(serializer, excludeNullObject);
			}
			/*if (handlingAssessmentQuestion) {
				serializer = handleAssessmentQuestionTransformers(serializer);
			}*/

			if (excludes != null) {
				serializer.exclude(excludes);
			}

			try {
				//model = protocolSwitch(model);
				serializedData = deepSerialize ? serializer.deepSerialize(model) : serializer.serialize(model);
				log(model, serializedData);

			} catch (Exception ex) {
				LOGGER.error("serialize: happened to throw exception", ex);
				if (model instanceof ContentSearchResult) {
					LOGGER.error("Serialization failed for resource : " + ((ContentSearchResult) model).getContentId());
				} else if (model instanceof List) {
					List<?> list = (List<?>) model;
					if (list != null && list.size() > 0 && list.get(0) instanceof ContentSearchResult) {
						LOGGER.error("Serialization failed for list resources of size : " + list.size() + " resource : " + ((ContentSearchResult) list.get(0)).getContentId());
					}
				} else {
					LOGGER.error("Serialization failed" + ex);
				}
				throw new MethodFailureException(ex.getMessage());
			}
		} else {
			serializedData = new XStream().toXML(model);
		}
		return serializedData;
	}

	/**
	 * Need to check if we are going to serialize any instance of
	 * AssessmentQuestion. Since question may be stored either in mysql or in
	 * mongo, we need a custom transformer here to make sure that it is properly
	 * serialized.
	 * 
	 * @param model
	 *            Model which will be serialized
	 * @return boolean signifying if we are serializing instance of
	 *         AssessmentQuestion
	 */
	//disabled while removing api-jar dependency
	/*private static boolean willSerializeAssessmentQuestion(Object model) {
		if (model != null) {
			if (model instanceof CollectionItem) {
				ContentSearchResult resource = ((CollectionItem) model).getResource();
				if (resource instanceof AssessmentQuestion) {
					if (((AssessmentQuestion) resource).isQuestionNewGen()) {
						return true;
					}
				}
				return false;
			} else if (model instanceof Collection) {
				Set<CollectionItem> items = ((Collection) model).getCollectionItems();
				if (items != null) {
					for (CollectionItem ci : items) {
						if (ci.getResource() instanceof AssessmentQuestion) {
							if (((AssessmentQuestion) ci.getResource()).isQuestionNewGen()) {
                                return true;
                            }
						}
					}
				}
			}
		}
		return false;
	}*/

	@SuppressWarnings("unchecked")
	private static void log(final Object model, final String data) {
		HttpServletRequest request = null;
		if (RequestContextHolder.getRequestAttributes() != null) {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			if (request != null && request.getMethod() != null && (request.getMethod().equalsIgnoreCase(RequestMethod.POST.name()) || request.getMethod().equalsIgnoreCase(RequestMethod.PUT.name()))) {
				Map<String, Object> payLoadObject = new HashMap<>();
				try {
					if (SessionContextSupport.getLog() != null && SessionContextSupport.getLog().get("payLoadObject") != null) {
						payLoadObject = (Map<String, Object>) SessionContextSupport.getLog().get("payLoadObject");
					}
					try {
						if (data != null) {
							payLoadObject.put("data", data);
						}
					} catch (Exception e) {
						LOGGER.error("Error: " + e);
					}

				} catch (Exception e) {
					LOGGER.error("Error : " + e);
				}

				SessionContextSupport.putLogParameter("payLoadObject", payLoadObject);
			}
		}
	}

	public static String serialize(Object object) {
		return new JSONSerializer().exclude("*.class").transform(new ExcludeNullTransformer(), void.class).deepSerialize(object);
	}



}
