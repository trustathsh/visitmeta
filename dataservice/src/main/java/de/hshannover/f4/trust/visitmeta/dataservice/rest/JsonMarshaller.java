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
 * This file is part of visitmeta-dataservice, version 0.5.1,
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
package de.hshannover.f4.trust.visitmeta.dataservice.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

public class JsonMarshaller {

	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_TYPENAME = "typename";
	public static final String KEY_PROPERTIES = "properties";
	public static final String KEY_IDENTIFIERS = "identifiers";
	public static final String KEY_METADATA = "metadata";
	public static final String KEY_LINKS = "links";
	public static final String KEY_UPDATES = "updates";
	public static final String KEY_DELETES = "deletes";

	public JSONArray toJson(List<IdentifierGraph> graphs) {
		JSONArray jsonGraphs = new JSONArray();
		for (IdentifierGraph g : graphs) {
			JSONObject jsonGraph = toJson(g);
			jsonGraphs.put(jsonGraph);
		}
		return jsonGraphs;
	}

	public JSONObject toJson(IdentifierGraph graph) {
		JSONObject jsonGraph = new JSONObject();

		try {
			jsonGraph.put(KEY_TIMESTAMP, graph.getTimestamp());
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		Set<Link> seenLinks = new HashSet<>();
		JSONArray jsonLinks = new JSONArray();
		for (Identifier i : graph.getIdentifiers()) {
			for (Link l : i.getLinks()) {
				if (!seenLinks.contains(l)) {
					JSONObject jsonLink = toJson(l);
					jsonLinks.put(jsonLink);
					seenLinks.add(l);
				}
			}
			if (i.getMetadata().size() > 0) {
				JSONObject identifierWithMetadata = toJson(i, i.getMetadata());
				jsonLinks.put(identifierWithMetadata);
			}

			/**
			 * When using a filter with a maxDepth of 0, only the start identifier is in the result.
			 * This special case handles identifiers with no metadata attached to them.
			 * In normal cases, we don't want single identifier in a result, only when it is the only item in a graph.
			 */
			if (i.getMetadata().size() == 0
					&& graph.getIdentifiers().size() == 1) {
				JSONObject identifierWithoutMetadataAndLinks = toJson(i, null);
				jsonLinks.put(identifierWithoutMetadataAndLinks);
			}
		}
		try {
			jsonGraph.put(KEY_LINKS, jsonLinks);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonGraph;
	}

	public JSONObject toJson(Identifier identifier, List<Metadata> metadata) {
		JSONObject identifierWithMetadata = new JSONObject();

		JSONObject jsonIdentifier = toJson(identifier);
		try {
			identifierWithMetadata.put(KEY_IDENTIFIERS, jsonIdentifier);

			if (metadata != null) {
				for (Metadata m : metadata) {
					JSONObject jsonMetadata = toJson(m);
					identifierWithMetadata.accumulate(KEY_METADATA, jsonMetadata);
				}
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}

		return identifierWithMetadata;
	}

	public JSONObject toJson(Link link) {
		JSONObject jsonLink = new JSONObject();
		IdentifierPair pair = link.getIdentifiers();
		JSONObject jsonId1 = toJson(pair.getFirst());
		JSONObject jsonId2 = toJson(pair.getSecond());

		try {
			jsonLink.accumulate(KEY_IDENTIFIERS, jsonId1);
			jsonLink.accumulate(KEY_IDENTIFIERS, jsonId2);

			for (Metadata m : link.getMetadata()) {
				JSONObject jsonMetadata = toJson(m);
				jsonLink.accumulate(KEY_METADATA, jsonMetadata);
			}
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonLink;
	}

	public JSONObject toJson(Propable propable) {
		JSONObject jsonPropable = new JSONObject();
		try {
			jsonPropable.put(KEY_TYPENAME, propable.getTypeName());

			JSONObject jsonProperties = new JSONObject();
			for (String property : propable.getProperties()) {
				jsonProperties.put(property, propable.valueFor(property));
			}
			jsonPropable.put(KEY_PROPERTIES, jsonProperties);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonPropable;
	}

	public JSONObject toJson(Delta delta) {
		JSONObject jsonDelta = new JSONObject();

		JSONArray jsonUpdates = toJson(delta.getUpdates());
		JSONArray jsonDeletes = toJson(delta.getDeletes());

		try {
			jsonDelta.put(KEY_UPDATES, jsonUpdates);
			jsonDelta.put(KEY_DELETES, jsonDeletes);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
		return jsonDelta;
	}

	public JSONObject toJson(Map<Long, Long> changes) {
		JSONObject newJSONObject = new JSONObject();
		for (Entry<Long, Long> entry : changes.entrySet()) {
			try {
				newJSONObject.put(entry.getKey().toString(), entry.getValue());
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		return newJSONObject;
	}
}
