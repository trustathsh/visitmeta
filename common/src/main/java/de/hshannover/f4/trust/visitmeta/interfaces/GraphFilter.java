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
 * This file is part of visitmeta common, version 0.0.6,
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
package de.hshannover.f4.trust.visitmeta.interfaces;





import java.util.List;

public interface GraphFilter {

	/**
	 * @return
	 * The starting place of the search.
	 */
	public Identifier getStartIdentifier();
	
	/**
	 * @return
	 * The maximum distance of any included identifiers. 
	 * Distance is measured by number of links away from the starting identifier.
	 */
	public int getMaxDepth();
	
	/**
	 * @return
	 * A list of link-types which to include in the filter step. 
	 * Here, it will be represented by a list of strings denoting the typenames.
	 * E.g. meta:access-request-ip
	 */
	public List<String> getMatchLinks();
	
	/**
	 * @return
	 * A list of metadata-types which to include in the filter step. 
	 * Here, it will be represented by a list if strings denoting the typenames.
	 * E.g. meta:roles
	 * <i>Note: The result filter being null means, that no metadata will be filtered, while an 
	 * empty list will result in all metadata being filtered and only the identifiers returned.</i>
	 */
	public List<String> getResultFilter();
}
