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
package de.hshannover.f4.trust.metalyzer.api.impl;

import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.DeltaReturner;
import de.hshannover.f4.trust.metalyzer.api.ListReturner;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerFilter;
import de.hshannover.f4.trust.metalyzer.api.exception.FilterTimeException;
import de.hshannover.f4.trust.metalyzer.api.exception.NegativeTimestampException;
import de.hshannover.f4.trust.metalyzer.api.exception.TimestampException;
import de.hshannover.f4.trust.metalyzer.api.finder.GeneralFinderImpl;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;

public class ReturnerTest {
	
	private MetalyzerFilter returner;
	
	private GeneralFinderImpl finder;
	
	@Test
	public void testIfAsIdentifiersCallsGeneralFinder() throws Exception {
		givenNewReturner();
		
		whenAsIdentifiersCalled();
		
		thenGeneralFinderIdentifierShouldBeCalled();
	}

	private void givenNewReturner() throws NegativeTimestampException {
		finder = Mockito.mock(GeneralFinderImpl.class);
		
		returner = new MetalyzerFilterImpl(finder);
		returner.getMetalyzerTime().at(10);
	}

	private void whenAsIdentifiersCalled() throws FilterTimeException {
		((ListReturner)returner).asIdentifiers();
	}

	private void thenGeneralFinderIdentifierShouldBeCalled() {
		Mockito.verify(finder).filterAsIdentifiers((MetalyzerFilterImpl) returner);
	}
	
	@Test
	public void testIfAsMetadataCallsGeneralFinder() throws Exception {
		givenNewReturner();
		
		whenAsMetadataCalled();
		
		thenGeneralFinderMetadataShouldBeCalled();
	}

	private void whenAsMetadataCalled() throws FilterTimeException {
		((ListReturner)returner).asMetadata();
	}

	private void thenGeneralFinderMetadataShouldBeCalled() {
		Mockito.verify(finder).filterAsMetadata((MetalyzerFilterImpl) returner);
	}
	
	@Test( expected = FilterTimeException.class)
	public void testCallAsIdentifiersWithoutTimeSet() throws FilterTimeException, NegativeTimestampException {
		givenNewReturnerWithoutTime();
		
		whenAsIdentifiersCalled();
	}

	private void givenNewReturnerWithoutTime() {
		finder = Mockito.mock(GeneralFinderImpl.class);
		
		returner = new MetalyzerFilterImpl(finder);
	}
	
	@Test( expected = FilterTimeException.class )
	public void testCallAsMetadataWithoutTimeSet() throws FilterTimeException {
		givenNewReturnerWithoutTime();
		
		whenAsMetadataCalled();
	}
	
	@Test
	public void testIfAsIdentifierDeltaCallsGeneralFinder() throws Exception {
		givenNewDeltaReturner();
		
		whenAsIdentifierDeltaCalled();
		
		thenGeneralFinderIdentifierDeltaShouldBeCalled();
	}

	private void givenNewDeltaReturner() throws TimestampException {
		finder = Mockito.mock(GeneralFinderImpl.class);
		
		returner = new MetalyzerFilterImpl(finder);
		returner.getMetalyzerTime().atDelta(10,15);
	}

	private void whenAsIdentifierDeltaCalled() throws FilterTimeException {
		((DeltaReturner)returner).asIdentifierDelta();
	}

	private void thenGeneralFinderIdentifierDeltaShouldBeCalled() {
		Mockito.verify(finder).filterAsIdentifierDelta((MetalyzerFilterImpl) returner);
	}
	
	@Test
	public void testIfAsMetadataDeltaCallsGeneralFinder() throws Exception {
		givenNewDeltaReturner();
		
		whenAsMetadataDeltaCalled();
		
		thenGeneralFinderMetadataDeltaShouldBeCalled();
	}

	private void whenAsMetadataDeltaCalled() throws FilterTimeException {
		((DeltaReturner)returner).asMetadataDelta();
	}

	private void thenGeneralFinderMetadataDeltaShouldBeCalled() {
		Mockito.verify(finder).filterAsMetadataDelta((MetalyzerFilterImpl) returner);
	}
	
	@Test( expected = FilterTimeException.class)
	public void testCallAsIdentifierDeltaWithoutTimeSet() throws FilterTimeException, NegativeTimestampException {
		givenNewReturnerWithoutTime();
		
		whenAsIdentifierDeltaCalled();
	}
	
	@Test( expected = FilterTimeException.class)
	public void testCallAsMetadataDeltaWithoutTimeSet() throws FilterTimeException, NegativeTimestampException {
		givenNewReturnerWithoutTime();
		
		whenAsMetadataDeltaCalled();
	}
}
