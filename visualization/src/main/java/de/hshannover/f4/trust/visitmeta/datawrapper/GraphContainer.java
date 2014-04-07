package de.hshannover.f4.trust.visitmeta.datawrapper;

import de.hshannover.f4.trust.visitmeta.graphCalculator.Calculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FacadeLogic;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator;
import de.hshannover.f4.trust.visitmeta.graphCalculator.FactoryCalculator.CalculatorType;
import de.hshannover.f4.trust.visitmeta.gui.GraphConnection;
import de.hshannover.f4.trust.visitmeta.gui.util.RestConnection;
import de.hshannover.f4.trust.visitmeta.network.Connection;
import de.hshannover.f4.trust.visitmeta.network.FacadeNetwork;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection;
import de.hshannover.f4.trust.visitmeta.network.FactoryConnection.ConnectionType;

/**
 * 
 * @author oelsner
 * 
 */
public class GraphContainer {
	private String mName = null;
	private Connection mConnection = null;
	private Calculator mCalculator = null;
	private RestConnection mRestConnection = null;
	private FacadeNetwork mFacadeNetwork = null;
	private FacadeLogic mFacadeLogic = null;
	private GraphConnection mGraphConnection = null;
	private TimeHolder mTimeHolder = null;
	private TimeSelector mTimeSelector = null;
	private TimeManagerCreation mTimeManagerCreation = null;
	private TimeManagerDeletion mTimeManagerDeletion = null;
	private SettingManager mSettingManager = null;

	public GraphContainer(String name, RestConnection restConnection) {
		mName = name;
		mTimeHolder = new TimeHolder(this);
		mTimeSelector = new TimeSelector(this);
		mSettingManager = new SettingManager(this);
		mTimeManagerCreation = new TimeManagerCreation(this);
		mTimeManagerDeletion = new TimeManagerDeletion(this);

		mRestConnection = restConnection;

		mConnection = FactoryConnection.getConnection(ConnectionType.REST, this);
		mCalculator = FactoryCalculator.getCalculator(CalculatorType.JUNG);
		mFacadeNetwork = new FacadeNetwork(this);
		mFacadeLogic = new FacadeLogic(this);
		mGraphConnection = new GraphConnection(this);

		new Thread(mFacadeNetwork).start();
		new Thread(mFacadeLogic).start();
		new Thread(mTimeManagerCreation).start();
		new Thread(mTimeManagerDeletion).start();
	}

	public TimeHolder getTimeHolder() {
		return mTimeHolder;
	}

	public TimeSelector getTimeSelector() {
		return mTimeSelector;
	}

	public TimeManagerCreation getTimeManagerCreation() {
		return mTimeManagerCreation;
	}

	public TimeManagerDeletion getTimeManagerDeletion() {
		return mTimeManagerDeletion;
	}

	public SettingManager getSettingManager() {
		return mSettingManager;
	}

	public String getName() {
		return mName;
	}

	public GraphConnection getGraphConnection() {
		return mGraphConnection;
	}

	public FacadeNetwork getFacadeNetwork() {
		return mFacadeNetwork;
	}

	public Calculator getCalculator() {
		return mCalculator;
	}

	public FacadeLogic getFacadeLogic() {
		return mFacadeLogic;
	}

	public Connection getConnection() {
		return mConnection;
	}

	public RestConnection getRestConnection() {
		return mRestConnection;
	}

	@Override
	public int hashCode() {
		return mName.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof GraphContainer))
			return false;
		return mName.equals((((GraphContainer) o).mName));
	}

}