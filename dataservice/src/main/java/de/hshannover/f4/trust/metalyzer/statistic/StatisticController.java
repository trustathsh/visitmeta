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
 * This file is part of visitmeta dataservice, version 0.1.2,
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
/**
 * Project: Metalyzer 
 * Author: Hassan Nahle, Juri Seewald
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */


package de.hshannover.f4.trust.metalyzer.statistic;
import java.util.ArrayList;
import java.util.HashMap;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerAPI;
import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;


public class StatisticController {
	
    private static StatisticController statController;
    
    private String connectionName;
    
    private StatisticController() {
    	// do nothing
    }
    
    /**
	 * @return  Returns an object of the StatisticController.
	 */
    public static synchronized StatisticController getInstance() {
    	if (statController == null) {
                statController = new StatisticController();
        }
    	
        return statController;
    }

	/**
	 * To get the connection to MetalyzerAPI.
	 * @return an object of the MetalyzerAPI.
	 */	
	public MetalyzerAPI getAPI() {
		if (connectionName != null) {
			return MetalyzerAPI.getInstance(connectionName);
		} else {
			return MetalyzerAPI.getInstance(MetalyzerAPI.DEFAULT_CONNECTION);
		}
	}
    
    /**
     * Set metalyzerAPI to specified connection
     */
    public void setConnection(String connectionName){
    	if (connectionName != null) {
    		this.connectionName = connectionName;
    	}
    }

    /**
     * evaluate the mean of different calculations
     * @author Hassan Nahle, Juri Seewald
     * @param from start time
     * @param to end time
     * @param filter Chooses the characteristic (for example: CHANGEROLE)
     * @param type
     * @return returns a result (as a double value) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public double evaluateMean(long from, long to,StandardIdentifierType filter, MeanType type) throws MetalyzerAPIException{
    	return calculateMean(from, to, filter, type);
    }
    
    /**
     * evaluate the mean of different calculations at current timestamp
     * @author Juri Seewald     
     * @param filter Chooses the characteristic (for example: CHANGEROLE)
     * @param type
     * @return returns a result (as a double value) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public double evaluateMean(StandardIdentifierType filter, MeanType type) throws MetalyzerAPIException{    	
    	return calculateMean(0, 0, filter, type);
    }
    
    /**
     * evaluate the mean of different calculations at given timestamp
     * @author Juri Seewald 
     * @param timestamp    
     * @param filter Chooses the characteristic (for example: CHANGEROLE)
     * @param type
     * @return returns a result (as a double value) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public double evaluateMean(long timestamp, StandardIdentifierType filter, MeanType type) throws MetalyzerAPIException{
    	return calculateMean(timestamp, 0, filter, type);
    }

    /**
     * evaluate the frequency (relative and absolute) of different calculations
     * @author Hassan Nahle, Juri Seewald
     * @param from start time
     * @param to end time 
     * @param select Chooses the characteristic (for example: RELATIVE_FREQUENCY)
     * @param type Chooses the characteristic (for example: CHANGEROLE)
     * @param selection Chooses the main calculate characteristic (for example: RELATIVE_FREQUENCY)
     * @return returns a result (as a HashMap(typename, value)) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public HashMap<String, Double> evaluateFrequency(long from, long to, FrequencyType type, FrequencySelection selection) 
    	throws MetalyzerAPIException {
    		return calculateFrequency(from, to, type, selection);
    }
    
    /**
     * evaluate the frequency (relative and absolute) of different calculations at current timestamp
     * @author Juri Seewald 
     * @param select Chooses the characteristic (for example: RELATIVE_FREQUENCY)
     * @param type Chooses the characteristic (for example: CHANGEROLE)
     * @param selection Chooses the main calculate characteristic (for example: RELATIVE_FREQUENCY)
     * @return returns a result (as a HashMap(typename, value)) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public HashMap<String, Double> evaluateFrequency(FrequencyType type, FrequencySelection selection) 
        	throws MetalyzerAPIException {
    	return calculateFrequency(0, 0, type, selection);
        }
    
    /**
     * evaluate the frequency (relative and absolute) of different calculations at given timestamp
     * @author Juri Seewald 
     * @param timestamp
     * @param select Chooses the characteristic (for example: RELATIVE_FREQUENCY)
     * @param type Chooses the characteristic (for example: CHANGEROLE)
     * @param selection Chooses the main calculate characteristic (for example: RELATIVE_FREQUENCY)
     * @return returns a result (as a HashMap(typename, value)) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public HashMap<String, Double> evaluateFrequency(long timestamp, FrequencyType type, FrequencySelection selection) 
        	throws MetalyzerAPIException {
    	return calculateFrequency(timestamp, 0, type, selection);
        }

    /**
     * Represents a query of a time interval for the edge per nodes, which be stored in a result object
     * The ResultObject includes information about nodes in a given time interval.
     * It includes general statistical information about the time interval (mean, standardDeviation) and some informations about the 
     * time stamp (edge per nodes of all nodes and deviation)
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the result of the node query as a ResultObject<NodeResult>
     */    
    public ResultObject<NodeResult> getNodeCharecteristic(long from, long to){
    	ArrayList<Long> intervalTimestamps = getAPI().getTimestampsBetween(from, to);
    	ArrayList<Long> evaluationList = new ArrayList<>();
    	double meanResult = 0.0;
    	double stdDeviation = 0.0;
    	double deviation = 0.0;
    	long numbersOfIdentifier;
		ArrayList<NodeResult> nodeResult = new ArrayList<>();
    	for(long timestamp : intervalTimestamps){
    		numbersOfIdentifier= getAPI().getGraphAnalyzer().countNodesAt(timestamp);
    		evaluationList.add(numbersOfIdentifier);
    	}
        meanResult = new RMean(evaluationList).getMean();
    	for(long timestamp : intervalTimestamps){
    		numbersOfIdentifier= getAPI().getGraphAnalyzer().countNodesAt(timestamp);
    		deviation = Math.abs(numbersOfIdentifier - meanResult);
    		nodeResult.add(new NodeResult(timestamp,deviation,numbersOfIdentifier));
    	}
        stdDeviation = new RStandardDeviation(evaluationList).getStandardDeviation();
    	return new ResultObject<NodeResult>(meanResult,stdDeviation,nodeResult);
    			
      }
	    
    /**
     * Represents a query of a time interval for edges, which be stored in a result object
     * The ResultObject includes information about edges in a given time interval.
     * It includes general statistical information about the time interval (mean, standardDeviation) and some informations about the 
     * time stamp (number of edges and deviation)
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the result of the edge query as a ResultObject<EdgeResult>
     */
	public ResultObject<EdgeResult> getEdgeCharecteristic(long from, long to){
		ArrayList<Long> intervalTimestamps = getAPI().getTimestampsBetween(from, to);
    	ArrayList<Long> evaluationList = new ArrayList<>();
    	double meanResult = 0.0;
    	double stdDeviation = 0.0;
    	double deviation = 0.0;
    	ArrayList<EdgeResult> edgeResult = new ArrayList<>();
    	long numbersOfEdges;
		for(long timestamp : intervalTimestamps){
    		numbersOfEdges= getAPI().getGraphAnalyzer().countEdgesAt(timestamp);
    		evaluationList.add(numbersOfEdges);
    	}
        meanResult = new RMean(evaluationList).getMean();
		
     	for(long timestamp : intervalTimestamps){
    		numbersOfEdges= getAPI().getGraphAnalyzer().countEdgesAt(timestamp);
    		deviation = Math.abs(numbersOfEdges - meanResult);
    		edgeResult.add(new EdgeResult(timestamp,deviation,numbersOfEdges));
    	}
     	
		stdDeviation = new RStandardDeviation(evaluationList).getStandardDeviation();
    	return new ResultObject<EdgeResult>(meanResult,stdDeviation,edgeResult);	
	}
    	
	/**
     * Represents a query of a time interval for edges, which be stored in a result object
     * The ResultObject includes information about edges in a given time interval.
     * It includes general statistical information about the time interval (mean, standardDeviation) and some informations about the 
     * time stamp (number of edges and deviation)
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the result of the edge per nodes query as a ResultObject<MeanOfEdgeResult>
     */    	
	public ResultObject<MeanOfEdgeResult> getMeanOfEdgeCharecteristic(long from, long to){
		ArrayList<Long> intervalTimestamps = getAPI().getTimestampsBetween(from, to);
    	double meanResult = 0.0;
    	double stdDeviation = 0.0;
    	double deviation = 0.0;
    	double meanOfEdges = 0.0;
    	
    	ArrayList<MeanOfEdgeResult> edgeResult = new ArrayList<>();
		ArrayList<Double> edgesEvaluation = new ArrayList<Double>();
		for(long timestamp : intervalTimestamps){
    		meanOfEdges= getAPI().getGraphAnalyzer().getMeanOfEdges(timestamp);
    		edgesEvaluation.add(meanOfEdges);
    	}
		meanResult = new RMean(edgesEvaluation).getMean();
		for(long timestamp : intervalTimestamps){
    		meanOfEdges = getAPI().getGraphAnalyzer().getMeanOfEdges(timestamp);
    		deviation = Math.abs(meanOfEdges - meanResult);
    		edgeResult.add(new MeanOfEdgeResult(timestamp,deviation,meanOfEdges));
    	}
		
		stdDeviation = new RStandardDeviation(edgesEvaluation).getStandardDeviation();
    	return new ResultObject<MeanOfEdgeResult>(meanResult,stdDeviation,edgeResult);
	}
        
	/** 
	 * return an ArrayList with RateOfChangeResult objects at different timestamps
	 * @author Juri Seewald
	 * @param from start time
	 * @param to end time
	 * @param type selects the standard identifier
	 * @return RateOfChangeResult objects, which contains the timestamp, difference of count of identifier to previous and next timestamp
     */
	public ArrayList<RateOfChangeResult> rateOfChange(long from, long to, StandardIdentifierType type){
   	ArrayList<RateOfChangeResult> resultList = new ArrayList<>();
   	ArrayList<Long> timestamps = getAPI().getTimestampsBetween(from, to);
   	
		long nextStamp = timestamps.get(0+1);
		long currentStamp = timestamps.get(0);
		
		int current = getAPI().getIdentifierFinder().get(type, currentStamp).size();
		int next = getAPI().getIdentifierFinder().get(type, nextStamp).size();
		
		resultList.add(new RateOfChangeResult(currentStamp, current, 0, current-next));
   	   	
   	
   	for(int i = 1; i<timestamps.size()-1; i++){
   		long previousTimestamp = timestamps.get(i-1);
   		long nextTimestamp = timestamps.get(i+1);
   		long currentTimestamp = timestamps.get(i);
   		
   		int countOfMetdataPrevious = getAPI().getIdentifierFinder().get(type, previousTimestamp).size();
   		int countOfMetadataCurrent = getAPI().getIdentifierFinder().get(type, currentTimestamp).size();
   		int countOfMetadataNext = getAPI().getIdentifierFinder().get(type, nextTimestamp).size();
   		
   		int differencePrevious = countOfMetadataCurrent-countOfMetdataPrevious;
   		int differenceNext = countOfMetadataCurrent-countOfMetadataNext;
   		
   		RateOfChangeResult resObject = new RateOfChangeResult(currentTimestamp, countOfMetadataCurrent, differencePrevious, differenceNext);
   		resultList.add(resObject);
   	}
   	
   	long previousStamp = timestamps.get(0+1);
		currentStamp = timestamps.get(0);
		
		current = getAPI().getIdentifierFinder().get(type, currentStamp).size();
		int previous = getAPI().getIdentifierFinder().get(type, previousStamp).size();
		
		resultList.add(new RateOfChangeResult(currentStamp, current, current-previous, 0));
   	
   	return resultList;
   }
   
	/**
	 * returns an ArrayList depending on type of metadata with RateOfChangeResult objects at different timestamps
	 * @author Juri Seewald
	 * @param from start time
	 * @param to end time
	 * @param type selects the metadata
	 * @return RateOfChangeResult objects, which contains the timestamp, difference of metadata to previous and next timestamp
	 */
	public ArrayList<RateOfChangeResult> rateOfChange(long from, long to, String type){
	   	ArrayList<RateOfChangeResult> resultList = new ArrayList<>();
	   	ArrayList<Long> timestamps = getAPI().getTimestampsBetween(from, to);
	   	
			long nextStamp = timestamps.get(0+1);
			long currentStamp = timestamps.get(0);
			
			int current = getAPI().getMetadataFinder().get(type, currentStamp).size();
			int next = getAPI().getMetadataFinder().get(type, nextStamp).size();
			
			resultList.add(new RateOfChangeResult(currentStamp, current, 0, current-next));
	   	   	
	   	
	   	for(int i = 1; i<timestamps.size()-1; i++){
	   		long previousTimestamp = timestamps.get(i-1);
	   		long nextTimestamp = timestamps.get(i+1);
	   		long currentTimestamp = timestamps.get(i);
	   		
	   		int countOfMetdataPrevious = getAPI().getMetadataFinder().get(type, previousTimestamp).size();
	   		int countOfMetadataCurrent = getAPI().getMetadataFinder().get(type, currentTimestamp).size();
	   		int countOfMetadataNext = getAPI().getMetadataFinder().get(type, nextTimestamp).size();
	   		
	   		int differencePrevious = countOfMetadataCurrent-countOfMetdataPrevious;
	   		int differenceNext = countOfMetadataCurrent-countOfMetadataNext;
	   		
	   		RateOfChangeResult resObject = new RateOfChangeResult(currentTimestamp, countOfMetadataCurrent, differencePrevious, differenceNext);
	   		resultList.add(resObject);
   	}
   	
   	long previousStamp = timestamps.get(0+1);
		currentStamp = timestamps.get(0);
		
		current = getAPI().getMetadataFinder().get(type, currentStamp).size();
		int previous = getAPI().getMetadataFinder().get(type, previousStamp).size();
		
		resultList.add(new RateOfChangeResult(currentStamp, current, current-previous, 0));
		    	
   	return resultList;
   }
   
	/**
	 * returns an ArrayList independent of a metadata type with RateOfChangeResult objects at different timestamps
	 * @author Juri Seewald
	 * @param from start time
	 * @param to end time
	 * @param type selects the metadata
	 * @return RateOfChangeResult objects, which contains the timestamp, difference of metadata to previous and next timestamp
	 */
	public ArrayList<RateOfChangeResult> rateOfChange(long from, long to){
   	ArrayList<RateOfChangeResult> resultList = new ArrayList<>();
   	ArrayList<Long> timestamps = getAPI().getTimestampsBetween(from, to);
   	
		long nextStamp = timestamps.get(0+1);
		long currentStamp = timestamps.get(0);
		
		int current = getAPI().getMetadataFinder().get(currentStamp).size();
		int next = getAPI().getMetadataFinder().get(nextStamp).size();
		
		resultList.add(new RateOfChangeResult(currentStamp, current, 0, current-next));
   	   	
   	
   	for(int i = 1; i<timestamps.size()-1; i++){
   		//relevant timestamps for creation of a RateOfChange object
   		long previousTimestamp = timestamps.get(i-1);
   		long nextTimestamp = timestamps.get(i+1);
   		long currentTimestamp = timestamps.get(i);
   		
   		//number of Metadatas at relevant timestamps
   		int countOfMetdataPrevious = getAPI().getMetadataFinder().get(previousTimestamp).size();
   		int countOfMetadataCurrent = getAPI().getMetadataFinder().get(currentTimestamp).size();
   		int countOfMetadataNext = getAPI().getMetadataFinder().get(nextTimestamp).size();
   		
   		int differencePrevious = countOfMetadataCurrent-countOfMetdataPrevious;
   		int differenceNext = countOfMetadataCurrent-countOfMetadataNext;
   		
   		RateOfChangeResult resObject = new RateOfChangeResult(currentTimestamp, countOfMetadataCurrent, differencePrevious, differenceNext);
   		resultList.add(resObject);
   	}
   	
   	long previousStamp = timestamps.get(0+1);
		currentStamp = timestamps.get(0);
		
		current = getAPI().getMetadataFinder().get(currentStamp).size();
		int previous = getAPI().getMetadataFinder().get(previousStamp).size();
		
		resultList.add(new RateOfChangeResult(currentStamp, current, current-previous, 0));
		    	
   	return resultList;
   }
   
	/**
	 * calculates the relation of nodes to links
	 * @param timestamp timestamp at calculation time
	 * @return the relation of nodes to links
	 */
	public double relationOfNodesToLinks(long timestamp){    	
   	double numberOfNodes = getAPI().getGraphAnalyzer().countNodesAt(timestamp);
   	double numberOfLinks = getAPI().getGraphAnalyzer().countEdgesAt(timestamp);    	
   	return numberOfNodes/numberOfLinks;
   }
   
	/**
	 * counts all IP adresses in a given timeinterval
	 * @author Hassan Nahle
	 * @param from start time
	 * @param to end time 
	 * @return a int value of all IP adresses 
	 * @throws MetalyzerAPIException
	 */ 
	public int countIpAdresses(long from, long to) throws MetalyzerAPIException{
    	return getAPI().getIdentifierFinder().get(StandardIdentifierType.IP_ADDRESS, from, to).getAvailables().size();
    }
	
	/**
	 * counts all MAC adresses in a given timeinterval
	 * @author Hassan Nahle
	 * @param from start time
	 * @param to end time
	 * @return a int value of all MAC adresses
	 * @throws MetalyzerAPIException
	 */
	public int countMacAdresses(long from, long to) throws MetalyzerAPIException{
		return getAPI().getIdentifierFinder().get(StandardIdentifierType.MAC_ADDRESS, from, to).getAvailables().size();
	}
	
	/**
	 * counts all devices in a given timeinterval
	 * @author Hassan Nahle
	 * @param from start time
	 * @param to end time
	 * @return a int value of all devices
	 * @throws MetalyzerAPIException
	 */
	public int countDevices(long from, long to) throws MetalyzerAPIException{
		return getAPI().getIdentifierFinder().get(StandardIdentifierType.DEVICE, from, to).getAvailables().size();
	}
	
	/**
	 * counts all identity identifier in a given timeinterval
	 * @author Hassan Nahle
	 * @param from start time
	 * @param to end time
	 * @return a int value of all identity identifer
	 * @throws MetalyzerAPIException
	 */
	public int countIdentity(long from, long to) throws MetalyzerAPIException{
	    return getAPI().getIdentifierFinder().get(StandardIdentifierType.IDENTITY, from, to).getAvailables().size();
	}

	/**
	 * counts all devices in a given timeinterval
	 * @author Hassan Nahle
	 * @param from start time
	 * @param to end time
	 * @return a int value of all devices
	 * @throws MetalyzerAPIException
	 */
	public int countAccessRequests(long from, long to) throws MetalyzerAPIException{
		return getAPI().getIdentifierFinder().get(StandardIdentifierType.ACCESS_REQUEST, from, to).getAvailables().size();
	}
	
	/**
	 * counts all identifier in a given timeinterval
	 * @author Hassan Nahle
	 * @param from start time
	 * @param to end time
	 * @return a int value of all identifier
	 * @throws MetalyzerAPIException
	 */
	public int countIdentifier(long from, long to) throws MetalyzerAPIException {    	
		return getAPI().getIdentifierFinder().get(StandardIdentifierType.ALL, from, to).getAvailables().size();
		 
    }
    
	/**
	 * counts all identifier on a given timestamp
	 * @author Hassan Nahle, Juri Seewald
	 * @param timestamp timestamp
	 * @return a int value of all identifier
	 * @throws MetalyzerAPIException
	 */
	public int countIdentifier(long timestamp) throws MetalyzerAPIException {
		return getAPI().getIdentifierFinder().get(StandardIdentifierType.ALL, timestamp).size();
		
		
    }

	/**
	 * counts all identifier at the current time
	 * @author Hassan Nahle, Juri Seewald
	 * @return a long value of all identifier
	 * @throws MetalyzerAPIException
	 */
	public long countIdentifier() throws MetalyzerAPIException {    	
		return getAPI().getIdentifierFinder().count();
		
    }
    
	/**
	 * counts all metadata in a given timeinterval
	 * @author Hassan Nahle, Juri Seewald
	 * @param from start time
	 * @param to end time
	 * @return a int value of all identifier
	 * @throws MetalyzerAPIException
	 */
	public int countMetadata(long from, long to) throws MetalyzerAPIException{
			return getAPI().getMetadataFinder().get(from, to).getAvailables().size();
    }
    
	/**
	 * counts all metadata on a given timestamp
	 * @author Hassan Nahle, Juri Seewald
	 * @param from start time
	 * @param timestamp timestamp
	 * @return a int value of all identifier
	 * @throws MetalyzerAPIException
	 */    
	public int countMetadata(long timestamp) throws MetalyzerAPIException{
		return getAPI().getMetadataFinder().get(timestamp).size();
    }
    
	/**
	 * counts all metadata at the current time
	 * @author Hassan Nahle, Juri Seewald
	 * @return a long value of all identifier
	 * @throws MetalyzerAPIException
	 */    
	public long countMetadata() throws MetalyzerAPIException{
		return getAPI().getMetadataFinder().count();
    }   
    
	/**
	 * calculate the mean.
	 * @author Juri Seewald
	 * @param fromOrTimestamp 
	 * @param to 
	 * @param filter chooses the StandardIdentifier
	 * @param type choose the 
	 * @return the mean
	 */
	private double calculateMean(long fromOrTimestamp, long to, StandardIdentifierType filter, MeanType type){
    	boolean isDeltaMethod = fromOrTimestamp !=0 && to !=0;
    	ArrayList<Identifier> listOfIdentifiers;
    	ArrayList<Integer> evalList;
    	
    	if(isDeltaMethod){
    		listOfIdentifiers = new ArrayList<>(getAPI().getIdentifierFinder().get(filter, fromOrTimestamp, to).getAvailables());
    	} else if(fromOrTimestamp != 0.0){
    		listOfIdentifiers = new ArrayList<>(getAPI().getIdentifierFinder().get(filter, fromOrTimestamp));
    	} else {
    		listOfIdentifiers = new ArrayList<>(getAPI().getIdentifierFinder().getCurrent(filter));
    	}
    	
    	switch(type){
		case LFI:
	        evalList = Counter.countLinksFromIdentifiers(listOfIdentifiers);
	        break;
		default:
			evalList = new ArrayList<Integer>();
			break;    		
    	}
    	return new RMean(evalList).getMean();
    }
    
	/**
	 * calculates the frequency
	 * @author Juri Seewald
	 * @param fromOrTimestamp
	 * @param to
	 * @param type the calculation type
	 * @param selection absolute or relative frequency
	 * @return the frequency
	 */
	public HashMap<String, Double> calculateFrequency(long fromOrTimestamp, long to, FrequencyType type, FrequencySelection selection){    	
    	
    	boolean isDeltaMethod = fromOrTimestamp !=0 && to !=0;	
    	ArrayList<? extends Propable> list=null;
    	
    	if(isDeltaMethod){
    		if(type == FrequencyType.IDENTIFIER){
    			list = new ArrayList<>(getAPI().getIdentifierFinder().get(StandardIdentifierType.ALL,fromOrTimestamp,to).getAvailables());
    		}else if(type == FrequencyType.DEVICES){
    			list = new ArrayList<>(getAPI().getIdentifierFinder().get(StandardIdentifierType.DEVICE,fromOrTimestamp,to).getAvailables());
    		}else if(type == FrequencyType.METADATA){
    			list = new ArrayList<>(getAPI().getMetadataFinder().get(fromOrTimestamp,to).getAvailables());
    		}else if(type == FrequencyType.ROLES){
    			list = new ArrayList<> (getAPI().getMetadataFinder().get("role",fromOrTimestamp,to).getAvailables());
    		}
    	} else if(fromOrTimestamp!=0.0){
    		if(type == FrequencyType.IDENTIFIER){
    			list = new ArrayList<>(getAPI().getIdentifierFinder().get(fromOrTimestamp));
    		}else if(type == FrequencyType.DEVICES){
    			list = new ArrayList<>(getAPI().getIdentifierFinder().get(StandardIdentifierType.DEVICE, fromOrTimestamp));
    		}else if(type == FrequencyType.METADATA){
    			list = new ArrayList<>(getAPI().getMetadataFinder().get(fromOrTimestamp));
    		}else if(type == FrequencyType.ROLES){
    			list = new ArrayList<>(getAPI().getMetadataFinder().get("role",fromOrTimestamp));
    		}
    	}else{
    		if(type == FrequencyType.IDENTIFIER){
    			list = new ArrayList<>(getAPI().getIdentifierFinder().getCurrent(StandardIdentifierType.ALL));
    		}else if(type == FrequencyType.DEVICES){
    			list = new ArrayList<>(getAPI().getIdentifierFinder().getCurrent(StandardIdentifierType.DEVICE));
    		}else if(type == FrequencyType.METADATA){
    			list = new ArrayList<>(getAPI().getMetadataFinder().getCurrent());
    		}else if(type == FrequencyType.ROLES){
    			list = new ArrayList<> (getAPI().getMetadataFinder().getCurrent("role"));    		
    		}else{
    			list = new ArrayList<>();
    		}
    	}    	
    	return new RFrequency(list, type, selection).getFrequency();    	
    }
}
