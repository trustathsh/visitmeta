package de.hshannover.f4.trust.visitmeta.gui;

public class ConnectionListItem {
	private String mName;
	private ConnectionTab mConnectionTab;

	public ConnectionListItem(String pName, ConnectionTab pConnectionTab) {
		mName = pName;
		mConnectionTab = pConnectionTab;
	}

	public ConnectionTab getConnectionTab() {
		return this.mConnectionTab;
	}

	@Override
	public int hashCode() {
		return this.mName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof ConnectionListItem)) {
			return false;
		}
		ConnectionListItem other = (ConnectionListItem) o;
		return this.mName.equals(other.mName);
	}

	@Override
	public String toString() {
		return this.mName;
	}

	public String getName() {
		return this.mName;
	}
}
