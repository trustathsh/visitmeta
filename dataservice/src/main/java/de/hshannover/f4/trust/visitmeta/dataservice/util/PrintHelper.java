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
package de.hshannover.f4.trust.visitmeta.dataservice.util;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.tooling.GlobalGraphOperations;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class PrintHelper {

	public static String printIdentifierGraph(IdentifierGraph graph) {
		StringBuilder sb = new StringBuilder();
		sb.append("---- IdentifierGraph ----\n");
		sb.append("timestamp: "
				+ graph.getTimestamp());
		for (Identifier i : graph.getIdentifiers()) {
			sb.append(i);
			for (Metadata m : i.getMetadata()) {
				sb.append("\t"
						+ m);
			}
			for (Link l : i.getLinks()) {
				sb.append("\t"
						+ l);
				for (Metadata m : l.getMetadata()) {
					sb.append("\t\t"
							+ m);
				}
			}
		}
		sb.append("---- ----\n");
		return sb.toString();
	}

	public static String printIdentifierGraph(InternalIdentifierGraph graph) {
		StringBuilder sb = new StringBuilder();
		sb.append("---- IdentifierGraph ----\n");
		sb.append("timestamp: "
				+ graph.getTimestamp() + "\n");
		for (InternalIdentifier i : graph.getIdentifiers()) {
			sb.append(i
					+ "\n");
			for (InternalMetadata m : i.getMetadata()) {
				sb.append("\t"
						+ m + "\n");
			}
			for (InternalLink l : i.getLinks()) {
				sb.append("\t"
						+ l + "\n");
				for (InternalMetadata m : l.getMetadata()) {
					sb.append("\t\t"
							+ m + "\n");
				}
			}
		}
		sb.append("---- ----\n");
		sb.append("---- ----\n");
		return sb.toString();
	}

	public static void printIdentifierGraphs(List<IdentifierGraph> graphs) {
		StringBuilder sb = new StringBuilder();
		sb.append("---- List<IdentifierGraph> ----\n");
		for (IdentifierGraph g : graphs) {
			sb.append(printIdentifierGraph(g));
		}
		sb.append("---- ----");
	}

	public static String printInternalIdentifierGraphs(List<InternalIdentifierGraph> graphs) {
		StringBuilder sb = new StringBuilder();
		sb.append("---- List<InternalIdentifierGraph> ----\n");
		for (InternalIdentifierGraph g : graphs) {
			sb.append(printIdentifierGraph(g));
		}
		sb.append("---- ----\n");
		return sb.toString();
	}

	public static String printNeo4JDump(GraphDatabaseService db) {
		StringBuilder sb = new StringBuilder();
		for (Node n : GlobalGraphOperations.at(db).getAllNodes()) {
			for (String s : n.getPropertyKeys()) {
				sb.append(s
						+ " : " + n.getProperty(s) + "\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

}
