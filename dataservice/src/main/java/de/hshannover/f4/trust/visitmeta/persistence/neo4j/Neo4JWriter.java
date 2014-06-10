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
 * This file is part of visitmeta dataservice, version 0.0.5,
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
/**
 *
 */
package de.hshannover.f4.trust.visitmeta.persistence.neo4j;



import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Transaction;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalMetadata;
import de.hshannover.f4.trust.visitmeta.persistence.AbstractWriter;

/**
 * Neo4J extension of AbstractWriter. Basically only adds transactions to the
 * methods of the superclass.
 * 
 * @author rosso
 * 
 */
public class Neo4JWriter extends AbstractWriter {
	private static Logger log = Logger.getLogger(Neo4JWriter.class);
	private Transaction mTransaction;
	private Lock mLock;
	private Neo4JConnection mConnection;
	private Long mLastTimestamp;

	public Neo4JWriter(Neo4JRepository graph, Neo4JConnection connection) {
		mRepo = graph;
		mLock = new ReentrantLock();
		mConnection = connection;
	}

	@Override
	public void finishTransaction() {
		log.trace("Finishing transaction ...");
		mTransaction.success();
		mTransaction.finish();
		mLock.unlock();
	}

	@Override
	public void beginTransaction() {
		mLock.lock();
		mLastTimestamp = mConnection.getTimestampManager().getCurrentTime();
		log.trace("Beginning transaction ...");
		mTransaction = ((Neo4JRepository) mRepo).beginTx();
	}

	@Override
	protected Transaction beginSubmit() {
		log.trace("Beginning transaction ...");
		Transaction tx = ((Neo4JRepository) mRepo).beginTx();
		mLock.lock();

		return tx;
	}

	@Override
	protected void finishSubmit(Transaction tx) {
		log.trace("Finishing transaction ...");
		tx.success();
		tx.finish();
		mLock.unlock();
	}

	@Override
	protected void submitUpdate(List<InternalMetadata> meta) {
		for (InternalMetadata m : meta) {
			mConnection.getTimestampManager().incrementCounter(
					m.getPublishTimestamp());
		}
	}

	@Override
	protected void submitDelete(int n) {
		for (int i = 0; i < n; i++) {
			mConnection.getTimestampManager().incrementCounter(mLastTimestamp);
		}
	}
}
