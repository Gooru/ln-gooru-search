package org.ednovo.gooru.search.es.constant;

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

	public static final String TYPE_RUBRIC = "rubric";

	public static final String TYPE_QUIZ = "quiz";

	public static final String TYPE_PUBLISHER = "publisher";

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
			"*.assessmentCode", "*.assessmentGooruId", "*.assets", "*.codes", "*.difficultyLevel", "*.helpContentLink", "*.importCode", "*.instruction", "*.isFolderAbsent",
			"*.scorePoints", "*.sourceContentInfo", "*.userVote", "*.queryUId", "*.answers.question", "*.hints.question", "*.resource.contentMetaAssoc", "*.resource.indexType",
			"*.resource.isFeatured", "*.resource.isFeaturedBoolean", "*.resource.isNew", "*.resource.recordSource", "*.resource.settings", "*.resource.s3UploadFlag", "*.resource.taxonomySet",
			"*.resource.views", "*.resource.thumbnails" };

	public static final String COLLECTION_ITEM_EXCLUDES[] = { "*.contentMetaAssoc", "*.indexType", "*.isFeatured", "*.isFeaturedBoolean", "*.isNew", "*.recordSource", "*.settings", "*.s3UploadFlag" };

	public static final String V2_EXCLUDES[] = {"results", "query"};
	
	public static final String AGG_EXCLUDES[] = {"aggregations"};

	public static final String COLLECTION_QUIZ = "collection-quiz";

	public static final String TYPE_LIBRARY = "library";

	public static final String SEARCH_QUERY = "searchQuery";

	public static final String TYPE_SOURCE = "source";

	public static final String AUTO_COMPLETE = "autocomplete";

	public static final String SEARCH_FORMAT[] = { "subjects", "resourceFormat" };

	public static final String TYPE_SCOLLECTION = "scollection";

	public static final String QUERY = "query";

	public static final String SUGGEST = "suggest";

	public static final String QUERY_STRING = "query_string";

	public static final String CHECK_SUGGEST = "check-suggest";

	public static final String FIELD = "field";

	public static final String TERM = "term";

	public static final String FIND_SPECIAL_CHARACTERS_REGEX = "([^a-z0-9A-Z])";

	public static final String REPLACE_CHARACTERS = "\\\\$1";

	public static final String UNDERSCORE_COLON = "_colon";

	public static final String SEARCH_FILTER_NAME = "filter-name";

	public static final String TERMS = "terms";

	public static final String FACETS = "facets";

	public static final String FACET_FIELD = "taxonomy.subject.label";

	public static final String FACET_NAME = "subject";

	public static final String SUBJECT_FILTER = "subjectName";

	public static final String FACET_FILTER = "facet_filter";

	public static final String OPTIONS = "options";

	public static final String SCORE = "score";

	public static final String FREQ = "freq";

	public static final String MULTI_RESOURCE = "multi_resource";

	public static final String SEARCH_USER_ID = "user_uid";

	public static final String SPELLCHECKER = "spellchecker";

	public static final String DOT = ".";

	public static final String HYPHEN = "-";

	public static final String FLT = "flt";

	public static final String BOOST_FIELD = "boostField";

	public static final String FLT_NOT = "fltNot";

	public static final String FLT_OR = "fltOr";

	public static final String SEARCH_LIMIT_MESSAGE = "Gooru APIs make the top 20 results for each of our eight content categories available to developers. Developers will we able to access a maximum of 160 results. Please contact developers@goorulearning.org with any questions";

	public static final String DISABLE_SPELLCHECK = "disableSpellCheck";

	public static final String SIZE = "size";

	public static final String GOORU_SESSION_TOKEN = "gooru-session-token";

	public static final String INCLUDE_COLLECTION_ITEM = "includeCollectionItem";

	public static final String INCLUDE_CIMIN = "includeCIMin";

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static final String UUID = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";

	public static final String ALLOW_DUPLICATES = "allowDuplicates";

	public static final String USER = "USER";

	public static final String TYPE_USER = "user";

	public static final String ANONYMOUS = "anonymous";

	public static final String VALUE = "value";

	public static final String SEARCH_DICTIONARY_CHEMICAL_FORMULA = "search.dictionary.chemical.formula";

	public static final String SEARCH_TAXONOMY_ROOT_CODE = "search.taxonomy.root.code";

	public static final String SEARCH_TAXONOMY_SUBJECT_CLASSIFICATION = "search.taxonomy.subject.classification";

	public static final String FLT_STANDARD = "flt.standard";
	
	public static final String FLT_SUBJECT = "flt.subject";

	public static final String FLT_COURSE = "flt.course";

	public static final String FLT_DOMAIN = "flt.domain";

	public static final String FLT_STANDARD_DISPLAY = "flt.standardDisplay";

	public static final String FLT_RELATED_LEAF_INTERNAL_CODES = "flt.relatedLeafInternalCodes";

	public static final String FLT_RELATED_GUT_CODES = "flt.relatedGutCode";

	public static final String SEARCH_HITS = "hits";

	public static final String SEARCH_TOTAL = "total";

	public static final String SEARCH_SOURCE = "_source";
	public static final String SEARCH_CATEGORY = "category";
	public static final String SEARCH_GRADE = "grade";

	public static final String SEARCH_USER_GOORU_UID = "gooruUID";

	public static final String SEARCH_TYPE = "type";
	public static final String SEARCH_LAST_MDIFIED = "lastModified";
	public static final String SEARCH_ADD_DATE = "addDate";
	public static final String SEARCH_FIRST_NAME = "firstName";
	public static final String SEARCH_LAST_NAME = "lastName";

	public static final String SEARCH_TAXONOMY = "taxonomy";
	public static final String SUBJECT = "subject";
	public static final String SEARCH_ORGANIZATION = "organization";
	public static final String SEARCH_CODE = "code";
	public static final String SEARCH_NAME = "name";

	public static final String SEARCH_ORGANIZATION_UID = "organizationUId";
	public static final String SEARCH_QUESTION = "question";

	public static final String SEARCH_NOTES = "notes";
	public static final String SEARCH_URL = "url";
	public static final String SEARCH_ICON = "icon";
	public static final String SEARCH_DEFINITION = "definition";
	
	public static final String SEARCH_COUNT = "count";
	public static final String SEARCH_PROFILE_VISIBILITY = "profileVisibility";
	public static final String SEARCH_RATINGS = "ratings";
	public static final String SEARCH_AVERAGE = "average";
	public static final String SEARCH_CONTENT_TAGS = "contentTags";
	public static final String SEARCH_SKILL = "skill";
	public static final String SEARCH_USER_NAME = "userName";
	public static final String SEARCH_USER_PROFILE_IMAGE = "userProfileImage";

	public static final String SEARCH_DISTRICTCODE = "districtCode";

	public static final String SEARCH_DISTRICTNAME = "districtName";

	public static final String SEARCH_DISTRICTID = "districtId";

	public static final String SEARCH_STATE_PROVIENCE = "stateProvince";
	public static final String SEARCH_SEARCH_QUERY = "searchquery";

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
	public static final String SEARCH_SEARCH_COUNT = "searchCount";
	public static final String SEARCH_LAST_ACCESSTIME = "lastAccessTime";
	public static final String SEARCH_HAS_NOSTANDARDS = "hasNoStandard";
	public static final String SEARCH_CATEGORY_VALUES = "all";
	public static final String SEARCH_VALUE_EMPTY = "Empty";
	public static final String SEARCH_VALUE_NOTEMPTY = "NotEmpty";
	public static final String SEARCH_FLT_STANDARD = "flt.standard";
	public static final String SEARCH_FLT_GUT_CODE = "flt.gutCode";
	public static final String SESSION_TOKEN_SEARCH = "sessionToken";
	public static final String GOORU_HEADER_SESSION_TOKEN = "Gooru-Session-Token";
	public static final String GOORU_API_KEY = "Gooru-ApiKey";
	public static final String SESSION_TOKEN = "SESSION_TOKEN";
	public static final String MODEL = "model";
	public static final String REST_MODEL = "rest/model";
	public static final String JSON = "json";
	public static final String USER_CAPS = "USER";
	public static final String SEARCH_HAS_STANDARDS = "hasStandard";
	String USER_AGENT = "User-Agent";
	String MOBILE = "Mobile";
	String GOORU_HEADER_AUTHORIZATION = "Authorization";
	String HEADER_CODEPATH = "CodePath";
	String STR_ZERO = "0";
	String SEARCH_USER_TYPE = "userType";
	String COMMA = ",";
	String ESCAPED_STAR = "\\*";
	String EMPTY_STRING = "";
	String STAR = "*";
	String INVALID_KEYWORD_ERROR_MESSAGE = "Please search with valid keyword having atleast 3 letters without * or search with some filter for relevant results";
	String FLT_RATING = "flt.rating";
	String FLT_COLLECTION_TYPE = "flt.collectionType";
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
	public static final String AMPERSAND_CONTENT_FORMAT = "&^contentFormat";
	public static final String FLT_CONTENT_FORMAT = "flt.contentFormat";
	public static final String AMPERSAND_COLLECTION_TYPE = "&^collectionType";
	public static final String FLT_COURSE_ID = "&^courseId";
	public static final String FLT_TENANT_ID = "&^tenant.tenantId";
	public static final String FLT_PUBLISHER_QUALITY_INDICATOR = "&^statistics.publisherQualityIndicator";
	public static final String AMPERSAND_AUDIENCE = "&^audience";
	public static final String FLT_LANGUAGE = "flt.language";
	public static final String FLT_LANGUAGE_ID = "flt.languageId";
	public static final String AMPERSAND_CREATOR_ID = "&^creatorId";
	public static final String FLT_CREATOR_ID = "flt.creatorId";
	public static final String AMPERSAND_OWNER_ID = "&^ownerId";
	public static final String FLT_OWNER_ID = "flt.ownerId";

	public static final String CONTENT_CDN_URL = "contentCDN";
	public static final String USER_CDN_URL = "userCDN";
	public static final String USER_PREFERENCES = "userPreferences";
	public static final String USER_LANGUAGE_PREFERENCES = "userLanguagePreferences";
	public static final String LANGUAGE_ID = "languageId";
	public static final String DEFAULT_GOORU_LANG_ID = "defaultGooruLangId";
	public static final String AMPERSAND_PQI_GTE = "&^publisherQualityIndicatorGTE";

	public static final String SEARCH_REQ_20 = "2.0";
	public static final String SESSION_TOKEN_20 = "special-token-2.0";

	public static final String TENANT_ROOT = "tenant_root";

	public static final String TENANT = "tenant";

	public static final String ALL_DISCOVERABLE_TENANT_IDS = "allDiscoverableTenantIds";

	public static final String AMPERSAND_STANDARD = "&^standard";
	public static final String AMPERSAND_STANDARD_DISPLAY = "&^standardDisplay";
	public static final String AMPERSAND_RELATED_LEAF_INTERNAL_CODES = "&^relatedLeafInternalCodes";
	public static final String AMPERSAND_EQ_INTERNAL_CODE = "&^taxonomy.allEquivalentInternalCodes";
	public static final String AMPERSAND_EQ_DISPLAY_CODE = "&^taxonomy.allEquivalentDisplayCodes";
	public static final String AMPERSAND_EQ_RELATED_INTERNAL_CODE = "&^taxonomy.allEqRelatedInternalCodes";

	public static final String STANDARD_PREFERENCE = "standard_preference";
	public static final String LANGUAGE_PREFERENCE = "language_preference";
	public static final String ACCESS_TOKEN_VALIDITY = "access_token_validity";
	public static final String LEAF_INTERNAL_CODE = "leafInternalCode";

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
	public static final String CLIENT_SOURCE = "clientSource";
	public static final String CAMELCASE_SEARCH = "Search";
	public static final String TYPE_COMPETENCY_GRAPH = "competency-graph";
	public static final String KEYWORD_COMPETENCY = "keywordcompetency";
	public static final String BELOW_AVERAGE = "below-average";
	public static final String ABOVE_AVERAGE = "above-average";
	public static final String AVERAGE = "average";

	public static final String TOKEN = "Token";
	public static final String API_COMPETENCY_NODE = "api.competency.node";
	public static final String DNS_ENV = "dns.env";
	public static final String KEYWORDS = "keywords";
	Pattern DEFAULT_FILTERS = Pattern.compile("flt.collectionType|flt.contentFormat|flt.resourceFormat|flt.publishStatus|flt.courseType|aggBy");
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
	public static final String OR_SYMBOL = "|";
	public static final String TENANT_TREE = "tenant_tree";
	public static final String DEFAULT_FC_VISIBILITY = "default.fc_visibility";
	public static final String ET_MAX_VAL = "et.max.value";
	public static final String LEARNING_MAPS = "learning-maps";

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

		RESOURCE("resource"), QUESTION("question"), COLLECTION("collection"), ASSESSMENT("assessment"), ASSESSMENT_EXTERNAL("assessment-external"), COLLECTION_EXTERNAL("collection-external"),
		OFFLINE_ACTIVITY("offline-activity"), RUBRIC("rubric"), COURSE("course"), UNIT("unit"), LESSON("lesson");

		private String value;

		ContentFormat(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}
	public static final String CONTENTS = "contents";
	Pattern CUL_MATCH = Pattern.compile("course|unit|lesson");
	Pattern RQCA_MATCH = Pattern.compile("resource|question|collection|assessment|scollection|offline-activity");
	Pattern RESOURCE_MATCH = Pattern.compile("resource|question");
	Pattern COLLECTION_MATCH = Pattern.compile("scollection|collection|assessment|collection-external|assessment-external|offline-activity");
	public static final String PEDAGOGY_UNDERSCORE = "pedagogy_";
	public static final String TOTAL_HIT_COUNT = "totalHitCount";
	public static final String RESULT_COUNT = "resultCount";
	public static final String SEARCH_RESULTS = "searchResults";
	public static final String FLT_FWCODE = "flt.fwCode";
	public static final String SIGNATURE_CONTENTS = "signatureContents";
	public static final String FLT_TAXONOMY_GUT_CODE = "flt.taxonomyGutCode";
	public static final String TAXONOMY_GUT_CODE = "taxonomyGutCode";
	Pattern TAX_FILTERS = Pattern.compile("standard");
	public static final String AGG_FIELDNAME = "<>-^field";
	Pattern SEARCH_TYPES_MATCH = Pattern.compile("resource|question|collection|assessment|scollection|rubric|course|unit|lesson|publisher|searchQuery|autocomplete");
	public static final String AGG_BY = "aggBy";
	public static final String AGG_RESPONSE_LIMIT = "aggSize";
	public static final String AGGS = "aggs";
	public static final String RESCORE = "rescore";
	public static final String POST_FILTER = "post_filter";
	public static final String CONTENT_COUNTS = "contentCounts";
	public static final String STATS = "stats";
	public static final String COLLECTIONS = "collections";
	public static final String ASSESSMENTS = "assessments";
	public static final String RESOURCES = "resources";
    public static final String DOMAIN = "domain";
    public static final String[] START_WITH_BOOLEAN_OPERATORS = new String[] { "AND NOT ", "OR NOT ", "NOT AND ", "NOT OR ", "OR ", "AND " };
    public static final String[] END_WITH_BOOLEAN_OPERATORS = new String[] { " AND NOT", " OR NOT", " NOT AND", " NOT OR", " OR", " AND" };
    Pattern SCOPE_MYCONTENT_LIBRARY_MATCH = Pattern.compile("my-content|tenant-library|subtenant-library|open-library|course|open-featured");
    Pattern SCOPE_MATCH = Pattern.compile("my-content|tenant-library|subtenant-library|open-library|course|open-featured|open-all");
    public static final String LIBRARY = "library";
    public static final String _V3 = "_v3";
    public static final String HOT_SPOT_IMAGE_QUESTION = "hot_spot_image_question";
    public static final String AUDIENCE_TEACHERS = "Teachers";
    public static final String AUDIENCE_ALL_STUDENTS = "All Students";
	public static final String EXTERNAL = "external";
	public static final String UNDERSCORE = "_";
	public static final String TILDE = "~";
	public static final String PUNC_MATCH = "[\\p{Punct}&&[^a-zA-Z0-9\\'\\.]]";
	public static final String SPL_CHAR_MATCH = "[^a-zA-Z0-9\\']";
	public static final String IS_CROSSWALK = "isCrosswalk";
	public static final String SCOPE_KEY = "scopeKey";
	public static final Pattern CONTAINER_MATCH = Pattern.compile("collection|course");
	public static final String APPLICATION_JSON_UTF_8_VALUE = "application/json; charset=UTF-8";

}
