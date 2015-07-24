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
 * This file is part of visitmeta-common, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.data;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.MapServerData;

public class DataserviceDataImpl extends DataImpl implements DataserviceData {

	private String mUrl;

	private boolean mRawXml;

	private List<MapServerData> mSubDataList;

	private DataserviceDataImpl() {
		mSubDataList = new ArrayList<MapServerData>();
	}

	public DataserviceDataImpl(String name, String url, boolean rawXml) {
		this();
		setName(name);
		setUrl(url);
		setRawXml(rawXml);
	}

	@Override
	public DataserviceData copy() {
		DataserviceData tmpCopy = new DataserviceDataImpl(getName(), getUrl(), isRawXml());
		tmpCopy.setMapServerData(getMapServerData());
		return tmpCopy;
	}

	@Override
	public DataserviceData clone() {
		return (DataserviceData) super.clone();
	}

	@Override
	public void changeData(DataserviceData newData) {
		setName(newData.getName());
		setUrl(newData.getUrl());
		setRawXml(newData.isRawXml());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Data> getSubData() {
		return (List<Data>) (List<?>) getMapServerData();
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void setUrl(String url) {
		mUrl = url;
	}

	@Override
	public boolean isRawXml() {
		return mRawXml;
	}

	@Override
	public void setRawXml(boolean rawXml) {
		mRawXml = rawXml;
	}

	@Override
	public void addMapServerData(MapServerData connection) {
		mSubDataList.add(connection);
	}

	@Override
	public void setMapServerData(List<MapServerData> connection) {
		mSubDataList = connection;
	}

	@Override
	public List<MapServerData> getMapServerData() {
		return new ArrayList<MapServerData>(mSubDataList);
	}

	@Override
	public void removeMapServerData(MapServerData connection) {
		mSubDataList.remove(connection);
	}

	@Override
	public void removeMapServerData(int index) {
		mSubDataList.remove(index);
	}

	@Override
	public Class<?> getDataTypeClass() {
		return DataserviceData.class;
	}

}
