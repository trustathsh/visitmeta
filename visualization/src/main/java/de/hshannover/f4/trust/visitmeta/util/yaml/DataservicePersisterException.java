package de.hshannover.f4.trust.visitmeta.util.yaml;

public class DataservicePersisterException extends Exception {

	private static final long serialVersionUID = 1514493635404925141L;

	private String mMsg;

	public DataservicePersisterException(){

	}

	public DataservicePersisterException(String msg){
		mMsg = msg;
	}

	@Override
	public String toString() {
		if(mMsg != null){
			return this.getClass().getSimpleName() + "(" + mMsg + ")";
		}else {
			return this.getClass().getSimpleName();
		}
	}

	@Override
	public String getMessage(){
		return mMsg;
	}

}
