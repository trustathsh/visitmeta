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
package de.hshannover.f4.trust.metalyzer.gui.characteristics;

import java.util.ArrayList;

import de.hshannover.f4.trust.metalyzer.gui.MetalyzerGuiController;
import de.hshannover.f4.trust.metalyzer.gui.labels.CharacteristicLabels;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class UserPanel extends SemanticPanel {

	private ArrayList<User> mUsers;
	
	public UserPanel(MetalyzerGuiController guiController, String characteristic) {
		super(guiController, characteristic);
		if(characteristic.equals(CharacteristicLabels.USER_IP)) {
			super.setComboBoxLabel("Name: ");
			mLabelView.addLabel("UserIp", "Count of IpAddresses:", 0);
		} else if(characteristic.equals(CharacteristicLabels.USER_MAC)) {
			super.setComboBoxLabel("Name: ");
			mLabelView.addLabel("UserMac", "Count of MacAddresses:", 0);
		} else if(characteristic.equals(CharacteristicLabels.USER_DEVICES)) {
			super.setComboBoxLabel("Name: ");
			mLabelView.addLabel("UserDev", "Count of Devices:", 0);
		} else if(characteristic.equals(CharacteristicLabels.USER_ALL)) {
			super.setComboBoxLabel("Name: ");
			mLabelView.addLabel("UserLogin", "First Login:", "");
			mLabelView.addLabel("UserIp", "Count of IpAddresses:", 0);
			mLabelView.addLabel("UserMac", "Count of MacAddresses:", 0);
			mLabelView.addLabel("UserDev", "Count of Devices:", 0);
		}  else if(characteristic.equals(CharacteristicLabels.USER_AUTH)) {
			super.setComboBoxLabel("Timestamp: ");
			this.getTimeSelectionView().hideTimeSelectors();
		}
	}
	
	/**
	 * Sets the timestamp value of the user login label
	 * @param login the timestap of the first user login as string
	 * */
	public void setUserLogin(String login) {
		mLabelView.setValueById("UserLogin", login);
	}
	
	/**
	 * Sets the count of ipaddresses of a user
	 * @param ipAddressCount the count of ipaddress of a user
	 * */
	public void setCountOfIpAddresses(int ipAddressCount) {
		mLabelView.setValueById("UserIp", ipAddressCount);
	}
	
	/**
	 * Sets the count of macaddresses of a user
	 * @param ipAddressCount the count of macaddresses of a user
	 * */
	public void setCountOfMacAddresses(int macAddressCount) {
		mLabelView.setValueById("UserMac", macAddressCount);
	}
	
	/**
	 * Sets the count of devices of a user
	 * @param ipAddressCount the count of devices of a user
	 * */
	public void setCountOfDevices(int devCount) {
		mLabelView.setValueById("UserDev", devCount);
	}
	
	/**
	 * Fills a {@link ComboBox} with an {@link ArrayList} of users
	 * @param users ArrayList of users
	 */
	public void setUserItems(ArrayList<User> users) {
		mComboBoxView.clear();
		for(User user : users) {
			if(!mComboBoxView.containsItem(user.getName())) {
				mComboBoxView.addItem(user.getName());
			}
		}		
	}

	/**
	 * @return An {@link ArrayList} which contains all users 
	 */
	public User getUser(String name) {
		if(mUsers == null) {
			return null;
		}
		for(User u : mUsers) {
			if(u.getName().equals(name)) {
				return u;
			}
		}
		return null;
	}
	
	/**
	 * Sets an ArrayList of all users
	 * @param users
	 */
	public void setUsers(ArrayList<User> users) {
		mUsers = users;
	}
}
