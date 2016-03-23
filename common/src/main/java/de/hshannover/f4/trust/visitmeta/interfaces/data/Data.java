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
 * This file is part of visitmeta-common, version 0.6.0,
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
package de.hshannover.f4.trust.visitmeta.interfaces.data;

import java.util.List;

public interface Data extends SubData {

	public List<Data> getSubData();

	/**
	 * Creates and returns a copy(1-1) of this object. The general intent is that, for any object {@code x}, the
	 * expression:
	 * <blockquote>
	 * 
	 * <pre>
	 * x.copy() != x
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.copy().getClass() == x.getClass()
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.copy().equals(x)
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * 
	 * @return a 1-1 copy of this instance.
	 */
	public Data copy();

	/**
	 * Creates and returns a clone of this object. The general intent is that, for any object {@code x}, the
	 * expression:
	 * <blockquote>
	 * 
	 * <pre>
	 * x.clone() != x
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.clone().getClass() == x.getClass()
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.clone().equals(x)
	 * </pre>
	 * 
	 * </blockquote>
	 * does <b>not</b> have to true!
	 * 
	 * @return a clone of this instance.
	 */
	public Data clone();

	public String getName();

	public void setName(String name);

	public Class<?> getDataTypeClass();
}
