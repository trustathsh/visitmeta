package de.hshannover.f4.trust.metalyzer.interfaces;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

public interface IpAddressService {
	/**
	 * Query to get all IP-Addresses at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the IP-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of all IP-Addresses at the given Unix-Timestamp.
	 */
	public ArrayList<IpAddress> getAllIpAddresses(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all IP-Addresses at the given period of time.
	 */
	public ArrayList<IpAddress> getAllIpAddressesFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all IP-Addresses at the current timestamp.
	 */
	public ArrayList<IpAddress> getCurrentIpAddresses() throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given IP-Address.
	 */
	public long getTimestampForIpAddress(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given IP-Address.
	 */
	public long getTimestampForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given IP-Address.
	 */
	public long getTimestampForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given IP-Address.
	 */
	public  ArrayList<User> getUsersForIpAddress(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given IP-Address.
	 */
	public  ArrayList<User> getUsersForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the users should be searched.
	 * @return
	 * Returns a JSON-String of the users of the given IP-Address.
	 */
	public ArrayList<User> getUsersForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given IP-Address.
	 */
	public ArrayList<Device> getDevicesForIpAddress(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given IP-Address.
	 */
	public ArrayList<Device> getDevicesForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given IP-Address.
	 */
	public ArrayList<Device> getDevicesForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;

	/**
	 * Query to get all MAC-Addresses of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the MAC-Addresses of the given IP-Address.
	 */
	public ArrayList<MacAddress> getMacAddressesForIpAddress(IpAddress ips) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the MAC-Addresses of the given IP-Address.
	 */
	public ArrayList<MacAddress> getMacAddressesForIpAddressFromTo(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific IP-Address.
	 * @param IpAddress 
	 * IP-Address to which the MAC-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the MAC-Addresses of the given IP-Address.
	 */
	public ArrayList<MacAddress> getMacAddressesForIpAddressCurrent(IpAddress ip) throws InterruptedException, ExecutionException, TimeoutException;
}
