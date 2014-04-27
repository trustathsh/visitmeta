package de.hshannover.f4.trust.visitmeta.ifmap.dumpData;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of irongui, version 0.4.1, implemented by the Trust@FHH
 * research group at the Hochschule Hannover, a program to visualize the content
 * of a MAP Server (MAPS), a crucial component within the TNC architecture.
 * 
 * The development was started within the bachelor
 * thesis of Tobias Ruhe at Hochschule Hannover (University of
 * Applied Sciences and Arts Hannover). irongui is now maintained
 * and extended within the ESUKOM research project. More information
 * can be found at the Trust@FHH website.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
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


import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;

public class Metadata {

	private String mName;
	private String mPublisher;
	private String mCardinality;
	private String mTimestamp;
	private ArrayList<MetadataEntry> mChilds;
	private HashMap<String, String> mAttributes;
	private Document mDocument;


	public Metadata() {
		mChilds = new ArrayList<MetadataEntry>();
		mAttributes = new HashMap<String, String>();
		mPublisher = "";
		mCardinality = "";
		mTimestamp = "";
	}
	public Metadata(String name) {
		this();
		mName = name;
	}

	public Metadata(String name, String publisher, String cardinality, String timestamp){
		mName = name;
		mPublisher = publisher;
		mCardinality = cardinality;
		mTimestamp = timestamp;
	}

	public Metadata(String name, String publisher, String cardinality, String timestamp,
			ArrayList<MetadataEntry> childs){
		mName = name;
		mPublisher = publisher;
		mCardinality = cardinality;
		mTimestamp = timestamp;
		if(childs != null){
			mChilds = childs;
		}
		else {
			mChilds = new ArrayList<MetadataEntry>();
		}
	}

	public boolean hasChilds(){
		return !mChilds.isEmpty();
	}

	public void addChild(MetadataEntry entry){
		mChilds.add(entry);
	}

	public boolean hasAttributes(){
		return !mAttributes.isEmpty();
	}

	public void addAttribute(String key, String value){
		mAttributes.put(key, value);
	}

	public String getAttribute(String key){
		return mAttributes.get(key);
	}

	public ArrayList<MetadataEntry> getChilds(){
		return mChilds;
	}

	public HashMap<String, String> getAttributes() {
		return mAttributes;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getPublisher() {
		return mPublisher;
	}

	public void setPublisher(String publisher) {
		mPublisher = publisher;
	}

	public String getCardinality() {
		return mCardinality;
	}

	public void setCardinality(String cardinality) {
		mCardinality = cardinality;
	}

	public boolean isSingleValue(){
		return mCardinality.equalsIgnoreCase("singleValue");
	}

	public String getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(String timestamp) {
		mTimestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(this == o) return true;
		if(!(o instanceof Metadata)) return false;

		Metadata meta = (Metadata)o;

		if(this.hashCode() == meta.hashCode()){
			return true;
		}
		else return false;
	}

	@Override
	public int hashCode(){
		StringBuffer buf = new StringBuffer();
		buf.append(this.mName);
		buf.append(this.mPublisher);
		buf.append(this.mCardinality);
		buf.append(this.mAttributes.toString());
		buf.append(this.mChilds.toString());
		return buf.toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("name="+this.mName+", publisher="+this.mPublisher+", timestamp="+this.mTimestamp+" ,cardinality="+
				this.mCardinality+" [ "+this.mChilds.toString()+" ] {"+this.hashCode()+"}");
		return buf.toString();
	}

	public void setDocument(Document newDocument) {
		mDocument = newDocument;
	}

	public Document getDocument() {
		return mDocument;
	}
}
