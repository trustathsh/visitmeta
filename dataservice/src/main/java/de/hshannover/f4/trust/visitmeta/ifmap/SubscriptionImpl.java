package de.hshannover.f4.trust.visitmeta.ifmap;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;

public class SubscriptionImpl implements Subscription {

	public String name;
	public String identifier;
	public String identifierType;
	public String filterLinks;
	public String filterResult;
	public String terminalIdentifierTypes;
	public boolean startupSubscribe;
	public int maxDepth;
	public int maxSize;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getIdentifierType() {
		return identifierType;
	}

	@Override
	public void setIdentifierType(String identifierType) {
		this.identifierType = identifierType;
	}

	@Override
	public String getFilterLinks() {
		return filterLinks;
	}

	@Override
	public void setFilterLinks(String filterLinks) {
		this.filterLinks = filterLinks;
	}

	@Override
	public String getFilterResult() {
		return filterResult;
	}

	@Override
	public void setFilterResult(String filterResult) {
		this.filterResult = filterResult;
	}

	@Override
	public String getTerminalIdentifierTypes() {
		return terminalIdentifierTypes;
	}

	@Override
	public void setTerminalIdentifierTypes(String terminalIdentifierTypes) {
		this.terminalIdentifierTypes = terminalIdentifierTypes;
	}

	@Override
	public boolean isStartupSubscribe() {
		return startupSubscribe;
	}

	@Override
	public void setStartupSubscribe(boolean startupSubscribe) {
		this.startupSubscribe = startupSubscribe;
	}

	@Override
	public int getMaxDepth() {
		return maxDepth;
	}

	@Override
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	@Override
	public int getMaxSize() {
		return maxSize;
	}

	@Override
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

}
