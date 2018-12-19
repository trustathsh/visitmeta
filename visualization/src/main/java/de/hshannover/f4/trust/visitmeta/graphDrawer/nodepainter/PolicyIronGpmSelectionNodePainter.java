package de.hshannover.f4.trust.visitmeta.graphDrawer.nodepainter;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.datawrapper.policy.PolicyNode;
import de.hshannover.f4.trust.visitmeta.datawrapper.policy.PolicyType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.GraphPanel;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.ExtendedIdentifierHelper;

public class PolicyIronGpmSelectionNodePainter extends SelectionNodePainter {

	private static final Logger LOGGER = Logger.getLogger(PolicyIronGpmSelectionNodePainter.class);
	
	private static final List<String> POLICY_IDENTIFIER_TYPENAMES = Arrays.asList("rule", "policy", "pattern-vertex");
	private static final List<String> POLICY_METADATA_TYPENAMES = Arrays.asList("policy-rule", "rule-pattern", "pattern-edge", "pattern-metadata", "pattern-matched");
	
	private static final String POLICY_METADATA_NS_URI = "http://www.trust.f4.hs-hannover.de/2016/POLICY/IRONGPM/METADATA/1";
	private static final String POLICY_IDENTIFIER_NS_URI = "http://www.trust.f4.hs-hannover.de/2016/POLICY/IRONGPM/IDENTIFIER/1";
	
	private static final String POLICY_METADATA_NS_URI_PREFIX = "xmlns:policy";

	private GraphPanel mPanel;

	private Propable mSelectedNode;

	private boolean mIsValid;

	private Set<GraphicWrapper> mSelectedPolicyParents;
	private Set<GraphicWrapper> mSelectedPolicyChilds;

	
	public PolicyIronGpmSelectionNodePainter(GraphPanel panel) {
		super(panel);
		mPanel = panel;
	}

	private boolean isNewSelectedNode(GraphicWrapper selectedNode) {
		Propable selectedPropable = selectedNode.getData();

		if (selectedPropable != null && mSelectedNode != selectedPropable) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setNewNewSelectedNode(GraphicWrapper selectedNode) {
		Propable selectedPropable = (Propable) selectedNode.getData();

		mSelectedNode = selectedPropable;
		if (checkIfPolicyNode(mSelectedNode)) {
			mIsValid = true;
			mSelectedPolicyChilds = PolicyNode.getAllChilds(selectedNode, PolicyType.IRONGPM);
			mSelectedPolicyParents = PolicyNode.getAllParents(selectedNode, PolicyType.IRONGPM);
		} else {
			mIsValid = false;
		}
	}
	
	private boolean checkIfPolicyNode(Propable selectedNode) {
		if (selectedNode instanceof Identifier) {
			return checkIfPolicyIdentifier((Identifier) selectedNode);
		} else if (selectedNode instanceof Metadata) {
			return checkIfPolicyMetadata((Metadata) selectedNode);
		}
		return false;
	}
	
	private boolean checkIfPolicyMetadata(Metadata metadata) {
		LOGGER.trace("Check if metadata is a IronGPM policy metadatum.");
		
		String typeName = metadata.getTypeName();
		if (POLICY_METADATA_TYPENAMES.contains(typeName) && checkUrlOfMetadata(metadata)) {
			return true;
		}
		
		return false;
	}

	private boolean checkUrlOfMetadata(Metadata metadata) {
		LOGGER.trace("Check if XML namespace of metadata is the correct IronGPM policy metadata XML namespace.");
		
		String key = findUrlPropertyKey(metadata.getProperties());
		
		if (!key.isEmpty()) {
			String xmlnsProperty = metadata.valueFor(key);
			if (xmlnsProperty.equals(POLICY_METADATA_NS_URI)) {
				return true;
			}
		} 
		
		return false;
	}

	private String findUrlPropertyKey(List<String> properties) {
		for (String property : properties) {
			if (property.contains(POLICY_METADATA_NS_URI_PREFIX)) {
				return property;
			}
		}
		return "";
	}

	private boolean checkIfPolicyIdentifier(Identifier identifier) {
		LOGGER.trace("Check if identifier is a IronGPM policy identifier.");
		if (ExtendedIdentifierHelper.isExtendedIdentifier(identifier)) {
			String innerTypeName = ExtendedIdentifierHelper.getExtendedIdentifierInnerTypeName(identifier);
			
			if (POLICY_IDENTIFIER_TYPENAMES.contains(innerTypeName) && checkUrlOfIdentifier(identifier)) {
				return true;
			} else {
				LOGGER.trace("Identifier is not a IronGPM policy identifier.");
				return false;
			}
		} else {
			LOGGER.trace("Identifier is not a IronGPM policy identifier.");
			return false;
		}
	}

	private boolean checkUrlOfIdentifier(Identifier identifier) {
		LOGGER.trace("Check if XML namespace of identifier is the correct IronGPM policy identifier XML namespace.");
		Document document = ExtendedIdentifierHelper.getDocument(identifier);

		if (document != null) {
			String xmlnsAttribute = document.getDocumentElement().getAttribute("xmlns");

			if (xmlnsAttribute.equals(POLICY_IDENTIFIER_NS_URI)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void paintMetadataNode(Metadata metadata, GraphicWrapper graphic) {
		GraphicWrapper selectedNode = mPanel.getSelectedNode();

		if(selectedNode == null){
			return;
		}

		// only if selected node is a IronGPM policy metadata
		if (isNewSelectedNode(selectedNode)) {
			setNewNewSelectedNode(selectedNode);
		}

		if (!mIsValid) {
			return;
		}

		if (mSelectedPolicyParents.contains(graphic) || mSelectedPolicyChilds.contains(graphic)) {
			graphic.setPaint(mColorSelectedNode);
		}
	}

	@Override
	public void paintIdentifierNode(Identifier identifier, GraphicWrapper graphic) {
		GraphicWrapper selectedNode = mPanel.getSelectedNode();

		if (selectedNode == null) {
			return;
		}

		// only if selected node is a IronGPM policy identifier
		if (isNewSelectedNode(selectedNode)) {
			setNewNewSelectedNode(selectedNode);
		}

		if (!mIsValid) {
			return;
		}

		if (mSelectedPolicyParents.contains(graphic) || mSelectedPolicyChilds.contains(graphic)) {
			graphic.setPaint(mColorSelectedNode);
		}

	}

}
