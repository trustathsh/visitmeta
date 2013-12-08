package de.fhhannover.inform.trust.visitmeta.dataservice.rest;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;

import de.fhhannover.inform.trust.visitmeta.dataservice.Application;
import de.fhhannover.inform.trust.visitmeta.dataservice.util.ConfigParameter;

public class RestService implements Runnable {


	private static final Logger log = Logger.getLogger(RestService.class);

	private final String url  = Application.getDSConfig().getProperty(ConfigParameter.DS_REST_URL);

	private final Map<String, String> params = new HashMap<String, String>();


	@Override
	public void run() {
		log.debug("run() ...");

		params.put("com.sun.jersey.config.property.packages", "de.fhhannover.inform.trust.visitmeta.dataservice.rest");

		log.info("starting REST service on "+url+"...");

		try {

			HttpServer server = GrizzlyWebContainerFactory.create(url, params);
			log.debug("REST service running.");
			// TODO shutdown server properly

		} catch (Exception e) {

			throw new RuntimeException(e);
		}

		try {

			synchronized (this){

				wait();
			}

		} catch (InterruptedException e) {

			log.error(e.getMessage(), e);
		}

		log.debug("... run()");
	}

}
