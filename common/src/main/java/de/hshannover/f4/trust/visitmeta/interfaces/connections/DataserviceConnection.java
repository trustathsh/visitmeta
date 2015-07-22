package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import de.hshannover.f4.trust.visitmeta.interfaces.data.DataserviceData;


public interface DataserviceConnection extends Connection, DataserviceData {

	@Override
	public DataserviceConnection copy();

	@Override
	public DataserviceConnection clone();

}
