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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.SemanticPanel;
import de.hshannover.f4.trust.metalyzer.gui.characteristics.UserPanel;
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

public class UserControl extends SemanticControl {

	private static final Logger log = Logger.getLogger(UserControl.class);
	private long mTimestampFrom = 0;
	private long mTimestampTo = 0;
	private UserPanel mUserPanel;
	private TimeSelectionView mTimeSelectionView;
	private int numberOfDevices;
	private int numberOfMacAddresses;
	private int numberOfIpAddresses;

	public UserControl(MetalyzerGuiController guiController) {
		super(guiController);
		log.debug("UserControl initialized");
	}
	
	@Override
	public void fillControl(AnalysePanel panel, long t1, long t2) {
		this.mTimestampFrom  = t1;
		this.mTimestampTo  = t2;
		this.mUserPanel = (UserPanel)panel;
		this.mTimeSelectionView = mUserPanel.getTimeSelectionView();
		
		String characteristicName = mUserPanel.getCharacteristic();
		numberOfDevices = 0;
		numberOfMacAddresses = 0;
		numberOfIpAddresses = 0;
		try {
			ArrayList<User> users = null;
			switch(mTimeSelectionView.getSelectionMode()) {
			case Intervall:
				users = mConnection.getAllUsersFromTo(mTimestampFrom, mTimestampTo);
				break;
			case Live:
				users = mConnection.getCurrentUsers();
				break;
			case Timestamp:
				users = mConnection.getAllUsers(mTimestampTo);
				break;
			default:
				users = null;
				break;
			}
			mUserPanel.setUsers(users);
			SortedSet<Long> filteredTimestampList = new TreeSet<Long>();
			if(!characteristicName.equals(CharacteristicLabels.USER_AUTH)) {
				UtilHelper.sortUsers(users);
				mUserPanel.setUserItems(users);
			} else {
				Long lastTimestamp = null;
				for(Long timestamp : mGuiController.getTimestampList()) {
					if(lastTimestamp == null || !mConnection.getAllUsers(timestamp).equals(mConnection.getAllUsers(lastTimestamp))) {
						filteredTimestampList.add(timestamp);
						lastTimestamp = timestamp;
					}
				}
				mUserPanel.setTimestamps(UtilHelper.convertTimestamps(filteredTimestampList));
				mUserPanel.setTimestampItems(UtilHelper.convertTimestamps(filteredTimestampList));
			}
		} catch (ExecutionException e) {
			log.error(e.getMessage());
		} catch (TimeoutException e) {
			log.error(e.getMessage());
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
		updateItem(mUserPanel, mUserPanel.getDefaultItem());
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
		this.mUserPanel = (UserPanel)panel;
		mUserPanel.clearProperties();
		
		//TODO avoid complete reload 
		if(mUserPanel != null) {
			fillControl(mUserPanel, mTimestampFrom, mTimestampTo);
		}
	}
	
	/**
	 * Decides which user-characteristic was selected
	 * and calls the method to fill the corresponding view
	 * @param semanticPanel
	 * @param {@link String} item
	 */
	@Override
	public void updateItem(SemanticPanel semanticPanel, String item) {
		mUserPanel = (UserPanel)semanticPanel;
		this.mItem = item;
		
		String characteristic = semanticPanel.getCharacteristic();
		
		if (characteristic.equals(CharacteristicLabels.USER_ALL)) {
			fillUserAllView(mUserPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.USER_IP)) {
			fillIpView(mUserPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.USER_MAC)) {
			fillMacView(mUserPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.USER_DEVICES)) {
			fillDeviceView(mUserPanel, item);
		}
		if (characteristic.equals(CharacteristicLabels.USER_AUTH)) {
			fillAuthenticationsView(mUserPanel, item);
		}
		mUserPanel.setVisible(true);
	}
	
	/**
	 * Fills the UserAllView
	 * @param semanticPanel
	 * @param username
	 */
	private void fillUserAllView(UserPanel userPanel, String username) {
		User user = userPanel.getUser(username);
		if(user == null) {
			userPanel.setCountOfIpAddresses(0);
			userPanel.setCountOfMacAddresses(0);
			userPanel.setCountOfDevices(0);
			log.info("No user with Name: "+username+" found.");
			return;
		}

		ArrayList<ArrayList<?>> userList = new ArrayList<ArrayList<?>>();
		ArrayList<IpAddress> ipAddresses = new ArrayList<IpAddress>();
		ArrayList<MacAddress> macAddresses = new ArrayList<MacAddress>();
		ArrayList<Device> devices = new ArrayList<Device>();
		long firstLogin = 0;
		
		try {
			
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					user.setSearchFromTimestamp(mTimestampFrom);
					user.setSearchToTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForUserFromTo(user);
					firstLogin = mConnection.getTimestampForUserFromTo(user);
					break;
				case Live:
					macAddresses = mConnection.getMacAddressesForUserCurrent(user);
					firstLogin = mConnection.getTimestampForUserCurrent(user);
					break;
				case Timestamp:
					user.setSearchTimestamp(mTimestampTo);
					firstLogin = mConnection.getTimestampForUser(user);
					macAddresses = mConnection.getMacAddressesForUser(user);
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
							devices = mConnection.getDevicesForMacAddressFromTo(mac);
							break;
						case Live:
							ipAddresses = mConnection.getIpAddressesForMacAddressCurrent(mac);
							devices = mConnection.getDevicesForMacAddressCurrent(mac);
							break;
						case Timestamp:
							mac.setSearchTimestamp(mTimestampTo);
							ipAddresses = mConnection.getIpAddressesForMacAddress(mac);
							devices = mConnection.getDevicesForMacAddress(mac);
							break;
						default:
							ipAddresses = null;
							devices = null;
							break;
					}
					
					userList.add(ipAddresses);
					userList.add(tempMacAddresses);
					userList.add(devices);
				}
				String datestring = UtilHelper.convertTimestampToDatestring(firstLogin);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> ipAddr = new ArrayList<String>();
				List<String> type = new ArrayList<String>();
				List<String> macAddr = new ArrayList<String>();
				List<String> dev = new ArrayList<String>();
				
				if(userList != null) {
					int[] elementListSizes = new int[3];
					int count = 0;
					for(ArrayList<?> elementList : userList) {
						elementListSizes[count%3] = elementList.size();
						Arrays.sort(elementListSizes);
						for(Object element : elementList) {
							if(element instanceof IpAddress) {
								ipAddr.add(((IpAddress) element).getAddress());
								type.add(((IpAddress) element).getType());
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
					
					data.add(ipAddr);
					data.add(type);
					data.add(macAddr);
					data.add(dev);
					
					numberOfIpAddresses = ipAddresses.size();
					numberOfMacAddresses = macAddresses.size();
					numberOfDevices = devices.size();
					
					userPanel.setUserLogin(datestring);
					userPanel.setCountOfIpAddresses(numberOfIpAddresses);
					userPanel.setCountOfMacAddresses(numberOfMacAddresses);
					userPanel.setCountOfDevices(numberOfDevices);

					
					userPanel.setPropertyData(data);
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
	 * Fills the UserIpView
	 * @param semanticPanel
	 * @param username
	 */
	private void fillIpView(UserPanel userPanel, String username) {
		User user = userPanel.getUser(username);
		if(user == null) {
			userPanel.setCountOfIpAddresses(0);
			log.info("No user with Name: "+username+" found.");
			return;
		}

		ArrayList<IpAddress> ipAddresses = null;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					user.setSearchFromTimestamp(mTimestampFrom);
					user.setSearchToTimestamp(mTimestampTo);
					ipAddresses = mConnection.getIpAddressesForUserFromTo(user);
					break;
				case Live:
					ipAddresses = mConnection.getIpAddressesForUserCurrent(user);
					break;
				case Timestamp:
					user.setSearchTimestamp(mTimestampTo);
					ipAddresses = mConnection.getIpAddressesForUser(user);
					break;
				default:
					ipAddresses = null;
					break;
			}
			
	
			if (ipAddresses != null) {
				numberOfIpAddresses = ipAddresses.size();
				userPanel.setCountOfIpAddresses(numberOfIpAddresses);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> addr = UtilHelper.convertIpAddressToString(ipAddresses);
				List<String> type = UtilHelper.convertIpTypeToString(ipAddresses);
				data.add(addr);
				data.add(type);
				userPanel.setPropertyData(data);
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
	 * Fills the UserMacView
	 * @param semanticPanel
	 * @param username
	 */
	private void fillMacView(UserPanel userPanel, String username) {
		User user = userPanel.getUser(username);
		if(user == null) {
			userPanel.setCountOfMacAddresses(0);
			log.info("No user with Name: "+username+" found.");
			return;
		}
		
		ArrayList<MacAddress> macAddresses;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					user.setSearchFromTimestamp(mTimestampFrom);
					user.setSearchToTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForUserFromTo(user);
					break;
				case Live:
					macAddresses = mConnection.getMacAddressesForUserCurrent(user);
					break;
				case Timestamp:
					user.setSearchTimestamp(mTimestampTo);
					macAddresses = mConnection.getMacAddressesForUser(user);
					break;
				default:
					macAddresses = null;
					break;
			}

			if (macAddresses != null) {
				numberOfMacAddresses = macAddresses.size();
				userPanel.setCountOfMacAddresses(numberOfMacAddresses);
				userPanel.setPropertyData(UtilHelper.convertMacToString(macAddresses));
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
	 * Fills the UserDeviceView
	 * @param semanticPanel
	 * @param username
	 */
	private void fillDeviceView(UserPanel userPanel, String username) {
		User user = userPanel.getUser(username);
		if(user == null) {
			userPanel.setCountOfDevices(0);
			log.info("No user with Name: "+username+" found.");
			return;
		}
		
		ArrayList<Device> devices;
		try {
			switch(mTimeSelectionView.getSelectionMode()) {
				case Intervall:
					user.setSearchFromTimestamp(mTimestampFrom);
					user.setSearchToTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForUserFromTo(user);
					break;
				case Live:
					devices = mConnection.getDevicesForUserCurrent(user);
					break;
				case Timestamp:
					user.setSearchTimestamp(mTimestampTo);
					devices = mConnection.getDevicesForUser(user);
					break;
				default:
					devices = null;
					break;
			}
			
			if (devices != null) {
				numberOfDevices = devices.size();
				userPanel.setCountOfDevices(numberOfDevices);
				List<Collection<?>> data = new ArrayList<Collection<?>>();
				List<String> dev = UtilHelper.convertDeviceNameToString(devices);
				List<String> devattr = UtilHelper.convertDeviceAttributesToString(devices);
				data.add(dev);
				data.add(devattr);
				userPanel.setPropertyData(data);
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
	 * Fills the UserAuthenticationsView
	 * @param semanticPanel
	 * @param datestring
	 */
	private void fillAuthenticationsView(UserPanel userPanel,
			String datestring) {
		// Disables the selection controls, is necessary for the exporter to determine which fields to be filled
		userPanel.getTimeSelectionView().setSelectionMode(TimeSelection.None);
		
		ArrayList<User> users = null;	
		SortedSet<Long> timestampList = mGuiController.getTimestampList();
		
		if (timestampList != null) {
			for (Long timestamp : timestampList) {
				String str = UtilHelper.convertTimestampToDatestring(timestamp);
				if (str.equals(datestring)) {
					try {
						users = mConnection.getAllUsers(timestamp);
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

		userPanel.clearProperties();
		if (users != null) {
			List<Collection<?>> data = new ArrayList<Collection<?>>();
			List<String> name = UtilHelper.convertUserNameToString(users);
			List<String> role = UtilHelper.convertUserRoleToString(users);
			data.add(name);
			data.add(role);
			userPanel.setPropertyData(data);
		}
	}
}
