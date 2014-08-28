package de.hshannover.f4.trust.visitmeta.dataservice.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

import de.hshannover.f4.trust.visitmeta.dataservice.ApplicationFacade;

@Provider
public class ApplicationProvider extends SingletonTypeInjectableProvider<Context, ApplicationFacade> {

	public ApplicationProvider() {
		super(ApplicationFacade.class, new ApplicationFacade( /* dependecies ... */ ));
	}
}
