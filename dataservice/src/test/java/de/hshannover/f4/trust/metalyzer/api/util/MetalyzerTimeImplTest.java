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
package de.hshannover.f4.trust.metalyzer.api.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.DeltaReturner;
import de.hshannover.f4.trust.metalyzer.api.ListReturner;
import de.hshannover.f4.trust.metalyzer.api.exception.NegativeTimestampException;
import de.hshannover.f4.trust.metalyzer.api.exception.TimestampException;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerFilterImpl;
import de.hshannover.f4.trust.metalyzer.api.impl.MetalyzerTimeImpl;

public class MetalyzerTimeImplTest {
	
	private MetalyzerTimeImpl time;
	private long[] delta = new long[2];
	private Object returnObject;
	
	@Test( expected = RuntimeException.class )
	public void testInitTimeWithNullObject() {
		givenNewEmptyTime();
	}

	private void givenNewEmptyTime() {
		new MetalyzerTimeImpl(null);
	}
	
	@Test
	public void testIfListReturnerIsReturnedByAtCurrent() {
		givenNewTime();
		
		whenAtCurrentIsCalled();
		
		thenReturnObjecShouldBeListReturner();
	}

	private void givenNewTime() {
		MetalyzerFilterImpl filter = Mockito.mock(MetalyzerFilterImpl.class);
		
		time = new MetalyzerTimeImpl(filter);
	}
	
	private void whenAtCurrentIsCalled() {
		returnObject = time.atCurrent();
	}

	private void thenReturnObjecShouldBeListReturner() {
		assertThat(returnObject,instanceOf(ListReturner.class));
	}
	
	@Test
	public void testIfListReturnerIsReturnedByAt() throws NegativeTimestampException {
		givenNewTime();
		
		whenAtIsCalled();
		
		thenReturnObjecShouldBeListReturner();
	}
	
	private void whenAtIsCalled() throws NegativeTimestampException {
		returnObject = time.at(10);
	}
	
	@Test
	public void testIfDeltaReturnerIsReturnedByAtDelta() throws Exception {
		givenNewTime();
		
		whenAtDeltaIsCalled();
		
		thenReturnObjecShouldBeDeltaReturner();
	}
	
	private void whenAtDeltaIsCalled() throws TimestampException {
		returnObject = time.atDelta(10, 12);
	}
	
	private void thenReturnObjecShouldBeDeltaReturner() {
		assertThat(returnObject,instanceOf(DeltaReturner.class));
	}

	@Test
	public void testCallAtCurrentMethodAtLast() throws TimestampException {
		givenNewTime();
		
		whenAtCurrentIsCalledAtLast();
		
		thenCurrentShouldBeSet();
	}

	private void whenAtCurrentIsCalledAtLast() throws TimestampException {
		time.atDelta(2, 5);
		time.at(10);
		time.atCurrent();
	}

	private void thenCurrentShouldBeSet() {
		assertEquals(false, time.isAt());
		assertEquals("Time should return true because atCurrent was called last!",true,time.isCurrent());
	}
	
	@Test
	public void testCallAtMethodAtLast() throws TimestampException {
		givenNewTime();
		
		whenAtIsCalledAtLast();
		
		thenAtShouldBeSet();
	}

	private void whenAtIsCalledAtLast() throws TimestampException {
		time.atDelta(12, 15);
		time.atCurrent();
		time.at(10);
	}

	private void thenAtShouldBeSet() {
		assertEquals(false,time.isCurrent());
		assertEquals("Time should return true because at was called last!",true, time.isAt());
		assertEquals(10, time.getAtTimestampt());
	}
	
	@Test
	public void testCallAtDeltaAtLast() throws TimestampException {
		givenNewTime();
		
		whenAtDeltaIsCalledAtLast();
		
		thenAtDeltaShouldBeSet();
	}

	private void whenAtDeltaIsCalledAtLast() throws TimestampException {
		time.atCurrent();
		time.at(10);
		time.atDelta(12, 15);
		delta[0] = 12;
		delta[1] = 15;
	}

	private void thenAtDeltaShouldBeSet() {
		assertEquals(false,time.isCurrent());
		assertEquals(false, time.isAt());
		assertEquals("Time should return true because atDelta was called last!",true,time.isDelta());
		assertArrayEquals(delta, time.getDelta());
	}
	
	@Test( expected = NegativeTimestampException.class )
	public void testCallAtWithNegativeTimestamp() throws NegativeTimestampException {
		givenNewTime();
		
		whenAtIsCalledWithNegativeTimesampt();
	}

	private void whenAtIsCalledWithNegativeTimesampt() throws NegativeTimestampException {
		time.at(-4);
	}
	
	@Test( expected = NegativeTimestampException.class )
	public void testCallAtDeltaWithNegativeTimestamp() throws TimestampException {
		givenNewTime();
		
		whenAtDeltaIsCalledWithNegativeTimesampt();
	}

	private void whenAtDeltaIsCalledWithNegativeTimesampt() throws TimestampException {
		time.atDelta(-4,10);
	}
	
	@Test( expected = TimestampException.class )
	public void testCallAtDeltaWithImpossibleDelta() throws TimestampException {
		givenNewTime();
		
		whenAtDeltaIsCalledWithImpossibleDelta();
	}

	private void whenAtDeltaIsCalledWithImpossibleDelta() throws TimestampException {
		time.atDelta(17,10);
	}
}
