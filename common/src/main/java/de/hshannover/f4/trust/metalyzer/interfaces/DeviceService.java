package de.hshannover.f4.trust.metalyzer.interfaces;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;


public interface DeviceService {
	/**
	 * Query to get all devices at the given Unix-Timestamp.
	 * @param timestamp
	 * Unix-Timestamp to which the devices should be searched.
	 * @return
	 * Returns a JSON-String of all devices at the given Unix-Timestamp.
	 */
	public ArrayList<Device> getAllDevices(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all devices at the given period of time.
	 */
	public ArrayList<Device> getAllDevicesFromTo(long fromTimestamp, long toTimestamp)throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all devices at the current timestamp.
	 */
	public ArrayList<Device> getCurrentDevices() throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	public long getTimestampForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	public long getTimestampForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific device.
	 * @param device 
	 * Device to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given device.
	 */
	public long getTimestampForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddressesForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddressesForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific device.
	 * @param device 
	 * Device to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given device.
	 */
	public ArrayList<IpAddress> getIpAddressesForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddressesForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddressesForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific device.
	 * @param device 
	 * Device to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given device.
	 */
	public ArrayList<MacAddress> getMacAddressesForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	public ArrayList<User> getUsersForDevice(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	public ArrayList<User> getUsersForDeviceCurrent(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific device.
	 * @param device 
	 * Device to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given device.
	 */
	public ArrayList<User> getUsersForDeviceFromTo(Device dev) throws InterruptedException, ExecutionException, TimeoutException;
}
