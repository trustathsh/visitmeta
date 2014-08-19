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
package de.hshannover.f4.trust.metalyzer;

import static de.hshannover.f4.trust.metalyzer.PropertyStrings.*;

import java.util.ArrayList;

import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.IdentifierFinder;
import de.hshannover.f4.trust.metalyzer.api.MetadataFinder;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerAPI;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * TestGraphBuilder 2.0 Helper class to build serveral different test graphs in
 * an standardized way.
 * 
 * @author Johannes Busch
 * 
 */
public class TestGraphBuilder {
	GraphBuilderHelper gh;

	private ArrayList<Identifier> identifiers10;
	private ArrayList<Metadata> metas10;

	private ArrayList<Identifier> identifiers11;
	private ArrayList<Metadata> metas11;

	private IdentifierImpl pdp;
	private IdentifierImpl devSwitch;

	/**
	 * Initializes center pdp and switch.
	 */
	private void buildPDPAndSwitch() {
		// Creating the PDP
		pdp = gh.createDevice("pdp");
		// ---------------------------------------------------

		// Adding the IP address to the pdp
		IdentifierImpl pdpIP = gh.createIP("IPv4", "192.168.0.1");

		gh.buildDeviceIPLink(pdp, pdpIP, 10);
		// ----------------------------------------------------

		// Adding the MAC address to the pdp
		IdentifierImpl pdpMAC = gh.createMAC("11:22:33:AA:BB:CC");

		gh.buildIPMACLink(pdpIP, pdpMAC, 10);
		// ------------------------------------------------------

		// Creating a switch
		devSwitch = gh.createDevice("switch");
		// ------------------------------------------------------

		// Adding a IP to the switch
		IdentifierImpl switchIP = gh.createIP("IPv4", "192.168.0.200");

		gh.buildDeviceIPLink(devSwitch, switchIP, 10);
		// ---------------------------------------------------------
	}

	/**
	 * Returns a mocked MetalyzerAPI with a mocked IdentifierFinder. The mocked
	 * finder returns a list of identifiers when get method is called like this:
	 * finder.get(10);
	 * 
	 * @return
	 */
	public MetalyzerAPI getMockedIdentifierFinderAt10() {
		buildTestGraphAt10();

		IdentifierFinder finder = Mockito.mock(IdentifierFinder.class);

		Mockito.when(finder.get(10)).thenReturn(identifiers10);

		MetalyzerAPI api = Mockito.mock(MetalyzerAPI.class);

		Mockito.when(api.getIdentifierFinder()).thenReturn(finder);

		return api;
	}
	
	/**
	 * Returns a mocked MetalyzerAPI with a mocked MetadataFinder. The mocked
	 * finder returns a list of metadatas when get method is called like this:
	 * finder.get(10);
	 * 
	 * @return
	 */
	public MetalyzerAPI getMockedMetadataFinderAt10() {
		buildTestGraphAt10();

		MetadataFinder finder = Mockito.mock(MetadataFinder.class);

		Mockito.when(finder.get(10)).thenReturn(metas10);

		MetalyzerAPI api = Mockito.mock(MetalyzerAPI.class);

		Mockito.when(api.getMetadataFinder()).thenReturn(finder);

		return api;
	}

	/**
	 * Returns a mocked GraphService with a graph at Timestamp 10.
	 * 
	 * @return
	 */
	public GraphService getGraphServiceAt10() {
		buildTestGraphAt10();

		GraphService service = Mockito.mock(GraphService.class);

		IdentifierGraphImpl graph = new IdentifierGraphImpl(10);

		initGraph(graph);

		ArrayList<IdentifierGraph> graphs = new ArrayList<>();

		graphs.add(graph);

		Mockito.when(service.getGraphAt(10)).thenReturn(graphs);

		return service;
	}

	/**
	 * Builds the complete graph structure at the timestamp 10.
	 */
	private void buildTestGraphAt10() {
		identifiers10 = new ArrayList<>();
		metas10 = new ArrayList<>();

		gh = new GraphBuilderHelper(identifiers10, metas10);
		buildPDPAndSwitch();
		buildAR10();
	}

	/**
	 * Just builds all additional parts of the timestamp 10 graph.
	 */
	private void buildAR10() {
		// Starting with the Access-Request
		IdentifierImpl ar = gh.createAR("ar:10");

		MetadataImpl cap = new MetadataImpl("capability", false, 10);
		metas10.add(cap);
		cap.addProperty(CAP_NAME.get(), "access-server-allowed");

		ar.addMetadata(cap);
		// ----------------------------------------------------

		// Adding a Link to the pdp.
		gh.buildAuthByLink(pdp, ar, 10);
		// --------------------------------------------------

		// Adding a IP Address
		IdentifierImpl ip = gh.createIP("IPv4", "192.168.0.10");

		gh.buildARIPLink(ar, ip, 10);
		// ----------------------------------------------------

		// Adding a MAC Address
		IdentifierImpl mac = gh.createMAC("10:22:33:AA:BB:DD");

		gh.buildARMACLink(ar, mac, 10);
		// ---------------------------------------------------

		// Adding Link between IP and MAC
		gh.buildIPMACLink(ip, mac, 10);
		// -----------------------------------------------------

		// Adding an Identity
		IdentifierImpl ident = gh.createIdentity("username", "john doe");

		MetadataImpl authAs = new MetadataImpl("authenticated-as", true, 10);
		metas10.add(authAs);

		LinkImpl l_AR_ID = gh.buildRoleLink(ar, ident, 10, "employee");
		l_AR_ID.addMetadata(authAs);
		// --------------------------------------------------------------

		// Adding a Device
		IdentifierImpl dev = gh.createDevice("John Doe Device");

		LinkImpl l_AR_DEV = gh.buildARDevLink(ar, dev, 10);

		MetadataImpl devAttr = new MetadataImpl("device-attribute", false, 10);
		devAttr.addProperty(DEV_ATTR.get(), "low-disc-space");
		metas10.add(devAttr);

		l_AR_DEV.addMetadata(devAttr);
		// -------------------------------------------------------------------

		// Adding the Switch
		gh.buildLayer2Link(ar, devSwitch, 10, "15", "2", "employee-vlan");
		// ----------------------------------------------------------------------
	}

	private void initGraph(IdentifierGraphImpl graph) {
		for (Identifier ident : identifiers10) {
			graph.insert(ident);
		}
	}

	/**
	 * Returns a mocked MetalyzerAPI with a mocked IdentifierFinder. The mocked
	 * finder returns a list of identifiers when get method is called like this:
	 * finder.get(11);
	 * 
	 * @return
	 */
	public MetalyzerAPI getMockedIdentifierFinderAt11() {
		buildTestGraphAt11();

		IdentifierFinder finder = Mockito.mock(IdentifierFinder.class);
		
		identifiers11.addAll(identifiers10);
		Mockito.when(finder.get(11)).thenReturn(identifiers11);

		MetalyzerAPI api = Mockito.mock(MetalyzerAPI.class);

		Mockito.when(api.getIdentifierFinder()).thenReturn(finder);

		return null;
	}
	
	/**
	 * Returns a mocked MetalyzerAPI with a mocked MetadataFinder. The mocked
	 * finder returns a list of metadatas when get method is called like this:
	 * finder.get(10);
	 * 
	 * @return
	 */
	public MetalyzerAPI getMockedMetadataFinderAt11() {
		buildTestGraphAt11();

		MetadataFinder finder = Mockito.mock(MetadataFinder.class);
		
		metas11.addAll(metas10);
		Mockito.when(finder.get(11)).thenReturn(metas11);

		MetalyzerAPI api = Mockito.mock(MetalyzerAPI.class);

		Mockito.when(api.getMetadataFinder()).thenReturn(finder);

		return api;
	}
	
	/**
	 * Builds the complete graph structure at the timestamp 11.
	 */
	private void buildTestGraphAt11() {
		identifiers10 = new ArrayList<>();
		metas10 = new ArrayList<>();
		gh = new GraphBuilderHelper(identifiers10, metas10);

		buildPDPAndSwitch();
		buildAR10();

		identifiers11 = new ArrayList<>();
		metas11 = new ArrayList<>();
		gh = new GraphBuilderHelper(identifiers11, metas11);

		buildAR11();
	}

	/**
	 * Just builds all additional parts of the timestamp 11 graph.
	 */
	private void buildAR11() {
		// Starting with the Access-Request
		IdentifierImpl ar = gh.createAR("ar:11");
		// ----------------------------------------------------

		// Adding a Link to the pdp.
		gh.buildAuthByLink(pdp, ar, 11);
		// --------------------------------------------------

		// Adding a IP Address
		IdentifierImpl ip = gh.createIP("IPv4", "192.168.0.11");

		gh.buildARIPLink(ar, ip, 11);
		// ----------------------------------------------------

		// Adding second IP Address
		IdentifierImpl secIP = gh.createIP("IPv4", "192.168.0.111");

		gh.buildARIPLink(ar, secIP, 11);
		// ----------------------------------------------------

		// Adding a MAC Address with Link to ip
		IdentifierImpl mac = gh.createMAC("11:21:33:AA:BB:DD");

		gh.buildIPMACLink(ip, mac, 11);
		// ---------------------------------------------------

		// Adding second MAC Address
		IdentifierImpl secMac = gh.createMAC("11:22:33:AA:BB:DD");

		gh.buildARMACLink(ar, secMac, 11);
		// ---------------------------------------------------

		// Adding third MAC Address
		IdentifierImpl thirdMac = gh.createMAC("11:23:33:AA:BB:DD");

		gh.buildARMACLink(ar, thirdMac, 11);
		// ---------------------------------------------------

		// Adding an Identity
		IdentifierImpl ident = gh.createIdentity("username", "Albert Einstein");

		MetadataImpl authAs = new MetadataImpl("authenticated-as", true, 11);
		metas11.add(authAs);

		LinkImpl l_AR_ID = gh.buildRoleLink(ar, ident, 11, "physicist");
		l_AR_ID.addMetadata(authAs);
		// --------------------------------------------------------------

		// Adding a second Identity
		IdentifierImpl secIdent = gh.createIdentity("username", "Galileo Galilei");

		MetadataImpl secAuthAs = new MetadataImpl("authenticated-as", true, 11);
		metas11.add(secAuthAs);

		LinkImpl l_SEC_AR_ID = gh.buildLink(ar, secIdent);
		l_SEC_AR_ID.addMetadata(secAuthAs);
		// --------------------------------------------------------------

		// Adding a Device
		IdentifierImpl dev = gh.createDevice("Albert Einstein Device");

		LinkImpl l_AR_DEV = gh.buildARDevLink(ar, dev, 11);

		MetadataImpl devAttr = new MetadataImpl("device-attribute", false, 11);
		devAttr.addProperty(DEV_ATTR.get(), "low-disc-space");
		metas11.add(devAttr);

		l_AR_DEV.addMetadata(devAttr);
		
		IdentifierImpl devIP = gh.createIP("IPv4", "192.168.0.211");
		
		gh.buildDeviceIPLink(dev, devIP, 11);
		// -------------------------------------------------------------------
	}
}
