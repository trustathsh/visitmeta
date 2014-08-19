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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
 * Project: Metalyzer 
 * Author: Sören Grzanna
 * Author: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */

package de.hshannover.f4.trust.metalyzer.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Generic class to store the results of a delta-querie.
 * 
 * @author Johannes Busch
 * @author Sören Grzanna
 *
 * @param <T> Type of stored data in this delta.
 */
public class MetalyzerDelta<T> {
	private Collection<T> deletes;
	private Collection<T> updates;
	private Collection<T> preDeltaAvailables;

	public MetalyzerDelta(Collection<T> deletes, Collection<T> updates, Collection<T> availables) {
		this.deletes = deletes;
		this.updates = updates;
		this.preDeltaAvailables = availables;
	}
	
	/**
	 * Returns all deletes in the given time delta.
	 * @return List of deleted Objects( Identifier or Metadata )
	 */
	public Collection<T> getDeletes() {
		Collection<T> deletes = new ArrayList<T>();
		deletes.addAll(this.deletes);
		return deletes;
	}
	
	/**
	 * Returns all updates in the given time delta.
	 * @return List of updated Objects( Identifier or Metadata )
	 */
	public Collection<T> getUpdates() {
		Collection<T> updates = new ArrayList<T>();
		updates.addAll(this.updates);
		return updates;
	}
	
	/**
	 * Returns all Object that were present in the given time delta.
	 * Uses internal an HashSet
	 * @return List of all present Objects( Identifier or Metadata )
	 */
	public Collection<T> getAvailables(){
		HashSet<T> deltaAvailables = new HashSet<T>();
		deltaAvailables.addAll(this.preDeltaAvailables);
		deltaAvailables.addAll(this.updates);
		return deltaAvailables;
	}
}
