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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.TestGraphBuilder;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerDelta;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.api.exception.TimestampException;
import de.hshannover.f4.trust.metalyzer.api.finder.GeneralFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.DeltaImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class GeneralFinderImplTest {
	
	private GeneralFinderImpl finder;
	private GraphService graphServ;
	
	private ArrayList<IdentifierGraph> graphs;
	private IdentifierImpl[] idents = new IdentifierImpl[5];
	private MetadataImpl[] metas = new MetadataImpl[5];
	
	IdentifierGraphImpl internalGraph = new IdentifierGraphImpl(10);
	
	private ArrayList<Identifier> expectedIdents;
	private ArrayList<Metadata> expectedMetadata;
	private MetalyzerDelta<Identifier> expectedIdentDelta;
	private MetalyzerDelta<Metadata> expectedMetaDelta;
	
	private ArrayList<Identifier> actualIdents;
	private ArrayList<Metadata> actualMetadata;
	private MetalyzerDelta<Identifier> actualIdentDelta;
	private MetalyzerDelta<Metadata> actualMetaDelta;
	
	@Test( expected = RuntimeException.class )
	public void testCreateGeneralFinderImplWithNull() {
		finder = new GeneralFinderImpl(null);
	}
	
	@Test( expected = RuntimeException.class )
	public void testCallFilterAsIdentifiersWithNull() {
		finder = new GeneralFinderImpl(Mockito.mock(GraphService.class));
		
		finder.filterAsIdentifiers(null);
	}
	
	@Test( expected = RuntimeException.class )
	public void testCallFilterAsMetadataWithNull() {
		finder = new GeneralFinderImpl(Mockito.mock(GraphService.class));
		
		finder.filterAsMetadata(null);
	}
	
	@Test( expected = RuntimeException.class )
	public void testCallFilterAsIdentifierDeltaWithNull() {
		finder = new GeneralFinderImpl(Mockito.mock(GraphService.class));
		
		finder.filterAsIdentifierDelta(null);
	}
	
	@Test( expected = RuntimeException.class )
	public void testCallFilterAsMetadataDeltaWithNull() {
		finder = new GeneralFinderImpl(Mockito.mock(GraphService.class));
		
		finder.filterAsMetadataDelta(null);
	}
	
	/**
	 * Description:
	 * Uses a Filter without any identifiers or metadata.
	 * Just checks if a not filtered Result is returned.
	 */
	@Test
	public void testFinderWithEmptyFilter() {
		givenStandardFilterFinder();
		
		whenEmpyFilterMethodCallHappened();
		
		thenFilterFinderShouldReturnEmptyArrayList();
	}

	private void givenStandardFilterFinder() {
		buildCurrentGraph();
		
		Mockito.when(graphServ.getCurrentGraph()).thenReturn(graphs);
		
		finder = new GeneralFinderImpl(graphServ);
	}

	private void whenEmpyFilterMethodCallHappened() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.getMetalyzerTime().atCurrent();
		
		expectedIdents = new ArrayList<>();
		
		expectedIdents.add(idents[0]);
		expectedIdents.add(idents[1]);
		expectedIdents.add(idents[2]);
		expectedIdents.add(idents[3]);
		
		actualIdents = finder.filterAsIdentifiers(filter);
	}

	private void thenFilterFinderShouldReturnEmptyArrayList() {
		assertThat("Should return an full ( unfiltered ) ArrayList!",actualIdents, is(expectedIdents));
	}
	
	/**
	 * Description:
	 * Uses a filter just with an device at current. Returns the filtered graph as an identifier list.
	 * @throws MetalyzerAPIException
	 */
	@Test
	public void testFinderWithDeviceFilterAtCurrent() {
		givenFilterFinderWithIdentifiers();
		
		whenFilterMethodCalledWithIdentifierFilter();
		
		thenFilterFinderShouldReturnFilteredIdentifierList();
	}

	private void givenFilterFinderWithIdentifiers() {
		buildCurrentGraph();
		
		Mockito.when(graphServ.getCurrentGraph()).thenReturn(graphs);
		
		finder = new GeneralFinderImpl(graphServ);
	}
	
	private void buildCurrentGraph() {
		graphServ = Mockito.mock(GraphService.class);
		
		IdentifierGraphImpl graph = new IdentifierGraphImpl(10);
		
		getGraphData(graph);
		getGraphData(internalGraph);
 		
		graphs = new ArrayList<>();
		graphs.add(graph);
	}
	
	private void getGraphData(IdentifierGraphImpl graph) {
		idents[0] = new IdentifierImpl("device");
		graph.insert(idents[0]);
		
		metas[0] = new MetadataImpl("event", true, 10);
		idents[0].addMetadata(metas[0]);
		
		idents[1] = new IdentifierImpl("identity");
		graph.insert(idents[1]);
		
		metas[1] = new MetadataImpl("location", true, 10);
		idents[1].addMetadata(metas[1]);
		
		idents[2] = new IdentifierImpl("device");
		graph.insert(idents[2]);
		
		idents[3] = new IdentifierImpl("ip-address");
		graph.insert(idents[3]);
		
		metas[2] = new MetadataImpl("event", true, 10);
		idents[3].addMetadata(metas[2]);
		
		metas[3] = new MetadataImpl("device-ip",true,10);
		LinkImpl l_d_i = graph.connect(idents[2], idents[3]);
		
		l_d_i.addMetadata(metas[3]);
	}

	private void whenFilterMethodCalledWithIdentifierFilter() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or(StandardIdentifierType.DEVICE).getMetalyzerTime().atCurrent();
		
		expectedIdents = new ArrayList<>();
		
		expectedIdents.add(idents[0]);
		expectedIdents.add(idents[2]);
		
		actualIdents = finder.filterAsIdentifiers(filter);
	}

	private void thenFilterFinderShouldReturnFilteredIdentifierList() {
		assertThat("Should return a filtered list!",actualIdents,is(expectedIdents));
		Mockito.verify(graphServ).getCurrentGraph();
	}
	
	/**
	 * Description:
	 * Uses a filter with a device and a identity at current. Returns the filtered graph as an identifier list.
	 * @throws MetalyzerAPIException
	 */
	@Test
	public void testFinderWithMultipleIdentifierFilterAtCurrent() {
		givenFilterFinderWithIdentifiers();
		
		whenFilterMethodCalledWithMultipleIdentifierFilter();
		
		thenFilterFinderShouldReturnFilteredIdentifierList();
	}

	private void whenFilterMethodCalledWithMultipleIdentifierFilter() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or(StandardIdentifierType.DEVICE).or(StandardIdentifierType.IDENTITY).getMetalyzerTime().atCurrent();
		
		expectedIdents = new ArrayList<>();
		
		expectedIdents.add(idents[0]);
		expectedIdents.add(idents[1]);
		expectedIdents.add(idents[2]);
		
		actualIdents = finder.filterAsIdentifiers(filter);
	}
	
	@Test
	public void testFinderWithMetdataFilterAtCurrentAsIdentifier() {
		givenFilterFinderWithFilteredIdentifiers();
		
		whenFilterMethodCalledWithMetadataFilter();
		
		thenFilteredGetCurrentGraphShouldBeCalled();
	}

	private void givenFilterFinderWithFilteredIdentifiers() {
		GraphService graphServ = Mockito.mock(GraphService.class);
		
		IdentifierGraphImpl graph = new IdentifierGraphImpl(10);
		
		getActualData(graph);
		getExpectedData(internalGraph);
 		
		graphs = new ArrayList<>();
		graphs.add(graph);
		
		Mockito.when(graphServ.getCurrentGraph()).thenReturn(graphs);
		
		finder = new GeneralFinderImpl(graphServ);
		
	}

	private void getActualData(IdentifierGraphImpl graph) {
		IdentifierImpl i1 = new IdentifierImpl("device");
		graph.insert(i1);
		
		MetadataImpl m1 = new MetadataImpl("event", true, 10);
		i1.addMetadata(m1);
		
		IdentifierImpl i2 = new IdentifierImpl("identity");
		graph.insert(i2);
		
		MetadataImpl m2 = new MetadataImpl("role", true, 10);
		i2.addMetadata(m2);
		
		IdentifierImpl i3 = new IdentifierImpl("device");
		graph.insert(i3);
		
		IdentifierImpl i4 = new IdentifierImpl("ip-address");
		graph.insert(i4);
		
		MetadataImpl m3 = new MetadataImpl("event", true, 10);
		i4.addMetadata(m3);
		
		MetadataImpl m4 = new MetadataImpl("device-ip",true,10);
		LinkImpl l_d_i = graph.connect(i3, i4);
		
		l_d_i.addMetadata(m4);
	}

	private void getExpectedData(IdentifierGraphImpl graph) {
		idents[0] = new IdentifierImpl("device");
		graph.insert(idents[0]);
		
		idents[1] = new IdentifierImpl("identity");
		graph.insert(idents[1]);
		
		metas[1] = new MetadataImpl("role", true, 10); 
		idents[1].addMetadata(metas[1]);
		
		idents[2] = new IdentifierImpl("device");
		graph.insert(idents[2]);
		
		idents[3] = new IdentifierImpl("ip-address");
		graph.insert(idents[3]);
		
		metas[3] = new MetadataImpl("device-ip",true,10);
		LinkImpl l_d_i = graph.connect(idents[2], idents[3]);
		
		l_d_i.addMetadata(metas[3]);
	}

	private void whenFilterMethodCalledWithMetadataFilter() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or("role").or("device-ip").getMetalyzerTime().atCurrent();
		
		expectedIdents = new ArrayList<>();
		
		expectedIdents.add(idents[0]);
		expectedIdents.add(idents[1]);
		expectedIdents.add(idents[2]);
		expectedIdents.add(idents[3]);
		
		actualIdents = finder.filterAsIdentifiers(filter);
	}

	private void thenFilteredGetCurrentGraphShouldBeCalled() {
		assertThat("Should return an identifier list with filtered Metadata",actualIdents,is(expectedIdents));
	}
	
	@Test
	public void testFinderWithIdentifierFilterAt() {
		givenFilterFinderWithIdentifiers();
		
		whenFilterMethodCalledWithIdentifierFilterAt();
		
		thenFilteredGetGraphAtShouldBeCalled();
	}

	private void whenFilterMethodCalledWithIdentifierFilterAt() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or(StandardIdentifierType.DEVICE).or(StandardIdentifierType.ACCESS_REQUEST).getMetalyzerTime().at(10);
		
		finder.filterAsIdentifiers(filter);
	}

	private void thenFilteredGetGraphAtShouldBeCalled() {
		Mockito.verify(graphServ).getGraphAt(10);
	}
	
	/**
	 * Description:
	 * Filters Metadata ( ip-mac, role ) in a Graph at 10. Returns a identifier List.
	 * @throws MetalyzerAPIException
	 */
	@Ignore("MetalyzerGraphFilter should not be used anymore! Result should be tested instead of verfiy!")
	@Test
	public void testFinderWithMetadataFilterAt() {
		givenFilterFinderWithIdentifierGraph();
		
		whenFilterMethodCalledWithMetadataFilterAt();
		
		thenFilteredGetGraphAtWithFilterShouldBeCalled();
	}

	private void givenFilterFinderWithIdentifierGraph() {
		graphServ = new TestGraphBuilder().getGraphServiceAt10();
		
		finder = new GeneralFinderImpl(graphServ);
	}

	private void whenFilterMethodCalledWithMetadataFilterAt() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or("ip-mac").or("role").getMetalyzerTime().at(10);
		
		actualIdents = finder.filterAsIdentifiers(filter);
	}

	private void thenFilteredGetGraphAtWithFilterShouldBeCalled() {
		Mockito.verify(graphServ).getGraphAt(10);
		
		for(Identifier ident : actualIdents) {
			for(Metadata m : ident.getMetadata()) {
				System.out.println(m);
			}
		}
		
		assertThat(actualIdents,is(expectedIdents));
	}
	
	@Ignore("MetalyzerGraphFilter should not be used anymore! Result should be tested instead of verfiy!")
	@Test
	public void testFilterAsMetadataWithEmptyFilterAtCurrent() {
		givenFilterFinderWithMetadata();
		
		whenFilterAsMetadataWithEmptyFilterIsCalled();
		
		thenFinderShouldReturnUnfilteredMetadataListAtCurrent();
	}

	private void givenFilterFinderWithMetadata() {
		buildCurrentGraph();
		
		Mockito.when(graphServ.getCurrentGraph()).thenReturn(graphs);
		
		finder = new GeneralFinderImpl(graphServ);
	}

	private void whenFilterAsMetadataWithEmptyFilterIsCalled() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.getMetalyzerTime().atCurrent();
		
		expectedMetadata = new ArrayList<>();
		
		// Order is important because the link is filtered before the second identifier
		expectedMetadata.add(metas[0]);
		expectedMetadata.add(metas[1]);
		expectedMetadata.add(metas[3]);
		expectedMetadata.add(metas[2]);
		
		actualMetadata = finder.filterAsMetadata(filter);
	}

	private void thenFinderShouldReturnUnfilteredMetadataListAtCurrent() {
		assertThat("Should return an unfiltered Metadata list",actualMetadata,is(expectedMetadata));
	}
	
	@Ignore("MetalyzerGraphFilter should not be used anymore! Result should be tested instead of verfiy!")
	@Test
	public void testFilterAsMetadataWithMultipleIdentFilterAtCurrent() {
		givenFilterFinderWithMetadata();
		
		whenFilterAsMetadataWithMultipleIdentifierFilterIsCalled();
		
		thenFinderShouldReturnFilteredMetadataListAtCurrent();
	}

	private void whenFilterAsMetadataWithMultipleIdentifierFilterIsCalled() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or(StandardIdentifierType.DEVICE).or(StandardIdentifierType.IDENTITY).getMetalyzerTime().atCurrent();
		
		expectedMetadata = new ArrayList<>();
		
		// Order is important because the link is filtered before the second identifier
		expectedMetadata.add(metas[0]);
		expectedMetadata.add(metas[1]);
		expectedMetadata.add(metas[3]);
		
		actualMetadata = finder.filterAsMetadata(filter);
	}

	private void thenFinderShouldReturnFilteredMetadataListAtCurrent() {
		assertThat("Should return an filtered Metadata list",actualMetadata,is(expectedMetadata));
	}
	
	@Ignore("MetalyzerGraphFilter should not be used anymore! Result should be tested instead of verfiy!")
	@Test
	public void testAsMetadataWithMultipleFilterAtCurrent() {
		givenFilterFinderWithMetadataFilter();
		
		whenAsMetadataWithMultipleFilterIsCalled();
		
		thenVerifyFinderCalledCurrentGraphWithFilter();
	}

	private void givenFilterFinderWithMetadataFilter() {
		buildCurrentGraph();
		
		//TODO need to be fixed.
		
		finder = new GeneralFinderImpl(graphServ);
	}

	private void whenAsMetadataWithMultipleFilterIsCalled() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or("event").or("device-ip").getMetalyzerTime().atCurrent();
		
		expectedMetadata = new ArrayList<>();
		
		// Order is important because the link is filtered before the second identifier
		expectedMetadata.add(metas[0]);
		expectedMetadata.add(metas[1]);
		expectedMetadata.add(metas[2]);
		
		actualMetadata = finder.filterAsMetadata(filter);
	}
	
	private void thenVerifyFinderCalledCurrentGraphWithFilter() {
		Mockito.verify(graphServ).getCurrentGraph(null);
	}

	@Test
	public void testFilterAsIdentifierDeltaWhereGetDeltaIsCalled() {
		givenFinderWithDelta();
		
		whenFilterAsMetadataDeltaIsCalled();
		
		thenGraphServGetDeltaShouldBeCalled();
	}

	private void givenFinderWithDelta() {
		graphServ = Mockito.mock(GraphService.class);
		
		ArrayList<IdentifierGraph> deletes = new ArrayList<>();
		
		IdentifierGraphImpl delete = new IdentifierGraphImpl(12);
		
		idents[0] = new IdentifierImpl("ip-address");
		delete.insert(idents[0]);
		
		metas[0] = new MetadataImpl("event", true, 10);
		idents[0].addMetadata(metas[0]);
		
		idents[1] = new IdentifierImpl("identity");
		delete.insert(idents[1]);
		
		metas[1] = new MetadataImpl("location", true, 10);
		idents[1].addMetadata(metas[1]);
		
		deletes.add(delete);
		
		ArrayList<IdentifierGraph> updates = new ArrayList<>();
		
		IdentifierGraphImpl update = new IdentifierGraphImpl(10);
		
		idents[2] = new IdentifierImpl("device");
		update.insert(idents[2]);
		
		metas[2] = new MetadataImpl("event", true, 10);
		idents[2].addMetadata(metas[2]);
		
		idents[3] = new IdentifierImpl("identity");
		update.insert(idents[3]);
		
		metas[3] = new MetadataImpl("capability", true, 10);
		idents[3].addMetadata(metas[3]);
		
		updates.add(update);
		
		DeltaImpl delta = new DeltaImpl(deletes, updates);
		
		Mockito.when(graphServ.getDelta(10, 15)).thenReturn(delta);
		
		SortedMap<Long,Long> changes = new TreeMap<>();
		changes.put(10L, 4L);
		changes.put(15L, 0L);
		
		Mockito.when(graphServ.getChangesMap()).thenReturn(changes);
		
		finder = new GeneralFinderImpl(graphServ);
	}

	private void whenFilterAsMetadataDeltaIsCalled() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.getMetalyzerTime().atDelta(10, 15);
		
		finder.filterAsIdentifierDelta(filter);
	}

	private void thenGraphServGetDeltaShouldBeCalled() {
		Mockito.verify(graphServ).getDelta(10, 15);
	}
	
	@Test
	public void testAsIdentifierDeltaWithEmptyFilter() {
		givenFinderWithDelta();
		
		whenAsIdentifierDeltaIsCalledWithEmptyFilter();
		
		thenFullMetalyzerDeltaShouldBeReturned();
	}

	private void whenAsIdentifierDeltaIsCalledWithEmptyFilter() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.getMetalyzerTime().atDelta(10, 15);
		
		ArrayList<Identifier> deletes = new ArrayList<>();
		
		deletes.add(idents[0]);
		deletes.add(idents[1]);
		
		ArrayList<Identifier> updates = new ArrayList<>();
		
		updates.add(idents[2]);
		updates.add(idents[3]);
		
		expectedIdentDelta = new MetalyzerDelta<Identifier>(deletes, updates, new ArrayList<Identifier>());
		
		actualIdentDelta = finder.filterAsIdentifierDelta(filter);
	}

	private void thenFullMetalyzerDeltaShouldBeReturned() {
		assertThat(actualIdentDelta.getDeletes(),is(expectedIdentDelta.getDeletes()));
		assertThat(actualIdentDelta.getUpdates(),is(expectedIdentDelta.getUpdates()));
	}
	
	@Test
	public void testAsMetadataDeltaWithMultipleFilter() {
		givenFinderWithDelta();
		
		whenAsMetadataDeltaWithFilterIsCalled();
		
		thenFinderShouldReturnFilteredMetadataDelta();
	}

	private void whenAsMetadataDeltaWithFilterIsCalled() {
		MetalyzerFilterImpl filter = new MetalyzerFilterImpl(finder);
		
		filter.or("event").or("location").getMetalyzerTime().atDelta(10, 15);
		
		ArrayList<Metadata> deletes = new ArrayList<>();
		
		deletes.add(metas[0]);
		deletes.add(metas[1]);
		
		ArrayList<Metadata> updates = new ArrayList<>();
		
		updates.add(metas[2]);
		
		expectedMetaDelta = new MetalyzerDelta<>(deletes, updates, new ArrayList<Metadata>());
		
		actualMetaDelta = finder.filterAsMetadataDelta(filter);
	}

	private void thenFinderShouldReturnFilteredMetadataDelta() {
		assertThat(actualMetaDelta.getDeletes(),is(expectedMetaDelta.getDeletes()));
		assertThat(actualMetaDelta.getUpdates(),is(expectedMetaDelta.getUpdates()));
	}
}
