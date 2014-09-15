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
 * This file is part of visitmeta-dataservice, version 0.1.2,
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
package de.hshannover.f4.trust.visitmeta.persistence.inmemory;



import java.util.List;

import org.neo4j.graphdb.Transaction;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifier;
import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.AbstractWriter;
import de.hshannover.f4.trust.visitmeta.persistence.Repository;
@Deprecated
public class InMemoryWriter extends AbstractWriter {
	private Object mLock;

	public InMemoryWriter(Object lock, Repository repo) {
		mLock = lock;
		mRepo = repo;
	}

	@Override
	public void submitUpdate(InternalIdentifier id, List<InternalMetadata> meta) {
		synchronized (mLock) {
			super.submitUpdate(id, meta);
		}
	}


	@Override
	public void submitUpdate(InternalIdentifier id1, InternalIdentifier id2,
			List<InternalMetadata> meta) {
		synchronized (mLock) {
			super.submitUpdate(id1, id2, meta);
		}
	}

	@Override
	public void finishTransaction() {
		// TODO Auto-generated method stub
	}

	@Override
	public void beginTransaction() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void submitUpdate(List<InternalMetadata> meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void submitDelete(int n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Transaction beginSubmit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void finishSubmit(Transaction tx) {
		// TODO Auto-generated method stub
		
	}
}
