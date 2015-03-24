package de.hshannover.f4.trust.visitmeta.ifmap.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;

public class SearchResultMock implements IfmapMock<SearchResult> {

	private static int mSerachResultNameCount = 1;

	private String mSerachResultName = "SearchResultMock_" + getSerachResultNameCount();

	private SearchResult mSearchResult_mock;

	private List<ResultItem> mResultItems;

	public SearchResultMock(SearchResult.Type type) {
		mSearchResult_mock = mock(SearchResult.class);
		mResultItems = new ArrayList<ResultItem>();

		when(mSearchResult_mock.getResultItems()).thenReturn(mResultItems);
		when(mSearchResult_mock.getType()).thenReturn(type);
		when(mSearchResult_mock.getName()).thenReturn(mSerachResultName);
	}

	public SearchResultMock(List<ResultItem> resultItems, SearchResult.Type type) {
		this(type);

		mResultItems = resultItems;
		when(mSearchResult_mock.getResultItems()).thenReturn(mResultItems);
	}

	public void addResultItem(ResultItem resultItem) {
		mResultItems.add(resultItem);
		when(mSearchResult_mock.getResultItems()).thenReturn(mResultItems);
	}

	public void setName(String searchResultName) {
		when(mSearchResult_mock.getName()).thenReturn(searchResultName);
	}

	@Override
	public SearchResult getMock() {
		return mSearchResult_mock;
	}

	private String getSerachResultNameCount() {
		String count = mSerachResultNameCount + "";
		mSerachResultNameCount++;
		return count;
	}

}
