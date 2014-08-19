/** Project: Metalyzer
* Author: Michael Felchner
* Author: Mihaela Stein
* Author: Sven Steinbach
* Last Change:
* 	by: $Author: $
* 	date: $Date: $
* Copyright (c): Hochschule Hannover
*/

package de.hshannover.f4.trust.metalyzer.semantics.entities;

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

/**
 * Entity-Class to represent IpAddress-Objects with the publish-timestamp, address and address-type.
 *
 */
public class IpAddress extends Data {
	private String address;
	private String type;
	
	public IpAddress(long timestamp, long searchTimestamp, long searchFromTimestamp, long searchToTimestamp, String address, String type) {
		super(timestamp, searchTimestamp, searchFromTimestamp, searchToTimestamp);
		setAddress(address);
		setType(type);
	}
	
	public IpAddress(long timestamp, String address, String type) {
		super(timestamp);
		setAddress(address);
		setType(type);
	}
	
	public IpAddress(long timestamp, String address) {
		this(timestamp, address, "");
	}
	
	public IpAddress(String address, String type) {
		this(0, address, type);
	}
	
	/**
	 * 
	 * @return
	 * Returns the IP-Address of the IpAddress-Object.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 
	 * @param address
	 * IP-Address of the IpAddress-Object.
	 */
	public void setAddress(String address) {
		if (address != null) {
			this.address = address;
		} else {
			this.address = "000.000.000.000";
		}
	}
	
	/**
	 * 
	 * @return
	 * Returns the type of the IP-Address.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 
	 * @param type 
	 * Type of the IP-Address.
	 */
	public void setType(String type) {
		if (type != null) {
			this.type = type;
		} else {
			this.type = "IPv4";
		}
	}
	
	@Override
	public String toString() {
		return "[" + super.toString() 
				+ "; Address: " + address 
				+ "; Type: " + type
				+ "]";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (!o.getClass().equals(getClass())) {
			return false;
		}
		IpAddress ipAddress = (IpAddress)o;
		return getAddress().equals(ipAddress.getAddress())
				&& getType().equals(ipAddress.getType());
	}
	
	@Override
	public int hashCode() {
		return address.hashCode() 
				+ type.hashCode()
				+ super.hashCode();
	}
}