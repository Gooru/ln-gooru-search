package org.ednovo.gooru.search.es.processor.util;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.thoughtworks.xstream.XStream;

import flexjson.JSONSerializer;

public class JsonSerializer {

	public static final String FORMAT_JSON = "json";

	public static ModelAndView toModelAndView(Object object) {
		ModelAndView jsonmodel = new ModelAndView("rest/model");
		jsonmodel.addObject("model", object);
		return jsonmodel;
	}

	public static ModelAndView toJsonModelAndView(Object model,
			boolean deepSerialize) {
		return toModelAndView(serializeToJson(model, deepSerialize));
	}

	public static ModelAndView toModelAndView(Object obj,
			String type) {
		return toModelAndView(serialize(obj, type));
	}

	public static ModelAndView toModelAndViewWithInFilter(Object obj,
			String type,
			String... includes) {
		return toModelAndView(serialize(obj, type, null, includes));
	}

	public static ModelAndView toModelAndViewWithIoFilter(Object obj,
			String type,
			String[] excludes,
			String... includes) {
		return toModelAndView(serialize(obj, type, excludes, includes));
	}

	// need to improve logic
	public static ModelAndView toModelAndViewWithErrorObject(Object obj,
			String type,
			String entityName,
			Errors errors,
			String[] excludes,
			String... includes) {
		return toModelAndView(serialize(obj, type, excludes, includes));
	}

	/**
	 * @param model
	 * @param excludes
	 * @param includes
	 * @return
	 */
	public static String serializeToJsonWithExcludes(Object model,
			String[] excludes,
			String... includes) {
		return serialize(model, FORMAT_JSON, excludes, includes);
	}

	public static String serialize(Object model,
			String type) {
		return serialize(model, type, null);
	}

	public static String serializeToJson(Object model,
			String... includes) {
		return serialize(model, FORMAT_JSON, null, includes);
	}

	public static JSONObject serializeToJsonObjectWithExcludes(Object model,
			String[] excludes,
			String... includes) throws Exception {
		return new JSONObject(serialize(model, FORMAT_JSON, excludes, includes));
	}

	public static JSONObject serializeToJsonObject(Object model,
			String... includes) throws Exception {
		return new JSONObject(serialize(model, FORMAT_JSON, null, includes));
	}

	public static String serializeToJsonWithExcludes(Object model,
			String[] excludes,
			boolean deepSerialize,
			String... includes) {
		return serialize(model, FORMAT_JSON, excludes, deepSerialize, includes);
	}

	public static String serializeToJson(Object model,
			boolean deepSerialize,
			String... includes) {
		return serialize(model, FORMAT_JSON, null, deepSerialize, includes);
	}

	public static JSONObject serializeToJsonObjectWithExcludes(Object model,
			String[] excludes,
			boolean deepSerialize,
			String... includes) throws Exception {
		return new JSONObject(serialize(model, FORMAT_JSON, excludes, deepSerialize, includes));
	}

	public static JSONObject serializeToJsonObject(Object model,
			boolean deepSerialize,
			String... includes) throws Exception {
		return new JSONObject(serialize(model, FORMAT_JSON, null, deepSerialize, includes));
	}

	/**
	 * @param model
	 * @param type
	 * @param excludes
	 * @param includes
	 * @return
	 */
	public static String serialize(Object model,
			String type,
			String[] excludes,
			boolean deepSerialize,
			String... includes) {
		if (model == null) {
			return "";
		}
		String serializedData = null;
		JSONSerializer serializer = new JSONSerializer();

		if (type == null || type.equals("json")) {

			if (includes != null) {
				includes = (String[]) ArrayUtils.add(includes, "*.contentPermissions");
				serializer.include(includes);
			} else {
				serializer.include("*.contentPermissions");
			}

			if (excludes != null) {
				serializer.exclude(excludes);
			}

			try {

				serializedData = deepSerialize ? serializer.deepSerialize(model) : serializer.serialize(model);

			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}

		} else {
			serializedData = new XStream().toXML(model);
		}
		return serializedData;
	}

	/**
	 * @param model
	 * @param type
	 * @param excludes
	 * @param includes
	 * @return
	 */
	public static String serialize(Object model,
			String type,
			String[] excludes,
			String... includes) {
		return serialize(model, type, excludes, false, includes);
	}

}

