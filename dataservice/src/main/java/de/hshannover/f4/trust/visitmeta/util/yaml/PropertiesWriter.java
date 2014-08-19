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
 * This file is part of visitmeta dataservice, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.util.yaml;

import java.io.FileNotFoundException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.util.Properties;

public class PropertiesWriter extends YamlPersister {

	private static final Logger log = Logger.getLogger(PropertiesWriter.class);

	public static final String TAG = "!Properties";


	private String mFileName;

	private boolean mAppend;


	private DumperOptions mOptions;

	private Representer mRepresenter;

	/**
	 * Create a JyamlWriter for ApplicationProperties
	 *
	 * @param fileName
	 */
	public PropertiesWriter(String fileName){
		log.trace("new PropertiesWriter()...");
		mFileName = fileName;
		mAppend = false;
		mOptions = buildDumperOptions();
		mRepresenter = new Representer();
		mRepresenter.addClassTag(Properties.class, new Tag(TAG));
	}

	private DumperOptions buildDumperOptions(){
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		return options;
	}

	public synchronized void persist(Map<String, Object> propertiesData) throws FileNotFoundException {
		log.debug("persist()...");
		persist(mFileName, propertiesData, mAppend, mRepresenter, mOptions);
	}
}
