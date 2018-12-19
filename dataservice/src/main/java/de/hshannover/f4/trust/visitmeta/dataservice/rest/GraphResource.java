/*
 * #%L
 * =====================================================
 *   _____                _     ____  _   _       _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \| | | | ___ | | | |
 *    | | | '__| | | / __| __|/ / _` | |_| |/ __|| |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _  |\__ \|  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_| |_||___/|_| |_|
 *                             \____/
 * 
 * =====================================================
 * 
 * Hochschule Hannover
 * (University of Applied Sciences and Arts, Hannover)
 * Faculty IV, Dept. of Computer Science
 * Ricklinger Stadtweg 118, 30459 Hannover, Germany
 * 
 * Email: trust@f4-i.fh-hannover.de
 * Website: http://trust.f4.hs-hannover.de/
 * 
 * This file is part of visitmeta-dataservice, version 0.6.0,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2016 Trust@HsH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package de.hshannover.f4.trust.visitmeta.dataservice.rest;

import java.util.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.hshannover.f4.trust.visitmeta.dataservice.Application;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.GraphFilterImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.NamespaceHolder;
import de.hshannover.f4.trust.visitmeta.exceptions.ifmap.ConnectionException;
import de.hshannover.f4.trust.visitmeta.interfaces.Delta;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphFilter;
import de.hshannover.f4.trust.visitmeta.interfaces.GraphService;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

/**
 * Represents the graph of the application in a "RESTful" manner.
 * For each request a new object of this class will be created. The
 * resource is accessible under the path <tt>/graph</tt>.
 * All methods delegate the calculation to a {@link GraphService} implementation.
 *
 * Be aware that some of the methods don't behave in a clean RESTful
 * way. For example: /graph/42 returns the graph at timestamp 42, under
 * some circumstances a later call to /graph/42 may return a different
 * result than the first call.
 *
 * @author Ralf Steuerwald
 *
 */
@Path("{connectionName}/graph")
public class GraphResource {

	private static final Logger log = Logger.getLogger(GraphResource.class);

	@QueryParam("rawData")
	@DefaultValue("false")
	private boolean mIncludeRawXML;

	@QueryParam("onlyNotifies")
	@DefaultValue("false")
	private boolean mGetNotify;

	@GET
	@Path("index")
	@Produces("text/html")
	public String index() {
		try {
			@SuppressWarnings("resource")
			String site = new Scanner(getClass().getResourceAsStream("/index.html")).useDelimiter("\\A").next();
			return site;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "";
	}

	/**
	 * Returns a mapping from timestamps to the number of changes occurred at
	 * that timestamp.
	 * Example-URL: <tt>http://example.com:8000/graph/changes</tt>
	 */
	@GET
	@Path("changes")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getChangesMap(@PathParam("connectionName") String name) {
		SortedMap<Long, Long> changes;
		try {
			changes = Application.getConnectionManager().getGraphService(name).getChangesMap();
		} catch (ConnectionException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return jsonMarshaller().toJson(changes);
	}

	/**
	 * Returns the oldest known graph.
	 * Example-URL: <tt>http://example.com:8000/graph/initial</tt>
	 */
	@GET
	@Path("initial")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getInitialGraph(@PathParam("connectionName") String name) {
		List<IdentifierGraph> graphs;
		try {

			graphs = Application.getConnectionManager().getGraphService(name).getInitialGraph();

		} catch (ConnectionException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return jsonMarshaller().toJson(graphs);
	}

	@POST
	@Path("initial")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Object getInitialGraph(@PathParam("connectionName") String name, JSONObject jobj) {
		List<IdentifierGraph> graph;
		GraphFilter filter;
		try {
			filter = parseFilterFromJson(jobj, name);
			graph = Application.getConnectionManager().getGraphService(name).getInitialGraph(filter);
		} catch (ConnectionException | JSONException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}
		return jsonMarshaller().toJson(graph);
	}

	/**
	 * Returns the graph at the given timestamp. If the given timestamp
	 * is not a exact change-timestamp the next smaller timestamp to the
	 * given timestamp will be chosen. If query parameter onlyNotifes is
	 * given only notify metadata matching the given timestamp is returned.
	 * Example-URL: <tt>http://example.com:8000/graph/314159265[?onlyNotifes=true]</tt>
	 */
	@GET
	@Path("{at}")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getGraphAt(@PathParam("connectionName") String name, @PathParam("at") long timestamp) {
		List<IdentifierGraph> graphs;
		try {
			if (!mGetNotify) {
				graphs = Application.getConnectionManager().getGraphService(name).getGraphAt(timestamp);
			} else {
				graphs = Application.getConnectionManager().getGraphService(name).getNotifiesAt(timestamp);
			}
		} catch (ConnectionException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return jsonMarshaller().toJson(graphs);
	}

	@POST
	@Path("{at}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Object
			getGraphAt(@PathParam("connectionName") String name, @PathParam("at") long timestamp, JSONObject jobj) {
		List<IdentifierGraph> graph;
		GraphFilter filter;
		try {
			filter = parseFilterFromJson(jobj, name);
			graph = Application.getConnectionManager().getGraphService(name).getGraphAt(timestamp, filter);
		} catch (ConnectionException | JSONException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}
		return jsonMarshaller().toJson(graph);
	}

	/**
	 * Returns the current graph. More precisely the graph with the
	 * latest change of the IF-MAP structure.
	 * Example-URL: <tt>http://example.com:8000/graph/current</tt>
	 *
	 * @return the latest graph as a list of sub-graphs
	 */
	@GET
	@Path("current")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getCurrentGraph(@PathParam("connectionName") String name) {
		List<IdentifierGraph> graphs;
		try {

			graphs = Application.getConnectionManager().getGraphService(name).getCurrentGraph();

		} catch (ConnectionException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return jsonMarshaller().toJson(graphs);
	}

	@POST
	@Path("current")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Object getCurrentGraph(@PathParam("connectionName") String name, JSONObject jobj) {
		List<IdentifierGraph> graph;
		GraphFilter filter;
		try {
			filter = parseFilterFromJson(jobj, name);
			graph = Application.getConnectionManager().getGraphService(name).getCurrentGraph(filter);
		} catch (ConnectionException | JSONException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}
		return jsonMarshaller().toJson(graph);
	}

	/**
	 * Returns the delta object from the given timestamp <tt>from</tt> to the second
	 * timestamp <tt>to</tt>.
	 * Example-URL: <tt>http://example.com:8000/graph/42/43</tt>
	 *
	 * @param t1
	 *            the timestamp to start with the delta calculation
	 * @param t2
	 *            the timestamp to end with the delta calculation
	 * @return a {@link JSONObject} containing the updates and deletes
	 */
	@GET
	@Path("{from}/{to}")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getDelta(@PathParam("connectionName") String name, @PathParam("from") long t1,
			@PathParam("to") long t2) {
		Delta delta;
		try {

			delta = Application.getConnectionManager().getGraphService(name).getDelta(t1, t2);

		} catch (ConnectionException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
		}

		return jsonMarshaller().toJson(delta);
	}

	@GET
	@Path("adjacencies")
	@Produces(MediaType.APPLICATION_JSON)
	public Object getAllAdjacencyMatrices(@PathParam("connectionName") String name) {
		GraphService graphService = null;
		Map<Long,double[][]> list = new HashMap<>();
		try {
			graphService = Application.getConnectionManager().getGraphService(name);

			if(graphService != null)
				list = graphService.getAdjacencyMatrices(name);

		} catch (ConnectionException e) {
			e.printStackTrace();
		}
		//List<Adjacency> list = new ArrayList<>();

		return new Gson().toJson(list);
	}

	/**
	 * Returns the appropriate {@link JsonMarshaller} for the current request.
	 *
	 * @return a {@link JsonMarshaller}
	 */
	protected JsonMarshaller jsonMarshaller() {
		if (mIncludeRawXML) {
			return new JsonMarshallerBloated();
		} else {
			return new JsonMarshaller();
		}
	}

	private GraphFilter parseFilterFromJson(JSONObject jobj, String connection) throws JSONException {
		log.trace("Trying to parse JSON to filter: "
				+ jobj);
		String resultFilter = jobj.getString("resultFilter");
		resultFilter = resultFilter.equalsIgnoreCase("null") ? null : resultFilter;
		String matchLinks = jobj.getString("matchLinks");
		int maxDepth = jobj.getInt("maxDepth");
		IdentifierImpl startId = null;

		JSONObject id = jobj.getJSONObject("startId");

		String type = id.getString("type");
		startId = new IdentifierImpl(type);
		id.remove("type");
		Iterator<?> i = id.keys();

		while (i.hasNext()) {
			String tmpKey = (String) i.next();
			if (tmpKey.contains("@")) {
				startId.addProperty("/"
						+ type + "[" + tmpKey + "]", id.getString(tmpKey));
			} else {
				startId.addProperty("/"
						+ type + "/" + tmpKey, id.getString(tmpKey));
			}
		}

		return new GraphFilterImpl(startId, resultFilter, matchLinks, maxDepth,
				NamespaceHolder.getPrefixMap(connection));
	}
}
