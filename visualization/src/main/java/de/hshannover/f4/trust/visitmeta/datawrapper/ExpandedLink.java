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
 * This file is part of visitmeta-visualization, version 0.2.0,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;





import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.Link;

/**
 * Wrapper class that contains two identifiers connected by a link.
 */
public class ExpandedLink{

	private static final Logger LOGGER = Logger.getLogger(ExpandedLink.class);

	private Link               mLink;
	private NodeIdentifier     mFirst;
	private NodeIdentifier     mSecond;
	private List<NodeMetadata> mMetaList;

	public ExpandedLink(Link link, NodeIdentifier first, NodeIdentifier second, List<NodeMetadata> metaList) {
		mLink = link;
		addFirst(first);
		addSecond(second);
		mMetaList = metaList;
	}

	public Link getLink() {
		LOGGER.trace("Method getLink() called.");
		return mLink;
	}

	public void setLink(Link link) {
		LOGGER.trace("Method setLink(" + link + ") called.");
		mLink = link;
	}

	public NodeIdentifier getFirst() {
		LOGGER.trace("Method getFirst() called.");
		return mFirst;
	}

	public void setFirst(NodeIdentifier identifier) {
		LOGGER.trace("Method setFirst(" + identifier + ") called.");
		/* Delete old */
		NodeIdentifier first = mFirst;
		if(first.contains(this)) {
			first.deleteLink(this);
		}
		/* Add new */
		addFirst(identifier);
	}

	private void addFirst(NodeIdentifier identifier) {
		LOGGER.trace("Method addFirst(" + identifier + ") called.");
		mFirst = identifier;
		if(!identifier.contains(this)) {
			identifier.addLink(this);
		}
	}

	private void deleteFirst(NodeIdentifier identifier) {
		LOGGER.trace("Method deleteFirst(" + identifier + ") called.");
		NodeIdentifier first = mFirst;
		if(first != null && first == identifier) {
			mFirst = null;
			if(first.contains(this)) {
				first.deleteLink(this);
			}
		}
	}

	public NodeIdentifier getSecond() {
		LOGGER.trace("Method getSecond() called.");
		return mSecond;
	}

	public void setSecond(NodeIdentifier identifier) {
		LOGGER.trace("Method getSecond( " + identifier + ") called.");
		/* Delete old */
		NodeIdentifier second = mSecond;
		if(second.contains(this)) {
			second.deleteLink(this);
		}
		/* Add new */
		addSecond(identifier);
	}
	private void addSecond(NodeIdentifier identifier) {
		LOGGER.trace("Method addSecond( " + identifier + ") called.");
		mSecond = identifier;
		if(!identifier.contains(this)) {
			identifier.addLink(this);
		}
	}
	private void deleteSecond(NodeIdentifier identifier) {
		LOGGER.trace("Method deleteSecond( " + identifier + ") called.");
		NodeIdentifier second = mSecond;
		if(second != null && second == identifier) {
			mSecond = null;
			if(second.contains(this)) {
				second.deleteLink(this);
			}
		}
	}

	public void deleteIdentifier(NodeIdentifier identifier) {
		LOGGER.trace("Method deleteIdentifier( " + identifier + ") called.");
		deleteFirst(identifier);
		deleteSecond(identifier);
	}

	public List<NodeMetadata> getMetadata() {
		LOGGER.trace("Method getMetadata() called.");
		return mMetaList;
	}

	public void setMetadata(List<NodeMetadata> metaList) {
		LOGGER.trace("Method getMetadata(" + metaList + ") called.");
		mMetaList = metaList;
	}

	public void addMetadata(NodeMetadata metadata) {
		LOGGER.trace("Method addMetadata(" + metadata + ") called.");
		mMetaList.add(metadata);
	}

	public void deleteMetadata(NodeMetadata metadata) {
		LOGGER.trace("Method addMetadata(" + metadata + ") called.");
		mMetaList.remove(metadata);
	}

	public boolean contains(NodeIdentifier identifier) {
		LOGGER.trace("Method contains(" + identifier + ") called.");
		if(mFirst == identifier) {
			return true;
		}
		if(mSecond == identifier) {
			return true;
		}
		return false;
	}

	public boolean contains(NodeMetadata metadata) {
		LOGGER.trace("Method contains(" + metadata + ") called.");
		return mMetaList.contains(metadata);
	}

	public boolean hasMetadata() {
		LOGGER.trace("Method hasMetadata() called.");
		return mMetaList.size() > 0;
	}
}
