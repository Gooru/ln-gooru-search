package org.ednovo.gooru.suggest.v3.data.provider.service;

import org.ednovo.gooru.suggest.v3.data.provider.model.LessonDataProviderCriteria;
import org.ednovo.gooru.suggest.v3.model.LessonContextData;
import org.json.JSONException;

public interface LessonDataProviderService {

	public LessonContextData getLessonData(LessonDataProviderCriteria lessonDataProviderCriteria) throws JSONException;

}
