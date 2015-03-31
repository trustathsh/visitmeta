package de.hshannover.f4.trust.visitmeta.ifmap.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;

public class ResultItemMock implements IfmapMock<ResultItem> {

	private ResultItem mResultItem_mock;

	private List<Document> mMetadata;

	private ResultItemMock() {
		mResultItem_mock = mock(ResultItem.class);
		mMetadata = new ArrayList<Document>();
		when(mResultItem_mock.getMetadata()).thenReturn(mMetadata);
	}

	public ResultItemMock(Identifier id1, Identifier id2) {
		this();

		when(mResultItem_mock.getIdentifier1()).thenReturn(id1);
		when(mResultItem_mock.getIdentifier2()).thenReturn(id2);
		when(mResultItem_mock.holdsLink()).thenReturn(true);
	}

	public ResultItemMock(Identifier id1) {
		this();

		when(mResultItem_mock.getIdentifier1()).thenReturn(id1);
		when(mResultItem_mock.holdsLink()).thenReturn(false);
	}

	@Override
	public ResultItem getMock() {
		return mResultItem_mock;
	}

	public void addMetadata(Document... doc) {
		for (Document d : doc) {
			mMetadata.add(d);
		}

		when(mResultItem_mock.getMetadata()).thenReturn(mMetadata);
	}
	
	public void setMetadata(List<Document> docList) {
		mMetadata = docList;
		when(mResultItem_mock.getMetadata()).thenReturn(mMetadata);
	}

}
