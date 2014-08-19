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
package de.hshannover.f4.trust.metalyzer.api.finder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.exception.NegativeTimestampException;
import de.hshannover.f4.trust.metalyzer.api.exception.TimestampException;
import de.hshannover.f4.trust.metalyzer.api.finder.MetadataFinderImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.DeltaImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class MetadataFinderImplTest {
	private MetadataFinderImpl finder;
	private Collection<Metadata> metaList = new ArrayList<>();
	
	private void givenEmptyMockService() {
		GraphService service = Mockito.mock(SimpleGraphService.class);
		
		DeltaImpl delta = new DeltaImpl(new ArrayList<IdentifierGraph>(), new ArrayList<IdentifierGraph>());
		
		Mockito.when(service.getDelta(Mockito.anyLong(), Mockito.anyLong())).thenReturn(delta);
		
		finder = new MetadataFinderImpl(service);
	}
	
	@Test(expected = NegativeTimestampException.class)  
	public void testGetMetadataWithTimestampNegative() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get("filter",-10);
	}
	
	@Test(expected = NegativeTimestampException.class)  
	public void testGetMetadataDeltaWithNegativeTimestamp() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get("filter", -10, 20);
	}
	
	@Test(expected = TimestampException.class)  
	public void testGetMetadataDeltaWithImpossibleDelta() throws MetalyzerAPIException {
		givenEmptyMockService();
		
		finder.get("filer", 15, 10);
	}
	
	@Test
	@Ignore("Deactivated because the MetalyzerGraphFilter Class is Deprecated")
	public void testGetMetadataFilteredByTimestampAndFilter() throws MetalyzerAPIException {
		givenListOfMetadata();
		
		whenGetMetadataIsCalled();
		
		thenMetadataFilteredListShouldHaveSize();
	}

	private void givenListOfMetadata() {
		ArrayList<IdentifierGraph> graphs = new ArrayList<>();
		graphs.add(getGraphAt10());
		
		GraphService service = Mockito.mock(SimpleGraphService.class);
		
		// TODO Must be refactored. GraphFilter Class is deprecated
		//Mockito.when(service.getGraphAt(Mockito.anyLong(), Mockito.any(MetalyzerGraphFilter.class))).thenReturn(graphs);
		
		finder = new MetadataFinderImpl(service);
	}

	private void whenGetMetadataIsCalled() throws MetalyzerAPIException {
		metaList = finder.get("role", 10);
	}

	private void thenMetadataFilteredListShouldHaveSize() {
		assertEquals("Compares the sizes of Metadata-Lists filtered by timestamp and filter",1,metaList.size());
	}
	
	@Test
	public void testGetCurrentMetadata() throws MetalyzerAPIException {
		givenCurrentListOfMetadata();
		
		whenGetCurrentMetadataIsCalled();
		
		thenCurrentMetadataListShouldHaveSize();
	}
	
	private void givenCurrentListOfMetadata() {
		ArrayList<IdentifierGraph> graphs = new ArrayList<>();
		graphs.add(getGraphAt10());
		graphs.add(getGraphAt11());
		
		GraphService service = Mockito.mock(SimpleGraphService.class);
		
		Mockito.when(service.getCurrentGraph()).thenReturn(graphs);
		
		finder = new MetadataFinderImpl(service);
	}

	private void whenGetCurrentMetadataIsCalled() throws MetalyzerAPIException {
		metaList = finder.getCurrent();
		
	}

	private void thenCurrentMetadataListShouldHaveSize() {
		assertEquals("Compares the sizes of Metadata-Lists",4,metaList.size());
	}

	private IdentifierGraphImpl getGraphAt10() {
		IdentifierImpl identAccessRequest = new IdentifierImpl("access_request");
		
		IdentifierImpl identDevice = new IdentifierImpl("device");
		
		LinkImpl link = new LinkImpl(identAccessRequest, identDevice);
		link.addMetadata(new MetadataImpl("role",true,10));
		
		identAccessRequest.addLink(link);
		identDevice.addLink(link);
		
		IdentifierGraphImpl graph10 = new IdentifierGraphImpl(10);
		graph10.insert(identAccessRequest);
		graph10.insert(identDevice);
		
		return graph10;
	}
	
	private IdentifierGraphImpl getGraphAt11() {
		IdentifierImpl identAccessRequest = new IdentifierImpl("access_request");
		identAccessRequest.addMetadata(new MetadataImpl("capability",true,11));
		
		IdentifierImpl identIdentity = new IdentifierImpl("identity");
		
		LinkImpl link = new LinkImpl(identAccessRequest, identIdentity);
		link.addMetadata(new MetadataImpl("authenticated-as", true, 11));
		link.addMetadata(new MetadataImpl("role",true,11));
		
		identAccessRequest.addLink(link);
		identIdentity.addLink(link);
		
		IdentifierGraphImpl graph11 = new IdentifierGraphImpl(11);
		graph11.insert(identAccessRequest);
		graph11.insert(identIdentity);
		
		return graph11;
	}
}
