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
package de.hshannover.f4.trust.metalyzer.gui.charts.data;

import java.util.Map;

/** 
 * Project: Metalyzer
 * Author: Daniel Huelse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public class StatisticData {
	
	private Number mSingleValue;
	private Map<String,? extends Number> mMultiValue;
	
	public StatisticData(Number value) {
		this.mSingleValue = value;
	}
	
	public StatisticData(Map<String,? extends Number> value) {
		this.mMultiValue = value;
	}

	/**
	 * Returns a single statistic value
	 * @return single value
	 * */
	public Number getSingleValue() {
		return mSingleValue;
	}

	/**
	 * Sets a single statistic value
	 * @param value
	 * */
	public void setSingleValue(double value) {
		this.mSingleValue = value;
	}

	/**
	 * Returns a multi statistic value
	 * @return multi value
	 * */
	public Map<String, ? extends Number> getMultiValue() {
		return mMultiValue;
	}

	/**
	 * Sets a multiple statistic value
	 * @param value 
	 * */
	public void setMultiValue(Map<String, ? extends Number> value) {
		this.mMultiValue = value;
	}

}
