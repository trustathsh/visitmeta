package de.hshannover.f4.trust.visitmeta.dataservice.graphservice.testcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import java.util.SortedMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class TemporaryEmptyGraphTestCase extends AbstractTestCase {

	private final String TESTCASE_FILENAME = TESTCASES_DIRECTORY + File.separator + "TemporaryEmptyGraph.yml";

	@Override
	public String getTestcaseFilename() {
		return TESTCASE_FILENAME;
	}

	@Override
	public void getInitialGraph() throws JSONException {
		List<IdentifierGraph> initialGraph = mService.getInitialGraph();
		assertEquals(2, initialGraph.get(0).getIdentifiers().size());

		JSONArray actual = toJson(initialGraph);
		JSONArray expected = buildJSONFromYamlFile("link1", 0l);
		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getGraphAt() throws JSONException {
		getGraphAt0();
		getGraphAt1();
		getGraphAt2();
	}

	private void getGraphAt0() throws JSONException {
		long timestamp = 0;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);
		assertEquals(2, graphAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = buildJSONFromYamlFile("link1", 0l);
		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt1() throws JSONException {
		long timestamp = 1;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);
		assertEquals(0, graphAt.size());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = new JSONArray();
		assertTrue(jsonsEqual(actual, expected));
	}

	private void getGraphAt2() throws JSONException {
		long timestamp = 2;
		List<IdentifierGraph> graphAt = mService.getGraphAt(timestamp);
		assertEquals(2, graphAt.get(0).getIdentifiers().size());

		JSONArray actual = toJson(graphAt);
		JSONArray expected = buildJSONFromYamlFile("link2", 2l);
		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getCurrentGraph() throws JSONException {
		List<IdentifierGraph> currentGraph = mService.getCurrentGraph();
		assertEquals(2, currentGraph.get(0).getIdentifiers().size());

		JSONArray actual = toJson(currentGraph);
		JSONArray expected = buildJSONFromYamlFile("link2", 2l);
		assertTrue(jsonsEqual(actual, expected));
	}

	@Override
	public void getDelta() throws JSONException {
		getDelta0to1();
		getDelta1to2();
		getDelta0to2();
	}

	private void getDelta0to1() throws JSONException {
		long t1 = 0;
		long t2 = 1;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> updates = delta.getUpdates();
		List<IdentifierGraph> deletes = delta.getDeletes();

		assertEquals(0, updates.size());
		assertEquals(1, deletes.size());

		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));

		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();

		{
			JSONObject wrapper = new JSONObject();
			wrapper.put("timestamp", 1l);
			JSONObject linksWrapper = new JSONObject();
			JSONArray linksWrapperArray = new JSONArray();
			linksWrapperArray.put(linksWrapper);
			wrapper.put("links", linksWrapperArray);
			JSONArray identifiers = new JSONArray();
			linksWrapper.put("identifiers", identifiers);
			JSONObject first = new JSONObject();
			identifiers.put(first);
			JSONObject second = new JSONObject();
			identifiers.put(second);
			JSONObject metadata = new JSONObject();
			linksWrapper.put("metadata", metadata);

			first.put("typename", "device");
			JSONObject firstProps = new JSONObject();
			first.put("properties", firstProps);
			firstProps.put("name", "device1");

			second.put("typename", "ip-address");
			JSONObject secondProbs = new JSONObject();
			second.put("properties", secondProbs);
			secondProbs.put("value", "10.0.0.1");
			secondProbs.put("type", "IPv4");

			metadata.put("typename", "device-ip");
			JSONObject metadataProbs = new JSONObject();
			metadata.put("properties", metadataProbs);
			metadataProbs.put("dhcp-server", "dhcp");

			expectedDeletes.put(wrapper);
		}

		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	private void getDelta1to2() throws JSONException {
		long t1 = 1;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> updates = delta.getUpdates();
		List<IdentifierGraph> deletes = delta.getDeletes();

		assertEquals(1, updates.size());
		assertEquals(0, deletes.size());

		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();

		{
			JSONObject wrapper = new JSONObject();
			wrapper.put("timestamp", 2l);
			JSONObject linksWrapper = new JSONObject();
			JSONArray linksWrapperArray = new JSONArray();
			linksWrapperArray.put(linksWrapper);
			wrapper.put("links", linksWrapperArray);
			JSONArray identifiers = new JSONArray();
			linksWrapper.put("identifiers", identifiers);
			JSONObject first = new JSONObject();
			identifiers.put(first);
			JSONObject second = new JSONObject();
			identifiers.put(second);
			JSONObject metadata = new JSONObject();
			linksWrapper.put("metadata", metadata);

			first.put("typename", "device");
			JSONObject firstProps = new JSONObject();
			first.put("properties", firstProps);
			firstProps.put("name", "device1");

			second.put("typename", "ip-address");
			JSONObject secondProbs = new JSONObject();
			second.put("properties", secondProbs);
			secondProbs.put("value", "10.0.0.1");
			secondProbs.put("type", "IPv4");

			metadata.put("typename", "device-ip");
			JSONObject metadataProbs = new JSONObject();
			metadata.put("properties", metadataProbs);
			metadataProbs.put("dhcp-server", "dhcp");

			expectedUpdates.put(wrapper);
		}

		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));

		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	private void getDelta0to2() throws JSONException {
		long t1 = 0;
		long t2 = 2;
		Delta delta = mService.getDelta(t1, t2);
		List<IdentifierGraph> updates = delta.getUpdates();
		List<IdentifierGraph> deletes = delta.getDeletes();
		assertEquals(0, updates.size());
		assertEquals(0, deletes.size());

		JSONArray actualUpdates = toJson(updates);
		JSONArray expectedUpdates = new JSONArray();
		assertTrue(jsonsEqual(actualUpdates, expectedUpdates));

		JSONArray actualDeletes = toJson(deletes);
		JSONArray expectedDeletes = new JSONArray();
		assertTrue(jsonsEqual(actualDeletes, expectedDeletes));
	}

	@Override
	public void getChangesMap() throws JSONException {
		SortedMap<Long, Long> changesMap = mService.getChangesMap();

		long t0 = 0;
		long t1 = 1;
		long t2 = 2;

		assertTrue(changesMap.size() == 3);

		assertEquals(1, (long) changesMap.get(t0));
		assertEquals(1, (long) changesMap.get(t1));
		assertEquals(1, (long) changesMap.get(t2));

		JSONObject actual = toJson(changesMap);
		JSONObject expected = new JSONObject();
		expected.put("0", 1l);
		expected.put("1", 1l);
		expected.put("2", 1l);
		assertTrue(jsonsEqual(actual, expected));
	}

}
