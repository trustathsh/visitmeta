package de.hshannover.f4.trust.visitmeta.gui.util;


public class RESTConnection {

	private String mName;
	private String mUrl;
	private boolean mDumping;


	public RESTConnection(String name, String url, boolean dumping){
		setName(name);
		setUrl(url);
		setDumping(dumping);
	}

	@Override
	public RESTConnection clone() {
		return new RESTConnection(mName, mUrl, mDumping);
	}

	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public String getUrl() {
		return mUrl;
	}
	public void setUrl(String endpoint) {
		mUrl = endpoint;
	}
	public boolean isDumping() {
		return mDumping;
	}
	public void setDumping(boolean dumping) {
		mDumping = dumping;
	}

	@Override
	public String toString(){
		return mName;
	}

	public void update(String name, String url, boolean dumping) {
		setName(name);
		setUrl(url);
		setDumping(dumping);
	}

}
