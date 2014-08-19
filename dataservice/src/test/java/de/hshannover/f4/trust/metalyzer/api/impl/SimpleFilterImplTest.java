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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.HashSet;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerTime;
import de.hshannover.f4.trust.metalyzer.api.exception.FilterException;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.finder.GeneralFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;

public class SimpleFilterImplTest {
	
	private MetalyzerFilterImpl filter;
	
	private HashSet<String> expected = new HashSet<>();
	
	private MetalyzerTime actualTime;

	@Test
	public void testFilterCanAddIdentifier() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenIdentifierIsAdded();
		
		thenFilterShouldHaveIdentifier();
	}

	private void givenNewFilter() {
		GeneralFinderImpl filterFinder = Mockito.mock(GeneralFinderImpl.class);
		
		filter = new MetalyzerFilterImpl(filterFinder);
	}
	
	private void whenIdentifierIsAdded() throws MetalyzerAPIException {
		filter.or(StandardIdentifierType.DEVICE);
		
		expected.add("device");
	}
	
	private void thenFilterShouldHaveIdentifier() {
		assertThat("Returns an simple added Device Identifier",filter.getIdentifiers(),is(expected));
	}
	
	@Test(expected = FilterException.class)
	public void testFilterAddIdentifierTypeALL() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenIdentifierTypeALLIsAdded();
	}

	private void whenIdentifierTypeALLIsAdded() throws MetalyzerAPIException {
		filter.or(StandardIdentifierType.ALL);
	}
	
	@Test
	public void testFilterAddServeralIdentifiers() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenIdentifiersAreAdded();
		
		thenFilterShouldHaveServeralIdentifiers();
	}

	private void whenIdentifiersAreAdded() throws MetalyzerAPIException {
		filter.or(StandardIdentifierType.IDENTITY).or(StandardIdentifierType.DEVICE);
		
		expected.add("identity");
		expected.add("device");
	}

	private void thenFilterShouldHaveServeralIdentifiers() {
		assertThat("Filter should return several Identifiers",filter.getIdentifiers(),is(expected));
	}
	
	@Test
	public void testFilterAddSingleMetadata() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenMetadataIsAdded();
		
		thenFilterShouldHaveSingleMetadata();
	}

	private void whenMetadataIsAdded() throws MetalyzerAPIException {
		filter.or("ip-mac");
		
		expected.add("ip-mac");
	}

	private void thenFilterShouldHaveSingleMetadata() {
		assertThat("Checks if the given Metadata is corretly rreturned!",filter.getMetadata(),is(expected));
	}
	
	@Test
	public void testFilterAddSeveralMetadata() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenMetadataAreAdded();
		
		thenFilterShouldHaveServeralMetadata();
	}

	private void whenMetadataAreAdded() throws MetalyzerAPIException {
		filter.or("ip-mac").or("role");
		
		expected.add("ip-mac");
		expected.add("role");
	}

	private void thenFilterShouldHaveServeralMetadata() {
		assertThat("Filter should return serveral Metadata added.",filter.getMetadata(), is(expected));
	}
	
	@Test
	public void testFilterWithSameIdentifier() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenIdentifierIsSeveralTimesAdded();
		
		thenFilterShouldHaveOneIdentifier();
	}
	
	private void whenIdentifierIsSeveralTimesAdded() throws MetalyzerAPIException {
		filter.or(StandardIdentifierType.DEVICE).or(StandardIdentifierType.DEVICE);
		
		expected.add("device");
	}

	private void thenFilterShouldHaveOneIdentifier() {
		assertThat("Filter should return only one Identifier",filter.getIdentifiers(),is(expected));
	}
	
	@Test
	public void testFilterWithSameMetadata() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenMetadataIsSeveralTimesAdded();
		
		thenFilterShouldHaveOneMetadata();
	}
	
	private void whenMetadataIsSeveralTimesAdded() throws MetalyzerAPIException {
		filter.or("ip-mac").or("ip-mac");
		
		expected.add("ip-mac");
	}

	private void thenFilterShouldHaveOneMetadata() {
		assertThat("Filter should only return one Metadata.",filter.getMetadata(),is(expected));
	}
	
	@Test
	public void testFilterWithIdentifierAndMetadata() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenIdentifierAndMetadataAreAdded();
		
		thenFilterShouldHaveIdentifierAndMetadata();
	}
	
	private void whenIdentifierAndMetadataAreAdded() throws MetalyzerAPIException {
		filter.or("ip-mac").or("role").or(StandardIdentifierType.DEVICE).or(StandardIdentifierType.IDENTITY);
		
		expected.add("ip-mac");
		expected.add("role");
		expected.add("device");
		expected.add("identity");
	}

	private void thenFilterShouldHaveIdentifierAndMetadata() {
		HashSet<String> actual = new HashSet<String>(filter.getIdentifiers());
		
		actual.addAll(filter.getMetadata());
		
		assertThat("Checks if alle Metadata were properly added.",actual,is(expected));
	}
	
	@Test
	public void testFilterWithNotIdentifiers() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenNotIdentifierAreAdded();
		
		thenFilterShouldHaveNotIdentifiers();
	}

	private void whenNotIdentifierAreAdded() throws MetalyzerAPIException {
		filter.not(StandardIdentifierType.DEVICE).not(StandardIdentifierType.IDENTITY);
		
		expected.add("device");
		expected.add("identity");
	}

	private void thenFilterShouldHaveNotIdentifiers() {
		assertThat("Filter should return all not Identifiers.",filter.getNotIdentifiers(),is(expected));
	}
	
	@Test
	@Ignore("Deactivated because the functionality is not implemented in the Finder class")
	public void testFilterWithNotMetadata() throws MetalyzerAPIException {
		givenNewFilter();
		
		whenNotMetadataAreAdded();
		
		thenFilterShouldHaveNotMetadata();
	}

	private void whenNotMetadataAreAdded() {
		//filter.not("ip-mac").not("role");
		
		expected.add("ip-mac");
		expected.add("role");
	}

	private void thenFilterShouldHaveNotMetadata() {
		assertThat("Filter should return all not Metadata.", filter.getNotMetadata(),is(expected));
	}
	
	@Test
	public void testIfFilterReturnedCurrentTimeObject() {
		givenNewFilter();
		
		whenFilterAtCurrentIsCalled();
		
		thenFilterShouldReturnCurrentTimeObject();
	}

	private void whenFilterAtCurrentIsCalled() {
		actualTime = filter.getMetalyzerTime();
	}

	private void thenFilterShouldReturnCurrentTimeObject() {
		assertThat(actualTime,instanceOf(MetalyzerTime.class));
	}
}
