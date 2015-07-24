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
package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LayoutHelper {

	// Auxiliary objects so that all distances are harmonious.
	public static final Insets mNullInsets = new Insets(0, 0, 0, 0); // no free space

	public static final Insets mLblInsets = new Insets(2, 5, 2, 2); // label distances

	public static final Insets mXinsets = new Insets(0, 10, 0, 10); // Input components Clearances

	/**
	 * Hilfsroutine beim Hinzufuegen einer Komponente zu einem Container im
	 * GridBagLayout. Die Parameter sind Constraints beim Hinzufuegen.
	 * 
	 * @param x x-Position
	 * @param y y-Position
	 * @param width Breite in Zellen
	 * @param height Hoehe in Zellen
	 * @param weightx Gewicht
	 * @param weighty Gewicht
	 * @param cont Container
	 * @param comp Hinzuzufuegende Komponente
	 * @param insets Abstaende rund um die Komponente
	 */
	public static void addComponent(int x, int y, int width, int height, double weightx, double weighty,
			Container cont, Component comp, Insets insets) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.insets = insets;
		cont.add(comp, gbc);
	}
}
