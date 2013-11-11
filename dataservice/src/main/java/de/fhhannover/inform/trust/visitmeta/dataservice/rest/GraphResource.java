package de.fhhannover.inform.trust.visitmeta.dataservice.rest;

/*
 * #%L
 * ====================================================
 *   _____                _     ____  _____ _   _ _   _
 *  |_   _|_ __ _   _ ___| |_  / __ \|  ___| | | | | | |
 *    | | | '__| | | / __| __|/ / _` | |_  | |_| | |_| |
 *    | | | |  | |_| \__ \ |_| | (_| |  _| |  _  |  _  |
 *    |_| |_|   \__,_|___/\__|\ \__,_|_|   |_| |_|_| |_|
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
 * This file is part of VisITMeta, version 0.0.1, implemented by the Trust@FHH 
 * research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2013 Trust@FHH
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

import java.util.List;

import java.util.Scanner;
import java.io.File;
import java.io.InputStream;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import de.fhhannover.inform.trust.visitmeta.dataservice.Application;
import de.fhhannover.inform.trust.visitmeta.dataservice.graphservice.SimpleGraphService;
import de.fhhannover.inform.trust.visitmeta.interfaces.Delta;
import de.fhhannover.inform.trust.visitmeta.interfaces.GraphFilter;
import de.fhhannover.inform.trust.visitmeta.interfaces.GraphService;
import de.fhhannover.inform.trust.visitmeta.interfaces.IdentifierGraph;

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
@Path("/graph")
public class GraphResource {

	private static final Logger log = Logger.getLogger(GraphResource.class);

	private SimpleGraphService mGraphService;

	@QueryParam("rawData")
	@DefaultValue("false")
	private boolean mIncludeRawXML;

	public GraphResource() {
		mGraphService = initGraphService();
	}

	@GET
	@Path("index")
	@Produces("text/html")
	public String index() {
		try {
			String site = new Scanner(getClass().getResourceAsStream("/index.html")).useDelimiter("\\A").next();
			return site;
		} catch (Exception e) {
			e.printStackTrace();
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
	public JSONObject getChangesMap() {
		JSONObject changes = new JSONObject(mGraphService.getChangesMap());
		return changes;
	}

	/**
	 * Returns the oldest known graph.
	 * Example-URL: <tt>http://example.com:8000/graph/initial</tt>
	 */
	@GET
	@Path("initial")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getInitialGraph() {
		List<IdentifierGraph> graphs = mGraphService.getInitialGraph();
		JSONArray jsonGraphs = jsonMarshaller().toJson(graphs);
		return jsonGraphs;
	}

	/**
	 * Returns the graph at the given timestamp. If the given timestamp
	 * is not a exact change-timestamp the next smaller timestamp to the
	 * given timestamp will be chosen.
	 * Example-URL: <tt>http://example.com:8000/graph/314159265</tt>
	 */
	@GET
	@Path("{at}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONArray getGraphAt(@PathParam("at") long timestamp) {
		List<IdentifierGraph> graphs = mGraphService.getGraphAt(timestamp);
		JSONArray jsonGraphs = jsonMarshaller().toJson(graphs);
		return jsonGraphs;
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
	public JSONArray getCurrentGraph() {
		List<IdentifierGraph> graphs = mGraphService.getCurrentGraph();
		JSONArray jsonGraphs = jsonMarshaller().toJson(graphs);
		return jsonGraphs;
	}

	public List<IdentifierGraph> getInitialGraph(GraphFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IdentifierGraph> getGraphAt(long timestamp, GraphFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IdentifierGraph> getCurrentGraph(GraphFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns the delta object from the given timestamp <tt>from</tt> to the second
	 * timestamp <tt>to</tt>.
	 * Example-URL: <tt>http://example.com:8000/graph/42/43</tt>
	 *
	 * @param t1 the timestamp to start with the delta calculation
	 * @param t2 the timestamp to end with the delta calculation
	 * @return a {@link JSONObject} containing the updates and deletes
	 */
	@GET
	@Path("{from}/{to}")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getDelta(@PathParam("from") long t1, @PathParam("to") long t2) {
		Delta delta = mGraphService.getDelta(t1, t2);
		JSONObject jsonDelta = jsonMarshaller().toJson(delta);
		return jsonDelta;
	}

	// return the GraphService instance, can be overwritten to return a mock for unit test purposes
	protected SimpleGraphService initGraphService() {
		return Application.getGraphservice();
	}

	/**
	 * Returns the appropriate {@link JsonMarshaller} for the current request.
	 * @return a {@link JsonMarshaller}
	 */
	protected JsonMarshaller jsonMarshaller() {
		if (mIncludeRawXML) {
			return new JsonMarshallerBloated();
		} else {
			return new JsonMarshaller();
		}
	}
}