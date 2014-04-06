package de.hshannover.f4.trust.visitmeta.dataservice.util;

import de.hshannover.f4.trust.visitmeta.ifmap.Connection;


/**
 * ConnectionData is a data-class for the Connection-class.
 * It used by the ConnectionsReaderWriter.class to load from property.
 *
 */
public class ConnectionData {

	/**
	 * required connection value
	 */
	public String mConnectionName;
	/**
	 * required connection value
	 */
	public String mUrl;
	/**
	 * required connection value
	 */
	public String mUserName;
	/**
	 * required connection value
	 */
	public String mUserPass;

	/**
	 * optional connection value
	 */
	public boolean mAuthenticationBasic;
	/**
	 * optional connection value
	 */
	public String mTruststorePath;
	/**
	 * optional connection value
	 */
	public String mTruststorePass;
	/**
	 * optional connection value
	 */
	public int mMaxPollResultSize;
	/**
	 * optional connection value
	 */
	public boolean mStartupConnect;
	/**
	 * optional connection value
	 */
	public boolean mStartupDump;


	public ConnectionData(String connectionName, String url, String userName, String userPass){
		mConnectionName = connectionName;
		mUrl = url;
		mUserName = userName;
		mUserPass = userPass;

		mAuthenticationBasic = Connection.DEFAULT_AUTHENTICATION_BASIC;
		mTruststorePath = Connection.DEFAULT_TRUSTSTORE_PATH;
		mTruststorePass = Connection.DEFAULT_TRUSTSTORE_PASS;
		mMaxPollResultSize = Connection.DEFAULT_MAX_POLL_RESULT_SIZE;
		mStartupConnect = Connection.DEFAULT_STARTUP_CONNECT;
		mStartupDump = Connection.DEFAULT_STARTUP_DUMP;
	}

}
