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
import de.hshannover.f4.trust.metalyzer.gui.characteristics.IpAddressPanel;
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

public class IpAddressControl extends SemanticControl {

	private static final Logger log = Logger.getLogger(IpAddressControl.class);
	private IpAddressPanel mIpAddressPanel;
	private long mTimestampFrom = 0;
	private long mTimestampTo = 0;
	private TimeSelectionView mTimeSelectionView;

	private int numberOfDevices = 0;
	private int numberOfMacAddresses = 0;
	private int numberOfUsers = 0;
	
	public IpAddressControl(MetalyzerGuiController guiController) {
		super(guiController);
		log.debug("IpControl initialized");
	}
	
	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		this.mTimestampFrom  = t1;
		this.mTimestampTo  = t2;
		this.mIpAddressPanel = (IpAddressPanel)panel;
		this.mTimeSelectionView = mIpAddressPanel.getTimeSelectionView();
		
		String characteristicName = mIpAddressPanel.getCharacteristic();
		numberOfDevices = 0;
		numberOfMacAddresses = 0;
		numberOfUsers = 0;
		try {
			ArrayList<IpAddress> ipAddresses = null;
			switch(mTimeSelectionView.getSelectionMode()) {
			case Intervall:
				ipAddresses = mConnection.getAllIpAddressesFromTo(mTimestampFrom, mTimestampTo);
				break;
			case Live:
				ipAddresses = mConnection.getCurrentIpAddresses();
				break;
			case Timestamp:
				ipAddresses = mConnection.getAllIpAddresses(mTimestampTo);
				break;
			default:
				ipAddresses = null;
				break;
			}
			mIpAddressPanel.setIpAddresses(ipAddresses);
			SortedSet<Long> filteredTimestampList = new TreeSet<Long>();
			if(!characteristicName.equals(CharacteristicLabels.IP_AUTH)) {
				UtilHelper.sortIpAddresses(ipAddresses);
				mIpAddressPanel.setIpAddressItems(ipAddresses);
			} else {
				Long lastTimestamp = null;
				for(Long timestamp : mGuiController.getTimestampList()) {
					if(lastTimestamp == null || !mConnection.getAllIpAddresses(timestamp).equals(mConnection.getAllIpAddresses(lastTimestamp))) {
						filteredTimestampList.add(timestamp);
						lastTimestamp = timestamp;
					}
				}
				mIpAddressPanel.setTimestamps(UtilHelper.convertTimestamps(filteredTimestampList));
				mIpAddressPanel.setTimestampItems(UtilHelper.convertTimestamps(filteredTimestampList));
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		updateItem(mIpAddressPanel, mIpAddressPanel.getDefaultItem());
	}
	
	/**
	 * Decides which ip-characteristic was selected
	 * and calls the method to fill the corresponding view
	 * @param semanticPanel
	 * @param {@link String} item
	 */
	@Override
	public void updateItem(SemanticPanel semanticPanel, String item) {
		this.mIpAddressPanel = (IpAddressPanel)semanticPanel;
		this.mItem = item;
		String characteristic = semanticPanel.getCharacteristic();

		if (characteristic.equals(CharacteristicLabels.IP_ALL)) {
			fillIpAddressAllView(mIpAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.IP_USER)) {
			fillIpAddressUserView(mIpAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.IP_MAC)) {
			fillIpAddressMacView(mIpAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.IP_DEVICES)) {
			fillIpAddressDeviceView(mIpAddressPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.IP_AUTH)) {
			fillIpAddressAuthenticationsView(mIpAddressPanel, item);
		}
		mIpAddressPanel.setVisible(true);
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
		this.mIpAddressPanel = (IpAddressPanel)panel;
		mIpAddressPanel.clearProperties();
		
		//TODO avoid complete reload 
		if(mIpAddressPanel != null) {
			fillControl(mIpAddressPanel, mTimestampFrom, mTimestampTo);
		}
		
	}
	
	/**
	 * Fills the IpUserView
	 * @param semanticPanel
	 * @param ip
	 */
	private void fillIpAddressAllView(IpAddressPanel ipAddressPanel, String ip) {
		IpAddress ipAddress = ipAddressPanel.getIp(ip);
		if(ipAddress == null) {
			ipAddressPanel.setCountOfUsers(0);
			ipAddressPanel.setCountOfMacAddresses(0);
			ipAddressPanel.setCountOfDevices(0);
			log.info("No IpAddress: "+ip+" found.");
			return;
		}
		
		ArrayList<ArrayList<?>> ipList = new ArrayList<ArrayList<?>>();
		ArrayList<User> users;
		ArrayList<MacAddress> macAddresses;
		ArrayList<Device> devices;
		
		try {
			macAddresses = mConnection.getMacAddressesForIpAddress(ipAddress);
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					ipAddress.setSearchFromTimestamp(mTimestampFrom);
					ipAddress.setSearchToTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForIpAddressFromTo(ipAddress);
					break;
				case Live:
					macAddresses = mConnection.getMacAddressesForIpAddressCurrent(ipAddress);
					break;
				case Timestamp:
					ipAddress.setSearchTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForIpAddress(ipAddress);
					break;
				default:
					macAddresses = null;
					break;
			}
			
			for(MacAddress mac : macAddresses) {
				ArrayList<MacAddress> tempMacAddresses = new ArrayList<MacAddress>();
				tempMacAddresses.add(mac);

				switch(mTimeSelectionView.getSelectionMode()) {
					case Intervall:
						mac.setSearchFromTimestamp(mTimestampFrom);
						mac.setSearchToTimestamp(mTimestampTo);
						users = mConnection.getUsersForMacAddressFromTo(mac);
						devices = mConnection.getDevicesForMacAddressFromTo(mac);
						break;
					case Live:
						users = mConnection.getUsersForMacAddressCurrent(mac);
						devices = mConnection.getDevicesForMacAddressCurrent(mac);
						break;
					case Timestamp:
						mac.setSearchTimestamp(mTimestampTo);
						users = mConnection.getUsersForMacAddress(mac);
						devices = mConnection.getDevicesForMacAddress(mac);
						break;
					default:
						users = null;
						devices = null;
						break;
				}

				ipList.add(users);
				ipList.add(tempMacAddresses);
				ipList.add(devices);
			}
			
			if (macAddresses != null) {
				
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> user = new ArrayList<String>();
				List<String> role = new ArrayList<String>();
				List<String> macAddr = new ArrayList<String>();
				List<String> dev = new ArrayList<String>();
				if(ipList != null) {
					int[] elementListSizes = new int[3];
					int count = 0;
					for(ArrayList<?> elementList : ipList) {
						elementListSizes[count%3] = elementList.size();
						Arrays.sort(elementListSizes);
						for(Object element : elementList) {
							if(element instanceof User) {
								user.add(((User) element).getName());
								role.add(((User) element).getRole());
							}
							for(int i=0; i<elementListSizes[2]; i++) {
								if(element instanceof MacAddress) {
									macAddr.add(((MacAddress) element).getAddress());
								}
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
					data.add(macAddr);
					data.add(dev);
					
					numberOfUsers = user.size();
					numberOfMacAddresses = macAddr.size();
					numberOfDevices = dev.size();
					
					ipAddressPanel.setCountOfUsers(numberOfUsers);
					ipAddressPanel.setCountOfMacAddresses(numberOfMacAddresses);
					ipAddressPanel.setCountOfDevices(numberOfDevices);

					ipAddressPanel.setPropertyData(data);
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
	 * Fills the IpUserView
	 * @param semanticPanel
	 * @param ip
	 */
	private void fillIpAddressUserView(IpAddressPanel ipAddressPanel, String ip) {
		IpAddress ipAddress = ipAddressPanel.getIp(ip);
		if(ipAddress == null) {
			ipAddressPanel.setCountOfUsers(0);
			log.info("No IpAddress: "+ip+" found.");
			return;
		}
		
		ArrayList<User> users;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					ipAddress.setSearchFromTimestamp(mTimestampFrom);
					ipAddress.setSearchToTimestamp(mTimestampTo);
					users = mConnection.getUsersForIpAddressFromTo(ipAddress);
					break;
				case Live:
					users = mConnection.getUsersForIpAddressCurrent(ipAddress);
					break;
				case Timestamp:
					ipAddress.setSearchTimestamp(mTimestampTo);
					users = mConnection.getUsersForIpAddress(ipAddress);
					break;
				default:
					users = null;
					break;
			}
			
			if (users != null) {
				numberOfUsers = users.size();
				ipAddressPanel.setCountOfUsers(numberOfUsers);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> name = UtilHelper.convertUserNameToString(users);
				List<String> role = UtilHelper.convertUserRoleToString(users);
				data.add(name);
				data.add(role);
				ipAddressPanel.setPropertyData(data);
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
	 * Fills the IpMacView
	 * @param semanticPanel
	 * @param ip
	 */
	private void fillIpAddressMacView(IpAddressPanel ipAddressPanel, String ip) {
		IpAddress ipAddress = ipAddressPanel.getIp(ip);
		if(ipAddress == null) {
			ipAddressPanel.setCountOfMacAddresses(numberOfMacAddresses);
			log.info("No IpAddress: "+ip+" found.");
			return;
		}
		
		ArrayList<MacAddress> macAddresses;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
			case Intervall:
				ipAddress.setSearchFromTimestamp(mTimestampFrom);
				ipAddress.setSearchToTimestamp(mTimestampTo);
				macAddresses = mConnection.getMacAddressesForIpAddressFromTo(ipAddress);
				break;
			case Live:
				macAddresses = mConnection.getMacAddressesForIpAddressCurrent(ipAddress);
				break;
			case Timestamp:
				ipAddress.setSearchTimestamp(mTimestampTo);
				macAddresses = mConnection.getMacAddressesForIpAddress(ipAddress);
				break;
			default:
				macAddresses = null;
				break;
		}
			
			if (macAddresses != null) {
				numberOfMacAddresses = macAddresses.size();
				ipAddressPanel.setCountOfMacAddresses(numberOfMacAddresses);
				ipAddressPanel.setPropertyData(UtilHelper.convertMacToString(macAddresses));
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
	 * Fills the IpDeviceView
	 * @param semanticPanel
	 * @param ip
	 */
	private void fillIpAddressDeviceView(IpAddressPanel ipAddressPanel, String ip) {
		IpAddress ipAddress = ipAddressPanel.getIp(ip);
		if(ipAddress == null) {
			ipAddressPanel.setCountOfDevices(0);
			log.info("No IpAddress: "+ip+" found.");
			return;
		}
		
		ArrayList<Device> devices;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					ipAddress.setSearchFromTimestamp(mTimestampFrom);
					ipAddress.setSearchToTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForIpAddressFromTo(ipAddress);
					break;
				case Live:
					devices = mConnection.getDevicesForIpAddressCurrent(ipAddress);
					break;
				case Timestamp:
					ipAddress.setSearchTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForIpAddress(ipAddress);
					break;
				default:
					devices = null;
					break;
			}

			if (devices != null) {
				numberOfDevices = devices.size();
				ipAddressPanel.setCountOfDevices(numberOfDevices);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> dev = UtilHelper.convertDeviceNameToString(devices);
				List<String> devattr = UtilHelper.convertDeviceAttributesToString(devices);
				data.add(dev);
				data.add(devattr);
				ipAddressPanel.setPropertyData(data);
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
	 * Fills the IpAuthenticationsView
	 * @param semanticPanel
	 * @param datestring
	 */
	private void fillIpAddressAuthenticationsView(IpAddressPanel ipAddressPanel,
			String datestring) {
		// Disables the selection controls, is necessary for the exporter to determine which fields to be filled
		ipAddressPanel.getTimeSelectionView().setSelectionMode(TimeSelection.None);
		
		ArrayList<IpAddress> ipAddresses = null;	
		SortedSet<Long> timestampList = mGuiController.getTimestampList();
		
		if (timestampList != null) {
			for (Long timestamp : timestampList) {
				String str = UtilHelper.convertTimestampToDatestring(timestamp);
				if (str.equals(datestring)) {
					try {
						ipAddresses = mConnection.getAllIpAddresses(timestamp);
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

		ipAddressPanel.clearProperties();
		if (ipAddresses != null) {
			List<Collection<?>> data = new ArrayList<Collection<?>>();
			List<String> ip = UtilHelper.convertIpAddressToString(ipAddresses);
			List<String> ipType = UtilHelper.convertIpTypeToString(ipAddresses);
			data.add(ip);
			data.add(ipType);
			ipAddressPanel.setPropertyData(data);
		}
	}
}
