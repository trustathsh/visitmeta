package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ConnectionDialogWindowListener implements WindowListener {

	private NewConnectionDialog mDialog;

	public ConnectionDialogWindowListener(NewConnectionDialog dialog) {
		mDialog = dialog;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// mDialog.dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		JTextAreaAppander.clearJTextAreas();
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}
