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
 * This file is part of visitmeta-visualization, version 0.6.0,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;





import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

/**
 * Wrapper class that contains an indentifier and its current position in the
 * layout.
 */
public class NodeIdentifier extends Position {

	private static final Logger LOGGER = Logger.getLogger(NodeIdentifier.class);

	private Identifier         mIdentifier;
	private List<NodeMetadata> mNodeMetadata;
	private List<ExpandedLink> mExpandedLinks;

	public NodeIdentifier(Identifier identifier) {
		super();
		mIdentifier    = identifier;
		mNodeMetadata  = new ArrayList<>();
		mExpandedLinks = new ArrayList<>();
	}

	public Identifier getIdentifier() {
		LOGGER.trace("Method getIdentifier() called.");
		return mIdentifier;
	}

	public void setIdentifier(Identifier identifier) {
		LOGGER.trace("Method setIdentifier(" + identifier + ") called.");
		mIdentifier = identifier;
		setChanged();
		notifyObservers();
	}

	public List<NodeMetadata> getMetadata() {
		LOGGER.trace("Method getMetadata() called.");
		return mNodeMetadata;
	}

	public void setMetadata(List<NodeMetadata> nodeMetadata) {
		LOGGER.trace("Method setMetadata(" + nodeMetadata + ") called.");
		mNodeMetadata = nodeMetadata;
		setChanged();
		notifyObservers();
	}

	public void addNodeMetadata(NodeMetadata nodeMetadata) {
		LOGGER.trace("Method addNodeMetadata(" + nodeMetadata + ") called.");
		mNodeMetadata.add(nodeMetadata);
		setChanged();
		notifyObservers();
	}

	public void deleteNodeMetadata(NodeMetadata nodeMetadata) {
		LOGGER.trace("Method addNodeMetadata(" + nodeMetadata + ") called.");
		mNodeMetadata.remove(nodeMetadata);
		mIdentifier.getMetadata().remove(nodeMetadata.getMetadata());
		setChanged();
		notifyObservers();
	}

	public boolean hasMetadata() {
		LOGGER.trace("Method hasMetadata() called.");
		return mNodeMetadata.size() > 0;
	}

	public boolean hasLinks() {
		LOGGER.trace("Method hasLinks() called.");
		return mExpandedLinks.size() > 0;
	}

	/**
	 * Can't manage bidirectional relationship.
	 * @param link
	 */
	public void addLink(ExpandedLink link) {
		LOGGER.trace("Method addLink(" + link + ") called.");
		mExpandedLinks.add(link);
	}

	/**
	 * Manage bidirectional relationship.
	 * @param link
	 */
	public void deleteLink(ExpandedLink link) {
		LOGGER.trace("Method deleteLink(" + link + ") called.");
		mExpandedLinks.remove(link);
		if(link.contains(this)) {
			link.deleteIdentifier(this);
		}
	}

	public boolean contains(NodeMetadata metadata) {
		LOGGER.trace("Method contains(" + metadata + ") called.");
		return mNodeMetadata.contains(metadata);
	}

	public boolean contains(ExpandedLink link) {
		LOGGER.trace("Method contains(" + link + ") called.");
		return mExpandedLinks.contains(link);
	}
}
