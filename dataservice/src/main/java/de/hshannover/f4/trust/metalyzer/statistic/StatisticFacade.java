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
package de.hshannover.f4.trust.metalyzer.statistic;


import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;


public interface StatisticFacade {
    
	/**
     * evaluate the mean of different calculations
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @param filter Chooses the characteristic (for example: CHANGEROLE)
     * @param type
     * @return returns a result (as a double value) of a given characteristic
     * @throws MetalyzerAPIException
     */
	public String evaluateMean(long from, long to, StandardIdentifierType filter, MeanType type);
	
	/**
     * evaluate the mean of different calculations at current timestamp
     * @author Juri Seewald     
     * @param filter Chooses the characteristic (for example: CHANGEROLE)
     * @param type
     * @return returns a result (as a double value) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public String evaluateMean(StandardIdentifierType filter, MeanType type);
    
    /**
     * evaluate the mean of different calculations at given timestamp
     * @author Juri Seewald 
     * @param timestamp    
     * @param filter Chooses the characteristic (for example: CHANGEROLE)
     * @param type
     * @return returns a result (as a double value) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public String evaluateMean(long timestamp, StandardIdentifierType filter, MeanType type);
            
    /**
     * evaluate the frequency (relative and absolute) of different calculations
     * @author Hassan Nahle
     * @param from start time
     * @param to end time 
     * @param select Chooses the characteristic (for example: RELATIVE_FREQUENCY)
     * @param type Chooses the characteristic (for example: CHANGEROLE)
     * @param selection Chooses the main calculate characteristic (for example: RELATIVE_FREQUENCY)
     * @return returns a result (as a HashMap(typename, value)) of a given characteristic
     * @throws MetalyzerAPIException
     */	
    public String evaluateFrequency(long from, long to,FrequencyType type, FrequencySelection selection);

    /**
     * evaluate the frequency (relative and absolute) of different calculations at current timestamp
     * @author Juri Seewald 
     * @param select Chooses the characteristic (for example: RELATIVE_FREQUENCY)
     * @param type Chooses the characteristic (for example: CHANGEROLE)
     * @param selection Chooses the main calculate characteristic (for example: RELATIVE_FREQUENCY)
     * @return returns a result (as a HashMap(typename, value)) of a given characteristic
     * @throws MetalyzerAPIException
     */
    public String evaluateFrequency(FrequencyType type, FrequencySelection selection);
    
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
    public String evaluateFrequency(long timestamp, FrequencyType type, FrequencySelection selection);
    
    
    /**
     * Represents a query of a time interval for the edge per nodes, which be stored in a result object
     * The ResultObject includes information about nodes in a given time interval.
     * It includes general statistical information about the time interval (mean, standardDeviation) and some informations about the 
     * time stamp (edge per nodes of all nodes and deviation)
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the result of the edge query as json-string
     */    
    public String getNodeCharecteristic(long from, long to);
    
    /**
     * Represents a query of a time interval for edges, which be stored in a result object
     * The ResultObject includes information about edges in a given time interval.
     * It includes general statistical information about the time interval (mean, standardDeviation) and some informations about the 
     * time stamp (number of edges and deviation)
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the result of the edge query as json-string
     */
    
    public String getEdgeCharecteristic(long from, long to);
    
    /**
     * Represents a query of a time interval for edges, which be stored in a result object
     * The ResultObject includes information about edges in a given time interval.
     * It includes general statistical information about the time interval (mean, standardDeviation) and some informations about the 
     * time stamp (number of edges and deviation)
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the result of the edge query 
     */
    
    public String getMeanOfEdgeCharecteristic(long from, long to);
    
    /**
    * counts all identifier in the graph 
    * @author Hassan Nahle
    * @param from start time
    * @param to end time
    * @return returns the sum of identifier 
    */  
    public String sumOfIdentifier(long from, long to);
    
    /**
    * counts all identifier at the current time in the graph 
    * @author Hassan Nahle
    * @return returns the sum of identifier 
    */ 
    
	public String sumOfIdentifier();
    /**
    * counts all identifier in the graph 
    * @author Hassan Nahle
    * @param timestamp timestamp
    * @return returns the sum of identifier 
    */  
    public String sumOfIdentifier(long timestamp);
  
    
    /**   
     * counts all metadata at the current time in the graph  
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the sum of metadata as a json-string
     */
    public String sumOfMetadata();
    
    /**   
     * counts all metadata in the graph  
     * @author Hassan Nahle
     * @param from start time
     * @param to end time
     * @return returns the sum of metadata as a json-string
     */
    public String sumOfMetadata(long from, long to);
    
    
    /**   
     * counts all metadata in the graph  
     * @author Hassan Nahle
     * @param timestamp timestamp
     * @return returns the sum of metadata as a json-string
     */
    public String sumOfMetadata(long timestamp);
    
    /**
     * return an ArrayList with RateOfChangeResult objects at different timestamps
     * @author Juri Seewald
     * @param from start time
     * @param to end time
     * @param type selects the standard identifier
     * @return ArrayList of RateOfChangeResult objects as Json-String, which contains the timestamp, difference of count of identifier to previous and next timestamp
     */
    public String rateOfChange(long from, long to, StandardIdentifierType type);
    
    /**
     * returns an ArrayList depending on type of metadata with RateOfChangeResult objects at different timestamps
     * @author Juri Seewald
     * @param from start time
     * @param to end time
     * @param type selects the metadata
     * @return RateOfChangeResult objects, which contains the timestamp, difference of metadata to previous and next timestamp
     */
    public String rateOfChange(long from, long to, String type);
    
    /**
     * returns an ArrayList independent of a metadata type with RateOfChangeResult objects at different timestamps
     * @author Juri Seewald
     * @param from start time
     * @param to end time
     * @param type selects the metadata
     * @return RateOfChangeResult objects, which contains the timestamp, difference of metadata to previous and next timestamp
     */
    public String rateOfChange(long from, long to);
    
    /**
     * @author Juri Seewald
     * calculates the relation of nodes to links
     * @param timestamp timestamp at calculation time
     * @return double
     */
    public String relationOfNodesToLinks(long timestamp);


    
    
    
}
