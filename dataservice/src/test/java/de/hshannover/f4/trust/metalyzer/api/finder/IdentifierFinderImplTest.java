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
package de.hshannover.f4.trust.metalyzer.api.finder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.exception.NegativeTimestampException;
import de.hshannover.f4.trust.metalyzer.api.exception.NoFilterException;
import de.hshannover.f4.trust.metalyzer.api.exception.TimestampException;
import de.hshannover.f4.trust.metalyzer.api.finder.IdentifierFinderImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.DeltaImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

public class IdentifierFinderImplTest {
	private IdentifierFinderImpl finder;
	private Collection<Identifier> identifiers;
	private Collection<Identifier> expectedIdentifiers;
	
	private MetalyzerDelta<Identifier> delta;
	
	@Test
	public void testGetCurrentIdentifierWithoutFilters() {
		givenListOfCurrentIdentifiers();
		
		whenGetCurrentIdentifierIsCalledWithoutFilter();
		
		thenUnfilteredCurrentIdentifierListShouldHaveSize();
		//thenUnfilteredCurrentIdentifierListShouldBe(); Auskommentiert da Testen von Arrays zur Zeit nicht geht.
	}

	private void givenListOfCurrentIdentifiers() {
		IdentifierGraphImpl graph1 = new IdentifierGraphImpl(10);
		graph1.insert(new IdentifierImpl("device"));
		graph1.insert(new IdentifierImpl("device"));
		graph1.insert(new IdentifierImpl("ip-address"));
		IdentifierGraphImpl graph2 = new IdentifierGraphImpl(11);
		graph2.insert(new IdentifierImpl("ip-address"));
		graph2.insert(new IdentifierImpl("identity"));
		
		expectedIdentifiers.addAll(graph1.getIdentifiers());
		expectedIdentifiers.addAll(graph2.getIdentifiers());
		
		ArrayList<IdentifierGraph> graphs = new ArrayList<>();
		graphs.add(graph1);
		graphs.add(graph2);
		
		GraphService service = Mockito.mock(SimpleGraphService.class);
		
		Mockito.when(service.getCurrentGraph()).thenReturn(graphs);
		
		finder = new IdentifierFinderImpl(service);
	}

	private void whenGetCurrentIdentifierIsCalledWithoutFilter() {
		try {
			identifiers = finder.getCurrent();
		} catch (MetalyzerAPIException e) {
			fail("Exception was thrown. This should not happen! " + e.getMessage());
		}
	}

	private void thenUnfilteredCurrentIdentifierListShouldHaveSize() {
		assertEquals("Returns an unfiltered current Identifier list",expectedIdentifiers.size(), identifiers.size());
	}
	
//	private void thenUnfilteredCurrentIdentifierListShouldBe() {
//		assertArrayEquals(expectedIdentifiers, identifiers);
//	}
	
	@Test
	public void testGetCurrentIdentifierFilteredByDevice() {
		givenListOfCurrentIdentifiers();
		
		whenGetCurrentIdentifierIsCalledWithFilter(StandardIdentifierType.DEVICE);
		
		thenFilteredCurrentIdentifierListShouldHaveSize();
	}
	
	private void whenGetCurrentIdentifierIsCalledWithFilter(StandardIdentifierType filter) {
		try {
			identifiers = finder.getCurrent(filter);
		} catch (MetalyzerAPIException e) {
			fail("Exception was thrown. This should not happen! " + e.getMessage());
		}
	}
	
	private void thenFilteredCurrentIdentifierListShouldHaveSize() {
		assertEquals("Tests if the returned List has the right size() filtered by Device",2, identifiers.size());
	}
	
	@Test
	public void testGetIdentifiersFilteredByIpaddressAtTimestamp() throws MetalyzerAPIException {
		givenListOfIdentifiersAtTimestampt();
		
		whenGetIdentifierIsCalled();
		
		thenIdentifierListShouldHaveSize();
	}

	private void givenListOfIdentifiersAtTimestampt() {
		IdentifierGraphImpl graph1 = new IdentifierGraphImpl(10);
		graph1.insert(new IdentifierImpl("device"));
		graph1.insert(new IdentifierImpl("device"));
		graph1.insert(new IdentifierImpl("ip-address"));
		IdentifierGraphImpl graph2 = new IdentifierGraphImpl(10);
		graph2.insert(new IdentifierImpl("ip-address"));
		graph2.insert(new IdentifierImpl("identity"));
		
		expectedIdentifiers.addAll(graph1.getIdentifiers());
		expectedIdentifiers.addAll(graph2.getIdentifiers());
		
		ArrayList<IdentifierGraph> graphs = new ArrayList<>();
		graphs.add(graph1);
		graphs.add(graph2);
		
		GraphService service = Mockito.mock(SimpleGraphService.class);
		
		Mockito.when(service.getGraphAt(10)).thenReturn(graphs);
		
		finder = new IdentifierFinderImpl(service);
	}

	private void whenGetIdentifierIsCalled() throws MetalyzerAPIException {
		identifiers = finder.get(StandardIdentifierType.IP_ADDRESS, 10);
	}

	private void thenIdentifierListShouldHaveSize() {
		assertEquals("Tests if the returned List has the right size() filtered by IP_Address", 2, identifiers.size());
	}
	
	@Test
	public void testGetIdentifierDeltaFiltered() throws MetalyzerAPIException {
		givenVisITMetaDelta();
		
		whenGetIdentifierDeltaIsCalled();
		
		thenMetalyzerDeltaShouldBe();
	}

	private void givenVisITMetaDelta() {
		IdentifierGraphImpl updates10 = new IdentifierGraphImpl(10);
		updates10.insert(new IdentifierImpl("device"));
		updates10.insert(new IdentifierImpl("device"));
		updates10.insert(new IdentifierImpl("ip-address"));
		updates10.insert(new IdentifierImpl("mac-address"));
		IdentifierGraphImpl updates11 = new IdentifierGraphImpl(11);
		updates11.insert(new IdentifierImpl("device"));
		updates11.insert(new IdentifierImpl("device"));
		updates11.insert(new IdentifierImpl("ip-address"));
		
		IdentifierGraphImpl deletes10 = new IdentifierGraphImpl(10);
		deletes10.insert(new IdentifierImpl("ip-address"));
		deletes10.insert(new IdentifierImpl("identity"));
		IdentifierGraphImpl deletes11 = new IdentifierGraphImpl(11);
		deletes11.insert(new IdentifierImpl("ip_address"));
		deletes11.insert(new IdentifierImpl("identity"));
		deletes11.insert(new IdentifierImpl("mac-address"));
		deletes11.insert(new IdentifierImpl("mac-address"));
		
		ArrayList<IdentifierGraph> updates = new ArrayList<>();
		updates.add(updates10);
		updates.add(updates11);
		
		ArrayList<IdentifierGraph> deletes = new ArrayList<>();
		deletes.add(deletes10);
		deletes.add(deletes11);
		
		DeltaImpl delta = new DeltaImpl(deletes, updates);
		
		GraphService service = Mockito.mock(SimpleGraphService.class);
		
		Mockito.when(service.getDelta(10, 11)).thenReturn(delta);
		
		SortedMap<Long, Long> changes = new TreeMap<>();
		changes.put(10L, 6L);
		changes.put(11L, 6L);
		
		Mockito.when(service.getChangesMap()).thenReturn(changes);
		
		finder = new IdentifierFinderImpl(service);
	}

	private void whenGetIdentifierDeltaIsCalled() throws MetalyzerAPIException {
		delta = finder.get(StandardIdentifierType.DEVICE, 10, 11);
	}

	private void thenMetalyzerDeltaShouldBe() {
		
		assertEquals("Tests the Delta.getUpdates",4, delta.getUpdates().size());
		assertEquals("Tests the Delta.getDeletes",0, delta.getDeletes().size());
	}

	@Test(expected = NoFilterException.class) 
	public void testGetCurrentIdentifierWithFilterNull() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.getCurrent(null);
	}
	
	private void givenEmptyMockService() {
		GraphService service = Mockito.mock(SimpleGraphService.class);
		finder = new IdentifierFinderImpl(service);
	}
	
	@Test(expected = NoFilterException.class)  
	public void testGetIdentifierWithFilterNull() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get(null,10);
	}
	
	@Test(expected = NegativeTimestampException.class)  
	public void testGetIdentifierWithTimestampNegative() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get(StandardIdentifierType.ALL,-10);
	}
	
	@Test(expected = NegativeTimestampException.class)  
	public void testGetIdentifierDeltaWithNegativeTimestamp() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get(StandardIdentifierType.ALL, -10, 20);
	}
	
	@Test(expected = TimestampException.class)  
	public void testGetIdentifierDeltaWithImpossibleDelta() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get(StandardIdentifierType.ALL, 15, 10);
	}
	
	@Test(expected = NoFilterException.class)  
	public void testGetIdentifierDeltaWithNullFilter() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get(null, 10, 15);
	}
	
	@Before
	public void initTest() {
		expectedIdentifiers = new ArrayList<Identifier>();
	}

}
