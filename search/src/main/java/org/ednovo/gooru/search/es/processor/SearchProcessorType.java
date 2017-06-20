/**
 * 
 */
package org.ednovo.gooru.search.es.processor;

/**
 * List of Search Processors Types
 * 
 * @author SearchTeam
 * 
 */
public enum SearchProcessorType {
	
	CollectionSuggest(),
	
	ResourceSuggest(),
	
	SCollectionSuggest(),
	
	QuizSuggest(),
	
	QuestionSuggest(),

	BlackListQueryValidation(),

	LimitValidation(),
	
	DictionaryQueryExpansion(),

	TaxonomyQueryExpansion(),

	ContentUserPreference(),
	
	ContentUserProficiency(),

	FilterConstruction(),
	
	ContentFilterConstruction(),
	
	ResourceFilterConstruction(),
	
	SCollectionFilterConstruction(),
	
	UserFilterConstruction(),
	
	TaxonomyFilterConstruction(),
	
	SearchQueryFilterConstruction(),
	
	QuestionFilterConstruction(),

	FilterDetection(),

	EsDslQueryBuild(),
	
	LibraryEsDslQueryBuild(),
	
	ResourceEsDslQueryBuild(),

	Elasticsearch(),

	MapDeserializer(),

	HitsDeserializer(),

	ResourceDeserializer(),

	CollectionDeserializer(),

	QuizDeserializer(),

	SCollectionDeserializer(),

	TaxonomyDeserializer(),

	QuestionDeserializer(),

	UserDeserializer(),
	
	LibraryDeserializer(),
	
	SearchQueryDeserializer(),
	
	AttributionDeserializer(),
	
	MultiResourceSearch(),
	
	MultiResourceDeserializer(),
	
	EsSuggestDslQueryBuild(),
	
	FacetFilterConstruction(),
	
	PublisherDeserializer(),
	
	AggregatorDeserializer(),
	
	SubjectFacetFilter(),
	
	FacetDeserializer(),

    SpellChecker(),

    EsSpellCheckerDslQueryBuild(),

    SpellCheckerDeserializer(),
	
	SchoolDistrictDeserializer(), 
	
	SchoolDistrictFilterConstruction(),
	
	SkillsDeserializeProcessor(),
	
	ContributorFilterConstruction(),
	
	ContributorDeserializeProcessor(),
	
	CourseDeserializeProcessor(),
	
	CourseFilterConstruction(),
	
	UnitFilterConstruction(),
	
	LessonFilterConstruction(),
	
	UnitDeserializeProcessor(),
	
	LessonDeserializeProcessor(),

	ResourceSuggestDeserializer(),
	
	RubricFilterConstruction(),
	
	RubricDeserializeProcessor();

	private SearchProcessorType() {
	}

}
