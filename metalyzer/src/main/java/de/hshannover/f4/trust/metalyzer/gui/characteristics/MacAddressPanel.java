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
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class MacAddressPanel extends SemanticPanel {

	private ArrayList<MacAddress> mMacAddresses;
	
	public MacAddressPanel(MetalyzerGuiController guiController, String characteristic) {
		super(guiController, characteristic);
		if(characteristic.equals(CharacteristicLabels.MAC_USER)) {
			super.setComboBoxLabel("MacAddresses: ");
			mLabelView.addLabel("MacUser", "Count of Users:", 0);
		} else if(characteristic.equals(CharacteristicLabels.MAC_IP)) {
			super.setComboBoxLabel("MacAddresses: ");
			mLabelView.addLabel("MacIp", "Count of IpAddresses:", 0);
		} else if(characteristic.equals(CharacteristicLabels.MAC_DEVICES)) {
			super.setComboBoxLabel("MacAddresses: ");
			mLabelView.addLabel("MacDev", "Count of Devices:", 0);
		} else if(characteristic.equals(CharacteristicLabels.MAC_ALL)) {
			super.setComboBoxLabel("MacAddresses: ");
			mLabelView.addLabel("MacUser", "Count of Users:", 0);
			mLabelView.addLabel("MacIp", "Count of IpAddresses:", 0);
			mLabelView.addLabel("MacDev", "Count of Devices:", 0);
		} else if(characteristic.equals(CharacteristicLabels.MAC_AUTH)) {
			super.setComboBoxLabel("Timestamp: ");
			this.getTimeSelectionView().hideTimeSelectors();
		}
	}
	
	/**
	 * Sets the count of user of a macaddress
	 * @param ipAddressCount the count of user of a macaddress
	 * */
	public void setCountOfUsers(int userCount) {
		mLabelView.setValueById("MacUser", userCount);
	}
	
	/**
	 * Sets the count of ipaddress of a macaddress
	 * @param ipAddressCount the count of ipaddress of a macaddress
	 * */
	public void setCountOfIpAddresses(int ipAddressCount) {
		mLabelView.setValueById("MacIp", ipAddressCount);
	}
	
	/**
	 * Sets the count of devices of a macaddress
	 * @param ipAddressCount the count of devices of a macaddress
	 * */
	public void setCountOfDevices(int devCount) {
		mLabelView.setValueById("MacDev", devCount);
	}
	
	/**
	 * Sets an ArrayList of all macAddresses
	 * @param macAddresses
	 */
	public void setMacAddresses(ArrayList<MacAddress> macAddresses) {
		mMacAddresses = macAddresses;
	}
	
	/**
	 * Fills a {@link ComboBox} with an {@link ArrayList} of macAddresses
	 * @param macAddresses ArrayList of macAddresses
	 */
	public void setMacAddressItems(ArrayList<MacAddress> macAddresses) {
		mComboBoxView.clear();
		for(MacAddress mac : macAddresses) {
			if(!mComboBoxView.containsItem(mac.getAddress())) {
				mComboBoxView.addItem(mac.getAddress());
			}
		}
			
	}
	
	/**
	 * @return An {@link ArrayList} which contains all macAddresses 
	 */
	public MacAddress getMac(String macAddress) {
		if(mMacAddresses == null) {
			return null;
		}
		for(MacAddress mac : mMacAddresses) {
			if(mac.getAddress().equals(macAddress)) {
				return mac;
			}
		}
		return null;
	}

}
