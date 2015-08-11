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
 * This file is part of visitmeta-dataservice, version 0.5.0,
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

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.NODE_TYPE_KEY;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_TYPE_NAME_IDENTIFIER;

import org.junit.Test;
import org.mockito.Mockito;
import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphType;
import scala.collection.Iterator;

/**
 * Test-class for Neo4JExecutor.
 *
 * @author Johannes Busch
 *
 */
public class Neo4JExecutorTest {

	private ExecutionEngine cypher;

	private Neo4JExecutor executor;

	private String query;

	@Test
	public void testVerifyCountIdentifierQueryAtCurrent() {
		givenNewIdentifierExecutor(GraphType.IDENTIFIER);

		executor.count(GraphType.IDENTIFIER);

		Mockito.verify(cypher).execute(query);
	}

	private void givenNewIdentifierExecutor(GraphType type) {
		cypher = Mockito.mock(ExecutionEngine.class);

		@SuppressWarnings("unchecked")
		Iterator<Object> nodes = Mockito.mock(Iterator.class);

		Mockito.when(nodes.hasNext()).thenReturn(true);
		Mockito.when(nodes.next()).thenReturn(2L);

		ExecutionResult result = Mockito.mock(ExecutionResult.class);

		Mockito.when(result.columnAs("result")).thenReturn(nodes);

		if (GraphType.IDENTIFIER == type) {
			buildIdentifierQuerieAtCurrent();

			Mockito.when(cypher.execute(query)).thenReturn(result);
		}

		executor = new Neo4JExecutor(cypher);
	}

	private void buildIdentifierQuerieAtCurrent() {
		String source = "START i=node:node_auto_index('"
				+ NODE_TYPE_KEY + ":"
				+ VALUE_TYPE_NAME_IDENTIFIER + "')";
		String match = "MATCH (m)-[:Meta]-(i)-[:Link]-(l)-[:Meta]-(m)";
		String countReturn = "RETURN count(distinct i) as result";
		String current = "WHERE (m."
				+ KEY_TIMESTAMP_DELETE + " = "
				+ InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP + ")";

		query = source
				+ " " + match + " " + current + " " + countReturn;
	}
}
