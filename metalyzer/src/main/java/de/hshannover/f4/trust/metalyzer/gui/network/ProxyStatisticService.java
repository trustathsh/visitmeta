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

import java.util.HashMap;
import java.util.Map;
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

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.gui.misc.MessageBox;
import de.hshannover.f4.trust.metalyzer.interfaces.StatisticService;
import de.hshannover.f4.trust.metalyzer.statistic.EdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.metalyzer.statistic.MeanOfEdgeResult;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;
import de.hshannover.f4.trust.metalyzer.statistic.NodeResult;
import de.hshannover.f4.trust.metalyzer.statistic.ResultObject;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class ProxyStatisticService implements StatisticService {

	private static final int REQUEST_TIMEOUT = 2;
	private Future<ClientResponse> mFuture; 
	private ClientResponse mClientResponse;
	private AsyncWebResource mAnalyseRessource;
	private Gson mGson;
	
	public ProxyStatisticService(AsyncWebResource analyseRessource) {
		this.mAnalyseRessource = analyseRessource.path("statistics");
		mGson = new Gson();
	}
	
	@Override
	public double getMeanCurrent(StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException {
		double mean = 0;
		mFuture = mAnalyseRessource
				.path("evaluateMean")
				.path("current")
				.path("filter")
				.path(filter.toString())
				.path("type")
				.path(type.toString())					
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			mean = mGson.fromJson(mClientResponse.getEntity(String.class), Double.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The mean information not available.", error);
		}
		return mean;
	}
	
	@Override
	public double getMeanTimestamp(long timestamp, StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException {
		double mean = 0;
		mFuture = mAnalyseRessource
				.path("evaluateMean")
				.path("timestamp")
				.path(mGson.toJson(timestamp))
				.path("filter")
				.path(filter.toString())
				.path("type")
				.path(type.toString())					
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			mean = mGson.fromJson(mClientResponse.getEntity(String.class), Double.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The mean information not available.", error);
		}
		return mean;
	}
	
	@Override
	public double getMeanFromTo(long from, long to, StandardIdentifierType filter, MeanType type) throws InterruptedException, ExecutionException, TimeoutException {
		double mean = 0;
		mFuture = mAnalyseRessource
				.path("evaluateMean")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.path("filter")
				.path(filter.toString())
				.path("type")
				.path(type.toString())					
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			mean = mGson.fromJson(mClientResponse.getEntity(String.class), Double.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The mean information not available.", error);
		}
		return mean;
	}

	@Override
	public Map<String, Double> getFrequencyCurrent(FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException {
		Map<String,Double> frequency = null;
		mFuture = mAnalyseRessource
				.path("evaluateFrequency")
				.path("current")
				.path("type")
				.path(type.toString())
				.path("selection")
				.path(selection.toString())
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			frequency = mGson.fromJson(mClientResponse.getEntity(String.class), new TypeToken<HashMap<String,Double>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The frequency information not available.", error);
		}
		return frequency;
	}
	
	@Override
	public Map<String, Double> getFrequencyTimestamp(long timestamp, FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException {
		Map<String,Double> frequency = null;
		mFuture = mAnalyseRessource
				.path("evaluateFrequency")
				.path("timestamp")
				.path(mGson.toJson(timestamp))
				.path("type")
				.path(type.toString())
				.path("selection")
				.path(selection.toString())
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			frequency = mGson.fromJson(mClientResponse.getEntity(String.class), new TypeToken<HashMap<String,Double>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The frequency information not available.", error);
		}
		return frequency;
	}

	@Override
	public Map<String, Double> getFrequencyFromTo(long from, long to, FrequencyType type, FrequencySelection selection) throws InterruptedException, ExecutionException, TimeoutException {
		Map<String,Double> frequency = null;
		mFuture = mAnalyseRessource
				.path("evaluateFrequency")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.path("type")
				.path(type.toString())
				.path("selection")
				.path(selection.toString())
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			frequency = mGson.fromJson(mClientResponse.getEntity(String.class), new TypeToken<HashMap<String,Double>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The frequency information not available.", error);
		}
		return frequency;
	}
	
	@Override
	public int getIdentifierCount() throws InterruptedException,
			ExecutionException, TimeoutException {
		int identiferCount = 0;
		mFuture = mAnalyseRessource
				.path("sumOfIdentifier")
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			identiferCount = mGson.fromJson(mClientResponse.getEntity(String.class), Integer.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The tdentifier count information not available.", error);
		}
		return identiferCount;
	}
	
	@Override
	public int getIdentifierCount(long timestamp) throws InterruptedException,
			ExecutionException, TimeoutException {
		int identiferCount = 0;
		mFuture = mAnalyseRessource
				.path("sumOfIdentifier")
				.path("timestamp")
				.path(mGson.toJson(timestamp))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			identiferCount = mGson.fromJson(mClientResponse.getEntity(String.class), Integer.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The identifier count information not available.", error);
		}
		return identiferCount;
	}

	@Override
	public int getIdentifierCount(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		int identiferCount = 0;
		mFuture = mAnalyseRessource
				.path("sumOfIdentifier")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			identiferCount = mGson.fromJson(mClientResponse.getEntity(String.class), Integer.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The identifier count information not available.", error);
		}
		return identiferCount;
	}

	@Override
	public int getMetadataCount() throws InterruptedException,
			ExecutionException, TimeoutException {
		int metadataCount = 0;
		mFuture = mAnalyseRessource
				.path("sumOfMetadata")
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);	 
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			metadataCount = mGson.fromJson(mClientResponse.getEntity(String.class), Integer.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The metadata count information not available.", error);
		}
		return metadataCount;
	}
	
	@Override
	public int getMetadataCount(long timestamp) throws InterruptedException,
			ExecutionException, TimeoutException {
		int metadataCount = 0;
		mFuture = mAnalyseRessource
				.path("sumOfMetadata")
				.path("timestamp")
				.path(mGson.toJson(timestamp))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);	 
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			metadataCount = mGson.fromJson(mClientResponse.getEntity(String.class), Integer.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The metadata count information not available.", error);
		}
		return metadataCount;
	}

	
	/**
	 * Returns the count of metadata at given timestamp interval from and to
	 * @param from
	 * @param to
	 * @return metadataCount
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@Override
	public int getMetadataCount(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		int metadataCount = 0;
		mFuture = mAnalyseRessource
				.path("sumOfMetadata")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);	 
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			metadataCount = mGson.fromJson(mClientResponse.getEntity(String.class), Integer.class);
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The metadata count information not available.", error);
		}
		return metadataCount;
	}

	@Override
	public ResultObject<NodeResult> getNodeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		ResultObject<NodeResult> result = null;
		mFuture = mAnalyseRessource
				.path("nodeCharecteristic")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);	 
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			result = mGson.fromJson(mClientResponse.getEntity(String.class), new TypeToken<ResultObject<NodeResult>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The node information for this graph is not available.", error);
		}
		return result;
	}
	
	@Override
	public ResultObject<EdgeResult> getEdgeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		ResultObject<EdgeResult> result = null;
		mFuture = mAnalyseRessource
				.path("edgeCharecteristic")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);	 
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			result = mGson.fromJson(mClientResponse.getEntity(String.class), new TypeToken<ResultObject<EdgeResult>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The edge information for this graph is not available.", error);
		}
		return result;
	}
	
	@Override
	public ResultObject<MeanOfEdgeResult> getMeanOfEdgeCharecteristic(long from, long to) throws InterruptedException, ExecutionException, TimeoutException {
		ResultObject<MeanOfEdgeResult> result = null;
		mFuture = mAnalyseRessource
				.path("meanOfEdgeCharecteristic")
				.path("from")
				.path(mGson.toJson(from))
				.path("to")
				.path(mGson.toJson(to))
				.accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);
		
		mClientResponse = mFuture.get(REQUEST_TIMEOUT, TimeUnit.SECONDS);	 
		Status status = mClientResponse.getClientResponseStatus();
		if(status == Status.OK) {
			result = mGson.fromJson(mClientResponse.getEntity(String.class), new TypeToken<ResultObject<MeanOfEdgeResult>>(){}.getType());
		} else {
			String error = mClientResponse.getEntity(String.class);
			MessageBox.showErrorOutputDialog(status.getReasonPhrase(), "The mean of edge information for this graph is not available.", error);
		}
		return result;
	}
}
