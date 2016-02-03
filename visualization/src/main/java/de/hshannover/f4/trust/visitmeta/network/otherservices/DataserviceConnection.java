package de.hshannover.f4.trust.visitmeta.network.otherservices;

import javax.ws.rs.core.MediaType;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class DataserviceConnection extends RestConnection {
	
	Properties config = Main.getConfig();
	
	public String getGraphAt(Long timestamp) {
		return buildWebResource(config.getString(VisualizationConfig.KEY_CONNECTION_DATASERVICE_URL,
				VisualizationConfig.DEFAULT_VALUE_CONNECTION_DATASERVICE_URL))
				.path("localMAPServer").path("graph").path(timestamp.toString())
				.queryParam("rawData", "true")
				.accept(MediaType.APPLICATION_JSON).get(String.class);
	}

}
