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

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class NotifyMetadataTestCase extends AbstractTestCase {
	private final String TESTCASE_FILENAME = TESTCASES_DIRECTORY + File.separator
			+ "NotifyMetadata.yml";

	@Override
	public String getTestcaseFilename() {
		return TESTCASE_FILENAME;
	}

	@Override
	public void getInitialGraph() throws Exception {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();
		assertTrue(initialGraph.isEmpty());

		JSONArray actual = toJson(initialGraph);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));	
		}

	@Override
	public void getGraphAt() throws Exception {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);

		assertTrue(graphAt.isEmpty());
		System.out.println(toJson(graphAt));

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getCurrentGraph() throws Exception {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();

		assertTrue(currentGraph.isEmpty());

		JSONArray actual = toJson(currentGraph);
		JSONArray expected = new JSONArray();

		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getDelta() throws Exception {
		long t1 = 0;
		long t2 = 0;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> deletes = delta.getDeletes();
		List<IdentifierGraph> updates = delta.getUpdates();

		assertTrue(deletes.isEmpty());
		assertTrue(updates.isEmpty());

		JSONArray actualUpdates = toJson(updates);
		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedUpdates = new JSONArray();
		JSONArray expectedDeletes = new JSONArray();

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	@Override
	public void getChangesMap() throws Exception {
		SortedMap<Long, Long> changesMap = mService.getChangesMap();
		SortedMap<Long, Long> expected = new TreeMap<Long, Long>();
		expected.put(0l, 2l);
		expected.put(1l, 2l);
		expected.put(2l, 2l);

		testChangesMap(expected, changesMap);
		testChangesMapJSON(expected, changesMap);
	}

	@Test
	public void getNotifiesAt() throws Exception {
		getNotifiesAt0();
		getNotifiesAt1();
		getNotifiesAt2();
	}

	public void getNotifiesAt0() throws Exception {
		long timestamp = 0;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(2, notifiesAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection("device1", "device2", "event1");
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
			JSONObject idPair1 = createJSONIdentifierMetadataConnection("device3", "event3");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}	
	
	public void getNotifiesAt2() throws Exception {
		long timestamp = 2;
		List<IdentifierGraph> notifiesAt = mService.getNotifiesAt(timestamp);
		assertEquals(2, notifiesAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(notifiesAt);
		JSONArray expected = new JSONArray();
		{
			JSONObject idPair1 = createJSONIdentifierMetadataConnection("device1", "device3", "event3");
			JSONObject subGraph1 = createJSON(timestamp, idPair1);
			expected.put(subGraph1);
		}

		assertTrue(jsonsEqual(actual, expected));
	}
}
