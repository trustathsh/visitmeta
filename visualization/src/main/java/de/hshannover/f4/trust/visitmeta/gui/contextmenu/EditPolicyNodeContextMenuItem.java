package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JWindow;

import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.messages.MetadataLifetime;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.PublishUpdate;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.visitmeta.gui.PanelXmlTree;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.network.otherservices.IfmapConnection;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.StringHelper;

public class EditPolicyNodeContextMenuItem implements ContextMenuItem {

	private List<String> policyTypeNames = Arrays.asList("signature", "anomaly", "rule", "condition");

	private IfmapConnection mIfmapConnection;

	public EditPolicyNodeContextMenuItem() {
		try {
			mIfmapConnection = new IfmapConnection();
		} catch (IfmapErrorResult | IfmapException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(Propable propable) {
		JWindow editWindow = createGUI(propable);
		editWindow.setVisible(true);
	}

	private JWindow createGUI(Propable propable) {
		final JWindow window = new JWindow();
		window.setLayout(new BorderLayout());
		
		// TODO create new GUI element to show and edit node content (using detail information or new popup menu?)
		// TODO use PanelXMWTree to display content
		
		PanelXmlTree content = new PanelXmlTree();
		content.fill(propable);

		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Save changes");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// TODO get metadata for link to parent (from policy-viewpoint) and associated parent identifier
					
					PublishRequest request = createRequest();
					
					// TODO send publish request (lifetime forever) via ifmapj as (new or existing?) user; new connection with username policy-editor?
					mIfmapConnection.send(request);
				} catch (IfmapErrorResult | IfmapException exception) {
					JOptionPane.showMessageDialog(null, StringHelper.breakLongString(e.toString(), 80), exception.getClass()
							.getSimpleName(), JOptionPane.ERROR_MESSAGE);
				}
				window.setVisible(false);
			}
		});
		
		JButton discardButton = new JButton("Discard changes");
		discardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.setVisible(false);
			}
		});
		buttonPanel.add(saveButton);
		buttonPanel.add(discardButton);
		
		JRadioButton destinationSwitchProductive = new JRadioButton("Productive", true);
		JRadioButton destinationSwitchWhatIf = new JRadioButton("What-If", false);
		ButtonGroup destinationSwitch = new ButtonGroup();
		destinationSwitch.add(destinationSwitchProductive);
		destinationSwitch.add(destinationSwitchWhatIf);
		
		JPanel destinationSwitchPanel = new JPanel();
		destinationSwitchPanel.add(destinationSwitchProductive);
		destinationSwitchPanel.add(destinationSwitchWhatIf);
		
		window.add(destinationSwitchPanel, BorderLayout.NORTH);
		window.add(content, BorderLayout.CENTER);
		window.add(buttonPanel, BorderLayout.SOUTH);
		
		return window;
	}
	
	private PublishRequest createRequest() {
		PublishRequest request = Requests.createPublishReq();
		
		PublishUpdate publishUpdate = Requests.createPublishUpdate();
		publishUpdate.setLifeTime(MetadataLifetime.forever);
		
		request.addPublishElement(publishUpdate);
		
		
		return request;
	}

	@Override
	public String getItemTitle() {
		return "Edit Policy item";
	}

	@Override
	public boolean canHandle(Propable node) {
		if (node instanceof Identifier) {
			Identifier i = (Identifier) node;
			if (ExtendedIdentifierHelper.isExtendedIdentifier(i) &&
					policyTypeNames.contains(ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName(i).toLowerCase())) {
				return true;
			} else if (policyTypeNames.contains(i.getTypeName())) {
				return true;
			} else {
				return false;
			}
		} else if (node instanceof Metadata) {
			Metadata m = (Metadata) node;
			if (policyTypeNames.contains(m.getTypeName().toLowerCase())) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

}
