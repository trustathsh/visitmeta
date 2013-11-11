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

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.visitmeta.ifmap.PollResult;

public class ThreadedWriter implements Runnable, Writer {
	private Writer mWriter;
	private LinkedBlockingQueue<PollResult> mTasks;
	private Logger log = Logger.getLogger(ThreadedWriter.class);


	private ThreadedWriter() {
		mTasks = new LinkedBlockingQueue<>();
	}

	public ThreadedWriter(Writer w) {
		this();
		mWriter = w;
	}

	@Override
	public void submitPollResult(PollResult pr) {
		log.debug("Adding new PollResult to queue");
		mTasks.add(pr);
	}

	@Override
	public void run() {
		while (!isStopped()) {
			PollResult pr = null;
			try {
				pr = mTasks.take();
				log.debug("Processing PollResult ...");
				mWriter.submitPollResult(pr);
			} catch (InterruptedException e) {
				log.fatal("Writer thread got interrupted");
				e.printStackTrace();
			}
		}
	}

	protected boolean isStopped() {
		return Thread.currentThread().isInterrupted();
	}

}
