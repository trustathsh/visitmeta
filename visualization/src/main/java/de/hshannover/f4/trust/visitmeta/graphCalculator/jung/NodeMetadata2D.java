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
 * This file is part of visitmeta-visualization, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.graphCalculator.jung;





import org.apache.log4j.Logger;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;




/**
 * A node-class to represent a metadata.
 */
public class NodeMetadata2D extends Node2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private NodeMetadata     mNodeMetadata;
	private NodeIdentifier2D mNodeIdentifier2D;
	private ExpandedLink2D   mExpandedLink2D;

	private static final Logger LOGGER = Logger.getLogger(NodeMetadata2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS


	public NodeMetadata2D(NodeMetadata nodeMetadata, Layout2D layout2D,
			NodeIdentifier2D nodeIdentifier2D){
		super(nodeMetadata, layout2D);
		mNodeMetadata = nodeMetadata;
		mNodeIdentifier2D = nodeIdentifier2D;
		mExpandedLink2D = null;
	}

	public NodeMetadata2D(NodeMetadata nodeMetadata, Layout2D layout2D,
			ExpandedLink2D expandedLink2D){
		super(nodeMetadata, layout2D);
		mNodeMetadata = nodeMetadata;
		mNodeIdentifier2D = null;
		mExpandedLink2D = expandedLink2D;
	}

	// /////////////////////////////////////////////////////////////////////////////////////// SUPER


	public NodeIdentifier2D getNodeIdentifier2D(){
    	return mNodeIdentifier2D;
    }

	public void setNodeIdentifier2D(NodeIdentifier2D nodeIdentifier2D){
    	mNodeIdentifier2D = nodeIdentifier2D;
    }

	public ExpandedLink2D getExpandedLink2D(){
    	return mExpandedLink2D;
    }

	public void setExpandedLink2D(ExpandedLink2D expandedLink2D){
    	mExpandedLink2D = expandedLink2D;
    }

	public NodeMetadata getNodeMetadata(){
		return mNodeMetadata;
	}

	@Override
	public String toString(){
		LOGGER.trace("Method toString() called.");
		return "Vm" + mId + "|" + mNodeMetadata.getMetadata().getTypeName();
	}

	@Override
	public String getContent(){
		LOGGER.trace("Method getContent() called.");
		return mNodeMetadata.getMetadata().getTypeName();
	}

	@Override
	public int getContentLength(){
		LOGGER.trace("Method getContentLength() called.");
		return mNodeMetadata.getMetadata().getTypeName().length();
	}

}
