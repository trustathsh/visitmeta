package de.hshannover.f4.trust.metalyzer.interfaces;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import de.hshannover.f4.trust.metalyzer.semantics.entities.Device;
import de.hshannover.f4.trust.metalyzer.semantics.entities.IpAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.MacAddress;
import de.hshannover.f4.trust.metalyzer.semantics.entities.User;

public interface UserService {
	/**
	 * Query to get all users at the given Unix-Timestamp.
	 * @param timestamp 
	 * Unix-Timestamp to which the users should be searched.
	 * @return 
	 * Returns a JSON-String of all users at the given Unix-Timestamp.
	 */

	public ArrayList<User> getAllUsers(long timestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users at the given period of time.
	 * @param fromTimestamp 
	 * Unix-Timestamp of the beginning of the search-period.
	 * @param toTimestamp
	 * Unix-Timestamp of the end of the search-period.
	 * @return 
	 * Returns a JSON-String of all users at the given period of time.
	 */
	public ArrayList<User> getAllUsersFromTo(long fromTimestamp, long toTimestamp) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all users at the current timestamp.
	 * @return 
	 * Returns a JSON-String of all users at the current timestamp.
	 */
	public ArrayList<User> getCurrentUsers() throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific user.
	 * @param user 
	 * User to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given user.
	 */
	public long getTimestampForUser(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific user.
	 * @param user 
	 * User to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given user.
	 */
	public long getTimestampForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get the Unix-Timestamp of a specific user.
	 * @param user 
	 * User to which the Unix-Timestamp should be searched.
	 * @return 
	 * Returns a JSON-String of the Unix-Timestamp of the given user.
	 */
	public long getTimestampForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific user.
	 * @param user 
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given user.
	 */
	public ArrayList<IpAddress> getIpAddressesForUser(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific user.
	 * @param user 
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given user.
	 */
	public ArrayList<IpAddress> getIpAddressesForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all IP-Addresses of a specific user.
	 * @param user 
	 * User to which the IP-Addresses should be searched.
	 * @return
	 * Returns a JSON-String of the IP-Addresses of the given user.
	 */
	public ArrayList<IpAddress> getIpAddressesForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific user.
	 * @param user 
	 * User to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given user.
	 */
	public ArrayList<MacAddress> getMacAddressesForUser(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * !!! At the moment this method returns an incorrect result because of an unexpected implementation of delta-methods !!!
	 * Query to get all MAC-Addresses of a specific user.
	 * @param user 
	 * User to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given user.
	 */
	public ArrayList<MacAddress> getMacAddressesForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all MAC-Addresses of a specific user.
	 * @param user 
	 * User to which the MAC-Addresses should be searched.
	 * @return 
	 * Returns a JSON-String of the MAC-Addresses of the given user.
	 */
	public ArrayList<MacAddress> getMacAddressesForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific user.
	 * @param user 
	 * User to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given user.
	 */
	public ArrayList<Device> getDevicesForUser(User user) throws InterruptedException, ExecutionException, TimeoutException;
	
	/**
	 * Query to get all devices of a specific user.
	 * @param user 
	 * User to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given user.
	 */
	public ArrayList<Device> getDevicesForUserFromTo(User user) throws InterruptedException, ExecutionException, TimeoutException;
	/**
	 * Query to get all devices of a specific user.
	 * @param user 
	 * User to which the devices should be searched.
	 * @return 
	 * Returns a JSON-String of the devices of the given user.
	 */
	public ArrayList<Device> getDevicesForUserCurrent(User user) throws InterruptedException, ExecutionException, TimeoutException;
}
