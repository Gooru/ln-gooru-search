/**
 * 
 */
package org.ednovo.gooru.search.es.constant;

/**
 * Contains all the search setting types and the default value of the settings.
 * 
 * @author SearchTeam
 * 
 */
public enum SearchSettingType {
	//TODO move these to a property file
	S_VERSION("setting.version", "2463573737435353"),
	S_PROFILE("search.profile", "default"),
	S_ES_POINT("search.elasticsearch.point", "http://localhost:9200"),
	S_ES_POINT_USERNAME("search.elasticsearch.point.username", ""),
	S_ES_POINT_PASSWORD("search.elasticsearch.point.password", ""),
	S_ES_INDEX_PREFIX("search.elasticsearch.index.prefix", "gooru_local_"),
	S_ES_INDEX_SUFFIX("search.elasticsearch.index.suffix", "_v2"),

	S_WORDNET_PATH("search.wordnet.path", "/opt/wordnet/dict/"),

	S_RESOURCE_FIELD_COLLECTION_TAXONOMY_SUBJECT_LABEL_BOOST("search.resource.field.collection.taxonomy.subject.label.boost", 1.1F),
	S_RESOURCE_FIELD_COLLECTION_TAXONOMY_COURSE_LABEL_BOOST("search.resource.field.collection.taxonomy.course.label.boost", 1.4F),
	S_RESOURCE_FIELD_COLLECTION_TAXONOMY_UNIT_LABEL_BOOST("search.resource.field.collection.taxonomy.unit.label.boost", 1.6F),
	S_RESOURCE_FIELD_COLLECTION_TAXONOMY_TOPIC_LABEL_BOOST("search.resource.field.collection.taxonomy.topic.label.boost", 1.8F),
	S_RESOURCE_FIELD_COLLECTION_TAXONOMY_LESSON_LABEL_BOOST("search.resource.field.collection.taxonomy.lesson.label.boost", 2.0F),
	S_RESOURCE_FIELD_TITLE_BOOST("search.resource.field.title.boost", 1.1F),
	S_RESOURCE_FIELD_TAGS_BOOST("search.resource.field.tags.boost", 1.1F),
	S_RESOURCE_FIELD_DESCRIPTION_BOOST("search.resource.field.description.boost", 1.1F),
	S_RESOURCE_FIELD_TAXONOMY_SUBJECT_LABEL_BOOST("search.resource.field.taxonomy.subject.label.boost", 1.1F),
	S_RESOURCE_FIELD_TAXONOMY_COURSE_LABEL_BOOST("search.resource.field.taxonomy.course.label.boost", 1.4F),
	S_RESOURCE_FIELD_TAXONOMY_UNIT_LABEL_BOOST("search.resource.field.taxonomy.unit.label.boost", 1.6F),
	S_RESOURCE_FIELD_TAXONOMY_TOPIC_LABEL_BOOST("search.resource.field.taxonomy.topic.label.boost", 1.8F),
	S_RESOURCE_FIELD_TAXONOMY_LESSON_LABEL_BOOST("search.resource.field.taxonomy.lesson.label.boost", 2.0F),

	S_RESOURCE_QUERY_USERQUERY_BOOST("search.resource.query.user_query.boost", 1.0F),
	S_RESOURCE_QUERY_EXAPNSION_TAXONOMY_LABEL_BOOST("search.resource.query.expansion.taxonomy.label.boost", 1.0F),
	S_RESOURCE_USER_PROFILE_GRADE_BOOST("search.resource.user.profile.grade.boost", 1.0F),
	S_RESOURCE_USER_PROFILE_SUBJECT_BOOST("search.resource.user.profile.subject.boost", 1.0F),
	S_RESOURCE_USER_PREFERRED_CATEGORY_BOOST("search.resource.user.preferred.category.boost", 1.0F),
	S_RESOURCE_QUERY_EXPANSION("search.resource.query.expansion", "false"),
	S_RESOURCE_QUERY_SCORE("search.resource.query.score", "_score * ((log(doc['statistics.voteUp'].value  + 2) * 1.0))"),
	S_RESOURCE_QUERY_MINSCORE("search.resource.query.min_score", 0.1F),
	//S_RESOURCE_FIELDS("search.resource.fields", "activeStatus,addDate,answer,answerOptionCount,assetURI,attribution,attributionSuggestion,batchId,category,classplanContent,recordSource,resourceDomainName,resourceSourceId,resourceSourceName,resourceSourceType,resourceType,resourceUrlStatus,resourceUrlStatusId,segmentConcepts,segmentTitles,sharing,siteName,size,source,sourceName,sourceType,standards,statusIsBroken,statusIsFrameBreaker,subscriberCount,tags,taxonomyCourse,taxonomyDataSet,taxonomyGrade,taxonomyLesson,taxonomySubject,taxonomyTopic,taxonomyUnit,text,thumbnail,timeToCompleteInSec,title,type,typeEscaped,updatedCustomFields,url,contentId,distinguish,description,folder,grade,isFeatured"),
	S_RESOURCE_FIELDS("search.resource.fields", "id,contentFormat,url,title,description,thumbnail,createdAt,updatedAt,shortTitle,narration,publishStatus,collectionId,visibleOnProfile,originalCreator,creator,contentSubFormat,collectionIds,collectionTitles,question,metadata,taxonomy"),
	//S_RESOURCE_QUERY_FIELDS("search.resource.query.fields", "_all,classplanContent,lesson,segmentTitles,instancesTitles,description,text,instancesDescriptions,instancesNaratives,tags,title,taxonomy.course.label, taxonomy.subject.label,taxonomy.lesson.label,taxonomy.unit.label,taxonomy.topic.label,collectionTaxonomyCourses,collectionTaxonomyUnits,collectionTaxonomyTopics,collectionTaxonomySubjects,collectionTaxonomyLessons,resourceSource.attribution"),
	S_RESOURCE_QUERY_FIELDS("search.resource.query.fields", "_all,description,text,tags,title,shortTitle,narration,collectionTitles,taxonomy.course.label,taxonomy.subject.label,taxonomy.lesson.label,taxonomy.unit.label,taxonomy.topic.label,resourceSource.attribution"),
	S_RESOURCE_QUERY_NATIVESCORE_ACTIVE("search.resource.query.nativescore.enablefallback","true"),
	S_RESOURCE_QUERY_NATIVESCORE_SCRIPT("search.resource.query.nativescore.script", "gooru-v2"),
	S_RESOURCE_QUERY_NATIVESCORE_LANG("search.resource.query.nativescore.lang","native"),
	S_RESOURCE_QUERY_NATIVESCORE_DEBUG("search.resource.query.nativescore.debug","false"),
	S_RESOURCE_QUERY_NATIVESCORE_FIELD("search.resource.query.nativescore.field","statistics.preComputedWeight"),
	S_RESOURCE_USER_SUBJECT_PROFICIENCY_BOOST("search.resource.user.topic.proficiency.boost", 1.0F),
	S_RESOURCE_USER_COURSE_PROFICIENCY_BOOST("search.resource.user.topic.proficiency.boost", 1.0F),
	S_RESOURCE_USER_UNIT_PROFICIENCY_BOOST("search.resource.user.topic.proficiency.boost", 1.0F),
	S_RESOURCE_USER_TOPIC_PROFICIENCY_BOOST("search.resource.user.topic.proficiency.boost", 1.0F),
	S_RESOURCE_USER_LESSON_PROFICIENCY_BOOST("search.resource.user.lesson.proficiency.boost", 1.0F),
	S_RESOURCE_USER_CONCEPT_PROFICIENCY_BOOST("search.resource.user.concept.proficiency.boost", 1.0F),
	S_RESOURCE_MULTIMATCH_MINSHOULDMATCH("search.resource.multimatch.min_should_match", "60%"),
	S_RESOURCE_MULTIMATCH_CUTOFF_FREQUENCY("search.resource.multimatch.cutoff_frequency", 0.001F),
	S_RESOURCE_SCORE_DEMOTEVALUE("search.resource.score.demote_value", 5.0F),

	
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_VIDEO("search.resource.query.min_score.category.video", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_INTERACTIVE("search.resource.query.min_score.category.interactive", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_WEBSITE("search.resource.query.min_score.category.website", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_EXAM("search.resource.query.min_score.category.exam", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_TEXTBOOK("search.resource.query.min_score.category.textbook", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_HANDOUT("search.resource.query.min_score.category.handout", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_SLIDE("search.resource.query.min_score.category.slide", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_LESSON("search.resource.query.min_score.category.lesson", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_QUESTION("search.resource.query.min_score.category.question", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_IMAGE("search.resource.query.min_score.category.image", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_CHALLENGE("search.resource.query.min_score.category.challenge", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_BOOK("search.resource.query.min_score.category.book", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_AUDIO("search.resource.query.min_score.category.audio", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_CATEGORY_MOOC("search.resource.query.min_score.category.image", 0.1F),
	
	S_RESOURCE_QUERY_MINSCORE_DEFAULT_CATEGORY("search.resource.query.min_score.default.category",0.1F),
    S_RESOURCE_QUERY_SPELLCHECKER_FIELD("search.resource.query.spellchecker.field", "customSpellCheckerSuggest"),
    S_RESOURCE_QUERY_SPELLCHECKER_MULTI_WORDS_ACTIVE("search.resource.query.spellchecker.multi_words.active", "true"),
    S_RESOURCE_QUERY_SPELLCHECKER_TAXONOMY_FIELD("search.resource.query.spellchecker.taxonomy.field", "taxonomy.standards"),
    S_RESOURCE_QUERY_SPELLCHECKER_ACTIVE("search.resource.query.spellchecker.active", "true"),
    S_RESOURCE_QUERY_SPELLCHECKER_SCORE_THRESHOLD("search.resource.query.spellchecker.score.threshold", "0.6"),
    S_RESOURCE_QUERY_SPELLCHECKER_FREQ_THRESHOLD("search.resource.query.spellchecker.frequency.threshold", "10"),


	S_COLLECTION_FIELD_CLASSPLAN_CONTENT_BOOST("search.collection.field.classplan.content.boost", 1.1F),
	S_COLLECTION_FIELD_SEGMENT_TITLE_BOOST("search.collection.field.segment.title.boost", 1.4F),
	S_COLLECTION_FIELD_RESOURCE_TITLE_BOOST("search.collection.field.resource.title.boost", 1.4F),
	S_COLLECTION_FIELD_QUIZ_GOALS_BOOST("search.collection.field.quiz.goals.boost", 1.6F),
	S_COLLECTION_FIELD_TITLE_BOOST("search.collection.field.title.boost", 1.1F),
	S_COLLECTION_FIELD_TAGS_BOOST("search.collection.field.tags.boost", 1.1F),
	S_COLLECTION_FIELD_DESCRIPTION_BOOST("search.collection.field.description.boost", 1.1F),
	S_COLLECTION_FIELD_TAXONOMY_SUBJECT_LABEL_BOOST("search.collection.field.taxonomy.subject.label.boost", 1.1F),
	S_COLLECTION_FIELD_TAXONOMY_COURSE_LABEL_BOOST("search.collection.field.taxonomy.course.label.boost", 1.4F),
	S_COLLECTION_FIELD_TAXONOMY_UNIT_LABEL_BOOST("search.collection.field.taxonomy.unit.label.boost", 1.6F),
	S_COLLECTION_FIELD_TAXONOMY_TOPIC_LABEL_BOOST("search.collection.field.taxonomy.topic.label.boost", 1.8F),
	S_COLLECTION_FIELD_TAXONOMY_LESSON_LABEL_BOOST("search.collection.field.taxonomy.lesson.label.boost", 2.0F),
	S_COLLECTION_FIELDS("search.collection.fields", "id,publishStatus,learningObjective,description,contentFormat,grade,title,collaboratorIds,thumbnail,createdAt,updatedAt,modifierId,metadata,statistics,owner,creator,originalCreator,taxonomy,resourceTitles,resourceIds,collectionContents"),
	S_COLLECTION_QUERY_FIELDS("search.collection.query.fields", "_all,taxonomyDataSet,username,creatorname,resourceTitles,learningObjective,title,taxonomyCourse,taxonomySubject,taxonomyLesson,classplanContent,taxonomyUnit,taxonomyTopic"),

	S_COLLECTION_QUERY_USERQUERY_BOOST("search.collection.query.user_query.boost", 1.0F),
	S_COLLECTION_QUERY_EXAPNSION_TAXONOMY_LABEL_BOOST("search.collection.query.expansion.taxonomy.label.boost", 1.0F),
	S_COLLECTION_USER_PROFILE_GRADE_BOOST("search.collection.user.profile.grade.boost", 1.0F),
	S_COLLECTION_USER_PROFILE_SUBJECT_BOOST("search.collection.user.profile.subject.boost", 1.0F),
	S_COLLECTION_QUERY_EXPANSION("search.collection.query.expansion", "false"),
	S_COLLECTION_QUERY_SCORE("search.collection.query.score", "_score * ((log(doc['voteUp'].value  + 2) * 1.0))"),
	S_COLLECTION_QUERY_MINSCORE("search.collection.query.min_score", 0.1F),
    S_COLLECTION_QUERY_SPELLCHECKER_FIELD("search.collection.query.spellchecker.field", "customSpellCheckerSuggest"),
    S_COLLECTION_QUERY_SPELLCHECKER_MULTI_WORDS_ACTIVE("search.collection.query.spellchecker.multi_words.active", "true"),
    S_COLLECTION_QUERY_SPELLCHECKER_TAXONOMY_FIELD("search.collection.query.spellchecker.taxonomy.field", "taxonomy.standards"),
    S_COLLECTION_QUERY_SPELLCHECKER_ACTIVE("search.collection.query.spellchecker.active", "true"),

	S_SCOLLECTION_FIELD__BOOST("search.scollection.query.user_query.boost", 1.0F),
	S_SCOLLECTION_FIELD_TITLE_BOOST("search.scollection.field.title.boost", 10.0F),
	S_SCOLLECTION_FIELD_TAGS_BOOST("search.scollection.field.tags.boost", 1.1F),
	S_SCOLLECTION_FIELD_DESCRIPTION_BOOST("search.scollection.field.description.boost", 1.1F),
	S_SCOLLECTION_FIELD_TAXONOMY_SUBJECT_LABEL_BOOST("search.scollection.field.taxonomy.subject.label.boost", 1.1F),
	S_SCOLLECTION_FIELD_TAXONOMY_COURSE_LABEL_BOOST("search.scollection.field.taxonomy.course.label.boost", 1.4F),
	S_SCOLLECTION_FIELD_TAXONOMY_UNIT_LABEL_BOOST("search.scollection.field.taxonomy.unit.label.boost", 1.6F),
	S_SCOLLECTION_FIELD_TAXONOMY_TOPIC_LABEL_BOOST("search.scollection.field.taxonomy.topic.label.boost", 1.8F),
	S_SCOLLECTION_FIELD_TAXONOMY_LESSON_LABEL_BOOST("search.scollection.field.taxonomy.lesson.label.boost", 2.0F),

	S_SCOLLECTION_QUERY_USERQUERY_BOOST("search.scollection.query.user_query.boost", 3.0F),
	S_SCOLLECTION_QUERY_EXAPNSION_TAXONOMY_LABEL_BOOST("search.scollection.query.expansion.taxonomy.label.boost", 1.0F),
	S_SCOLLECTION_USER_PROFILE_GRADE_BOOST("search.scollection.user.profile.grade.boost", 1.0F),
	S_SCOLLECTION_USER_PROFILE_SUBJECT_BOOST("search.scollection.user.profile.subject.boost", 1.0F),
	S_SCOLLECTION_QUERY_EXPANSION("search.scollection.query.expansion", "false"),
	//S_SCOLLECTION_QUERY_SCORE("search.scollection.query.score", "_score * ( (log(doc['collectionItemCount'].value+2) * 3.0) + (log(doc['voteUp'].value+2) * 2.0)+ (log(doc['subscriberCount'].value+2) * 1.5)+ (log(doc['viewsCount'].value+2) * 0.5)+ (log(doc['voteDown'].value+2) * -1.6)+ (log(doc['hasNoDescription'].value+1) * -1.3))"),
	S_SCOLLECTION_QUERY_SCORE("search.scollection.query.score", "_score * doc['statistics.preComputedWeight'].value"),
	S_SCOLLECTION_QUERY_MINSCORE("search.scollection.query.min_score", 0.1F),
	/*S_SCOLLECTION_FIELDS("search.scollection.fields", "id, contentId, sharing, goals, description, category, batchId, grade, resultUid, title, collaborators, folder, thumbnail, type, lastModified, addDate, network, creator, owner, statistics, socialData, taxonomy, segment, distinguish, isFeatured, estimatedTime, assetURI, collectionType, narrationLink, notes, keyPoints, language, collectionItem, resourceGooruOIds, socialData, lastModifiedUserUid, description, category, owner, organization"),
	S_SCOLLECTION_QUERY_FIELDS("search.scollection.query.fields", "_all,taxonomyDataSet,title,taxonomyCourse,taxonomySubject,taxonomyLesson,taxonomyUnit,learningObjectives,taxonomyTopic"),
	*/
	S_SCOLLECTION_FIELDS("search.scollection.fields", "id,publishStatus,learningObjective,description,contentFormat,grade,title,collaboratorIds,thumbnail,createdAt,updatedAt,modifierId,metadata,statistics,owner,creator,originalCreator,taxonomy,resourceTitles,resourceIds,collectionContents"),
	S_SCOLLECTION_QUERY_FIELDS("search.scollection.query.fields", "_all,taxonomyDataSet,username,creatorname,resourceTitles,learningObjective,title,taxonomyCourse,taxonomySubject,taxonomyLesson,classplanContent,taxonomyUnit,taxonomyTopic"),
	S_SCOLLECTION_USER_SUBJECT_PROFICIENCY_BOOST("search.scollection.user.topic.proficiency.boost", 1.0F),
	S_SCOLLECTION_USER_COURSE_PROFICIENCY_BOOST("search.scollection.user.topic.proficiency.boost", 1.0F),
	S_SCOLLECTION_USER_UNIT_PROFICIENCY_BOOST("search.scollection.user.topic.proficiency.boost", 1.0F),
	S_SCOLLECTION_USER_TOPIC_PROFICIENCY_BOOST("search.scollection.user.topic.proficiency.boost", 1.0F),
	S_SCOLLECTION_USER_LESSON_PROFICIENCY_BOOST("search.scollection.user.topic.proficiency.boost", 1.0F),
	S_SCOLLECTION_USER_CONCEPT_PROFICIENCY_BOOST("search.scollection.user.topic.proficiency.boost", 1.0F),
	S_SCOLLECTION_SCORE_DEMOTEVALUE("search.scollection.score.demote_value", 5.0F),
    S_SCOLLECTION_QUERY_SPELLCHECKER_FIELD("search.scollection.query.spellchecker.field", "customSpellCheckerSuggest"),
    S_SCOLLECTION_QUERY_SPELLCHECKER_MULTI_WORDS_ACTIVE("search.scollection.query.spellchecker.multi_words.active", "true"),
    S_SCOLLECTION_QUERY_SPELLCHECKER_TAXONOMY_FIELD("search.scollection.query.spellchecker.taxonomy.field", "taxonomy.standards"),
    S_SCOLLECTION_QUERY_SPELLCHECKER_ACTIVE("search.scollection.query.spellchecker.active", "true"),
	S_SCOLLECTION_QUERY_NATIVESCORE_ACTIVE("search.scollection.query.nativescore.enablefallback","true"),
	S_SCOLLECTION_QUERY_NATIVESCORE_SCRIPT("search.scollection.query.nativescore.script", "gooru-v2"),
	S_SCOLLECTION_QUERY_NATIVESCORE_LANG("search.scollection.query.nativescore.lang","not-native"),
	S_SCOLLECTION_QUERY_NATIVESCORE_DEBUG("search.scollection.query.nativescore.debug","false"),
	S_SCOLLECTION_QUERY_NATIVESCORE_FIELD("search.scollection.query.nativescore.field","statistics.preComputedWeight"),

    S_QUIZ_FIELD_SEGMENT_TITLE_BOOST("search.quiz.field.segment.title.boost", 1.0F),
	S_QUIZ_FIELD_LEARNING_OBJECTIVES_BOOST("search.quiz.field.learning_objectives.boost", 1.0F),
	S_QUIZ_FIELD_TITLE_BOOST("search.quiz.field.title.boost", 1.1F),
	S_QUIZ_FIELD_TAGS_BOOST("search.quiz.field.tags.boost", 1.1F),
	S_QUIZ_FIELD_DESCRIPTION_BOOST("search.quiz.field.description.boost", 1.1F),
	S_QUIZ_FIELD_TAXONOMY_SUBJECT_LABEL_BOOST("search.quiz.field.taxonomy.subject.label.boost", 1.1F),
	S_QUIZ_FIELD_TAXONOMY_COURSE_LABEL_BOOST("search.quiz.field.taxonomy.course.label.boost", 1.4F),
	S_QUIZ_FIELD_TAXONOMY_UNIT_LABEL_BOOST("search.quiz.field.taxonomy.unit.label.boost", 1.6F),
	S_QUIZ_FIELD_TAXONOMY_TOPIC_LABEL_BOOST("search.quiz.field.taxonomy.topic.label.boost", 1.8F),
	S_QUIZ_FIELD_TAXONOMY_LESSON_LABEL_BOOST("search.quiz.field.taxonomy.lesson.label.boost", 2.0F),
	S_QUIZ_FIELDS("search.quiz.fields", "gooruOId,contentId,sharing,goals,description,category,batchId,grade,title,collaborators,folder,thumbnail,type,lastModified,addDate,network,creator,owner,statistics,socialData,taxonomy,segment,distinguish,isFeatured,assetURI,organization,collaboratorEmails,name,learningObjectives,quizCollection,vocabulary,collectionGooruOid,assessmentGooruOid"),
	S_QUIZ_QUERY_FIELDS("search.quiz.query.fields", "_all,taxonomyDataSet,title,taxonomyCourse,segmentTitles,taxonomySubject,taxonomyLesson,taxonomyUnit,learningObjectives,taxonomyTopic"),

	S_QUIZ_QUERY_USERQUERY_BOOST("search.quiz.query.user_query.boost", 3.0F),
	S_QUIZ_QUERY_EXAPNSION_TAXONOMY_LABEL_BOOST("search.quiz.query.expansion.taxonomy.label.boost", 1.0F),
	S_QUIZ_USER_PROFILE_GRADE_BOOST("search.quiz.user.profile.grade.boost", 1.0F),
	S_QUIZ_USER_PROFILE_SUBJECT_BOOST("search.quiz.user.profile.subject.boost", 1.0F),
	S_QUIZ_QUERY_EXPANSION("search.quiz.query.expansion", "false"),
	S_QUIZ_QUERY_SCORE("search.quiz.query.score", "_score"),
	S_QUIZ_QUERY_MINSCORE("search.quiz.query.min_score", 0.1F),

	S_TAXONOMY_FIELDS("search.taxonomy.fields", "id,codeId,codeUId,label,code,displayOrder,parentId,typeId,description,depth,rootNodeId,codeImage,s3UploadFlag"),
	S_TAXONOMY_QUERY_FIELDS("search.taxonomy.query.fields", "codeQuery,label"),
	S_TAXONOMY_SUB_LEVEL_FIELDS("search.taxonomy_sub_level.fields", "nextLevelLabels"),
	S_TAXONOMY_SUB_LEVEL_QUERY_FIELDS("search.taxonomy_sub_level.query.fields", "label"),
	
	S_SKILLS_FIELDS("search.skills.fields","label,codeId"),

	
	S_USER_FIELDS("search.user.fields", "userID,firstName,lastName,userName,gooruUID,accountId,grade,network,emailID,createdOn,roleSet,confirmStatus,userProfileImage,lastLogin,isDeleted,aboutMe,active,accountTypeId,districtCode,districtId,districtName,stateProvince,userType"),
	S_USER_QUERY_FIELDS("search.user.query.fields", "_all"),

	S_QUESTION_FIELDS("search.question.fields", "resultUid,gooruOId,owner,creator,sharing,type,isFeatured,distinguish,collaborators,grade,taxonomy,question,contentId,assetURI,lastModified,addDate,socialData,statistics,organization,category"),
	S_QUESTION_QUERY_FIELDS("search.question.query.fields", "_all"),

	S_LIBRARY_FIELDS("search.library.fields", "collections,quizzes,simple_collections,simple_quizzes"),
	S_LIBRARY_QUERY_FIELDS("search.library.query.fields", "_all"),

	S_DICTIONARY_FIELDS("search.dictionary.fields", "type,word,difinition"),
	S_DICTIONARY_QUERY_FIELDS("search.dictionary.query.fields", "_all"),

	S_SEARCHQUERY_FIELDS("search.searchquery.fields", "searchquery"),
	S_SEARCHQUERY_QUERY_FIELDS("search.searchquery.query.fields", "_all"),
	
	S_AUTOCOMPLETE_FIELDS("search.searchquery.fields", "searchquery"),
	S_AUTOCOMPLETE_QUERY_FIELDS("search.searchquery.query.fields", "_all"),
		
	S_CONTRIBUTOR_FIELDS("search.contributor.fields", "userName,organizationName,userProfileImage,publishername,aggregatorname"),
	S_CONTRIBUTOR_QUERY_FIELDS("search.contributor.query.fields", "userName,organizationName,publishername,aggregatorname"),

	S_ATTRIBUTION_FIELDS("search.attribution.fields", "resourceSource.attribution,customField.customFields.cfHost"),
	S_ATTRIBUTION_QUERY_FIELDS("search.attribution.query.fields", "resourceSource.attributionSuggestion,customField.customFields.cfHost"),
 //publisher & aggregator
	S_PUBLISHER_FIELDS("search.publisher.fields","publishername"),
	S_PUBLISHER_QUERY_FIELDS("search.publisher.query.fields","publishername"),
	

	S_AGGREGATOR_FIELDS("search.aggregator.fields","aggregatorname"),
	S_AGGREGATOR_QUERY_FIELDS("search.aggregator.query.fields","aggregatorname"),
	
	S_RESOURCE_QUERY_ANALYZER("search.resource.query.analyzer", "standard"),
	S_COLLECTION_QUERY_ANALYZER("search.collection.query.analyzer", "standard"),
	S_SCOLLECTION_QUERY_ANALYZER("search.scollection.query.analyzer", "gooru_kstem"),
	S_QUIZ_QUERY_ANALYZER("search.quiz.query.analyzer", "snowball"),
	S_QUESTION_QUERY_ANALYZER("search.question.query.analyzer", "snowball"),
	
	UPDATE_RESOURCE_RESOCRING("update.resource.rescoring", "false"),
	UPDATE_SCOLLECTION_RESOCRING("update.scollection.rescoring", "false"),
	
	S_RESOURCE_QUERY_EMAIL_UUID_ANALYZER("search.resource.query.email.uuid.analyzer", "standard"),
	S_COLLECTION_QUERY_EMAIL_UUID_ANALYZER("search.collection.query.email.uuid.analyzer", "standard"),
	S_SCOLLECTION_QUERY_EMAIL_UUID_ANALYZER("search.scollection.query.email.uuid.analyzer", "standard"),
	S_QUIZ_QUERY_EMAIL_UUID_ANALYZER("search.quiz.query.email.uuid.analyzer", "standard"),
	S_QUESTION_QUERY_EMAIL_UUID_ANALYZER("search.question.query.email.uuid.analyzer", "standard"),
	
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_WEBPAGE("search.resource.query.min_score.resourceformat.webpage", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_QUESTION("search.resource.query.min_score.resourceformat.question", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_VIDEO("search.resource.query.min_score.resourceformat.video", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_INTERACTIVE("search.resource.query.min_score.resourceformat.interactive", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_IMAGE("search.resource.query.min_score.resourceformat.image", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_TEXT("search.resource.query.min_score.resourceformat.text", 0.1F),
	S_RESOURCE_QUERY_MINSCORE_RESOURCE_FORMAT_AUDIO("search.resource.query.min_score.resourceformat.audio", 0.1F),
	
	S_RESOURCE_QUERY_MINSCORE_DEFAULT_RESOURCE_FORMAT("search.resource.query.min_score.default.resourceformat",0.1F),
	
	S_RESOURCE_USER_PREFERRED_RESOURCE_FORMAT_BOOST("search.resource.user.preferred.resourceformat.boost", 1.0F),
	
	S_SCOLLECTION_ITEM_FIELDS("search.scollection.item.fields", "gooruOId,contentId,sharing,goals,batchId,grade,resultUid,title,collaborators,folder,thumbnail,type,lastModified,addDate,network,creator,statistics,taxonomy,segment,distinguish,isFeatured,estimatedTime,assetURI,collectionType,narrationLink,notes,keyPoints,language,resourceGooruOIds,socialData,lastModifiedUserUid,description,category,owner,organization,collectionItemCount,collectionItem"),
	
	S_SCOLLECTION_ITEM_SPECIFIC_FIELDS("search.scollection.item.specificfields","gooruOId,contentId,sharing,goals,batchId,grade,resultUid,title,collaborators,folder,thumbnail,type,lastModified,addDate,network,creator,statistics,taxonomy,segment,distinguish,isFeatured,estimatedTime,assetURI,collectionType,narrationLink,notes,keyPoints,language,resourceGooruOIds,socialData,lastModifiedUserUid,description,category,owner,organization,collectionItemCount,license,ratings,questionCount,resourceCount,languageObjective,libraryName,libraryNameAnalyzed,contentTags,averageTimeSpent,collectionItem.gooruOid,collectionItem.title,collectionItem.discription,collectionItem.thumbnail,collectionItem.resourceGooruOIds,collectionItem.category,collectionItem.resourceFormat,skill"),
	
	S_STANDARD_QUERY_FIELDS("search.standard.query.fields", "codeQuery,label"),
	
	S_SCHOOLDISTRICT_FIELDS("search.schooldistrict.fields","districtCode,districtId,districtName,school,stateProvince"),
	
	S_FILTER_ALIAS_SHARING("filter-alias@sharing", "publishStatus"),
	//S_FILTER_ALIAS_TYPE("filter-alias@type", "contentFormat"),
	S_FILTER_ALIAS_CATEGORY("filter-alias@category", "contentSubFormat"),
	S_FILTER_ALIAS_QUESTION_TYPE("filter-alias@questionType", "contentSubFormat"),
	S_FILTER_ALIAS_RESOURCE_FORMAT("filter-alias@resourceFormat", "contentSubFormat"),
	S_FILTER_ALIAS_COLLECTION_TYPE("filter-alias@collectionType", "contentFormat"),
	S_FILTER_ALIAS_STANDARD("filter-alias@standard", "taxonomy.standards"),
	S_FILTER_ALAIS_UNIT("filter-alias@unit","taxonomy.unit@taxonomy.unit.codeId"),
	S_FILTER_ALAIS_COURSE_NAME("filter-alias@courseName","taxonomy.course@taxonomy.course.label"),
	S_FILTER_ALAIS_HAS_TAXONOMY("filter-alias@hasTaxonomy","taxonomy.hasNoTaxonomy"),
	S_FILTER_ALAIS_CREATOR_ID("filter-alias@creatorUId","creator.userId"),
	S_FILTER_ALAIS_CREATOR_USERNAME("filter-alias@creatorUsername","creator.usernameDisplay"),
	S_FILTER_ALAIS_OWNER_USERNAME("filter-alias@ownerUsername","owner.usernameDisplay"),
	S_FILTER_ALAIS_COURSE("filter-alias@course","taxonomy.course@taxonomy.course.codeId"),
	S_FILTER_ALAIS_OWNER_EMAILID("filter-alias@ownerEmailId","owner.emailId"),
	S_FILTER_ALAIS_HAS_NO_DESCRIPTION("filter-alias@hasNoDescription","statistics.hasNoDescription"),
	S_FILTER_ALAIS_CREATOR("filter-alias@creator","creator.firstName|creator.lastName|creator.usernameDisplay|creator.fullName"),
	S_FILTER_ALAIS_HAS_NO_STANDARD("filter-alias@hasNoStandard","taxonomy.hasNoStandard"),
	S_FILTER_ALAIS_HAS_STANDARD("filter-alias@hasStandard","taxonomy.hasStandard"),
	S_FILTER_DETECTION_URL("filter-detection@url","url~~((?:https?|ftp|mailto):?((//)?|www.)(?:[-a-zA-Z0-9]+.)*[-a-zA-Z0-9]+.*)"),
    S_FILTER_ALAIS_OWNER("filter-alias@owner","owner.firstName|owner.lastName|owner.usernameDisplay|owner.fullName"),
    S_FILTER_SPLITBY_APPROX("filter-splitBy@approx","subjectName,courseName,topicName,unitName,lessonName"),
    S_FILTER_SKIPWORDS_ALL("filter-skipWords@all","contentSubFormat,contentFormat,category,resourceFormat"),
    S_FILTER_LOWERCASE("filter-case@lowercase","contentSubFormat,contentFormat,standard,subjectName,courseName,topicName,unitName,lessonName,mediaType,attribution,creatorEmailId,ownerEmailId,creatorUsername,ownerUsername,owner,creator,category,resourceFormat,instructional,title,questionType,collectionType,publisher,aggregator"),
    S_RESOURCE_FIELD_TITLESTANDARD_BOOST("search.resource.field.titleStandard.boost",5.0F),
    S_RESOURCE_RESCORE_SCRIPT_LANG("search.resource.rescore.script.lang","groovy"),
    S_RESOURCE_FIELD_TITLE_STANDARD_BOOST("search.resource.field.title.standard.boost",5.0F),
	;

	String name;
	Object defaultValue;

	private SearchSettingType(String name,
			Object defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public String getDefaultValueAsString() {
		return defaultValue.toString();
	}
	
	public static SearchSettingType getType(String typeName) {
		try {
			return SearchSettingType.valueOf(typeName);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static SearchSettingType getCategoryType(String typeName) {
		try {
			if( SearchSettingType.getType(typeName) != null){
				return SearchSettingType.valueOf(typeName);
			}else{
				return SearchSettingType.valueOf("S_RESOURCE_QUERY_MINSCORE_DEFAULT_CATEGORY");
			}
		} catch (Exception ex) {
			return null;
		}
	}

	public static SearchSettingType getResourceFormatType(String typeName) {
		try {
			if( SearchSettingType.getType(typeName) != null){
				return SearchSettingType.valueOf(typeName);
			}else{
				return SearchSettingType.valueOf("S_RESOURCE_QUERY_MINSCORE_DEFAULT_RESOURCE_FORMAT");
			}
		} catch (Exception ex) {
			return null;
		}
	}
}
