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
 * This file is part of visitmeta-dataservice, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta.ifmap;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;

public class SubscriptionImpl implements Subscription {

	public String name;
	public String identifier;
	public String identifierType;
	public String filterLinks;
	public String filterResult;
	public String terminalIdentifierTypes;
	public boolean startupSubscribe;
	public int maxDepth;
	public int maxSize;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getStartIdentifier() {
		return identifier;
	}

	@Override
	public void setStartIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getIdentifierType() {
		return identifierType;
	}

	@Override
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	@Override
	public String getMatchLinksFilter() {
		return filterLinks;
	}

	@Override
	public void setMatchLinksFilter(String filterLinks) {
		this.filterLinks = filterLinks;
	}

	@Override
	public String getResultFilter() {
		return filterResult;
	}

	@Override
	public void setResultFilter(String filterResult) {
		this.filterResult = filterResult;
	}

	@Override
	public String getTerminalIdentifierTypes() {
		return terminalIdentifierTypes;
	}

	@Override
	public void setTerminalIdentifierTypes(String terminalIdentifierTypes) {
		this.terminalIdentifierTypes = terminalIdentifierTypes;
	}

	@Override
	public boolean isStartupSubscribe() {
		return startupSubscribe;
	}

	@Override
	public void setStartupSubscribe(boolean startupSubscribe) {
		this.startupSubscribe = startupSubscribe;
	}

	@Override
	public int getMaxDepth() {
		return maxDepth;
	}

	@Override
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	@Override
	public int getMaxSize() {
		return maxSize;
	}

	@Override
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

}
