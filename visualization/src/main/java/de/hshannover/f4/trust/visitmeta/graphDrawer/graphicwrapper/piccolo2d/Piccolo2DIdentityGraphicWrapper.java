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

import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.hshannover.f4.trust.visitmeta.graphDrawer.graphicwrapper.IdentityGraphicWrapper;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.util.DocumentUtils;

public class Piccolo2DIdentityGraphicWrapper extends Piccolo2DGraphicWrapper implements IdentityGraphicWrapper {

	private static final String ELEMENT_ID_NAME = "name";

	public Piccolo2DIdentityGraphicWrapper(PPath node, PText text) {
		super(node, text);
	}

	/**
	 * For extendet-Identity don't deEscapeXml().
	 */
	@Override
	protected Document getDocument() {
		Object data = getData();

		if (data instanceof Propable) {
			Propable propable = (Propable) data;

			return DocumentUtils.parseXmlString(propable.getRawData());
		}
		return null;
	}

	@Override
	public String getName() {
		Element rootElement = super.getRootElement();

		String name = rootElement.getAttribute(ELEMENT_ID_NAME);

		return name;
	}

	@Override
	public String getExtendetNodeTypeName() {
		String nameValue = getName();
		
		if (nameValue == null) {
			return null;
		}

		Document extendetIdentifier = DocumentUtils.parseEscapedXmlString(nameValue);

		if (extendetIdentifier != null) {
			Element rootElement = extendetIdentifier.getDocumentElement();
			return rootElement.getNodeName();
		}
		return null;
	}
}
