package de.fhhannover.inform.trust.visitmeta.persistence;

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
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of VisITMeta, version 0.0.2, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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


import java.util.List;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalLink;
import de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.fhhannover.inform.trust.visitmeta.ifmap.PollResult;
import de.fhhannover.inform.trust.visitmeta.ifmap.ResultItem;

public abstract class AbstractWriter implements Writer {
	protected Repository mRepo;
	protected Long mLastTimestamp;
	private static final Logger log = Logger.getLogger(AbstractWriter.class);

	@Override
	public void submitPollResult(PollResult pr) {
		beginTransaction();
		for (ResultItem update : pr.getUpdates()) {
			if (update.getId1() == null)
				submitUpdate(update.getId2(), update.getMetadata());
			if (update.getId2() == null)
				submitUpdate(update.getId1(), update.getMetadata());
			else
				submitUpdate(update.getId1(), update.getId2(), update.getMetadata());
		}
		for (ResultItem delete : pr.getDeletes()) {
			if (delete.getId1() == null)
				submitDelete(delete.getId2(), delete.getMetadata());
			if (delete.getId2() == null)
				submitDelete(delete.getId1(), delete.getMetadata());
			else
				submitDelete(delete.getId1(), delete.getId2(), delete.getMetadata());
		}
		finishTransaction();
	}

	public abstract void finishTransaction();
	public abstract void beginTransaction();

	protected void submitUpdate(InternalIdentifier id, List<InternalMetadata> meta) {
		log.debug("Got update for a single identifier");
		InternalIdentifier in = mRepo.findIdentifier(id);
		if (in == null) {
			in = mRepo.insert(id);
			for (InternalMetadata m : meta) {
				in.addMetadata(m);
			}
		} else {
			for (InternalMetadata m : meta) {
				if (m.isSingleValue()) {
					if (!in.hasMetadata(m)) {
						in.addMetadata(m);
					} else {
						in.removeMetadata(m);
						in.addMetadata(m);
					}
				}
				in.addMetadata(m);
			}
		}
		log.trace("Added update for a single identifier");

	}

	protected void submitUpdate(InternalIdentifier id1, InternalIdentifier id2, List<InternalMetadata> meta) {
		log.debug("Persistance got update for two identifiers");
		InternalIdentifier idGraph1 = mRepo.findIdentifier(id1);
		InternalIdentifier idGraph2 = mRepo.findIdentifier(id2);
		InternalLink l;

		if (idGraph1 != null && idGraph2 != null) {
			// both identifiers are in the graph, this means we either have to
			// update the metadata
			// on the link between them or establish a new link between them
			l = mRepo.findCommonLink(idGraph1, idGraph2);
			if (l == null) {
				l = mRepo.connect(idGraph1, idGraph2);
			}
			for (InternalMetadata m : meta) {
				if (m.isSingleValue()) {
					if (!l.hasMetadata(m)) {
						l.addMetadata(m);
					} else {
						l.removeMetadata(m);
						l.addMetadata(m);
					}
				} else {
					l.addMetadata(m);
				}
			}
		} else if (idGraph1 != null && idGraph2 == null) {
			// add link to idGraph1 with metadata
			log.trace("Found one identifier in the graph");
			idGraph2 = mRepo.insert(id2);
			l = mRepo.connect(idGraph1, idGraph2);
			for (InternalMetadata m : meta) {
				l.addMetadata(m);
			}
		} else if (idGraph1 == null && idGraph2 != null) {
			// add link to idGraph2 with metadata
			log.trace("Found one identifier in the graph");
			idGraph1 = mRepo.insert(id1);
			l = mRepo.connect(idGraph1, idGraph2);
			for (InternalMetadata m : meta) {
				l.addMetadata(m);
			}
		} else {
			// both links are not in the graph
			idGraph1 = mRepo.insert(id1);
			idGraph2 = mRepo.insert(id2);
			l = mRepo.connect(idGraph1, idGraph2);
			for (InternalMetadata m : meta)
				l.addMetadata(m);
		}
		log.trace("Added update for two identifiers");
	}

	protected void submitDelete(InternalIdentifier id1, InternalIdentifier id2, List<InternalMetadata> meta) {
		log.debug("Persistance got delete for two identifiers");
		InternalIdentifier idGraph1 = mRepo.findIdentifier(id1);
		InternalIdentifier idGraph2 = null;
		InternalLink linkToEdit = null;
		for (InternalLink l : idGraph1.getLinks()) {
			InternalLink il = (InternalLink) l;
			if (il.getIdentifiers().getFirst().equals(id2)
					|| il.getIdentifiers().getSecond().equals(id2)) {
				linkToEdit = il;
			}
		}

		if (linkToEdit != null) {
			for (InternalMetadata m : meta)
				linkToEdit.removeMetadata(m);
			if (linkToEdit.getMetadata().size() == 0) {
				log.trace("Deleting link "+linkToEdit);
				idGraph1 = (InternalIdentifier) linkToEdit.getIdentifiers()
						.getFirst();
				idGraph2 = (InternalIdentifier) linkToEdit.getIdentifiers()
						.getSecond();
				mRepo.disconnect(idGraph1, idGraph2);
			}
		} else {
			throw new RuntimeException(
					"Someone is trying to delete from a link that is not in the graph or "
							+ "not connected to the given identifiers. Bad Boy!");
		}
		log.trace("Performed delete for two identifiers");

	}

	protected void  submitDelete(InternalIdentifier id, List<InternalMetadata> meta) {
		log.debug("Got delete for single identifier");
		InternalIdentifier idGraph = mRepo.findIdentifier(id);
		if (idGraph != null) {
			for (InternalMetadata m : meta)
				idGraph.removeMetadata(m);
		} else {
			throw new RuntimeException(
					"Someone is trying to delete from an identifier that is not "
							+ "in the graph. Bad Boy!");
		}
		log.trace("Performed delete for a single identifier");
	}
}
