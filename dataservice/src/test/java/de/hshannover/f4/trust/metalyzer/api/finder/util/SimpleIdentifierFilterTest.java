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
package de.hshannover.f4.trust.metalyzer.api.finder.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import de.hshannover.f4.trust.metalyzer.api.finder.util.IdentifierFilter;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class SimpleIdentifierFilterTest {
	
	private IdentifierFilter filter;
	private ArrayList<Identifier> expected = new ArrayList<>();
	private ArrayList<Identifier> actual = new ArrayList<>();
	
	@Test
	public void testIfSimpleIdentifierFilters() {
		givenNewSimpleIdentifierFilter();
		
		whenFilterIdentifierIsCalled();
		
		thenFilterShouldNotFilterAnyIdentifier();
	}

	private void givenNewSimpleIdentifierFilter() {
		HashSet<String> identifiers = new HashSet<>();
		
		identifiers.add("device");
		
		filter = new IdentifierFilter(identifiers);
	}

	private void whenFilterIdentifierIsCalled() {
		
		Identifier i1 = new IdentifierImpl("device");
		Identifier i2 = new IdentifierImpl("device");
		
		expected.add(i1);
		expected.add(i2);
		
		ArrayList<Identifier> toBeFiltered = new ArrayList<>();
		
		toBeFiltered.add(i1);
		toBeFiltered.add(new IdentifierImpl("identity"));
		toBeFiltered.add(i2);
		toBeFiltered.add(new IdentifierImpl("ip-address"));
		
		filter.filter(toBeFiltered);
		
		actual = (ArrayList<Identifier>) filter.getList();
	}

	private void thenFilterShouldNotFilterAnyIdentifier() {
		assertThat(actual, is(expected));
		
	}
}
