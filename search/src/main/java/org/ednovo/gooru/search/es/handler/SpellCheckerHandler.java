package org.ednovo.gooru.search.es.handler;

import java.util.List;
import java.util.Map;

import org.ednovo.gooru.search.es.constant.EsIndex;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.es.processor.SearchProcessorType;
import org.ednovo.gooru.search.es.service.SearchSettingService;
import org.springframework.stereotype.Component;

import static org.ednovo.gooru.search.es.processor.SearchProcessorType.*;


@Component
public class SpellCheckerHandler extends SearchHandler<SearchData, List<Map<String, Object>>> {

  private static final SearchProcessorType[][] searchProcessorTypes = new SearchProcessorType[][]{
      {EsSpellCheckerDslQueryBuild}, {Elasticsearch}, {SpellCheckerDeserializer}
  };

  @Override
  protected SearchProcessorType[][] getProcessorTypeChain() {
    return searchProcessorTypes;
  }

  @Override
  protected SearchHandlerType getType() {
    return SearchHandlerType.SPELLCHECKER;
  }

  @Override
  protected EsIndex getIndexType() {
    return EsIndex.RESOURCE;
  }


  @Override
  public SearchResponse<List<Map<String, Object>>> search(SearchData searchData) {
    SearchSettingService.validateCache();
    SearchResponse<List<Map<String, Object>>> response = new SearchResponse<List<Map<String, Object>>>();
    searchData.setIndexType(EsIndex.RESOURCE);
    searchData.setType(EsIndex.RESOURCE.toString());
    processorChain.executeProcessorChain(searchData, response, transactionTemplate);
    return response;
  }

}

