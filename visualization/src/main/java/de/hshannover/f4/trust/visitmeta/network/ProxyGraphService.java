/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-visualization, version 0.5.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.network;





import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.WebResource;

import de.hshannover.f4.trust.visitmeta.implementations.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.implementations.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.implementations.LinkImpl;
import de.hshannover.f4.trust.visitmeta.implementations.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.implementations.internal.DeltaImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphFilter;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 *
 * @author Ralf Steuerwald
 * @author Bastian Hellmann
 *
 */
public class ProxyGraphService implements GraphService {

	private static final Logger log = Logger.getLogger(ProxyGraphService.class);

	private WebResource mService;
	private boolean mIncludeRawXML;
	private ObjectMapper mObjectMapper = new ObjectMapper();

	private static final String TIMESTAMP   = "timestamp";
	private static final String LINKS       = "links";
	private static final String IDENTIFIERS = "identifiers";
	private static final String TYPENAME    = "typename";
	private static final String PROPERTIES  = "properties";
	private static final String METADATA    = "metadata";
	private static final String DELTA_UPDATES = "updates";
	private static final String DELTA_DELETES = "deletes";
	private static final String RAW_XML = "rawData";

	public ProxyGraphService(WebResource service) {
		mService = service;
		mIncludeRawXML = false;
	}

	public ProxyGraphService(WebResource service, boolean includeRawXML) {
		mService = service;
		mIncludeRawXML = includeRawXML;
	}

	/**
	 * @throws RuntimeException
	 * @throws {@link NumberFormatException}
	 */
	@Override
	public SortedMap<Long, Long> getChangesMap() {
		log.trace("Method getChangesMap() called.");
		String json = mService
				.path("changes")
				.queryParam("rawData", Boolean.toString(mIncludeRawXML))
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);
		SortedMap<Long, Long> changesMap = new TreeMap<>();

		JsonNode node = parseJson(json);
		Iterator<String> keys = node.getFieldNames();
		while (keys.hasNext()) {
			String key = keys.next();
			JsonNode valueNode = node.get(key);
			if (valueNode.isInt()) {
				long timestamp = Long.parseLong(key);
				long changes   = valueNode.getIntValue();
				changesMap.put(timestamp, changes);
			} else {
				throw new RuntimeException("value of '"+key+"' is not an int");
			}
		}
		return changesMap;
	}

	@Override
	public List<IdentifierGraph> getInitialGraph() {
		log.trace("Method getInitialGraph() called.");
		String json = mService
				.path("initial")
				.queryParam("rawData", Boolean.toString(mIncludeRawXML))
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);

		JsonNode rootNode = parseJson(json);
		return extractGraphsFromJson(rootNode);
	}

	@Override
	public List<IdentifierGraph> getGraphAt(long timestamp) {
		log.trace("Method getGraphAt(" + timestamp + ") called.");
		String json = mService
				.path(Long.toString(timestamp))
				.queryParam("rawData", Boolean.toString(mIncludeRawXML))
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);

		JsonNode rootNode = parseJson(json);
		return extractGraphsFromJson(rootNode);
	}

	@Override
	public List<IdentifierGraph> getNotifiesAt(long timestamp) {
		//TODO not yet implemented
		log.info("not yet implemented!");
		return null;
	}

	@Override
	public List<IdentifierGraph> getCurrentGraph() {
		log.trace("Method getCurrentGraph() called.");
		String json = mService
				.path("current")
				.queryParam("rawData", Boolean.toString(mIncludeRawXML))
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);

		JsonNode rootNode = parseJson(json);
		return extractGraphsFromJson(rootNode);
	}

	@Override
	public List<IdentifierGraph> getInitialGraph(GraphFilter filter) {
		log.trace("Method getInitialGraph(" + filter + ") called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public List<IdentifierGraph> getGraphAt(long timestamp, GraphFilter filter) {
		log.trace("Method getGraphAt(" + timestamp + ", " + filter + ") called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public List<IdentifierGraph> getCurrentGraph(GraphFilter filter) {
		log.trace("Method getCurrentGraph(" + filter + ") called.");
		throw new UnsupportedOperationException();
	}

	@Override
	public Delta getDelta(long t1, long t2) {
		log.trace("Method getDelta(" + t1 + "," + t2 + ") called.");
		String json = mService
				.path(Long.toString(t1) + "/" + Long.toString(t2))
				.queryParam("rawData", Boolean.toString(mIncludeRawXML))
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);

		return extractDeltasFromJson(json);
	}

	private Delta extractDeltasFromJson(String json) {
		List<IdentifierGraph> updates = new ArrayList<IdentifierGraph>();
		List<IdentifierGraph> deletes = new ArrayList<IdentifierGraph>();

		// TODO fill lists
		/**
		 * {"updates":[],"deletes":[]}
		 * multiple timestamps
		 */
		JsonNode rootNode = parseJson(json);

		JsonNode jsonDeltaUpdates = rootNode.get(DELTA_UPDATES);
		if (jsonDeltaUpdates != null) {
			log.trace("Found updates on delta");
			if (jsonDeltaUpdates.isArray()) {
				Iterator<JsonNode> updateElements = jsonDeltaUpdates.getElements();
				while (updateElements.hasNext()) {
					updates.add(buildGraphFromJson(updateElements.next()));
				}
			}
		}

		JsonNode jsonDeltaDeletes = rootNode.get(DELTA_DELETES);
		if (jsonDeltaUpdates != null) {
			log.trace("Found deletes on delta");
			if (jsonDeltaDeletes.isArray()) {
				Iterator<JsonNode> deleteElements = jsonDeltaDeletes.getElements();
				while (deleteElements.hasNext()) {
					deletes.add(buildGraphFromJson(deleteElements.next()));
				}
			}
		}

		return new DeltaImpl(deletes, updates);
	}

	private IdentifierGraph buildGraphFromJson(JsonNode jsonGraph) {
		log.trace("Method buildGraphFromJson(...) called.");
		log.trace("Parameter 'jsonGraph': " + jsonGraph);
		JsonNode jsonTimestamp = jsonGraph.get(TIMESTAMP);
		if (jsonTimestamp == null) {
			throw new RuntimeException("timestamp for graph is missing in '"+jsonGraph+"'");
		}
		if (!jsonTimestamp.isLong()) {
			throw new RuntimeException("timestamp is not a long in '"+jsonGraph+"'");
		}
		long timestamp = jsonTimestamp.getLongValue();

		IdentifierGraphImpl graph = new IdentifierGraphImpl(timestamp);

		JsonNode jsonlinkList = jsonGraph.get(LINKS);
		if (jsonlinkList == null) {
			throw new RuntimeException("no links in graph '"+jsonGraph+"' found");
		}

		insertIdentifierInto(graph, jsonlinkList);

		return graph;
	}

	private void insertIdentifierInto(IdentifierGraphImpl graph, JsonNode jsonLinkList) {
		log.trace("Method insertIdentifierInto(...) called.");
		Iterator<JsonNode> jsonLinks = jsonLinkList.getElements();
		while (jsonLinks.hasNext()) {
			JsonNode jsonLink = jsonLinks.next();
			JsonNode identifiers = jsonLink.get(IDENTIFIERS);
			JsonNode metadata = jsonLink.get(METADATA);

			// two identifiers -> link
			if (identifiers.isArray()) {
				log.trace("JsonNode contains two identifiers");

				Iterator<JsonNode> ids = identifiers.getElements();
				List<IdentifierImpl> identifierList = new ArrayList<IdentifierImpl>();
				while (ids.hasNext()) {
					JsonNode currentIdentifier = ids.next();
					IdentifierImpl identifier = (IdentifierImpl) buildIdentifierFromJson(currentIdentifier);
					identifierList.add(identifier);
				}

				for (IdentifierImpl identifier : identifierList) {
					IdentifierImpl identifierFound = graph.findIdentifier(identifier);
					if (identifierFound == null) {
						log.trace("Identifier not found in graph, inserting: " + identifier);
						graph.insert(identifier);
					}
					else {
						identifierList.set(identifierList.indexOf(identifier), identifierFound);
					}
				}

				LinkImpl linkImpl = graph.connect(identifierList.get(0), identifierList.get(1));

				log.trace("Adding metadata to link'" + linkImpl + "'");
				List<Metadata> metadataList = extractMetadata(metadata);
				for (Metadata m : metadataList) {
					linkImpl.addMetadata(m);
				}

				log.trace("Creating link: " + linkImpl);

				// one identifier
			} else {
				log.trace("JsonNode contains one identifier");

				// convert Identifier to IdentifierImpl
				IdentifierImpl identifier = (IdentifierImpl) buildIdentifierFromJson(identifiers);

				// FIXME InternalIdentifier on the client side? bah?!
				// TODO <VA> Who wrote the above FIXME comment and what shall it mean? Is it already fixed?

				// insert identifier into graph
				IdentifierImpl identifierFound = graph.findIdentifier(identifier);
				if (identifierFound == null) {
					log.trace("Identifier not found, inserting: " + identifier);
					graph.insert(identifier);
				}
				else {
					identifier = identifierFound;
				}

				log.trace("Adding metadata to identifier '" + identifier + "'");
				List<Metadata> metadataList = extractMetadata(metadata);
				for (Metadata m : metadataList) {
					identifier.addMetadata(m);
				}

			}
		}
	}

	private List<Metadata> extractMetadata(JsonNode jsonNode) {
		log.trace("Method extractMetadata(..) called");
		log.trace("Parameter 'jsonNode':" + jsonNode);
		if (jsonNode == null) {
			log.trace("No metadata found.");
			return new ArrayList<Metadata>();
		}

		Metadata metadatum = null;
		List<Metadata> metadataList = new ArrayList<Metadata>();

		// multiple metadata
		if (jsonNode.isArray()) {
			Iterator<JsonNode> metadatas = jsonNode.getElements();
			JsonNode currentMetadata = null;
			while (metadatas.hasNext()) {
				currentMetadata = metadatas.next();
				metadatum = buildMetadataFromJson(currentMetadata);
				metadataList.add(metadatum);
			}

			// only one metadatum
		} else {
			metadatum = buildMetadataFromJson(jsonNode);
			metadataList.add(metadatum);
		}

		return metadataList;
	}

	private Metadata buildMetadataFromJson(JsonNode jsonMetadata) {
		log.trace("Method buildIdentifierFromJson(...) called.");
		log.trace("Parameter 'jsonIdentifier': " + jsonMetadata);
		JsonNode jsonTypename = jsonMetadata.get(TYPENAME);
		if (jsonTypename == null) {
			throw new RuntimeException("no typename found for metadata '"+jsonMetadata+"'");
		}
		String typename = jsonTypename.getValueAsText();

		JsonNode jsonProperties = jsonMetadata.get(PROPERTIES);
		if (jsonProperties == null) {
			throw new RuntimeException("no properties found for metadata '"+jsonMetadata+"'");
		}

		boolean isSingleValue = false;
		long publishTimestamp = 0;
		Map<String, String> propertiesMap = new HashMap<String, String>();

		Iterator<String> properties = jsonProperties.getFieldNames();
		while (properties.hasNext()) {
			String key = properties.next();
			String value = jsonProperties.get(key).getValueAsText();

			if (key.contains("@ifmap-cardinality")) {
				isSingleValue = value.equalsIgnoreCase("singleValue") ? true : false;
			} else if (key.contains("[@ifmap-timestamp]")) {
				publishTimestamp = getXsdStringAsCalendar(value);
			}

			propertiesMap.put(key, value);
		}

		log.trace("Creating metadata (typename: " + typename + ", isSingleValue: " + isSingleValue + ", publishTimestamp: " + publishTimestamp + ")");
		MetadataImpl metadata = new MetadataImpl(typename, isSingleValue, publishTimestamp);
		for (String key : propertiesMap.keySet()) {
			log.trace("Adding property to metadata: " + key + " = " + propertiesMap.get(key));
			metadata.addProperty(key, propertiesMap.get(key));
		}

		if (mIncludeRawXML) {
			JsonNode jsonRawXML = jsonMetadata.get(RAW_XML);
			if (jsonRawXML != null ) {
				metadata.setRawData(jsonRawXML.getValueAsText());
			}
		}

		return metadata;
	}

	private Identifier buildIdentifierFromJson(JsonNode jsonIdentifier) {
		log.trace("Method buildIdentifierFromJson(...) called.");
		log.trace("Parameter 'jsonIdentifier': " + jsonIdentifier);
		JsonNode jsonTypename = jsonIdentifier.get(TYPENAME);
		if (jsonTypename == null) {
			throw new RuntimeException("no typename found for identifier '"+jsonIdentifier+"'");
		}
		String typename = jsonTypename.getValueAsText();
		IdentifierImpl identifier = new IdentifierImpl(typename);

		JsonNode jsonProperties = jsonIdentifier.get(PROPERTIES);
		if (jsonProperties == null) {
			throw new RuntimeException("no properties found for identifier '"+jsonIdentifier+"'");
		}
		Iterator<String> properties = jsonProperties.getFieldNames();
		while (properties.hasNext()) {
			String key = properties.next();
			String value = jsonProperties.get(key).getValueAsText();

			identifier.addProperty(key, value);
		}

		if (mIncludeRawXML) {
			JsonNode jsonRawXML = jsonIdentifier.get(RAW_XML);
			if (jsonRawXML != null ) {
				identifier.setRawData(jsonRawXML.getValueAsText());
			}
		}

		return identifier;
	}

	/**
	 * @throws RuntimeException
	 */
	private JsonNode parseJson(String json) {
		log.trace("Method parseJson(...) called.");
		log.trace("Parameter 'json': " + json);
		try {
			// TODO <VA>: debug only, remove later?
			ObjectMapper mapper = new ObjectMapper();
			Object jsonObject = mapper.readValue(json, Object.class);
			String jsonFormatted = mapper.defaultPrettyPrintingWriter().writeValueAsString(jsonObject);
			log.debug("Parameter 'json' (formatted) in parseJson():\n" + jsonFormatted);

			return mObjectMapper.readTree(json);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("could not parse '"+json+"' as JSON: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException("error while reading '"+json+"' as JSON: " + e.getMessage(), e);
		}
	}

	/**
	 * @param json
	 * @return
	 */
	private List<IdentifierGraph> extractGraphsFromJson(JsonNode rootNode) {
		List<IdentifierGraph> graphs = new ArrayList<IdentifierGraph>();
		Iterator<JsonNode> jsonGraphs = rootNode.getElements();
		while (jsonGraphs.hasNext()) {
			JsonNode currentJsonGraph = jsonGraphs.next();
			IdentifierGraph graph = buildGraphFromJson(currentJsonGraph);
			graphs.add(graph);
		}
		return graphs;
	}

	/**
	 * Transforms a given xsd:DateTime {@link String} (e.g. 2003-05-31T13:20:05-05:00) to a Java {@link Calendar} object. Uses DatetypeConverter to parse the
	 * {@link String} object.
	 *
	 * @param xsdDateTime - the xsd:DateTime that is to be transformed.
	 * @return a {@link Calendar} object representing the given xsd:DateTime
	 */
	private Long getXsdStringAsCalendar(String xsdDateTime) {
		assert xsdDateTime != null && !xsdDateTime.isEmpty();
		try {
			if (xsdDateTime.contains("+")) {
				int idxTz = xsdDateTime.lastIndexOf("+");
				int idxLastColon = xsdDateTime.lastIndexOf(":");
				if(idxLastColon < idxTz) {
					String p1 = xsdDateTime.substring(0, idxTz+3);
					String p2 = xsdDateTime.substring(idxTz+3, xsdDateTime.length());
					xsdDateTime = p1 + ":" + p2;
				}
			}
			if (xsdDateTime.contains(":")) {	// if the String contains an ':' literal, we try to interpret it as a xsdDateTime-string
				return DatatypeConverter.parseDateTime(xsdDateTime).getTimeInMillis();
			} else {	// try to parse a time in milliseconds to a Calendar object
				Calendar tmp = new GregorianCalendar();
				tmp.setTimeInMillis(Long.parseLong(xsdDateTime));
				return tmp.getTimeInMillis();
			}
		} catch (IllegalArgumentException e) {
			log.error("Illegal data/time format found (incoming String was: "
					+ xsdDateTime + "); setting to current date/time.");
			return new GregorianCalendar().getTimeInMillis();
		}
	}

	@Override
	public long count(GraphType type) {
		log.error("Not implemented!");
		throw new RuntimeException("Not implemented!");
	}

	@Override
	public long count(GraphType type, long timestamp) {
		log.error("Not implemented!");
		throw new RuntimeException("Not implemented!");
	}

	@Override
	public long count(GraphType type, long from, long to) {
		log.error("Not implemented!");
		throw new RuntimeException("Not implemented!");
	}

	@Override
	public double meanOfEdges() {
		log.error("Not implemented!");
		throw new RuntimeException("Not implemented!");
	}

	@Override
	public double meanOfEdges(long timestamp) {
		log.error("Not implemented!");
		throw new RuntimeException("Not implemented!");
	}
}
