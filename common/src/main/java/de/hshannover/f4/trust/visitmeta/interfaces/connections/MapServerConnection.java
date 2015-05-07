package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import de.hshannover.f4.trust.ifmapj.channel.SSRC;
import de.hshannover.f4.trust.ifmapj.messages.PollResult;
import de.hshannover.f4.trust.ifmapj.messages.SubscribeRequest;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;

/**
 * A interface for Connections within the dataservice.
 *
 * @author Bastian Hellmann
 * @author Marcel Reichenbach
 */
public interface MapServerConnection extends Connection, MapServerConnectionData {

	/**
	 * Start a subscription.
	 *
	 * @param subscriptionName the subscription name.
	 * 
	 * @throws ConnectionException
	 */
	public void startSubscription(String subscriptionName) throws ConnectionException;

	/**
	 * Stop a subscription.
	 *
	 * @param subscriptionName the subscription name.
	 * 
	 * @throws ConnectionException
	 */
	public void stopSubscription(String subscriptionName) throws ConnectionException;

	/**
	 * Stop all subscriptions.
	 *
	 * @throws ConnectionException
	 */
	public void stopAllSubscriptions() throws ConnectionException;

	/**
	 * Send a {@link SubscribeRequest} to the MAP server.
	 *
	 * @param subscription
	 * 
	 * @throws ConnectionException
	 */
	public void subscribe(Subscription subscription, boolean update) throws ConnectionException;

	/**
	 * Returns the {@link GraphService} associated with this {@link MapServerConnection}.
	 *
	 * @return a {@link GraphService} instance
	 */
	public GraphService getGraphService();
	
	/**
	 * Returns the publisher id of the {@link SSRC} associated with this {@link MapServerConnection}.
	 *
	 * @return The publisher id of the {@link SSRC}
	 * 
	 * @throws ConnectionException
	 */
	public String getPublisherId() throws ConnectionException;

	/**
	 * Returns the session id of the {@link SSRC} associated with this {@link MapServerConnection}.
	 *
	 * @return The session id of the SSRC
	 * 
	 * @throws ConnectionException
	 */
	public String getSessionId() throws ConnectionException;
	
	/**
	 * Sends a poll-request over the ARC and waits for the {@link PollResult}.
	 *
	 * @return PollResult the ifmapj {@link PollResult} to the poll-call
	 * 
	 * @throws ConnectionException
	 */
	public PollResult poll() throws ConnectionException;
}
