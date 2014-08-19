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
 * Entity-Class to represent User-Objects with the publish-timestamp, name and role.
 *
 */
public class User extends Data {
	private String name;
	private String role;
	
	public User(long timestamp, long searchTimestamp, long searchFromTimestamp, long searchToTimestamp, String name, String role) {
		super(timestamp, searchTimestamp, searchFromTimestamp, searchToTimestamp);
		setName(name);
		setRole(role);
	}
	
	public User(long timestamp, String name, String role) {
		super(timestamp);
		setName(name);
		setRole(role);
	}
	
	public User(long timestamp, String name) {
		this(timestamp, name, "");
	}
	
	public User(String name, String role) {
		this(0, name, role);
	}
	
	/**
	 * 
	 * @return
	 * Returns the name of the User-Object.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 * Name of the User-Object.
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
	 * Returns the role of the User-Object.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * 
	 * @param role
	 * Role of the User-Object.
	 */
	public void setRole(String role) {
		if (role != null) {
			this.role = role;
		} else {
			this.role = "";
		}
	}
	
	@Override
	public String toString() {
		return "[" + super.toString() 
				+ "; Name: " + name 
				+ "; Role: " + role 
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
		User user = (User)o;
		return getName().equals(user.getName())
				&& getRole().equals(user.getRole());
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() 
				+ role.hashCode() 
				+ super.hashCode();
	}
}
