package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ConnectionTreeMainWindowListener implements MouseListener {

	private MainWindow mMainWindow;

	public ConnectionTreeMainWindowListener(MainWindow mainWindow) {
		mMainWindow = mainWindow;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON3) {

			mMainWindow.showConnectionTreePopupMenu(event.getX(), event.getY());

		} else if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() > 1) {

			mMainWindow.showConnectionTab();

		}
	}

	@Override
	public void mousePressed(MouseEvent event) {
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

}
