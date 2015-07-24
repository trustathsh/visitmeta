package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public abstract class ParameterPanel extends JPanel {

	private static final long serialVersionUID = 7267839554328061241L;

	private List<ParameterListener> mParameterListeners;

	public ParameterPanel() {
		mParameterListeners = new ArrayList<ParameterListener>();
	}

	protected void fireParameterChanged() {
		for (ParameterListener pml : mParameterListeners) {
			pml.parameterChanged();
		}
	}

	public void addParameterListener(ParameterListener newListener) {
		mParameterListeners.add(newListener);
	}

	public abstract Data getData();

	public abstract void setNameTextFieldEditable();
}
