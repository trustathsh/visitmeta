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
 * This file is part of visitmeta visualization, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d;

import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * A class that extends {@link PBasicInputEventHandler}.
 * Handles clicks of the left mouse button and clears the panel that shows
 * properties of {@link Identifier} or {@link Metadata} objects.
 * 
 * Used to allow de-selecting nodes by clicking in the background.
 * 
 * @author Bastian Hellmann
 *
 */
public class NodeSelectionEventHandler extends PBasicInputEventHandler {

	private static final int MOUSE_LEFT_BUTTON = 1;
	private GraphConnection mConnection;

	public NodeSelectionEventHandler(GraphConnection connection) {
		mConnection = connection;
	}

	@Override
	public void mouseClicked(PInputEvent event) {
		super.mouseClicked(event);
		PNode pickedNode = event.getPickedNode();

		if (event.getButton() == MOUSE_LEFT_BUTTON && (pickedNode instanceof PCamera || pickedNode instanceof PPath)) {
			mConnection.setAndShowPropable(null, false);
		}
	}
}
