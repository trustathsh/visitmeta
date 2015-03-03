package de.hshannover.f4.trust.visitmeta.ifmap.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;

public class PollResultMock implements IfmapMock<PollResult> {

	private PollResult mPollResult_mock;

	private List<SearchResult> mSearchResults;

	public PollResultMock() {
		mPollResult_mock = mock(PollResult.class);
		mSearchResults = new ArrayList<SearchResult>();

		when(mPollResult_mock.getResults()).thenReturn(mSearchResults);
	}

	public PollResultMock(List<SearchResult> searchResults) {
		this();

		mSearchResults = searchResults;
		when(mPollResult_mock.getResults()).thenReturn(mSearchResults);
	}

	public void addSearchResult(SearchResult searchResult) {
		mSearchResults.add(searchResult);
		when(mPollResult_mock.getResults()).thenReturn(mSearchResults);
	}

	@Override
	public PollResult getMock() {
		return mPollResult_mock;
	}

}
