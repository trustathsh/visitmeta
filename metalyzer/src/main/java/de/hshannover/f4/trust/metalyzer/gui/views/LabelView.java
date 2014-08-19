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

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import net.java.dev.designgridlayout.DesignGridLayout;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class LabelView extends View {

	private JLabel mTextDescription;
	private JLabel mTextValue;
	private JPanel mLabelContainer;
	
	private Map<String, TextLabel> mLabelData;

	private DesignGridLayout layout;
	private int mViewIndent;
	
	public LabelView() {
		mLabelData = new LinkedHashMap<String, TextLabel>(); 
		mLabelContainer = new JPanel();
		layout = new DesignGridLayout(mLabelContainer);
		layout.margins(0);
		
		this.add(mLabelContainer);
	}

	public LabelView(String name) {
		mLabelData = new LinkedHashMap<String, TextLabel>(); 
		mLabelContainer = new JPanel();
		Border line = BorderFactory.createLineBorder(Color.GRAY);
		mLabelContainer.setBorder(BorderFactory.createTitledBorder(line,name));
		layout = new DesignGridLayout(mLabelContainer);
	
		this.add(mLabelContainer);
	}
	
	/**
	 * Creates an empty TextLabel, thats can be filled later
	 * @param id the label id of the TextLabel
	 * */
	public void createLabel(String id) {
		addLabel(id, "", "");
	}
	
	/**
	 * Creates a TextLabel with given parameters and adds it to the TextLabelView
	 * @param id the label id of the TextLabel
	 * @param description the text of the label, that describes the value 
	 * @param value a numeric value, that is displayed by the label
	 * @see TextLabel
	 * */
	public void addLabel(String id, String description, Number value) {
		addLabel(id, description, String.valueOf(value));
	}
	
	/**
	 * Creates a TextLabel with given parameters and adds it to the TextLabelView
	 * @param id the label id of the TextLabel
	 * @param description the text of the label, that describes the value 
	 * @param value a text value, that is displayed by the label
	 * @see TextLabel
	 * */
	public void addLabel(String id, String description, String value) {
		mTextDescription = new JLabel(description);
		mTextValue = new JLabel(String.valueOf(value));
		
		mLabelData.put(id, new TextLabel(mTextDescription,mTextValue));
		layout.row().center().indent(mViewIndent).add(mTextDescription).add(mTextValue);
	}

	/**
	 * Sets the value of a TextLabel, by given id
	 * @param id the label id of the TextLabel
	 * @param value a numeric value, that is displayed by the label
	 * @see TextLabel
	 * */
	public void setValueById(String id, Number value) {
		setValueById(id, String.valueOf(value));
	}
	
	/**
	 * Sets the value of a TextLabel, by given id
	 * @param id the label id of the TextLabel
	 * @param value a text value, that is displayed by the label
	 * @see TextLabel
	 * */
	public void setValueById(String id, String value) {
		if(mLabelData.containsKey(id)) {
			TextLabel l = mLabelData.get(id);
			l.setValue(value);
			mLabelData.put(id, l);
		}
	}
	
	/**
	 * Sets the all values of a TextLabel, by given id
	 * @param id the label id of the TextLabel
	 * @param description a text value, that is displayed by the label
	 * @param value a numeric value, that is displayed by the label
	 * @see TextLabel
	 * */
	public void setDataById(String id, String description, Number value) {
		setDataById(id, description, String.valueOf(value));
	}
	
	/**
	 * Sets the all values of a TextLabel, by given id
	 * @param id the label id of the TextLabel
	 * @param description a text value, that is displayed by the label
	 * @param value a text value, that is displayed by the label
	 * @see TextLabel
	 * */
	public void setDataById(String id, String description, String value) {
		if(mLabelData.containsKey(id)) {
			TextLabel l = mLabelData.get(id);
			l.setDescription(description);
			l.setValue(value);
			mLabelData.put(id, l);
		}
	}
	
	/**
	 * Returns all label descriptions and its values
	 * @return a map of all {@link TextLabel} description and values as strings
	 * */
	public LinkedHashMap<String,String> getLabelData() {
		LinkedHashMap<String,String> labelData = new LinkedHashMap<String,String>();
		for(String id : mLabelData.keySet()) {
			TextLabel l = mLabelData.get(id);
			labelData.put(l.getDescription().getText(), l.getValue().getText());
		}
		return labelData;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String key : mLabelData.keySet()) {
			sb.append("ID: "+key+" , ");
			sb.append(mLabelData.get(key));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Set one indentation space before the left-most label.
	 * @param indent 
	 * */
	public void setViewIndent(int indent) {
		this.mViewIndent = indent;
	}
	
	/**
	 * Returns the indentation space before the left-most label.
	 * @return indent 
	 * */
	public int getViewIndent() {
		return mViewIndent;
	}

	class TextLabel {
		private JLabel mDescription;
		private JLabel mValue;
		
		public TextLabel(JLabel description, JLabel value) {
			this.mDescription = description;
			this.mValue = value;
		}
		
		public TextLabel(String description, String value) {
			this.mDescription = new JLabel(description);
			this.mValue = new JLabel(value);
		}
		
		/**
		 * Sets the description of a TextLabel
		 * @param description the text of the description label
		 * */
		public void setDescription(String description) {
			this.mDescription.setText(description);
		}
		
		/**
		 * Returns the description label
		 * @return description label
		 * */
		public JLabel getDescription() {
			return mDescription;
		}
		
		/**
		 * Sets the value of a TextLabel
		 * @param value the text of the value label
		 * */
		public void setValue(String value) {
			this.mValue.setText(value);
		}
		
		/**
		 * Returns the value label
		 * @return value label
		 * */
		public JLabel getValue() {
			return mValue;
		}
		
		@Override
		public String toString() {
			return "Desc: "+mDescription.getText()+", Value:"+mValue.getText();
		}
	}
}

