package de.hshannover.f4.trust.visitmeta.interfaces.connections;


public interface DataserviceConnection extends Connection, DataserviceConnectionData {

	@Override
	public DataserviceConnection copy();

	@Override
	public DataserviceConnection clone();

}
