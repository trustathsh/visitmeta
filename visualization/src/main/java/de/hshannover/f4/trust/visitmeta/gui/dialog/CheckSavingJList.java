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
