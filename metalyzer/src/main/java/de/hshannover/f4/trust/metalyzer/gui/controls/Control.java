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
package de.hshannover.f4.trust.metalyzer.gui.controls;

import de.hshannover.f4.trust.metalyzer.gui.characteristics.AnalysePanel;

/** 
 * Project: Metalyzer
 * Author: Daniel Hülse
 * Last Change:
 *        by: $Author: $
 *        date: $Date: $
 * Copyright (c): Hochschule Hannover
 */

public interface Control {
	
	/**
	 * Fills a {@link AnalysePanel} according to the task of the control
	 * @param analaysePanel the panel that should be filled e.g. {@link StatisticPanel} or a {@link SemanticPanel}
	 * @param t1 a unix timestamp, and the first timestamp of the time intervall
	 * @param t2 a unix timestamp, and the last timestamp of the time intervall
	 * */
	public void fillControl(AnalysePanel panel, long t1, long t2);
	
	/**
	 * Updates a {@link AnalysePanel} according to the task of the control
	 * @param analaysePanel the panel that should be updated e.g. {@link StatisticPanel} or a {@link SemanticPanel}
	 * @param t1 a unix timestamp, and the first timestamp of the time intervall
	 * @param t2 a unix timestamp, and the last timestamp of the time intervall
	 * */
	public void updateControl(AnalysePanel panel, long t1, long t2);
}
