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
 * This file is part of visitmeta-common, version 0.4.2,
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
package de.hshannover.f4.trust.visitmeta.implementations.internal;






/**
 * Encapsulates two {@link InternalIdentifier}, since an {@link InternalLink} always carries two identifiers.
 * <i>Note: In the IF-MAP specification the identifiers have got no order.</i>
 * <i>Make sure getFirst() and getSecond() are idempotent!</i>
 */
public class InternalIdentifierPair {
	private InternalIdentifier mFirst;
	private InternalIdentifier mSecond;

	public InternalIdentifierPair(InternalIdentifier id1, InternalIdentifier id2) {
		mFirst = id1;
		mSecond = id2;
	}

	public InternalIdentifier getFirst() {
		return mFirst;
	}
	public InternalIdentifier getSecond() {
		return mSecond;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
		buffer.append("(");
		buffer.append(mFirst.toString());
		buffer.append(" ---- ");
		buffer.append(mSecond.toString());
		buffer.append(")");
		return buffer.toString();
	}

}
