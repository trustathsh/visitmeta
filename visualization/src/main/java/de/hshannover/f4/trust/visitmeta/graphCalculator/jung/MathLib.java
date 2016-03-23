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
package de.hshannover.f4.trust.visitmeta.graphCalculator.jung;





import org.apache.log4j.Logger;



/**
 * Some mathematical helper-methods (all public-static).
 */
public class MathLib{

	private static final Logger LOGGER = Logger.getLogger(MathLib.class);

	/**
	 * Round the value to 'digits'-decimal places.
	 *
	 * @param value The value to be rounded.
	 * @param digits The number of decimal places the value should be round to.
	 * @return The rounded value.
	 */
	public static double round(double value, int digits){
		LOGGER.trace("Method round(" + value + ", " + digits + ") called.");
		double factor = Math.pow(10, digits);
		return Math.round(value * factor) / factor;
	}

	/**
	 * Round the value to an int.
	 *
	 * @param value The value to be rounded.
	 * @return The rounded value.
	 */
	public static int round(double value){
		LOGGER.trace("Method round(" + value + ") called.");
		value = value + 0.5;
		return (int)value;
	}

	/**
	 * Round up.
	 *
	 * @param value The value to be rounded.
	 * @return The rounded value.
	 */
	public static int roundUp(double value){
		LOGGER.trace("Method roundUp(" + value + ") called.");
		value = value + 0.999999;
		return (int)value;
	}

	/**
	 * Round down.
	 *
	 * @param value The value to be rounded.
	 * @return The rounded value.
	 */
	public static int roundDown(double value){
		LOGGER.trace("Method roundDown(" + value + ") called.");
		return (int)value;
	}

	/**
	 * <p>Pythagorean theorem.</p>
	 * <p>Calculates a*a + b*b = c*c</p>
	 *
	 * @param a
	 * @param b
	 * @return c
	 */
	public static double pythagoras(double a, double b){
		LOGGER.trace("Method pythagoras(" + a + ", " + b + ") called.");
		return Math.sqrt(a * a + b * b);
	}

}
