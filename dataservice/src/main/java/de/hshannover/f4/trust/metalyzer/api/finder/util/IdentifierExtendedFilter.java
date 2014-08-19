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
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.finder.util;

import java.util.HashSet;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * IdentifierFilter filters a given List of Identifiers by the given HashSet.
 * Also it removes all not wanted Metadata given by meta.
 * 
 * @author Johannes Busch
 *
 */
public class IdentifierExtendedFilter extends GenericElementFilter<Identifier> {
	
	/**
	 * Identifier filter list.
	 */
	private HashSet<String> idents;
	/**
	 * Metadata filter list.
	 */
	private HashSet<String> meta;
	
	/**
	 * Initialiases a new filter with the given identifier and metadata sets.
	 * @param idents
	 * @param meta
	 */
	public IdentifierExtendedFilter(HashSet<String> idents, HashSet<String> meta) {
		this.idents = idents;
		this.meta = meta;
	}
	
	/**
	 * Filters the given List of Identifier. Also removes not wanted Metadata.
	 */
	@Override
	public void filter(List<Identifier> idents) {
		for(Identifier ident : idents) {
			if(this.idents.contains(ident.getTypeName())) {
				
				filterMetadata(ident.getMetadata());
				
				for(Link l : ident.getLinks()) {
					filterMetadata(l.getMetadata());
				}
				
				elements.add(ident);
			}
		}
	}
	
	
	/**
	 * Filters the given metadata list against the given filter.
	 * @param meta
	 */
	private void filterMetadata(List<Metadata> meta) {
		for(int i=0;i<meta.size();i++) {
			if(!this.meta.contains(meta.get(i).getTypeName())) {
				meta.remove(i);
			}
		}
	}

}
