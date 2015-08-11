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
 * This file is part of visitmeta-common, version 0.5.1,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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

/**
 * This interface represents the changes of an IF-MAP graph structure between two points in time.
 * @author rosso
 *
 */
public interface Delta {

	/**
	 * @return
	 * A List of IdentifierGraphs that represent that delete operations. Since Metadata can only
	 * be identified by the Identifier it is connected to, the Identifiers of deleted Metadata
	 * is included in the delete result also. This does not necessarily mean that the respective
	 * Identifier itself is to be deleted. This is only the case if an Identifier in the delete
	 * result has no more Metadata connected to it or to any of its links, after the corresponding
	 * deletes for Metadata have been carried out.
	 */
	public List<IdentifierGraph> getDeletes();
	/**
	 * @return
	 * A List of IdentifierGraphs that represent that update operations. Since Metadata can only
	 * be identified by the Identifier it is connected to, the Identifiers of updated Metadata
	 * is included in the update result also.
	 */
	public List<IdentifierGraph> getUpdates();
}
