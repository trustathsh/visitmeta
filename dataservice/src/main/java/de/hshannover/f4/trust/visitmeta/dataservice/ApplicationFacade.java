package de.hshannover.f4.trust.visitmeta.dataservice;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Application facade for the REST layer.
 */
public class ApplicationFacade extends ResourceConfig {

	public ApplicationFacade( /* dependecies ... */ ) {
		packages("de.hshannover.f4.trust.visitmeta.dataservice.rest");
//		register( DEPENDENCY_OBJECT_INSTANCE_OR_CLASS )
		register(new FacadeBinder());
	}
}

class FacadeBinder extends AbstractBinder {

	@Override
	protected void configure() {
//		bind( DEPENDENCY_OBJECT_INSTANCE_OR_CLASS ).to( DEPENDENCY_CLASS )
	}
}
