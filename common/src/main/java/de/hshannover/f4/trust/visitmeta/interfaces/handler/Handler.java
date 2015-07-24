package de.hshannover.f4.trust.visitmeta.interfaces.handler;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public interface Handler<T extends Data> {

	public Class<T> handle();
}
