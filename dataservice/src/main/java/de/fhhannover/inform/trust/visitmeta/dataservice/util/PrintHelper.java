package de.fhhannover.inform.trust.visitmeta.dataservice.util;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.tooling.GlobalGraphOperations;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.fhhannover.inform.trust.visitmeta.interfaces.Identifier;
import de.fhhannover.inform.trust.visitmeta.interfaces.IdentifierGraph;
import de.fhhannover.inform.trust.visitmeta.interfaces.Link;
import de.fhhannover.inform.trust.visitmeta.interfaces.Metadata;

public class PrintHelper {

	public static void printIdentifierGraph(IdentifierGraph graph) {
		System.out.println("---- IdentifierGraph ----");
		System.out.println("timestamp: " + graph.getTimestamp());
		for (Identifier i : graph.getIdentifiers()) {
			System.out.println(i);
			for (Metadata m : i.getMetadata()) {
				System.out.println("\t" + m);
			}
			for (Link l : i.getLinks()) {
				System.out.println("\t" + l);
				for (Metadata m : l.getMetadata()) {
					System.out.println("\t\t" + m);
				}
			}
		}
		System.out.println("---- ----");
	}

	public static void printIdentifierGraph(InternalIdentifierGraph graph) {
		System.out.println("---- IdentifierGraph ----");
		System.out.println("timestamp: " + graph.getTimestamp());
		for (InternalIdentifier i : graph.getIdentifiers()) {
			System.out.println(i);
			for (InternalMetadata m : i.getMetadata()) {
				System.out.println("\t" + m);
			}
			for (InternalLink l : i.getLinks()) {
				System.out.println("\t" + l);
				for (InternalMetadata m : l.getMetadata()) {
					System.out.println("\t\t" + m);
				}
			}
		}
		System.out.println("---- ----");
		System.out.println("---- ----");
	}

	public static void printIdentifierGraphs(List<IdentifierGraph> graphs) {
		System.out.println("---- List<IdentifierGraph> ----");
		for (IdentifierGraph g : graphs) {
			printIdentifierGraph(g);
		}
		System.out.println("---- ----");
	}

	public static void printInternalIdentifierGraphs(List<InternalIdentifierGraph> graphs) {
		System.out.println("---- List<InternalIdentifierGraph> ----");
		for (InternalIdentifierGraph g : graphs) {
			printIdentifierGraph(g);
		}
		System.out.println("---- ----");
	}

	public static void printNeo4JDump(GraphDatabaseService db) {
		for (Node n : GlobalGraphOperations.at(db).getAllNodes()) {
			for (String s : n.getPropertyKeys()) {
				System.out.println(s + " : " + n.getProperty(s));
			}
			System.out.println();
		}
	}

}