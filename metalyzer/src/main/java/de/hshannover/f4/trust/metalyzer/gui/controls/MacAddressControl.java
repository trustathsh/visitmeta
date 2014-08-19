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
import de.hshannover.f4.trust.metalyzer.gui.characteristics.MacAddressPanel;
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

public class MacAddressControl extends SemanticControl {

	private static final Logger log = Logger.getLogger(MacAddressControl.class);
	private MacAddressPanel mMacAddressPanel;
	private long mTimestampFrom = 0;
	private long mTimestampTo = 0;
	private TimeSelectionView mTimeSelectionView;

	private int numberOfDevices = 0;
	private int numberOfIpAddresses = 0;
	private int numberOfUsers = 0;
	
	public MacAddressControl(MetalyzerGuiController guiController) {
		super(guiController);
		log.debug("MacControl initialized");
	}
	
	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		this.mTimestampFrom  = t1;
		this.mTimestampTo  = t2;
		this.mMacAddressPanel = (MacAddressPanel)panel;
		this.mTimeSelectionView = mMacAddressPanel.getTimeSelectionView();
		
		String characteristicName = mMacAddressPanel.getCharacteristic();
		numberOfDevices = 0;
		numberOfUsers = 0;
		numberOfIpAddresses = 0;
		try {
			ArrayList<MacAddress> macAddresses = null;
			switch(mTimeSelectionView.getSelectionMode()) {
			case Intervall:
				macAddresses = mConnection.getAllMacAddressesFromTo(mTimestampFrom, mTimestampTo);
				break;
			case Live:
				macAddresses = mConnection.getCurrentMacAddresses();
				break;
			case Timestamp:
				macAddresses = mConnection.getAllMacAddresses(mTimestampTo);
				break;
			default:
				macAddresses = null;
				break;
			}
			mMacAddressPanel.setMacAddresses(macAddresses);
			SortedSet<Long> filteredTimestampList = new TreeSet<Long>();
			if(!characteristicName.equals(CharacteristicLabels.MAC_AUTH)) {
				UtilHelper.sortMacAddresses(macAddresses);
				mMacAddressPanel.setMacAddressItems(macAddresses);
			} else {
				Long lastTimestamp = null;
				for(Long timestamp : mGuiController.getTimestampList()) {
					if(lastTimestamp == null || !mConnection.getAllMacAddresses(timestamp).equals(mConnection.getAllMacAddresses(lastTimestamp))) {
						filteredTimestampList.add(timestamp);
						lastTimestamp = timestamp;
					}
				}
				mMacAddressPanel.setTimestamps(UtilHelper.convertTimestamps(filteredTimestampList));
				mMacAddressPanel.setTimestampItems(UtilHelper.convertTimestamps(filteredTimestampList));
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		updateItem(mMacAddressPanel, mMacAddressPanel.getDefaultItem());
	}
	
	
	/**
	 * Refrehes the timestamps of this view
	 * @param t1
	 * @param t2
	 * */
	@Override
	public void updateControl(AnalysePanel panel, long t1, long t2) {
		this.mTimestampFrom  = t1;
		this.mTimestampTo  = t2;
		this.mMacAddressPanel = (MacAddressPanel)panel;
		mMacAddressPanel.clearProperties();
		
		//TODO avoid complete reload 
		if(mMacAddressPanel != null) {
			fillControl(mMacAddressPanel, mTimestampFrom, mTimestampTo);
		}	
	}
	
	/**
	 * Decides which mac-characteristic was selected
	 * and calls the method to fill the corresponding view
	 * @param semanticPanel
	 * @param {@link String} item
	 */
	@Override
	public void updateItem(SemanticPanel semanticPanel, String item) {
		this.mMacAddressPanel = (MacAddressPanel)semanticPanel;
		this.mItem = item;
		String characteristic = semanticPanel.getCharacteristic();

		if (characteristic.equals(CharacteristicLabels.MAC_ALL)) {
			fillMacAddressAllView(mMacAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.MAC_USER)) {
			fillMacAddressUserView(mMacAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.MAC_IP)) {
			fillMacAddressIpView(mMacAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.MAC_DEVICES)) {
			fillMacAddressDeviceView(mMacAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.MAC_AUTH)) {
			fillMacAddressAuthenticationsView(mMacAddressPanel, item);
		}

		mMacAddressPanel.setVisible(true);
	}
	
	/**
	 * Fills the MacAllView
	 * @param semanticPanel
	 * @param mac
	 */
	private void fillMacAddressAllView(MacAddressPanel mMacAddressPanel, String mac) {
		MacAddress macAddress = mMacAddressPanel.getMac(mac);
		if(macAddress == null) {
			mMacAddressPanel.setCountOfIpAddresses(0);
			mMacAddressPanel.setCountOfUsers(0);
			mMacAddressPanel.setCountOfDevices(0);
			log.info("No MacAddress: "+mac+" found.");
			return;
		}
		
		ArrayList<ArrayList<?>> macList = new ArrayList<ArrayList<?>>();
		
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
		ArrayList<Device> devices = new ArrayList<Device>();
		
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					macAddress.setSearchFromTimestamp(mTimestampFrom);
					macAddress.setSearchToTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForMacAddressFromTo(macAddress);
					users = mConnection.getUsersForMacAddressFromTo(macAddress);
					ipAddresses = mConnection.getIpAddressesForMacAddressFromTo(macAddress);
					break;
				case Live:
					devices = mConnection.getDevicesForMacAddressCurrent(macAddress);
					users = mConnection.getUsersForMacAddressCurrent(macAddress);
					ipAddresses = mConnection.getIpAddressesForMacAddressCurrent(macAddress);
					break;
				case Timestamp:
					macAddress.setSearchTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForMacAddress(macAddress);
					users = mConnection.getUsersForMacAddress(macAddress);
					ipAddresses = mConnection.getIpAddressesForMacAddress(macAddress);
					break;
				default:
					devices = null;
					users = null;
					ipAddresses = null;
					break;
			}
			
			macList.add(users);
			macList.add(ipAddresses);
			macList.add(devices);
				
			List<Collection<?>> data = new ArrayList<Collection<?>>();
			List<String> ipAddr = new ArrayList<String>();
			List<String> type = new ArrayList<String>();
			List<String> user = new ArrayList<String>();
			List<String> role = new ArrayList<String>();
			List<String> dev = new ArrayList<String>();
		
			if(macList != null) {
				int[] elementListSizes = new int[3];
				int count = 0;
				for(ArrayList<?> elementList : macList) {
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
						
						if(element instanceof Device) {
							dev.add(((Device) element).getName());
						}
						
						if(elementList.get(elementList.size()-1).equals(element)
								&& elementList.size() < elementListSizes[2]) {
							if(element instanceof Device) {
								for(int j=elementList.size(); j<elementListSizes[2]; j++) {
									dev.add("");
								}
							}
							if(element instanceof IpAddress) {
								for(int j=elementList.size(); j<elementListSizes[2]; j++) {
									ipAddr.add("");
									type.add("");
								}
							}
							if(element instanceof User) {
								for(int j=elementList.size(); j<elementListSizes[2]; j++) {
									user.add("");
									role.add("");
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
				data.add(dev);
				
				numberOfIpAddresses = ipAddresses.size();
				numberOfUsers = user.size();
				numberOfDevices = devices.size();
				
				mMacAddressPanel.setCountOfIpAddresses(numberOfIpAddresses);
				mMacAddressPanel.setCountOfUsers(numberOfUsers);
				mMacAddressPanel.setCountOfDevices(numberOfDevices);

				mMacAddressPanel.setPropertyData(data);
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
	 * Fills the MacUserView
	 * @param semanticPanel
	 * @param mac
	 */
	private void fillMacAddressUserView(MacAddressPanel mMacAddressPanel, String mac) {
		MacAddress macAddress = mMacAddressPanel.getMac(mac);
		if(macAddress == null) {
			mMacAddressPanel.setCountOfUsers(0);
			log.info("No MacAddress: "+mac+" found.");
			return;
		}
		
		ArrayList<User> users;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					macAddress.setSearchFromTimestamp(mTimestampFrom);
					macAddress.setSearchToTimestamp(mTimestampTo);
					users = mConnection.getUsersForMacAddressFromTo(macAddress);
					break;
				case Live:
					users = mConnection.getUsersForMacAddressCurrent(macAddress);
					break;
				case Timestamp:
					macAddress.setSearchTimestamp(mTimestampTo);
					users = mConnection.getUsersForMacAddress(macAddress);
					break;
				default:
					users = null;
					break;
			}
			
			if (users != null) {
				numberOfUsers  = users.size();
				mMacAddressPanel.setCountOfUsers(numberOfUsers);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> name = UtilHelper.convertUserNameToString(users);
				List<String> role = UtilHelper.convertUserRoleToString(users);
				data.add(name);
				data.add(role);
				mMacAddressPanel.setPropertyData(data);
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
	 * Fills the MacIpView
	 * @param semanticPanel
	 * @param mac
	 */
	private void fillMacAddressIpView(MacAddressPanel mMacAddressPanel, String mac) {
		MacAddress macAddress = mMacAddressPanel.getMac(mac);
		if(macAddress == null) {
			mMacAddressPanel.setCountOfIpAddresses(0);
			log.info("No MacAddress: "+mac+" found.");
			return;
		}
		ArrayList<IpAddress> ipAddresses;
		mMacAddressPanel.setComboBoxLabel("MacAddress: ");
		
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					macAddress.setSearchFromTimestamp(mTimestampFrom);
					macAddress.setSearchToTimestamp(mTimestampTo);
					ipAddresses = mConnection.getIpAddressesForMacAddressFromTo(macAddress);
					break;
				case Live:
					ipAddresses = mConnection.getIpAddressesForMacAddressCurrent(macAddress);
					break;
				case Timestamp:
					macAddress.setSearchTimestamp(mTimestampTo);
					ipAddresses = mConnection.getIpAddressesForMacAddress(macAddress);
					break;
				default:
					ipAddresses = null;
					break;
			}
			
			if (ipAddresses != null) {
				numberOfIpAddresses = ipAddresses.size();
				mMacAddressPanel.setCountOfIpAddresses(numberOfIpAddresses);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> addr = UtilHelper.convertIpAddressToString(ipAddresses);
				List<String> type = UtilHelper.convertIpTypeToString(ipAddresses);
				data.add(addr);
				data.add(type);
				mMacAddressPanel.setPropertyData(data);
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
	 * Fills the MacDeviceView
	 * @param semanticPanel
	 * @param mac
	 */
	private void fillMacAddressDeviceView(MacAddressPanel mMacAddressPanel, String mac) {
		MacAddress macAddress = mMacAddressPanel.getMac(mac);
		if(macAddress == null) {
			mMacAddressPanel.setCountOfDevices(0);
			log.info("No MacAddress: "+mac+" found.");
			return;
		}
		
		ArrayList<Device> devices;
		mMacAddressPanel.setComboBoxLabel("MacAddress: ");
		
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					macAddress.setSearchFromTimestamp(mTimestampFrom);
					macAddress.setSearchToTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForMacAddressFromTo(macAddress);
					break;
				case Live:
					devices = mConnection.getDevicesForMacAddressCurrent(macAddress);
					break;
				case Timestamp:
					macAddress.setSearchTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForMacAddress(macAddress);
					break;
				default:
					devices = null;
					break;
			}

			if (devices != null) {
				numberOfDevices = devices.size();
				mMacAddressPanel.setCountOfDevices(numberOfDevices);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> dev = UtilHelper.convertDeviceNameToString(devices);
				List<String> devattr = UtilHelper.convertDeviceAttributesToString(devices);
				data.add(dev);
				data.add(devattr);
				mMacAddressPanel.setPropertyData(data);
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
	private void fillMacAddressAuthenticationsView(MacAddressPanel macAddressPanel,
			String datestring) {
		// Disables the selection controls, is necessary for the exporter to determine which fields to be filled
		macAddressPanel.getTimeSelectionView().setSelectionMode(TimeSelection.None);
		
		ArrayList<MacAddress> macAddresses = null;	
		SortedSet<Long> timestampList = mGuiController.getTimestampList();
		
		if (timestampList != null) {
			for (Long timestamp : timestampList) {
				String str = UtilHelper.convertTimestampToDatestring(timestamp);
				if (str.equals(datestring)) {
					try {
						macAddresses = mConnection.getAllMacAddresses(timestamp);
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

		macAddressPanel.clearProperties();
		if (macAddresses != null) {
			List<Collection<?>> data = new ArrayList<Collection<?>>();
			List<String> mac = UtilHelper.convertMacToString(macAddresses);
			data.add(mac);
			macAddressPanel.setPropertyData(data);
		}
	}
}
