package de.hshannover.f4.trust.visitmeta.data;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;
import de.hshannover.f4.trust.visitmeta.interfaces.data.SubData;

public abstract class DataImpl implements Data {

	private String mName;

	@Override
	public Data getSubDataAt(int index) {
		List<Data> tmp = getSubData();
		if (tmp != null) {
			return tmp.get(index);
		}
		return null;
	}

	@Override
	public int getSubDataCount() {
		List<Data> tmp = getSubData();
		if (tmp != null) {
			return tmp.size();
		}
		return 0;
	}

	@Override
	public int getIndexOfSubData(SubData channel) {
		List<Data> tmp = getSubData();
		if (tmp != null) {
			return tmp.indexOf(channel);
		}
		return -1;
	}

	@Override
	public Data clone() {
		Data tmp = copy();
		tmp.setName(tmp.getName() + "(clone)");
		return tmp;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public void setName(String name) {
		mName = name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
