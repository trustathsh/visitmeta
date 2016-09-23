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
package de.hshannover.f4.trust.visitmeta.datawrapper.policy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.piccolo2d.nodes.PPath;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;

public abstract class PolicyNode {

	private static Set<GraphicWrapper> getParents(GraphicWrapper selectedNode, PolicyType policyType) {
		Set<GraphicWrapper> parents = new HashSet<GraphicWrapper>();
		Propable selectedPropable = (Propable) selectedNode.getData();
		List<GraphicWrapper> edgesNodes = selectedNode.getEdgesNodes();

		String identStartingType;

		switch (selectedNode.getNodeType()) {
		// If selected node = IDENTIFIER
			case IDENTIFIER:
				identStartingType = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) selectedPropable);
				for (GraphicWrapper edgeNode : edgesNodes) {
					// check for edges between two policy identifier
					List<GraphicWrapper> tmpEdgesNodes = edgeNode.getEdgesNodes();
					for (GraphicWrapper tmpEdgeNode : tmpEdgesNodes) {
						// now check the identifier edges
						Propable propable = (Propable) tmpEdgeNode.getData();
						String typeName = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) propable);
						if (PolicyHierarchy.isParent(identStartingType, typeName, policyType)) {
							parents.add(tmpEdgeNode);
							parents.add(edgeNode);
						}
					}
				}
				break;
			case METADATA:
				// If selected node = METADATA
				for (GraphicWrapper identifierNode : edgesNodes) {
					Propable identifier = (Propable) identifierNode.getData();
					identStartingType = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) identifier);

					// if selected policy-action or policy-feature metadata
					String metadataTypeName = selectedPropable.getTypeName();
					if (PolicyHierarchy.isParent(metadataTypeName, identStartingType, policyType)) {
						parents.add(identifierNode);
					}

					// for metadata between two policy identifier
					// check who is the child
					for (GraphicWrapper otherIdentifierNode : edgesNodes) {
						if (identifierNode != otherIdentifierNode) {
							Propable otherIdentifier = (Propable) otherIdentifierNode.getData();
							String typeName = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) otherIdentifier);
							if (PolicyHierarchy.isParent(identStartingType, typeName, policyType)) {
								parents.add(otherIdentifierNode);
							}
						}
					}
				}
				break;
			default:
				break;
		}

		return parents;
	}

	public static Set<GraphicWrapper> getAllChilds(GraphicWrapper selectedNode, PolicyType policyType) {
		Set<GraphicWrapper> allNodeChilds = new HashSet<GraphicWrapper>();
		
		Set<GraphicWrapper> selectedNodeChilds = getChilds(selectedNode, policyType);
		allNodeChilds.addAll(selectedNodeChilds);

		do {
			Set<GraphicWrapper> tmpNodeChilds = new HashSet<GraphicWrapper>();
			
			for (GraphicWrapper nodeChild : selectedNodeChilds) {
				tmpNodeChilds.addAll(getChilds(nodeChild, policyType));
			}

			if (!tmpNodeChilds.isEmpty()) {
				allNodeChilds.addAll(tmpNodeChilds);
			}

			selectedNodeChilds = tmpNodeChilds;

		} while (!selectedNodeChilds.isEmpty());

		return allNodeChilds;
	}

	public static Set<GraphicWrapper> getAllParents(GraphicWrapper selectedNode, PolicyType type) {
		Set<GraphicWrapper> allNodeParents = new HashSet<GraphicWrapper>();

		Set<GraphicWrapper> selectedNodeParents = getParents(selectedNode, type);
		allNodeParents.addAll(selectedNodeParents);

		do {

			Set<GraphicWrapper> tmpNodeParents = new HashSet<GraphicWrapper>();

			for (GraphicWrapper nodeParent : selectedNodeParents) {
				tmpNodeParents.addAll(getParents(nodeParent, type));
			}

			if (!tmpNodeParents.isEmpty()) {
				allNodeParents.addAll(tmpNodeParents);
			}

			selectedNodeParents = tmpNodeParents;

		} while (!selectedNodeParents.isEmpty());
		
		return allNodeParents;
	}

	public static Set<GraphicWrapper> getChilds(GraphicWrapper selectedNode, PolicyType policyType) {
		Set<GraphicWrapper> childs = new HashSet<GraphicWrapper>();
		Propable selectedPropable = (Propable) selectedNode.getData();
		List<GraphicWrapper> edgesNodes = selectedNode.getEdgesNodes();

		switch (selectedNode.getNodeType()) {
			case IDENTIFIER:
				// If selected node = IDENTIFIER
				String identStartingType = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) selectedPropable);
				for (GraphicWrapper edgeNode : edgesNodes) {

					// check for metadata attached to identifier
					Propable metadata = (Propable) edgeNode.getData();
					String metadataTypeName = metadata.getTypeName();
					if (PolicyHierarchy.isChild(identStartingType, metadataTypeName, policyType)) {
						childs.add(edgeNode);
					}

					// check for edges between two policy identifier
					List<GraphicWrapper> tmpEdgesNodes = edgeNode.getEdgesNodes();
					for (GraphicWrapper tmpEdgeNode : tmpEdgesNodes) {
						// now check the identifier edges
						Propable propable = (Propable) tmpEdgeNode.getData();
						String typeName = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) propable);
						if (PolicyHierarchy.isChild(identStartingType, typeName, policyType)) {
							childs.add(tmpEdgeNode);
							childs.add(edgeNode);
						}
					}
				}
				break;
			case METADATA:
				// If selected node = METADATA
				// for metadata between two policy identifier
				for (GraphicWrapper identifierNode : edgesNodes) {
					Propable identifier = (Propable) identifierNode.getData();
					String metaStartingType = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) identifier);
					// check who is the child
					for (GraphicWrapper otherIdentifierNode : edgesNodes) {
						if (identifierNode != otherIdentifierNode) {
							Propable otherIdentifier = (Propable) otherIdentifierNode.getData();
							String typeName = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName((Identifier) otherIdentifier);
							if (PolicyHierarchy.isChild(metaStartingType, typeName, policyType)) {
								childs.add(otherIdentifierNode);
							}
						}
					}
				}
				break;
			default:
				break;
		}

		return childs;
	}

	public static Set<PPath> getEdges(Set<GraphicWrapper> policyNodes) {
		Set<PPath> edges = new HashSet<PPath>();

		for (GraphicWrapper policyNode : policyNodes) {
			List<PPath> tmpEdges = policyNode.getEdges();
			edges.addAll(tmpEdges);
		}

		return edges;
	}
}
