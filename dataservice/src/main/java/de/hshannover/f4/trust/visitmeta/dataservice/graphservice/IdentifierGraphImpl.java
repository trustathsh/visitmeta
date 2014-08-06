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
 * This file is part of visitmeta dataservice, version 0.1.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;



import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class IdentifierGraphImpl implements IdentifierGraph {
	/**
	 * List of all Identifiers that are in the graph.
	 */
	private List<Identifier> mIdentifiers;
	/**
	 * Unix Epoch representing the time of the last update to the graph.
	 */
	private long mTimestamp;


	public IdentifierGraphImpl(long timestamp) {
		mTimestamp = timestamp;
		mIdentifiers = new ArrayList<Identifier>();
	}

	@Override
	public Identifier getStartIdentifier() {
		return null;
	}

	@Override
	public List<Identifier> getIdentifiers() {
		return mIdentifiers;
	}

	@Override
	public long getTimestamp() {
		return mTimestamp;
	}

	/**
	 * Searches the graph for an {@link IdentifierImpl} representation of the
	 * {@link InternalIdentifier} passed as argument.
	 * @param id {@link InternalIdentifier} to look for.
	 * @return
	 * {@link IdentifierImpl} representation of the {@link InternalIdentifier} passed as argument.
	 * Null otherwise.
	 */
	public IdentifierImpl findIdentifier(InternalIdentifier id) {
		for (Identifier identifier : mIdentifiers) {
			if (id.sameAs(identifier)) {
				return (IdentifierImpl)identifier;
			}
		}
		return null;
	}

	/**
	 * Takes an {@link Identifier and inserts it into the graph by copying its data into a
	 * {@link IdentifierImpl} representation.
	 * @param id
	 */
	public void insert(Identifier id) {
		mIdentifiers.add(id);
	}

	/**
	 * Connects two Identifiers in the graph by creating a link and adding it to each Identifier
	 * @param id1
	 * @param id2
	 * @return The freshly created link.
	 */
	public LinkImpl connect(IdentifierImpl id1,
			IdentifierImpl id2) {
		if (mIdentifiers.contains(id1) && mIdentifiers.contains(id2)) {
			LinkImpl l = new LinkImpl(id1, id2);
			id1.addLink(l);
			id2.addLink(l);
			return l;
		}
		throw new IllegalArgumentException(
				"Trying to connect two Identifiers of which at least one is not in the graph ... "+
				"we strongly disapprove");
	}

}
