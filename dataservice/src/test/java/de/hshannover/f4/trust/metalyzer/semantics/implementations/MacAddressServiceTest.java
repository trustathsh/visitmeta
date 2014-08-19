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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
import de.hshannover.f4.trust.metalyzer.semantics.services.MacAddressService;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.implementations.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.implementations.LinkImpl;
import de.hshannover.f4.trust.visitmeta.implementations.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class MacAddressServiceTest {
	private MacAddressService macAddressService;
	private MacAddress mac;
	private MacAddress nonExistingMac = new MacAddress(123456, "00:00:00:00:00:00");
	private IpAddress ipAddress;
	private User user;
	private Device device;
	private long timestamp;
	private long expectedTimestamp;
	private long nonValidTimestamp = 0;  // 01.01.1970 00:00:00
	private ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> expectedMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
	private ArrayList<IpAddress> expectedIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<User> expectedUsers = new ArrayList<User>();
	private ArrayList<Device> devices = new ArrayList<Device>();
	private String deviceAttribute = "low-disk-space";
	private ArrayList<String> deviceAttributes = new ArrayList<String>();
	private ArrayList<Device> expectedDevices = new ArrayList<Device>();
	private ArrayList<MacAddress> currentMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> expectedCurrentMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<IpAddress> currentIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<IpAddress> expectedCurrentIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<User> currentUsers = new ArrayList<User>();
	private ArrayList<User> expectedCurrentUsers = new ArrayList<User>();
	private ArrayList<Device> currentDevices = new ArrayList<Device>();
	private ArrayList<Device> expectedCurrentDevices = new ArrayList<Device>();

	// getMacAddresses() timestamp-request:
	@Test
	public void testGetMacAddresses() {
		givenIFMAPGraphWithMacAddresses();
		whenGetMacAddressesIsCalledWithTimestamp(expectedTimestamp);
		thenMacAddressesShouldBe();
	}
	
	// getMacAddresses() current-request:
	@Test
	public void testGetCurrentMacAddresses() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentMacAddressesIsCalled();
		thenCurrentMacAddressesShouldBe();
	}

	// getTimestamp() timestamp-request:
	@Test
	public void testGetTimestampOfExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetTimestampIsCalledWithMacAddress(mac);
		thenTimestampOfMacAddressShouldBe();
	}
	
	// getTimestamp() current-request:
	@Test
	public void testGetTimestampOfExistingMacAddressCurrent() {
		givenIFMAPGraphWithMacAddresses();
		whenGetTimestampCurrentIsCalledWithMacAddress(mac);
		thenTimestampOfMacAddressCurrentShouldBe();
	}

	// getIpAddresses() timestamp-request:
	@Test
	public void testGetIpAddresses() {
		givenIFMAPGraphWithMacAddresses();
		whenGetIpAddressesIsCalledWithMacAddress(mac);
		thenIpAddressesShouldBe();
	}
	
	// getIpAddresses() current-request:
	@Test
	public void testGetCurrentIpAddresses() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentIpAddressesIsCalledWithMacAddress(mac);
		thenCurrentIpAddressesShouldBe();
	}

	// getUsers() timestamp-request:
	@Test
	public void testGetUsers() {
		givenIFMAPGraphWithMacAddresses();
		whenGetUsersIsCalledWithMacAddress(mac);
		thenUsersShouldBe();
	}
	
	// getUsers() current-request:
	@Test
	public void testGetCurrentUsers() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentUsersIsCalledWithMacAddress(mac);
		thenCurrentUsersShouldBe();
	}

	// getDevices() timestamp-request:
	@Test
	public void testGetDevices() {
		givenIFMAPGraphWithMacAddresses();
		whenGetDevicesIsCalledWithMacAddress(mac);
		thenDevicesShouldBe();
	}
	
	// getDevices() current-request:
	@Test
	public void testGetCurrentDevices() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentDevicesIsCalledWithMacAddress(mac);
		thenCurrentDevicesShouldBe();
	}

	// getTimestamp timestamp-request (negative):
	@Test
	public void testGetTimestampOfNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetTimestampIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenTimestampOfNonExistingMacAddressShouldBe();
	}
	
	// getTimestamp current-request (negative):
	@Test
	public void testGetTimestampOfNonExistingMacAddressCurrent() {
		givenIFMAPGraphWithMacAddresses();
		whenGetTimestampCurrentIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenTimestampOfNonExistingMacAddressCurrentShouldBe();
	}

	// getMacAddresses timestamp-request (negative):
	@Test
	public void testGetMacAddressesWithNonValidTimestamp() {
		givenIFMAPGraphWithMacAddresses();
		whenGetMacAddressesIsCalledWithNonValidTimestamp(nonValidTimestamp);
		thenMacAddressesWithNonValidTimestampShouldBe();
	}

	// getDevices timestamp-request (negative):
	@Test
	public void testGetDevicesWithNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetDevicesIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenDevicesWithNonExistingMacAddressShouldBe();
	}
	
	// getDevices current-request (negative):
	@Test
	public void testGetCurrentDevicesWithNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentDevicesIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenCurrentDevicesWithNonExistingMacAddressShouldBe();
	}

	// getIpAddresses timestamp-request (negative):
	@Test
	public void testGetIpAddressesWithNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetIpAddressesIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenGetIpAddressesWithNonExistingMacAddressShouldBe();
	}
	
	// getIpAddresses current-request (negative):
	@Test
	public void testGetCurrentIpAddressesWithNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentIpAddressesIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenGetCurrentIpAddressesWithNonExistingMacAddressShouldBe();
	}
	
	// getUsers timestamp-request (negative):
	@Test
	public void testGetUsersWithNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetUsersIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenGetUsersWithNonExistingMacAddressShouldBe();
	}
	
	// getUsers current-request (negative):
	@Test
	public void testGetCurrentUsersWithNonExistingMacAddress() {
		givenIFMAPGraphWithMacAddresses();
		whenGetCurrentUsersIsCalledWithNonExistingMacAddress(nonExistingMac);
		thenGetCurrentUsersWithNonExistingMacAddressShouldBe();
	}

	// getMacAddresses timestamp-request:
	private void whenGetMacAddressesIsCalledWithTimestamp(long expectedTimestamp) {
		macAddresses = macAddressService.getMacAddresses(RequestType.TIMESTAMP_REQUEST, expectedTimestamp, 0);
	}

	// getMacAddresses timestamp-request:
	private void thenMacAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of MAC-Addresses is correct.", expectedMacAddresses, macAddresses);
	}
	
	// getMacAddresses current-request:
	private void whenGetCurrentMacAddressesIsCalled() {
		currentMacAddresses = macAddressService.getMacAddresses(RequestType.CURRENT_REQUEST, 0, 0);
	}
	
	// getMacAddresses current-request:
	private void thenCurrentMacAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of MAC-Addresses is correct", expectedCurrentMacAddresses, currentMacAddresses);
	}

	// getTimestamp timestamp-request:
	private void whenGetTimestampIsCalledWithMacAddress(MacAddress m) {
		timestamp = macAddressService.getTimestamp(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getTimestamp timestamp-request:
	private void thenTimestampOfMacAddressShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	// getTimestamp current-request:
	private void whenGetTimestampCurrentIsCalledWithMacAddress(MacAddress m) {
		timestamp = macAddressService.getTimestamp(RequestType.CURRENT_REQUEST, m);
	}
	
	// getTimestamp current-request:
	private void thenTimestampOfMacAddressCurrentShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}

	// getIpAddresses timestamp-request:
	private void whenGetIpAddressesIsCalledWithMacAddress(MacAddress m) {
		ipAddresses = macAddressService.getIpAddresses(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getIpAddresses tiemstamp-request:
	private void thenIpAddressesShouldBe() {
		assertEquals("Tests if the returnd ArrayList of ip-addresses is correct.", expectedIpAddresses, ipAddresses);
	}
	
	// getIpAddresses current-request:
	private void whenGetCurrentIpAddressesIsCalledWithMacAddress(MacAddress m) {
		currentIpAddresses = macAddressService.getIpAddresses(RequestType.CURRENT_REQUEST, m);
	}
	
	// getIpAddresses current-request:
	private void thenCurrentIpAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of ip-addresses is correct.", expectedCurrentIpAddresses, currentIpAddresses);
	}

	// getUsers timestamp-request:
	private void whenGetUsersIsCalledWithMacAddress(MacAddress m) {
		users = macAddressService.getUsers(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getUsers timestamp-request:
	private void thenUsersShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", expectedUsers, users);
	}
	
	// getUsers current-request:
	private void whenGetCurrentUsersIsCalledWithMacAddress(MacAddress m) {
		currentUsers = macAddressService.getUsers(RequestType.CURRENT_REQUEST, m);
	}
	
	// getUsers current-request:
	private void thenCurrentUsersShouldBe() {
		assertEquals("Test if the returned ArrayList of users is correct.", expectedCurrentUsers, currentUsers);
	}

	// getDevices timestamp-request:
	private void whenGetDevicesIsCalledWithMacAddress(MacAddress m) {
		devices = macAddressService.getDevices(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getDevices timestamp-request:
	private void thenDevicesShouldBe(){
		assertEquals("Test if the returned ArrayList of devices is correct.", expectedDevices, devices);
	}
	
	// getDevices current-request:
	private void whenGetCurrentDevicesIsCalledWithMacAddress(MacAddress m) {
		currentDevices = macAddressService.getDevices(RequestType.CURRENT_REQUEST, m);
	}
	
	// getDevices current-request:
	private void thenCurrentDevicesShouldBe() {
		assertEquals("Tests if the returned ArrayList of devices is correct.", expectedCurrentDevices, currentDevices);
	}

	// getTimestamp timestamp-request (negative):
	private void whenGetTimestampIsCalledWithNonExistingMacAddress(MacAddress m) {
		timestamp = macAddressService.getTimestamp(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getTimestamp timestamp-request (negative):
	private void thenTimestampOfNonExistingMacAddressShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}
	
	// getTimestamp current-request (negative):
	private void whenGetTimestampCurrentIsCalledWithNonExistingMacAddress(MacAddress m) {
		timestamp = macAddressService.getTimestamp(RequestType.CURRENT_REQUEST, m);
	}
	
	// getTimestamp current-request (negative):
	private void thenTimestampOfNonExistingMacAddressCurrentShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}

	// getMacAddresses timestamp-request (negative):
	private void whenGetMacAddressesIsCalledWithNonValidTimestamp(long nonValidTimestamp) {
		macAddresses = macAddressService.getMacAddresses(RequestType.TIMESTAMP_REQUEST, nonValidTimestamp, 0);
	}

	// getMacAddresses timestamp-request (negative):
	private void thenMacAddressesWithNonValidTimestampShouldBe() {
		assertEquals("Tests if the returned ArrayList of MAC-Addresses is correct.", 0, macAddresses.size());
	}

	// getDevices timestamp-request (negative):
	private void whenGetDevicesIsCalledWithNonExistingMacAddress(MacAddress m) {
		devices = macAddressService.getDevices(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getDevices timestamp-request (negative):
	private void thenDevicesWithNonExistingMacAddressShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", 0, devices.size());
	}
	
	// getDevices current-request (negative):
	private void whenGetCurrentDevicesIsCalledWithNonExistingMacAddress(MacAddress m) {
		currentDevices = macAddressService.getDevices(RequestType.CURRENT_REQUEST, m);
	}
	
	// getDevices current-request (negative):
	private void thenCurrentDevicesWithNonExistingMacAddressShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", 0, currentDevices.size());
	}

	// getIpAddresses timestamp-request (negative):
	private void whenGetIpAddressesIsCalledWithNonExistingMacAddress(MacAddress m) {
		ipAddresses = macAddressService.getIpAddresses(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getIpAddresses timestamp-request (negative):
	private void thenGetIpAddressesWithNonExistingMacAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of ip-addresses is correct.", 0, ipAddresses.size());
	}
	
	// getIpAddresses current-request (negative):
	private void whenGetCurrentIpAddressesIsCalledWithNonExistingMacAddress(MacAddress m) {
		currentIpAddresses = macAddressService.getIpAddresses(RequestType.CURRENT_REQUEST, m);
	}
	
	// getIpAddresses current-request (negative):
	private void thenGetCurrentIpAddressesWithNonExistingMacAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of ip-addresses is correct.", 0, currentIpAddresses.size());
	}

	// getUsers timestamp-request (negative):
	private void whenGetUsersIsCalledWithNonExistingMacAddress(MacAddress m) {
		users = macAddressService.getUsers(RequestType.TIMESTAMP_REQUEST, m);
	}

	// getUsers timestamp-request (negative):
	private void thenGetUsersWithNonExistingMacAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", 0, users.size());
	}
	
	// getUsers current-request (negative):
	private void whenGetCurrentUsersIsCalledWithNonExistingMacAddress(MacAddress m) {
		currentUsers = macAddressService.getUsers(RequestType.CURRENT_REQUEST, m);
	}
	
	// getUsers current-request (negative):
	private void thenGetCurrentUsersWithNonExistingMacAddressShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", 0, currentUsers.size());
	}

	private void givenIFMAPGraphWithMacAddresses() {
		expectedTimestamp = 123456;

		mac = new MacAddress(expectedTimestamp, "aa:bb:cc:dd:ee:ff");
		mac.setSearchTimestamp(expectedTimestamp);
		expectedMacAddresses.add(mac);
		expectedCurrentMacAddresses.add(mac);

		ipAddress = new IpAddress(expectedTimestamp, "192.168.0.1", "IPv4");
		expectedIpAddresses.add(ipAddress);
		expectedCurrentIpAddresses.add(ipAddress);

		user = new User(expectedTimestamp, "Bob", "staff");
		expectedUsers.add(user);
		expectedCurrentUsers.add(user);

		device = new Device(expectedTimestamp, "pc-01", deviceAttributes);
		deviceAttributes.add(deviceAttribute);
		expectedDevices.add(device);
		expectedCurrentDevices.add(device);

		//MAC-Address-Identifier:
		IdentifierImpl id1 = new IdentifierImpl("mac-address");
		id1.addProperty("/mac-address[@value]", mac.getAddress());

		//Access-Request-Identifier:
		IdentifierImpl id2 = new IdentifierImpl("access-request");

		//IP-Address-Identifier:
		IdentifierImpl id3 = new IdentifierImpl("ip-address");
		id3.addProperty("/ip-address[@value]", ipAddress.getAddress());
		id3.addProperty("/ip-address[@type]", ipAddress.getType());

		//User-Identifier:
		IdentifierImpl id4 = new IdentifierImpl("identity");
		id4.addProperty("/identity[@type]", "username");
		id4.addProperty("/identity[@name]", user.getName());

		//Device-Identifier:
		IdentifierImpl device1 = new IdentifierImpl("device");
		device1.addProperty("/device/name", device.getName());
		
		//Link between MAC-Address and Access-Request with access-request-mac-metadata:
		MetadataImpl meta1 = new MetadataImpl("access-request-mac", true, mac.getTimestamp());
		LinkImpl link1 = new LinkImpl(id1, id2);
		link1.addMetadata(meta1);
		id1.addLink(link1);
		id2.addLink(link1);

		//Link between Identity and Access-Request with authenticated-as-metadata:
		MetadataImpl meta2 = new MetadataImpl("authenticated-as", true, user.getTimestamp());
		LinkImpl link2 = new LinkImpl(id4, id2);
		link2.addMetadata(meta2);
		id4.addLink(link2);
		id2.addLink(link2);

		//Link between Identity and Access-equest with role-metadata:
		MetadataImpl meta3 = new MetadataImpl("role", true, user.getTimestamp());
		meta3.addProperty("/meta:role/name", user.getRole());
		LinkImpl link3 = new LinkImpl(id4, id2);
		link3.addMetadata(meta3);
		id4.addLink(link3);
		id2.addLink(link3);

		//Link between IP-Address and Access-Request with access-request-ip-metadata:
		MetadataImpl meta4 = new MetadataImpl("access-request-ip", true, ipAddress.getTimestamp());
		LinkImpl link4 = new LinkImpl(id3, id2);
		link4.addMetadata(meta4);
		id3.addLink(link4);
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
		LinkImpl link5 = new LinkImpl(id1, id3);
		link5.addMetadata(meta5);
		id1.addLink(link5);
		id3.addLink(link5);

		Collection<Identifier> colId = new ArrayList<Identifier>();
		colId.add(id1);

		SemanticsController semCon = Mockito.mock(SemanticsController.class);
		MetalyzerAPI mAPI = Mockito.mock(MetalyzerAPI.class);
		IdentifierFinder identifierFinder = Mockito.mock(IdentifierFinder.class);
		try {
			Mockito.when(identifierFinder.get(StandardIdentifierType.MAC_ADDRESS, mac.getTimestamp())).thenReturn(colId);
			Mockito.when(identifierFinder.getCurrent(StandardIdentifierType.MAC_ADDRESS)).thenReturn(colId);
		} catch (MetalyzerAPIException e) {
			fail("Exception was thrown. This should not happen!" + e.getMessage());
		}
		Mockito.when(mAPI.getIdentifierFinder()).thenReturn(identifierFinder);
		Mockito.when(semCon.getConnection()).thenReturn(mAPI);
		macAddressService = new MacAddressService(semCon);
	}
}
