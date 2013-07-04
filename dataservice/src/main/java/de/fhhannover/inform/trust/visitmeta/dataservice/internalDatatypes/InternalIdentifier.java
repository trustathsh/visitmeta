package de.fhhannover.inform.trust.visitmeta.dataservice.internalDatatypes;

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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
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

import java.util.Collections;
import java.util.List;

import de.fhhannover.inform.trust.visitmeta.interfaces.Identifier;
import de.fhhannover.inform.trust.visitmeta.interfaces.Propable;

/**
 * Internal representation of an IF-MAP identifier.
 */
public abstract class InternalIdentifier implements Propable{

	public abstract void addMetadata(InternalMetadata meta);

	/**
	 * Returns the raw, unparsed XML data used to describe this Identifier. <b>Note: The xml version
	 * and encoding is included.</b>
	 */
	public abstract String getRawData();

	/**
	 * Removes all Metadata connected to the identifier
	 */
	public abstract void clearMetadata();

	public abstract void removeMetadata(InternalMetadata meta);

	public abstract boolean hasMetadata(InternalMetadata meta);

	/**
	 * Removes all Links connected to the Identifier. <b>Note: The List containing the Links 
	 * is just cleared. The connected identifier is left unchanged.</b>
	 */
	public abstract void clearLinks();

	public abstract void addProperty(String name, String value);

	public abstract List<InternalLink> getLinks();

	public abstract List<InternalMetadata> getMetadata();

	@Deprecated
	public abstract void removeLink(InternalLink link);

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof InternalIdentifier)) {
			return false;
		}
		InternalIdentifier other = (InternalIdentifier) o;
		if (!getTypeName().equals(other.getTypeName()))
			return false;

		List<String> myProperties = getProperties();
		if (myProperties.size() != other.getProperties().size()) {
			return false;
		}
		for (String property : myProperties) {
			String myValue = valueFor(property);
			if (myValue == null) {
				if (!(other.valueFor(property) == null))
					return false;
			} else {
				if (!myValue.equals(other.valueFor(property)))
					return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + getTypeName().hashCode();
		List<String> keys = getProperties();
		Collections.sort(keys);
		for (String key : keys) {
			result = prime * result + valueFor(key).hashCode();
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer();
		tmp.append(getTypeName() + "[" + hashCode() + "] Properties[");
		for (String p : getProperties()) {
			tmp.append("(" + p + ", " + valueFor(p) + ")");
		}
		tmp.append("] Links[");
		int i = 0;
		for (InternalLink l : getLinks()) {
			tmp.append(l.hashCode());
			if (i != getLinks().size() - 1) {
				tmp.append(", ");
			}
			i++;
		}
		tmp.append("] Metadata[");
		i = 0;
		for (InternalMetadata m : getMetadata()) {
			tmp.append(m.hashCode());
			if (i != getMetadata().size() - 1) {
				tmp.append(", ");
			}
			i++;
		}
		tmp.append("]");
		return tmp.toString();
	}

	/**
	 * Checks if this identifier is valid at the given timestamp.
	 * An identifier is valid if it has any link or metadata which is valid.
	 * @param timestamp the timestamp to check
	 * @return the result wether it is valid or not
	 */
	public boolean isValidAt(long timestamp) {
		for (InternalMetadata m : getMetadata()) {
			if (((InternalMetadata) m).isValidAt(timestamp)) {
				return true;
			}
		}
		for (InternalLink l : getLinks()) {
			for (InternalMetadata m : l.getMetadata()) {
				if (((InternalMetadata) m).isValidAt(timestamp)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Performs a kind of equals check, but for Objects implementing the Identifier interface.
	 * An Identifier and an InternalIdentifier are equal if they have the same typename and 
	 * the same properties.
	 * @param identifier The Identifier to compare with
	 * @return Wether the identifier is "the same" as this object or not
	 */
	public boolean sameAs(Identifier identifier) {
		InternalIdentifier internal = this;
		if (!identifier.getTypeName().equals(internal.getTypeName())) {
			return false;
		}

		List<String> identifierProperties = identifier.getProperties();
		if (identifierProperties.size() != internal.getProperties().size()) {
			return false;
		}
		for (String property : identifierProperties) {
			String identifierValue = identifier.valueFor(property);
			if (identifierValue == null) {
				if (!(internal.valueFor(property) == null)) {
					return false;
				}
			} else {
				if (!identifierValue.equals(internal.valueFor(property))) {
					return false;
				}
			}
		}
		return true;
	}
}
