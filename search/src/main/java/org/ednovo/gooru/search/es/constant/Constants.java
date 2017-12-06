package org.ednovo.gooru.search.es.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public interface Constants {

	// ~ Static fields/initializers =============================================

	public static final String TYPE_STANDARD = "standard";

	public static final String SINGLE = "single";

	public static final String STANDARD_CODE = "standardCode";

	public static final String TYPE_TAXONOMY = "taxonomy";

	public static final String TYPE_ATTRIBUTION = "attribution";

	public static final String TYPE_RESOURCE = "resource";

	public static final String QUERY_TYPE = "queryType";

	public static final String MULTI_CATEGORY = "multiCategory";

	public static final String MULTI_QUERY = "multiQuery";

	public static final String COLLECTION_SUGGEST = "collection-suggest";

	public static final String QUIZ_SUGGEST = "quiz-suggest";

	public static final String TYPE_COLLECTION = "collection";

	public static final String TYPE_ASSESSMENT = "assessment";

	public static final String TYPE_QUIZ = "quiz";

	public static final String TYPE_PUBLISHER = "publisher";

	public static final String TYPE_AGGREGATOR = "Aggregator";

	public static final String PUBLISHER = "publisher";

	public static final String AGGREGATOR = "Aggregator";

	public static final String MULTI_RESOURCE_FORMAT = "multiResourceFormat";

	public static final String MULTI_EXCLUDES[] = { "*.organization", "*.fileData", "*.fileHash", "*.fromQa", "*.isFeatured", "*.isFeaturedBoolean", "*.isLive", "*.isNew", "*.lessonsString", "*.new",
			"*.parentUrl", "*.resourceInstances", "*.resourceLearnguides", "*.resourceMetaData", "*.resourceOid", "*.resourceSegments", "*.responses", "*.score", "*.siteName", "*.sourceReference",
			"*.subscriptions", "*.tags", "*.taxonomySet.*", "*.resourceInfo.text", "*.userPermSet", "*.userUploadedImage", "*.vocaularyString", "*.class", "*.answerId", "*.answerType", "*.isCorrect",
			"*.matchingAnswer", "question.question", "*.sequence", "*.unit", "*.hintId", "*.assessmentCode", "*.assessmentGooruId", "*.assets", "*.codes", "*.creator", "*.difficultyLevel",
			"*.helpContentLink", "*.importCode", "*.instruction", "*.isFolderAbsent", "*.quizNetwork", "*.scorePoints", "*.sourceContentInfo", "*.user", "*.userVote", "*.queryUId" };

	public static final String SINGLE_EXCLUDES[] = { "*.organization", "fileData", "fileHash", "fromQa", "isFeaturedBoolean", "isLive", "isNew", "lessonsString", "new", "parentUrl",
			"resourceInstances", "resourceLearnguides", "resourceMetaData", "resourceOid", "resourceSegments", "responses", "score", "siteName", "sourceReference", "subscriptions", "taxonomySet.*",
			"text", "userPermSet", "userUploadedImage", "vocaularyString", "*.class", "*.answerId", "*.answerType", "*.isCorrect", "*.matchingAnswer", "*.question", "*.sequence", "*.unit", "*.hintId",
			"*.assessmentCode", "*.assessmentGooruId", "*.assets", "*.codes", "*.difficultyLevel", "*.helpContentLink", "*.importCode", "*.instruction", "*.isFolderAbsent", "*.quizNetwork",
			"*.scorePoints", "*.sourceContentInfo", "*.userVote", "*.queryUId", "*.answers.question", "*.hints.question", "*.resource.contentMetaAssoc", "*.resource.indexType",
			"*.resource.isFeatured", "*.resource.isFeaturedBoolean", "*.resource.isNew", "*.resource.recordSource", "*.resource.settings", "*.resource.s3UploadFlag", "*.resource.taxonomySet",
			"*.resource.views", "*.resource.thumbnails" };

	public static final String COLLECTION_ITEM_EXCLUDES[] = { "*.contentMetaAssoc", "*.indexType", "*.isFeatured", "*.isFeaturedBoolean", "*.isNew", "*.recordSource", "*.settings", "*.s3UploadFlag" };

	public static final String COLLECTION_QUIZ = "collection-quiz";

	public static final String TYPE_LIBRARY = "library";

	public static final String SEARCH_QUERY = "searchQuery";

	public static final String TYPE_SOURCE = "source";

	public static final String AUTO_COMPLETE = "autocomplete";

	public static final String HTTPS_PROTOCOL_TYPE = "https";

	public static final String HTTP_PROTOCOL_TYPE = "http";

	public static final String SEARCH_REFERER = "referer";

	public static final String REQUEST_PROTOCOL = "requestProtocol";

	public static final ArrayList<String> API_KEYS = new ArrayList<String>(Arrays.asList("ASERTYUIOMNHBGFDXSDWERT123RTGHYT"));

	public static final String SEARCH_FORMAT[] = { "subjects", "resourceFormat" };

	public static final String X_FORWARDED_PROTO = "X-FORWARDED-PROTO";

	public static final String TAXONOMY_ROOT_CODE_USER = "user_taxonomy_root_code";

	public static final String Level = "level";

	public static final String LIST = "list";

	public static final String RELOAD = "reload";

	public static final String TYPE_SCOLLECTION = "scollection";

	public static final String QUERY = "query";

	public static final String SUGGEST = "suggest";

	public static final String QUERY_STRING = "query_string";

	public static final String CHECK_SUGGEST = "check-suggest";

	public static final String FIELD = "field";

	public static final String TERM = "term";

	// Attribution & Aggregation

	public static final String PUBISHER = "publisher";

	public static final String SCHOOLDISTRICT = "schooldistrict";

	public static final String FIND_SPECIAL_CHARACTERS_REGEX = "([^a-z0-9A-Z])";

	public static final String REPLACE_CHARACTERS = "\\\\$1";

	public static final String UNDERSCORE_COLON = "_colon";

	public static final String SEARCH_FILTER_NAME = "filter-name";

	public static final String AND = "and";

	public static final String TERMS = "terms";

	public static final String FACETS = "facets";

	public static final String FACET_FIELD = "taxonomy.subject.label";

	public static final String FACET_NAME = "subject";

	public static final String SUBJECT_FILTER = "subjectName";

	public static final String FACET_FILTER = "facet_filter";

	public static final String OPTIONS = "options";

	public static final String SCORE = "score";

	public static final String FREQ = "freq";

	public static final String COLLECTION_EDIT = "collection-edit";

	public static final String SCOLLECTION_EDIT = "scollection-edit";

	// public static String TOTAL_HIT_COUNT = "TotalHitCount";

	public static final String MULTI_RESOURCE = "multi_resource";

	public static final String SEARCH_USER_ID = "user_uid";

	public static final String TAXONOMY_SUB_LEVEL = "taxonomy_sub_level";

	public static final String[] CATEGORY_LIST = { "Video", "Interactive", "Website", "Slide", "Textbook", "Handout", "Lesson", "Exam" };

	public static final String SPELLCHECKER = "spellchecker";

	public static final String DOT = ".";

	public static final String HYPHEN = "-";

	public static final String[] BS_CATEGORY_LIST = { "Video", "Images", "Interactive", "Website", "Slide", "Textbook", "Handout", "Lesson", "Exam", "Cases", "Documents", "Activities", "Audio",
			"Maps", "Readings", "File", "Laps", "StudyGuides", "Homework", "ProjectPlan" };

	public static final String FLT = "flt";

	public static final String BOOST_FIELD = "boostField";

	public static final String FLT_NOT = "fltNot";

	public static final String FLT_OR = "fltOr";

	public static final String SEARCH_LIMIT_MESSAGE = "Gooru APIs make the top 20 results for each of our eight content categories available to developers. Developers will we able to access a maximum of 160 results. Please contact developers@goorulearning.org with any questions";

	public static final String DISABLE_SPELLCHECK = "disableSpellCheck";

	public static final String SIZE = "size";

	public static final String FIELDS = "fields";

	public static final String PRETTY = "pretty";

	public static final String GOORU_SESSION_TOKEN = "gooru-session-token";

	public static final String CONTENT_PROVIDER = "contentprovider";

	public static final String ERROR_INCLUDE[] = { "*.fieldError", "*.errorCount", "*.code", "*.defaultMessage", "*.field", "*.objectName", "*.rejectedValue" };

	public static final String BLACK_LIST_WORD_RESPONSE_MESSAGE = "Remember:This is a search engine for learning. To ensure a safe search experience, Gooru does not provide any results for the search term you entered.";

	public static final String INCLUDE_COLLECTION_ITEM = "includeCollectionItem";

	public static final String INCLUDE_CIMIN = "includeCIMin";

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String UUID = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

	public static final String newFormats = "{\"data\":[{\"website\": { \"resourceFormat\": \"webpage\", \"instructionalUse\": null }}, {\"video\": { \"resourceFormat\": \"video\", \"instructionalUse\": null }}, {\"interactive\": { \"resourceFormat\": \"interactive\", \"instructionalUse\": null }}, {\"audio\": { \"resourceFormat\": \"audio\", \"instructionalUse\": null }}, {\"question\": { \"resourceFormat\": \"question\", \"instructionalUse\": null }}, {\"website,recipes\": { \"resourceFormat\": \"webpage\", \"instructionalUse\": null }}, {\"text\": { \"resourceFormat\": \"text\", \"instructionalUse\": null }}, {\"exam\": [ { \"resourceFormat\": \"text\", \"instructionalUse\": \"quiz\", \"validate\": \"not_http\" }, { \"resourceFormat\": \"interactive\", \"instructionalUse\": \"quiz\", \"validate\": \"http\" } ]}, {\"lesson\": { \"resourceFormat\": \"text\", \"instructionalUse\": \"curriculum plan\" }}, {\"slide\": { \"resourceFormat\": \"image\", \"instructionalUse\": \"presentation\" }},{ \"handout\": { \"resourceFormat\": \"text\", \"instructionalUse\": \"handout\" }}, {\"textbook\": { \"resourceFormat\": \"text\", \"instructionalUse\": \"reading\" }}, {\"image\": { \"resourceFormat\": \"image\", \"instructionalUse\": null }}, {\"assessment\": [ { \"resourceFormat\": \"text\", \"instructionalUse\": \"quiz\", \"validate\": \"not_http\" }, { \"resourceFormat\": \"interactive\", \"instructionalUse\": \"quiz\", \"validate\": \"http\" } ]}, {\"challenge\": { \"resourceFormat\": \"interactive\", \"instructionalUse\": \"quiz\" }}, {\"animation\": { \"resourceFormat\": \"interactive\", \"instructionalUse\": null }}, {\"app\": { \"resourceFormat\": \"webpage\", \"instructionalUse\": null }}, {\"mooc\": { \"resourceFormat\": \"webpage\", \"instructionalUse\": null }}, {\"book\": { \"resourceFormat\": \"text\", \"instructionalUse\": \"book\" }}, {\"reference material\": { \"resourceFormat\": \"webpage\", \"instructionalUse\": \"reference material\" }}, {\"presentation\": { \"resourceFormat\": \"image\", \"instructionalUse\": \"presentation\" }},{ \"activity\": [ { \"resourceFormat\": \"text\", \"instructionalUse\": \"activity\", \"validate\": \"not_http\" }, { \"resourceFormat\": \"interactive\", \"instructionalUse\": \"activity\", \"validate\": \"http\" } ]}, {\"curriculum plan\": { \"resourceFormat\": \"text\", \"instructionalUse\": \"curriculum plan\" }}, {\"quiz\": [ { \"resourceFormat\": \"text\", \"instructionalUse\": \"quiz\", \"validate\": \"not_http\" }, { \"resourceFormat\": \"interactive\", \"instructionalUse\": \"quiz\", \"validate\": \"http\" } ]}, {\"gooru/classplan\": { \"resourceFormat\": \"gooru classplan\", \"instructionalUse\": null }},{ \"gooru/classbook\": { \"resourceFormat\": \"gooru classbook\", \"instructionalUse\": null }}, {\"gooru/studyshelf\": { \"resourceFormat\": \"gooru studyshelf\", \"instructionalUse\": null }},{ \"gooru/notebook\": { \"resourceFormat\": \"gooru notebook\", \"instructionalUse\": null }}, {\"qb/response\": { \"resourceFormat\": \"Qb Response\", \"instructionalUse\": null }}, {\"qb/question\": { \"resourceFormat\": \"qb question\", \"instructionalUse\": null }},{ \"question\": { \"resourceFormat\": \"question\", \"instructionalUse\": null }}, {\"assessment-exam\": { \"resourceFormat\": \"assessment exam\", \"instructionalUse\": null }}, {\"scollection\": { \"resourceFormat\": \"scollection\", \"instructionalUse\": null }}, {\"classpage\": { \"resourceFormat\": \"classpage\", \"instructionalUse\": null }}, {\"assignment\": { \"resourceFormat\": \"assignment\", \"instructionalUse\": null }}, {\"folder\": { \"resourceFormat\": \"folder\", \"instructionalUse\": null } }]}";

	public static final String UN_RESTRICTED_SEARCH = "unrestrictedSearch";

	public static final String ALLOW_DUPLICATES = "allowDuplicates";

	public static final String DETAIL_TYPE = "detailType";

	public static final String CONTENT_DELIVERY_NETWORK_PATH = "ContentDeliveryNetworkPath";

	public static final String ASSESSMENT_URL = "assessment/url";

	public static final String USER = "USER";

	public static final String TYPE_USER = "user";

	public static final String OAUTH_ACCESS_TOKEN = "oauthAccessToken";

	public static final String ANONYMOUS = "anonymous";

	public static final String APPLICATION_KEY = "applicationKey";

	public static final int AUTHENTICATION_CACHE_EXPIRY_TIME_IN_SEC = 28800;

	public static final String CONTRIBUTOR = "contributor";

	public static final String INSIGHT_QUERY_URI_RAWDATA = "insights.activitystream.url.rawdata";

	public static final String INSIGHT_QUERY_URI = "insights.activity.query.url";

	public static final String DATE1 = "[date1]";

	public static final String DATE2 = "[date2]";

	public static final String USERID = "[gooruUId]";

	public static final String EVENT = "[event]";

	public static final String RESOURCE_FORMAT_INSTRUCTIONAL_DATA = "resource.format.instructional.data";

	public static final String DATA = "data";

	public static final String VALUE = "value";

	public static final String EXAM = "exam";

	public static final String ACTIVITY = "activity";

	public static final String VALIDATE = "validate";

	public static final String RESOURCEFORMAT = "resourceFormat";

	public static final String INSTRUCTIONALUSE = "instructionalUse";

	public static final String RESOURCE_CATEGORY_FORMATE = "resource_category_format";

	public static final String SEARCH_DICTIONARY_CHEMICAL_FORMULA = "search.dictionary.chemical.formula";

	public static final String SEARCH_TAXONOMY_ROOT_CODE = "search.taxonomy.root.code";

	public static final String FLT_STANDARD = "flt.standard";

	public static final String FLT_STANDARD_DISPLAY = "flt.standardDisplay";

	public static final String SEARCH_HITS = "hits";

	public static final String SEARCH_TOTAL = "total";

	public static final String TAXONOMYDATASET = "taxonomyDataSet";

	public static final String SEARCH_SOURCE = "_source";

	public static final String AGGREGATOR_NAME = "aggregatorname";

	public static final String RESOURCESOURCE_ATTRIBUTION = "resourceSource.attribution";

	public static final String CUSTOMFIELD_CUSTOMFIELDS_CFHOST = "customField.customFields.cfHost";

	public static final String SEARCH_GOORU_OID = "gooruOId";

	public static final String SEARCH_RESULT_UID = "resultUid";

	public static final String USERNAME_DISPLAY = "usernameDisplay";

	public static final String SEARCH_CONTENT_ID = "contentId";

	public static final String SEARCH_ASSESSMENT = "assessment";

	public static final String SEARCH_QUIZ = "quiz";

	public static final String SEARCH_ANONYMOUS = "anonymous";

	public static final String SEARCH_CURRICULUM = "curriculum";

	public static final String SEARCH_CURRICULUM_CODE = "curriculumCode";

	public static final String SEARCH_COURSE = "course";

	public static final String SEARCH_CODE_ID = "codeId";

	public static final String SEARCH_SHARING = "sharing";
	public static final String SEARCH_GOALS = "goals";
	public static final String SEARCH_DESCRIPTION = "description";
	public static final String SEARCH_CATEGORY = "category";
	public static final String SEARCH_BATCHID = "batchId";
	public static final String SEARCH_GRADE = "grade";
	public static final String SEARCH_RESOURCE_ID = "resultUid";
	public static final String SEARCH_TITLE = "title";
	public static final String SEARCH_COLLABORATORS = "collaborators";

	public static final String SEARCH_USER_GOORU_UID = "gooruUID";
	public static final String SEARCH_GOORU_UID = "gooruUId";
	public static final String SEARCH_FOLDER = "folder";
	public static final String SEARCH_THUMBNAIL = "thumbnail";
	public static final String SEARCH_TYPE = "type";
	public static final String SEARCH_LAST_MDIFIED = "lastModified";
	public static final String SEARCH_ADD_DATE = "addDate";
	public static final String SEARCH_CREATOR = "creator";
	public static final String SEARCH_FIRST_NAME = "firstName";
	public static final String SEARCH_LAST_NAME = "lastName";
	public static final String SEARCH_USER_NAME_DISPLAY = "usernameDisplay";
	public static final String SEARCH_OWNER = "owner";
	public static final String SEARCH_ID = "id";
	public static final String SEARCH_STATISTICS = "statistics";
	public static final String SEARCH_VIEWS_COUNT = "viewsCount";
	public static final String SEARCH_VOTE_DOWN = "voteDown";
	public static final String SEARCH_VOTE_UP = "voteUp";
	public static final String SEARCH_SUBSCRIBERCOUNT = "subscriberCount";

	public static final String SEARCH_SOCIALDATA = "socialData";
	public static final String SEARCH_COLLABORATOR_COUNT = "collaboratorCount";
	public static final String SEARCH_TAXONOMY = "taxonomy";
	public static final String SEARCH_TAXONOMY_SKILLS = "taxonomySkills";
	public static final String SEARCH_TAXONOMY_SUBJECT = "subject";
	public static final String SEARCH_TAXONOMY_COURSE = "course";
	public static final String SEARCH_TAXONOMY_UNIT = "unit";
	public static final String SEARCH_TAXONOMY_TOPIC = "topic";
	public static final String SEARCH_TAXONOMY_LESSION = "lesson";
	public static final String SEARCH_SEGMENT = "segment";
	public static final String SEARCH_SEGMENT_TITLES_AND_OIDS = "segmentTiltlesAndOIds";
	public static final String SEARCH_DISTINGUISH = "distinguish";
	public static final String SEARCH_IS_FEATURED = "isFeatured";
	public static final String SEARCH_ASSET_URI = "assetURI";
	public static final String SEARCH_ORGANIZATION = "organization";
	public static final String SEARCH_CODE = "code";
	public static final String SEARCH_PARTY_UID = "partyUid";
	public static final String SEARCH_NAME = "name";

	public static final String SEARCH_ASSESSMENT_GOORU_OID = "assessmentGooruOid";

	public static final String SEARCH_COLLECTION_GOORU_OID = "collectionGooruOid";
	public static final String SEARCH_COLLECTION_QUIZ = "collectionQuiz";
	public static final String SEARCH_CUSTOM_FIELD = "customField";
	public static final String SEARCH_UPDATED_CUSTOM_FIELD = "updatedCustomFields";
	public static final String SEARCH_CUSTOM_FIELD_JSON = "customFieldsJson";
	public static final String SEARCH_SEARCH_ALIAS_VALUE_MAP = "searchAliasValueMap";
	public static final String SEARCH_ORGANIZATION_UID = "organizationUId";
	public static final String SEARCH_QUESTION = "question";
	public static final String SEARCH_RESOURCE = "resource";

	public static final String SEARCH_CONCEPT = "concept";
	public static final String SEARCH_EXPLANATION = "explanation";
	public static final String SEARCH_SEARCH_SOURCE = "source";
	public static final String SEARCH_ADMIN_FLAG = "adminFlag";
	public static final String SEARCH_ANSWER = "answer";
	public static final String SEARCH_ANSWER_TEXT = "answerText";
	public static final String SEARCH_HINT = "hint";
	public static final String SEARCH_HINT_TEXT = "hintText";
	public static final String SEARCH_FIELDS = "fields";
	public static final String SEARCH_COLABORATOR_EMAILS = "collaboratorEmails";
	public static final String SEARCH_LEARNING_OBJECTIVES = "learningObjectives";
	public static final String SEARCH_QUIZ_COLLECTION = "quizCollection";
	public static final String SEARCH_VECABULARY = "vocabulary";
	public static final String SEARCH_COLLECTION_GOORUOID = "collectionGooruOid";
	public static final String SEARCH_ASSESSMENT_GOORUOID = "assessmentGooruOid";
	public static final String SEARCH_SEGMENT_TITLES = "segmentTitles";
	public static final String SEARCH_ESTIMATED_TIME = "estimatedTime";
	public static final String SEARCH_COLLECTION_ITEM_COUNT = "collectionItemCount";
	public static final String SEARCH_COLLECTION_TYPE = "collectionType";
	public static final String SEARCH_NARRATION_LINK = "narrationLink";
	public static final String SEARCH_NOTES = "notes";
	public static final String SEARCH_KEYPOINTS = "keyPoints";
	public static final String SEARCH_LANGUAGE = "language";
	public static final String SEARCH_LICENSE = "license";
	public static final String SEARCH_URL = "url";
	public static final String SEARCH_TAG = "tag";
	public static final String SEARCH_ICON = "icon";
	public static final String SEARCH_DEFINITION = "definition";
	public static final String SEARCH_COLLECTION_ITEM = "collectionItem";
	public static final String SEARCH_COLLECTION_ITEM_ID = "collectionItemId";
	public static final String SEARCH_RESOURCE_FORMAT = "resourceFormat";
	public static final String SEARCH_RESOURCE_TYPE = "resourceType";
	public static final String SEARCH_ITEM_TYPE = "itemType";
	public static final String SEARCH_ITEM_SEQUENCE = "itemSequence";
	public static final String SEARCH_NARRATION = "narration";
	public static final String SEARCH_NARRATION_TYPE = "narrationType";
	public static final String SEARCH_START = "start";
	public static final String SEARCH_STOP = "stop";
	public static final String SEARCH_RESOURCE_CONTENT_ID = "resourceContentId";
	public static final String SEARCH_ATTRIBUTION = "attribution";
	public static final String SEARCH_DOMAIN_NAME = "domainName";
	public static final String SEARCH_RESOURCE_GOORU_OIDS = "resourceGooruOIds";
	public static final String SEARCH_COUNT = "count";
	public static final String SEARCH_SOCIAL_DATA = "socialData";
	public static final String SEARCH_LAST_MODIFIED_USER_ID = "lastModifiedUserUid";
	public static final String SEARCH_PROFILE_VISIBILITY = "profileVisibility";
	public static final String SEARCH_RATINGS = "ratings";
	public static final String SEARCH_AVERAGE = "average";
	public static final String SEARCH_AUDIENCE = "audience";
	public static final String SEARCH_DEPTHOFKNOWLEDGE = "depthOfknowledge";
	public static final String SEARCH_INSTRUCTIONAL_METHOD = "instructionalMethod";
	public static final String SEARCH_LEARNING_AND_INOVATION = "learningAndInovation";
	public static final String SEARCH_LANGUAGE_OBJECTIVE = "languageObjective";
	public static final String SEARCH_CONTENT_TAGS = "contentTags";
	public static final String SEARCH_SKILL = "skill";
	public static final String SEARCH_LIBRARY_NAME = "libraryName";
	public static final String SEARCH_AVERAGE_TIME_SPENT = "averageTimeSpent";
	public static final String SEARCH_SCOLLECTION_REMIX_COUNT = "scollectionRemixCount";
	public static final String SEARCH_USER_NAME = "userName";
	public static final String SEARCH_USER_PROFILE_IMAGE = "userProfileImage";
	public static final String SEARCH_ORGANIZATION_NAME = "organizationName";
	public static final String SEARCH_PUBLISHER_NAME = "publishername";
	public static final String SEARCH_AGGREGATOR_NAME = "aggregatorname";
	public static final String SEARCH_MODE = "searchMode";
	public static final String SEARCH_MODE_SIMPLE = "simple";
	public static final String SEARCH_SCOLLECTIONS = "collections";
	public static final String SEARCH_QUIZZES = "quizzes";
	public static final String SEARCH_SIMPLE_COLLECTIONS = "simple_collections";
	public static final String SEARCH_SIMPLE_QUIZZESS = "simple_quizzes";
	public static final String SEARCH_ASSESSMENT_QUESTION = "assessment-question";
	public static final String SEARCH_PROTOCOL_SUPPORTED = "protocolSupported";
	public static final String SEARCH_RECORD_SOURCE = "recordSource";
	public static final String SEARCH_RESOURCE_SOURCE = "resourceSource";
	public static final String SEARCH_RESOURCE_SOURCE_ID = "resourceSourceId";

	public static final String SEARCH_FRAMEBREAKER = "frameBreaker";
	public static final String SEARCH_ACTIVE_STATUS = "activeStatus";
	public static final String SEARCH_DOCUMENT_ID = "documentid";
	public static final String SEARCH_DOCUMENT_KEY = "documentkey";
	public static final String SEARCH_HAS_ORIGINAL_URL_ACCESS = "hasOriginalUrlAccess";
	public static final String SEARCH_STATUS_BROKEN = "statusIsBroken";
	public static final String SEARCH_SITE_NAME = "siteName";
	public static final String SEARCH_IS_OER = "isOer";
	public static final String SEARCH_EDUCATIONAL_USE = "educationalUse";
	public static final String SEARCH_MOMENTSOFLEARNING = "momentsofLearning";
	public static final String SEARCH_PARTY_PERMISSION_MAP = "partyPermissionsMap";
	public static final String SEARCH_UID = "uId";
	public static final String SEARCH_MEDIA_TYPE = "mediaType";
	public static final String SEARCH_VIEWS_COUNTN = "viewsCountN";
	public static final String SEARCH_RESOURCE_ADDEDCOUNT = "resourceAddedCount";
	public static final String SEARCH_RESOURCE_USED_USER_COUNT = "resourceUsedUserCount";
	public static final String SEARCH_HAS_FRAMEBREAKER = "hasFrameBreakerN";
	public static final String SEARCH_USEDIN_COLLECTION_COUNT = "usedInCollectionCount";
	public static final String SEARCH_AVERAGE_TIME = "averageTime";
	public static final String SEARCH_STANDARD_MAPPINGS = "standardMappings";
	public static final String SEARCH_RESOURCE_FEED = "resourceFeed";
	public static final String SEARCH_DURATION_SEC = "durationInSec";
	public static final String SEARCH_RESOURCE_INFO_TAGS = "resourceInfo.tags";
	public static final String SEARCH_RESOURCE_INFO_NUM_OF_PAGES = "resourceInfo.numOfPages";
	public static final String SEARCH_SCOLLECTION_TITLES = "scollectionTitles";
	public static final String SEARCH_SCOLECTION_GOORU_OID = "scollectionGooruOIds";
	public static final String SEARCH_INSTRUCTIONAL = "instructional";
	public static final String SEARCH_REVIEW_COUNT = "reviewCount";
	public static final String SEARCH_PUBLISHER = "publisher";
	public static final String SEARCH_AGGREGATOR = "aggregator";
	public static final String SEARCH_TIME_COMPLETE_IN_SEC = "timeToCompleteInSec";
	public static final String SEARCH_DISTRICTCODE = "districtCode";

	public static final String SEARCH_DISTRICTNAME = "districtName";

	public static final String SEARCH_DISTRICTID = "districtId";

	public static final String SEARCH_SCHOOL = "school";

	public static final String SEARCH_STATE = "state";
	public static final String SEARCH_STATE_PROVIENCE = "stateProvince";
	public static final String SEARCH_SEARCH_QUERY = "searchquery";
	public static final String SEARCH_LABEL = "label";
	public static final String SEARCH_CODE_UID = "codeUId";
	public static final String SEARCH_DISPLAY_ORDER = "displayOrder";

	public static final String SEARCH_PARENT_ID = "parentId";
	public static final String SEARCH_TYPE_ID = "typeId";
	public static final String SEARCH_DEPTH = "depth";
	public static final String SEARCH_ROOT_NODE_ID = "rootNodeId";
	public static final String SEARCH_CODE_IMAGE = "codeImage";
	public static final String SEARCH_S3_UPLOAD_FLAG = "s3UploadFlag";
	public static final String SEARCH_ACCOUNT_ID = "accountId";
	public static final String SEARCH_NETWORK = "network";
	public static final String SEARCH_EMAIL_ID = "emailID";
	public static final String SEARCH_CREATED_ON = "createdOn";
	public static final String SEARCH_ROLE_SET = "roleSet";
	public static final String SEARCH_CONFIRMS_STATUS = "confirmStatus";
	public static final String SEARCH_LAST_LOGIN = "lastLogin";
	public static final String SEARCH_IS_DELETED = "isDeleted";
	public static final String SEARCH_ACCOUNT_REGISTER_TYPE = "accountRegisterType";
	public static final String SEARCH_ACCOUNT_TYPE_ID = "accountTypeId";
	public static final String SEARCH_ABOUT_ME = "aboutMe";
	public static final String SEARCH_META = "meta";
	public static final String SEARCH_ACTIVE = "active";
	public static final String SEARCH_PARENT_ACCOUNT_USER_NAME = "parentAccountUserName";
	public static final String SEARCH_CHILD_ACCOUNT_COUNT = "childAccountCount";
	public static final String DEFAULT_RESCORE_SCRIPT = "((_score/60*100) - _score) * doc['statistics.preComputedWeight'].value";
	public static final String DEFAULT_RESCORE_LANG = "painless";
	public static final String DEFAULT_RESCORE_MODE = "multiply";
	public static final int DEFAULT_RESCORE_WINDOW_SIZE = 300;
	public static final String RESCORE_SCRIPT = "search.resource.rescore.script";
	public static final String RESCORE_SCRIPT_LANG = "search.resource.rescore.script.lang";
	public static final String RESCORE_MODE = "search.resource.rescore.mode";
	public static final String RESCORE_SCRIPT_LIMIT = "long.search.resource.rescore.window.size";
	public static final String SEARCH_SPLCHR = "[-/@#$%^&_+=()=]+";
	public static final String SEARCH_HAS_ATTRIBUTION = "hasAttribution";
	public static final String SEARCH_HASNO_DESCRIPTION = "hasNoDescription";
	public static final String SEARCH_HASNO_TAXONOMY = "hasNoTaxonomy";
	public static final String SEARCH_RESPONSE = "responses";
	public static final String SEARCH_QUERY_VALUES = "queryValues";
	public static final String SEARCH_STATUS = "status";
	public static final String SEARCH_SEARCH_COUNT = "searchCount";
	public static final String SEARCH_LAST_ACCESSTIME = "lastAccessTime";
	public static final String SEARCH_HAS_NOSTANDARDS = "hasNoStandard";
	public static final String SEARCH_CATEGORY_VALUES = "all";
	public static final String SEARCH_VALUE_EMPTY = "Empty";
	public static final String SEARCH_VALUE_NOTEMPTY = "NotEmpty";
	public static final String SEARCH_SUPER_ADMIN = "superadmin";
	public static final String SEARCH_CONTENT_ADMIN = "Content_Admin";
	public static final String SEARCH_COMMA_SEPARATOR = ",";
	public static final String SEARCH_FLT_STANDARD = "flt.standard";
	public static final String SEARCH_DOT_SEPERTOR = ".";
	public static final String SEARCH_HYPHEN_SEPERTOR = "-";
	public static final String SEARCH_QUESTION_COUNT = "questionCount";
	public static final String SEARCH_RESOURCE_COUNT = "resourceCount";
	public static final Short SHORT_ZERO = 0;
	public static final String SESSION_TOKEN_SEARCH = "sessionToken";
	public static final String GOORU_HEADER_SESSION_TOKEN = "Gooru-Session-Token";
	public static final String GOORU_API_KEY = "Gooru-ApiKey";
	public static final String SESSION_TOKEN = "SESSION_TOKEN";
	public static final String MODEL = "model";
	public static final String REST_MODEL = "rest/model";
	public static final String JSON = "json";
	public static final String USER_CAPS = "USER";
	public static final String SEARCH_HAS_DESCRIPTION = "hasDescription";
	public static final String SEARCH_HAS_TAXONOMY = "hasTaxonomy";
	public static final String SEARCH_HAS_STANDARDS = "hasStandard";
	String USER_AGENT = "User-Agent";
	String MOBILE = "Mobile";
	String GOORU_HEADER_AUTHORIZATION = "Authorization";
	String HEADER_CODEPATH = "CodePath";
	String STR_ZERO = "0";
	String STR_ONE = "1";
	String STR_ZERO_COMMA_ONE = "0,1";
	String SEARCH_USER_TYPE = "userType";
	String COMMA = ",";
	String ESCAPED_STAR = "\\*";
	String EMPTY_STRING = "";
	String STAR = "*";
	String INVALID_KEYWORD_ERROR_MESSAGE = "Please search with valid keyword having atleast 3 letters without * or search with some filter for relevant results";
	String SEARCH_TAXONOMY_V2 = "taxonomyV2";
	String COLLECTION_TYPES = "collection|scollection|assessment";
	String OTHER = "other";
	String SEARCH_CURRICULUM_DESC = "curriculumDesc";
	String FLT_RATING = "flt.rating";
	String FLT_COLLECTION_TYPE = "flt.collectionType";
	String TWENTY_FIRST_CENTURY_SKILLS_CODE = "21_CS_";
	String HIGHER_EDUCATION_MATCH = "higher education|highereducation|h";
	String HIGHER_EDUCATION = "Higher Education";
	String KINDERGARTEN = "Kindergarten";
	String K_UPPER_CASE = "K";
	String PRE_K_UPPER_CASE = "Pre-K";
	String PRE_K_LOWER_CASE = "pre-k";
	String NULL_STRING = "null";
	String RESOURCE = "resource";
	String COLLECTION = "collection";
	String REDIS_SERVER_HOST = "redis.server.host";
	String REDIS_SERVER_PORT = "redis.server.port";
	String BOOL = "bool";
	String TYPE_QUESTION = "question";
	String FLT_RESOURCE_FORMAT = "flt.resourceFormat";

	String CF = "cf";
	String INFO_DOT = "info.";
	String AMPERSAND = "&";
	String CARET_SYMBOL = "^";
	String NOT_SYMBOL = "!";
	String FLT_COURSE_MISSING = "#^courseMissing";

	public static final String TYPE_COURSE = "course";
	public static final String FLT_COURSE_TYPE = "&^courseType";
	public static final String FLT_IS_FEATURED = "&^isFeatured";
	public static final String FLT_PUBLISH_STATUS = "&^publishStatus";
	public static final String FLT_STATUS_BROKEN = "&^statistics.statusIsBroken";
	public static final String FLT_CONTENT_FORMAT = "&^contentFormat";
	public static final String FLT_COURSE_ID = "&^courseId";
	public static final String FLT_TENANT_ID = "&^tenant.tenantId";

	public static final String CONTENT_CDN_URL = "contentCDN";
	public static final String USER_PREFERENCES = "userPreferences";

	public static final String THUMBNAIL_URL = "url";
	public static final String SEARCH_REQ_20 = "2.0";
	public static final String SESSION_TOKEN_20 = "special-token-2.0";

	public static final String TENANT_ROOT = "tenant_root";

	public static final String TENANT = "tenant";

	public static final String ALL_DISCOVERABLE_TENANT_IDS = "allDiscoverableTenantIds";

	public static final String AMPERSAND_STANDARD = "&^standard";
	public static final String AMPERSAND_STANDARD_DISPLAY = "&^standardDisplay";
	public static final String AMPERSAND_EQ_INTERNAL_CODE = "&^taxonomy.allEquivalentInternalCodes";
	public static final String AMPERSAND_EQ_DISPLAY_CODE = "&^taxonomy.allEquivalentDisplayCodes";
	public static final String AMPERSAND_EQ_FRAMEWORK_CODE = "&^taxonomy.allEquivalentFrameworkCodes";

	public static final String STANDARD_PREFERENCE = "standard_preference";

	public static final String LEAF_INTERNAL_CODE = "leafInternalCode";
	public static final String PARENT_TITLE = "parentTitle";

	public static final String PRE_TEST = "pre-test";
	public static final String POST_TEST = "post-test";
	public static final String BENCHMARK = "benchmark";
	public static final String BACKFILL = "backfill";
	public static final Pattern POST_TEST_OR_BENCHMARK = Pattern.compile("post-test|benchmark");
	public static final String STUDY_PLAYER = "study-player";
	public static final String RESOURCE_STUDY = "resource-study";
	public static final String COLLECTION_STUDY = "collection-study";

	public static final String SCORE_AVERAGE_MIN = "score.average.min";
	public static final String SCORE_AVERAGE_MAX = "score.average.max";
	public static final String TIMESPENT_AVERAGE_MIN = "timespent.average.min";
	public static final String TIMESPENT_AVERAGE_MAX = "timespent.average.max";
	public static final String CONTENT_SUB_FORMAT = "contentSubFormat";
	public static final String INTERNAL = "internal";
	public static final String EXTERNAL = "external";
	public static final String CLIENT_SOURCE = "clientSource";
	public static final String CAMELCASE_SEARCH = "Search";
	public static final String FRAMEWORK_CODE = "frameworkCode";
	public static final String TYPE_COMPETENCY_GRAPH = "competency-graph";
	public static final String KEYWORD_COMPETENCY = "keywordcompetency";
	public static final String BELOW_AVERAGE = "below-average";
	public static final String ABOVE_AVERAGE = "above-average";
	public static final String AVERAGE = "average";

	public static final String TOKEN = "Token";
	public static final String GUT_COMPETENCY_ID = "gutCompetencyId";
	public static final String AUTHORIZATION = "Authorization";
	public static final String TOKEN_SPACE = "Token ";
	public static final String API_COMPETENCY_NODE = "api.competency.node";
	public static final String DNS_ENV = "dns.env";
	public static final String KEYWORDS = "keywords";
	Pattern DEFAULT_FILTERS = Pattern.compile("flt.collectionType|flt.contentFormat|flt.resourceFormat|flt.publishStatus|flt.courseType");
	public static final String KEYWORD = "keyword";
	public static final String ANALYZER = "analyzer";
	public static final String MATCH = "match";
	public static final String STANDARD = "standard";
	public static final String AUTOCOMPLETE_KEYWORD = "autocomplete_keyword";
	public static final String TEXT = "text";
	public static final String SEARCH = "search";
	public static final String PUBLISHERS = "publishers";
	public static final String FROM = "from";
	public static final Pattern STANDARD_MATCH = Pattern.compile("standard_level_1|standard_level_2");
	public static final String TAXONOMY_RESOURCE = "taxonomy_resource";
	public static final String TAXONOMY_COLLECTION = "taxonomy_collection";
	public static final String LEARNING_TARGET_LEVEL_0 = "learning_target_level_0";
	public static final String GET_METHOD = "GET";
	public static final String SLASH_SEPARATOR = "/";
	public static final String UNDERSCORE_SEARCH = "_search";
	public static final String COLON = ":";
	public static final String HTTP = "http";
	public static final String GLOBAL = "global";
	public static final String DISCOVERABLE = "discoverable";
	public static final String GLOBAL_TENANT_IDS = "globalTenantIds";
	public static final String DISCOVERABLE_TENANT_IDS = "discoverableTenantIds";
	public static final String OR_SYMBOL = "|";
	public static final String TENANT_TREE = "tenant_tree";
	public static final String DEFAULT_FC_VISIBILITY = "default.fc_visibility";
	public static final String TYPE_UNIT = "unit";
	public static final String TYPE_LESSON = "lesson";
	public static final String LEARNING_MAPS = "learning-maps";
	public static final String CROSSWALK_ID = "crosswalkId";

	public enum PublishedStatus {

		PUBLISHED("published"), UNPUBLISHED("unpublished"), FEATURED("featured");

		private String status;

		PublishedStatus(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

	}
	
	public enum CollectionSubFormat {
		
		PRE_TEST("pre-test"), POST_TEST("post-test"), BENCHMARK("benchmark"), BACKFILL("backfill");

		private String subFormat;

		CollectionSubFormat(String subFormat) {
			this.subFormat = subFormat;
		}

		public String getStatus() {
			return subFormat;
		}
	}

	public enum ContentFormat {

		RESOURCE("resource"), 
		QUESTION("question"), 
		COLLECTION("collection"), 
		ASSESSMENT("assessment"),
		;

		private String contentFormat;

		ContentFormat(String contentFormat) {
			this.contentFormat = contentFormat;
		}

		public String getContentFormat() {
			return this.contentFormat;
		}

	}
	public static final String CONTENTS = "contents";
	Pattern CUL_MATCH = Pattern.compile("course|unit|lesson");

}
