package de.fhhannover.inform.trust.visitmeta.ifmap.exception;

public class ConnectionException extends Exception {


	private static final long serialVersionUID = 685270918564136642L;


	@Override
	public String toString() {
		return "Error: " + this.getClass().getSimpleName();
	}
}
