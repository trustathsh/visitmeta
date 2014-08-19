package de.hshannover.f4.trust.metalyzer.interfaces;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

public interface MacAddressService {
	/**
	 * Query to get all MAC-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the given Unix-Timestamp.
	 */
	public ArrayList<MacAddress> getAllMacAddresses(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all MAC-Addresses at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the given period of time.
	 */
	public ArrayList<MacAddress> getAllMacAddressesFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all MAC-Addresses at the current timestamp.
	 */
	public ArrayList<MacAddress> getCurrentMacAddresses() throws InterruptedException, ExecutionException, TimeoutException;
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestampForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestampForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given MAC-Address.
	 */
	public long getTimestampForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	public ArrayList<User> getUsersForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	public ArrayList<User> getUsersForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given MAC-Address.
	 */
	public ArrayList<User> getUsersForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevicesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevicesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given MAC-Address.
	 */
	public ArrayList<Device> getDevicesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddressesForMacAddress(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddressesForMacAddressFromTo(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific MAC-Address.
	 * @param MacAddress 
	 * MAC-Address to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given MAC-Address.
	 */
	public ArrayList<IpAddress> getIpAddressesForMacAddressCurrent(MacAddress mac) throws InterruptedException, ExecutionException, TimeoutException;
}
