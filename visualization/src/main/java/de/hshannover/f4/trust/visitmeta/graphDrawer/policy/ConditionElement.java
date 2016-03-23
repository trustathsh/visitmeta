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
 * This file is part of visitmeta-visualization, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.policy;

import java.util.ArrayList;
import java.util.List;

public class ConditionElement {

	public enum ConditionElementType {
		SIGNATURE,
		ANOMALY,
		HINT;
		
		public static ConditionElementType valueOfString(String type) {
			String typeLowCase = type.toLowerCase();
			if (typeLowCase.contains(SIGNATURE.toString().toLowerCase())) {
				return SIGNATURE;
			} else if (typeLowCase.contains(ANOMALY.toString().toLowerCase())) {
				return ANOMALY;
			} else if (typeLowCase.contains(HINT.toString().toLowerCase())) {
				return HINT;
			}
			return null;
		}
	}

	public ConditionElementType type;

	public String id;

	public boolean result;

	public List<ConditionElement> childs;

	public ConditionElement() {
		childs = new ArrayList<ConditionElement>();
	}

	// TODO
	// public ConditionElement(String id, boolean result) {
	// this.id = id;
	// this.result = result;
	// }
}
