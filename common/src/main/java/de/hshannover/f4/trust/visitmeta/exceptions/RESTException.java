package de.hshannover.f4.trust.visitmeta.exceptions;

public class RESTException extends Exception {

	private static final long serialVersionUID = 7640152275830235264L;

	public RESTException(String msg) {
		super(msg);
	}

	@Override
	public String toString() {
		return super.getClass().getSimpleName() + " [Message: " + super.getMessage() + "]";
	}
}
