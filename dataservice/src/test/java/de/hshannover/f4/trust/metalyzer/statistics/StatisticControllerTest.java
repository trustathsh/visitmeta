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


package de.hshannover.f4.trust.metalyzer.statistics;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.TestGraphBuilder;
import de.hshannover.f4.trust.metalyzer.api.IdentifierFinder;
import de.hshannover.f4.trust.metalyzer.api.MetadataFinder;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerAPI;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.statistic.Counter;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;
import de.hshannover.f4.trust.metalyzer.statistic.RFrequency;
import de.hshannover.f4.trust.metalyzer.statistic.RMean;
import de.hshannover.f4.trust.metalyzer.statistic.StatisticController;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;


/**
 * @author juriseewald
 *
 */
public class StatisticControllerTest {
	private TestGraphBuilder builder = new TestGraphBuilder();
	private IdentifierFinder identfinder = builder.getMockedIdentifierFinderAt10().getIdentifierFinder();
	private MetadataFinder metadataFinder = builder.getMockedMetadataFinderAt10().getMetadataFinder();
	private MetalyzerAPI mApi = Mockito.mock(MetalyzerAPI.class);
	private StatisticController sc = StatisticController.getInstance();
	private Collection<Metadata> metadataColl;
	private Collection<Identifier> identifierColl;
	
	public StatisticControllerTest(){
		Mockito.when(mApi.getIdentifierFinder()).thenReturn(identfinder);
		Mockito.when(mApi.getMetadataFinder()).thenReturn(metadataFinder);
		Mockito.when(sc.getAPI()).thenReturn(mApi);
	}
	
	@Test
	public void testEvaluateMeanOfLinks() {
		givenCollectionOfAllIdentifierAtTimestamp();
		thenMeanOfLinksOfIdentifiersReturns();
	}
	
	@Test
	public void testEvaluateMeanOfLinksWithEmptyList(){
		givenEmptyListOfIdentifier();
		thenMeanReturnsNull();
	}
	
	@Test
	public void testEvaluateFrequencyTypeIdentifierAtTimestamp(){
		givenCollectionOfAllIdentifierAtTimestamp();
		thenEvaluateFrequencyReturnsMapOfIdentifier();		
	}
	
	@Test
	public void testEvaluateFrequencyTypeDeviceAtTimestamp(){
		givenCollectionOfAllIdentifierAtTimestamp();
		thenEvaluateFrequencyReturnsMapOfDevices();
	}
	@Test
	public void testEvaluateFrequencyTypeMetadataAtTimestamp(){
		givenCollectionOfAllMetataAtTimestamp();
		thenEvaluateFrequencyReturnsMapOfMetada();
	}
	@Test
	public void testEvaluateFrequencyTypeRolesAtTimestamp(){
		givenCollectionOfAllMetataAtTimestamp();
		thenEvaluateFrequencyReturnsMapOfRoles();
	}
	
	
	@Test
	public void testEvaluateFrequencyTypeIdentifierAtCurrent(){
		givenCollectionOfAllIdentifierAtCurrent();
		thenEvaluateFrequencyReturnsMapOfIdentifierAtCurrent();		
	}
	
	@Test
	public void testEvaluateFrequencyTypeDeviceAtCurrent(){
		givenCollectionOfAllIdentifierAtCurrent();
		thenEvaluateFrequencyReturnsMapOfDevicesAtCurrent();
	}
	@Test
	public void testEvaluateFrequencyTypeMetadataAtCurrent(){
		givenCollectionOfAllMetadataAtCurrent();
		thenEvaluateFrequencyReturnsMapOfMetadaAtCurrent();
	}
	@Test
	public void testEvaluateFrequencyTypeRolesAtCurrent(){
		givenCollectionOfAllMetataAtTimestamp();
		thenEvaluateFrequencyReturnsMapOfRolesAtCurrent();
	}	
	
	private void givenCollectionOfAllMetataAtTimestamp(){
		metadataColl = metadataFinder.get(10);
	}	
	private void givenCollectionOfAllIdentifierAtTimestamp(){
		identifierColl = identfinder.get(10);
	}	
	private void givenCollectionOfRolesAtTimestamp(){
		metadataFinder.get("roles", 10);
	}
	private void givenCollectionOfDevicesAtTimestamp(){
		identfinder.get(StandardIdentifierType.DEVICE, 10);
	}
	private void givenCollectionOfAllMetadataAtCurrent(){
		metadataColl = metadataFinder.getCurrent();
	}
	private void givenCollectionOfAllIdentifierAtCurrent(){
		identifierColl = identfinder.getCurrent();
	}
	private void thenMeanOfLinksOfIdentifiersReturns(){
		double expected = new RMean(Counter.countLinksFromIdentifiers(identfinder.get(StandardIdentifierType.DEVICE, 10))).getMean();
		assertEquals(expected,sc.evaluateMean(StandardIdentifierType.DEVICE, MeanType.LFI),0.001);;
	}
	
	private void givenEmptyListOfIdentifier(){
		identifierColl = identfinder.get(StandardIdentifierType.DEVICE, 0);
	}
	private void thenMeanReturnsNull(){
		assertEquals(0.0, sc.evaluateMean(StandardIdentifierType.DEVICE, MeanType.LFI),0.001);
	}
	
	private void thenEvaluateFrequencyReturnsMapOfIdentifier(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(identifierColl), FrequencyType.IDENTIFIER, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(10, FrequencyType.IDENTIFIER, FrequencySelection.RELATIVE_FREQUENCY));
	}
	
	private void thenEvaluateFrequencyReturnsMapOfMetada(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(metadataColl), FrequencyType.METADATA, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(10, FrequencyType.METADATA, FrequencySelection.RELATIVE_FREQUENCY));
	}
	
	private void thenEvaluateFrequencyReturnsMapOfDevices(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(identifierColl), FrequencyType.DEVICES, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(10, FrequencyType.DEVICES, FrequencySelection.RELATIVE_FREQUENCY));
	}
	private void thenEvaluateFrequencyReturnsMapOfRoles(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(metadataColl), FrequencyType.ROLES, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(10, FrequencyType.ROLES, FrequencySelection.RELATIVE_FREQUENCY));
	}
	
	private void thenEvaluateFrequencyReturnsMapOfIdentifierAtCurrent(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(identifierColl), FrequencyType.IDENTIFIER, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(FrequencyType.IDENTIFIER, FrequencySelection.RELATIVE_FREQUENCY));
	}
	
	private void thenEvaluateFrequencyReturnsMapOfMetadaAtCurrent(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(metadataColl), FrequencyType.METADATA, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(FrequencyType.METADATA, FrequencySelection.RELATIVE_FREQUENCY));
	}
	
	private void thenEvaluateFrequencyReturnsMapOfDevicesAtCurrent(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(identifierColl), FrequencyType.DEVICES, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(FrequencyType.DEVICES, FrequencySelection.RELATIVE_FREQUENCY));
	}
	private void thenEvaluateFrequencyReturnsMapOfRolesAtCurrent(){
		HashMap<String,Double> expected = new RFrequency(new ArrayList<Propable>(metadataColl), FrequencyType.ROLES, FrequencySelection.RELATIVE_FREQUENCY).getFrequency();
		assertEquals(expected, sc.evaluateFrequency(FrequencyType.ROLES, FrequencySelection.RELATIVE_FREQUENCY));
	}
	
}


