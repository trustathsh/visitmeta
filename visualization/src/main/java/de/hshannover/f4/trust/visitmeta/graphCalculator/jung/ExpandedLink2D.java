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
 * This file is part of visitmeta-visualization, version 0.4.0,
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





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.ExpandedLink;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.graphCalculator.MetadataCollocation;
import edu.uci.ics.jung.graph.Graph;



/**
 * Represents the link between two node-identifiers (NodeIdentifier2D).
 */
public class ExpandedLink2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private ExpandedLink                      mExpandedLink;
	private NodeIdentifier2D                  mStartIdentifier2D;
	private NodeIdentifier2D                  mEndIdentifier2D;

	private Graph<Node2D, Edge2D>             mGraph;
	private Layout2D                          mLayout2D;
	private MetadataCollocation               mMetadataCollocation;

	private List<NodeMetadata>                mNodesMetadata;
	private List<NodeMetadata2D>              mNodesMetadata2D;
	private Map<NodeMetadata, NodeMetadata2D> mNodeMetadataMap;
	private List<Edge2D>                      mEdges2D;

	private static final Logger               LOGGER = Logger.getLogger(ExpandedLink2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public ExpandedLink2D(ExpandedLink expandedLink, NodeIdentifier2D start, NodeIdentifier2D end,
	        Graph<Node2D, Edge2D> graph, Layout2D layout2D,
	        MetadataCollocation metadataCollocation){

		mExpandedLink        = expandedLink;
		mStartIdentifier2D   = start;
		mEndIdentifier2D     = end;
		mStartIdentifier2D.addExpandedLink2D(this);
		mEndIdentifier2D.addExpandedLink2D(this);

		mGraph               = graph;
		mLayout2D            = layout2D;
		mMetadataCollocation = metadataCollocation;

		mNodesMetadata   = new ArrayList<>();
		mNodesMetadata2D = new ArrayList<>();
		mNodeMetadataMap = new HashMap<>();
		mEdges2D         = new ArrayList<>();

		for(NodeMetadata nodeMeta : mExpandedLink.getMetadata()){
			addNodeMetadata(nodeMeta);
		}
	}

	// ///////////////////////////////////////////////////////////////////////// PUBLIC - ADD REMOVE

	/**
	 * Adds a metadata-node to the ExpandedLink2D-object.
	 * @param nodeMetadata The metadata-node to be added.
	 */
	public void addNodeMetadata(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadata(" + nodeMetadata + ") called.");

		if(mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		switch(mMetadataCollocation){
			case SIMPLE:      addNodeMetadataAsSimple(nodeMetadata); break;
			case CHAIN:       addNodeMetadataAsChain(nodeMetadata); break;
			case FORK:        addNodeMetadataAsFork(nodeMetadata); break;
			case FORK_LINKED: addNodeMetadataAsForkLinked(nodeMetadata); break;
		}
	}


	/**
	 * Removes a metadata-node from the ExpandedLink2D-object.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	public void removeNodeMetadata(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadata(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		switch(mMetadataCollocation){
			case SIMPLE:      removeNodeMetadataAsSimple(nodeMetadata); break;
			case CHAIN:       removeNodeMetadataAsChain(nodeMetadata); break;
			case FORK:        removeNodeMetadataAsFork(nodeMetadata); break;
			case FORK_LINKED: removeNodeMetadataAsForkLinked(nodeMetadata); break;
		}
	}


	/**
	 * Remove this instance (ExpandedLink2D) from graph.
	 */
	public void removeYourself(){
		LOGGER.trace("Method removeYourself() called.");

		for(NodeMetadata n : mNodesMetadata){
			removeNodeMetadata(n);
		}
	}


	// ///////////////////////////////////////////////////////////////// PUBLIC - CHANGE COLLOCATION

	/**
	 * Changes the collocation (in other words: alignment or orientation) of the metadata-nodes.
	 * @param metadataCollocation The collocation-type for the metadata-nodes.
	 */
	public void changeMetadataCollocation(MetadataCollocation metadataCollocation){
		LOGGER.trace("Method changeMetadataCollocation(" + metadataCollocation + ") called.");

		mMetadataCollocation = metadataCollocation;
		clearMetadataCollocations();

		// Links (here: edges) with no metadata-nodes do not exist
		if(mNodesMetadata2D.isEmpty()){
			return;
		}

		switch(mMetadataCollocation){
			case SIMPLE:      changeMetadataCollocationAsSimple(); break;
			case CHAIN:       changeMetadataCollocationAsChain(); break;
			case FORK:        changeMetadataCollocationAsFork(); break;
			case FORK_LINKED: changeMetadataCollocationAsForkLinked(); break;
		}
	}


	// ///////////////////////////////////////////////////////////////////////////// PUBLIC - GETTER

	/**
	 * Calculates the length of the ExpandedLink2D-object and returns it.<br>
	 * It calculates the length by evaluating the number of String-Characters of every
	 * metadata-node.
	 * @return Length of the ExpandedLink2D-object.
	 */
	public int getTotalLength(){
		LOGGER.trace("Method getTotalLength() called.");

		int startLength = MathLib.roundUp((mStartIdentifier2D.getContentLength()) / 2.0);
		int endLength   = MathLib.roundUp((mEndIdentifier2D.getContentLength()) / 2.0);
		int totalLength = startLength + endLength;

		if(mMetadataCollocation == MetadataCollocation.CHAIN){
			for(NodeMetadata2D nm : mNodesMetadata2D){
				totalLength = totalLength + nm.getContentLength();
			}
		}
		else if(mMetadataCollocation == MetadataCollocation.FORK ||
				mMetadataCollocation == MetadataCollocation.FORK_LINKED){
			int maxLength = 0;
			for(NodeMetadata2D nm : mNodesMetadata2D){
				if(nm.getContentLength() > maxLength){
					maxLength = nm.getContentLength();
				}
			}
			totalLength = totalLength + maxLength;
		}

		return totalLength;
	}

	public NodeIdentifier2D getStart(){
		LOGGER.trace("Method getStart() called.");
		return mStartIdentifier2D;
	}

	public void setStart(NodeIdentifier2D node){
		LOGGER.trace("Method setStart(" + node + ") called.");
		mStartIdentifier2D = node;
	}

	public NodeIdentifier2D getEnd(){
		LOGGER.trace("Method getEnd() called.");
		return mEndIdentifier2D;
	}

	public void setEnd(NodeIdentifier2D node){
		LOGGER.trace("Method setEnd(" + node + ") called.");
		mEndIdentifier2D = node;
	}

	public List<NodeMetadata> getNodesMetadata(){
		LOGGER.trace("Method getNodesMetadata() called.");
		return mNodesMetadata;
	}

	public List<NodeMetadata2D> getNodesMetadata2D(){
		LOGGER.trace("Method getNodesMetadata2D() called.");
		return mNodesMetadata2D;
	}

	public NodeMetadata2D getNodeMetadata2D(NodeMetadata nodeMetadata){
		LOGGER.trace("Method getNodesMetadata2D("+nodeMetadata+") called.");
		return mNodeMetadataMap.get(nodeMetadata);
	}

	public int getNodesMetadata2DCount(){
		LOGGER.trace("Method getNodesMetadata2DCount() called.");
		return mNodesMetadata2D.size();
	}

	public Map<NodeMetadata, NodeMetadata2D> getNodeMetadataMap(){
		LOGGER.trace("Method getNodeMetadataMap() called.");
		return mNodeMetadataMap;
	}

	public List<Edge2D> getEdges2D(){
		LOGGER.trace("Method getEdges2D() called.");
		return mEdges2D;
	}

	public Edge2D getEdge2D(int i){
		LOGGER.trace("Method getEdge2D(" + i + ") called.");
		return mEdges2D.get(i);
	}

	// //////////////////////////////////////////////////////////////// PRIVATE - ADD REMOVE METHODS


	/**
	 * Append and aling this metadata-node as SIMPLE.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsSimple(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsSimple(" + nodeMetadata + ") called.");

		NodeMetadata2D nodeMetadata2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		addMetadataToLists(nodeMetadata, nodeMetadata2D);

		if(mNodesMetadata.size() == 1  &&  mEdges2D.isEmpty()){
			Edge2D edge = new Edge2D(mStartIdentifier2D, mEndIdentifier2D);
			addEdgeToList(edge);
			addEdgeToGraph(edge);
		}
	}


	/**
	 * Remove this metadata-node as SIMPLE.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsSimple(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsSimple(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D nodeMetadata2D = mNodeMetadataMap.get(nodeMetadata);
		removeMetadataFromLists(nodeMetadata, nodeMetadata2D);

		// Links with no metadata-node do not have an edge.
		if(!hasNodesMetadata2D()  &&  mEdges2D.size() == 1){
			Edge2D edge = mEdges2D.get(0);
			removeEdgeFromGraph(edge);
			removeEdgeFromList(edge);
		}
	}


	/**
	 * Append and aling this metadata-node as CHAIN.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsChain(NodeMetadata nodeMetadata){
		LOGGER.trace("Method aadNodeMetadataAsChain(" + nodeMetadata + ") called.");

		NodeMetadata2D nodeMetadata2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		Node2D nodePrevious           = null;
		Node2D nodeNext               = mEndIdentifier2D;
		Edge2D edgeIn                 = null;
		Edge2D edgeOut                = null;
		Edge2D edgeToBeRemoved        = null;

		if(!hasNodesMetadata2D()){
			//nodeMetadata2D is the first node on the link.
			nodePrevious = mStartIdentifier2D;
		}
		else{
			nodePrevious = mNodesMetadata2D.get(mNodesMetadata2D.size()-1);
			edgeToBeRemoved = nodePrevious.getEdgeOut();
			removeEdgeFromGraph(edgeToBeRemoved);
			removeEdgeFromList(edgeToBeRemoved);
		}

		edgeIn = new Edge2D(nodePrevious, nodeMetadata2D);
		addEdgeToList(edgeIn);
		edgeOut = new Edge2D(nodeMetadata2D, nodeNext);
		addEdgeToList(edgeOut);

		nodeMetadata2D.setEdgeIn(edgeIn);
		nodeMetadata2D.setEdgeOut(edgeOut);
		addMetadataToLists(nodeMetadata, nodeMetadata2D);

		addMetadataToGraph(nodeMetadata2D);
		addEdgeToGraph(edgeIn);
		addEdgeToGraph(edgeOut);
		nodeMetadata2D.setPositionTriggeredByJung(nodePrevious);
	}


	/**
	 * Remove this metadata-node as CHAIN.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsChain(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsChain(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D metaToBeRemoved = mNodeMetadataMap.get(nodeMetadata);
		Edge2D edgeInToBeRemoved       = metaToBeRemoved.getEdgeIn();
		Edge2D edgeOutToBeRemoved      = metaToBeRemoved.getEdgeOut();
		Node2D nodePrevious            = edgeInToBeRemoved.getStart();
		Node2D nodeNext                = edgeOutToBeRemoved.getEnd();
		Edge2D newEdge                 = new Edge2D(nodePrevious, nodeNext);

		removeEdgeFromGraph(edgeInToBeRemoved);
		removeEdgeFromGraph(edgeOutToBeRemoved);
		removeMetadataFromGraph(metaToBeRemoved);

		removeEdgeFromList(edgeInToBeRemoved);
		removeEdgeFromList(edgeOutToBeRemoved);
		removeMetadataFromLists(nodeMetadata, metaToBeRemoved);

		// Links with no metadata-node do not have an edge.
		if(hasNodesMetadata2D()){
			nodePrevious.setEdgeOut(newEdge);
			nodeNext.setEdgeIn(newEdge);
			addEdgeToList(newEdge);
			addEdgeToGraph(newEdge);
		}
	}


	/**
	 * Append and aling this metadata-node as FORK.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsFork(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsFork(" + nodeMetadata + ") called.");

		NodeMetadata2D newMeta2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		Edge2D newEdgeIn         = new Edge2D(mStartIdentifier2D, newMeta2D);
		Edge2D newEdgeOut        = new Edge2D(newMeta2D, mEndIdentifier2D);

		newMeta2D.setEdgeIn(newEdgeIn);
		newMeta2D.setEdgeOut(newEdgeOut);

		addMetadataToLists(nodeMetadata, newMeta2D);
		addEdgeToList(newEdgeIn);
		addEdgeToList(newEdgeOut);

		addMetadataToGraph(newMeta2D);
		addEdgeToGraph(newEdgeIn);
		addEdgeToGraph(newEdgeOut);
		if(hasNodesMetadata2D()){
			newMeta2D.setPositionTriggeredByJung(mStartIdentifier2D);
		}
		else{
			newMeta2D.setPositionTriggeredByJung(getLastNodeMetadata2D());
		}
	}


	/**
	 * Remove this metadata-node as FORK.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsFork(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsFork(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D meta2DToBeRemoved  = mNodeMetadataMap.get(nodeMetadata);
		Edge2D         edgeInToBeRemoved  = meta2DToBeRemoved.getEdgeIn();
		Edge2D         edgeOutToBeRemoved = meta2DToBeRemoved.getEdgeOut();

		removeEdgeFromGraph(edgeInToBeRemoved);
		removeEdgeFromGraph(edgeOutToBeRemoved);
		removeMetadataFromGraph(meta2DToBeRemoved);

		removeEdgeFromList(edgeInToBeRemoved);
		removeEdgeFromList(edgeOutToBeRemoved);
		removeMetadataFromLists(nodeMetadata, meta2DToBeRemoved);
	}


	/**
	 * Append and aling this metadata-node as FORK_LINKED.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsForkLinked(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsForkLinked(" + nodeMetadata + ") called.");

		boolean firstNodeMetadata = true;
		if(hasNodesMetadata2D()){
			firstNodeMetadata = false;
		}

		NodeMetadata2D newMeta2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		Edge2D edgeIn   = new Edge2D(mStartIdentifier2D, newMeta2D);
		Edge2D edgeOut  = new Edge2D(newMeta2D, mEndIdentifier2D);
		Edge2D forkEdge = null;

		newMeta2D.setEdgeIn(edgeIn);
		newMeta2D.setEdgeOut(edgeOut);

		if(!firstNodeMetadata){
			forkEdge = new Edge2D(getLastNodeMetadata2D(), newMeta2D);
			getLastNodeMetadata2D().setForkEdgeOut(forkEdge);
			newMeta2D.setForkEdgeIn(forkEdge);
		}

		addMetadataToLists(nodeMetadata, newMeta2D);
		addEdgeToList(edgeIn);
		addEdgeToList(edgeOut);
		if(!firstNodeMetadata){
			addEdgeToList(forkEdge);
		}

		addMetadataToGraph(newMeta2D);
		addEdgeToGraph(edgeIn);
		addEdgeToGraph(edgeOut);
		if(!firstNodeMetadata){
			addEdgeToGraph(forkEdge);
			newMeta2D.setPositionTriggeredByJung(getLastNodeMetadata2D());
		}
		else{
			newMeta2D.setPositionTriggeredByJung(mStartIdentifier2D);
		}
	}


	/**
	 * Remove this metadata-node as FORK_LINKED.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsForkLinked(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsForkLinked(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D meta2DToBeRemoved = mNodeMetadataMap.get(nodeMetadata);
		Edge2D edgeIn                    = meta2DToBeRemoved.getEdgeIn();
		Edge2D edgeOut                   = meta2DToBeRemoved.getEdgeOut();
		Edge2D edgeInFork                = meta2DToBeRemoved.getForkEdgeIn();
		Edge2D edgeOutFork               = meta2DToBeRemoved.getForkEdgeOut();
		Node2D forkPreviousNode          = null;
		Node2D forkNextNode              = null;

		if(edgeInFork != null){
			forkPreviousNode = edgeInFork.getStart();
		}
		if(edgeOutFork != null){
			forkNextNode = edgeOutFork.getEnd();
		}

		removeEdgeFromGraph(edgeInFork);
		removeEdgeFromGraph(edgeOutFork);
		removeEdgeFromGraph(edgeIn);
		removeEdgeFromGraph(edgeOut);
		removeMetadataFromGraph(meta2DToBeRemoved);

		removeEdgeFromList(edgeInFork);
		removeEdgeFromList(edgeOutFork);
		removeEdgeFromList(edgeIn);
		removeEdgeFromList(edgeOut);
		removeMetadataFromLists(nodeMetadata, meta2DToBeRemoved);

		if(forkPreviousNode != null && forkNextNode != null){
			Edge2D newEdge = new Edge2D(forkPreviousNode, forkNextNode);
			forkPreviousNode.setForkEdgeOut(newEdge);
			forkNextNode.setForkEdgeIn(newEdge);
			addEdgeToList(newEdge);
			addEdgeToGraph(newEdge);
		}
		else if(forkPreviousNode == null && forkNextNode != null){
			forkNextNode.setForkEdgeIn(null);
		}
		else if(forkPreviousNode != null && forkNextNode == null){
			forkPreviousNode.setForkEdgeOut(null);
		}
	}



	// //////////////////////////////////////////////////////////////// PRIVATE - CHANGE COLLOCATION


	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.SIMPLE.</p>
	 * <p>See also: MetadataCollocation.java (SIMPLE)</p>
	 */
	private void changeMetadataCollocationAsSimple(){
		LOGGER.trace("Method changeMetadataCollocationAsSimple() called.");

		if(mEdges2D.isEmpty()){
			Edge2D newEdge = new Edge2D(mStartIdentifier2D, mEndIdentifier2D);
			addEdgeToList(newEdge);
			addEdgeToGraph(newEdge);
		}
	}


	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.CHAIN.</p>
	 * <p>See also: MetadataCollocation.java (CHAIN)</p>
	 */
	private void changeMetadataCollocationAsChain(){
		LOGGER.trace("Method changeMetadataCollocationAsChain() called.");

		Node2D previousNode = mStartIdentifier2D;
		Node2D currentNode  = null;
		Edge2D edge         = null;

		for(Node2D nm : mNodesMetadata2D){
			currentNode = nm;
			edge = new Edge2D(previousNode, currentNode);
			previousNode.setEdgeOut(edge);
			currentNode.setEdgeIn(edge);

			addEdgeToList(edge);
			addEdgeToGraph(edge);
			previousNode = nm;
		}

		edge = new Edge2D(previousNode, mEndIdentifier2D);
		addEdgeToList(edge);
		addEdgeToGraph(edge);
	}


	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.FORK.</p>
	 * <p>See also: MetadataCollocation.java (FORK)</p>
	 */
	private void changeMetadataCollocationAsFork(){
		LOGGER.trace("Method changeMetadataCollocationAsFork() called.");

		Edge2D edgeIn  = null;
		Edge2D edgeOut = null;

		for(Node2D metaNode : mNodesMetadata2D){
			edgeIn  = new Edge2D(mStartIdentifier2D, metaNode);
			edgeOut = new Edge2D(metaNode, mEndIdentifier2D);
			metaNode.setEdgeIn(edgeIn);
			metaNode.setEdgeOut(edgeOut);

			addEdgeToList(edgeIn);
			addEdgeToGraph(edgeIn);
			addEdgeToList(edgeOut);
			addEdgeToGraph(edgeOut);
		}
	}


	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.FORK_LINKED.</p>
	 * <p>See also: MetadataCollocation.java (FORK_LINKED)</p>
	 */
	private void changeMetadataCollocationAsForkLinked(){
		LOGGER.trace("Method changeMetadataCollocationAsForkLinked() called.");

		changeMetadataCollocationAsFork();

		Node2D previousNode = mNodesMetadata2D.get(0);
		Node2D currentNode  = null;
		Edge2D edge         = null;

		for(int i = 1; i < mNodesMetadata2D.size(); i++){
			currentNode = mNodesMetadata2D.get(i);
			edge = new Edge2D(previousNode, currentNode);
			previousNode.setForkEdgeOut(edge);
			currentNode.setForkEdgeIn(edge);
			previousNode = mNodesMetadata2D.get(i);

			addEdgeToList(edge);
			addEdgeToGraph(edge);
		}
	}

	// //////////////////////////////////////////////////////////////////// PRIVATE - HELPER METHODS

	private boolean addMetadataToLists(NodeMetadata nodeMetadata, NodeMetadata2D nodeMetadata2D){
		LOGGER.trace("Method addMetadataToLists("+nodeMetadata+", "+nodeMetadata2D+") called.");

		boolean addSuccessful1 = false;
		boolean addSuccessful2 = false;
		boolean addSuccessful3 = false;
		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			addSuccessful1 = mNodesMetadata.add(nodeMetadata);
			addSuccessful2 = mNodesMetadata2D.add(nodeMetadata2D);
			if(mNodeMetadataMap.put(nodeMetadata, nodeMetadata2D) == null){
				addSuccessful3 = true;
			}
		}

		if(addSuccessful1 && addSuccessful2 && addSuccessful3){
			return true;
		}
		else{
			return false;
		}
	}


	private boolean removeMetadataFromLists(NodeMetadata nodeMetadata,
			NodeMetadata2D nodeMetadata2D){
		LOGGER.trace("Method removeMetadataFromLists("+nodeMetadata+", "+nodeMetadata2D+") called.");

		boolean removeSuccessful1 = false;
		boolean removeSuccessful2 = false;
		boolean removeSuccessful3 = false;
		if(mNodeMetadataMap.containsKey(nodeMetadata)){
			removeSuccessful1 = mNodesMetadata.remove(nodeMetadata);
			removeSuccessful1 = mNodesMetadata2D.remove(nodeMetadata2D);
			if(mNodeMetadataMap.remove(nodeMetadata) != null){
				removeSuccessful3 = true;
			}
		}

		if(removeSuccessful1 && removeSuccessful2 && removeSuccessful3){
			return true;
		}
		else{
			return false;
		}
	}


	private boolean addMetadataToGraph(NodeMetadata2D nodeMetadata2D){
		LOGGER.trace("Method addMetadataToGraph("+nodeMetadata2D+") called.");
		if(!mGraph.containsVertex(nodeMetadata2D)){
			return mGraph.addVertex(nodeMetadata2D);
		}
		return false;
	}


	private boolean removeMetadataFromGraph(NodeMetadata2D nodeMetadata2D){
		LOGGER.trace("Method removeMetadataFromGraph("+nodeMetadata2D+") called.");
		if(mGraph.containsVertex(nodeMetadata2D)){
			return mGraph.removeVertex(nodeMetadata2D);
		}
		return false;
	}


	private boolean addEdgeToList(Edge2D edge2D){
		LOGGER.trace("Method addEdgeToList("+edge2D+") called.");
		return mEdges2D.add(edge2D);
	}


	private boolean removeEdgeFromList(Edge2D edge2D){
		LOGGER.trace("Method removeEdgeFromList("+edge2D+") called.");
		return mEdges2D.remove(edge2D);
	}


	private boolean addEdgeToGraph(Edge2D edge2D){
		LOGGER.trace("Method addEdgeToGraph("+edge2D+") called.");
		if(!mGraph.containsEdge(edge2D)){
			return mGraph.addEdge(edge2D, edge2D.getStart(), edge2D.getEnd());
		}
		return false;
	}


	private boolean removeEdgeFromGraph(Edge2D edge2D){
		LOGGER.trace("Method removeEdgeFromGraph("+edge2D+") called.");
		if(mGraph.containsEdge(edge2D)){
			return mGraph.removeEdge(edge2D);
		}
		return false;
	}


	private boolean hasNodesMetadata2D(){
		return !mNodesMetadata2D.isEmpty();
	}


	private NodeMetadata2D getLastNodeMetadata2D(){
		if(!mNodesMetadata2D.isEmpty()){
			return mNodesMetadata2D.get(mNodesMetadata2D.size()-1);
		}
		return null;
	}



	private void clearMetadataConnection(NodeMetadata2D nodeMetadata2D){
		LOGGER.trace("Method clearMetadataConnection("+nodeMetadata2D+") called.");
		nodeMetadata2D.setForkEdgeIn(null);
		nodeMetadata2D.setForkEdgeOut(null);
		nodeMetadata2D.setEdgeIn(null);
		nodeMetadata2D.setEdgeOut(null);
	}


	/**
	 * Remove all edges from the ExpandedLink2D-object.
	 * Clear all connections of the NodeMetadata2D-objects.
	 */
	private void clearMetadataCollocations(){
		LOGGER.trace("Method removeAllEdgesFromGraph() called.");

		for(Edge2D e : mEdges2D){
			removeEdgeFromGraph(e);
		}
		for(NodeMetadata2D m : mNodesMetadata2D){
			clearMetadataConnection(m);
		}
		mEdges2D.clear();
	}




}
