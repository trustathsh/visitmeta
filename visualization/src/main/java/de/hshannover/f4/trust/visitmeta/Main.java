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
 * This file is part of visitmeta visualization, version 0.0.3,
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
package de.hshannover.f4.trust.visitmeta;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.datawrapper.PropertiesManager;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerCreation;
import de.hshannover.f4.trust.visitmeta.datawrapper.TimeManagerDeletion;
import de.hshannover.f4.trust.visitmeta.graphCalculator.Calculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.GuiController;
import de.hshannover.f4.trust.visitmeta.network.Connection;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;

/**
 * Class with main-method.
 */
public final class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class);

	/**
	 * Wegen der Sicherheit!
	 */
	private Main() {
	}

	/**
	 * Main-Methode
	 * 
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		LOGGER.trace("Method main(" + args + ") called.");

		String vConnectionTypeString = PropertiesManager.getProperty("application", "dataservice.connectiontype",
				"local").toUpperCase();
		ConnectionType vConnectionType = ConnectionType.valueOf(vConnectionTypeString);

		Connection vConnection = FactoryConnection.getConnection(vConnectionType);
		Calculator vCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);

		TimeManagerCreation vTimerCreation = TimeManagerCreation.getInstance();
		TimeManagerDeletion vTimerDeletion = TimeManagerDeletion.getInstance();

		FacadeNetwork vNetwork = new FacadeNetwork(vConnection);
		FacadeLogic vLogic = new FacadeLogic(vNetwork, vCalculator);
		GraphConnection connController = new GraphConnection(vLogic);
		GuiController gui = new GuiController();
		gui.addConnection("Default", connController);

		Thread vThreadCreation = new Thread(vTimerCreation);
		Thread vThreadDeletion = new Thread(vTimerDeletion);
		Thread vThreadNetwork = new Thread(vNetwork);
		Thread vThreadLogic = new Thread(vLogic);

		vThreadCreation.start();
		vThreadDeletion.start();
		gui.show();
		vThreadNetwork.start();
		vThreadLogic.start();

	}
}
