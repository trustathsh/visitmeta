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
 * This file is part of visitmeta dataservice, version 0.1.2,
 * implemented by the Trust@HsH research group at the Hochschule Hannover.
 * %%
 * Copyright (C) 2012 - 2014 Trust@HsH
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
package de.hshannover.f4.trust.metalyzer.statistic.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import de.hshannover.f4.trust.metalyzer.api.StandardIdentifierType;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencySelection;
import de.hshannover.f4.trust.metalyzer.statistic.FrequencyType;
import de.hshannover.f4.trust.metalyzer.statistic.MeanType;
import de.hshannover.f4.trust.metalyzer.statistic.StatisticController;
import de.hshannover.f4.trust.metalyzer.statistic.StatisticFacade;

/**
 * Boundary-Class to provide the Statistic-Services over REST.
 * 
 * @author hassannahle
 * @author juriSeewald
 * 
 */
@Path("{connectionName}/analysis/statistics")
public class StatisticRessource implements StatisticFacade {

	private StatisticController statisticController;
	private Gson gson;

	@PathParam("connectionName")
	private String connectionName;

	public StatisticRessource() {
		statisticController = StatisticController.getInstance();
		gson = new Gson();
	}

	/**
	 * evaluate the mean of different calculations
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @param filter
	 *            Chooses the characteristic (for example: CHANGEROLE)
	 * @param type
	 * @return returns a result (as a double value) of a given characteristic
	 * @throws MetalyzerAPIException
	 */
	@Override
	@GET
	@Path("evaluateMean/from/{from}/to/{to}/filter/{filter}/type/{type}")
	@Produces(MediaType.TEXT_PLAIN)
	public String evaluateMean(@PathParam("from") long from,
			@PathParam("to") long to,
			@PathParam("filter") StandardIdentifierType filter,
			@PathParam("type") MeanType type) {
		try {
			statisticController.setConnection(connectionName);
			return new Gson().toJson(statisticController.evaluateMean(from, to,
					filter, type));
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * evaluate the frequency (relative and absolute) of different calculations
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @param select
	 *            Chooses the characteristic (for example: RELATIVE_FREQUENCY)
	 * @param type
	 *            Chooses the characteristic (for example: CHANGEROLE)
	 * @param selection
	 *            Chooses the main calculate characteristic (for example:
	 *            RELATIVE_FREQUENCY)
	 * @return returns a result (as a HashMap(typename, value)) of a given
	 *         characteristic
	 * @throws MetalyzerAPIException
	 */
	@Override
	@GET
	@Path("evaluateFrequency/from/{from}/to/{to}/type/{type}/selection/{selection}")
	@Produces(MediaType.TEXT_PLAIN)
	public String evaluateFrequency(@PathParam("from") long from,
			@PathParam("to") long to, @PathParam("type") FrequencyType type,
			@PathParam("selection") FrequencySelection selection) {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.evaluateFrequency(from, to,
					type, selection));
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * counts all metadata in the graph
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @return returns the sum of metadata as a json-string
	 */
	@Override
	@GET
	@Path("sumOfMetadata/from/{from}/to/{to}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sumOfMetadata(@PathParam("from") long from,
			@PathParam("to") long to) {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.countMetadata(from, to));
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * counts all metadata in the graph
	 * 
	 * @author Hassan Nahle
	 * @param timestamp
	 *            timestamp
	 * @return returns the sum of metadata as a json-string
	 */
	@Override
	@GET
	@Path("sumOfMetadata/timestamp/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sumOfMetadata(@PathParam("timestamp") long timestamp) {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.countMetadata(timestamp));
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * counts all metadata at the current time in the graph
	 * 
	 * @author Hassan Nahle
	 * @return returns the sum of metadata as a json-string
	 */
	@Override
	@GET
	@Path("sumOfMetadata")
	@Produces(MediaType.TEXT_PLAIN)
	public String sumOfMetadata() {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.countMetadata());
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * counts all identifier in the graph
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @return returns the sum of identifier as a json-string
	 */
	@GET
	@Override
	@Path("sumOfIdentifier/from/{from}/to/{to}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sumOfIdentifier(@PathParam("from") long from,
			@PathParam("to") long to) {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.countIdentifier(from, to));
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * counts all identifier in the graph
	 * 
	 * @author Hassan Nahle
	 * @param timestamp
	 *            timestamp
	 * @return returns the sum of identifier as a json-string
	 */
	@GET
	@Override
	@Path("sumOfIdentifier/timestamp/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sumOfIdentifier(@PathParam("timestamp") long timestamp) {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.countIdentifier(timestamp));
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * counts all identifier in the graph
	 * 
	 * @author Hassan Nahle
	 * @param timestamp
	 *            timestamp
	 * @return returns the sum of identifier as a json-string
	 */
	@GET
	@Override
	@Path("sumOfIdentifier")
	@Produces(MediaType.TEXT_PLAIN)
	public String sumOfIdentifier() {
		try {
			statisticController.setConnection(connectionName);
			return gson.toJson(statisticController.countIdentifier());
		} catch (MetalyzerAPIException e) {
			Logger.getLogger(StatisticRessource.class.getName()).log(
					Level.SEVERE, null, e);
		}
		return null;
	}

	/**
	 * Represents a query of a time interval for edges, which be stored in a
	 * result object The ResultObject includes information about edges in a
	 * given time interval. It includes general statistical information about
	 * the time interval (mean, standardDeviation) and some informations about
	 * the time stamp (number of edges and deviation)
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @return returns the result of the edge query as json-string
	 */
	@GET
	@Path("edgeCharecteristic/from/{from}/to/{to}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String getEdgeCharecteristic(@PathParam("from") long from,
			@PathParam("to") long to) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.getEdgeCharecteristic(from, to));
	}

	/**
	 * Represents a query of a time interval for edges, which be stored in a
	 * result object The ResultObject includes information about edges in a
	 * given time interval. It includes general statistical information about
	 * the time interval (mean, standardDeviation) and some informations about
	 * the time stamp (number of edges and deviation)
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @return returns the result of the edge query as json-string
	 */
	@GET
	@Override
	@Path("meanOfEdgeCharecteristic/from/{from}/to/{to}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getMeanOfEdgeCharecteristic(@PathParam("from") long from,
			@PathParam("to") long to) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.getMeanOfEdgeCharecteristic(
				from, to));
	}

	/**
	 * Represents a query of a time interval for the edge per nodes, which be
	 * stored in a result object The ResultObject includes information about
	 * nodes in a given time interval. It includes general statistical
	 * information about the time interval (mean, standardDeviation) and some
	 * informations about the time stamp (edge per nodes of all nodes and
	 * deviation)
	 * 
	 * @author Hassan Nahle
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @return returns the result of the edge query as json-string
	 */
	@GET
	@Override
	@Path("nodeCharecteristic/from/{from}/to/{to}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getNodeCharecteristic(@PathParam("from") long from,
			@PathParam("to") long to) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.getNodeCharecteristic(from, to));
	}

	/**
	 * return an ArrayList with RateOfChangeResult objects at different
	 * timestamps
	 * 
	 * @author Juri Seewald
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @param type
	 *            selects the standard identifier
	 * @return ArrayList of RateOfChangeResult objects as Json-String, which
	 *         contains the timestamp, difference of count of identifier to
	 *         previous and next timestamp
	 */

	@Override
	@GET
	@Path("rateOfChange/from/{from}/to/{to}/type/{type}")
	@Produces(MediaType.TEXT_PLAIN)
	public String rateOfChange(@PathParam("from") long from,
			@PathParam("to") long to,
			@PathParam("type") StandardIdentifierType type) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.rateOfChange(from, to, type));
	}

	/**
	 * returns an ArrayList depending on type of metadata with
	 * RateOfChangeResult objects at different timestamps
	 * 
	 * @author Juri Seewald
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @param type
	 *            selects the metadata
	 * @return RateOfChangeResult objects, which contains the timestamp,
	 *         difference of metadata to previous and next timestamp
	 */
	@Override
	@GET
	@Path("rateOfChangeMeta/from/{from}/to/{to}/type/{type}")
	@Produces(MediaType.TEXT_PLAIN)
	public String rateOfChange(@PathParam("from") long from,
			@PathParam("to") long to, @PathParam("type") String type) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.rateOfChange(from, to, type));
	}

	/**
	 * returns an ArrayList independent of a metadata type with
	 * RateOfChangeResult objects at different timestamps
	 * 
	 * @author Juri Seewald
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @param type
	 *            selects the metadata
	 * @return RateOfChangeResult objects, which contains the timestamp,
	 *         difference of metadata to previous and next timestamp
	 */
	@Override
	@GET
	@Path("rateOfChange/from/{from}/to/{to}")
	@Produces(MediaType.TEXT_PLAIN)
	public String rateOfChange(@PathParam("from") long from,
			@PathParam("to") long to) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.rateOfChange(from, to));
	}

	/**
	 * calculates the relation of nodes to links
	 * 
	 * @author Juri Seewald
	 * @param timestamp
	 *            timestamp at calculation time
	 * @return double
	 */
	@Override
	@GET
	@Path("relationOfNodesToLinks/timestamp/{timestamp}")
	@Produces(MediaType.TEXT_PLAIN)
	public String relationOfNodesToLinks(@PathParam("timestamp") long timestamp) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController
				.relationOfNodesToLinks(timestamp));
	}

	/**
	 * evaluate the mean of different calculations at given timestamp
	 * 
	 * @author Juri Seewald
	 * @param timestamp
	 * @param filter
	 *            Chooses the characteristic (for example: CHANGEROLE)
	 * @param type
	 * @return returns a result (as a double value) of a given characteristic
	 * @throws MetalyzerAPIException
	 */
	@Override
	@GET
	@Path("evaluateMean/current/filter/{filter}/type/{type}")
	@Produces(MediaType.TEXT_PLAIN)
	public String evaluateMean(
			@PathParam("filter") StandardIdentifierType filter,
			@PathParam("type") MeanType type) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.evaluateMean(filter, type));
	}

	/**
	 * evaluate the frequency (relative and absolute) of different calculations
	 * 
	 * @author Juri Seewald
	 * @param from
	 *            start time
	 * @param to
	 *            end time
	 * @param select
	 *            Chooses the characteristic (for example: RELATIVE_FREQUENCY)
	 * @param type
	 *            Chooses the characteristic (for example: CHANGEROLE)
	 * @param selection
	 *            Chooses the main calculate characteristic (for example:
	 *            RELATIVE_FREQUENCY)
	 * @return returns a result (as a HashMap(typename, value)) of a given
	 *         characteristic
	 * @throws MetalyzerAPIException
	 */
	@Override
	@GET
	@Path("evaluateMean/timestamp/{timestamp}/filter/{filter}/type/{type}")
	@Produces(MediaType.TEXT_PLAIN)
	public String evaluateMean(@PathParam("timestamp") long timestamp,
			@PathParam("filter") StandardIdentifierType filter,
			@PathParam("type") MeanType type) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.evaluateMean(timestamp, filter,
				type));
	}

	/**
	 * evaluate the frequency (relative and absolute) of different calculations
	 * at current timestamp
	 * 
	 * @author Juri Seewald
	 * @param select
	 *            Chooses the characteristic (for example: RELATIVE_FREQUENCY)
	 * @param type
	 *            Chooses the characteristic (for example: CHANGEROLE)
	 * @param selection
	 *            Chooses the main calculate characteristic (for example:
	 *            RELATIVE_FREQUENCY)
	 * @return returns a result (as a HashMap(typename, value)) of a given
	 *         characteristic
	 * @throws MetalyzerAPIException
	 */
	@Override
	@GET
	@Path("evaluateFrequency/current/type/{type}/selection/{selection}")
	@Produces(MediaType.TEXT_PLAIN)
	public String evaluateFrequency(@PathParam("type") FrequencyType type,
			@PathParam("selection") FrequencySelection selection) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.evaluateFrequency(type,
				selection));
	}

	/**
	 * evaluate the frequency (relative and absolute) of different calculations
	 * at given timestamp
	 * 
	 * @author Juri Seewald
	 * @param timestamp
	 * @param select
	 *            Chooses the characteristic (for example: RELATIVE_FREQUENCY)
	 * @param type
	 *            Chooses the characteristic (for example: CHANGEROLE)
	 * @param selection
	 *            Chooses the main calculate characteristic (for example:
	 *            RELATIVE_FREQUENCY)
	 * @return returns a result (as a HashMap(typename, value)) of a given
	 *         characteristic
	 * @throws MetalyzerAPIException
	 */
	@GET
	@Path("evaluateFrequency/timestamp/{timestamp}/type/{type}/selection/{selection}")
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public String evaluateFrequency(@PathParam("timestamp") long timestamp,
			@PathParam("type") FrequencyType type,
			@PathParam("selection") FrequencySelection selection) {
		statisticController.setConnection(connectionName);
		return gson.toJson(statisticController.evaluateFrequency(timestamp,
				type, selection));
	}

}
