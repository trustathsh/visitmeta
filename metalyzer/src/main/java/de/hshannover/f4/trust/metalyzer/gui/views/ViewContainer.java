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

import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class ViewContainer extends View{
	
	private BoxLayout layout;
	private LinkedList<View> views;

	public ViewContainer() {
		views = new LinkedList<View>();
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
	}
	
	/**
	 * Adds a view to the ViewContainer
	 * @param view the view that should be added
	 **/
	public void addView(View view) {
		views.add(view);
		restructure(views);
	}
	
	/**
	 * Adds a view to the ViewContainer
	 * @param index the position where the view is placed e.g 1 after the TopView
	 * @param view the view that should be added
	 **/
	public void addView(int index, View view) {
		views.add(index, view);
		restructure(views);
	}
	
	/**
	 * Adds a view at the top position to the ViewContainer
	 * @param view the view that should be added
	 **/
	public void addTopView(View view) {
		views.addFirst(view);
		restructure(views);
	}
	
	/**
	 * Adds a view at the bottom position to the ViewContainer
	 * @param view the view that should be added
	 **/
	public void addBottomView(View view) {
		views.addLast(view);
		restructure(views);
	}
	
	/**
	 * Removies a view from the ViewContainer
	 * @param view the view that should be removed
	 **/
	public void removeView(View view) {
		views.remove(view);
		restructure(views);
	}
	
	/**
	 * Restructures the view position of the ViewContainer
	 * @param views the list of {@link Views} held by this container
	 * */
	private void restructure(List<View> views) {
		this.removeAll();
		for(View v : views) {
			v.setAlignmentX(CENTER_ALIGNMENT);
			this.add(v);
		}
	}
	
	/**
	 * Checks whether the  view container has the view or not
	 * @param view 
	 * @return true when thr view container contains the given view
	 * */
	public boolean hasView(View view) {
		return views.contains(view);
	}
}
