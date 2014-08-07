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
 * This file is part of visitmeta visualization, version 0.1.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.graphCalculator;





/**
 * The LayoutType represents the Layout to be chosen for the Graph.
 */
public enum LayoutType{

	/**
	 * <p>A Force-Directed-Layout.</p>
	 * <p>This implementation (LayoutForceDirected2D) uses the functionalities of FRLayout of JUNG2.
	 * JUNG2 uses a self-implemented Fruchterman-Reingold Force-Directed-Layout.</p>
	 */
	FORCE_DIRECTED,

	/**
	 * <p>A Spring-Layout.</p>
	 * <p>This implementation (LayoutSpring2D) uses the functionalities of SpringLayout of JUNG2.
	 * JUNG2 uses a self-implemented Spring-Layout.</p>
	 */
	SPRING,
	
	/**
	 * <p>A Bipartite-Layout adapted to the specific structure of MAP graphs
	 * (with identifier nodes connected via metadata nodes).</p>
	 * <p>This implementation (LayoutBipartite2D) uses the class StaticLayout of JUNG2 for consistency.
	 * The layout algorithm itself has been developed by Trust@HsH group.</p>
	 */
	BIPARTITE
}
