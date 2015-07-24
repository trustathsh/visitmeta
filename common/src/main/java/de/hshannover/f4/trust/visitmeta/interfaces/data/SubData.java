package de.hshannover.f4.trust.visitmeta.interfaces.data;

public interface SubData {

	public Data getSubDataAt(int index);

	public int getSubDataCount();

	public int getIndexOfSubData(SubData connection);
}
