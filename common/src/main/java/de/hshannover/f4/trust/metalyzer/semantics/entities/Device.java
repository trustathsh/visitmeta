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
 * Entity-Class to represent Device-Objects with the publish-timestamp, name and attributes.
 *
 */
import java.util.ArrayList;

public class Device extends Data {
	private String name;
	private ArrayList<String> attributes;
	
	public Device(long timestamp, long searchTimestamp, long searchFromTimestamp, long searchToTimestamp, String name, ArrayList<String> attributes) {
		super(timestamp, searchTimestamp, searchFromTimestamp, searchToTimestamp);
		setName(name);
		setAttributes(attributes);
	}
	
	public Device(long timestamp, String name, ArrayList<String> attributes) {
		super(timestamp);
		setName(name);
		setAttributes(attributes);
	}
	
	public Device(long timestamp, String name) {
		this(timestamp, name, new ArrayList<String>());
	}
	
	public Device(String name, ArrayList<String> attributes) {
		this(0, name, attributes);
	}
	
	/**
	 * 
	 * @return 
	 * Returns the name of the Device-Object.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name 
	 * Name of the Device-Object.
	 */
	public void setName(String name) {
		if (name != null) {
			this.name = name;
		} else {
			this.name = "";
		}
	}
	
	/**
	 * 
	 * @return 
	 * Returns the attributes of the Device-Object.
	 */
	public ArrayList<String> getAttributes() {
		return attributes;
	}
	
	/**
	 * 
	 * @param attributes 
	 * List of attributes to add to the Device-Object.
	 */
	public void setAttributes(ArrayList<String> attributes) {
		if (attributes != null) {
			this.attributes = attributes;
		}
	}
	
	/**
	 * 
	 * @param attribute 
	 * A single attribute to add to the Device-Object.
	 */
	public void addAttribute(String attribute) {
		if (attribute != null) {
			this.attributes.add(attribute);
		} else {
			this.attributes.add("");
		}
	}
	
	/**
	 * 
	 * @return
	 * Returns the number of attributes of the Device-Object.
	 */
	public int getAttributeCount() {
		return attributes.size();
	}
	
	@Override
	public String toString() {
		return "[" + super.toString() 
				+ "; Name: " + name 
				+ "; Attributes: " + attributes.toString()
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
		Device device = (Device)o;
		return getName().equals(device.getName())
				&& getAttributes().equals(device.getAttributes());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() 
				+ attributes.hashCode()
				+ super.hashCode();
	}
}
