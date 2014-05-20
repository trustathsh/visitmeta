package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.Component;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DocumentChangedListener implements DocumentListener {

	private Component mSource;

	public DocumentChangedListener(Component source){
		mSource = source;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		if(mSource.hasFocus()){
			dataChanged();
		}
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		if(mSource.hasFocus()){
			dataChanged();
		}
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		if(mSource.hasFocus()){
			dataChanged();
		}
	}

	protected abstract void dataChanged();

}
