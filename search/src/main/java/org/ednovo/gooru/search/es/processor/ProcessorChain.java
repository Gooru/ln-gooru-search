/**
* 
*/
package org.ednovo.gooru.search.es.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ednovo.gooru.search.es.exception.SearchException;
import org.ednovo.gooru.search.es.model.SearchData;
import org.ednovo.gooru.search.es.model.SearchResponse;
import org.ednovo.gooru.search.model.GooruAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author SearchTeam
 * 
 */
public final class ProcessorChain<I extends SearchData, O extends Object> {

	private SearchProcessorType[][] processorChainTypes;
	private Integer threadPoolLength = 0;
	private ExecutorService doerService;

	public ProcessorChain(SearchProcessorType[][] processorChainTypes) {
		this.processorChainTypes = processorChainTypes;
		for (Integer typeIndex = 0; typeIndex < processorChainTypes.length; typeIndex++) {
			threadPoolLength += processorChainTypes[typeIndex].length;
		}
		threadPoolLength *= 3;
	}

	public void executeProcessorChain(final I searchData, final SearchResponse<O> response, final TransactionTemplate transactionTemplate) {

		final GooruAuthenticationToken authentication = (GooruAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

		SearchProcessorType[][] processorChainTypes2 = getProcessorChainTypes();
		for (Integer typeIndex = 0; typeIndex < processorChainTypes2.length; typeIndex++) {

			SearchProcessorType[] processorTypes = processorChainTypes2[typeIndex];
			if (processorTypes.length > 1) {
				// This block will enable only processor contain another processor
				if (doerService == null) {
					doerService = Executors.newFixedThreadPool(threadPoolLength);
				}

				for (final SearchProcessorType processorType : processorTypes) {
					if (searchData.getException() != null) {
						if (searchData.getException() instanceof SearchException) {
							throw (SearchException) searchData.getException();
						}
						throw new RuntimeException(searchData.getException().getMessage());
					}
					List<Callable<SearchResponse<Object>>> tasks = new ArrayList<Callable<SearchResponse<Object>>>();
					tasks.add(new Callable<SearchResponse<Object>>() {
						@Override
						public SearchResponse<Object> call() throws Exception {
							try {
								if (!searchData.isSkipType(processorType)) {
									final SearchProcessor processsor = SearchProcessor.get(processorType);
									if (authentication != null) {
										SecurityContextHolder.getContext().setAuthentication(authentication);
									}
									if (processsor.isTransactional()) {
										transactionTemplate.execute(new TransactionCallbackWithoutResult() {

											@Override
											protected void doInTransactionWithoutResult(TransactionStatus status) {
												processsor.process(searchData, (SearchResponse<Object>) response);
											}
										});
									} else {
										processsor.process(searchData, (SearchResponse<Object>) response);
									}
								}
							} catch (Exception ex) {
								searchData.setException(ex);
							}
							return (SearchResponse<Object>) response;
						}
					});
					try {
						doerService.invokeAll(tasks, 60L, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
			    /* doerService.shutdown();
                while (!doerService.isTerminated()) {
                        // Wait for processors to get completed
                }*/
				if (searchData.getException() != null) {
					if (searchData.getException() instanceof SearchException) {
						throw (SearchException) searchData.getException();
					} else {
						throw new RuntimeException(searchData.getException());
					}
				}
			} else if (!searchData.isSkipType(processorTypes[0])) {
				SearchProcessor.get(processorTypes[0]).process(searchData, (SearchResponse<Object>) response);
			}
		}
	}

	public SearchProcessorType[][] getProcessorChainTypes() {
		return processorChainTypes;
	}

	public void setProcessorChainTypes(SearchProcessorType[][] processorChainTypes) {
		this.processorChainTypes = processorChainTypes;
	}
}
