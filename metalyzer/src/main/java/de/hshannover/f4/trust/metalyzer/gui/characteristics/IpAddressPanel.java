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
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class IpAddressPanel extends SemanticPanel {

	private ArrayList<IpAddress> mIpAddresses;
	
	public IpAddressPanel(MetalyzerGuiController guiController, String characteristic) {
		super(guiController, characteristic);
		if(characteristic.equals(CharacteristicLabels.IP_USER)) {
			super.setComboBoxLabel("IpAddress: ");
			mLabelView.addLabel("IpUser", "Count of Users:", 0);
		} else if(characteristic.equals(CharacteristicLabels.IP_MAC)) {
			super.setComboBoxLabel("IpAddress: ");
			mLabelView.addLabel("IpMac", "Count of MacAddresses:", 0);
		} else if(characteristic.equals(CharacteristicLabels.IP_DEVICES)) {
			super.setComboBoxLabel("IpAddress: ");
			mLabelView.addLabel("IpDev", "Count of Devices:", 0);
		} else if(characteristic.equals(CharacteristicLabels.IP_ALL)) {
			super.setComboBoxLabel("IpAddress: ");
			mLabelView.addLabel("IpUser", "Count of Users:", 0);
			mLabelView.addLabel("IpMac", "Count of MacAddresses:", 0);
			mLabelView.addLabel("IpDev", "Count of Devices:", 0);
		} else if(characteristic.equals(CharacteristicLabels.IP_AUTH)) {
			super.setComboBoxLabel("Timestamp: ");
			this.getTimeSelectionView().hideTimeSelectors();
		}
	}
	
	/**
	 * Sets the count of user of a ipaddress
	 * @param ipAddressCount the count of user of a ipaddress
	 * */
	public void setCountOfUsers(int userCount) {
		mLabelView.setValueById("IpUser", userCount);
	}
	
	/**
	 * Sets the count of macaddresses of a ipaddress
	 * @param ipAddressCount the count of macaddresses of a ipaddress
	 * */
	public void setCountOfMacAddresses(int macAddressCount) {
		mLabelView.setValueById("IpMac", macAddressCount);
	}
	
	/**
	 * Sets the count of devices of a ipaddress
	 * @param ipAddressCount the count of devices of a ipaddress
	 * */
	public void setCountOfDevices(int devCount) {
		mLabelView.setValueById("IpDev", devCount);
	}
	
	/**
	 * Fills a {@link ComboBox} with an {@link ArrayList} of ipAddresses
	 * @param ipAddresses ArrayList of ipAddresses
	 */
	public void setIpAddressItems(ArrayList<IpAddress> ipAddresses) {
		mComboBoxView.clear();
		for(IpAddress ip : ipAddresses) {
			if(!mComboBoxView.containsItem(ip.getAddress())) {
				mComboBoxView.addItem(ip.getAddress());
			}
		}
			
	}
	
	/**
	 * Sets an ArrayList of all ipAddresses
	 * @param ipAddresses
	 */
	public void setIpAddresses(ArrayList<IpAddress> ipAddresses) {
		mIpAddresses = ipAddresses;
	}
	
	/**
	 * @return An {@link ArrayList} which contains all ipAddresses 
	 */
	public IpAddress getIp(String ipAddress) {
		if(mIpAddresses == null) {
			return null;
		}
		for(IpAddress ip : mIpAddresses) {
			if(ip.getAddress().equals(ipAddress)) {
				return ip;
			}
		}
		return null;
	}

}
