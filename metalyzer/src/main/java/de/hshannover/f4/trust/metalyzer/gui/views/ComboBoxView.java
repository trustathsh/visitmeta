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
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.ISpannableGridRow;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class ComboBoxView<T> extends View {
	
	private DesignGridLayout layout;
	private ISpannableGridRow row;
	private int mViewIndent;
	private boolean isCenter = false;
	
	private JLabel mDescription;
	private JComboBox<T> mComboBox;

	public ComboBoxView() {
		this("", false);
	}
	
	public ComboBoxView(String description, boolean center) {
		this.isCenter = center;
		this.mDescription = new JLabel(description);
		this.mComboBox = new JComboBox<T>();
		if(isCenter) {
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.add(mDescription);
			this.add(mComboBox);
		} else {
			layout = new DesignGridLayout(this);
			row = layout.row().grid(mDescription).add(mComboBox).empty(5);
		}

	}
	
	/**
	 * Sets the description text of the ComboBoxView
	 * @param description text
	 * */
	public void setDescription(String description) {
		mDescription.setText(description);
	}
	
	/**
	 * Returns the description text of the ComboBoxView
	 * @param description text
	 * */
	public String getDescription() {
		return mDescription.getText();
	}
	
	/**
	 * Adds a ActionListener to the ComboBoxView, that is triggered
	 * when an item is selected
	 * @param listener of the ComboBoxView combobox
	 * */
	public void addListener(ActionListener listener) {
		mComboBox.addActionListener(listener);
	}
	
	/**
	 * Cleas all data of the ComboBoxView
	 * @see JComboBox#removeAllItems()
	 * */
	public void clear() {
		mComboBox.removeAllItems();
	}
	
	/**
	 * Checks whether the combobox contains a item or not
	 * @param item
	 * @return true if the items is in the combox
	 * */
	public boolean containsItem(T item) {
		for(int i = 0; i < mComboBox.getItemCount(); i++) {
			T it = mComboBox.getItemAt(i);
			if(it.equals(item)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds an item to the ComboBoxView combobox
	 * @param item that will be added to the combobox
	 * @see JComboBox#addItem(item)
	 * */
	public void addItem(T item) {
		mComboBox.addItem(item);
	}
	
	/**
	 * Returns the default item of the combobox, that is currently selected
	 * @return item the default item
	 * @see JComboBox#getSelectedItem()
	 * */
	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T) mComboBox.getSelectedItem();
	}
	
	/**
	 * Set one indentation space before the left-most label.
	 * @param indent is set if isCenter is false
	 * */
	public void setViewIndent(int indent) {
		if(!isCenter) {
			this.mViewIndent = indent;
			row.indent(mViewIndent);
		}
	}
	
	/**
	 * Returns the indentation space before the left-most label.
	 * @return indent returns -1 if isCenter is true
	 * */
	public int getViewIndent() {
		if(isCenter) {
			return -1;
		}
		return mViewIndent;
	}


}
