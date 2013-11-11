package de.fhhannover.inform.trust.visitmeta;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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
/* Imports ********************************************************************/
import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.datawrapper.PropertiesManager;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeManagerCreation;
import de.fhhannover.inform.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.Calculator;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.FacadeLogic;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.fhhannover.inform.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.fhhannover.inform.trust.visitmeta.gui.GuiController;
import de.fhhannover.inform.trust.visitmeta.network.Connection;
import de.fhhannover.inform.trust.visitmeta.network.FacadeNetwork;
import de.fhhannover.inform.trust.visitmeta.network.FactoryConnection;
import de.fhhannover.inform.trust.visitmeta.network.FactoryConnection.ConnectionType;
/* Class **********************************************************************/
/**
 * Class with main-method.
 */
public class Main {
/* Attributes *****************************************************************/
	private static final Logger LOGGER = Logger.getLogger(Main.class);
/* Constructors ***************************************************************/
/* Methods ********************************************************************/
	/**
	 * Main-Methode
	 * @param args not used.
	 */
	public static void main(String[] args) {
		LOGGER.trace("Method main(" + args + ") called.");

		String vConnectionTypeString = PropertiesManager.getProperty(
				"application",
				"dataservice.connectiontype",
				"local").toUpperCase();
		ConnectionType vConnectionType = ConnectionType.valueOf(vConnectionTypeString);
		
		Connection     vConnection     = FactoryConnection.getConnection(vConnectionType);
		Calculator     vCalculator     = FactoryCalculator.getCalculator(CalculatorType.JUNG);

		TimeManagerCreation vTimerCreation = TimeManagerCreation.getInstance();
		TimeManagerDeletion vTimerDeletion = TimeManagerDeletion.getInstance();

		FacadeNetwork vNetwork    = new FacadeNetwork(vConnection);
		FacadeLogic   vLogic      = new FacadeLogic(vNetwork, vCalculator);
		GuiController vController = new GuiController(vLogic);

		Thread vThreadCreation = new Thread(vTimerCreation);
		Thread vThreadDeletion = new Thread(vTimerDeletion);
		Thread vThreadNetwork  = new Thread(vNetwork);
		Thread vThreadLogic    = new Thread(vLogic);

		vThreadCreation.start();
		vThreadDeletion.start();
		vController.show();
		vThreadNetwork.start();
		vThreadLogic.start();

	}
}
