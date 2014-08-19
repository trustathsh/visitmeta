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

import java.util.List;


public abstract class StatisticalCalculator {

    protected final REvaluator rEvaluator;
	
    public abstract void evaluate(String evalString);
	
	public StatisticalCalculator() {
		rEvaluator = REvaluator.getInstance();
	}
	
	/**
	 * @author Hassan Nahle
	 * @param evaluationList A evaluation list 
	 * @return Returns a evaluation list which is converted as a string
	 **/
    public String convertEvaluationList(List<?> evaluationList){
		StringBuilder sb = new StringBuilder();
		if(evaluationList.size() > 0) {
			sb.append(evaluationList.get(0));
			for(int i = 1; i < evaluationList.size(); i++) {
				sb.append(",");
				sb.append(evaluationList.get(i));
			}
		}
		return sb.toString();
	}
}
