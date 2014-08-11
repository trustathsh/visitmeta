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
 * This file is part of visitmeta visualization, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.io.FileNotFoundException;

import javax.swing.JList;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;

import com.sun.jersey.api.client.UniformInterfaceException;

import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.gui.dialog.ConnectionDialog.DataServicePanel;
import de.hshannover.f4.trust.visitmeta.gui.dialog.ConnectionDialog.MapServerPanel;
import de.hshannover.f4.trust.visitmeta.gui.dialog.ConnectionDialog.TabPanel;
import de.hshannover.f4.trust.visitmeta.gui.util.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RestConnection;
import de.hshannover.f4.trust.visitmeta.util.yaml.DataservicePersister;
import de.hshannover.f4.trust.visitmeta.util.yaml.DataservicePersisterException;

public class CheckSavingJList<E> extends JList<E> {

	private static final long serialVersionUID = -2925933797917763759L;

	private static final Logger log = Logger.getLogger(CheckSavingJList.class);

	private static DataservicePersister mDataservicePersister = Main.getDataservicePersister();

	private int mPreviousIndex = -1;

	private boolean mResetSelection;

	private TabPanel mJPanel;

	static {
		log.addAppender(new JTextAreaAppander());
	}

	public CheckSavingJList(TabPanel panel) {
		mJPanel = panel;
	}


	@Override
	protected void fireSelectionValueChanged(int firstIndex, int lastIndex, boolean isAdjusting){
		if(mJPanel.mChanges && !mResetSelection){
			int n = JOptionPane.showConfirmDialog(mJPanel, "Would you like to save your changes?", "Save changes?", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION){

				try {
					yesOption();
				} catch (FileNotFoundException	| DataservicePersisterException | JSONException e) {
					// logging already finished
				}

			}else if(n == JOptionPane.NO_OPTION){
				noOption();
				return;
			}else if(n == JOptionPane.CLOSED_OPTION){
				noOption();
				return;
			}
		}
		mPreviousIndex = getSelectedIndex();
		super.fireSelectionValueChanged(firstIndex, lastIndex, isAdjusting);
	}

	public void yesOption() throws DataservicePersisterException, FileNotFoundException, JSONException, JSONException {
		if(mJPanel instanceof DataServicePanel){
			DataServicePanel panel = (DataServicePanel)mJPanel;
			DataserviceConnection tmp = panel.mPreviousConnection.copy();	// for rollback
			panel.updateDataserviceConnection(panel.mPreviousConnection);	// update the model
			try {

				mDataservicePersister.update(tmp.getName(), panel.mPreviousConnection);
				panel.mChanges = false;

			} catch (FileNotFoundException e) {
				log.error("Error while updating the Dataservice-Connection", e);
				//rollBack
				panel.mPreviousConnection.update(tmp);
				resetSelection();
				throw e;
			} catch (DataservicePersisterException e) {
				log.warn(e.toString());
				//rollBack
				panel.mPreviousConnection.update(tmp);
				resetSelection();
				throw e;
			}
		}else if(mJPanel instanceof MapServerPanel){
			MapServerPanel panel = (MapServerPanel)mJPanel;

			RestConnection tmp = panel.mPreviousConnection.copy();	// for rollback
			panel.updateRestConnection(panel.mPreviousConnection);	// update the model

			try {
				panel.mPreviousConnection.saveInDataservice();
				panel.mChanges = false;
			}catch (UniformInterfaceException | JSONException e){
				log.error("error while save RestConnection in dataservice", e);
				//rollBack
				panel.mPreviousConnection.update(tmp);
				resetSelection();
				throw e;
			}
		}
	}

	public void noOption() {
		resetSelection();
	}

	private void resetSelection(){
		mResetSelection = true;
		setSelectedIndex(mPreviousIndex);
		mResetSelection = false;
	}

}
