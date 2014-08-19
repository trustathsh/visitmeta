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
 * This file is part of visitmeta dataservice, version 0.1.2,
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
/**
 * Project: Metalyzer 
 * Author: Johannes Busch
 * Author: Sören Grzanna
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.finder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Contains several Helper methods to filter
 * metadata and to format the given lists.
 * 
 * @author Sören Grzanna
 * @author Johannes Busch
 *
 */

public class MetadataFinderHelper {
	
	/**
	 * Collect all Metadata from the IdentifierGraph without doppel links
	 * @author Sören Grzanna
	 * @param graphs IdentifierGraph
	 * @return Collection<Metadata> with all Metadata from IdentifierGraph
	 */
	public static Collection<Metadata> getMetaCollection(List<IdentifierGraph> graphs) {
		Collection<Metadata> metadata = new ArrayList<Metadata>();
		HashSet<Link> copiedLinks = new HashSet<Link>();

		for(IdentifierGraph graph : graphs) {
			for(Identifier ident : graph.getIdentifiers()) {
				metadata.addAll(ident.getMetadata());
				MetadataFinderHelper.linkCheckCopy(ident.getLinks(), metadata, copiedLinks);
			}
		}
		
		return metadata;
	}
	
	/**
	 * supports the getMetaCollection
	 * @author Sören Grzanna
	 * @param linklist
	 * @param metadata
	 * @param copiedLinks
	 */
	private static void linkCheckCopy(List<Link> linklist, Collection<Metadata> metadata, HashSet<Link> copiedLinks){
		for(Link l : linklist) {
			if(! (copiedLinks.contains(l))) {
				metadata.addAll(l.getMetadata());
				copiedLinks.add(l);
			}
		}
	}
	
	/**
	 * Filtered the metadata collection with the help of HashSet of string
	 * @author Sören Grzanna
	 * @param Collection<Metadata> collection of Metadata
	 * @param HashSet<String> is used to filter
	 * @return List<Metadata> filtered List of Collection<Metadata>
	 */
	public static List<Metadata> getFiltered(Collection<Metadata> collection, HashSet<String> filter) {
		List<Metadata> metadata = new ArrayList<>();
		
		for(Metadata meta : collection){
			if(filter.contains(meta.getTypeName()) ){
				metadata.add(meta);
			}
		}
		
		return metadata;
	}
	
	/**
	 * Filtered the metadata collection with the help of string
	 * @author Sören Grzanna
	 * @param Collection<Metadata> collection of Metadata
	 * @param HashSet<String> is used to filter
	 * @return List<Metadata> filtered List of Collection<Metadata>
	 */
	public static List<Metadata> getFiltered(Collection<Metadata> collection, String filter) {
		HashSet<String> filterset = new HashSet<String>();
		filterset.add(filter);
		return getFiltered(collection, filterset);
	}
	
	
	/**
	 * Returns a new list of metadata containing the given collection.
	 * @author Johannes Busch
	 * @param collection
	 * @return
	 */
	public static List<Metadata> get(Collection<Metadata> collection) {
		List<Metadata> metadata = new ArrayList<>(collection);
		
		return metadata;
	}
}
