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

import java.util.List;

import org.w3c.dom.Element;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.GraphicWrapper;
import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.piccolo2d.Piccolo2DPolicyActionGraphicWrapper.RuleStatusEnum;
import de.hshannover.f4.trust.visitmeta.graphDrawer.policy.ConditionElement;

public interface PolicyActionGraphicWrapper extends GraphicWrapper {

	/**
	 * Returns all other policy-action metadata ({@link PolicyActionGraphicWrapper}) contains of the same action
	 * identifier of this policy-action.
	 * 
	 * @return all other policy-action metadata List<{@link PolicyActionGraphicWrapper}>
	 */
	public List<PolicyActionGraphicWrapper> getOtherPolicyActions();

	public List<PolicyActionGraphicWrapper> getPolicyActionsSameRule();

	public List<PolicyActionGraphicWrapper> getPolicyActionsSameRuleAndDevice();

	public String getRuleId();

	public boolean getRuleResult();

	public String getDevice();

	public List<Element> getFeatureElements();

	public List<ConditionElement> getConditionElementResults();

	public RuleStatusEnum getRuleStatus();

	public int getRuleTrueCount();

	public int getRuleFalseCount();

	public String getRuleLastFiredDevice();

	public RuleStatusEnum getRuleStatusForDevice();

	public int getRuleTrueCountForDevice();

	public int getRuleFalseCountForDevice();

	public List<String> getDevicesSameRule();

	public List<String> getDevicesSameAction();

	public int getActionTrueCount();

	public int getActionFalseCount();

	public String getActionLastExecutedDevice();

	public int getActionTrueCountForDevice();

	public int getActionFalseCountForDevice();

	public List<PolicyActionGraphicWrapper> getOtherPolicyActionsSameRule();

	public List<PolicyActionGraphicWrapper> getOtherPolicyActionsSameDevice();

	public List<PolicyActionGraphicWrapper> getOtherPolicyActionsSameRuleAndDevice();

	public boolean isCurrentRuleResultForDevice();

	public PolicyActionGraphicWrapper getCurrentPolicyActionSameRule(String device);

	public PolicyActionGraphicWrapper getPreviousPolicyActionSameRule();

	public PolicyActionGraphicWrapper getPreviousPolicyActionSameAction();

	public boolean isCurrentRuleResult();

}
