package de.hshannover.f4.trust.visitmeta.dataservice.util;

import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

public class SimpleNamespaceContext implements NamespaceContext {
	
	private final Map<String, String> mNamespaceMap;
	
	public SimpleNamespaceContext(Map<String, String> nsMap) {
		mNamespaceMap = nsMap;
	}

	@Override
	public String getNamespaceURI(String prefix) {
		if (mNamespaceMap.containsKey(prefix)) {
			return mNamespaceMap.get(prefix);
		}
		return XMLConstants.NULL_NS_URI;
	}

	@Override
	public String getPrefix(String namespaceURI) {
		throw new RuntimeException("Not implemented!");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Iterator getPrefixes(String namespaceURI) {
		throw new RuntimeException("Not implemented!");
	}

}
