package de.hshannover.f4.trust.visitmeta.ifmap.dumpData;

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
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of irongui, version 0.4.1, implemented by the Trust@FHH
 * research group at the Hochschule Hannover, a program to visualize the content
 * of a MAP Server (MAPS), a crucial component within the TNC architecture.
 * 
 * The development was started within the bachelor
 * thesis of Tobias Ruhe at Hochschule Hannover (University of
 * Applied Sciences and Arts Hannover). irongui is now maintained
 * and extended within the ESUKOM research project. More information
 * can be found at the Trust@FHH website.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
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


import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

import de.hshannover.f4.trust.ifmapj.identifier.AccessRequest;
import de.hshannover.f4.trust.ifmapj.identifier.Device;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identity;
import de.hshannover.f4.trust.ifmapj.identifier.IpAddress;
import de.hshannover.f4.trust.ifmapj.identifier.MacAddress;
import de.hshannover.f4.trust.visitmeta.ifmap.dumpData.IdentifierConstants.IdentifierType;

/**
 * Represents an IdentifierData
 * 
 */
public class IdentifierData extends IfmapDataType {

	private final Hashtable<String, String> mAttributes = new Hashtable<String, String>();
	private String mName = null;
	private Identifier mRequestObject = null;
	private IdentifierType mType = null;

	public IdentifierData(String name) {
		super();
		this.mType = IdentifierConstants.IdentifierType.OTHER;
		mName = name;
	}

	public IdentifierData(Identifier o) {
		super();
		if (o instanceof AccessRequest) {
			mName = "access-request";
			AccessRequest art = (AccessRequest) o;
			mRequestObject = art;
			mAttributes.put("name", art.getName());
			if (art.getAdministrativeDomain() != null)
				mAttributes.put("administrative-domain",
						art.getAdministrativeDomain());
			this.mType = IdentifierConstants.IdentifierType.ACCESS_REQUEST;
		} else if (o instanceof Device) {
			mName = "device";
			Device d = (Device) o;
			mRequestObject = d;
			mAttributes.put("name", d.getName());
			this.mType = IdentifierConstants.IdentifierType.DEVICE;
		} else if (o instanceof IpAddress) {
			mName = "ip-address";
			IpAddress ipt = (IpAddress) o;
			mRequestObject = ipt;
			mAttributes.put("value", ipt.getValue());
			mAttributes.put("type", ipt.getType().toString());
			if (ipt.getAdministrativeDomain() != null)
				mAttributes.put("administrative-domain",
						ipt.getAdministrativeDomain());
			this.mType = IdentifierConstants.IdentifierType.IP_ADDRESS;
		} else if (o instanceof Identity) {
			mName = "identity";
			Identity it = (Identity) o;
			mRequestObject = it;
			mAttributes.put("name", it.getName());
			mAttributes.put("type", it.getType().toString());
			if (it.getAdministrativeDomain() != null){
				mAttributes.put("administrative-domain",
						it.getAdministrativeDomain());
			}
			if (it.getOtherTypeDefinition() != null){
				mAttributes.put("other-type-definition",
						it.getOtherTypeDefinition());
			}
			this.mType = IdentifierConstants.IdentifierType.IDENTITY;
		} else if (o instanceof MacAddress) {
			mName = "mac-address";
			MacAddress mt = (MacAddress) o;
			mRequestObject = mt;
			mAttributes.put("value", mt.getValue());
			if (mt.getAdministrativeDomain() != null)
				mAttributes.put("administrative-domain",
						mt.getAdministrativeDomain());
			this.mType = IdentifierConstants.IdentifierType.MAC_ADDRESS;
		}
		else {
			this.mType = IdentifierConstants.IdentifierType.OTHER;
		}
	}

	public IdentifierType getType() {
		return mType;
	}

	public Identifier getRequestObject() {
		return mRequestObject;
	}

	private String getAttributeValue(String key) {
		return mAttributes.get(key);
	}

	public Map<String, String> getAttributes() {
		// We return an unmodifiable Map
		return Collections.unmodifiableMap(mAttributes);
	}

	public String getName() {
		return mName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof IdentifierData))
			return false;

		IdentifierData i2 = (IdentifierData) o;

		if (mType != i2.getType())
			return false;

		if (this.hashCode() == i2.hashCode()) {
			return true;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getName());
		switch (mType) {
		case ACCESS_REQUEST:
			buf.append("name=" + this.getAttributeValue("name"));
			buf.append("administrative-domain="
					+ this.getAttributeValue("administrative-domain"));
			break;
		case IP_ADDRESS:
			buf.append("value=" + this.getAttributeValue("value"));
			buf.append("type=" + this.getAttributeValue("type"));
			break;
		case DEVICE:
			buf.append("name=" + this.getAttributeValue("name"));
			buf.append("aik-name=" + this.getAttributeValue("aik-name"));
			break;
		case IDENTITY:
			buf.append("name=" + this.getAttributeValue("name"));
			buf.append("type=" + this.getAttributeValue("type"));
			buf.append("administrative-domain="
					+ this.getAttributeValue("administrative-domain"));
			break;
		case MAC_ADDRESS:
			buf.append("value=" + this.getAttributeValue("value"));
			buf.append("administrative-domain="
					+ this.getAttributeValue("administrative-domain"));
			break;
		}
		return buf.toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getName());
		buf.append(mAttributes.toString()+ " {"+this.hashCode()+"} [M] "+
				this.getMetadata().toString());
		return buf.toString();
	}

}
