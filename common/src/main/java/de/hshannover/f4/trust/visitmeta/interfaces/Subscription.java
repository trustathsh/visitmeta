package de.hshannover.f4.trust.visitmeta.interfaces;

public interface Subscription {

	public String getName();

	public void setName(String name);

	public String getStartIdentifier();

	public void setStartIdentifier(String identifier);

	public String getIdentifierType();

	public void setIdentifierType(String identifierType);

	public String getMatchLinksFilter();

	public void setMatchLinksFilter(String filterLinks);

	public String getResultFilter();

	public void setResultFilter(String filterResult);

	public String getTerminalIdentifierTypes();

	public void setTerminalIdentifierTypes(String terminalIdentifierTypes);

	public boolean isStartupSubscribe();

	public void setStartupSubscribe(boolean startupSubscribe);

	public int getMaxDepth();

	public void setMaxDepth(int maxDepth);

	public int getMaxSize();

	public void setMaxSize(int maxSize);

}