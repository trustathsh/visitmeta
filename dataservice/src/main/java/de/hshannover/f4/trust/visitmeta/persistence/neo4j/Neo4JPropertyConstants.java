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
package de.hshannover.f4.trust.visitmeta.persistence.neo4j;



import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 *
 * @author Ralf Steuerwald
 *
 */
public class Neo4JPropertyConstants {

	/**
	 * This prefix must be used for all property keys which should NOT be
	 * visible through the {@link Propable} interface.
	 */
	public static final String HIDDEN_PROPERTIES_KEY_PREFIX = "neo4j_";

	/**
	 * The key for the type of the node, e.g. values behind this key are
	 * "identifier", "link" or "Metadata".
	 */
	public static final String NODE_TYPE_KEY = HIDDEN_PROPERTIES_KEY_PREFIX + "type";
	
	/**
	 * The key for the "hash" property of nodes.
	 */
	public static final String KEY_HASH = HIDDEN_PROPERTIES_KEY_PREFIX
			+ "hash";

	/**
	 * The key for the publish timestamp of Metadata nodes.
	 */
	public static final String KEY_TIMESTAMP_PUBLISH = HIDDEN_PROPERTIES_KEY_PREFIX
			+ "start";

	/**
	 * The key for the delete timestamp of Metadata nodes.
	 */
	public static final String KEY_TIMESTAMP_DELETE = HIDDEN_PROPERTIES_KEY_PREFIX
			+ "end";

	/**
	 * The key for ifmap cardinality for Metadata nodes.
	 */
	public static final String KEY_META_CARDINALITY =
			HIDDEN_PROPERTIES_KEY_PREFIX + "cardinality";

	/**
	 * KEY_META_CARDINALITY value for single value Metadata.
	 */
	public static final String VALUE_META_CARDINALITY_SINGLE = "singleValue";

	/**
	 * KEY_META_CARDINALITY value for multi value Metadata."
	 */
	public static final String VALUE_META_CARDINALITY_MULTI = "multiValue";

	/**
	 * The key for the typename of the node, e.g. values behind this key are
	 * "ip-address", "mac-address" etc.
	 */
	public static final String KEY_TYPE_NAME = HIDDEN_PROPERTIES_KEY_PREFIX + "name";

	/**
	 * The key to access the raw XML data of a metadata or identifier node.
	 */
	public static final String KEY_RAW_DATA = HIDDEN_PROPERTIES_KEY_PREFIX + "rawData";

	/**
	 * KEY_TYPE_NAME value for Links.
	 */
	public static final String VALUE_TYPE_NAME_LINK = "link";
	
	/**
	 * KEY_TYPE_NAME value for Metadata;
	 */
	public static final String VALUE_TYPE_NAME_METADATA = "metadata";
	
	/**
	 * KEY_TYPE_NAME value for Identifiers;
	 */
	public static final String VALUE_TYPE_NAME_IDENTIFIER = "identifier";
	
	/**
	 * Key for the timestamp property of the Neo4J reference node, indicating the time of last
	 * change of the graph.
	 */
	public static final String ROOT_TIMESTAMP = "timestamp";
}
