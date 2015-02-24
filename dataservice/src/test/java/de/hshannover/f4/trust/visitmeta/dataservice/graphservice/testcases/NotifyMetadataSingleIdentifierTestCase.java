package de.hshannover.f4.trust.visitmeta.dataservice.graphservice.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;

import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class NotifyMetadataSingleIdentifierTestCase extends AbstractTestCase {
	private final String TESTCASE_FILENAME = TESTCASES_DIRECTORY + File.separator
			+ "NotifyMetadataSingleIdentifier.yml";

	@Override
	public String getTestcaseFilename() {
		return TESTCASE_FILENAME;
	}

	@Override
	public void getInitialGraph() throws Exception {
		// not implemented due to notify scenario
	}

	@Override
	public void getGraphAt() throws Exception {
		// not implemented due to notify scenario
	}

	@Override
	public void getCurrentGraph() throws Exception {
		// not implemented due to notify scenario
	}

	@Override
	public void getDelta() throws Exception {
		// not implemented due to notify scenario
	}

	@Override
	public void getChangesMap() throws Exception {
		SortedMap<Long, Long> changesMap = mService.getChangesMap();
		SortedMap<Long, Long> expected = new TreeMap<Long, Long>();
		expected.put(0l, 2l);
		expected.put(1l, 2l);

		testChangesMap(expected, changesMap);
		testChangesMapJSON(expected, changesMap);
	}

	@Test
	public void getNotifiesAt() throws Exception {
		getNotifiesAt0();
		getNotifiesAt1();
	}

	public void getNotifiesAt0() throws Exception {
		long timestamp = 0;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(1, notifiesAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection("device1", "event1");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}

	public void getNotifiesAt1() throws Exception {
		long timestamp = 1;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(1, notifiesAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection("device1", "event2");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}
}
