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
	public int getIndexOfSubData(SubData data) {
		List<Data> tmp = getSubData();
		if (tmp != null) {
			return tmp.indexOf(data);
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
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof Data) {
			if (((Data) o).getName() != null) {
				if (((Data) o).getName().equals(getName())) {
					return true;
				}
			} else if (getName() == null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}
}
