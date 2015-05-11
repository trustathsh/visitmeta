package de.hshannover.f4.trust.visitmeta.gui.dialog;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;

public class SubscriptionParameterPanel extends JPanel {

	private static final long serialVersionUID = -3686612903315798696L;

	private JLabel mJlName;
	private JLabel mJlStartIdentifier;
	private JLabel mJlStartIdentifierType;
	private JLabel mJlFilterLinks;
	private JLabel mJlFilterResult;
	private JLabel mJlTerminalIdentifierTypes;
	private JLabel mJlStartupSubscribe;
	private JLabel mJlMaxDepth;
	private JLabel mJlMaxSize;

	private JTextField mJtfName;
	private JTextField mJtfStartIdentifier;
	private JTextField mJtfStartIdentifierType;
	private JTextField mJtfFilterLinks;
	private JTextField mJtfFilterResult;
	private JTextField mJtfTerminalIdentifierTypes;
	private JTextField mJtfMaxDepth;
	private JTextField mJtfMaxSize;

	private JCheckBox mJcbStartupSubscribe;

	public SubscriptionParameterPanel() {
		createPanels();
	}

	public SubscriptionParameterPanel(SubscriptionData subscription) {
		createPanels();

		updatePanel(subscription);
	}

	private void createPanels() {
		setLayout(new GridBagLayout());

		mJlName = new JLabel("Name");
		mJlStartIdentifier = new JLabel("Start Identifier");
		mJlStartIdentifierType = new JLabel("Start Identifier Type");
		mJlFilterLinks = new JLabel("Filter Links");
		mJlFilterResult = new JLabel("Filter Result");
		mJlTerminalIdentifierTypes = new JLabel("Terminal Identifier Types");
		mJlMaxDepth = new JLabel("Max Depth");
		mJlMaxSize = new JLabel("max Size");
		mJlStartupSubscribe = new JLabel("Subscribe at start-up");

		mJtfName = new JTextField();
		mJtfName.setEditable(false);
		mJtfStartIdentifier = new JTextField();
		mJtfStartIdentifierType = new JTextField();
		mJtfFilterLinks = new JTextField();
		mJtfFilterResult = new JTextField();
		mJtfTerminalIdentifierTypes = new JTextField();
		mJtfMaxDepth = new JTextField();
		mJtfMaxSize = new JTextField();

		mJcbStartupSubscribe = new JCheckBox();

		// x y w h wx wy
		LayoutHelper.addComponent(0, 0, 1, 1, 1.0, 1.0, this, mJlName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 1, 1, 1, 1.0, 1.0, this, mJlStartIdentifier, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 2, 1, 1, 1.0, 1.0, this, mJlStartIdentifierType, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 3, 1, 1, 1.0, 1.0, this, mJlFilterLinks, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 4, 1, 1, 1.0, 1.0, this, mJlFilterResult, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 5, 1, 1, 1.0, 1.0, this, mJlTerminalIdentifierTypes, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 6, 1, 1, 1.0, 1.0, this, mJlMaxDepth, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 7, 1, 1, 1.0, 1.0, this, mJlMaxSize, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(0, 8, 1, 1, 1.0, 1.0, this, mJlStartupSubscribe, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 0, 1, 1, 1.0, 1.0, this, mJtfName, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 1, 1, 1, 1.0, 1.0, this, mJtfStartIdentifier, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 2, 1, 1, 1.0, 1.0, this, mJtfStartIdentifierType, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 3, 1, 1, 1.0, 1.0, this, mJtfFilterLinks, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 4, 1, 1, 1.0, 1.0, this, mJtfFilterResult, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 5, 1, 1, 1.0, 1.0, this, mJtfTerminalIdentifierTypes, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 6, 1, 1, 1.0, 1.0, this, mJtfMaxDepth, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 7, 1, 1, 1.0, 1.0, this, mJtfMaxSize, LayoutHelper.mLblInsets);
		LayoutHelper.addComponent(1, 8, 1, 1, 1.0, 1.0, this, mJcbStartupSubscribe, LayoutHelper.mLblInsets);
	}

	private void updatePanel(SubscriptionData subscription) {
		mJtfName.setText(subscription.getName());
		mJtfStartIdentifier.setText(subscription.getStartIdentifier());
		mJtfStartIdentifierType.setText(subscription.getIdentifierType());
		mJtfFilterLinks.setText(subscription.getMatchLinksFilter());
		mJtfFilterResult.setText(subscription.getResultFilter());
		mJtfTerminalIdentifierTypes.setText(subscription.getTerminalIdentifierTypes());
		mJtfMaxDepth.setText(String.valueOf(subscription.getMaxDepth()));
		mJtfMaxSize.setText(String.valueOf(subscription.getMaxSize()));
		mJcbStartupSubscribe.setSelected(subscription.isStartupSubscribe());
	}
}
