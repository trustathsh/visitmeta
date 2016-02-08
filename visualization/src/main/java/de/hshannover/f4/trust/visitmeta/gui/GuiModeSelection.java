package de.hshannover.f4.trust.visitmeta.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;

public class GuiModeSelection {

	private Logger mLogger = Logger.getLogger(GuiModeSelection.class);
	
	private JPanel mPanel;
	
	private Map<JRadioButton, GuiMode> mRadioButtons;
	
	private GraphPanel mGraphPanel;

	public GuiModeSelection(GraphPanel graphPanel) {
		mGraphPanel = graphPanel;
		mPanel = new JPanel();
		mRadioButtons = new HashMap<>();
		
		JRadioButton stateSwitchAnalysisMode = new JRadioButton("Anaysis", true);
		stateSwitchAnalysisMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mGraphPanel.setGuiMode(GuiMode.ANALYSIS);
				mLogger.debug("Gui mode changed to " + GuiMode.ANALYSIS);
			}
		});
		JRadioButton stateSwitchMonitoringMode = new JRadioButton("Monitoring", false);
		stateSwitchMonitoringMode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mGraphPanel.setGuiMode(GuiMode.MONITORING);
				mLogger.debug("Gui mode changed to " + GuiMode.MONITORING);
			}
		});

		mRadioButtons.put(stateSwitchAnalysisMode, GuiMode.ANALYSIS);
		mRadioButtons.put(stateSwitchMonitoringMode, GuiMode.MONITORING);
		
		ButtonGroup stateSwitchButtonGroup = new ButtonGroup();
		stateSwitchButtonGroup.add(stateSwitchAnalysisMode);
		stateSwitchButtonGroup.add(stateSwitchMonitoringMode);
		
		mPanel.add(stateSwitchAnalysisMode);
		mPanel.add(stateSwitchMonitoringMode);
	}
	
	public JPanel getJPanel() {
		return mPanel;
	}

	public GuiMode getSelectedMode() {
		for (JRadioButton button : mRadioButtons.keySet()) {
			if (button.isSelected()) {
				return mRadioButtons.get(button);
			}
		}
		
		return null;
	}
	
}
