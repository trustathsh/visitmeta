package de.hshannover.f4.trust.visitmeta.gui.contextmenu;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;
import de.hshannover.f4.trust.visitmeta.interfaces.Propable;
import de.hshannover.f4.trust.visitmeta.network.otherservices.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.network.otherservices.IronDetectConnection;

public class RunWhatIfAnalysisOnPolicyActionContextMenuItem implements ContextMenuItem {

	private Logger logger = Logger.getLogger(RunWhatIfAnalysisOnPolicyActionContextMenuItem.class);
	
	private static final String POLICY_ACTION_METADATA = "policy-action";
	private IronDetectConnection mIronDetectConnection;
	private DataserviceConnection mDataserviceConnection;

	public RunWhatIfAnalysisOnPolicyActionContextMenuItem() {
		mIronDetectConnection = new IronDetectConnection();
		mDataserviceConnection = new DataserviceConnection();
	}
	
	@Override
	public void actionPerformed(Propable propable) {
		Metadata metadata = (Metadata) propable;
		Long timestamp = metadata.getPublishTimestamp();
		logger.debug("Timestamp: " + timestamp);
		
		String json = mDataserviceConnection.getGraphAt(timestamp);
		logger.debug("Json result: " + json);
		
		String response = mIronDetectConnection.send(json);
		logger.debug(response);
	}

	@Override
	public String getItemTitle() {
		return "Run What-If Analysis";
	}

	@Override
	public boolean canHandle(Propable node) {
		if (node instanceof Metadata && node.getTypeName().equals(POLICY_ACTION_METADATA)) {
			return true;
		}
		
		return false;
	}

}
