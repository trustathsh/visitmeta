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
 * This file is part of visitmeta-common, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.interfaces;

import org.w3c.dom.Document;

public interface GraphFilter {

	/**
	 * @return The starting place of the search.
	 */
	public Identifier getStartIdentifier();

	/**
	 * @return The maximum distance of any included identifiers. Distance is
	 *         measured by number of links away from the starting identifier.
	 */
	public int getMaxDepth();

	/**
	 * @param metadata
	 *            to check if it matches the filter.
	 * @return true if the metadata matches the filter, otherwise false.
	 */
	public boolean matchMeta(Document metadata);

	/**
	 * 
	 * @param linkMeta
	 *            to check if it machtes the filter
	 * @return true if the metadata matches the matchlinks, otherwise false.
	 */
	public boolean matchLink(Document linkMeta);

	/**
	 * @return whether the filter matches every Metadata or not.
	 */
	public boolean matchEverything();

	/**
	 * @return whether the filter matches not any Metadata or not.
	 */
	public boolean matchNothing();
}
