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
import java.util.HashMap;
import org.rosuda.JRI.REXP;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;


public class RFrequency extends StatisticalCalculator {
	
	private FrequencySelection sel;
	private HashMap<String,Double> freq;
	private int size;
	
	

	/**
	 * evaluate the relative frequency of a given list
	 * @author Hassan Nahle
	 * @param evaluationList A list that contains the data to be claculated
	 * @param type Chooses the characteristic (for example: IDENTIFIER)
	 */
	public RFrequency(ArrayList<? extends Propable> evaluationList, FrequencyType type, FrequencySelection selection) {
		this.sel = selection;
		this.size = evaluationList.size();
		this.freq = new HashMap<String,Double>();
		
		evaluate(convertEvaluationList(prepareList(evaluationList,type)));
	}
	
	/**
	 * @author Hassan Nahle
	 * @return returns the absolute frequency of a given list
	 */
	public HashMap<String,Double> getFrequency(){
		return freq;
	}
	
	/**
	 * converts a ArrayList<?extends Propable> to a ArrayList<String>
	 * @author Hassan Nahle
	 * @param evaluationList 
	 * @return a ArrayList, that includes all typenames of a propable type
	 */
		
	private ArrayList<String> prepareList(ArrayList<? extends Propable> evaluationList, FrequencyType type){
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0;i<evaluationList.size();i++){
			String value = null;
			if(type == FrequencyType.IDENTIFIER || type == FrequencyType.METADATA){
				value = evaluationList.get(i).getTypeName();
			} else if(type == FrequencyType.DEVICES){
				value = evaluationList.get(i).valueFor("/device/name");
			} else if(type == FrequencyType.ROLES){
				value = evaluationList.get(i).valueFor("/meta:role/name");
			}
			
			if(value != null) {
				list.add("\""+value+"\"");
			}
		}
		return list;	
	}

	/**
	 * @author Hassan Nahle
	 * @param evalString a evaluationlist as a string (use convertEvaluationList)
	 */
	@Override
	public void evaluate(String evalString) {
		rEvaluator.evalInR("n<-"+size);
		rEvaluator.evalInR("x<-c("+evalString+")");
		
		REXP f = rEvaluator.evalInR("hf<-table(x)");
		REXP v = rEvaluator.evalInR("rownames(hf)");
		if(this.sel == FrequencySelection.RELATIVE_FREQUENCY) {
			f = rEvaluator.evalInR("rf<-hf/n");
			v = rEvaluator.evalInR("rownames(rf)");
		}

		double[] frequencyArray = f.asDoubleArray();
		String[] value = v.asStringArray();
		for(int i = 0; i < frequencyArray.length; i++) {
			freq.put(value[i], frequencyArray[i]);	
		}
	}

}
