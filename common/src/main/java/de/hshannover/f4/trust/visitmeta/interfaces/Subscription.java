package de.hshannover.f4.trust.visitmeta.interfaces;

public interface Subscription {

	public String getName();

	public void setName(String name);

	public String getIdentifier();

	public void setIdentifier(String identifier);

	public String getIdentifierType();

	public void setIdentifierType(String identifierType);

	public String getFilterLinks();

	public void setFilterLinks(String filterLinks);

	public String getFilterResult();

	public void setFilterResult(String filterResult);

	public String getTerminalIdentifierTypes();

	public void setTerminalIdentifierTypes(String terminalIdentifierTypes);

	public boolean isStartupSubscribe();

	public void setStartupSubscribe(boolean startupSubscribe);

	public int getMaxDepth();

	public void setMaxDepth(int maxDepth);

	public int getMaxSize();

	public void setMaxSize(int maxSize);

}