package de.hshannover.f4.trust.visitmeta.dataservice.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.ifmap.Connection;
import de.hshannover.f4.trust.visitmeta.util.ConnectionKey;
import de.hshannover.f4.trust.visitmeta.util.PropertiesReaderWriter;

public class ConnectionsReaderWriter extends PropertiesReaderWriter {

	private static final Logger log = Logger.getLogger(ConnectionsReaderWriter.class);

	private static final String TRUSTSTORE_PATH = Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PATH);
	private static final String TRUSTSTORE_PASS = Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_TRUSTSTORE_PASS);
	private static final int MAX_SIZE = Integer.parseInt(Application.getIFMAPConfig().getProperty(ConfigParameter.IFMAP_MAX_SIZE));

	public static final String PREFIX = "dataservice.ifmap.connection.";

	public static final String KEY_NAME = PREFIX + ConnectionKey.NAME;
	public static final String KEY_URL = PREFIX + ConnectionKey.URL;
	public static final String KEY_USER_NAME = PREFIX + ConnectionKey.USER_NAME;
	public static final String KEY_USER_PASSWORD = PREFIX + ConnectionKey.USER_PASSWORD;
	public static final String KEY_AUTHENTICATION_BASIC = PREFIX + ConnectionKey.AUTHENTICATION_BASIC;
	public static final String KEY_TRUSTSTORE_PATH = PREFIX + ConnectionKey.TRUSTSTORE_PATH;
	public static final String KEY_TRUSTSTORE_PASS = PREFIX + ConnectionKey.TRUSTSTORE_PASS;
	public static final String KEY_STARTUP_CONNECT = PREFIX + ConnectionKey.STARTUP_CONNECT;
	public static final String KEY_STARTUP_DUMP = PREFIX + ConnectionKey.STARTUP_DUMP;
	public static final String KEY_MAX_POLL_RESULT_SIZE = PREFIX + ConnectionKey.MAX_POLL_RESULT_SIZE;

	public static final String KEY_CONNECTION_COUNT = PREFIX + "connection.count";

	public ConnectionsReaderWriter(String fileName, boolean create) throws IOException {
		super(fileName, create);
	}

	public List<ConnectionData> getConnectionsData(){
		ArrayList<ConnectionData> connectionsList = new ArrayList<ConnectionData>();

		for(int i=0; i< getSize(); i++){
			// get required values
			String name = getProperty(KEY_NAME + "." + i);
			String url = getProperty(KEY_URL + "." + i);
			String userName = getProperty(KEY_USER_NAME + "." + i);
			String userPassword = getProperty(KEY_USER_PASSWORD + "." + i);

			// get optional values
			boolean authenticationBasic = Boolean.valueOf(getProperty(KEY_AUTHENTICATION_BASIC + "." + i));
			String truststorePath = getProperty(KEY_TRUSTSTORE_PATH + "." + i, TRUSTSTORE_PATH);
			String truststorePass = getProperty(KEY_TRUSTSTORE_PASS + "." + i, TRUSTSTORE_PASS);
			int maxPollResultSize = Integer.valueOf(getProperty(KEY_MAX_POLL_RESULT_SIZE + "." + i, String.valueOf(MAX_SIZE)));
			boolean startupConnect = Boolean.valueOf(getProperty(KEY_STARTUP_CONNECT + "." + i));
			boolean startupDump = Boolean.valueOf(getProperty(KEY_STARTUP_DUMP + "." + i));

			// build new Connection
			ConnectionData tmpConnection = new ConnectionData(name, url, userName, userPassword);

			//  =  optional values
			if(containsKey(KEY_AUTHENTICATION_BASIC + "." + i)){
				tmpConnection.mAuthenticationBasic = authenticationBasic;
			}

			if(containsKey(KEY_TRUSTSTORE_PATH + "." + i)){
				tmpConnection.mTruststorePath = truststorePath;
			}

			if(containsKey(KEY_TRUSTSTORE_PASS + "." + i)){
				tmpConnection.mTruststorePass = truststorePass;
			}

			if(containsKey(KEY_MAX_POLL_RESULT_SIZE + "." + i)){
				tmpConnection.mMaxPollResultSize = maxPollResultSize;
			}

			if(containsKey(KEY_STARTUP_CONNECT + "." + i)){
				tmpConnection.mStartupConnect = startupConnect;
			}

			if(containsKey(KEY_STARTUP_DUMP + "." + i)){
				tmpConnection.mStartupDump = startupDump;
			}

			// add to return list
			connectionsList.add(tmpConnection);
		}
		return connectionsList;
	}


	public void persistConnection(Connection connection) throws IOException{
		log.trace("persist connecton: " + connection.getConnectionName() + " ...");
		int index = getConnectionIndex(connection.getConnectionName());

		// store required values
		log.trace(connection.getConnectionName() + ": store required values");

		storeProperty(KEY_NAME + "." + index, connection.getConnectionName());
		storeProperty(KEY_URL + "." + index, connection.getUrl());
		storeProperty(KEY_USER_NAME + "." + index, connection.getUserName());
		storeProperty(KEY_USER_PASSWORD + "." + index, connection.getUserPass());

		// store optional values ​​only if different from the default
		log.trace(connection.getConnectionName() + ": store optional values");

		if(connection.isAuthenticationBasic() != Connection.DEFAULT_AUTHENTICATION_BASIC){
			storeProperty(KEY_AUTHENTICATION_BASIC + "." + index, String.valueOf(connection.isAuthenticationBasic()));
		}

		if(!connection.getTruststorePath().equals(Connection.DEFAULT_TRUSTSTORE_PATH)){
			storeProperty(KEY_TRUSTSTORE_PATH + "." + index, connection.getTruststorePath());
		}

		if(!connection.getTruststorePass().equals(Connection.DEFAULT_TRUSTSTORE_PASS)){
			storeProperty(KEY_TRUSTSTORE_PASS + "." + index, connection.getTruststorePass());
		}

		if(connection.getMaxPollResultSize() != Connection.DEFAULT_MAX_POLL_RESULT_SIZE){
			storeProperty(KEY_MAX_POLL_RESULT_SIZE + "." + index, String.valueOf(connection.getMaxPollResultSize()));
		}

		if(connection.isStartupConnect() != Connection.DEFAULT_STARTUP_CONNECT){
			storeProperty(KEY_STARTUP_CONNECT + "." + index, String.valueOf(connection.isStartupConnect()));
		}

		if(connection.isStartupDump() != Connection.DEFAULT_STARTUP_DUMP){
			storeProperty(KEY_STARTUP_DUMP + "." + index, String.valueOf(connection.isStartupDump()));
		}

		if(index >= getSize()){
			incrementSize();
		}


		log.debug("connecton: " + connection.getConnectionName() + " was persisted");
	}

	private void persistConnectionData(ConnectionData data) throws IOException{
		int index = getConnectionIndex(data.mConnectionName);

		storeProperty(KEY_NAME + "." + index, data.mConnectionName);
		storeProperty(KEY_URL + "." + index, data.mUrl);
		storeProperty(KEY_USER_NAME + "." + index, data.mUserName);
		storeProperty(KEY_USER_PASSWORD + "." + index, data.mUserPass);
		storeProperty(KEY_AUTHENTICATION_BASIC + "." + index, String.valueOf(data.mAuthenticationBasic));
		storeProperty(KEY_TRUSTSTORE_PATH + "." + index, data.mTruststorePath);
		storeProperty(KEY_TRUSTSTORE_PASS + "." + index, data.mTruststorePass);
		storeProperty(KEY_MAX_POLL_RESULT_SIZE + "." + index, String.valueOf(data.mMaxPollResultSize));
		storeProperty(KEY_STARTUP_CONNECT + "." + index, String.valueOf(data.mStartupConnect));
		storeProperty(KEY_STARTUP_DUMP + "." + index, String.valueOf(data.mStartupDump));

		if(index >= getSize()){
			incrementSize();
		}
	}

	public void deleteConnection(Connection connection) throws IOException{
		deleteConnection(getConnectionIndex(connection.getConnectionName()));
	}

	public void deleteConnection(int index) throws IOException{
		clear();
		List<ConnectionData> tmpList = getConnectionsData();
		// remove parameter index from tmpList
		tmpList.remove(index);

		// save connection list
		for(ConnectionData data: tmpList){
			persistConnectionData(data);
		}

		log.trace("Connection was deleted");
	}

	private void clear(){
		// remove all from properties
		for(int i=0; i<getSize() ; i++){
			removeProperty(KEY_NAME + "." + i);
			removeProperty(KEY_URL + "." + i);
			removeProperty(KEY_USER_NAME + "." + i);
			removeProperty(KEY_USER_PASSWORD + "." + i);
			removeProperty(KEY_AUTHENTICATION_BASIC + "." + i);
			removeProperty(KEY_TRUSTSTORE_PATH + "." + i);
			removeProperty(KEY_TRUSTSTORE_PASS + "." + i);
			removeProperty(KEY_MAX_POLL_RESULT_SIZE + "." + i);
			removeProperty(KEY_STARTUP_CONNECT + "." + i);
			removeProperty(KEY_STARTUP_DUMP + "." + i);
		}
		removeProperty(KEY_CONNECTION_COUNT);
	}

	private int getSize(){
		return Integer.valueOf(getProperty(KEY_CONNECTION_COUNT, "0"));
	}

	private void incrementSize() throws IOException{
		int size = getSize();
		storeProperty(KEY_CONNECTION_COUNT, String.valueOf(size + 1));
	}

	private void decrementSize() throws IOException{
		int size = getSize() == 0 ? 0 : getSize() - 1;
		storeProperty(KEY_CONNECTION_COUNT, String.valueOf(size));
	}

	public int getConnectionIndex(String connectionName){
		int size = getSize();
		for(int i=0; i < size; i++){
			if(getProperty(KEY_NAME + "." + i).equals(connectionName)){
				return i;
			}
		}
		return size;
	}
}
