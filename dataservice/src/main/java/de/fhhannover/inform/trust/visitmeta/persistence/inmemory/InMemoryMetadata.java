package de.fhhannover.inform.trust.visitmeta.persistence.inmemory;

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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InMemoryMetadata extends InternalMetadata {

	private static final Logger log = Logger.getLogger(InMemoryMetadata.class);

	private Map<String, String> mProperties;
	private String mTypename;
	private boolean mIsSingleValue;
	private String mRawData;

	private long mPublishTimestamp;
	private long mDeleteTimestamp = InternalMetadata.METADATA_NOT_DELETED_TIMESTAMP;

	private InMemoryMetadata() {
		mProperties = new HashMap<String, String>();
	}

	public InMemoryMetadata(String typename, boolean isSingleValue, long publishTimestamp) {
		this();
		mTypename = typename;
		mIsSingleValue = isSingleValue;
		mPublishTimestamp = publishTimestamp;
	}

	public InMemoryMetadata(InternalMetadata original) {
		this(original.getTypeName(), original.isSingleValue(), original.getPublishTimestamp());
		mRawData = original.getRawData();
		for (String property : original.getProperties()) {
			this.addProperty(property, original.valueFor(property));
		}
	}

	@Override
	public InMemoryMetadata clone() {
		return new InMemoryMetadata(this);
	}

	@Override
	public void addProperty(String name, String value) {
		if (mProperties.containsKey(name)) {
			log.warn("property '"+name+"' already exists, overwriting with '"+value+"'");
		}
		mProperties.put(name, value);
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
	public boolean isSingleValue() {
		return mIsSingleValue;
	}

	@Override
	public String getTypeName() {
		return mTypename;
	}

	@Override
	public long getPublishTimestamp() {
		return mPublishTimestamp;
	}
	@Override
	public long getDeleteTimestamp() {
		return mDeleteTimestamp;
	}

	@Override
	public void setPublishTimestamp(long timestamp) {
		mPublishTimestamp = timestamp;
	}

	@Override
	public String getRawData() {
		return mRawData;
	}

	public void setRawData(String rawData) {
		mRawData = rawData;
	}
}
