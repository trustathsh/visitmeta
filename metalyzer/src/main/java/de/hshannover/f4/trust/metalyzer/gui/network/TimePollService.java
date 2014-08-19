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

import java.net.URI;
import java.util.Observable;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.hshannover.f4.trust.metalyzer.gui.misc.MessageBox;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class TimePollService extends Observable implements Runnable {

	private static final Logger log = Logger.getLogger(TimePollService.class);
	private static final long REQUEST_TIMEOUT_SECONDS = 1;
	private static final long POLL_INTERVALL_SECONDS = 4;

	private ScheduledExecutorService mService;
	private AsyncWebResource mRessource;
	private Gson mGson;
	private ClientResponse mResponse;
	private Future<ClientResponse> mFuture;

	private volatile boolean hasConnection = true;
	private volatile boolean doExit = false;
	
	private long mInitalDelay;
	private long mDelay = POLL_INTERVALL_SECONDS;
	private long mTimeout = REQUEST_TIMEOUT_SECONDS;

	public TimePollService(String url, String connection) {
		mGson = new Gson();
		mRessource = createRessource(url, connection);
		
		log.debug("TimePollService initialized on url:  " + url+ " and connection: " + connection);

		mFuture = mRessource.path("graph").path("changes").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		try {
			mResponse = mFuture.get(2, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			log.error(e);
			doExit = MessageBox.showErrorDialogRetryExit("Connection Error","The connection to the dataservice\n could not be established.");
			hasConnection = false;
		} finally {
			if (!hasConnection) {
				if(doExit) {
					shutdownAndExit();
				}
			} 
		}

	}
	
	/**
	 * Sets the delay of the polling service
	 * @param delay sets the amount of seconds for the fixed delay of the polling service.
	 * The default value is 4 seconds, a new delay could not fall be low this
	 * */
	public void setPollingDelay(long delay) {
		this.mDelay = Math.max(POLL_INTERVALL_SECONDS, delay);
	}
	
	/**
	 * Returns the polling delay
	 *@return the delay in seconds
	 * */
	public long getPollingDelay() {
		return mDelay;
	}
	
	/**
	 * Sets the request time out of the polling service
	 * @param timeout the time in seconds, that the request tries to complete, before the
	 * abort. The default value is 1 seconds, a new timeout could not fall be low this
	 * */
	public void setRequestTimeout(long timeout) {
		this.mTimeout = Math.max(POLL_INTERVALL_SECONDS, timeout);
	}
	
	/**
	 * Returns the request timeout
	 * @return timeout in seconds
	 * */
	public long getRequestTimeout() {
		return this.mTimeout;
	}

	/**
	 * Start the {@link ScheduledExecutorService} with a fixed delay of
	 * seconds and an initial delay of zero seconds.
	 * 
	 * @see ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long,
	 *      long, TimeUnit)
	 * */
	public void start() {
		start(0, mDelay);
	}

	/**
	 * Start the {@link ScheduledExecutorService} with a fixed delay and an
	 * initial delay of zero seconds.
	 * 
	 * @param delay
	 *            the delay of poll service
	 * @see ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long,
	 *      long, TimeUnit)
	 * */
	public void start(long delay) {
		start(0, delay);
	}

	/**
	 * Start the {@link ScheduledExecutorService} with a fixed delay and an
	 * initial delay.
	 * 
	 * @param initialDelay
	 *            the time to delay first execution of the poll service
	 * @param delay
	 *            the delay between execution of the poll service
	 * @see ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long,
	 *      long, TimeUnit)
	 * */
	public void start(long initialDelay, long delay) {
		this.mInitalDelay = initialDelay;
		this.mDelay = Math.max(POLL_INTERVALL_SECONDS, delay);
		
		log.debug("TimePollService started, with initial delay: "+ mInitalDelay + " and tick delay: " + mDelay);
		mService = Executors.newSingleThreadScheduledExecutor();
		mService.scheduleWithFixedDelay(this, this.mInitalDelay, this.mDelay , TimeUnit.SECONDS);
	}

	/**
	 * Shutdown the execution of the poll service
	 * */
	public void shutdown() {
		log.debug("TimePollService is shutting down");
		mService.shutdown();
	}
	
	/**
	 * Shutdown the {@link TimePollService} and closes the application
	 * */
	public void shutdownAndExit() {
		log.debug("Shutting down TimePollService and Exit Application");
		if(mService != null) {
			mService.shutdown();
		}
		System.exit(0);
	}

	/**
	 * Returns a list of timestamps
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @return timestamp list
	 * */
	public void poll() {
		if (hasConnection && !doExit) {
			mFuture = mRessource.path("graph").path("changes")
					.accept(MediaType.APPLICATION_JSON)
					.get(ClientResponse.class);

			try {
				mResponse = mFuture.get(mTimeout, TimeUnit.SECONDS);
				SortedMap<Long, Long> changes = mGson.fromJson(
						mResponse.getEntity(String.class),
						new TypeToken<SortedMap<Long, Long>>() {
						}.getType());
				SortedSet<Long> timestamps = (SortedSet<Long>) changes.keySet();
				setChanged();
				notifyObservers(timestamps);
			} catch (InterruptedException e) {
				hasConnection = false;
				log.error(e);
				doExit = MessageBox.showErrorDialogRetryExit("Interruption Error","Request was interrupted.");
			} catch (ExecutionException e) {
				hasConnection = false;
				log.error(e);
				doExit = MessageBox.showErrorDialogRetryExit("Execution Error","Request could not executed.");
			} catch (TimeoutException e) {
				hasConnection = false;
				log.error(e);
				doExit = MessageBox.showErrorDialogRetryExit("Connection Error","Connection lost.");
			} finally {
				if (!hasConnection) {
					if(doExit) {
						shutdownAndExit();
					} else {
						mService.shutdown();
						// tries restart if connection is lost during the polling request
						log.debug("Connection Restart");
						start();
						hasConnection = true;
					}
				} 
			}
		} else if(doExit) {
			shutdownAndExit();
		} else {
			mService.shutdown();
			log.debug("Initial Restart");
			// tries restart if inital connection failed.
			start();
			hasConnection = true;
		}
	}

	private AsyncWebResource createRessource(String url, String connection) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		String u = url + "/" + connection;
		URI uri = UriBuilder.fromUri(u).build();
		return client.asyncResource(uri);
	}

	@Override
	public void run() {
		log.debug("polling ...");
		poll();
	}

}
