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
 * This file is part of visitmeta metalyzer, version 0.0.1,
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
package de.hshannover.f4.trust.metalyzer.gui.views;

import java.awt.FlowLayout;

import javax.swing.JPanel;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public abstract class View extends JPanel {
	
	private String viewName;
	private FlowLayout layout;
	
	public View() {
		layout = new FlowLayout(FlowLayout.LEADING);
		this.setLayout(layout);
	}
	
	/**
	 * Sets the layout alignment of the view
	 * @param alignment 
	 * @see FlowLayout
	 * */
	public void setLayoutAlignment(int alignment) {
		layout.setAlignment(alignment);
	}
	
	/**
	 * Sets the layout alignment of the view
	 * @return alignment 
	 * @see FlowLayout
	 * */
	public int getLayoutAlignment() {
		return layout.getAlignment();
	}
	
	/**
	 * Sets the name of the view
	 * @param viewName the name of view
	 * @see String
	 * */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	/**
	 * Returns the name of the view
	 * @return viewName returns the name of the view
	 * */
	public String getViewName() {
		return viewName;
	}
	
	/**
	 * Sets the visibility of the view
	 * @param enabled if enabled is true the view is shown, otherwise not
	 * @see JPanel#setEnabled(boolean)
	 * */
	public void setViewEnabled(boolean enabled) {
		this.setEnabled(enabled);
	}
	
	/**
	 * Determines whether view is enabled.
	 * @return true if the view is enabled, false otherwise
	 * @see JPanel#isEnabled()
	 */
	public boolean isViewEnabled() {
		return this.isEnabled();
	}
}
