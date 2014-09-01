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
import de.hshannover.f4.trust.metalyzer.semantics.services.DeviceService;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController.RequestType;
import de.hshannover.f4.trust.visitmeta.implementations.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.implementations.LinkImpl;
import de.hshannover.f4.trust.visitmeta.implementations.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class DeviceServiceTest {
	private DeviceService deviceService;
	private Device device;
	private Device device2;
	private Device nonExistingDevice= new Device(0, "router01");
	private String deviceAttribute= "low-disc-space";
	private ArrayList<Device> expectedDevices= new ArrayList<Device>();
	private ArrayList<String> deviceAttributes= new ArrayList<String>();
	private IpAddress ipAddress;
	private IpAddress ipAddress2;
	private MacAddress macAddress;
	private MacAddress pdpMacAddress;
	private User user;
	private long timestamp;
	private long expectedTimestamp;
	private long nonValidTimestamp = 0; // 01.01.1970 00:00:00
	private ArrayList<Device> devices= new ArrayList<Device>();
	private ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> pdpMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> expectedMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<MacAddress> expectedPdpMacAddresses = new ArrayList<MacAddress>();
	private ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
	private ArrayList<IpAddress> expectedPCIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<IpAddress> expectedAuthIpAddresses = new ArrayList<IpAddress>();
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<User> expectedUsers = new ArrayList<User>();

	/*
	 * Timestamp
	 */

	/**
	 * test getDevices() from DeviceService
	 */
	@Test
	public void testGetDevices(){
		givenIFMAPGraphWithDevices();
		whenGetDevicesIsCalledWithTimestamp(expectedTimestamp);
		thenDevicesShouldBe();
	}

	/**
	 * test getTimestamp() from DeviceService
	 */
	@Test
	public void testGetTimestamp(){
		givenIFMAPGraphWithDevices();
		whenGetTimestampIsCalledWithDevice();
		thenTimestampShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService with an authentication Device (PDP)
	 */
	@Test
	public void testGetIpAddressesForAuthDevice(){
		givenIFMAPGraphWithDevices();
		whenGetIpAddressesIsCalledWithAuthDevice();
		thenAuthDeviceIpAddressesShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService with an regular Device (PC)
	 */
	@Test
	public void testGetIpAddressesForPCDevice(){
		givenIFMAPGraphWithDevices();
		whenGetIpAddressesIsCalledWithPCDevice();
		thenPCDeviceIpAddressesShouldBe();
	}
	
	/**
	 * test getMacAddresses() from DeviceService with an regular Device (PC)
	 */
	@Test
	public void testGetMacAddresses(){
		givenIFMAPGraphWithDevices();
		whenGetMacAddressesIsCalledWithDevice();
		thenMacAddressesShouldBe();
	}
	
	/**
	 * test getMacAddresses() from DeviceService with an authentication Device (PDP)
	 */
	@Test
	public void testGetMacAddressesForPdpDevice(){
		givenIFMAPGraphWithDevices();
		whenGetMacAddressesIsCalledWithPdpDevice();
		thenPdpMacAddressesShouldBe();
	}
	
	/**
	 * test getUsers() from DeviceService with an authentication Device (PDP)
	 */
	@Test
	public void testGetUsers(){
		givenIFMAPGraphWithDevices();
		whenGetUsersIsCalledWithDevice();
		thenUsersShouldBe();
	}
	
	/**
	 * test getDevices() from DeviceService with a non valid timestamp (negative test)
	 */
	@Test
	public void testGetDevicesWithNonValidTimestamp(){
		givenIFMAPGraphWithDevices();
		whenGetDevicesIsCalledWithNonValidTimestamp();
		thenDevicesWithNonValidTimestampShouldBe();
	}
	
	/**
	 * test getTimstamp() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetTimestmpWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetTimestampIsCalledWithNonExistingDevice();
		thenTimestampWithNonExistingDeviceShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetIpAddressesWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetIpAddressesIsCalledWithNonExistingDevice();
		thenIpAddressesWithNonExistingDeviceShouldBe();
	}
	
	/**
	 * test getMacAddresses() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetMacAddressesWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetMacAddressesIsCalledWithNonExistingDevice();
		thenMacAddressesWithNonExistingDeviceShouldBe();
	}
	
	/**
	 * test getUsers() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetUsersWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetUsersIsCalledWithNonExistingDevice();
		thenUsersWithNonExistingDeviceShouldBe();
	}
	
	/*
	 * End Timestamp
	 */
	
/*
 * Current
 */
	/**
	 * test getDevices() from DeviceService at current timestamp
	 */
	@Test
	public void testGetCurrentDevices(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentDevicesIsCalled();
		thenCurrentDevicesShouldBe();
	}

	/**
	 * test getTimestamp() from DeviceService at current timestamp
	 */
	@Test
	public void testGetCurrentTimestamp(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentTimestampIsCalledWithDevice();
		thenCurrentTimestampShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService at current timestamp with an authentication device (PDP)
	 */
	@Test
	public void testGetCurrentIpAddressesForAuthDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentIpAddressesIsCalledWithAuthDevice();
		thenCurrentAuthDeviceIpAddressesShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService at current timestamp with a regular device (PC)
	 */
	@Test
	public void testGetCurrentIpAddressesForPCDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentIpAddressesIsCalledWithPCDevice();
		thenCurrentPCDeviceIpAddressesShouldBe();
	}
	
	/**
	 * test getMacAddresses() from DeviceService at current timestamp with a authentication device (PDP)
	 */
	@Test
	public void testGetCurrentMacAddresses(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentMacAddressesIsCalledWithDevice();
		thenCurrentMacAddressesShouldBe();
	}
	
	/**
	 * test getMacAddresses() from DeviceService at current timestamp with a regular device (PC)
	 */
	@Test
	public void testGetCurrentMacAddressesForPdpDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentMacAddressesIsCalledWithPdpDevice();
		thenCurrentPdpMacAddressesShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService at current timestamp with a authentication device (PDP)
	 */
	@Test
	public void testGetCurrentUsers(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentUsersIsCalledWithDevice();
		thenCurrentUsersShouldBe();
	}
	
	/**
	 * test getTimestamp() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetCurrentTimestmpWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentTimestampIsCalledWithNonExistingDevice();
		thenCurrentTimestampWithNonExistingDeviceShouldBe();
	}
	
	/**
	 * test getIpAddresses() from DeviceService with a non valid device (negative test)
	 */	
	@Test
	public void testGetCurrentIpAddressesWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentIpAddressesIsCalledWithNonExistingDevice();
		thenCurrentIpAddressesWithNonExistingDeviceShouldBe();
	}
	
	/**
	 * test getMacAddresses() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetCurrentMacAddressesWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentMacAddressesIsCalledWithNonExistingDevice();
		thenCurrentMacAddressesWithNonExistingDeviceShouldBe();
	}
	
	/**
	 * test getUsers() from DeviceService with a non valid device (negative test)
	 */
	@Test
	public void testGetCurrentUsersWithNonExistingDevice(){
		givenIFMAPGraphWithDevices();
		whenGetCurrentUsersIsCalledWithNonExistingDevice();
		thenCurrentUsersWithNonExistingDeviceShouldBe();
	}

/*
 * End Current
*/
	/*
	 * Timestamp when and then Methods
	 */
	private void whenGetUsersIsCalledWithNonExistingDevice(){
		users= deviceService.getUsers(RequestType.TIMESTAMP_REQUEST,nonExistingDevice);
	}
	
	private void thenUsersWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned list of users is correct.", 0, users.size());
	}
	
	private void whenGetMacAddressesIsCalledWithNonExistingDevice(){
		macAddresses= deviceService.getMacAddresses(RequestType.TIMESTAMP_REQUEST,nonExistingDevice);
	}
	
	private void thenMacAddressesWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned list of MAC-Addresses is correct.", 0, macAddresses.size());
	}
	
	private void whenGetIpAddressesIsCalledWithNonExistingDevice(){
		ipAddresses= deviceService.getIpAddresses(RequestType.TIMESTAMP_REQUEST,nonExistingDevice);
	}
	
	private void thenIpAddressesWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned list of IP-Addresses is correct.", 0, ipAddresses.size());
	}
	
	private void whenGetTimestampIsCalledWithNonExistingDevice(){
		timestamp= deviceService.getTimestamp(RequestType.TIMESTAMP_REQUEST,nonExistingDevice);
	}
	
	private void thenTimestampWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}
	
	private void whenGetDevicesIsCalledWithNonValidTimestamp(){
		devices= deviceService.getDevices(RequestType.TIMESTAMP_REQUEST,nonValidTimestamp, 0);
	}
	
	private void thenDevicesWithNonValidTimestampShouldBe(){
		assertEquals("Tests if the returned list of devices is correct.", 0, devices.size());
	}
	
	private void whenGetUsersIsCalledWithDevice(){
		users= deviceService.getUsers(RequestType.TIMESTAMP_REQUEST,device);
	}
	
	private void thenUsersShouldBe(){
		assertEquals("Tests if the returned list of users is correct.", expectedUsers, users);
	}
	
	private void whenGetMacAddressesIsCalledWithDevice(){
		macAddresses= deviceService.getMacAddresses(RequestType.TIMESTAMP_REQUEST,device2);
	}
	
	private void whenGetMacAddressesIsCalledWithPdpDevice(){
		pdpMacAddresses= deviceService.getMacAddresses(RequestType.TIMESTAMP_REQUEST,device);
	}
	
	private void thenMacAddressesShouldBe(){
		assertEquals("Tests if the returned list of MAC-Addresses is correct.", expectedMacAddresses, macAddresses);
	}
	
	private void thenPdpMacAddressesShouldBe(){
		assertEquals("Tests if the returned list of MAC-Addresses is correct.", expectedPdpMacAddresses, pdpMacAddresses);
	}
	
	private void whenGetIpAddressesIsCalledWithAuthDevice(){
		ipAddresses= deviceService.getIpAddresses(RequestType.TIMESTAMP_REQUEST,device);
	}
	
	private void thenAuthDeviceIpAddressesShouldBe(){
		assertEquals("Tests if the returned list of IP-Addresses is correct.", expectedAuthIpAddresses, ipAddresses);
	}
	
	private void whenGetIpAddressesIsCalledWithPCDevice(){
		ipAddresses= deviceService.getIpAddresses(RequestType.TIMESTAMP_REQUEST,device2);
	}
	
	private void thenPCDeviceIpAddressesShouldBe(){
		assertEquals("Tests if the returned list of IP-Addresses is correct.", expectedPCIpAddresses, ipAddresses);
	}
	
	private void whenGetDevicesIsCalledWithTimestamp(long expectedTimestamp){
		devices= deviceService.getDevices(RequestType.TIMESTAMP_REQUEST, expectedTimestamp, 0);
	}
	
	private void thenDevicesShouldBe(){
		assertEquals("Tests if the returned list of devices is correct.", expectedDevices, devices);
	}
	
	
	private void whenGetTimestampIsCalledWithDevice(){
		timestamp= deviceService.getTimestamp(RequestType.TIMESTAMP_REQUEST,device);
	}
	
	private void thenTimestampShouldBe(){
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	/*
	 * End Timestamp when and then Methods
	 */
	
	/*
	 * Current when and then Methods
	 */
	private void whenGetCurrentUsersIsCalledWithNonExistingDevice(){
		users= deviceService.getUsers(RequestType.CURRENT_REQUEST,nonExistingDevice);
	}
	
	private void thenCurrentUsersWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned list of users is correct.", 0, users.size());
	}
	
	private void whenGetCurrentMacAddressesIsCalledWithNonExistingDevice(){
		macAddresses= deviceService.getMacAddresses(RequestType.CURRENT_REQUEST,nonExistingDevice);
	}
	
	private void thenCurrentMacAddressesWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned list of MAC-Addresses is correct.", 0, macAddresses.size());
	}
	
	private void whenGetCurrentIpAddressesIsCalledWithNonExistingDevice(){
		ipAddresses= deviceService.getIpAddresses(RequestType.CURRENT_REQUEST,nonExistingDevice);
	}
	
	private void thenCurrentIpAddressesWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned list of IP-Addresses is correct.", 0, ipAddresses.size());
	}
	
	private void whenGetCurrentTimestampIsCalledWithNonExistingDevice(){
		timestamp= deviceService.getTimestamp(RequestType.CURRENT_REQUEST,nonExistingDevice);
	}
	
	private void thenCurrentTimestampWithNonExistingDeviceShouldBe(){
		assertEquals("Tests if the returned timestamp is correct.", 0, timestamp);
	}
		
	private void thenCurrentDevicesWithNonValidTimestampShouldBe(){
		assertEquals("Tests if the returned list of devices is correct.", 0, devices.size());
	}
	
	private void whenGetCurrentUsersIsCalledWithDevice(){
		users= deviceService.getUsers(RequestType.CURRENT_REQUEST,device);
	}
	
	private void thenCurrentUsersShouldBe(){
		assertEquals("Tests if the returned list of users is correct.", expectedUsers, users);
	}
	
	private void whenGetCurrentMacAddressesIsCalledWithDevice(){
		macAddresses= deviceService.getMacAddresses(RequestType.CURRENT_REQUEST,device2);
	}
	
	private void whenGetCurrentMacAddressesIsCalledWithPdpDevice(){
		pdpMacAddresses= deviceService.getMacAddresses(RequestType.CURRENT_REQUEST,device);
	}
	
	private void thenCurrentMacAddressesShouldBe(){
		assertEquals("Tests if the returned list of MAC-Addresses is correct.", expectedMacAddresses, macAddresses);
	}
	
	private void thenCurrentPdpMacAddressesShouldBe(){
		assertEquals("Tests if the returned list of MAC-Addresses is correct.", expectedPdpMacAddresses, pdpMacAddresses);
	}
	
	private void whenGetCurrentIpAddressesIsCalledWithAuthDevice(){
		ipAddresses= deviceService.getIpAddresses(RequestType.CURRENT_REQUEST,device);
	}
	
	private void thenCurrentAuthDeviceIpAddressesShouldBe(){
		assertEquals("Tests if the returned list of IP-Addresses is correct.", expectedAuthIpAddresses, ipAddresses);
	}
	
	private void whenGetCurrentIpAddressesIsCalledWithPCDevice(){
		ipAddresses= deviceService.getIpAddresses(RequestType.CURRENT_REQUEST,device2);
	}
	
	private void thenCurrentPCDeviceIpAddressesShouldBe(){
		assertEquals("Tests if the returned list of IP-Addresses is correct.", expectedPCIpAddresses, ipAddresses);
	}
	
	private void whenGetCurrentDevicesIsCalled(){
		devices= deviceService.getDevices(RequestType.CURRENT_REQUEST, expectedTimestamp, 0);
	}
	
	private void thenCurrentDevicesShouldBe(){
		assertEquals("Tests if the returned list of devices is correct.", expectedDevices, devices);
	}
	
	
	private void whenGetCurrentTimestampIsCalledWithDevice(){
		timestamp= deviceService.getTimestamp(RequestType.CURRENT_REQUEST,device);
	}
	
	private void thenCurrentTimestampShouldBe(){
		assertEquals("Tests if the returned timestamp is correct.", expectedTimestamp, timestamp);
	}
	
	/*
	 * End Current when and then Methods
	 */

	private void givenIFMAPGraphWithDevices(){
		expectedTimestamp= 123456;

		deviceAttributes.add(deviceAttribute);
		
		// access-request-device
		device= new Device(expectedTimestamp, "pdp-1");
		device.setSearchTimestamp(expectedTimestamp);
		expectedDevices.add(device);
		
		// authenticated-by-device
		device2= new Device(expectedTimestamp,"pc-01",deviceAttributes);
		device2.setSearchTimestamp(expectedTimestamp);
		expectedDevices.add(device2);
		
		// access-request MAC
		macAddress= new MacAddress(expectedTimestamp, "aa:bb:cc:dd:ee:ff");
		expectedMacAddresses.add(macAddress);
		
		pdpMacAddress= new MacAddress(expectedTimestamp, "11:22:33:44:55:66");
		expectedPdpMacAddresses.add(pdpMacAddress);
		
		// access-request IP
		ipAddress= new IpAddress(expectedTimestamp, "192.168.0.1", "IPv4");
		expectedPCIpAddresses.add(ipAddress);
				
		// authenticated-by / device-ip IP
		ipAddress2= new IpAddress(expectedTimestamp, "192.168.0.20", "IPv4");
		expectedAuthIpAddresses.add(ipAddress2);

		user= new User(expectedTimestamp, "Bob", "staff");
		expectedUsers.add(user);

		//Device Identifier (authenticated-by)
		IdentifierImpl pdpDeviceId= new IdentifierImpl("device");
		pdpDeviceId.addProperty("/device/name", device.getName());
		
		//Device Identifier (access-request-device)
		IdentifierImpl deviceId2= new IdentifierImpl("device");
		deviceId2.addProperty("/device/name", device2.getName());

		//access-request
		IdentifierImpl accessId= new IdentifierImpl("access-request");

		//IP-Address Identifier (access-request-ip)
		IdentifierImpl ipId= new IdentifierImpl("ip-address");
		ipId.addProperty("/ip-address[@value]", ipAddress.getAddress());
		ipId.addProperty("/ip-address[@type]", ipAddress.getType());
		
		// IP-Address Identifier (device-ip)
		IdentifierImpl pdpDeviceIpId= new IdentifierImpl("ip-address");
		pdpDeviceIpId.addProperty("/ip-address[@value]", ipAddress2.getAddress());
		pdpDeviceIpId.addProperty("/ip-address[@type]", ipAddress2.getType());
		
		//MAC-Address Identifier (access-request-mac)
		IdentifierImpl macId= new IdentifierImpl("mac-address");
		macId.addProperty("/mac-address[@value]", macAddress.getAddress());
		
		//MAC-Address Identifier (pdp Mac (ip-mac))
		IdentifierImpl pdpMacId= new IdentifierImpl("mac-address");
		pdpMacId.addProperty("/mac-address[@value]", pdpMacAddress.getAddress());

		
		// User-Identifier:
		IdentifierImpl userId = new IdentifierImpl("identity");
		userId.addProperty("/identity[@type]", "username");
		userId.addProperty("/identity[@name]", user.getName());
		
		// Link between authenticated-by-device and access-request
		
		// Metadata
		MetadataImpl authByMeta= new MetadataImpl("authenticated-by", true, device.getTimestamp());
		
		// Link
		LinkImpl authLink= new LinkImpl(pdpDeviceId, accessId);
		authLink.addMetadata(authByMeta);
		pdpDeviceId.addLink(authLink);
		accessId.addLink(authLink);
		
		// Link between authenticated-by-device and device-ip

		// Metadata
		MetadataImpl devIpMeta= new MetadataImpl("device-ip", true,device.getTimestamp());
		
		// Link
		LinkImpl devIpLink= new LinkImpl(pdpDeviceId, pdpDeviceIpId);
		devIpLink.addMetadata(devIpMeta);
		pdpDeviceId.addLink(devIpLink);
		pdpDeviceIpId.addLink(devIpLink);
		
		// Link between ip-address (device-ip) and  pdpMacAdress (ip-mac)
		
		// Metadata
		MetadataImpl pdpMacMeta= new MetadataImpl("ip-mac", true, pdpMacAddress.getTimestamp());
		
		// Link
		LinkImpl pdpMacLink= new LinkImpl(pdpDeviceIpId, pdpMacId);
		pdpMacLink.addMetadata(pdpMacMeta);
		pdpDeviceIpId.addLink(pdpMacLink);
		pdpMacId.addLink(pdpMacLink);
		
		
		// Link between access-request and access-request-device

		// Metadata
		MetadataImpl accReqDevMeta= new MetadataImpl("access-request-device", true,device2.getTimestamp());
		MetadataImpl device2AttributeMeta= new MetadataImpl("device-attribute", true,device2.getTimestamp());
		device2AttributeMeta.addProperty("/meta:device-attribute/name", deviceAttribute);
		
		// Link
		LinkImpl accReqLink= new LinkImpl(deviceId2,accessId);
		accReqLink.addMetadata(accReqDevMeta);
		accReqLink.addMetadata(device2AttributeMeta);
		accessId.addLink(accReqLink);
		deviceId2.addLink(accReqLink);
		
		// Link between IP-Address and access-request (access-request-ip)
		
		// Metadata
		MetadataImpl accReqIpMeta= new MetadataImpl("access-request-ip", true,ipAddress.getTimestamp());
		
		// Link
		LinkImpl accReqIpLink= new LinkImpl(ipId, accessId);
		accReqIpLink.addMetadata(accReqIpMeta);
		ipId.addLink(accReqIpLink);
		accessId.addLink(accReqIpLink);
		
		// Link between MAC-Address and access-request (access-request-mac)
		
		// Metadata
		MetadataImpl accReqMacMeta= new MetadataImpl("access-request-mac", true, macAddress.getTimestamp());
		
		// Link
		LinkImpl accReqMacLink= new LinkImpl(macId,accessId);
		accReqMacLink.addMetadata(accReqMacMeta);
		macId.addLink(accReqMacLink);
		accessId.addLink(accReqMacLink);
		
		// Link between IP-Address and MAC-Address (ip-mac)
		
		// Metadata
		MetadataImpl ipMacMeta= new MetadataImpl("ip-mac", true, ipAddress.getTimestamp());
		
		// Link
		LinkImpl ipMacLink= new LinkImpl(ipId, macId);
		ipMacLink.addMetadata(ipMacMeta);
		ipId.addLink(ipMacLink);
		macId.addLink(ipMacLink);
		
		// Link between identitity (user) and access-request
		
		// Metadata
		MetadataImpl authAsMeta= new MetadataImpl("authenticated-as", true, user.getTimestamp());
		MetadataImpl roleMeta= new MetadataImpl("role", true, user.getTimestamp());
		roleMeta.addProperty("/meta:role/name", user.getRole());
		
		
		// Link
		LinkImpl userLink= new LinkImpl(accessId, userId);
		userLink.addMetadata(authAsMeta);
		userLink.addMetadata(roleMeta);
		accessId.addLink(userLink);
		userId.addLink(userLink);
		
		
		Collection<Identifier> deviceList = new ArrayList<Identifier>();
		deviceList.add(pdpDeviceId);
		deviceList.add(deviceId2);

		SemanticsController semCon = Mockito.mock(SemanticsController.class);
		MetalyzerAPI mAPI = Mockito.mock(MetalyzerAPI.class);
		IdentifierFinder identifierFinder = Mockito.mock(IdentifierFinder.class);
		try {
			Mockito.when(identifierFinder.get(StandardIdentifierType.DEVICE, device.getTimestamp())).thenReturn(deviceList);
			Mockito.when(identifierFinder.getCurrent(StandardIdentifierType.DEVICE)).thenReturn(deviceList);
		} catch (MetalyzerAPIException e) {
			fail("Exception was thrown. This should not happen!" + e.getMessage());
		}
		Mockito.when(mAPI.getIdentifierFinder()).thenReturn(identifierFinder);
		Mockito.when(semCon.getAPI()).thenReturn(mAPI);
		deviceService = new DeviceService(semCon);
	}
}
