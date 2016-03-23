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
 * This file is part of visitmeta-common, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.implementations;





import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class LinkImpl implements Link {
	private Identifier mFirstIdentifier;
	private Identifier mSecondIdentifier;
	private List<Metadata> mMeta;

	private LinkImpl() {
		mMeta = new ArrayList<Metadata>();
	}

	public LinkImpl(IdentifierImpl id1, IdentifierImpl id2) {
		this();
		mFirstIdentifier = id1;
		mSecondIdentifier = id2;
	}

	@Override
	public IdentifierPair getIdentifiers() {
		return new IdentifierPairImpl(mFirstIdentifier, mSecondIdentifier);
	}

	@Override
	public List<Metadata> getMetadata() {
		return mMeta;
	}

	public void addMetadata(Metadata m) {
		mMeta.add(m);
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer();
		tmp.append("Link" + "[" + hashCode() + "] Identifier[");
		tmp.append(getIdentifiers().getFirst().hashCode() + ", "
				+ getIdentifiers().getSecond().hashCode() + "] Metadata[");
		int i = 0;
		for (Metadata m : getMetadata()) {
			tmp.append(m.hashCode());
			if (i != getMetadata().size() - 1) {
				tmp.append(", ");
			}
			i++;
		}
		tmp.append("]");
		return tmp.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (! (o instanceof Link))
			return false;
		Link other = (Link) o;
		if (getIdentifiers().getFirst().equals(other.getIdentifiers().getFirst())) {
			if (getIdentifiers().getSecond().equals(other.getIdentifiers().getSecond()))
				return true;
		} else if (getIdentifiers().getFirst().equals(other.getIdentifiers().getSecond())) {
			if (getIdentifiers().getSecond().equals(other.getIdentifiers().getFirst()))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result +
				getIdentifiers().getFirst().hashCode() + getIdentifiers().getSecond().hashCode();
		return result;
	}
}
