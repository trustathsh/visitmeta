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
 * This file is part of visitmeta-dataservice, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta.persistence.inmemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierPair;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InMemoryLink extends InternalLink {
	private InternalIdentifier mFirstIdentifier;
	private InternalIdentifier mSecondIdentifier;
	private Set<InternalMetadata> mMeta;

	private InMemoryLink() {
		mMeta = new HashSet<InternalMetadata>();
	}

	public InMemoryLink(InternalIdentifier id1, InternalIdentifier id2) {
		this();
		mFirstIdentifier = id1;
		mSecondIdentifier = id2;

	}

	@Override
	public InternalIdentifierPair getIdentifiers() {
		return new InternalIdentifierPair(mFirstIdentifier, mSecondIdentifier);
	}

	@Override
	public void clearMetadata() {
		mMeta.clear();
	}

	@Override
	public void addMetadata(InternalMetadata meta) {
		mMeta.add(meta);
	}

	@Override
	public void removeMetadata(InternalMetadata meta) {
		removeMetadata(meta, true);
	}

	@Override
	public void removeMetadata(InternalMetadata meta,
			boolean isSingleValueDependent) {
		InternalMetadata toRemove = null;
		for (InternalMetadata m : mMeta) {
			if (!isSingleValueDependent) {
				if (m.equals(meta)) {
					toRemove = m;
					break;
				}
			} else {
				if (m.equalsForLinks(meta)) {
					toRemove = m;
					break;
				}
			}
		}
		if (toRemove != null) {
			mMeta.remove(toRemove);
		}
	}

	@Override
	public void updateMetadata(InternalMetadata meta) {
		InternalMetadata toUpdate = null;
		for (InternalMetadata m : mMeta) {
			if (m.equalsForLinks(meta)) {
				toUpdate = m;
				break;
			}
		}
		if (toUpdate != null) {
			mMeta.remove(toUpdate);
			mMeta.add(meta);
		}
	}

	@Override
	public boolean hasMetadata(InternalMetadata meta) {
		for (InternalMetadata m : mMeta) {
			if (m.equalsForLinks(meta)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean equalsSingleValueMetadata(InternalMetadata meta) {
		for (InternalMetadata m : mMeta) {
			if (m.equalsSingleValueMetadata(meta)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<InternalMetadata> getMetadata() {
		List<InternalMetadata> result = new ArrayList<>();
		result.addAll(mMeta);
		return result;
	}
}
