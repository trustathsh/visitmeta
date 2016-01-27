package de.hshannover.f4.trust.visitmeta.network.otherservices;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.UniformInterfaceException;

import de.hshannover.f4.trust.ironcommon.properties.Properties;
import de.hshannover.f4.trust.ironcommon.properties.PropertyException;
import de.hshannover.f4.trust.visitmeta.Main;
import de.hshannover.f4.trust.visitmeta.util.VisualizationConfig;

public class IronDetectConnection extends RestConnection {

	Properties config = Main.getConfig();
	
	public String send(String json) throws UniformInterfaceException, PropertyException {
		return buildWebResource(config.getString(VisualizationConfig.KEY_CONNECTION_IRONDETECT_URL,
					VisualizationConfig.DEFAULT_VALUE_CONNECTION_IRONDETECT_URL)).path("livechecker")
				.accept(MediaType.APPLICATION_JSON)
				.entity(json, MediaType.APPLICATION_JSON_TYPE)
				.put(String.class);
	}
	
}
