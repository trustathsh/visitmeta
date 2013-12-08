package de.fhhannover.inform.trust.visitmeta.ifmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import de.fhhannover.inform.trust.ifmapj.messages.SubscribeRequest;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.ConnectionException;
import de.fhhannover.inform.trust.visitmeta.ifmap.exception.NoSavedConnectionException;
import de.fhhannover.inform.trust.visitmeta.interfaces.GraphService;

public class ConnectionManager {


	private static Logger log = Logger.getLogger(ConnectionManager.class);

	private static Map<String, Connection> conPool = new HashMap<String,Connection>();


	public static Connection newConnection(String name, String url, String user, String userPass, String truststore, String truststorePass){
		log.trace("new connection " + name + "...");

		Connection newConnection = new Connection(name, url, user, userPass, truststore, truststorePass);
		conPool.put(name, newConnection);

		log.info("connection " + name + " is saved");
		return newConnection;
	}

	/**
	 * Delete a a saved connection.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void removeConnection(String name) throws ConnectionException{
		//		log.trace("remove connection " + name + "...");

		//		if(!getConnection(name).isConnected()){
		//			conPool.remove(name);
		//		}
		//TODO läuft der updateThread noch ? Dumping??

		log.info(" TODO ");

		//		log.info("connection " + name + " were deleted");
	}

	/**
	 * Connect the dataservice to a MAP-Server.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void connectTo(String name) throws ConnectionException {
		log.trace("connection to " + name + " ...");

		getConnection(name).connect();

		log.info("connected to " + name);
	}

	/**
	 * Close a connection to the MAP-Server.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void disconnectFrom(String name) throws ConnectionException {
		log.trace("disconnection from " + name + " ...");

		getConnection(name).disconnect();

		log.info("disconnected from " + name);
	}

	/**
	 * @return All saved connections.
	 */
	// TODO aktive connections abfragen
	public static String getSavedConnections(){

		StringBuilder sb = new StringBuilder();

		for(Connection c: conPool.values()){

			sb.append(c).append("\n");
		}

		log.trace("get active connections ...\n" + sb.toString());

		return sb.toString();
	}

	/**
	 * Start a Dumping-Service.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void startDumpingServiceFromConnection(String name) throws ConnectionException {
		log.trace("start dumping from connection " + name + " ...");

		getConnection(name).startDumpingService();

		log.info("dumping for connection " + name + " started");
	}

	/**
	 * Stopped a active Dumping-Service.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void stopDumpingServiceFromConnection(String name) throws ConnectionException {
		log.trace("stop dumping from connection " + name + " ...");

		getConnection(name).stopDumpingService();

		log.info("dumping for connection " + name + " stopped");
	}

	/**
	 * @return A Set<String> with the active subscriptions.
	 * @throws ConnectionException
	 */
	public static Set<String> getActiveSubscriptionsFromConnection(String name) throws ConnectionException {
		return getConnection(name).getActiveSubscriptions();
	}

	/**
	 * Send a SubscribeRequest to the map server.
	 * 
	 * @param name Is the connection name.
	 * @param request Is the SubscribeRequest.
	 * @throws ConnectionException
	 */
	public static void subscribeFromConnection(String name, SubscribeRequest request) throws ConnectionException {
		log.trace("new subscribe for Connection " + name + " ...");

		getConnection(name).subscribe(request);

		log.info("subscribe received");
	}

	/**
	 * Delete a subscription with his subscriptionName.
	 * 
	 * @param name Is the connection name.
	 * @param subscriptionName Is the name of the Subscription.
	 * @throws ConnectionException
	 */
	public static void deleteSubscribeFromConnection(String name, String subscriptionName) throws ConnectionException {
		log.trace("delete subscription from connection " + name + "[Name="+ subscriptionName + "] ...");

		getConnection(name).deleteSubscribe(subscriptionName);

		log.info("subscription(" + name + "[Name=" + subscriptionName + "]" + ") were deleted");
	}

	/**
	 * Delete all active subscriptions.
	 * 
	 * @param name Is the connection name.
	 * @throws ConnectionException
	 */
	public static void deleteSubscriptionsFromConnection(String name) throws ConnectionException {
		log.trace("delete all subscriptions from connection " + name + " ...");

		getConnection(name).deleteSubscriptions();

		log.info("all subscriptions were deleted");
	}

	private static Connection getConnection(String connectionName) throws NoSavedConnectionException{

		if(conPool.containsKey(connectionName)){

			return conPool.get(connectionName);
		}

		NoSavedConnectionException e = new NoSavedConnectionException();
		log.error(e.getClass().getSimpleName());
		throw e;
	}

	/**
	 * 
	 * 
	 * @param name Is the connection name.
	 * @return A SimpleGraphService from the Neo4J-Database.
	 * @throws ConnectionException
	 */
	public static GraphService getGraphServiceFromConnection(String name) throws ConnectionException {
		return getConnection(name).getGraphService();
	}
}
