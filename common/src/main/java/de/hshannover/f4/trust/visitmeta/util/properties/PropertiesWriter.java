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
package de.hshannover.f4.trust.visitmeta.util.properties;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import de.hshannover.f4.trust.visitmeta.util.yaml.YamlWriter;

public class PropertiesWriter extends YamlWriter {

	private static final Logger log = Logger.getLogger(PropertiesWriter.class);

	public static final String TAG = "!Properties";

	private String mFileName;

	private DumperOptions mOptions;

	private Representer mRepresenter;

	/**
	 * Create a JyamlWriter for application properties.
	 * @param fileName The file name or the file path to the yml-file.
	 */
	public PropertiesWriter(String fileName){
		log.trace("new PropertiesWriter()...");
		mFileName = fileName;
		mOptions = buildDumperOptions();
		mRepresenter = new Representer();
		mRepresenter.addClassTag(Properties.class, new Tag(TAG));
	}

	private DumperOptions buildDumperOptions(){
		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		return options;
	}

	/**
	 * Save the Map<String, Object> to the yml-file.
	 * @param propertiesData The Object to be stored.
	 * @throws IOException
	 */
	public void save(Map<String, Object> propertiesData) throws IOException {
		log.trace("save()...");
		persist(mFileName, propertiesData, mRepresenter, mOptions);
	}
}
