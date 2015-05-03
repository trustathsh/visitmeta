package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;

/**
 * A interface for Connections.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public interface Connection {

	/**
	 * Connects this {@link Connection} to a MAP server.
	 *
	 * @throws ConnectionException
	 */
	public void connect() throws ConnectionException;

	/**
	 * Disconnects the connection to a MAP server.
	 *
	 * @throws ConnectionException
	 */
	public void disconnect() throws ConnectionException;

	/**
	 * Returns the name of this {@link Connection}.
	 *
	 * @return name of this {@link Connection}
	 */
	public String getConnectionName();

	/**
	 * Returns if this {@link Connection} is connected to a MAP server.
	 *
	 * @return true, when the connection to a MAP server is established and active.
	 */
	public boolean isConnected();

}
