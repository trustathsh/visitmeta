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
package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.UniformInterfaceException;

import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.DialogHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.network.otherservices.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.network.otherservices.IronDetectConnection;

public class RunWhatIfAnalysisOnPolicyActionContextMenuItem implements ContextMenuItem {

	private Logger logger = Logger.getLogger(RunWhatIfAnalysisOnPolicyActionContextMenuItem.class);
	
	private static final String POLICY_ACTION_METADATA = "policy-action";
	private IronDetectConnection mIronDetectConnection;
	private DataserviceConnection mDataserviceConnection;

	public RunWhatIfAnalysisOnPolicyActionContextMenuItem() {
		mIronDetectConnection = new IronDetectConnection();
		mDataserviceConnection = new DataserviceConnection();
	}
	
	@Override
	public void actionPerformed(GraphicWrapper wrapper) {
		Propable propable = wrapper.getData();
		Metadata metadata = (Metadata) propable;
		Long timestamp = metadata.getPublishTimestamp();
		logger.debug("Timestamp: " + timestamp);
		
		String json = mDataserviceConnection.getGraphAt(timestamp);
		logger.debug("Json result: " + json);
		
		String response;
		try {
			response = mIronDetectConnection.send(json);
			logger.debug(response);
		} catch (UniformInterfaceException | PropertyException e) {
			DialogHelper.showErrorDialog(e.getMessage(), e.getClass().getSimpleName());
			logger.error(e.getMessage());
		}
	}

	@Override
	public String getItemTitle() {
		return "Run What-If Analysis";
	}

	@Override
	public boolean canHandle(Propable node) {
		if (node instanceof Metadata && node.getTypeName().equals(POLICY_ACTION_METADATA)) {
			return true;
		}
		
		return false;
	}

}
