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
 * This file is part of visitmeta-dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.NODE_TYPE_KEY;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_IDENTIFIER;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_LINK;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_METADATA;

import org.apache.log4j.Logger;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.cypher.SyntaxException;
import org.neo4j.kernel.impl.util.StringLogger;

import scala.collection.Iterator;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;
import de.hshannover.f4.trust.visitmeta.persistence.Executor;

/**
 * Implements the Executor interface to allow deeper queries against the neo4J
 * database.
 * 
 * @author Johannes Busch
 * 
 */
public class Neo4JExecutor implements Executor {

	private ExecutionEngine cypher;

	private static final Logger log = Logger.getLogger(Neo4JExecutor.class);

	private final String ALL_IDENTIFIER = "START i=node:node_auto_index('"
			+ NODE_TYPE_KEY + ":" + VALUE_TYPE_NAME_IDENTIFIER + "')";

	private final String ALL_LINKS = "START l=node:node_auto_index('"
			+ NODE_TYPE_KEY + ":" + VALUE_TYPE_NAME_LINK + "')";

	private final String ALL_METADATA = "START m=node:node_auto_index('"
			+ NODE_TYPE_KEY + ":" + VALUE_TYPE_NAME_METADATA + "')";

	/**
	 * Creates a new Executor wich queries the given database.
	 * 
	 * @param db
	 */
	public Neo4JExecutor(Neo4JConnection db) {
		cypher = new ExecutionEngine(db.getConnection(), StringLogger.DEV_NULL);
	}

	/**
	 * Creates a new executor with the given cypher engine. Just for testing
	 * purpose.
	 * 
	 * @param cypher
	 */
	public Neo4JExecutor(ExecutionEngine cypher) {
		this.cypher = cypher;
	}

	/**
	 * Returns a number of all current identifier in the graph.
	 * 
	 * @return
	 */
	@Override
	public long count(GraphType type) {
		String current = "WHERE (m." + KEY_TIMESTAMP_DELETE + " = "
				+ InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + ")";

		ExecutionResult result = executeQuery(buildQuerie(type, current));

		return getResult(result);
	}

	/**
	 * Builds a Queries for the given type. Uses for identifier an "or" match by
	 * using optional relations.
	 * 
	 * @param type
	 * @return
	 */
	private String buildQuerie(GraphType type, String time) {
		String source = "", match = "", countReturn = "";

		if (GraphType.IDENTIFIER == type) {
			source = ALL_IDENTIFIER;

			// Using an "or" in match
			match = "MATCH (m)-[:Meta]-(i)-[:Link]-(l)-[:Meta]-(m)";

			countReturn = "RETURN count(distinct i) as result";

		} else if (GraphType.LINK == type) {
			source = ALL_LINKS;

			match = "MATCH (l)-[:Meta]-(m)";

			countReturn = "RETURN count(distinct l) as result";

		} else if (GraphType.METADATA == type) {
			source = ALL_METADATA;

			countReturn = "RETURN count(distinct m) as result";

		} else {
			log.error("No Querie for this type known ( possibly null )");
			throw new RuntimeException(
					"No Querie for this type known ( possibly null )");
		}

		return source + " " + match + " " + time + " " + countReturn;
	}

	/**
	 * Executes the given query against the database and returns the
	 * ExecutionResult.
	 * 
	 * @param query
	 * @return
	 */
	private ExecutionResult executeQuery(String query) {
		try {
			return cypher.execute(query);
		} catch (SyntaxException exception) {
			log.error(exception.getMessage());
			throw new RuntimeException(
					"Database query exception. More Infos in log-file!");
		}
	}

	private long getResult(ExecutionResult result) {
		Iterator<Long> nodes = result.columnAs("result");

		if (nodes.hasNext()) {
			return nodes.next();
		}

		log.error("Error occured while counting!");
		throw new RuntimeException("Error occured while counting!");
	}

	@Override
	public long count(GraphType type, long timestamp) {
		String published = "WHERE (m. " + KEY_TIMESTAMP_PUBLISH + " <= "
				+ timestamp + " and ";
		String not_Deleted = "(m." + KEY_TIMESTAMP_DELETE + " = "
				+ InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + " or ";
		String deleted = "m." + KEY_TIMESTAMP_DELETE + " >= " + timestamp
				+ " ) )";

		ExecutionResult result = executeQuery(buildQuerie(type, published
				+ not_Deleted + deleted));

		return getResult(result);
	}

	@Override
	public long count(GraphType type, long from, long to) {
		String published = "WHERE (m. " + KEY_TIMESTAMP_PUBLISH + " <= " + to
				+ " and ";
		String not_Deleted = "(m." + KEY_TIMESTAMP_DELETE + " = "
				+ InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + " or ";
		String deleted = "m." + KEY_TIMESTAMP_DELETE + " >= " + from + " ) )";

		ExecutionResult result = executeQuery(buildQuerie(type, published
				+ not_Deleted + deleted));

		return getResult(result);
	}

	@Override
	public double meanOfEdges() {
		String current = "WHERE (m." + KEY_TIMESTAMP_DELETE + " = "
				+ InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + ")";

		String querie = ALL_IDENTIFIER + " MATCH (i)-[:Link]-(l)-[:Meta]-(m) "
				+ current + " RETURN count(l) / count(distinct i ) as result";

		ExecutionResult result = executeQuery(querie);

		Iterator<Double> nodes = result.columnAs("result");

		if (nodes.hasNext()) {
			return nodes.next();
		}

		log.error("Error occured while counting!");
		throw new RuntimeException("Error occured while counting!");
	}

	@Override
	public double meanOfEdges(long timestamp) {
		String published = "WHERE (m. " + KEY_TIMESTAMP_PUBLISH + " <= "
				+ timestamp + " and ";
		String not_Deleted = "(m." + KEY_TIMESTAMP_DELETE + " = "
				+ InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + " or ";
		String deleted = "m." + KEY_TIMESTAMP_DELETE + " >= " + timestamp
				+ " ) )";

		String time = published + not_Deleted + deleted;

		String querie = ALL_IDENTIFIER + " MATCH (i)-[:Link]-(l)-[:Meta]-(m) "
				+ time + " RETURN count(l) / count(distinct i ) as result";

		ExecutionResult result = executeQuery(querie);

		Iterator<Double> nodes = result.columnAs("result");

		if (nodes.hasNext()) {
			return nodes.next();
		}

		log.error("Error occured while counting!");
		throw new RuntimeException("Error occured while counting!");
	}
}
