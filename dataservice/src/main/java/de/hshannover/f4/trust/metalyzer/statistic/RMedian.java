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
 * Author: Hassan Nahle
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */

package de.hshannover.f4.trust.metalyzer.statistic;

import java.util.ArrayList;

import org.rosuda.JRI.REXP;


public class RMedian extends StatisticalCalculator {
    private double median = 0;
    
    /**
     * @author Hassan Nahle
     * evaluate the median of a given evaluationlist. 
     * This list should be a numerical Arraylist
     */
    public RMedian(ArrayList< ? extends Number> evaluationList){
    	 evaluate(convertEvaluationList(evaluationList));
    }
    
    /**
     * evaluate the median
     * @param evalString a evaluationlist as a string (use convertEvaluationList)
     */
    @Override
    public void evaluate(String evalString){		
        rEvaluator.evalInR("x=c("+ evalString +")");
        REXP rexp = rEvaluator.evalInR("median(x)");
        median = rexp.asDouble();	
        if(Double.isNaN(median)){
        	median = 0.0;
        }
    }

    /**
     * 
     * @return return the result of the evaluated list
     */
    public double getMedian(){
    	return median;
    }
}
