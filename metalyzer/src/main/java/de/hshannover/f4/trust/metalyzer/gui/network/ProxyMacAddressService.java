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
package de.hshannover.f4.trust.metalyzer.gui.network;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import de.hshannover.f4.trust.metalyzer.gui.misc.MessageBox;
import de.hshannover.f4.trust.metalyzer.gui.misc.UtilHelper;
import de.hshannover.f4.trust.metalyzer.interfaces.MacAddressService;
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

public class ProxyMacAddressService implements MacAddressService {

	private static final int REQUEST_TIMEOUT = 2;
	private Future<ClientResponse> mFuture; 
	private ClientResponse mClientResponse;
	private AsyncWebResource mAnalyseRessource;
	private Gson mGson;
	
	public ProxyMacAddressService(AsyncWebResource analyseRessource) {
		this.mAnalyseRessource = analyseRessource.path("semantics").path("macaddress");
		mGson = new Gson();
	}
	
	@Override
	public ArrayList<MacAddress> getAllMacAddresses(long timestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macaddresses = null;
		
		mFuture = mAnalyseRessource
				.path("macaddresses")
				.path(mGson.toJson(timestamp))
				.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			macaddresses = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<MacAddress>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No macaddresses for timestamp "+UtilHelper.convertTimestampToDatestring(timestamp) + " were found", error);
		}
		return macaddresses;
	}

	@Override
	public ArrayList<MacAddress> getAllMacAddressesFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macaddresses = null;
		
		mFuture = mAnalyseRessource
				.path("macaddresses")
				.path(mGson.toJson(fromTimestamp))
				.path(mGson.toJson(toTimestamp))
				.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			macaddresses = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<MacAddress>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),
					"No macaddresses for time intervall "+UtilHelper.convertTimestampToDatestring(fromTimestamp)+" "+UtilHelper.convertTimestampToDatestring(toTimestamp)+" were found", error);
		}
		return macaddresses;
	}

	@Override
	public ArrayList<MacAddress> getCurrentMacAddresses() throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<MacAddress> macaddresses = null;
		mFuture = mAnalyseRessource
				.path("macaddresses")
				.path("current")
				.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			macaddresses = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<MacAddress>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),
					"No macaddresses for the current timestamp were found", error);
		}
		return macaddresses;
	}

	@Override
	public long getTimestampForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = 0;
		mFuture = mAnalyseRessource
				.path("timestamps")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			timestamp = mGson.fromJson(mClientResponse.getEntity(String.class), Long.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No timestamp for macaddress "+mac.getAddress()+" was found", error);
		}
		return timestamp;
	}

	@Override
	public long getTimestampForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = 0;
		mFuture = mAnalyseRessource
				.path("timestamps")
				.path("fromto")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			timestamp = mGson.fromJson(mClientResponse.getEntity(String.class), Long.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No timestamp for macaddress "+mac.getAddress()+" was found", error);
		}
		return timestamp;
	}

	@Override
	public long getTimestampForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		long timestamp = 0;
		mFuture = mAnalyseRessource
				.path("timestamps")
				.path("current")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			timestamp = mGson.fromJson(mClientResponse.getEntity(String.class), Long.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No timestamp for macaddress "+mac.getAddress()+" was found", error);
		}
		return timestamp;
	}

	@Override
	public ArrayList<User> getUsersForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = null;
		mFuture = mAnalyseRessource
				.path("users")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			users = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<User>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No users for macaddress "+mac.getAddress()+" were found", error);
		}
		return users;
	}

	@Override
	public ArrayList<User> getUsersForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = null;
		mFuture = mAnalyseRessource
				.path("users")
				.path("fromto")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			users = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<User>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No users for macaddress "+mac.getAddress()+" were found", error);
		}
		return users;
	}

	@Override
	public ArrayList<User> getUsersForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<User> users = null;
		mFuture = mAnalyseRessource
				.path("users")
				.path("current")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			users = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<User>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No users for macaddress "+mac.getAddress()+" were found", error);
		}
		return users;
	}

	@Override
	public ArrayList<Device> getDevicesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = null;
		mFuture = mAnalyseRessource
				.path("devices")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			devices = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<Device>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No devices for macaddress "+mac.getAddress()+" were found", error);
		}
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = null;
		mFuture = mAnalyseRessource
				.path("devices")
				.path("fromto")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			devices = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<Device>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No devices for macaddress "+mac.getAddress()+" were found", error);
		}
		return devices;
	}

	@Override
	public ArrayList<Device> getDevicesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<Device> devices = null;
		mFuture = mAnalyseRessource
				.path("devices")
				.path("current")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			devices = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<Device>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No devices for macaddress "+mac.getAddress()+" were found", error);
		}
		return devices;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipaddresses = null;
		mFuture = mAnalyseRessource
				.path("ipaddresses")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			ipaddresses = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<IpAddress>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No ipaddresses for macaddress "+mac.getAddress()+" were found", error);
		}
		return ipaddresses;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipaddresses = null;
		mFuture = mAnalyseRessource
				.path("ipaddresses")
				.path("fromto")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			ipaddresses = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<IpAddress>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No ipaddresses for macaddress "+mac.getAddress()+" were found", error);
		}
		return ipaddresses;
	}

	@Override
	public ArrayList<IpAddress> getIpAddressesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException {
		ArrayList<IpAddress> ipaddresses = null;
		mFuture = mAnalyseRessource
				.path("ipaddresses")
				.path("current")
				.accept(MediaType.TEXT_PLAIN)
				.put(ClientResponse.class, mGson.toJson(mac));
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			ipaddresses = mGson.fromJson(mClientResponse.getEntity(String.class),new TypeToken<ArrayList<IpAddress>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(),"No ipaddresses for macaddress "+mac.getAddress()+" were found", error);
		}
		return ipaddresses;
	}

	
}
