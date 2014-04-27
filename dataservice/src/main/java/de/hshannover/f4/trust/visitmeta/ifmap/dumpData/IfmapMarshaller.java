package de.hshannover.f4.trust.visitmeta.ifmap.dumpData;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * Website: http://trust.inform.fh-hannover.de/
 * 
 * This file is part of irongui, version 0.4.1, implemented by the Trust@FHH
 * research group at the Hochschule Hannover, a program to visualize the content
 * of a MAP Server (MAPS), a crucial component within the TNC architecture.
 * 
 * The development was started within the bachelor
 * thesis of Tobias Ruhe at Hochschule Hannover (University of
 * Applied Sciences and Arts Hannover). irongui is now maintained
 * and extended within the ESUKOM research project. More information
 * can be found at the Trust@FHH website.
 * %%
 * Copyright (C) 2010 - 2013 Trust@FHH
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


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.hshannover.f4.trust.ifmapj.identifier.Identifier;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.ResultItem;
import de.hshannover.f4.trust.ifmapj.messages.SearchResult;
import de.hshannover.f4.trust.visitmeta.ifmap.Connection;



public class IfmapMarshaller {

	public static PollResultContainer marshallPollResult(PollResult result)
			throws RemoteException, NoValidIdentifierTypeException,
			LinkConstructionException {
		if (result != null) {
			ArrayList<IfmapDataType> rNew = new ArrayList<IfmapDataType>();
			ArrayList<IfmapDataType> rUpdate = new ArrayList<IfmapDataType>();
			ArrayList<IfmapDataType> rDelete = new ArrayList<IfmapDataType>();
			Collection<SearchResult> pNew = result.getUpdateResults();
			Collection<SearchResult> pUpdate = result.getSearchResults();
			Collection<SearchResult> pDelete = result.getDeleteResults();

			ArrayList<IfmapDataType> pn = processSearchUpdateDeleteResult(pNew);

			if (pn != null && pn.size() > 0) {
				for (IfmapDataType arr : pn) {
					if (!rNew.contains(arr)) {
						rNew.add(arr);
					} else {
						IfmapDataType idt = rNew.get(rNew.indexOf(arr));
						ArrayList<Metadata> meta = arr.getMetadata();
						for (Metadata m : meta) {
							idt.addOrReplaceMetadata(m);
						}
					}
				}
			}

			ArrayList<IfmapDataType> pu = processSearchUpdateDeleteResult(pUpdate);
			if (pu != null && pu.size() > 0) {
				for (IfmapDataType arr : pu) {
					if (!rUpdate.contains(arr)) {
						rUpdate.add(arr);
					} else {
						IfmapDataType idt = rUpdate.get(rUpdate.indexOf(arr));
						ArrayList<Metadata> meta = arr.getMetadata();
						for (Metadata m : meta) {
							idt.addOrReplaceMetadata(m);
						}
					}
				}
			}

			ArrayList<IfmapDataType> pd = processSearchUpdateDeleteResult(pDelete);
			if (pd != null && pd.size() > 0) {
				for (IfmapDataType arr : pd) {
					if (!pDelete.contains(arr))
						rDelete.add(arr);
				}
			}

			return new PollResultContainer(rNew, rUpdate, rDelete);
		}

		return null;
	}

	public static PollResultContainer filterPollResult(Connection con,
			PollResultContainer prc) {
		ArrayList<IfmapDataType> deleteResult = new ArrayList<IfmapDataType>();
		ArrayList<IfmapDataType> del = prc.getDelete();
		for (int i = 0; i < del.size(); i++) {
			IfmapDataType delData = del.get(i);
			if (!delData.getMetadata().isEmpty()) {
				String uuid = delData.getSubscriptionName();
				IdentifierData data = SubscriptionRepository.getInstance()
						.getIdentifierDataBySubscriptionName(con, uuid);
				if (data != null) {
					IdentifierData id1 = null;
					IdentifierData id2 = null;
					if (delData instanceof Link) {
						Link link = (Link) delData;
						id1 = link.getIdentifier1();
						id2 = link.getIdentifier2();
					} else {
						id1 = (IdentifierData) delData;
					}
					if(con.isDumpingActive()) {
						if (data.equals(id1) || (id2 != null && data.equals(id2))) {
							deleteResult.add(delData);
						}
					}else {
						deleteResult.add(delData);
					}
				}
			}
		}

		return new PollResultContainer(prc.getNew(), prc.getUpdate(),
				deleteResult);
	}

	private static Metadata[] processMetadata(Collection<Document> metadata) {
		Metadata[] metac = null;
		if (metadata != null && !metadata.isEmpty()) {
			Iterator<Document> metaIt = metadata.iterator();
			ArrayList<Metadata> metaContainer = null;
			if (metaIt != null && metaIt.hasNext()) {
				metaContainer = new ArrayList<Metadata>();
				// iterate over each child of ifmap:metadata
				while (metaIt.hasNext()) {
					// for (int i = 0; i < ml.size(); i++) {
					Document document = metaIt.next();
					Element doc = document.getDocumentElement();
					// ElementNSImpl meta = (ElementNSImpl) ml.get(i);
					Metadata data = new Metadata(doc.getNodeName());
					data.setDocument(document);
					NamedNodeMap map = doc.getAttributes();
					// get operational attributes
					for (int n = 0; n < map.getLength(); n++) {
						Node node = map.item(n);
						String name = node.getNodeName();
						if (name.equalsIgnoreCase("ifmap-publisher-id")) {
							data.setPublisher(node.getNodeValue());
						} else if (name.equalsIgnoreCase("ifmap-timestamp")) {
							data.setTimestamp(node.getNodeValue());
						} else if (name.equalsIgnoreCase("ifmap-cardinality")) {
							data.setCardinality(node.getNodeValue());
						} else {
							data.addAttribute(name, node.getNodeValue());
						}
					}

					// now process children of metadate element
					NodeList nl = doc.getChildNodes();

					for (int j = 0; j < nl.getLength(); j++) {
						// process the next child
						processChild(data, nl.item(j));
					}
					// FIXME What if nl has TextContent only? we need to process
					// this too!
					metaContainer.add(data);
				}
				metac = new Metadata[metaContainer.size()];
				int x = 0;
				for (Metadata m : metaContainer) {
					metac[x++] = m;
				}
			}
		}
		return metac;
	}

	private static ArrayList<IfmapDataType> processResultItem(
			SearchResult search, ArrayList<IfmapDataType> list)
					throws NoValidIdentifierTypeException, LinkConstructionException {
		Collection<ResultItem> resultItems = search.getResultItems();
		if (resultItems != null && !resultItems.isEmpty()) {
			Iterator<ResultItem> iterator = resultItems.iterator();
			while (iterator.hasNext()) {
				ResultItem item = iterator.next();
				Collection<Document> metadata = item.getMetadata();
				if (metadata != null && !metadata.isEmpty()) {
					Identifier i1 = item.getIdentifier1();
					Identifier i2 = item.getIdentifier2();
					IdentifierData ident1 = null;
					IdentifierData ident2 = null;
					ident1 = new IdentifierData(i1);
					ident1.setSubscriptionName(search.getName());
					if (i2 != null) {
						ident2 = new IdentifierData(i2);
						ident2.setSubscriptionName(search.getName());
					}

					Metadata[] metaContainer = processMetadata(metadata);
					if (metaContainer != null) {
						if (ident2 != null) {
							for (Metadata m : metaContainer) {
								Link l = null;

								l = new Link(m.getName(), ident1, ident2);

								if (l != null) {
									l.addOrReplaceMetadata(m);
								}
								l.setSubscriptionName(search.getName());
								list.add(l);
							}
							list.add(ident2);
						} else {
							for (Metadata m : metaContainer) {
								ident1.addOrReplaceMetadata(m);
							}
						}
						list.add(ident1);
					}
				}
			}

		}
		return list;
	}

	private static ArrayList<IfmapDataType> processSearchUpdateDeleteResult(
			Collection<SearchResult> resultCollection)
					throws NoValidIdentifierTypeException, LinkConstructionException {
		ArrayList<IfmapDataType> list = null;
		if (resultCollection != null && !resultCollection.isEmpty()) {
			list = new ArrayList<IfmapDataType>();
			for (SearchResult search : resultCollection) {
				processResultItem(search, list);
			}
		}
		return list;
	}

	/**
	 * 
	 * @param data
	 *            object to save payload of metadata element
	 * @param n
	 *            the current child to be processed
	 */
	private static void processChild(Metadata data, Node n) {
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			String nodeName = n.getNodeName();
			String txtContent = n.getTextContent();
			MetadataEntry entry = new MetadataEntry(nodeName, txtContent);
			NamedNodeMap nnm = n.getAttributes();
			if (nnm != null && nnm.getLength() > 0) {
				for (int l = 0; l < nnm.getLength(); l++) {
					entry.addAttribute(nnm.item(l).getNodeName(), nnm.item(l)
							.getNodeValue());
				}
			}
			data.addChild(entry);
		}
	}

}
