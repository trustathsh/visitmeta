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
package de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d;

import org.piccolo2d.PNode;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

import de.hshannover.f4.trust.visitmeta.interfaces.Propable;

/**
 * Factory class that creates {@link Piccolo2DGraphicWrapper} based on a given selected node {@link PPath} typName.
 *
 * @author Marcel Reichenbach
 *
 */
public class Piccolo2DGraphicWrapperFactory {

	/**
	 * Creates a new {@link Piccolo2DGraphicWrapper}.
	 *
	 * @param selectedNode the {@link PNode} of the selected node
	 * @param selectedNodeText the {@link PText} of the selected node
	 * @return a new {@link Piccolo2DGraphicWrapper}
	 */
	public static Piccolo2DGraphicWrapper create(PNode selectedNode, PText selectedNodeText) {
		String typeName = getTypeName(selectedNode);

		if (typeName == null) {
			return new Piccolo2DGraphicWrapper(selectedNode, selectedNodeText);
		}

		switch (typeName) {
			case "policy-action":
				return new Piccolo2DPolicyActionGraphicWrapper(selectedNode, selectedNodeText);
			case "identity":
				return new Piccolo2DIdentityGraphicWrapper(selectedNode, selectedNodeText);
			default:
				return new Piccolo2DGraphicWrapper(selectedNode, selectedNodeText);
		}
	}

	private static String getTypeName(PNode node) {
		if (node != null) {
			Object data = node.getParent().getAttribute("data");
			if (data instanceof Propable) {
				Propable propable = (Propable) data;
				String typName = propable.getTypeName();
				return typName;
			}
		}
		return null;
	}
}
