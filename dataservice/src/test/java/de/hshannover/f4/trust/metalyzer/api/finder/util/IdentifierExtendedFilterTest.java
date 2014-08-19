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
import java.util.List;

import org.junit.Test;

import de.hshannover.f4.trust.metalyzer.api.finder.util.IdentifierExtendedFilter;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class IdentifierExtendedFilterTest {
	
	private IdentifierImpl[] idents = new IdentifierImpl[5];
	private MetadataImpl[] metas = new MetadataImpl[5];
	
	private List<Identifier> expectedIdents;
	
	private List<Identifier> actualIdentifier;
	
	@Test
	public void testIdentifierAndMetadataFiltering() {
		givenIdentifierList();
		
		whenFilterWithIdentifierAndMetadataIsCalled();
		
		thenReturnShouldHaveOnlyFilteredResult();
	}

	private void givenIdentifierList() {
		IdentifierGraphImpl graph = new IdentifierGraphImpl(10);
		
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

	private void whenFilterWithIdentifierAndMetadataIsCalled() {
		expectedIdents = new ArrayList<>();
		expectedIdents.add(idents[0]);
		expectedIdents.add(idents[2]);
		expectedIdents.add(idents[3]);
		
		HashSet<String> i = new HashSet<>();
		i.add("device");
		i.add("ip-address");
		
		HashSet<String> m = new HashSet<>();
		i.add("event");
		
		IdentifierExtendedFilter filter = new IdentifierExtendedFilter(i, m);
		
		List<Identifier> l = new ArrayList<>();
		l.add(idents[0]);
		l.add(idents[1]);
		l.add(idents[2]);
		l.add(idents[3]);
		
		
		filter.filter(l);
		actualIdentifier = filter.getList();
	}

	private void thenReturnShouldHaveOnlyFilteredResult() {
		assertThat("Should return a filtered list!",actualIdentifier,is(expectedIdents));
	}
}
