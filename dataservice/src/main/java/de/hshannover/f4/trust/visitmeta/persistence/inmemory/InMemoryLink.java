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
 * This file is part of visitmeta dataservice, version 0.0.3,
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
package de.fhhannover.inform.trust.visitmeta.persistence.inmemory;




import java.util.ArrayList;
import java.util.List;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierPair;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InMemoryLink extends InternalLink {
	private InternalIdentifier mFirstIdentifier;
	private InternalIdentifier mSecondIdentifier;
	private List<InternalMetadata> mMeta;

	private InMemoryLink() {
		mMeta = new ArrayList<InternalMetadata>();
	}

	public InMemoryLink(InternalIdentifier id1, InternalIdentifier id2) {
		this();
		mFirstIdentifier = id1;
		mSecondIdentifier = id2;

	}

	/**
	 * Creates a new InternalLink based on the original.
	 * {@link InMemoryMetadata} is copied, but its {@link InMemoryIdentifier}
	 * are set to null.
	 *
	 * @param original
	 *            The original identifier
	 */
	public InMemoryLink(InMemoryLink original) {
		this();
		mFirstIdentifier = null;
		mSecondIdentifier = null;
	}

	/**
	 * Returns a clone of the {@link InMemoryLink}, but sets its
	 * {@link InMemoryIdentifier} to null.
	 *
	 * @return An {@link InMemoryLink} Object with clones Metadate, but
	 *         imcomplete since it does not have a start or end node.
	 */
	@Override
	public InMemoryLink clone() {
		return new InMemoryLink(this);
	}

	@Override
	public InternalIdentifierPair getIdentifiers() {
		return new InternalIdentifierPair(mFirstIdentifier, mSecondIdentifier);
	}

	@Override
	public void addMetadata(InternalMetadata meta) {
		mMeta.add(meta);
	}

	public void clearMetadata() {
		mMeta.clear();
	}

	public void removeMetadata(InternalMetadata meta) {
		mMeta.remove(meta);
	}

	public boolean hasMetadata(InternalMetadata meta) {
		return mMeta.contains(meta);
	}

	@Override
	public List<InternalMetadata> getMetadata() {
		return mMeta;
	}
}
