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
 * Entity-Class to represent general Data-Objects.
 *
 */
public class Data {
	private long timestamp;
	private long searchTimestamp;
	private long searchFromTimestamp;
	private long searchToTimestamp;
	
	public Data(long timestamp, long searchTimestamp, long searchFromTimestamp, long searchToTimestamp) {
		setTimestamp(timestamp);
		setSearchTimestamp(searchTimestamp);
		setSearchFromTimestamp(searchFromTimestamp);
		setSearchToTimestamp(searchToTimestamp);
	}
	
	public Data(long timestamp, long searchTimestamp) {
		this(timestamp, searchTimestamp, 0, 0);
	}
	
	public Data(long timestamp, long searchFromTimestamp, long searchToTimestamp) {
		this(timestamp, 0, searchFromTimestamp, searchToTimestamp);
	}
	
	public Data(long timestamp) {
		this(timestamp, 0, 0, 0);
	}
	
	public Data() {
		this(0, 0, 0, 0);
	}
	
	/**
	 * 
	 * @return
	 * Returns the Publish-Timestamp of the Data-Object.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * 
	 * @return
	 * Returns the Search-Timestamp of the Data-Object.
	 */
	public long getSearchTimestamp() {
		return searchTimestamp;
	}
	
	/**
	 * 
	 * @return
	 * Returns the SearchFrom-Timestamp of the Data-Object.
	 */
	public long getSearchFromTimestamp() {
		return searchFromTimestamp;
	}
	
	/**
	 * 
	 * @return
	 * Returns the SearchTo-Timestamp of the Data-Object.
	 */
	public long getSearchToTimestamp() {
		return searchToTimestamp;
	}
	
	/**
	 * 
	 * @param timestamp
	 * Publish-Timestamp of the Data-Object.
	 */
	public void setTimestamp(long timestamp) {
		if (timestamp >= 0) {
			this.timestamp = timestamp;
		} else {
			this.timestamp = 0;
		}
	}
	
	/**
	 * 
	 * @param searchTimestamp
	 * Search-Timestamp of the Data-Object.
	 */
	public void setSearchTimestamp(long searchTimestamp) {
		if (searchTimestamp >= 0) {
			this.searchTimestamp = searchTimestamp;
		} else {
			this.searchTimestamp = 0;
		}
	}
	
	/**
	 * 
	 * @param searchFromTimestamp
	 * SearchFrom-Timestamp of the Data-Object.
	 */
	public void setSearchFromTimestamp(long searchFromTimestamp) {
		if (searchFromTimestamp >= 0) {
			this.searchFromTimestamp = searchFromTimestamp;
		} else {
			this.searchFromTimestamp = 0;
		}
	}
	
	/**
	 * 
	 * @param searchToTimestamp
	 * SearchTo-Timestamp of the Data-Object.
	 */
	public void setSearchToTimestamp(long searchToTimestamp) {
		if (searchToTimestamp >= 0) {
			this.searchToTimestamp = searchToTimestamp;
		} else {
			this.searchToTimestamp = 0;
		}
	}
	
	@Override
	public String toString() {
		return "Publish-Timestamp: " + timestamp
				/*+ "\nSearchTimestamp: " + searchTimestamp
				+ "\nSearchFromTimestamp: " + searchFromTimestamp
				+ "\nSearchToTimestamp: " + searchToTimestamp*/;
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
		Data data = (Data)o;
		return this.timestamp == data.timestamp
				&& this.searchTimestamp == data.searchTimestamp
				&& this.searchFromTimestamp == data.searchFromTimestamp
				&& this.searchToTimestamp == data.searchToTimestamp;
	}
	
	@Override
	public int hashCode() {
		return (int)((timestamp >> 32) ^ (timestamp)
				+ (searchTimestamp >> 32) ^ (searchTimestamp)
				+ (searchFromTimestamp >> 32) ^ (searchFromTimestamp)
				+ (searchToTimestamp >> 32) ^ (searchToTimestamp));
	}
}
