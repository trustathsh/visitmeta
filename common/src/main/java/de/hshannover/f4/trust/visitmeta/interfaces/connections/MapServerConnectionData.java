package de.hshannover.f4.trust.visitmeta.interfaces.connections;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;
import de.hshannover.f4.trust.visitmeta.interfaces.SubscriptionData;
import de.hshannover.f4.trust.visitmeta.interfaces.data.Data;

public interface MapServerConnectionData extends Data {

	public String getConnectionName();

	public void setConnectionName(String connectionName);

	public String getUrl();

	public void setUrl(String url);

	public String getUserName();

	public void setUserName(String userName);

	public String getUserPassword();

	public void setUserPassword(String userPassword);

	public String getTruststorePath();

	public void setTruststorePath(String truststorePath);

	public String getTruststorePassword();

	public void setTruststorePassword(String truststorePass);

	/**
	 * Adds a subscription to the internal list of subscriptions for this {@link MapServerConnectionData}.
	 *
	 * @param subscription a subscription
	 */
	public void addSubscription(Subscription connection);

	/**
	 * Delete a subscription.
	 *
	 * @param subscriptionName the subscription name.
	 */
	public void deleteSubscription(String subscriptionName);

	/**
	 * Delete all subscriptions.
	 */
	public void deleteAllSubscriptions();

	/**
	 * Returns all active subscriptions.
	 *
	 * @return a {@link List} with all active subscriptions.
	 */
	public List<Data> getActiveSubscriptions();

	/**
	 * Returns a {@link List} of {@link Subscription} representing all subscriptions for this
	 * {@link MapServerConnectionData}.
	 *
	 * @return all subscriptions as a {@link List} of {@link Subscription}.
	 */
	public List<Data> getSubscriptions();

	public void setSubscriptionData(List<Data> connection);

	public int getMaxPollResultSize();

	public void setMaxPollResultSize(int maxPollResultSize);

	public void setConnected(boolean connected);

	public boolean isConnected();

	/**
	 * Set the flag for connecting on startup for this {@link MapServerConnectionData}.
	 *
	 * @param startupConnect connect on startup boolean
	 */
	public void setStartupConnect(boolean startupConnect);

	/**
	 * Returns if the flag for connecting on startup is set for this {@link MapServerConnectionData}.
	 *
	 * @return true, if this {@link MapServerConnectionData} shall connect on startup
	 */
	public boolean doesConnectOnStartup();

	public void setAuthenticationBasic(boolean authenticationBasic);

	public boolean isAuthenticationBasic();

	/**
	 * Only the connection properties. For Subscriptions use updateSubscription().
	 * 
	 * @param newData
	 */
	public void changeData(MapServerConnectionData newData);

	public void updateSubscription(SubscriptionData newData);
}
