package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.data.DataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public class RestSubscription extends DataImpl {

	private boolean mStartupConnect;

	public RestSubscription(String name, boolean startupConnect) {
		setName(name);
		setStartupConnect(startupConnect);
	}

	public boolean isStartupConnect() {
		return mStartupConnect;
	}

	public void setStartupConnect(boolean startupConnect) {
		mStartupConnect = startupConnect;
	}

	@Override
	public List<Data> getSubData() {
		// Subscriptions do not have subchannels
		return null;
	}

	@Override
	public Data copy() {
		// TODO Auto-generated method stub
		return null;
	}

}
