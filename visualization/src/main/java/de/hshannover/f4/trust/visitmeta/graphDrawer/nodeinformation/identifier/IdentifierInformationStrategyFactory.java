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
 * This file is part of visitmeta-visualization, version 0.4.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.graphDrawer.nodeinformation.identifier;

/**
 * Factory class for {@link IdentifierInformationStrategy}.
 * 
 * @author Bastian Hellmann
 *
 */
public class IdentifierInformationStrategyFactory {

	/**
	 * Returns a {@link IdentiferInformationStrategy} instance
	 * based on the given {@link IdentifierInformationStrategyType}.
	 * 
	 * @param type {@link IdentifierInformationStrategyType}
	 * @return {@link IdentifierInformationStrategy} instance for the given {@link IdentifierInformationStrategyType}
	 */
	public static IdentifierInformationStrategy create(
			IdentifierInformationStrategyType type) {

		switch (type) {
		case SINGLE_LINE:
			return new IdentifierInformationSingleLine();
		case MULTI_LINE:
			return new IdentifierInformationMultiLine();
		default:
			throw new IllegalArgumentException("No strategy for given type '" + type + "'");
		}
	}

}
