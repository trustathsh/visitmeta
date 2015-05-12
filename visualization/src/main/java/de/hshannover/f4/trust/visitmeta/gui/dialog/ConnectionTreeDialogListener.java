package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ConnectionTreeDialogListener implements MouseListener {

	private NewConnectionDialog mConnectionDialog;

	public ConnectionTreeDialogListener(NewConnectionDialog connectionDialog) {
		mConnectionDialog = connectionDialog;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		mConnectionDialog.switchParameterPanel();
	}

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3) {
			mConnectionDialog.showConnectionTreePopupMenu(event.getX(), event.getY());
		}
	}

}
