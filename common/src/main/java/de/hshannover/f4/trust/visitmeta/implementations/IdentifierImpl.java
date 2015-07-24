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
 * This file is part of visitmeta-common, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.implementations;





import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hshannover.f4.trust.visitmeta.implementations.internal.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class IdentifierImpl implements Identifier {
	private String mTypename;
	private Map<String, String> mProperties;
	private List<Link> mLinks;
	private List<Metadata> mMeta;
	private String mRawData;


	private IdentifierImpl() {
		mProperties = new HashMap<String, String>();
		mLinks = new ArrayList<>();
		mMeta = new ArrayList<>();
	}

	public IdentifierImpl(String typename) {
		this();
		mTypename = typename;
	}

	public IdentifierImpl(InternalIdentifier id) {
		this();
		mTypename = id.getTypeName();
		mRawData = id.getRawData();
		for (String key : id.getProperties()) {
			addProperty(key, id.valueFor(key));
		}
	}

	public void addProperty(String key, String value) {
		mProperties.put(key, value);
	}
	public void addMetadata(Metadata m) {
		mMeta.add(m);
	}
	
	public void setRawData(String rawData) {
		mRawData = rawData;
	}

	@Override
	public List<String> getProperties() {
		return new ArrayList<String>(mProperties.keySet());
	}

	@Override
	public boolean hasProperty(String p) {
		if (mProperties.containsKey(p))
			return true;
		return false;
	}

	@Override
	public String valueFor(String p) {
		return mProperties.get(p);
	}

	@Override
	public String getTypeName() {
		return mTypename;
	}

	@Override
	public List<Link> getLinks() {
		return mLinks;
	}

	@Override
	public List<Metadata> getMetadata() {
		return mMeta;
	}

	public void addLink(Link l) {
		mLinks.add(l);
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer();
		tmp.append(getTypeName() + "[" + hashCode() + "] Properties[");
		for (String p : getProperties()) {
			tmp.append("(" + p + ", " + valueFor(p) + ")");
		}
		tmp.append("] Links[");
		int i = 0;
		for (Link l : getLinks()) {
			tmp.append(l.hashCode());
			if (i != getLinks().size() - 1) {
				tmp.append(", ");
			}
			i++;
		}
		tmp.append("] Metadata[");
		i = 0;
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
	public String getRawData() {
		return mRawData;
	}
	

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Identifier)) {
			return false;
		}
		Identifier other = (Identifier) o;
		if (!getTypeName().equals(other.getTypeName()))
			return false;

		List<String> myProperties = getProperties();
		if (myProperties.size() != other.getProperties().size()) {
			return false;
		}
		for (String property : myProperties) {
			String myValue = valueFor(property);
			if (myValue == null) {
				if (!(other.valueFor(property) == null))
					return false;
			} else {
				if (!myValue.equals(other.valueFor(property)))
					return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + getTypeName().hashCode();
		List<String> keys = getProperties();
		Collections.sort(keys);
		for (String key : keys) {
			result = prime * result + valueFor(key).hashCode();
		}
		return result;
	}


}
