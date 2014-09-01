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
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.metalyzer.semantics.services.UserService;
import de.hshannover.f4.trust.visitmeta.implementations.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.implementations.LinkImpl;
import de.hshannover.f4.trust.visitmeta.implementations.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class UserServiceTest {
	private UserService userService;
	private User user;
	private User nonExistingUser = new User(123456, "NN", "staff");
	private IpAddress ipAddress;
	private MacAddress macAddress;
	private Device device;
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
	private ArrayList<User> currentUsers = new ArrayList<User>();
	private ArrayList<User> expectedCurrentUsers = new ArrayList<User>();
	private ArrayList<IpAddress> currentIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<IpAddress> expectedCurrentIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<MacAddress> currentMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> expectedCurrentMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<Device> currentDevices = new ArrayList<Device>();
	private ArrayList<Device> expectedCurrentDevices = new ArrayList<Device>();
	
	// getUsers timestamp-request:
	@Test
	public void testGetUsers() {
		givenIFMAPGraphWithUsers();
		whenGetUsersIsCalledWithTimestamp(expectedTimestamp);
		thenUsersShouldBe();
	}
	
	// getUsers current-request:
	@Test
	public void testGetCurrentUsers() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentUsersIsCalled();
		thenCurrentUsersShouldBe();
	}
	
	// getTimestamp timestamp-request:
	@Test
	public void testGetTimestampOfExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetTimestampIsCalledWithUser(user);
		thenTimestampOfUserShouldBe();
	}
	
	// getTimestamp current-request:
	@Test
	public void testGetTimestampOfExistingUserCurrent() {
		givenIFMAPGraphWithUsers();
		whenGetTimestampCurrentIsCalledWithUser(user);
		thenTimestampOfUserCurrentShouldBe();
	}
	
	// getIpAddresses timestamp-request:
	@Test
	public void testGetIpAddresses() {
		givenIFMAPGraphWithUsers();
		whenGetIpAddressesIsCalledWithUser(user);
		thenIpAddressesShouldBe();
	}
	
	// getIpAddresses current-request:
	@Test
	public void testGetCurrentIpAddresses() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentIpAddressesIsCalledWithUser(user);
		thenCurrentIpAddressesShouldBe();
	}
	
	// getMacAddresses timestamp-request:
	@Test
	public void testGetMacAddresses() {
		givenIFMAPGraphWithUsers();
		whenGetMacAddressesIsCalledWithUser(user);
		thenMacAddressesShouldBe();
	}
	
	// getMacAddresses current-request:
	@Test
	public void testGetCurrentMacAddresses() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentMacAddressesIsCalledWithUser(user);
		thenCurrentMacAddressesShouldBe();
	}
	
	// getDevices timestamp-request:
	@Test
	public void testGetDevices() {
		givenIFMAPGraphWithUsers();
		whenGetDevicesIsCalledWithUser(user);
		thenDevicesShouldBe();
	}
	
	// getDevices current-request:
	@Test
	public void testGetCurrentDevices() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentDevicesIsCalledWithUser(user);
		thenCurrentDevicesShouldBe();
	}
	
	
	// getTimestamp timestamp-request (negative):
	@Test
	public void testGetTimestampOfNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetTimestampIsCalledWithNonExistingUser(nonExistingUser);
		thenTimestampOfNonExistingUserShouldBe();
	}
	
	// getTimestamp current-request (negative):
	@Test
	public void testGetTimestampOfNonExistingUserCurrent() {
		givenIFMAPGraphWithUsers();
		whenGetTimestampIsCalledWithNonExistingUserCurrent(nonExistingUser);
		thenTimestampOfNonExistingUserCurrentShouldBe();
	}
	
	// getUsers timestamp-request (negative):
	@Test
	public void testGetUsersWithNonValidTimestamp() {
		givenIFMAPGraphWithUsers();
		whenGetUsersIsCalledWithNonValidTimestamp(nonValidTimestamp);
		thenUsersWithNonValidTimestampShouldBe();
	}
	
	// getDevices timestamp-request (negative):
	@Test
	public void testGetDevicesWithNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetDevicesIsCalledWithNonExistingUser(nonExistingUser);
		thenDevicesWithNonExistingUserShouldBe();
	}
	
	// getDevices current-request (negative):
	@Test
	public void testGetCurrentDevicesWithNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentDevicesIsCalledWithNonExistingUser(nonExistingUser);
		thenCurrentDevicesWithNonExistingUserShouldBe();
	}
	
	// getIpAddresses timestamp-request (negative):
	@Test
	public void testGetIpAddressesWithNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetIpAddressesIsCalledWithNonExistingUser(nonExistingUser);
		thenGetIpAddressesWithNonExistingUserShouldBe();
	}
	
	// getIpAddresses current-request (negative):
	@Test
	public void testGetCurrentIpAddressesWithNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentIpAddressesIsCalledWithNonExistingUser(nonExistingUser);
		thenGetCurrentIpAddressesWithNonExistingUserShouldBe();
	}
	
	
	// getMacAddresses timestamp-request (negative):
	@Test
	public void testGetMacAddressesWithNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetMacAddressesIsCalledWithNonExistingUser(nonExistingUser);
		thenGetMacAddressesWithNonExistingUserShouldBe();
	}
	
	// getMacAddresses current-request (negative):
	@Test
	public void testGetCurrentMacAddressesWithNonExistingUser() {
		givenIFMAPGraphWithUsers();
		whenGetCurrentMacAddressesIsCalledWithNonExistingUser(nonExistingUser);
		thenGetCurrentMacAddressesWithNonExistingUserShouldBe();
	}
	
	// getUsers timestamp-request:
	private void whenGetUsersIsCalledWithTimestamp(long expectedTimestamp) {
		users = userService.getUsers(RequestType.TIMESTAMP_REQUEST, expectedTimestamp, 0);
	}
	
	// getUsers timestamp-request:
	private void thenUsersShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", expectedUsers, users);
	}
	
	// getUsers current-request:
	private void whenGetCurrentUsersIsCalled() {
		currentUsers = userService.getUsers(RequestType.CURRENT_REQUEST, 0, 0);
	}
	
	// getUsers current-request:
	private void thenCurrentUsersShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", expectedCurrentUsers, currentUsers);
	}
	
	// getTimestamp timestamp-request:
	private void whenGetTimestampIsCalledWithUser(User u) {
		timestamp = userService.getTimestamp(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getTimestamp timestamp-request:
	private void thenTimestampOfUserShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	// getTimestamp current-request:
	private void whenGetTimestampCurrentIsCalledWithUser(User u) {
		timestamp = userService.getTimestamp(RequestType.CURRENT_REQUEST, u);
	}
	
	// getTimestamp current-request:
	private void thenTimestampOfUserCurrentShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	// getTimestamp timestamp-request (negative):
	private void whenGetTimestampIsCalledWithNonExistingUser(User u) {
		timestamp = userService.getTimestamp(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// // getTimestamp timestamp-request (negative):
	private void thenTimestampOfNonExistingUserShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}
	
	// getTimestamp current-request (negative):
	private void whenGetTimestampIsCalledWithNonExistingUserCurrent(User u) {
		timestamp = userService.getTimestamp(RequestType.CURRENT_REQUEST, u);
	}
	
	// getTimestamp current-request (negative):
	private void thenTimestampOfNonExistingUserCurrentShouldBe() {
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}

	// getUsers timestamp-request (negative):
	private void whenGetUsersIsCalledWithNonValidTimestamp(long nonValidTimestamp) {
		users = userService.getUsers(RequestType.TIMESTAMP_REQUEST, nonValidTimestamp, 0);
	}

	// getUsers timestamp-request (negative):
	private void thenUsersWithNonValidTimestampShouldBe() {
		assertEquals("Tests if the returned ArrayList of users is correct.", 0, users.size());
	}
	
	// getIpAddresses timestamp-request:
	private void whenGetIpAddressesIsCalledWithUser(User u) {
		ipAddresses = userService.getIpAddresses(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getIpAddresses timestamp-request:
	private void thenIpAddressesShouldBe() {
		assertEquals("Tests if the returnd ArrayList of ip-addresses is correct.", expectedIpAddresses, ipAddresses);
	}
	
	// getIpAddresses current-request:
	private void whenGetCurrentIpAddressesIsCalledWithUser(User u) {
		currentIpAddresses = userService.getIpAddresses(RequestType.CURRENT_REQUEST, u);
	}
	
	// getIpAddresses current-request:
	private void thenCurrentIpAddressesShouldBe() {
		assertEquals("Tests if the returnd ArrayList of ip-addresses is correct.", expectedCurrentIpAddresses, currentIpAddresses);
	}
	
	// getMacAddresses timestamp-request:
	private void whenGetMacAddressesIsCalledWithUser(User u) {
		macAddresses = userService.getMacAddresses(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getMacAddresses timestamp-request:
	private void thenMacAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", expectedMacAddresses, macAddresses);
	}
	
	// getMacAddresses current-request:
	private void whenGetCurrentMacAddressesIsCalledWithUser(User u) {
		currentMacAddresses = userService.getMacAddresses(RequestType.CURRENT_REQUEST, u);
	}
	
	// getMacAddresses current-request:
	private void thenCurrentMacAddressesShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", expectedCurrentMacAddresses, currentMacAddresses);
	}
	
	// getDevices timestamp-request:
	private void whenGetDevicesIsCalledWithUser(User u) {
		devices= userService.getDevices(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getDevices timestamp-request:
	private void thenDevicesShouldBe(){
		assertEquals("Test if the returned ArrayList of devices is correct.", expectedDevices, devices);
	}
	
	// getDevices current-request:
	private void whenGetCurrentDevicesIsCalledWithUser(User u) {
		currentDevices = userService.getDevices(RequestType.CURRENT_REQUEST, u);
	}
	
	// getDevices current-request:
	private void thenCurrentDevicesShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", expectedCurrentDevices, currentDevices);
	}
	
	// getDevices timestamp-request (negative):
	private void whenGetDevicesIsCalledWithNonExistingUser(User u) {
		devices= userService.getDevices(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getDevices timestamp-request (negative):
	private void thenDevicesWithNonExistingUserShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", 0, devices.size());
	}
	
	// getDevices current-request (negative):
	private void whenGetCurrentDevicesIsCalledWithNonExistingUser(User u) {
		currentDevices = userService.getDevices(RequestType.CURRENT_REQUEST, u);
	}
	
	// getDevices current-request (negative):
	private void thenCurrentDevicesWithNonExistingUserShouldBe() {
		assertEquals("Test if the returned ArrayList of devices is correct.", 0, currentDevices.size());
	}
	
	// getIpAddresses timestamp-request (negative):
	private void whenGetIpAddressesIsCalledWithNonExistingUser(User u) {
		ipAddresses = userService.getIpAddresses(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getIpAddresses timestamp-request (negative):
	private void thenGetIpAddressesWithNonExistingUserShouldBe() {
		assertEquals("Tests if the returned ArrayList of ip-addresses is correct.", 0, ipAddresses.size());
	}
	
	// getIpAddresses current-request (negative):
	private void whenGetCurrentIpAddressesIsCalledWithNonExistingUser(User u) {
		currentIpAddresses = userService.getIpAddresses(RequestType.CURRENT_REQUEST, u);
	}
	
	// getIpAddresses current-request (negative):
	private void thenGetCurrentIpAddressesWithNonExistingUserShouldBe() {
		assertEquals("Tests if the returned ArrayList of ip-addresses is correct.", 0, currentIpAddresses.size());
	}
	
	// getMacAddresses timestamp-request (negative):
	private void whenGetMacAddressesIsCalledWithNonExistingUser(User u) {
		macAddresses = userService.getMacAddresses(RequestType.TIMESTAMP_REQUEST, u);
	}
	
	// getMacAddresses timestamp-request (negative):
	private void thenGetMacAddressesWithNonExistingUserShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", 0, macAddresses.size());
	}
	
	// getMacAddresses current-request (negative):
	private void whenGetCurrentMacAddressesIsCalledWithNonExistingUser(User u) {
		currentMacAddresses = userService.getMacAddresses(RequestType.CURRENT_REQUEST, u);
	}
	
	// getMacAddresses current-request (negative):
	private void thenGetCurrentMacAddressesWithNonExistingUserShouldBe() {
		assertEquals("Tests if the returned ArrayList of mac-addresses is correct.", 0, currentMacAddresses.size());
	}
	
	private void givenIFMAPGraphWithUsers() {
		expectedTimestamp = 123456;
		
		user = new User(expectedTimestamp, "Bob", "staff");
		user.setSearchTimestamp(expectedTimestamp);
		expectedUsers.add(user);
		expectedCurrentUsers.add(user);
		
		ipAddress = new IpAddress(expectedTimestamp, "192.168.0.1", "IPv4");
		expectedIpAddresses.add(ipAddress);
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
		
		Collection<Identifier> colId = new ArrayList<Identifier>();
		colId.add(id1);
		
		SemanticsController semCon = Mockito.mock(SemanticsController.class);
		MetalyzerAPI mAPI = Mockito.mock(MetalyzerAPI.class);
		IdentifierFinder identifierFinder = Mockito.mock(IdentifierFinder.class);
		try {
			Mockito.when(identifierFinder.get(StandardIdentifierType.IDENTITY, user.getTimestamp())).thenReturn(colId);
			Mockito.when(identifierFinder.getCurrent(StandardIdentifierType.IDENTITY)).thenReturn(colId);
		} catch (MetalyzerAPIException e) {
			fail("Exception was thrown. This should not happen!" + e.getMessage());
		}
		Mockito.when(mAPI.getIdentifierFinder()).thenReturn(identifierFinder);
		Mockito.when(semCon.getAPI()).thenReturn(mAPI);
		userService = new UserService(semCon);
	}
}
