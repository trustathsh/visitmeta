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
 * This file is part of visitmeta-common, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta.implementations;





import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hshannover.f4.trust.visitmeta.implementations.internal.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class MetadataImpl implements Metadata {
	private Map<String, String> mProperties;
	private String mTypename;
	private boolean mIsSingleValue;
	private long mPublishTimestamp;
	private String mRawData;

	private MetadataImpl() {
		mProperties = new HashMap<String, String>();
	}

	public MetadataImpl(InternalMetadata m) {
		this();
		mTypename = m.getTypeName();
		mRawData = m.getRawData();
		for (String key : m.getProperties()) {
			addProperty(key, m.valueFor(key));
		}
		mIsSingleValue = m.isSingleValue();
		mPublishTimestamp = m.getPublishTimestamp();
	}

	public MetadataImpl(String typename, boolean isSingleValue, long publishTimestamp) {
		this();
		mTypename = typename;
		mIsSingleValue = isSingleValue;
		mPublishTimestamp = publishTimestamp;
	}

	public void addProperty(String key, String value) {
		mProperties.put(key, value);
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
		if (mProperties.containsKey(p)) {
			return true;
		}
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
	public boolean isSingleValue() {
		return mIsSingleValue;
	}

	@Override
	public long getPublishTimestamp() {
		return mPublishTimestamp;
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer();
		tmp.append(getTypeName() + "[" + hashCode() + "] Properties[");
		for (String p : getProperties()) {
			tmp.append("(" + p + ", " + valueFor(p) + ")");
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
		if (! (o instanceof Metadata) )
			return false;
		Metadata other = (Metadata) o;
		if (!getTypeName().equals(other.getTypeName()))
			return false;
		if (getProperties().size() != other.getProperties().size()) {
			return false;
		}
		for (String property : getProperties()) {
			String value = valueFor(property);
			if (value == null) {
				if (!(other.valueFor(property) == null))
					return false;
			} else {
				if (!valueFor(property).equals(other.valueFor(property)))
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
