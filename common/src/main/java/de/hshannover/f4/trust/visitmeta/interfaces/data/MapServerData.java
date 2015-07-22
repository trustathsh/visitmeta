package de.hshannover.f4.trust.visitmeta.interfaces.data;

import java.util.List;

import de.hshannover.f4.trust.visitmeta.interfaces.Subscription;

public interface MapServerData extends Data {

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
	 * Adds a {@link SubscriptionData} to the internal list of subscriptions-datas for this {@link MapServerData}.
	 *
	 * @param subscriptionData
	 */
	public void addSubscription(SubscriptionData subscriptionData);

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
	public List<SubscriptionData> getActiveSubscriptions();

	/**
	 * Returns a {@link List} of {@link Subscription} representing all subscriptions for this
	 * {@link MapServerData}.
	 *
	 * @return all subscriptions as a {@link List} of {@link Subscription}.
	 */
	public List<SubscriptionData> getSubscriptions();

	public void setSubscriptionData(List<SubscriptionData> connection);

	public int getMaxPollResultSize();

	public void setMaxPollResultSize(int maxPollResultSize);

	public void setConnected(boolean connected);

	public boolean isConnected();

	/**
	 * Set the flag for connecting on startup for this {@link MapServerData}.
	 *
	 * @param startupConnect connect on startup boolean
	 */
	public void setStartupConnect(boolean startupConnect);

	/**
	 * Returns if the flag for connecting on startup is set for this {@link MapServerData}.
	 *
	 * @return true, if this {@link MapServerData} shall connect on startup
	 */
	public boolean doesConnectOnStartup();

	public void setAuthenticationBasic(boolean authenticationBasic);

	public boolean isAuthenticationBasic();

	/**
	 * Only the connection properties. For Subscriptions use updateSubscription().
	 * 
	 * @param newData
	 */
	public void changeData(MapServerData newData);

	public void updateSubscription(SubscriptionData newData);

	@Override
	public MapServerData copy();

	@Override
	public MapServerData clone();
}
