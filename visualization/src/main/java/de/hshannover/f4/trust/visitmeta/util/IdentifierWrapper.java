package de.hshannover.f4.trust.visitmeta.util;

import javax.xml.namespace.NamespaceContext;

public interface IdentifierWrapper {

	public String getValueForXpathExpression(String xPathExpression);

	public String getValueForXpathExpressionOrElse(String xPathExpression,
			String defaultValue);

	public String toFormattedString();

	public void setNamespaceContext(NamespaceContext context);

}
