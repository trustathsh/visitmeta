package de.fhhannover.inform.trust.visitmeta.dataservice;

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
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

import de.fhhannover.inform.trust.visitmeta.util.PropertiesReaderWriter;

public class ConfigTest {

	@Before
	public void setUp() {
		
	}
	
	@Test(expected=IOException.class)
	public void testFileNotFound() throws IOException {
		File f = new File("src/test/resources/test.properties");
		if(f.exists())
			f.delete();
		PropertiesReaderWriter config = new PropertiesReaderWriter("src/test/resources/test.properties", false);
	}
	
	@Test
	public void testConfig() throws IOException {
		PropertiesReaderWriter config = new PropertiesReaderWriter("src/test/resources/test.properties", true);
		config.storeProperty("test.def.456", "kablamo");
		config = new PropertiesReaderWriter("src/test/resources/test.properties", false);
		assertEquals("kablamo", config.getProperty("test.def.456"));
	}
}
