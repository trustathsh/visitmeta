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
import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;

/** 
 * Project: Metalyzer
 * Author: Anton Zaiser
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class DevicePanel extends SemanticPanel {

	private ArrayList<Device> mDevices;
	
	public DevicePanel(MetalyzerGuiController guiController, String characteristic) {
		super(guiController, characteristic);
		if(characteristic.equals(CharacteristicLabels.DEVICE_IP)) {
			super.setComboBoxLabel("Device: ");
			mLabelView.addLabel("DevIp", "Count of IpAddresses:", 0);
		} else if(characteristic.equals(CharacteristicLabels.DEVICE_MAC)) {
			super.setComboBoxLabel("Device: ");
			mLabelView.addLabel("DevMac", "Count of MacAddresses:", 0);
		} else if(characteristic.equals(CharacteristicLabels.DEVICE_USER)) {
			mLabelView.addLabel("DevUser", "Count of Users:", 0);
		} else if(characteristic.equals(CharacteristicLabels.DEVICE_ALL)) {
			super.setComboBoxLabel("Device: ");
			mLabelView.addLabel("DevIp", "Count of IpAddresses:", 0);
			mLabelView.addLabel("DevMac", "Count of MacAddresses:", 0);
			mLabelView.addLabel("DevUser", "Count of Users:", 0);
		} else if(characteristic.equals(CharacteristicLabels.DEVICE_AUTH)) {
			super.setComboBoxLabel("Timestamp: ");
			this.getTimeSelectionView().hideTimeSelectors();
		}
	}
	
	/**
	 * Sets the count of user of a device
	 * @param ipAddressCount the count of user of a device
	 * */
	public void setCountOfUsers(int userCount) {
		mLabelView.setValueById("DevUser", userCount);
	}
	
	/**
	 * Sets the count of macaddresses of a device
	 * @param ipAddressCount the count of macaddresses of a device
	 * */
	public void setCountOfMacAddresses(int macAddressCount) {
		mLabelView.setValueById("DevMac", macAddressCount);
	}
	
	/**
	 * Sets the count of ipaddresses of a device
	 * @param ipAddressCount the count of ipaddresses of a device
	 * */
	public void setCountOfIpAddresses(int ipAddressCount) {
		mLabelView.setValueById("DevIp", ipAddressCount);
	}
	
	/**
	 * Fills a {@link ComboBox} with an {@link ArrayList} of devices
	 * @param devices ArrayList of devices
	 */
	public void setDeviceItems(ArrayList<Device> devices) {
		mComboBoxView.clear();
		for(Device device : devices) {
			if(!mComboBoxView.containsItem(device.getName())) {
				mComboBoxView.addItem(device.getName());
			}
		}
			
	}
	
	/**
	 * Sets an ArrayList of all devices
	 * @param devices
	 */
	public void setDevices(ArrayList<Device> devices) {
		mDevices = devices;
	}

	/**
	 * @return An {@link ArrayList} which contains all devices 
	 */
	public Device getDevice(String device) {
		if(mDevices == null) {
			return null;
		}
		for(Device d : mDevices) {
			if(d.getName().equals(device)) {
				return d;
			}
		}
		return null;
	}
	
	/**
	 * @param attributes An ArrayList that contains all attributes of a device 
	 * @return A String that contains all attributes of a device comma separated
	 */
	public String printDeviceAttributes(ArrayList<String> attributes) {
		StringBuilder result = new StringBuilder();
		result.append("{");
		if(attributes.size() > 0) {
			result.append(attributes.get(0));
			for(int i = 1; i < attributes.size(); i++) {
				result.append(", ");
				result.append(attributes.get(i));
			}
		}
	
		result.append("}");	
		return result.toString();
	}

}
