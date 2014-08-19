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
package de.hshannover.f4.trust.metalyzer.semantics.debug;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import de.hshannover.f4.trust.metalyzer.api.MetalyzerAPI;
import de.hshannover.f4.trust.metalyzer.api.exception.MetalyzerAPIException;
import de.hshannover.f4.trust.metalyzer.semantics.services.SemanticsController;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierPair;
import de.hshannover.f4.trust.visitmeta.interfaces.Link;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class SemanticsMain implements Runnable {
	
	Logger log= Logger.getLogger(SemanticsMain.class);
	
	/**
	 * @param args
	 */
	public void runSemantics() {
		
		SemanticsController semCon= SemanticsController.getInstance();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("Running runSemantics");
		

		Collection<Identifier> userList = null;
		try {
			userList = semCon.getConnection().getIdentifierFinder().getCurrent();
		} catch (MetalyzerAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(userList != null){
			Iterator<Identifier> iter= userList.iterator();
			log.info("------------------------------------Identifiers: ------------------------------------");
			while(iter.hasNext()){
				Identifier id= iter.next();
				log.info("Identifier Typename: "+id.getTypeName());
				for(Link idLink : id.getLinks()){
					IdentifierPair idPair= idLink.getIdentifiers();
					for(Metadata idMeta : idLink.getMetadata()){
						log.info("    Metadata from Link: "+idMeta.getTypeName());
					}
					log.info("    Link from: "+idPair.getFirst().getTypeName());
					log.info("           to: "+idPair.getSecond().getTypeName());
				}
				StringBuilder lsb= new StringBuilder();
				lsb.append(id.getProperties().toString());
				for(String fId : id.getProperties()){
					lsb.append("#: "+id.valueFor(fId).toString());
				}
				log.info(lsb.toString());
			}
		}

		Collection<Metadata> metaList = null;
		log.info("------------------------------------Metadata: ------------------------------------");
		try {
			metaList = semCon.getConnection().getMetadataFinder().getCurrent();
		} catch (MetalyzerAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(metaList != null){
			Iterator<Metadata> iter= metaList.iterator();
			while(iter.hasNext()){
				Metadata meta= iter.next();
				log.info("Metadata ("+meta.getTypeName()+"): Publishtimestamp:"+meta.getPublishTimestamp());
				StringBuilder lsb= new StringBuilder();
				lsb.append(meta.getProperties().toString());
				for(String fId : meta.getProperties()){
					lsb.append("#: "+meta.valueFor(fId).toString());
				}
				log.info(lsb.toString());

			}
		}
		log.info("End Run");
	}

	@Override
	public void run() {
		runSemantics();
		runCount();
	}

	private void runCount() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("Running Test");
		
		log.info("Current Nodes: " + MetalyzerAPI.getInstance().getGraphAnalyzer().countNodes());
		log.info("Nodes at 1400845125000: " + MetalyzerAPI.getInstance().getGraphAnalyzer().countNodesAt(1401450107000L));
		log.info("Nodes ( 1400845125000, 1400845127000 ): " + MetalyzerAPI.getInstance().getGraphAnalyzer().countNodes(1401450107000L,1401450113000L));
		
		log.info("Current Edges: " + MetalyzerAPI.getInstance().getGraphAnalyzer().countEdges());
		log.info("Edges at 1400845125000: " + MetalyzerAPI.getInstance().getGraphAnalyzer().countEdgesAt(1401450107000L));
		log.info("Edges ( 1400845125000, 1400845127000 ): " + MetalyzerAPI.getInstance().getGraphAnalyzer().countEdges(1401450107000L,1401450113000L));
		
		log.info("Current Metadata: " + MetalyzerAPI.getInstance().getMetadataFinder().count());
		log.info("Metadata at 1400845125000: " + MetalyzerAPI.getInstance().getMetadataFinder().count(1401450107000L));
		log.info("Metadata ( 1400845125000, 1400845127000 ): " + MetalyzerAPI.getInstance().getMetadataFinder().count(1401450107000L,1401450113000L));
		
		log.info("Mean of current Edges: " + MetalyzerAPI.getInstance().getGraphAnalyzer().getMeanOfEdges());
		log.info("Mean of current Edges at 1400845125000: " + MetalyzerAPI.getInstance().getGraphAnalyzer().getMeanOfEdges(1401450107000L));
	}
	
}