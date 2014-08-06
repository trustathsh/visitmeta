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
package de.hshannover.f4.trust.visitmeta.persistence.inmemory;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InMemoryIdentifier extends InternalIdentifier {
	private String mTypename;
	private Map<String, String> mProperties;
	private List<InternalLink> mLinks;
	private Set<InternalMetadata> mMeta;
	private String mRawData;

	private InMemoryIdentifier() {
		mMeta = new HashSet<InternalMetadata>();
		mLinks = new ArrayList<InternalLink>();
		mProperties = new HashMap<String, String>();
		mTypename = "";
	}

	public InMemoryIdentifier(String typename) {
		this();
		mTypename = typename;
	}

	/**
	 * Creates a new InternalIdentifier based on the original.
	 * {@link InMemoryMetadata} and properties are copied, but its
	 * {@link InMemoryLink}s are removed.
	 *
	 * @param original
	 *            The original identifier
	 */
	public InMemoryIdentifier(InternalIdentifier original) {
		this(original.getTypeName());
		mRawData = original.getRawData();

		for (String property : original.getProperties()) {
			this.addProperty(property, original.valueFor(property));
		}
	}

	/**
	 * Returns a clone of the {@link InMemoryIdentifier}, but removes its
	 * {@link InMemoryLink}s.
	 */
	@Override
	public InMemoryIdentifier clone() {
		return new InMemoryIdentifier(this);
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
		InternalMetadata toRemove = null;
		for(InternalMetadata m : mMeta) {
			if(m.equalsForLinks(meta)) {
				toRemove = m;
				break;
			}
		}
		if(toRemove != null) {
			mMeta.remove(toRemove);
		}
	}
	
	@Override
	public void updateMetadata(InternalMetadata meta) {
		InternalMetadata toUpdate = null;
		for(InternalMetadata m : mMeta) {
			if(m.equalsForLinks(meta)) {
				toUpdate = m;
				break;
			}
		}
		if(toUpdate != null) {
			mMeta.remove(toUpdate);
			mMeta.add(meta);
		}
	}
	
	@Override
	public boolean hasMetadata(InternalMetadata meta) {
		for(InternalMetadata m : mMeta) {
			if(m.equalsForLinks(meta)) {
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

	@Override
	public void addProperty(String name, String value) {
		mProperties.put(name, value);
	}

	@Override
	public void clearLinks() {
		mLinks.clear();
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
	public List<InternalLink> getLinks() {
		return mLinks;
	}

	@Override
	public String getTypeName() {
		return mTypename;
	}

	@Override
	public void removeLink(InternalLink link) {
		mLinks.remove(link);
	}


	/**
	 * IMPORTANT: This method is supposed to be called by the repository only.
	 * @param l
	 */
	void addLink(InMemoryLink link) {
		mLinks.add(link);
	}

	@Override
	public String getRawData() {
		return mRawData;
	}

	public void setRawData(String rawData) {
		mRawData = rawData;
	}
}
