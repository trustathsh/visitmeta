package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ConnectionTreeDialogListener implements MouseListener {

	private NewConnectionDialog mConnectionDialog;

	public ConnectionTreeDialogListener(NewConnectionDialog connectionDialog) {
		mConnectionDialog = connectionDialog;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		mConnectionDialog.switchParameterPanel();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
