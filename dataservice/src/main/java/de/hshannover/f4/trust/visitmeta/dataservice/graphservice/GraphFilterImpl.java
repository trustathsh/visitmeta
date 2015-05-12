package de.hshannover.f4.trust.visitmeta.dataservice.graphservice;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import de.hshannover.f4.trust.visitmeta.interfaces.GraphFilter;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;

public class GraphFilterImpl implements GraphFilter {

	private Identifier mStartIdentifier;
	private int mMaxDepth;
	private boolean mMatchEverything;
	private boolean mMatchNothing;
	private XPathExpression mResultFilter;
	private XPathExpression mMatchLinks;

	private static XPathFactory xpathFactory;

	public GraphFilterImpl(Identifier startIdentifier, String resultFilter,
			String matchLinks, int maxDepth) {
		mStartIdentifier = startIdentifier;
		mMaxDepth = maxDepth;
		mMatchNothing = resultFilter != null && resultFilter.length() == 0;
		mMatchEverything = resultFilter == null;

		xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		// tests validity of the filterstring
		try {
			if (resultFilter != null && resultFilter.length() > 0) {
				mResultFilter = xpath.compile(resultFilter);
			}
		} catch (XPathExpressionException e1) {
			//todo log failed filterstring stuff
		}
		
		try {
			mMatchLinks = xpath.compile(matchLinks);
		} catch (XPathExpressionException e1) {

		}
	}

	@Override
	public Identifier getStartIdentifier() {
		return mStartIdentifier;
	}

	@Override
	public int getMaxDepth() {
		return mMaxDepth;
	}

	@Override
	public boolean matchMeta(Document meta) {
		if (mMatchEverything) {
			return true;
		}
		if (mMatchNothing) {
			return false;
		}

		Object ret = null;
		try {
			ret = mResultFilter.evaluate(meta, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			return false;
		}
		return ((Boolean) ret).booleanValue();
	}

	@Override
	public boolean matchLink(Document meta) {
		Object ret = null;
		try {
			ret = mMatchLinks.evaluate(meta, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			return false;
		}
		return ((Boolean) ret).booleanValue();
	}

	@Override
	public boolean matchEverything() {
		return mMatchEverything;
	}

	@Override
	public boolean matchNothing() {
		return mMatchNothing;
	}

}
