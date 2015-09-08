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
package de.hshannover.f4.trust.visitmeta.graphDrawer.piccolo2d.noderenderer;

import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * Interface that defines methods how to draw nodes within Piccolo2D.
 *
 * @author Bastian Hellmann
 *
 */
public interface Piccolo2dNodeRenderer {

	/**
	 * Create a {@link PPath} object that represents an {@link Identifier} object.
	 *
	 * @param identifier
	 *            the {@link Identifier} object
	 * @param text
	 *            the {@link PText} object that represents information about the {@link Identifier}
	 * @return a {@link PPath} object
	 */
	public PPath createNode(Identifier identifier, PText text);

	/**
	 * Create a {@link PPath} object that represents an {@link Metadata} object.
	 *
	 * @param metadata
	 *            the {@link Metadata} object
	 * @param text
	 *            the {@link PText} object that represents information about the {@link Metadata}
	 * @return a {@link PPath} object
	 */
	public PPath createNode(Metadata metadata, PText text);
}
