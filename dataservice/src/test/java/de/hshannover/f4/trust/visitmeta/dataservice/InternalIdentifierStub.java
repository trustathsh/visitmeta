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
 * This file is part of visitmeta dataservice, version 0.1.0,
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

package de.hshannover.f4.trust.visitmeta.dataservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;

public class InternalIdentifierStub extends InternalIdentifier {

	public String typename = "";
	public List<InternalLink> links = new ArrayList<>();
	public List<InternalMetadata> metadata = new ArrayList<>();
	public Map<String, String> properties = new HashMap<String, String>();

	@Override
	public List<InternalLink> getLinks() {
		return links;
	}

	@Override
	public List<InternalMetadata> getMetadata() {
		return metadata;
	}

	@Override
	public List<String> getProperties() {
		return new ArrayList<String>(properties.keySet());
	}

	@Override
	public boolean hasProperty(String p) {
		return properties.containsKey(p);
	}

	@Override
	public String valueFor(String p) {
		return properties.get(p);
	}

	@Override
	public String getTypeName() {
		return typename;
	}

	@Override
	public void addMetadata(InternalMetadata meta) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearMetadata() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeMetadata(InternalMetadata meta) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void updateMetadata(InternalMetadata meta) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasMetadata(InternalMetadata meta) {
		return metadata.contains(meta);
	}

	@Override
	public void clearLinks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addProperty(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeLink(InternalLink link) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRawData() {
		// TODO Auto-generated method stub
		return null;
	}

}
