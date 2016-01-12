/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-visualization, version 0.5.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2015 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.IdentityGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.policy.ConditionElement;
import de.hshannover.f4.trust.visitmeta.graphDrawer.policy.ConditionElement.ConditionElementType;
import de.hshannover.f4.trust.visitmeta.graphDrawer.policy.PolicyNode;

public class Piccolo2DPolicyActionGraphicWrapper extends Piccolo2DGraphicWrapper implements PolicyActionGraphicWrapper {

	private static final List<String> VALIDE_CONDITION_ELEMENT_NODE_NAMES = Arrays.asList(new String[] {
			"signature-result", "anomaly-result" });

	private static final String VALIDE_FEATURE_ELEMENT_NODE_NAME = "feature";

	private static final String ELEMENT_ID_KEY = "id";

	private static final String ELEMENT_RESULT_KEY = "result";

	private static final String ELEMENT_DEVICE_KEY = "device";

	private static final String ELEMENT_POLICY_KEY = "policy";

	private static final String ELEMENT_TYPE_NAME_ACTION = "action";

	private static final String ELEMENT_TYPE_NAME_RULE = "rule";

	private static final String ELEMENT_TYPE_NAME_POLICY_ACTION = "policy-action";

	private static final String ELEMENT_TYPE_NAME_HAS_ELEMET = "has-element";

	public Piccolo2DPolicyActionGraphicWrapper(PPath node, PText text) {
		super(node, text);
	}

	public Piccolo2DPolicyActionGraphicWrapper(Piccolo2DGraphicWrapper policyAction) {
		this(policyAction.mNode, policyAction.mText);
	}

	@Override
	public List<PolicyActionGraphicWrapper> getOtherPolicyActions() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = new ArrayList<PolicyActionGraphicWrapper>();
		List<GraphicWrapper> edgesNodes = super.getEdgesNodes();

		if (edgesNodes != null) {
			for (GraphicWrapper edgeNode : edgesNodes) {
				if (!(edgeNode instanceof IdentityGraphicWrapper)) {
					continue;
				}

				IdentityGraphicWrapper identityNode = (IdentityGraphicWrapper) edgeNode;

				if (!ELEMENT_TYPE_NAME_ACTION.equals(identityNode.getExtendetNodeTypeName())) {
					continue;
				}

				// action identifier only

				List<GraphicWrapper> identifierEdgesNodes = identityNode.getEdgesNodes();
				for (GraphicWrapper identifierEdgeNode : identifierEdgesNodes) {
					if (!(identifierEdgeNode instanceof PolicyActionGraphicWrapper)) {
						continue;
					}

					PolicyActionGraphicWrapper policyActionNode = (PolicyActionGraphicWrapper) identifierEdgeNode;

					if (!ELEMENT_TYPE_NAME_POLICY_ACTION.equals(policyActionNode.getNodeTypeName())) {
						continue;
					}

					// policy-action metadata only

					if (equals(policyActionNode)) {
						continue;
					}

					// other policy-action metadatas only

					otherPolicyActions.add(policyActionNode);
				}

				// policy-action metadata has only one action identifier
				break;
			}
		}

		return otherPolicyActions;
	}

	@Override
	public List<PolicyActionGraphicWrapper> getOtherPolicyActionsSameRule() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = getOtherPolicyActions();
		List<PolicyActionGraphicWrapper> policyActionsSameRule = new ArrayList<PolicyActionGraphicWrapper>();

		String ruleId = getRuleId();

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActions) {
			if (ruleId.equals(policyAction.getRuleId())) {
				policyActionsSameRule.add(policyAction);
			}
		}

		return policyActionsSameRule;
	}

	@Override
	public List<PolicyActionGraphicWrapper> getOtherPolicyActionsSameDevice() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = getOtherPolicyActions();
		List<PolicyActionGraphicWrapper> policyActionsSameDevice = new ArrayList<PolicyActionGraphicWrapper>();

		String device = getDevice();

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActions) {
			if (device.equals(policyAction.getDevice())) {
				policyActionsSameDevice.add(policyAction);
			}
		}

		return policyActionsSameDevice;
	}

	@Override
	public List<PolicyActionGraphicWrapper> getOtherPolicyActionsSameRuleAndDevice() {
		List<PolicyActionGraphicWrapper> policyActionsSameRule = getOtherPolicyActionsSameRule();
		List<PolicyActionGraphicWrapper> policyActionsSameRuleAndDevice = new ArrayList<PolicyActionGraphicWrapper>();

		String device = getDevice();

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameRule) {
			if (device.equals(policyAction.getDevice())) {
				policyActionsSameRuleAndDevice.add(policyAction);
			}
		}

		return policyActionsSameRuleAndDevice;
	}

	@Override
	public List<PolicyActionGraphicWrapper> getPolicyActionsSameRule() {
		List<PolicyActionGraphicWrapper> allPolicyActions = getAllPolicyActionsFromRule();
		List<PolicyActionGraphicWrapper> policyActionsSameRule = new ArrayList<PolicyActionGraphicWrapper>();
		
		String ruleId = getRuleId();

		for (PolicyActionGraphicWrapper policyAction : allPolicyActions) {
			if (ruleId.equals(policyAction.getRuleId())) {
				policyActionsSameRule.add(policyAction);
			}
		}

		return policyActionsSameRule;
	}

	@Override
	public List<PolicyActionGraphicWrapper> getPolicyActionsSameRuleAndDevice() {
		List<PolicyActionGraphicWrapper> policyActionsSameRule = getPolicyActionsSameRule();
		List<PolicyActionGraphicWrapper> policyActionsSameRuleAndDevice = new ArrayList<PolicyActionGraphicWrapper>();

		String device = getDevice();

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameRule) {
			if (device.equals(policyAction.getDevice())) {
				policyActionsSameRuleAndDevice.add(policyAction);
			}
		}

		return policyActionsSameRuleAndDevice;
	}

	@Override
	public List<String> getDevicesSameRule() {
		List<PolicyActionGraphicWrapper> policyActionsSameRule = getPolicyActionsSameRule();
		List<String> devices = new ArrayList<String>();

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameRule) {
			String device = policyAction.getDevice();
			if (!devices.contains(device)) {
				devices.add(device);
			}
		}
		return devices;
	}

	@Override
	public List<String> getDevicesSameAction() {
		List<PolicyActionGraphicWrapper> policyActionsSameAction = getOtherPolicyActions();
		List<String> devices = new ArrayList<String>();

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameAction) {
			String device = policyAction.getDevice();
			if (!devices.contains(device)) {
				devices.add(device);
			}
		}
		return devices;
	}

	@Override
	public List<Element> getFeatureElements() {
		Element featureElement = (Element) super.getRootElement().getLastChild();
		List<Element> featureElements = new ArrayList<Element>();

		while (isFeatureElement(featureElement)) {
			featureElements.add(featureElement);

			featureElement = (Element) featureElement.getPreviousSibling();
		}

		return featureElements;
	}

	@Override
	public String getRuleId() {
		Element ruleResultElement = getRuleResultElement();

		NodeList childs = ruleResultElement.getChildNodes();

		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			String nodeName = node.getNodeName();
			if (!ELEMENT_ID_KEY.equals(nodeName)) {
				continue;
			}
			String ruleId = node.getTextContent();
			return ruleId;
		}
		return null;
	}

	@Override
	public boolean getRuleResult() {
		Element ruleResultElement = getRuleResultElement();

		NodeList childs = ruleResultElement.getChildNodes();

		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			String nodeName = node.getNodeName();
			if (!ELEMENT_RESULT_KEY.equals(nodeName)) {
				continue;
			}
			String ruleResult = node.getTextContent();
			return Boolean.valueOf(ruleResult);
		}
		return false;
	}

	@Override
	public String getDevice() {
		Element rootElement = super.getRootElement();

		NodeList childs = rootElement.getChildNodes();

		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			String nodeName = node.getNodeName();
			if (!ELEMENT_DEVICE_KEY.equals(nodeName)) {
				continue;
			}
			String device = node.getTextContent();
			return device;
		}
		return null;
	}

	@Override
	public List<ConditionElement> getConditionElementResults() {
		List<Element> conditions = getConditionElements();
		List<ConditionElement> conditionElements = new ArrayList<ConditionElement>();

		for (Element condition : conditions) {
			ConditionElement conditionElement = getConditionElement(condition);
			conditionElements.add(conditionElement);

			// check of hints
			NodeList childs = condition.getChildNodes();
			if (childs.getLength() > 2) {
				// skip item index 0 & 1 ( id and result )
				Node firstHintNode = childs.item(2);

				Element child = (Element) firstHintNode;
				do {
					ConditionElement childElement = getConditionElement(child);
					conditionElement.childs.add(childElement);

					child = (Element) child.getNextSibling();

				} while (child != null);
			}
		}

		return conditionElements;
	}

	private ConditionElement getConditionElement(Element condition) {
		NodeList childs = condition.getChildNodes();
		ConditionElement conditionElement = new ConditionElement();

		String conditionNodeName = condition.getNodeName();
		conditionElement.type = ConditionElementType.valueOfString(conditionNodeName);

		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			String nodeName = node.getNodeName();
			if (ELEMENT_ID_KEY.equals(nodeName)) {
				conditionElement.id = node.getTextContent();
			} else if (ELEMENT_RESULT_KEY.equals(nodeName)) {
				conditionElement.result = Boolean.valueOf(node.getTextContent());
			}
		}
		return conditionElement;
	}

	@Override
	public int getRuleTrueCount() {
		List<PolicyActionGraphicWrapper> otherPolicyActionsSameRule = getOtherPolicyActionsSameRule();

		int count = 0;

		// it self
		if (getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActionsSameRule) {
			if (policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getRuleFalseCount() {
		List<PolicyActionGraphicWrapper> otherPolicyActionsSameRule = getOtherPolicyActionsSameRule();

		int count = 0;

		// it self
		if (!getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActionsSameRule) {
			if (!policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getActionTrueCount() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = getOtherPolicyActions();

		int count = 0;

		// it self
		if (getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActions) {
			if (policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getActionFalseCount() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = getOtherPolicyActions();

		int count = 0;

		// it self
		if (!getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActions) {
			if (!policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getRuleTrueCountForDevice() {
		List<PolicyActionGraphicWrapper> policyActionsSameRule = getOtherPolicyActionsSameRuleAndDevice();

		int count = 0;

		// it self
		if (getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameRule) {
			if (policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getRuleFalseCountForDevice() {
		List<PolicyActionGraphicWrapper> policyActionsSameRule = getOtherPolicyActionsSameRuleAndDevice();

		int count = 0;

		// it self
		if (!getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameRule) {
			if (!policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getActionTrueCountForDevice() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = getOtherPolicyActionsSameDevice();

		int count = 0;

		// it self
		if (getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActions) {
			if (policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public int getActionFalseCountForDevice() {
		List<PolicyActionGraphicWrapper> otherPolicyActions = getOtherPolicyActionsSameDevice();

		int count = 0;

		// it self
		if (!getRuleResult()) {
			count++;
		}

		for (PolicyActionGraphicWrapper policyAction : otherPolicyActions) {
			if (!policyAction.getRuleResult()) {
				count++;
			}
		}

		return count;
	}

	@Override
	public String getRuleLastFiredDevice() {
		PolicyActionGraphicWrapper currentPolicyAction = getCurrentPolicyActionSameRule();

		if (currentPolicyAction != null) {
			if (currentPolicyAction.getRuleResult()) {
				return currentPolicyAction.getDevice();
			} else {

				PolicyActionGraphicWrapper policyAction = currentPolicyAction.getPreviousPolicyActionSameRule();

				do {

					if (policyAction != null) {
						if (policyAction.getRuleResult()) {
							return policyAction.getDevice();
						}

						policyAction = policyAction.getPreviousPolicyActionSameRule();
					}
				} while (policyAction != null);
			}
		}

		return "none";
	}

	@Override
	public String getActionLastExecutedDevice() {
		PolicyActionGraphicWrapper currentPolicyActionSameAction = getCurrentPolicyActionSameAction();

		if (currentPolicyActionSameAction != null) {
			if (currentPolicyActionSameAction.getRuleResult()) {
				return currentPolicyActionSameAction.getDevice();
			} else {

				PolicyActionGraphicWrapper policyAction = currentPolicyActionSameAction
						.getPreviousPolicyActionSameAction();

				do {

					if (policyAction != null) {
						if (policyAction.getRuleResult()) {
							return policyAction.getDevice();
						}

						policyAction = policyAction.getPreviousPolicyActionSameAction();
					}
				} while (policyAction != null);
			}
		}

		return "none";
	}

	@Override
	public RuleStatusEnum getRuleStatus() {
		PolicyActionGraphicWrapper previousPolicyAction = getPreviousPolicyActionSameRule();

		if (previousPolicyAction != null) {
			return getRuleStatus(getRuleResult(), previousPolicyAction.getRuleResult());
		}
		return null;
	}

	@Override
	public RuleStatusEnum getRuleStatusForDevice() {
		PolicyActionGraphicWrapper previousPolicyAction = getPreviousPolicyActionSameRuleAndDevice();

		if (previousPolicyAction != null) {
			return getRuleStatus(getRuleResult(), previousPolicyAction.getRuleResult());
		}
		return null;
	}

	private RuleStatusEnum getRuleStatus(boolean policyActionResult, boolean previousPolicyActionResult) {
		if (policyActionResult == previousPolicyActionResult) {
			return policyActionResult ? RuleStatusEnum.SAME_TRUE : RuleStatusEnum.SAME_FALSE;
		} else {
			return policyActionResult ? RuleStatusEnum.CHANGED_TRUE : RuleStatusEnum.CHANGED_FALSE;
		}
	}

	@Override
	public PolicyActionGraphicWrapper getPreviousPolicyActionSameRule() {
		return getPreviousPolicyAction(getOtherPolicyActionsSameRule(), getTimeStamp(), getTimeStampFraction());
	}

	protected PolicyActionGraphicWrapper getPreviousPolicyActionSameRuleAndDevice() {
		return getPreviousPolicyAction(getOtherPolicyActionsSameRuleAndDevice(), getTimeStamp(), getTimeStampFraction());
	}

	protected PolicyActionGraphicWrapper getCurrentPolicyActionSameRule() {
		List<PolicyActionGraphicWrapper> otherPolicyAction = getOtherPolicyActionsSameRule();
		// if this the current policy-action must be add it self
		otherPolicyAction.add(this);

		PolicyActionGraphicWrapper currentPolicyAction = getCurrentPolicyAction(otherPolicyAction, 0, 0);

		if (currentPolicyAction == null) {
			currentPolicyAction = this;
		}

		return currentPolicyAction;
	}

	protected PolicyActionGraphicWrapper getCurrentPolicyActionSameRuleAndDevice() {
		List<PolicyActionGraphicWrapper> otherPolicyAction = getOtherPolicyActionsSameRuleAndDevice();
		// if this the current policy-action must be add it self
		otherPolicyAction.add(this);

		PolicyActionGraphicWrapper currentPolicyAction = getCurrentPolicyAction(otherPolicyAction, 0, 0);

		if (currentPolicyAction == null) {
			currentPolicyAction = this;
		}

		return currentPolicyAction;
	}

	private PolicyActionGraphicWrapper getPreviousPolicyAction(List<PolicyActionGraphicWrapper> policyActions,
			long timeStamp, double timeStampFraction) {
		PolicyActionGraphicWrapper previous = null;
		for (PolicyActionGraphicWrapper policyAction : policyActions) {
			if (timeStamp > policyAction.getTimeStamp()) {
				previous = getPreviousPolicyAction(previous, policyAction);
			} else if (timeStamp == policyAction.getTimeStamp()) {
				if (timeStampFraction > policyAction.getTimeStampFraction()) {
					previous = getPreviousPolicyAction(previous, policyAction);
				}
			}
		}

		return previous;
	}

	private PolicyActionGraphicWrapper getPreviousPolicyAction(PolicyActionGraphicWrapper previous,
			PolicyActionGraphicWrapper policyAction) {
		if (previous == null || previous.getTimeStamp() > policyAction.getTimeStamp()) {
			return policyAction;
		} else if (previous.getTimeStamp() == policyAction.getTimeStamp()) {
			if (previous.getTimeStampFraction() > policyAction.getTimeStampFraction()) {
				return policyAction;
			}
		}
		return previous;
	}

	private PolicyActionGraphicWrapper getCurrentPolicyAction(List<PolicyActionGraphicWrapper> policyActions,
			long timeStamp, double timeStampFraction) {
		PolicyActionGraphicWrapper next = null;
		for (PolicyActionGraphicWrapper policyAction : policyActions) {
			if (timeStamp < policyAction.getTimeStamp()) {
				next = getNextPolicyAction(next, policyAction);
			} else if (timeStamp == policyAction.getTimeStamp()) {
				if (timeStampFraction < policyAction.getTimeStampFraction()) {
					next = getNextPolicyAction(next, policyAction);
				}
			}
		}

		return next;
	}

	private PolicyActionGraphicWrapper getNextPolicyAction(PolicyActionGraphicWrapper next,
			PolicyActionGraphicWrapper policyAction) {
		if (next == null || next.getTimeStamp() < policyAction.getTimeStamp()) {
			return policyAction;
		} else if (next.getTimeStamp() == policyAction.getTimeStamp()) {
			if (next.getTimeStampFraction() < policyAction.getTimeStampFraction()) {
				return policyAction;
			}
		}
		return next;
	}

	@Override
	public boolean isCurrentRuleResultForDevice() {
		PolicyActionGraphicWrapper nextPolicyAction = getCurrentPolicyActionSameRuleAndDevice();

		if (equals(nextPolicyAction)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isCurrentRuleResult() {
		PolicyActionGraphicWrapper nextPolicyAction = getCurrentPolicyActionSameRule();

		if (equals(nextPolicyAction)) {
			return true;
		}

		return false;
	}

	@Override
	public PolicyActionGraphicWrapper getCurrentPolicyActionSameRule(String device) {
		List<PolicyActionGraphicWrapper> policyActionsSameRule = getPolicyActionsSameRule();
		List<PolicyActionGraphicWrapper> policyActionsSameRuleAndDevice = new ArrayList<PolicyActionGraphicWrapper>();

		for (PolicyActionGraphicWrapper policyAction : policyActionsSameRule) {
			if (device.equals(policyAction.getDevice())) {
				policyActionsSameRuleAndDevice.add(policyAction);
			}
		}

		return getCurrentPolicyAction(policyActionsSameRuleAndDevice, 0, 0);
	}

	@Override
	public PolicyActionGraphicWrapper getPreviousPolicyActionSameAction() {
		return getPreviousPolicyAction(getOtherPolicyActions(), getTimeStamp(), getTimeStampFraction());
	}

	protected PolicyActionGraphicWrapper getCurrentPolicyActionSameAction() {
		PolicyActionGraphicWrapper currentPolicyAction = getCurrentPolicyAction(getOtherPolicyActions(), 0, 0);

		if (currentPolicyAction == null) {
			currentPolicyAction = this;
		}

		return currentPolicyAction;
	}

	protected Element getRuleResultElement() {
		return (Element) super.getRootElement().getFirstChild().getNextSibling();
	}

	protected List<Element> getConditionElements() {
		Element conditonElement = (Element) getRuleResultElement().getNextSibling();
		List<Element> conditionElements = new ArrayList<Element>();

		while (isConditionElement(conditonElement)) {
			conditionElements.add(conditonElement);

			conditonElement = (Element) conditonElement.getNextSibling();
		}

		return conditionElements;
	}

	protected List<PolicyActionGraphicWrapper> getAllPolicyActions() {
		List<PolicyActionGraphicWrapper> allPolicyActions = new ArrayList<PolicyActionGraphicWrapper>();

		IdentityGraphicWrapper policy = getRootPolicyIdentifier();

		if (policy != null) {
			Set<GraphicWrapper> allPolicyElements = PolicyNode.getAllChilds(policy);

			for (GraphicWrapper policyElement : allPolicyElements) {
				if (policyElement instanceof PolicyActionGraphicWrapper) {
					allPolicyActions.add((PolicyActionGraphicWrapper) policyElement);
				}
			}
		}
		return allPolicyActions;
	}

	protected IdentityGraphicWrapper getRootPolicyIdentifier() {
		Set<GraphicWrapper> parentsFromSelection = PolicyNode.getAllParents(this);

		for (GraphicWrapper parentElement : parentsFromSelection) {
			if (parentElement instanceof IdentityGraphicWrapper) {
				IdentityGraphicWrapper identity = (IdentityGraphicWrapper) parentElement;
				String nodeType = identity.getExtendetNodeTypeName();
				if (ELEMENT_POLICY_KEY.equals(nodeType)) {
					return identity;
				}
			}
		}
		return null;
	}

	protected IdentityGraphicWrapper getRuleNode() {
		List<GraphicWrapper> edgesNodes = super.getEdgesNodes();

		if (edgesNodes != null) {
			for (GraphicWrapper edgeNode : edgesNodes) {
				if (!(edgeNode instanceof IdentityGraphicWrapper)) {
					continue;
				}

				IdentityGraphicWrapper identityNode = (IdentityGraphicWrapper) edgeNode;

				if (!ELEMENT_TYPE_NAME_ACTION.equals(identityNode.getExtendetNodeTypeName())) {
					continue;
				}

				// action identity only

				List<GraphicWrapper> identifierEdgesNodes = identityNode.getEdgesNodes();
				for (GraphicWrapper metadataNode : identifierEdgesNodes) {
					if (!ELEMENT_TYPE_NAME_HAS_ELEMET.equals(metadataNode.getNodeTypeName())) {
						continue;
					}

					// has-element metadata only

					List<GraphicWrapper> metadataNodeEdgesNodes = metadataNode.getEdgesNodes();
					for (GraphicWrapper identifierNode : metadataNodeEdgesNodes) {
						if (!(identifierNode instanceof IdentityGraphicWrapper)) {
							continue;
						}

						// identity identifier only
						IdentityGraphicWrapper extendetIdentityNode = (IdentityGraphicWrapper) identifierNode;

						if (ELEMENT_TYPE_NAME_RULE.equals(extendetIdentityNode.getExtendetNodeTypeName())) {
							// rule identifier only
							return extendetIdentityNode;
						}
					}
				}
			}
		}

		return null;
	}

	protected List<PolicyActionGraphicWrapper> getAllPolicyActionsFromRule() {
		List<PolicyActionGraphicWrapper> allPolicyActions = new ArrayList<PolicyActionGraphicWrapper>();

		IdentityGraphicWrapper ruleNode = getRuleNode();
		List<GraphicWrapper> ruleNodes = ruleNode.getEdgesNodes();

		for (GraphicWrapper hasElement : ruleNodes) {
			List<GraphicWrapper> identifierNodes = hasElement.getEdgesNodes();

			for (GraphicWrapper identifier : identifierNodes) {
				if (!(identifier instanceof IdentityGraphicWrapper)) {
					continue;
				}

				// Identity identifier only
				IdentityGraphicWrapper identityNode = (IdentityGraphicWrapper) identifier;

				if (!ELEMENT_TYPE_NAME_ACTION.equals(identityNode.getExtendetNodeTypeName())) {
					continue;
				}

				// action identity only
				List<GraphicWrapper> identityNodeEdges = identityNode.getEdgesNodes();

				for (GraphicWrapper identityNodeEdge : identityNodeEdges) {
					if (identityNodeEdge instanceof PolicyActionGraphicWrapper) {
						allPolicyActions.add((PolicyActionGraphicWrapper) identityNodeEdge);
					}
				}
			}
		}

		return allPolicyActions;
	}

	protected static boolean isConditionElement(Element conditionElement) {
		String nodeName = conditionElement.getNodeName();

		if (VALIDE_CONDITION_ELEMENT_NODE_NAMES.contains(nodeName)) {
			return true;
		} else {
			return false;
		}
	}

	protected static boolean isFeatureElement(Element conditionElement) {
		String nodeName = conditionElement.getNodeName();

		if (VALIDE_FEATURE_ELEMENT_NODE_NAME.equals(nodeName)) {
			return true;
		} else {
			return false;
		}
	}

	private int getActionCountForRule() {
		Set<GraphicWrapper> policyElements = PolicyNode.getAllParents(this);

		for (GraphicWrapper policyElement : policyElements) {
			if (policyElement instanceof IdentityGraphicWrapper) {
				IdentityGraphicWrapper identityElement = (IdentityGraphicWrapper) policyElement;
				if ("rule".equals(identityElement.getExtendetNodeTypeName())) {
					Set<GraphicWrapper> childs = PolicyNode.getChilds(identityElement);
					int count = 0;
					for (GraphicWrapper child : childs) {
						if (child instanceof IdentityGraphicWrapper) {
							IdentityGraphicWrapper tmpIdentityElement = (IdentityGraphicWrapper) child;
							String typeName = tmpIdentityElement.getExtendetNodeTypeName();
							if ("action".equals(typeName)) {
								count++;
							}
						}
					}
					return count;
				}
			}
		}

		return -1;
	}

	public enum RuleStatusEnum {
		CHANGED_TRUE, CHANGED_FALSE, SAME_TRUE, SAME_FALSE;
	}
}
