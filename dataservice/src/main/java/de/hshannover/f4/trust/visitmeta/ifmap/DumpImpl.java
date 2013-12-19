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
 * This file is part of visitmeta dataservice, version 0.0.3,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@HsH
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
package de.hshannover.f4.trust.visitmeta.ifmap;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.DomHelpers;
import de.hshannover.f4.trust.ifmapj.exception.IfmapErrorResult;
import de.hshannover.f4.trust.ifmapj.exception.MarshalException;
import de.hshannover.f4.trust.ifmapj.exception.UnmarshalException;
import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.identifier.Identifiers;
import de.hshannover.f4.trust.ifmapj.messages.Request;
import de.hshannover.f4.trust.ifmapj.messages.RequestHandler;
import de.hshannover.f4.trust.ifmapj.messages.RequestImpl;
import de.hshannover.f4.trust.ifmapj.messages.Requests.Helpers;
import de.hshannover.f4.trust.ifmapj.messages.Result;

class DumpRequestHandler implements RequestHandler<DumpRequest> {
	
	DumpRequestHandler() { }
	
	public static final String DUMP_REQ_EL_NAME = "dump";
	public static final String DUMP_REQ_IDENT_FILTER = "identifier";
	public static final String DUMP_RES_EL_NAME = "dumpResult";
	public static final String DUMP_RES_LAST_UPDATE_ATTRIB = "last-update";


	@Override
	public Element toElement(Request req, Document doc) throws MarshalException {
		
		Helpers.checkRequestType(req, this);
		String identFilter = ((DumpRequest)req).getIdentifierFilter();
		
		Element ret = doc.createElementNS(Helpers.baseNsUri(),
				DomHelpers.makeRequestFQName(DUMP_REQ_EL_NAME));
		
		if (identFilter  != null)
			DomHelpers.addAttribute(ret, DUMP_REQ_IDENT_FILTER, identFilter);
		
		Helpers.addSessionId(ret, req);
		
		return ret;
	}

	@Override
	public Result fromElement(Element res) throws UnmarshalException,
			IfmapErrorResult {
		
		Element content = Helpers.getResponseContentErrorCheck(res);
		Attr lastUpdateNode = null;
		String lastUpdate = null;
		List<Element> children;
		List<Identifier> resIdents = new ArrayList<Identifier>();

		if (!DomHelpers.elementMatches(content, DUMP_RES_EL_NAME))
			throw new UnmarshalException("No dumpResult element found");
			

		lastUpdateNode = content.getAttributeNode(DUMP_RES_LAST_UPDATE_ATTRIB);
		
		if (lastUpdateNode == null)
			throw new UnmarshalException("No " + DUMP_RES_LAST_UPDATE_ATTRIB +
					" attribute in " + DUMP_RES_EL_NAME + " element found");
		
		lastUpdate = content.getAttribute(DUMP_RES_LAST_UPDATE_ATTRIB);
		
		children = DomHelpers.getChildElements(content);
		
		for (Element child : children)
			resIdents.add((Identifiers.fromElement(child)));
		
		return new DumpResultImpl(lastUpdate, resIdents);
	}

	@Override
	public Class<DumpRequest> handles() {
		return DumpRequest.class;
	}
}

class DumpRequestImpl extends RequestImpl implements DumpRequest {
	
	private String mIdentifierFilter;
	
	DumpRequestImpl(String identFilter) {
		mIdentifierFilter = identFilter;
	}

	DumpRequestImpl() {
		this(null);
	}

	@Override
	public void setIdentifierFilter(String filter) {
		mIdentifierFilter = filter;
	}

	@Override
	public String getIdentifierFilter() {
		return mIdentifierFilter;
	}
}

class DumpResultImpl implements DumpResult {
	
	private final String mLastUpdate;
	
	private final Collection<Identifier> mIdentifiers;
	
	DumpResultImpl(String lastUpdate, Collection<Identifier> identifiers) {
		
		if (identifiers == null)
			throw new NullPointerException("identifiers list is null");
		
		if (lastUpdate == null)
			throw new NullPointerException("lastUpdate is null");

		mLastUpdate = lastUpdate;
		mIdentifiers = identifiers;
	}

	@Override
	public String getLastUpdate() {
		return mLastUpdate;
	}

	@Override
	public Collection<Identifier> getIdentifiers() {
		return Collections.unmodifiableCollection(mIdentifiers);
	}
}
