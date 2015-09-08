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
 * This file is part of visitmeta-visualization, version 0.5.2,
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

import de.hshannover.f4.trust.visitmeta.datawrapper.NodeIdentifier;
import de.hshannover.f4.trust.visitmeta.datawrapper.NodeMetadata;
import de.hshannover.f4.trust.visitmeta.graphCalculator.MetadataCollocation;
import edu.uci.ics.jung.graph.Graph;



/**
 * A node-class to represent an identifier.
 */
public class NodeIdentifier2D extends Node2D{

	// ///////////////////////////////////////////////////////////////////////////////////// MEMBERS

	private NodeIdentifier                    mNodeIdentifier;
	private List<ExpandedLink2D>              mExpandedLinks2D;

	private Graph<Node2D, Edge2D>             mGraph;
	private MetadataCollocation               mMetadataCollocation;

	private List<NodeMetadata>                mNodesMetadata;
	private List<NodeMetadata2D>              mNodesMetadata2D;
	private Map<NodeMetadata, NodeMetadata2D> mNodeMetadataMap;
	private List<Edge2D>                      mEdges2D;

	private static final Logger               LOGGER = Logger.getLogger(NodeIdentifier2D.class);

	// //////////////////////////////////////////////////////////////////////////////// CONSTRUCTORS

	public NodeIdentifier2D(NodeIdentifier nodeIdentifier, Graph<Node2D, Edge2D> graph,
			Layout2D layout2D, MetadataCollocation metadataCollocation){
		super(nodeIdentifier, layout2D);

		mNodeIdentifier = nodeIdentifier;
		mExpandedLinks2D = new ArrayList<>();

		mGraph = graph;
		mLayout2D = layout2D;
		mMetadataCollocation = metadataCollocation;

		mNodesMetadata   = new ArrayList<>();
		mNodesMetadata2D = new ArrayList<>();
		mNodeMetadataMap = new HashMap<>();
		mEdges2D = new ArrayList<>();

		for(NodeMetadata nodeMeta : nodeIdentifier.getMetadata()){
			addNodeMetadata(nodeMeta);
		}
	}

	// ///////////////////////////////////////////////////////////////////////// PUBLIC - ADD REMOVE


	/**
	 * Adds and alignes a metadata-node to the NodeIdentifier2D-object.
	 *
	 * @param nodeMetadata The metadata-node to be added and aligned.
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
	 * Removes a metadata-node from the NodeIdentifier2D-object.
	 *
	 * @param nodeMetadata The metadata-node to be removed
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
	 * Add ExpandedLink2D
	 * @param expandedLink2D Link to be added
	 */
	public void addExpandedLink2D(ExpandedLink2D expandedLink2D){
		LOGGER.trace("Method addExpandedLink2D("+expandedLink2D+") called.");
		mExpandedLinks2D.add(expandedLink2D);
	}


	/**
	 * Remove this instance (NodeMetadata2D) from graph.
	 */
	public void removeYourself(){
		LOGGER.trace("Method removeYourself() called.");

		for(NodeMetadata n : mNodesMetadata){
			removeNodeMetadata(n);
		}
		if(mGraph.containsVertex(this)){
			mGraph.removeVertex(this);
		}
	}


	// ///////////////////////////////////////////////////////////////////////// PUBLIC - ADD REMOVE

	/**
	 * Changes the collocation (in other words: alignment or orientation) of the metadata-nodes.
	 * @param metadataCollocation The collocation-type for the metadata-nodes
	 */
	public void changeMetadataCollocation(MetadataCollocation metadataCollocation){
		LOGGER.trace("Method changeMetadataCollocation(" + metadataCollocation + ") called.");

		mMetadataCollocation = metadataCollocation;
		clearMetadataCollocations();

		switch(mMetadataCollocation){
			case SIMPLE:      changeMetadataCollocationAsSimple(); break;
			case CHAIN:       changeMetadataCollocationAsChain(); break;
			case FORK:        changeMetadataCollocationAsFork(); break;
			case FORK_LINKED: changeMetadataCollocationAsForkLinked(); break;
		}
	}


	// ///////////////////////////////////////////////////////////////////////////// PUBLIC - GETTER

	public List<ExpandedLink2D> getExpandedLinks2D(){
		LOGGER.trace("Method getExpandedLinks2D() called.");
		return mExpandedLinks2D;
	}

	public int getMetadatasCount(){
		LOGGER.trace("Method getMetadatasCount() called.");
		return mNodesMetadata2D.size();
	}

	public List<Edge2D> getEdges(){
		LOGGER.trace("Method getEdges() called.");
		return mEdges2D;
	}

	public List<NodeMetadata> getNodesMetadata(){
		LOGGER.trace("Method getNodesMetadata() called.");
		return mNodesMetadata;
	}

	public NodeMetadata getNodeMetadata(int i){
		LOGGER.trace("Method getNodesMetadata() called.");
		return mNodesMetadata.get(i);
	}

	public List<NodeMetadata2D> getNodesMetadata2D(){
		LOGGER.trace("Method getNodesMetadata2D() called.");
		return mNodesMetadata2D;
	}

	public NodeMetadata2D getNodeMetadata2D(int i){
		LOGGER.trace("Method getNodesMetadata2D("+i+") called.");
		return mNodesMetadata2D.get(i);
	}

	public NodeMetadata2D getNodeMetadata2D(NodeMetadata nodeMetadata){
		LOGGER.trace("Method getNodesMetadata2D("+nodeMetadata+") called.");
		return mNodeMetadataMap.get(nodeMetadata);
	}

	public Map<NodeMetadata, NodeMetadata2D> getNodeMetadataMap(){
		LOGGER.trace("Method getNodeMetadataMap() called.");
		return mNodeMetadataMap;
	}

	// /////////////////////////////////////////////////////////////////////////////////////// SUPER

	@Override
	public String toString(){
		LOGGER.trace("Method toString() called.");
		return "Vi" + mId + "|" + mNodeIdentifier.getIdentifier().getTypeName();
	}

	@Override
	public String getContent(){
		LOGGER.trace("Method getContent() called.");
		return mNodeIdentifier.getIdentifier().getTypeName();
	}

	@Override
	public int getContentLength(){
		LOGGER.trace("Method getContentLength() called.");
		return mNodeIdentifier.getIdentifier().getTypeName().length();
	}


	// //////////////////////////////////////////////////////////////////////// PRIVATE - ADD REMOVE

	/**
	 * Append and aling this metadata-node as SIMPLE.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsSimple(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsSimple(" + nodeMetadata + ") called.");

		NodeMetadata2D newMeta2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		addMetadataToLists(nodeMetadata, newMeta2D);
	}


	/**
	 * Removes this metadata-node as SIMPLE.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsSimple(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsSimple(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D meta2D = mNodeMetadataMap.get(nodeMetadata);
		removeMetadataFromLists(nodeMetadata, meta2D);
	}


	/**
	 * Append and aling this metadata-node as CHAIN.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsChain(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsChain(" + nodeMetadata + ") called.");

		NodeMetadata2D newMeta2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		Node2D nodePrevious      = null;
		Edge2D edge              = null;

		if(firstMetadata()){
			nodePrevious = this;
		}
		else{
			nodePrevious = getLastNodeMetadata2D();
		}

		edge = new Edge2D(nodePrevious, newMeta2D);
		if(!firstMetadata()){
			nodePrevious.setEdgeOut(edge);
		}
		newMeta2D.setEdgeIn(edge);

		addMetadataToLists(nodeMetadata, newMeta2D);
		addEdgeToList(edge);

		addMetadataToGraph(newMeta2D);
		addEdgeToGraph(edge);
		newMeta2D.setPositionTriggeredByJung(nodePrevious);
	}


	/**
	 * Remove this metadata-node as FORK_LINKED.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsChain(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsChain(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D meta2DToBeRemoved = mNodeMetadataMap.get(nodeMetadata);

		if(meta2DToBeRemoved.getEdgeOut() == null){
			//the node to be removed is the last one in the chain
			Edge2D edge2DToBeRemoved = meta2DToBeRemoved.getEdgeIn();
			Node2D node2DPrevious = edge2DToBeRemoved.getStart();

			removeEdgeFromGraph(edge2DToBeRemoved);
			removeMetadataFromGraph(meta2DToBeRemoved);

			removeEdgeFromList(edge2DToBeRemoved);
			removeMetadataFromLists(nodeMetadata, meta2DToBeRemoved);

			node2DPrevious.setEdgeOut(null);
		}
		else{
			//the node to be removed is somewhere in the middle of the chain
			Edge2D edge2DIn       = meta2DToBeRemoved.getEdgeIn();
			Edge2D edge2DOut      = meta2DToBeRemoved.getEdgeOut();
			Node2D node2DPrevious = edge2DIn.getStart();
			Node2D node2DNext     = edge2DOut.getEnd();
			Edge2D newEdge2D      = new Edge2D(node2DPrevious, node2DNext);

			removeEdgeFromGraph(edge2DIn);
			removeEdgeFromGraph(edge2DOut);
			removeMetadataFromGraph(meta2DToBeRemoved);

			removeEdgeFromList(edge2DIn);
			removeEdgeFromList(edge2DOut);
			removeMetadataFromLists(nodeMetadata, meta2DToBeRemoved);

			node2DPrevious.setEdgeOut(newEdge2D);
			node2DNext.setEdgeIn(newEdge2D);
			addEdgeToList(newEdge2D);
			addEdgeToGraph(newEdge2D);
		}
	}



	/**
	 * Append and aling this metadata-node as FORK.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsFork(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsFork(" + nodeMetadata + ") called.");

		NodeMetadata2D newMeta2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		Edge2D newEdge = new Edge2D(this, newMeta2D);

		newMeta2D.setEdgeIn(newEdge);

		addMetadataToLists(nodeMetadata, newMeta2D);
		addEdgeToList(newEdge);

		addMetadataToGraph(newMeta2D);
		addEdgeToGraph(newEdge);
		newMeta2D.setPositionTriggeredByJung(this);
	}


	/**
	 * Remove this metadata-node as FORK_LINKED.
	 * @param nodeMetadata The metadata-node to be removed.
	 */
	private void removeNodeMetadataAsFork(NodeMetadata nodeMetadata){
		LOGGER.trace("Method removeNodeMetadataAsFork(" + nodeMetadata + ") called.");

		if(!mNodeMetadataMap.containsKey(nodeMetadata)){
			return;
		}

		NodeMetadata2D meat2DToBeRemoved = mNodeMetadataMap.get(nodeMetadata);
		Edge2D edgeToBeRemoved           = meat2DToBeRemoved.getEdgeIn();

		removeEdgeFromGraph(edgeToBeRemoved);
		removeMetadataFromGraph(meat2DToBeRemoved);

		removeEdgeFromList(edgeToBeRemoved);
		removeMetadataFromLists(nodeMetadata, meat2DToBeRemoved);
	}


	/**
	 * Append and aling this metadata-node as FORK_LINKED.
	 * @param nodeMetadata The metadata-node to be added and arranged.
	 */
	private void addNodeMetadataAsForkLinked(NodeMetadata nodeMetadata){
		LOGGER.trace("Method addNodeMetadataAsForkLinked(" + nodeMetadata + ") called.");

		boolean firstNodeMetadata = firstMetadata();

		NodeMetadata2D newMeta2D = new NodeMetadata2D(nodeMetadata, mLayout2D, this);
		Edge2D mainEdge          = new Edge2D(this, newMeta2D);
		Edge2D forkEdge          = null;

		newMeta2D.setEdgeIn(mainEdge);

		if(!firstNodeMetadata){
			forkEdge = new Edge2D(getLastNodeMetadata2D(), newMeta2D);
			getLastNodeMetadata2D().setForkEdgeOut(forkEdge);
			newMeta2D.setForkEdgeIn(forkEdge);
		}

		addMetadataToLists(nodeMetadata, newMeta2D);
		addEdgeToList(mainEdge);
		if(!firstNodeMetadata){
			addEdgeToList(forkEdge);
		}

		addMetadataToGraph(newMeta2D);
		addEdgeToGraph(mainEdge);
		if(!firstNodeMetadata){
			addEdgeToGraph(forkEdge);
			newMeta2D.setPositionTriggeredByJung(getLastNodeMetadata2D());
		}
		else{
			newMeta2D.setPositionTriggeredByJung(this);
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

		Edge2D mainEdge         = meta2DToBeRemoved.getEdgeIn();
		Edge2D forkEdgeIn       = meta2DToBeRemoved.getForkEdgeIn();
		Edge2D forkEdgeOut      = meta2DToBeRemoved.getForkEdgeOut();
		Node2D forkPreviousNode = null;
		Node2D forkNextNode     = null;
		Edge2D newEdge          = null;

		if(forkEdgeIn != null){
			forkPreviousNode = forkEdgeIn.getStart();
			removeEdgeFromGraph(forkEdgeIn);
		}
		if(forkEdgeOut != null){
			forkNextNode = forkEdgeOut.getEnd();
			removeEdgeFromGraph(forkEdgeOut);
		}
		removeEdgeFromGraph(mainEdge);
		removeMetadataFromGraph(meta2DToBeRemoved);

		removeEdgeFromList(forkEdgeIn);
		removeEdgeFromList(forkEdgeOut);
		removeEdgeFromList(mainEdge);
		removeMetadataFromLists(nodeMetadata, meta2DToBeRemoved);

		if(forkPreviousNode != null && forkNextNode != null){
			newEdge = new Edge2D(forkPreviousNode, forkNextNode);
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

		// Yes, do nothing!
	}


	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.CHAIN.</p>
	 * <p>See also: MetadataCollocation.java (CHAIN)</p>
	 */
	private void changeMetadataCollocationAsChain(){
		LOGGER.trace("Method changeMetadataCollocationAsChain() called.");

		if(mNodesMetadata2D.size() > 0){
			Node2D from = this;
			Node2D to   = mNodesMetadata2D.get(0);
			Edge2D edge = new Edge2D(from, to);

			to.setEdgeIn(edge);
			addEdgeToList(edge);
			addEdgeToGraph(edge);

			for(int i = 1; i < mNodesMetadata2D.size(); i++){
				from = mNodesMetadata2D.get(i - 1);
				to   = mNodesMetadata2D.get(i);
				edge = new Edge2D(from, to);

				from.setEdgeOut(edge);
				to.setEdgeIn(edge);
				addEdgeToList(edge);
				addEdgeToGraph(edge);
			}
		}
	}

	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.FORK.</p>
	 * <p>See also: MetadataCollocation.java (FORK)</p>
	 */
	private void changeMetadataCollocationAsFork(){
		LOGGER.trace("Method changeMetadataCollocationAsFork() called.");

		Node2D from = this;
		Node2D to;
		Edge2D edge;

		for(Node2D m : mNodesMetadata2D){
			to   = m;
			edge = new Edge2D(from, to);

			to.setEdgeIn(edge);
			addEdgeToList(edge);
			addEdgeToGraph(edge);
		}
	}

	/**
	 * <p>Arranges the collocation of the metadata-nodes as MetadataCollocation.FORK_LINKED.</p>
	 * <p>See also: MetadataCollocation.java (FORK_LINKED)</p>
	 */
	private void changeMetadataCollocationAsForkLinked(){
		LOGGER.trace("Method changeMetadataCollocationAsForkLinked() called.");

		changeMetadataCollocationAsFork();

		Node2D from;
		Node2D to;
		Edge2D edge;

		for(int i = 1; i < mNodesMetadata2D.size(); i++){
			from = mNodesMetadata2D.get(i - 1);
			to = mNodesMetadata2D.get(i);
			edge = new Edge2D(from, to);

			from.setForkEdgeOut(edge);
			to.setForkEdgeIn(edge);
			addEdgeToList(edge);
			addEdgeToGraph(edge);
		}
	}



	// //////////////////////////////////////////////////////////////////////////// PRIVATE - HELPER

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



	/**
	 * Remove all edges from the ExpandedLink2D-object.
	 * Clear all connections of the NodeMetadata2D-objects.
	 */
	private void clearMetadataCollocations(){
		LOGGER.trace("Method removeAllEdgesFromGraph() called.");

		for(Edge2D e : mEdges2D){
			if(mGraph.containsEdge(e)){
				mGraph.removeEdge(e);
			}
		}
		for(Node2D m : mNodesMetadata2D){
			m.setForkEdgeIn(null);
			m.setForkEdgeOut(null);
			m.setEdgeIn(null);
			m.setEdgeOut(null);
		}
		mEdges2D.clear();
	}


	private boolean addEdgeToList(Edge2D edge2D){
		LOGGER.trace("Method addEdgeToList("+edge2D+") called.");
		if(!mEdges2D.contains(edge2D)){
			return mEdges2D.add(edge2D);
		}
		else{
			return false;
		}
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


	private boolean firstMetadata(){
		return mNodesMetadata2D.isEmpty();
	}


	private NodeMetadata2D getLastNodeMetadata2D(){
		if(!mNodesMetadata2D.isEmpty()){
			return mNodesMetadata2D.get(mNodesMetadata2D.size()-1);
		}
		return null;
	}




}
