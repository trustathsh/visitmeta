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
 * This file is part of visitmeta-dataservice, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;

import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.dataservice.util.SimpleNamespaceContext;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphFilter;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class GraphFilterImpl implements GraphFilter {

	private Identifier mStartIdentifier;
	private int mMaxDepth;
	private boolean mMatchMetaEverything;
	private boolean mMatchMetaNothing;
	private boolean mMatchLinkEverything;
	private boolean mMatchLinkNothing;
	private XPathExpression mResultFilter;
	private XPathExpression mMatchLinks;

	private static XPathFactory xpathFactory;

	private static final Logger log = Logger.getLogger(GraphFilterImpl.class);

	public GraphFilterImpl(Identifier startIdentifier, String resultFilter,
			String matchLinks, int maxDepth, Map<String, String> prefixMap) {
		mStartIdentifier = startIdentifier;
		mMaxDepth = maxDepth;
		mMatchMetaNothing = resultFilter != null
				&& resultFilter.length() == 0;
		mMatchMetaEverything = resultFilter == null;

		mMatchLinkEverything = matchLinks != null
				&& matchLinks.length() == 0;
		mMatchLinkNothing = matchLinks == null;

		xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		xpath.setNamespaceContext(new SimpleNamespaceContext(prefixMap));

		try {
			if (resultFilter != null
					&& resultFilter.length() > 0) {
				mResultFilter = xpath.compile(adaptFilterString(resultFilter));
			}
		} catch (XPathExpressionException e1) {
			log.error("Error at compiling result filter: "
					+ e1.getLocalizedMessage());
		}

		try {
			if (matchLinks != null
					&& matchLinks.length() > 0) {
				mMatchLinks = xpath.compile(adaptFilterString(matchLinks));
			}
		} catch (XPathExpressionException e1) {
			log.error("Error at compiling match-links filter: "
					+ e1.getLocalizedMessage());
		}
	}

	@Override
	public Identifier getStartIdentifier() {
		return mStartIdentifier;
	}

	@Override
	public int getMaxDepth() {
		return mMaxDepth;
	}

	@Override
	public boolean matchMeta(Document meta) {
		if (mMatchMetaEverything) {
			return true;
		}
		if (mMatchMetaNothing) {
			return false;
		}
		if (mResultFilter == null) {
			return false;
		}

		Object ret = null;
		try {
			ret = mResultFilter.evaluate(meta, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			return false;
		}
		return ((Boolean) ret).booleanValue();
	}

	@Override
	public boolean matchLink(Document meta) {
		if (mMatchLinkNothing) {
			return false;
		}
		if (mMatchLinkEverything) {
			return true;
		}
		if (mMatchLinks == null) {
			return false;
		}

		Object ret = null;
		try {
			ret = mMatchLinks.evaluate(meta, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			return false;
		}
		return ((Boolean) ret).booleanValue();
	}

	@Override
	public boolean matchEverything() {
		return mMatchMetaEverything;
	}

	@Override
	public boolean matchNothing() {
		return mMatchMetaNothing;
	}

	private String adaptFilterString(String fs) {
		String first = fs.replaceAll("^\\[", "*[");
		return first.replace(" [", " *[");
	}

}
