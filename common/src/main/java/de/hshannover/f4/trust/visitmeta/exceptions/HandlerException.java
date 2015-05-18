package de.hshannover.f4.trust.visitmeta.exceptions;

public class HandlerException extends Exception {

	private static final long serialVersionUID = 9167871990868743406L;

	public HandlerException(String msg) {
		super(msg);
	}

	@Override
	public String toString() {
		return super.getClass().getSimpleName() + " [Message: " + super.getMessage() + "]";
	}
}
