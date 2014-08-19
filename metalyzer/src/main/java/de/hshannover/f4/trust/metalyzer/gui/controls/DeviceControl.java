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
 * This file is part of visitmeta metalyzer, version 0.0.1,
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
package de.hshannover.f4.trust.metalyzer.gui.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.DevicePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.SemanticPanel;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.gui.misc.UtilHelper;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView;
import de.hshannover.f4.trust.metalyzer.gui.views.TimeSelectionView.TimeSelection;
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class DeviceControl extends SemanticControl {

	private static final Logger log = Logger.getLogger(DeviceControl.class);
	private DevicePanel mDevicePanel;
	private long mTimestampFrom = 0;
	private long mTimestampTo = 0;
	private TimeSelectionView mTimeSelectionView;
	
	private int numberOfMacAddresses = 0;
	private int numberOfIpAddresses = 0;
	private int numberOfUsers = 0;
	
	public DeviceControl(MetalyzerGuiController guiController) {
		super(guiController);
		log.debug("DeviceControl initialized");
	}

	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		this.mTimestampFrom  = t1;
		this.mTimestampTo  = t2;
		mDevicePanel = (DevicePanel)panel;
		this.mTimeSelectionView = mDevicePanel.getTimeSelectionView();
		
		String characteristicName = mDevicePanel.getCharacteristic();
		numberOfMacAddresses = 0;
		numberOfIpAddresses = 0;
		numberOfUsers = 0;
		try {
			ArrayList<Device> devices = mConnection.getAllDevices(mGuiController.getLastTimestamp());
			switch(mTimeSelectionView.getSelectionMode()) {
			case Intervall:
				devices = mConnection.getAllDevicesFromTo(mTimestampFrom, mTimestampTo);
				break;
			case Live:
				devices = mConnection.getCurrentDevices();
				break;
			case Timestamp:
				devices = mConnection.getAllDevices(mTimestampTo);
				break;
			default:
				devices = null;
				break;
			}
			mDevicePanel.setDevices(devices);
			SortedSet<Long> filteredTimestampList = new TreeSet<Long>();
			if(!characteristicName.equals(CharacteristicLabels.DEVICE_AUTH)) {
				UtilHelper.sortDevices(devices);
				mDevicePanel.setDeviceItems(devices);
			} else {
				Long lastTimestamp = null;
				for(Long timestamp : mGuiController.getTimestampList()) {
					if(lastTimestamp == null || !mConnection.getAllDevices(timestamp).equals(mConnection.getAllDevices(lastTimestamp))) {
						filteredTimestampList.add(timestamp);
						lastTimestamp = timestamp;
					}
				}
				mDevicePanel.setTimestamps(UtilHelper.convertTimestamps(filteredTimestampList));
				mDevicePanel.setTimestampItems(UtilHelper.convertTimestamps(filteredTimestampList));
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		updateItem(mDevicePanel, mDevicePanel.getDefaultItem());
	}
	
	/**
	 * Decides which device-characteristic was selected
	 * and calls the method to fill the corresponding view
	 * @param semanticPanel
	 * @param {@link String} item
	 */
	@Override
	public void updateItem(SemanticPanel semanticPanel, String item) {
		this.mDevicePanel = (DevicePanel)semanticPanel;
		this.mItem = item;
		String characteristic = semanticPanel.getCharacteristic();
		
		if (characteristic.equals(CharacteristicLabels.DEVICE_ALL)) {
			fillDeviceAllView(mDevicePanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.DEVICE_USER)) {
			fillDeviceUserView(mDevicePanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.DEVICE_IP)) {
			fillDeviceIpView(mDevicePanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.DEVICE_MAC)) {
			fillDeviceMacView(mDevicePanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.DEVICE_AUTH)) {
			fillDeviceAuthenticationsView(mDevicePanel, item);
		}
		mDevicePanel.setVisible(true);
	}
	
	/**
	 * Refrehes the timestamps of this view
	 * @param t1
	 * @param t2
	 * */
	@Override
	public void updateControl(AnalysePanel panel, long t1, long t2) {
		this.mDevicePanel = (DevicePanel)panel;
		this.mTimestampFrom  = t1;
		this.mTimestampTo  = t2;
		mDevicePanel.clearProperties();
		
		//TODO avoid complete reload 
		if(mDevicePanel != null) {
			fillControl(mDevicePanel, mTimestampFrom, mTimestampTo);
		}	
	}

	/**
	 * Fills the DeviceUserView
	 * @param semanticPanel
	 * @param dev
	 */
	private void fillDeviceAllView(DevicePanel devicePanel, String dev) {
		Device device = devicePanel.getDevice(dev);
		if(device == null) {
			devicePanel.setCountOfIpAddresses(0);
			devicePanel.setCountOfMacAddresses(0);
			devicePanel.setCountOfUsers(0);
			log.info("No Device: "+dev+" found.");
			return;
		}
		
		ArrayList<ArrayList<?>> deviceList = new ArrayList<ArrayList<?>>();
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
		ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();

		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					device.setSearchFromTimestamp(mTimestampFrom);
					device.setSearchToTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForDeviceFromTo(device);
					break;
				case Live:
					macAddresses = mConnection.getMacAddressesForDeviceCurrent(device);
					break;
				case Timestamp:
					device.setSearchTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForDevice(device);
					break;
				default:
					macAddresses = null;
					break;
			}

			if (macAddresses != null) {
				for(MacAddress mac : macAddresses) {
					ArrayList<MacAddress> tempMacAddresses = new ArrayList<MacAddress>();
					tempMacAddresses.add(mac);

					switch(mTimeSelectionView.getSelectionMode()) {
						case Intervall:
							mac.setSearchFromTimestamp(mTimestampFrom);
							mac.setSearchToTimestamp(mTimestampTo);
							ipAddresses = mConnection.getIpAddressesForMacAddressFromTo(mac);
							users = mConnection.getUsersForMacAddressFromTo(mac);
							break;
						case Live:
							ipAddresses = mConnection.getIpAddressesForMacAddressCurrent(mac);
							users = mConnection.getUsersForMacAddressCurrent(mac);
							break;
						case Timestamp:
							mac.setSearchTimestamp(mTimestampTo);
							ipAddresses = mConnection.getIpAddressesForMacAddress(mac);
							users = mConnection.getUsersForMacAddress(mac);
							break;
						default:
							ipAddresses = null;
							users = null;
							break;
					}
					
					deviceList.add(ipAddresses);
					deviceList.add(tempMacAddresses);
					deviceList.add(users);
				}

				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> user = new ArrayList<String>();
				List<String> role = new ArrayList<String>();
				List<String> ipAddr = new ArrayList<String>();
				List<String> type = new ArrayList<String>();
				List<String> macAddr = new ArrayList<String>();
				
				if(deviceList != null) {
					int[] elementListSizes = new int[3];
					int count = 0;
					for(ArrayList<?> elementList : deviceList) {
						elementListSizes[count%3] = elementList.size();
						Arrays.sort(elementListSizes);
						for(Object element : elementList) {
							if(element instanceof User) {
								user.add(((User) element).getName());
								role.add(((User) element).getRole());
							}
							
							if(element instanceof IpAddress) {
								ipAddr.add(((IpAddress) element).getAddress());
								type.add(((IpAddress) element).getType());
							}
							
							for(int i=0; i<elementListSizes[2]; i++) {
								if(element instanceof MacAddress) {
									macAddr.add(((MacAddress) element).getAddress());
								}
							}

							if(elementList.get(elementList.size()-1).equals(element)
									&& elementList.size() < elementListSizes[2]) {
								if(element instanceof User) {
									for(int j=elementList.size(); j<elementListSizes[2]; j++) {
										user.add("");
										role.add("");
									}
								}
								if(element instanceof IpAddress) {
									for(int j=elementList.size(); j<elementListSizes[2]; j++) {
										ipAddr.add("");
										type.add("");
									}
								}
							}
						}
						count++;
					}
					
					data.add(user);
					data.add(role);
					data.add(ipAddr);
					data.add(type);
					data.add(macAddr);
					
					numberOfIpAddresses = ipAddr.size();
					numberOfMacAddresses = macAddr.size();
					numberOfUsers = user.size();
					
					devicePanel.setCountOfIpAddresses(numberOfIpAddresses);
					devicePanel.setCountOfMacAddresses(numberOfMacAddresses);
					devicePanel.setCountOfUsers(numberOfUsers);

					devicePanel.setPropertyData(data);
				}
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Fills the DeviceUserView
	 * @param semanticPanel
	 * @param dev
	 */
	private void fillDeviceUserView(DevicePanel devicePanel, String dev) {
		Device device = devicePanel.getDevice(dev);
		if(device == null) {
			devicePanel.setCountOfUsers(0);
			log.info("No Device: "+dev+" found.");
			return;
		}
		
		ArrayList<User> users;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					device.setSearchFromTimestamp(mTimestampFrom);
					device.setSearchToTimestamp(mTimestampTo);
					users = mConnection.getUsersForDeviceFromTo(device);
					break;
				case Live:
					users = mConnection.getUsersForDeviceCurrent(device);
					break;
				case Timestamp:
					device.setSearchTimestamp(mTimestampTo);
					users = mConnection.getUsersForDevice(device);
					break;
				default:
					users = null;
					break;
			}
			
			if (users != null) {
				numberOfUsers = users.size();
				devicePanel.setCountOfUsers(numberOfUsers);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> name = UtilHelper.convertUserNameToString(users);
				List<String> role = UtilHelper.convertUserRoleToString(users);
				data.add(name);
				data.add(role);
				devicePanel.setPropertyData(data);
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Fills the DeviceIpView
	 * @param semanticPanel
	 * @param dev
	 */
	private void fillDeviceIpView(DevicePanel devicePanel, String dev) {
		Device device = devicePanel.getDevice(dev);
		if(device == null) {
			devicePanel.setCountOfIpAddresses(0);
			return;
		}

		ArrayList<IpAddress> ipAddresses;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					device.setSearchFromTimestamp(mTimestampFrom);
					device.setSearchToTimestamp(mTimestampTo);
					ipAddresses = mConnection.getIpAddressesForDeviceFromTo(device);
					break;
				case Live:
					ipAddresses = mConnection.getIpAddressesForDeviceCurrent(device);
					break;
				case Timestamp:
					device.setSearchTimestamp(mTimestampTo);
					ipAddresses = mConnection.getIpAddressesForDevice(device);
					break;
				default:
					ipAddresses = null;
					break;
			}
			
			if (ipAddresses != null) {
				numberOfIpAddresses = ipAddresses.size();
				devicePanel.setCountOfIpAddresses(numberOfIpAddresses);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> addr = UtilHelper.convertIpAddressToString(ipAddresses);
				List<String> type = UtilHelper.convertIpTypeToString(ipAddresses);
				data.add(addr);
				data.add(type);
				devicePanel.setPropertyData(data);
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Fills the DeviceMacView
	 * @param semanticPanel
	 * @param dev
	 */
	private void fillDeviceMacView(DevicePanel devicePanel, String dev) {
		Device device = devicePanel.getDevice(dev);
		if(device == null) {
			devicePanel.setCountOfMacAddresses(0);
			log.info("No Device: "+dev+" found.");
			return;
		}
		
		ArrayList<MacAddress> macAddresses;	
		try {	
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					device.setSearchFromTimestamp(mTimestampFrom);
					device.setSearchToTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForDeviceFromTo(device);
					break;
				case Live:
					macAddresses = mConnection.getMacAddressesForDeviceCurrent(device);
					break;
				case Timestamp:
					device.setSearchTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForDevice(device);
					break;
				default:
					macAddresses = null;
					break;
			}
			
			if (macAddresses != null) {
				numberOfMacAddresses = macAddresses.size();
				devicePanel.setCountOfMacAddresses(numberOfMacAddresses);
				devicePanel.setPropertyData(UtilHelper.convertMacToString(macAddresses));
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Fills the MacAuthenticationsView
	 * @param semanticPanel
	 * @param datestring
	 */
	private void fillDeviceAuthenticationsView(DevicePanel devicePanel,
			String datestring) {
		// Disables the selection controls, is necessary for the exporter to determine which fields to be filled
		devicePanel.getTimeSelectionView().setSelectionMode(TimeSelection.None);

		ArrayList<Device> devices = null;	
		SortedSet<Long> timestampList = mGuiController.getTimestampList();
		
		if (timestampList != null) {
			for (Long timestamp : timestampList) {
				String str = UtilHelper.convertTimestampToDatestring(timestamp);
				if (str.equals(datestring)) {
					try {
						devices = mConnection.getAllDevices(timestamp);
					} catch (ExecutionException e) {
						log.error("Connection could not be established or the data for the "
								+ "requested method is not available");
					} catch (TimeoutException e) {
						log.error("Request timeout exceeded");
					} catch (InterruptedException e) {
						log.error(e.getMessage());
					}
				}
			}
		}

		devicePanel.clearProperties();
		if (devices != null) {
			List<Collection<?>> data = new ArrayList<Collection<?>>();
			List<String> dev = UtilHelper.convertDeviceNameToString(devices);
			List<String> devattr = UtilHelper.convertDeviceAttributesToString(devices);
			data.add(dev);
			data.add(devattr);
			devicePanel.setPropertyData(data);
		}
	}
}
