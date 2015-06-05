package de.hshannover.f4.trust.visitmeta.gui.util;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DocumentChangedListener implements DocumentListener {

	@Override
	public void changedUpdate(DocumentEvent e) {
			dataChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
			dataChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
			dataChanged();
	}

	protected abstract void dataChanged();

}
