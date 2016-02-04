package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.IfmapException;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.MetadataLifetime;
import de.hshannover.f4.trust.ifmapj.messages.PublishDelete;
import de.hshannover.f4.trust.ifmapj.messages.PublishRequest;
import de.hshannover.f4.trust.ifmapj.messages.PublishUpdate;
import de.hshannover.f4.trust.ifmapj.messages.Requests;
import de.hshannover.f4.trust.ifmapj.metadata.Cardinality;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactory;
import de.hshannover.f4.trust.ifmapj.metadata.StandardIfmapMetadataFactoryImpl;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.IdentityGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.gui.dialog.DialogHelper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.network.otherservices.IfmapConnection;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;
import de.hshannover.f4.trust.visitmeta.util.StringHelper;

public class EditPolicyNodeContextMenuItem implements ContextMenuItem {

	private static final Object POLICY_IDENTIFIER_CONDITION = "condition";

	private static final String POLICY_QUALIFIED_NAME = "policy";;
	private static final String POLICY_METADATA_NS_URI = "http://www.trust.f4.hs-hannover.de/2015/POLICY/METADATA/1";

	private static final String POLICY_METADATA_LINK = "has-element";

	private Logger logger = Logger.getLogger(EditPolicyNodeContextMenuItem.class);
	
	private List<String> policyTypeNames = Arrays.asList("signature");
//	private List<String> policyTypeNames = Arrays.asList("signature", "anomaly", "rule", "condition");

	private IfmapConnection mIfmapConnection;

	private StandardIfmapMetadataFactory mMetadataFactory;

	public EditPolicyNodeContextMenuItem() {
		try {
			mIfmapConnection = new IfmapConnection();
			mMetadataFactory = new StandardIfmapMetadataFactoryImpl();
		} catch (IfmapErrorResult | IfmapException e) {
			DialogHelper.showErrorDialog(e.getMessage(), e.getClass().getSimpleName());
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public void actionPerformed(GraphicWrapper wrapper) {
		GraphicWrapper condition = findConditionNode(wrapper);
		if (condition == null) {
			logger.error("No condition identifier was found for signature: " + wrapper.getData().toString());
			DialogHelper.showErrorDialog("No condition identifier was found for signature: " + wrapper.getData().toString(), "No condition identifier found");
		} else {
			JDialog editWindow = createGUI(wrapper, condition);
			editWindow.setVisible(true);
		}
	}

	private JDialog createGUI(final GraphicWrapper originalSignature, final GraphicWrapper condition) {
		Propable originalSignaturePropable = originalSignature.getData();
		
		Dialog owner = null;
		final JDialog window = new JDialog(owner, getItemTitle());
		window.setSize(400, 200);
		window.setLayout(new BorderLayout());
		
		final Document originalSignatureDocument = ExtendedIdentifierHelper.getDocument(originalSignaturePropable);
		final Document newSignatureDocument = ExtendedIdentifierHelper.getDocument(originalSignaturePropable);
		final Document conditionDocument = ExtendedIdentifierHelper.getDocument(condition.getData());
		
		String id = originalSignatureDocument.getElementsByTagName("id").item(0).getTextContent();
		String featureExpression = originalSignatureDocument.getElementsByTagName("featureExpression").item(0).getTextContent();
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
		JLabel titleLabel = new JLabel("Signature");
		JLabel idLabel = new JLabel("ID: " + id);
		JLabel featureExpressionLabel = new JLabel("Feature Expression:");
		final JTextField featureExpressionValueTextField = new JTextField(featureExpression);
		contentPanel.add(titleLabel);
		contentPanel.add(idLabel);
		contentPanel.add(featureExpressionLabel);
		contentPanel.add(featureExpressionValueTextField);
		
		JPanel buttonPanel = new JPanel();
		JButton saveButton = new JButton("Save changes");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String newFeatureExpressionValue = featureExpressionValueTextField.getText();
					
					Node node = newSignatureDocument.getElementsByTagName("featureExpression").item(0);
					node.setTextContent(newFeatureExpressionValue);
					
					PublishRequest deleteRequest = createDeleteRequest(originalSignatureDocument, conditionDocument);
					PublishRequest updateRequest = createUpdateRequest(newSignatureDocument, conditionDocument);
					
					mIfmapConnection.send(deleteRequest);
					mIfmapConnection.send(updateRequest);
				} catch (IfmapErrorResult | IfmapException exception) {
					DialogHelper.showErrorDialog(StringHelper.breakLongString(e.toString(), 80), e.getClass().getSimpleName());
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
		window.add(contentPanel, BorderLayout.CENTER);
		window.add(buttonPanel, BorderLayout.SOUTH);
		
		return window;
	}
	
	private GraphicWrapper findConditionNode(GraphicWrapper wrapper) {
		List<GraphicWrapper> edgeNodes = wrapper.getEdgesNodes();
		for (GraphicWrapper edgeNode : edgeNodes) {
			if (edgeNode.getNodeTypeName().equals(POLICY_METADATA_LINK)) {
				List<GraphicWrapper> edgeNodes2 = edgeNode.getEdgesNodes();
				for (GraphicWrapper edgeNode2 : edgeNodes2) {
					if (edgeNode2 instanceof IdentityGraphicWrapper) {
						IdentityGraphicWrapper identity = (IdentityGraphicWrapper) edgeNode2;
						if (identity.getExtendedNodeTypeName().equals(POLICY_IDENTIFIER_CONDITION)) {
							return edgeNode2;
						}
					}
				}
			}
		}
		
		return null;
	}

	private PublishRequest createUpdateRequest(Document signatureDocument, Document conditionDocument) {
		PublishRequest request = Requests.createPublishReq();
		
		de.hshannover.f4.trust.ifmapj.identifier.Identifier signatureIdentifier;
		de.hshannover.f4.trust.ifmapj.identifier.Identifier conditionIdentifier;
		try {
			signatureIdentifier = Identifiers.createExtendedIdentity(signatureDocument);
			conditionIdentifier = Identifiers.createExtendedIdentity(conditionDocument);

			PublishUpdate publishUpdate = Requests.createPublishUpdate();
			publishUpdate.setIdentifier1(signatureIdentifier);
			publishUpdate.setIdentifier2(conditionIdentifier);
			publishUpdate.addMetadata(createMetadata());
			publishUpdate.setLifeTime(MetadataLifetime.forever);
			
			request.addPublishElement(publishUpdate);
		} catch (MarshalException e) {
			DialogHelper.showErrorDialog(e.getMessage(), "Marshal Exception");
			logger.error(e.getMessage());
		}
		
		return request;
	}
	
	private PublishRequest createDeleteRequest(Document signatureDocument, Document conditionDocument) {
		PublishRequest request = Requests.createPublishReq();
		
		de.hshannover.f4.trust.ifmapj.identifier.Identifier signatureIdentifier;
		de.hshannover.f4.trust.ifmapj.identifier.Identifier conditionIdentifier;
		try {
			signatureIdentifier = Identifiers.createExtendedIdentity(signatureDocument);
			conditionIdentifier = Identifiers.createExtendedIdentity(conditionDocument);
			
			PublishDelete publishDelete = Requests.createPublishDelete();
			publishDelete.setIdentifier1(signatureIdentifier);
			publishDelete.setIdentifier2(conditionIdentifier);
			
			request.addPublishElement(publishDelete);
		} catch (MarshalException e) {
			DialogHelper.showErrorDialog(e.getMessage(), "Marshal Exception");
			logger.error(e.getMessage());
		}
		
		return request;
	}

	private Document createMetadata() {
		Document doc = mMetadataFactory.create(POLICY_METADATA_LINK, POLICY_QUALIFIED_NAME,
				POLICY_METADATA_NS_URI, Cardinality.singleValue);

		return doc;
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
