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
/**
 * Project: Metalyzer 
 * Author: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer.api.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;

public class MetalyzerDeltaTest {
	private ArrayList<IdentifierImpl> updates;
	private ArrayList<IdentifierImpl> deletes;
	private ArrayList<IdentifierImpl> availables;
	private ArrayList<IdentifierImpl> test;	
	
	private MetalyzerDelta<IdentifierImpl> delta;
	
	@Before
	public void initTest() {
		updates = new ArrayList<>();
		deletes = new ArrayList<>();
		availables = new ArrayList<>();
		test = new ArrayList<>();	
	}
	
	@Test
	public void testMetalyzerDeltaAvailablesReturnedSize() {
		givenListOfIdentifiers();
		
		whenMetalyzerDeltaIsBuild();
		
		thenAvailablesListShouldBe();
	}
	
	private void givenListOfIdentifiers() {
		updates.add(new IdentifierImpl("test"));
		updates.add(new IdentifierImpl("newTest"));
		updates.add(new IdentifierImpl("AnotherTest"));
		
		availables.add(new IdentifierImpl("Was Here"));
		availables.add(new IdentifierImpl("Also Here"));
		
		test.addAll(updates);
		test.addAll(availables);
	}

	private void whenMetalyzerDeltaIsBuild() {
		delta = new MetalyzerDelta<IdentifierImpl>(deletes,updates,availables);
	}
	
	private void thenAvailablesListShouldBe() {
		
		assertEquals(test.size(), delta.getAvailables().size());
	}
}
