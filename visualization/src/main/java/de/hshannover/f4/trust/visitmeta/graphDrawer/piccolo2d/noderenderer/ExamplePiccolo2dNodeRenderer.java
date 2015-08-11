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
 * This file is part of visitmeta-visualization, version 0.5.0,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer;

import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

import de.hshannover.f4.trust.visitmeta.IfmapStrings;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.IdentifierWrapper;

/**
 * {@link Piccolo2dNodeRenderer} that shows possible usage of the interface.
 * All identifiers are rendered as rectangles with rounded corners, but extended identifiers as ellipses.
 * All metadata nodes are rendered as rectangles with rectangular corners, except device-ip metadata (rendered as
 * ellipses).
 *
 * Uses the static methods from {@link EllipsePiccolo2dNodeRenderer},
 * {@link RectanglesWithRectangularCornersPiccolo2dNodeRenderer} and
 * {@link RectanglesWithRoundedCornersPiccolo2dNodeRenderer} classes.
 *
 * @author Bastian Hellmann
 *
 */
public class ExamplePiccolo2dNodeRenderer implements Piccolo2dNodeRenderer {

	@Override
	public PPath createNode(Identifier identifier, PText text) {
		IdentifierWrapper wrapper = IdentifierHelper.identifier(identifier);
		String typeName = wrapper.getTypeName();
		switch (typeName) {
			case IfmapStrings.IDENTITY_EL_NAME:
				if (ExtendedIdentifierHelper.isExtendedIdentifier(identifier)) {
					String extendedIdentifierTypeName =
							ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName(identifier);
					if (extendedIdentifierTypeName.equals("service")) {
						return EllipsePiccolo2dNodeRenderer.createNode(text);
					}
				}
			case IfmapStrings.ACCESS_REQUEST_EL_NAME:
			case IfmapStrings.DEVICE_EL_NAME:
			case IfmapStrings.IP_ADDRESS_EL_NAME:
			case IfmapStrings.MAC_ADDRESS_EL_NAME:
			default:
				return RectanglesWithRoundedCornersPiccolo2dNodeRenderer.createNode(text);
		}
	}

	@Override
	public PPath createNode(Metadata metadata, PText text) {
		String typeName = metadata.getTypeName();
		switch (typeName) {
			case "device-ip":
				return EllipsePiccolo2dNodeRenderer.createNode(text);
			default:
				return RectanglesWithRectangularCornersPiccolo2dNodeRenderer.createNode(text);
		}
	}

}
