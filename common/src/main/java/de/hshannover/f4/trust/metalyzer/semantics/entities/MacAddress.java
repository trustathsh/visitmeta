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
 * This file is part of visitmeta common, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
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
 * Entity-Class to represent MacAddress-Objects with the publish-timestamp and address.
 *
 */
public class MacAddress extends Data {
	private String address;
	
	public MacAddress(long timestamp, long searchTimestamp, long searchFromTimestamp, long searchToTimestamp, String address) {
		super(timestamp, searchTimestamp, searchFromTimestamp, searchToTimestamp);
		setAddress(address);
	}
	
	public MacAddress(long timestamp, String address) {
		super(timestamp);
		setAddress(address);
	}
	
	public MacAddress(String address) {
		this(0, address);
	}
	
	/**
	 * 
	 * @return 
	 * Returns the MAC-Address of the MAC-Address-Object.
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 
	 * @param address
	 * MAC-Address of the MAC-Address-Object.
	 */
	public void setAddress(String address) {
		if (address != null) {
			this.address = address;
		} else {
			this.address = "00-00-00-00-00-00";
		}
	}
	
	@Override
	public String toString() {
		return "[" + super.toString() 
				+ "; Address: " + address
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
		MacAddress macAddress = (MacAddress)o;
		return getAddress().equals(macAddress.getAddress());
	}
	
	@Override
	public int hashCode() {
		return address.hashCode()
				+ super.hashCode();
	}
}
