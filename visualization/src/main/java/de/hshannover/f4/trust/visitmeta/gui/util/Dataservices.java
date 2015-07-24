package de.hshannover.f4.trust.visitmeta.gui.util;

import java.util.ArrayList;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.data.DataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.connections.DataserviceConnection;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

/**
 * Helper class for the {@link RESTConnectionModel}. It combines {@link DataserviceConnection}s.
 * 
 * @author Marcel Reichenbach
 *
 */
public class Dataservices extends DataImpl {

	List<Data> mList;

	@Override
	public String getName() {
		return Dataservices.class.getSimpleName();
	}

	@Override
	public List<Data> getSubData() {
		return new ArrayList<Data>(mList);
	}

	public void removeDataserviceConnection(DataserviceConnection dataservice){
		mList.remove(dataservice);
	}

	@Override
	public Data copy() {
		Dataservices tmpCopy = new Dataservices();
		tmpCopy.mList = getSubData();
		return tmpCopy;
	}

	@Override
	public Class<?> getDataTypeClass() {
		throw new UnsupportedOperationException();
	}

	public void addDataserviceConnection(DataserviceConnection newDataserviceConnection) {
		mList.add(newDataserviceConnection);
	}

}
