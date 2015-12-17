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
package de.hshannover.f4.trust.visitmeta.graphDrawer.policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyHierarchy {

	private static Map<String, PolicyHierarchy> policyHierarchy = new HashMap<String, PolicyHierarchy>();

	static {

		PolicyHierarchy policyAction = new PolicyHierarchy("policy-action");
		policyAction.childs = Collections.emptyList();

		PolicyHierarchy action = new PolicyHierarchy("action");
		action.childs.add(policyAction);

		PolicyHierarchy policyFeature = new PolicyHierarchy("policy-feature");
		policyFeature.childs = Collections.emptyList();

		PolicyHierarchy hint = new PolicyHierarchy("hint");
		hint.childs.add(policyFeature);

		PolicyHierarchy anomaly = new PolicyHierarchy("anomaly");
		anomaly.childs.add(hint);

		PolicyHierarchy signature = new PolicyHierarchy("signature");
		signature.childs.add(policyFeature);

		PolicyHierarchy condition = new PolicyHierarchy("condition");
		condition.childs.add(signature);
		condition.childs.add(anomaly);

		PolicyHierarchy rule = new PolicyHierarchy("rule");
		rule.childs.add(condition);
		rule.childs.add(action);

		PolicyHierarchy policy = new PolicyHierarchy("policy");
		policy.childs.add(rule);
		
		policyHierarchy.put(policy.type, policy);
		policyHierarchy.put(rule.type, rule);
		policyHierarchy.put(condition.type, condition);
		policyHierarchy.put(signature.type, signature);
		policyHierarchy.put(anomaly.type, anomaly);
		policyHierarchy.put(hint.type, hint);
		policyHierarchy.put(policyFeature.type, policyFeature);
		policyHierarchy.put(action.type, action);
		policyHierarchy.put(policyAction.type, policyAction);
	}

	private PolicyHierarchy(String type) {
		childs = new ArrayList<PolicyHierarchy>();
		this.type = type;
	}

	private String type;

	private List<PolicyHierarchy> childs;

	public static boolean isChild(String parent, String childToCheck) {
		return isChild(policyHierarchy.get(parent), childToCheck);
	}

	private static boolean isChild(PolicyHierarchy parent, String childToCheck) {
		if (parent == null || childToCheck == null) {
			return false;
		}

		for (PolicyHierarchy child : parent.childs) {
			if (child.type.equals(childToCheck)) {
				return true;
			}
		}

		for (PolicyHierarchy child : parent.childs) {
			return isChild(child, childToCheck);
		}

		return false;
	}

	public static boolean isParent(String child, String parentToCheck) {
		return isChild(parentToCheck, child);
	}

}
