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
 * This file is part of visitmeta visualization, version 0.0.7,
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
package de.hshannover.f4.trust.visitmeta.datawrapper;





import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

/**
 * A container class holding a metadata entity together with the corresponding
 * identifier or link (depending on metatdata type).
 * Necessary for unique identification of metadata.
 */
public class RichMetadata {

	private static final Logger LOGGER = Logger.getLogger(RichMetadata.class);

	/*
	 * mMetadata and either mIdentifier or mLink must be set (depending on metadata type).
	 * Setting mMetadata alone without mIdentifier or mLink is only provided for compatibility.
	 */
	private Metadata mMetadata;
	private Identifier mIdentifier;
	private Link mLink;

	/**
	 * Deprecated; set either identifier or link (depending on metatdata type).
	 */
	@Deprecated
	public RichMetadata(Metadata metadata) {
		mMetadata = metadata;
		mIdentifier = null;
		mLink = null;
	}

	public RichMetadata(Metadata metadata, Identifier identifier) {
		mMetadata = metadata;
		mIdentifier = identifier;
		mLink = null;
	}

	public RichMetadata(Metadata metadata, Link link) {
		mMetadata = metadata;
		mIdentifier = null;
		mLink = link;
	}

	public Metadata getMetadata() {
		return mMetadata;
	}

	public Identifier getIdentifier() {
		return mIdentifier;
	}

	public Link getLink() {
		return mLink;
	}

	@Override
	public boolean equals(Object o) {
		LOGGER.trace("Method equals(" + o + ") called for this = " + this + ".");
		if (o == null) {
			return false;
		}
		if (! (o instanceof RichMetadata)) {
			return false;
		}
		RichMetadata other = (RichMetadata) o;
		if (! mMetadata.equals(other.mMetadata)) {
			return false;
		}
		if (mIdentifier != null) {
			if (other.mIdentifier == null) {
				return false;
			}
			if (! mIdentifier.equals(other.mIdentifier)) {
				return false;
			}
		}
		else {
			if (other.mIdentifier != null) {
				return false;
			}
		}
		if (mLink != null) {
			if (other.mLink == null) {
				return false;
			}
			if (! mLink.equals(other.mLink)) {
				return false;
			}
		}
		else {
			if (other.mLink != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		LOGGER.trace("Method hashCode() called.");
		final int prime = 31;
		int result = 1;
		result = prime * result + mMetadata.hashCode();
		result = prime * result + (mIdentifier == null ? 0 : mIdentifier.hashCode());
		result = prime * result + (mLink == null ? 0 : mLink.hashCode());
		return result;
	}

	@Override
	public String toString(){
		LOGGER.trace("Method toString() called.");
		String result = "";
		if (mMetadata != null) {
			result += "metadata = " + mMetadata.hashCode();
		}
		if (mIdentifier != null) {
			result += ", identifier = " + mIdentifier.hashCode();
		}
		if (mLink != null) {
			result += ", link = " + mLink.hashCode();
		}
		return result;
	}

}
