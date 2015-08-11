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
 * This file is part of visitmeta-dataservice, version 0.5.1,
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



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import de.hshannover.f4.trust.visitmeta.dataservice.internalDatatypes.InternalIdentifierGraph;
import de.hshannover.f4.trust.visitmeta.dataservice.util.GraphHelper;

/**
 * @author rosso
 *
 */
public class SimpleGraphCache implements GraphCache {
	/**
	 * Maximum size (number of cached graphs) the cache is allowed to grow to. When the maximum
	 * size is reached, the least recently used entries are purged from the cache.
	 */
	private int mMaxCacheSize;
	/**
	 * The actual cache mapping timestamps to graph objects.
	 */
	private HashMap<Long, List<InternalIdentifierGraph>> mCache;
	/**
	 * Data structure to implement least recently used cache purging. Mapping the time of last
	 * access to graphs' timestamps.
	 */
	private TreeMap<Long, Long> mTouched;
	/**
	 * Helper data structure to glue together the cache and the touched graph. Mapping graphs'
	 * timestamps to the time of their last access.
	 */
	private HashMap<Long, Long> mCacheToTouched;

	private SimpleGraphCache() {
		mCache = new HashMap<>();
		mCacheToTouched = new HashMap<>();
		mTouched = new TreeMap<>();
	}

	public SimpleGraphCache(int maxCacheSize) {
		this();
		mMaxCacheSize = maxCacheSize;
	}

	@Override
	public boolean lookup(long timestamp) {
		return mCache.containsKey(timestamp);
	}


	@Override
	public synchronized List<InternalIdentifierGraph> fetch(long timestamp) {
		if (mCache.containsKey(timestamp)) {
			List<InternalIdentifierGraph> binky = new ArrayList<>();
			for (InternalIdentifierGraph graph : mCache.get(timestamp)) {
				binky.add(GraphHelper.cloneGraph(graph));
			}
			touch(timestamp);
			return binky;
		}
		return new ArrayList<InternalIdentifierGraph>();
	}

	@Override
	public synchronized void put(long timestamp, List<InternalIdentifierGraph> graph) {
		long now = System.currentTimeMillis();
		if (mCache.containsKey(timestamp)) {
				mCache.remove(timestamp);
		}
		if (mCacheToTouched.containsKey(timestamp)) {
			long oldnow = mCacheToTouched.get(timestamp);
			mCacheToTouched.remove(timestamp);
			if (mTouched.containsKey(oldnow)) {
				mTouched.remove(oldnow);
			}
		}
		mCache.put(timestamp, graph);
		mCacheToTouched.put(timestamp, now);
		mTouched.put(now, timestamp);

		if (mCache.size() > mMaxCacheSize) {
			long oldest = mTouched.firstKey();
			long oldestTimestamp = mTouched.get(oldest);
			mTouched.remove(oldest);
			mCache.remove(oldestTimestamp);
			mCacheToTouched.remove(oldestTimestamp);
		}
	}


	/**
	 * Helper method to update the recently used timestamp for the graph corresponding
	 * to the timestamp passed as argument.
	 * @param timestamp
	 */
	private void touch(long timestamp) {
		long now = System.currentTimeMillis();
		if (mCacheToTouched.containsKey(timestamp)) {
			long oldnow = mCacheToTouched.get(timestamp);
			mCacheToTouched.remove(timestamp);
			if (mTouched.containsKey(oldnow)) {
				mTouched.remove(oldnow);
			}
			mCacheToTouched.put(timestamp, now);
			mTouched.put(now, timestamp);
		}
	}

	@Override
	public void clearCache() {
		mCache.clear();
		mCacheToTouched.clear();
		mTouched.clear();
	}

}
