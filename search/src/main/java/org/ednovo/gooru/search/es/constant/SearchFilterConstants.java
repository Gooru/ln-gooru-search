package org.ednovo.gooru.search.es.constant;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class SearchFilterConstants {

	private static Map<String, String> contentSubFormatFilterMap;

	private static Map<String, String> contentSubFormatResponseMap;

	@PostConstruct
	private void init() {
		putContentSubFormatFilterMap();
		putContentSubFormatResponseMap();
	}

	private void putContentSubFormatFilterMap() {
		contentSubFormatFilterMap = new HashMap<>();
		contentSubFormatFilterMap.put("video", "video_resource");
		contentSubFormatFilterMap.put("webpage", "webpage_resource");
		contentSubFormatFilterMap.put("interactive", "interactive_resource");
		contentSubFormatFilterMap.put("image", "image_resource");
		contentSubFormatFilterMap.put("text", "text_resource");
		contentSubFormatFilterMap.put("audio", "audio_resource");
	}

	private void putContentSubFormatResponseMap() {
		contentSubFormatResponseMap = new HashMap<>();
		contentSubFormatResponseMap.put("video_resource", "Video");
		contentSubFormatResponseMap.put("webpage_resource", "Webpage");
		contentSubFormatResponseMap.put("interactive_resource", "Interactive");
		contentSubFormatResponseMap.put("image_resource", "Image");
		contentSubFormatResponseMap.put("text_resource", "Text");
		contentSubFormatResponseMap.put("audio_resource", "Audio");
	}

	public static String getContentSubFormatMapValue(String key) {
		return contentSubFormatFilterMap.containsKey(key.toLowerCase()) ? contentSubFormatFilterMap.get(key.toLowerCase()) : null;
	}

	public static Boolean contentSubFormatKeySetContains(String key) {
		return contentSubFormatFilterMap.containsKey(key.toLowerCase());
	}

	public static String getSubFormatResponseValue(String key) {
		return contentSubFormatResponseMap.containsKey(key) ? contentSubFormatResponseMap.get(key) : null;
	}

	public static Boolean isInSubFormatResponseKeySet(String key) {
		return contentSubFormatResponseMap.containsKey(key);
	}

}
