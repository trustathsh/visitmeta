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
 * This file is part of visitmeta-visualization, version 0.3.0,
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
package de.hshannover.f4.trust.visitmeta.util;

import javax.xml.namespace.NamespaceContext;

import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

/**
 * Wrapper-interface for common IF-MAP {@link Identifier} information.
 * 
 * @author Bastian Hellmann
 *
 */
public interface IdentifierWrapper {

	/**
	 * Evaluate the given XPath expression on this {@link Identifier} document and return
	 * the result string. If this {@link Identifier} object does not match the given
	 * expression the empty string is returned. If the evaluation of the
	 * expression fails null is returned.
	 *
	 * @param xPathExpression the XPath expression
	 * @return the result string, null or the empty string
	 */
	public String getValueForXpathExpression(String xPathExpression);

	/**
	 * Evaluate the given XPath expression on this {@link Identifier} document and return
	 * the result string. If the evaluation fails the given default value will
	 * be returned.
	 *
	 * @param xPathExpression the XPath expression
	 * @param defaultValue the default value
	 * @return the result string or the default value
	 */
	public String getValueForXpathExpressionOrElse(String xPathExpression,
			String defaultValue);

	/**
	 * Returns a formatted XML string representation of this {@link Identifier} document.
	 *
	 * @return formatted XML string
	 */
	public String toFormattedString();

	/**
	 * Set the given {@link NamespaceContext} as the namespace context for
	 * XPath queries.
	 *
	 * @param context the new namespace context
	 */
	public void setNamespaceContext(NamespaceContext context);

	/**
	 * Returns the typename for the {@link Identifier}.
	 * 
	 * @return typename of the {@link Identifier} as a {@link String}.
	 */
	public String getTypeName();

}
