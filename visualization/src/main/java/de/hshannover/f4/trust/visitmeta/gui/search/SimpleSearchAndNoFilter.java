package de.hshannover.f4.trust.visitmeta.gui.search;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

public class SimpleSearchAndNoFilter implements SeachAndFilterStrategy {

	private JPanel mSearchInputPanel;
	private Searchable mSearchableGraphPanel;
	private JTextField mInputTextfield;

	public SimpleSearchAndNoFilter(Searchable searchableGraphPanel) {
		mSearchableGraphPanel = searchableGraphPanel;

		initGUI();
	}

	private void initGUI() {
		mSearchInputPanel = new JPanel();
		mSearchInputPanel.setLayout(new BoxLayout(mSearchInputPanel,
				BoxLayout.X_AXIS));

		DefaultFormatter formatter = new DefaultFormatter();
		formatter.setCommitsOnValidEdit(true);
		mInputTextfield = new JFormattedTextField(formatter);

		mInputTextfield.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				mSearchableGraphPanel.search(mInputTextfield.getText());
			}
		});

		mSearchInputPanel.add(new JLabel("Search: "));
		mSearchInputPanel.add(mInputTextfield);
	}

	@Override
	public JPanel getJPanel() {
		return mSearchInputPanel;
	}

}
