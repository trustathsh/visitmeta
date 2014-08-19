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
/** Project: Metalyzer
 * Author: Michael Felchner
 * Author: Mihaela Stein
 * Author: Sven Steinbach
 * Last Change:
 * 	by: $Author: $
 * 	date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

package de.hshannover.f4.trust.metalyzer.semantics.implementations;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.mockito.Mockito;

import de.hshannover.f4.trust.metalyzer.api.IdentifierFinder;
import de.hshannover.f4.trust.metalyzer.api.MetalyzerAPI;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;
import de.hshannover.f4.trust.metalyzer.semantics.services.IpAddressService;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.implementations.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.implementations.LinkImpl;
import de.hshannover.f4.trust.visitmeta.implementations.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class IpAddressServiceTest {
	
	private IpAddressService ipAddressService;
	private User user;
	private IpAddress nonExistingIpAddress =  new IpAddress(123456, "192.168.0.2", "IPv4");
	private IpAddress ipAddress;
	private MacAddress macAddress;
	private Device device;
	private int expectedNumberOfIpAddresses;
	private int NumberOfIpAddresses;
	private long timestamp;
	private long expectedTimestamp;
	private long nonValidTimestamp = 0;  // 01.01.1970 00:00:00
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<User> expectedUsers = new ArrayList<User>();
	private ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
	private ArrayList<IpAddress> expectedIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> expectedMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<Device> devices = new ArrayList<Device>();
	private String deviceAttribute = "low-disk-space";
	private ArrayList<String> deviceAttributes = new ArrayList<String>();
	private ArrayList<Device> expectedDevices = new ArrayList<Device>();
	private ArrayList<IpAddress> currentIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<MacAddress> expectedCurrentMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> currentMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<IpAddress> expectedCurrentIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<User> currentUsers = new ArrayList<User>();
	private ArrayList<User> expectedCurrentUsers = new ArrayList<User>();
	private ArrayList<Device> currentDevices = new ArrayList<Device>();
	private ArrayList<Device> expectedCurrentDevices = new ArrayList<Device>();
	
	// getIpAddresses() timestamp-request:
	@Test
	public void testGetIpAddressesWithTimestamp() {
		givenIFMAPGraphWithIpAddresses();
		whenGetIpAddressesIsCalledWithTimestamp(expectedTimestamp);
		thenIpAddressesShouldBe();
	}
	
	// getIpAddresses timestamp-request (negative):
	@Test
	public void testGetIpAddressesWithNonValidTimestamp() {
		givenIFMAPGraphWithIpAddresses();
		whenGetIpAddressesIsCalledWithNonValidTimestamp(nonValidTimestamp);
		thenIpAddressesWithNonValidTimestampShouldBe();
	}
	
	// getTimestamp() timestamp-request:
	@Test
	public void testGetTimestampOfExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetTimestampIsCalledWithIpAddress(ipAddress);
		thenTimestampOfIpAddressShouldBe();
	}
	
	// getTimestamp timestamp-request (negative):
	@Test
	public void testGetTimestampOfNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetTimestampIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenTimestampOfNonExistingIpAddressShouldBe();
	}
		
	// getIpAddresses() current-request:
		@Test
		public void testGetCurrentIpAddresses() {
			givenIFMAPGraphWithIpAddresses();
			whenGetCurrentIpAddressesIsCalled();
			thenCurrentIpAddressesShouldBe();
	}

	// getTimestamp current-request (negative):
	@Test
	public void testGetTimestampOfNonExistingIpAddressCurrent() {
		givenIFMAPGraphWithIpAddresses();
		whenGetTimestampCurrentIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenTimestampOfNonExistingIpAddressCurrentShouldBe();
	}
	
	// getTimestamp() current-request:
	@Test
	public void testGetTimestampOfExistingIpAddressCurrent() {
		givenIFMAPGraphWithIpAddresses();
		whenGetTimestampCurrentIsCalledWithIpAddress(ipAddress);
		thenTimestampOfIpAddressCurrentShouldBe();
	}

	// getDevices() timestamp-request:
	@Test
	public void testGetDevicesWithIpAddress(){
		givenIFMAPGraphWithIpAddresses();
		whenGetDevicesIsCalledWithIpAddress(ipAddress);
		thenDevicesShouldBe();
	}
	
	// getDevices() current-request:
	@Test
	public void testGetCurrentDevices() {
		givenIFMAPGraphWithIpAddresses();
		whenGetCurrentDevicesIsCalledWithIpAddress(ipAddress);
		thenCurrentDevicesShouldBe();
	}
	
	// getDevice timestamp-request (negative):
	@Test
	public void testGetDevicesWithNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetDevicesIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenDevicesWithNonExistingIpAddressShouldBe();
	}
	
	// getDevices current-request (negative):
	@Test
	public void testGetCurrentDevicesWithNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetCurrentDevicesIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenCurrentDevicesWithNonExistingIpAddressShouldBe();
	}
	
	// getMcAddresses() timestamp-request:
	@Test
	public void testGetMacAddressesWithIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetMacAddressesIsCalledWithIpAddress(ipAddress);
		thenMacAddressesShouldBe();
	}
	
	// getMacAddresses() current-request:
	@Test
	public void testGetCurrentMacAddresses() {
		givenIFMAPGraphWithIpAddresses();
		whenGetCurrentMacAddressesIsCalledWithIpAddress(ipAddress);
		thenCurrentMacAddressesShouldBe();
	}

	// getMcAddresses() timestamp-request (negative):
	@Test
	public void testGetMacAddressesWithNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetMacAddressesIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenGetMacAddressesWithNonExistingIpAddressShouldBe();
	}
	
	// getMacAddresses current-request (negative):
	@Test
	public void testGetCurrentMacAddressesWithNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetCurrentMacAddressesIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenCurrentMacAddressesWithNonExistingIPAddressShouldBe();
	}

	private void thenCurrentMacAddressesWithNonExistingIPAddressShouldBe() {
		// TODO Auto-generated method stub
		
	}

	private void whenGetCurrentMacAddressesIsCalledWithNonExistingIpAddress(
			IpAddress nonExistingIpAddress2) {
		// TODO Auto-generated method stub
		
	}

	// getUsers() timestamp-request:
	@Test
	public void testGetUsersWithIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetUsersIsCalledWithIpAddress(ipAddress);
		thenUsersShouldBe();
	}
	
	// getUser timestamp-request (negative):
	@Test
	public void testGetUsersWithNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetUsersIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenUsersWithNonExistingIpAddressShouldBe();
	}
	
	// getUsers() current-request:
		@Test
		public void testGetCurrentUsers() {
			givenIFMAPGraphWithIpAddresses();
			whenGetCurrentUsersIsCalledWithIpAddress(ipAddress);
			thenCurrentUsersShouldBe();
		}
		
	// getUsers current-request (negative):
	@Test
	public void testGetCurrentUsersWithNonExistingIpAddress() {
		givenIFMAPGraphWithIpAddresses();
		whenGetCurrentUsersIsCalledWithNonExistingIpAddress(nonExistingIpAddress);
		thenGetCurrentUsersWithNonExistingIpAddressShouldBe();
	}

	/*
	@Test
	public void testCountIpAdressesWithTimestamp() {
		givenIFMAPGraphWithIpAddresses();
		whenCountIpAddressesIsCalledWithTimestamp(expectedTimestamp);
		thenNumberOfIpAddressesShouldBe();
	}
	*/
	
	// getIPAddresses timestamp-request:
	private void whenGetIpAddressesIsCalledWithTimestamp(long expectedTimestamp) {
		ipAddresses = ipAddressService.getIpAddresses(RequestType.TIMESTAMP_REQUEST, expectedTimestamp,0);
	}
	
	// getIpAddresses timestamp-request:
	private void thenIpAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of ipAddresses is correct.", expectedIpAddresses, ipAddresses);
	}
	
	// getIpAddresses current-request:
		private void whenGetCurrentIpAddressesIsCalled() {
			currentIpAddresses = ipAddressService.getIpAddresses(RequestType.CURRENT_REQUEST, 0, 0);
		}
		
	// getIpAddresses current-request:
	private void thenCurrentIpAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of IP-Addresses is correct", expectedCurrentIpAddresses, currentIpAddresses);
	}

	// getIpAddresses timestamp-request (negative):
	private void whenGetIpAddressesIsCalledWithNonValidTimestamp(long nonValidTimestamp) {
		ipAddresses = ipAddressService.getIpAddresses(RequestType.TIMESTAMP_REQUEST,nonValidTimestamp,0);
	}
	
	// getIpAddresses timestamp-request (negative):
	private void thenIpAddressesWithNonValidTimestampShouldBe() {
		assertEquals("Tests if the returned ArrayList of ipAddresses is correct.", 0, ipAddresses.size());
	}
	
	// getTimestamp timestamp-request:
	private void whenGetTimestampIsCalledWithIpAddress(IpAddress i) {
		timestamp = ipAddressService.getTimestamp(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getTimestamp timestamp-request:
	private void thenTimestampOfIpAddressShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	// getTimestamp current-request:
	private void whenGetTimestampCurrentIsCalledWithIpAddress(IpAddress i) {
		timestamp = ipAddressService.getTimestamp(RequestType.CURRENT_REQUEST, i);
	}
	
	// getTimestamp current-request:
	private void thenTimestampOfIpAddressCurrentShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	// getTimestamp timestamp-request (negative):
	private void whenGetTimestampIsCalledWithNonExistingIpAddress(IpAddress i) {
		timestamp = ipAddressService.getTimestamp(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getTimestamp timestamp-request (negative):
	private void thenTimestampOfNonExistingIpAddressShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}
	
	// getTimestamp current-request (negative):
	private void whenGetTimestampCurrentIsCalledWithNonExistingIpAddress(IpAddress i) {
		timestamp = ipAddressService.getTimestamp(RequestType.CURRENT_REQUEST, i);
	}
	
	// getTimestamp current-request (negative):
	private void thenTimestampOfNonExistingIpAddressCurrentShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}

	// getDevices timestamp-request:
	private void whenGetDevicesIsCalledWithIpAddress(IpAddress i) {
		devices = ipAddressService.getDevices(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getDevices timestamp-request:
	private void thenDevicesShouldBe(){
		assertEquals("Test if the returned ArrayList of devices is correct.", expectedDevices, devices);
	}
	
	// getDevices current-request:
	private void whenGetCurrentDevicesIsCalledWithIpAddress(IpAddress i) {
		currentDevices = ipAddressService.getDevices(RequestType.CURRENT_REQUEST, i);
	}
	
	// getDevices current-request:
	private void thenCurrentDevicesShouldBe() {
		assertEquals("Tests if the returned ArrayList of devices is correct.", expectedCurrentDevices, currentDevices);
	}
	
	// getDevices timestamp-request (negative):
	private void whenGetDevicesIsCalledWithNonExistingIpAddress(IpAddress i) {
		devices= ipAddressService.getDevices(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getDevices timestamp-request (negative):
	private void thenDevicesWithNonExistingIpAddressShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", 0, devices.size());
	}
	
	// getDevices current-request (negative):
	private void whenGetCurrentDevicesIsCalledWithNonExistingIpAddress(IpAddress i) {
		currentDevices = ipAddressService.getDevices(RequestType.CURRENT_REQUEST, i);
	}
	
	// getDevices current-request (negative):
	private void thenCurrentDevicesWithNonExistingIpAddressShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", 0, currentDevices.size());
	}
			
	// getMacAddresses timestamp-request:
	private void whenGetMacAddressesIsCalledWithIpAddress(IpAddress i) {
		macAddresses = ipAddressService.getMacAddresses(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getMacAddresses timestamp-request:
	private void thenMacAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", expectedMacAddresses, macAddresses);
	}
		
	// getMacAddresses current-request:
	private void whenGetCurrentMacAddressesIsCalledWithIpAddress(IpAddress i) {
		currentMacAddresses = ipAddressService.getMacAddresses(RequestType.CURRENT_REQUEST, i);
	}
		
	// getMacAddresses current-request:
	private void thenCurrentMacAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", expectedCurrentMacAddresses, currentMacAddresses);
	}
	
	// getMacAddresses timestamp-request (negative):
	private void whenGetMacAddressesIsCalledWithNonExistingIpAddress(IpAddress i) {
		macAddresses = ipAddressService.getMacAddresses(RequestType.TIMESTAMP_REQUEST,i);
	}
	private void thenGetMacAddressesWithNonExistingIpAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", 0, macAddresses.size());
	}
	
	// getUsers timestamp-request:
	private void whenGetUsersIsCalledWithIpAddress(IpAddress i) {
		users = ipAddressService.getUsers(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getUsers timestamp-request:
	private void thenUsersShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", expectedUsers, users);
	}
	
	// getUsers current-request:
	private void whenGetCurrentUsersIsCalledWithIpAddress(IpAddress i) {
		currentUsers = ipAddressService.getUsers(RequestType.CURRENT_REQUEST, i);
	}
	
	// getUsers current-request:
	private void thenCurrentUsersShouldBe() {
		assertEquals("Test if the returned ArrayList of users is correct.", expectedCurrentUsers, currentUsers);
	}

	// getUsers timestamp-request (negative):
	private void whenGetUsersIsCalledWithNonExistingIpAddress(	IpAddress i) {
		users = ipAddressService.getUsers(RequestType.TIMESTAMP_REQUEST,i);
	}
	
	// getUsers timestamp-request (negative):
	private void thenUsersWithNonExistingIpAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", 0, users.size());
	}
	
	// getUsers current-request (negative):
	private void whenGetCurrentUsersIsCalledWithNonExistingIpAddress(IpAddress i) {
		currentUsers = ipAddressService.getUsers(RequestType.CURRENT_REQUEST, i);
	}
		
	// getUsers current-request (negative):
	private void thenGetCurrentUsersWithNonExistingIpAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", 0, currentUsers.size());
	}

		

	private void givenIFMAPGraphWithIpAddresses() {
		expectedTimestamp = 123456;
		
		user = new User(expectedTimestamp, "Bob", "staff");
		expectedUsers.add(user);
		expectedCurrentUsers.add(user);
		
		ipAddress = new IpAddress(expectedTimestamp, "192.168.0.1", "IPv4");
		ipAddress.setSearchTimestamp(expectedTimestamp);
		expectedIpAddresses.add(ipAddress);
		expectedNumberOfIpAddresses = 1;
		expectedCurrentIpAddresses.add(ipAddress);
		
		macAddress = new MacAddress(expectedTimestamp, "aa:bb:cc:dd:ee:ff");
		expectedMacAddresses.add(macAddress);
		expectedCurrentMacAddresses.add(macAddress);
		
		device = new Device(expectedTimestamp, "pc-01", deviceAttributes);
		deviceAttributes.add(deviceAttribute);
		expectedDevices.add(device);
		expectedCurrentDevices.add(device);
		
		//User1-Identifier:
		IdentifierImpl id1 = new IdentifierImpl("identity");
		id1.addProperty("/identity[@type]", "username");
		id1.addProperty("/identity[@name]", user.getName());
		
		//Access-Request-Identifier:
		IdentifierImpl id2 = new IdentifierImpl("access-request");
		
		//IP-Address-Identifier:
		IdentifierImpl id3 = new IdentifierImpl("ip-address");
		id3.addProperty("/ip-address[@value]", ipAddress.getAddress());
		id3.addProperty("/ip-address[@type]", ipAddress.getType());
		
		//MAC-Address-Identifier:
		IdentifierImpl id4 = new IdentifierImpl("mac-address");
		id4.addProperty("/mac-address[@value]", macAddress.getAddress());
		
		//Device-Identifier:
		IdentifierImpl device1 = new IdentifierImpl("device");
		device1.addProperty("/device/name", device.getName());
			 
		//Link between Identity and Access-Request with authenticated-as-metadata:
		MetadataImpl meta1 = new MetadataImpl("authenticated-as", true, user.getTimestamp());
		LinkImpl link1 = new LinkImpl(id1, id2);
		link1.addMetadata(meta1);
		id1.addLink(link1);
		id2.addLink(link1);

		//Link between Identity and Access-equest with role-metadata:
		MetadataImpl meta2 = new MetadataImpl("role", true, user.getTimestamp());
		meta2.addProperty("/meta:role/name", user.getRole());
		LinkImpl link2 = new LinkImpl(id1, id2);
		link2.addMetadata(meta2);
		id1.addLink(link2);
		id2.addLink(link2);
		
		//Link between IP-Address and Access-Request with access-request-ip-metadata:
		MetadataImpl meta3 = new MetadataImpl("access-request-ip", true, ipAddress.getTimestamp());
		LinkImpl link3 = new LinkImpl(id3, id2);
		link3.addMetadata(meta3);
		id3.addLink(link3);
		id2.addLink(link3);
		
		//Link between MAC-Address and Access-Request with access-request-mac-metadata:
		MetadataImpl meta4 = new MetadataImpl("access-request-mac", true, macAddress.getTimestamp());
		LinkImpl link4 = new LinkImpl(id4, id2);
		link4.addMetadata(meta4);
		id4.addLink(link4);
		id2.addLink(link4);
		
		//Link between Device and Access-Request with access-request-device-metadata
		MetadataImpl deviceMeta1 = new MetadataImpl("access-request-device", true, device.getTimestamp());
		MetadataImpl deviceAttributeMeta1 = new MetadataImpl("device-attribute", true, device.getTimestamp());
		deviceAttributeMeta1.addProperty("/meta:device-attribute/name", deviceAttribute);
		LinkImpl deviceLink1 = new LinkImpl(id2, device1);
		deviceLink1.addMetadata(deviceMeta1);
		deviceLink1.addMetadata(deviceAttributeMeta1);
		device1.addLink(deviceLink1);
		id2.addLink(deviceLink1);
		
		//Link between MAC-Address and IP-Address with ip-mac-metadata:
		MetadataImpl meta5 = new MetadataImpl("ip-mac", true, ipAddress.getTimestamp());
		LinkImpl link5 = new LinkImpl(id3, id4);
		link5.addMetadata(meta5);
		id3.addLink(link5);
		id4.addLink(link5);
		
		Collection<Identifier> colId = new ArrayList<Identifier>();
		colId.add(id3);
		
		SemanticsController semCon = Mockito.mock(SemanticsController.class);
	
		MetalyzerAPI mAPI = Mockito.mock(MetalyzerAPI.class);
		
		IdentifierFinder identifierFinder = Mockito.mock(IdentifierFinder.class);
		try {
			Mockito.when(identifierFinder.get(StandardIdentifierType.IP_ADDRESS, ipAddress.getTimestamp())).thenReturn(colId);
			Mockito.when(identifierFinder.getCurrent(StandardIdentifierType.IP_ADDRESS)).thenReturn(colId);
		} catch (MetalyzerAPIException e) {
			fail("Exception was thrown. This should not happen!" + e.getMessage());
		}

		Mockito.when(mAPI.getIdentifierFinder()).thenReturn(identifierFinder);
		Mockito.when(semCon.getConnection()).thenReturn(mAPI);
		
		
		ipAddressService = new IpAddressService(semCon);
		
	}
	
}
