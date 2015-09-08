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
 * This file is part of visitmeta-visualization, version 0.5.2,
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
package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.data.DataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

/**
 * Helper class for the {@link RESTConnectionModel}. It combines {@link DataserviceConnection}s.
 * 
 * @author Marcel Reichenbach
 *
 */
public class Dataservices extends DataImpl {

	List<DataserviceConnection> mList;

	@Override
	public String getName() {
		return Dataservices.class.getSimpleName();
	}

	@Override
	public List<Data> getSubData() {
		return new ArrayList<Data>(mList);
	}

	public void removeDataserviceConnection(DataserviceConnection dataservice){
		mList.remove(dataservice);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Data copy() {
		Dataservices tmpCopy = new Dataservices();
		tmpCopy.mList = (List<DataserviceConnection>) (List<?>) getSubData();
		return tmpCopy;
	}

	@Override
	public Class<?> getDataTypeClass() {
		throw new UnsupportedOperationException();
	}

	public void addDataserviceConnection(DataserviceConnection newDataserviceConnection) {
		mList.add(newDataserviceConnection);
	}

}
