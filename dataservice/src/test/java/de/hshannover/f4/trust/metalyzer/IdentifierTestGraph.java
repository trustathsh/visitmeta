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
/**
 * Project: Metalyzer 
 * Author: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.metalyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierGraphImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.IdentifierGraph;

/**
 * 
 * 
 *	@deprecated Will be replaced by TestGraphBuilder
 */
@Deprecated
public class IdentifierTestGraph {
	private IdentifierImpl pdp, ip_address;
	private IdentifierGraphImpl graph_10 = new IdentifierGraphImpl(10);
	private IdentifierGraphImpl graph_20 = new IdentifierGraphImpl(20);
	
	
	public IdentifierTestGraph() {
		pdp = new IdentifierImpl("device");
		graph_10.insert(pdp);
		graph_20.insert(pdp);
		
		ip_address = new IdentifierImpl("ip-address");
		ip_address.addProperty("/ip-address[@value]", "10.0.0.255");
		ip_address.addProperty("/ip-address[@type]", "IPv4");
		graph_10.insert(ip_address);
		
		LinkImpl l_p_i = graph_10.connect(pdp, ip_address);
		
		MetadataImpl m = new MetadataImpl("device-ip",true,10);
		
		l_p_i.addMetadata(m);
		
		graphAt10();
		graphAt20();
	}
	
	private void graphAt10() {
		IdentifierImpl acc_req = new IdentifierImpl("access-request");
		graph_10.insert(acc_req);
		
		LinkImpl l_p_a = graph_10.connect(acc_req, pdp);
		
		MetadataImpl authby = new MetadataImpl("authenticated-by", true, 10);
		
		l_p_a.addMetadata(authby);
		
		IdentifierImpl identity = new IdentifierImpl("identity");
		graph_10.insert(identity);
		
		LinkImpl l_a_i = graph_10.connect(acc_req, identity);
		
		MetadataImpl authas = new MetadataImpl("authenticated-as", true, 10);
		
		MetadataImpl role = new MetadataImpl("role", true, 10);
		role.addProperty("/meta:role/name", "user");
		
		l_a_i.addMetadata(authas);
		l_a_i.addMetadata(role);
		
	}
	
	private void graphAt20() {
		IdentifierImpl acc_req = new IdentifierImpl("access-request");
		graph_20.insert(acc_req);
		
		MetadataImpl authby = new MetadataImpl("authenticated-by", true, 20);
		
		LinkImpl l_p_a = graph_20.connect(acc_req, pdp);
		l_p_a.addMetadata(authby);
		
		
		IdentifierImpl identity = new IdentifierImpl("identity");
		graph_20.insert(identity);
		
		MetadataImpl authas = new MetadataImpl("authenticated-as", true, 20);
		
		MetadataImpl role = new MetadataImpl("role", true, 20);
		role.addProperty("/meta:role/name", "admin");
		
		LinkImpl l_a_i = graph_20.connect(acc_req, identity);
		l_a_i.addMetadata(authas);
		l_a_i.addMetadata(role);
		
		
		IdentifierImpl ip_addr = new IdentifierImpl("ip-address");
		ip_addr.addProperty("/ip-address[@value]", "10.0.0.1");
		ip_addr.addProperty("/ip-address[@type]", "IPv4");
		graph_20.insert(ip_addr);
		
		MetadataImpl acc_req_ip = new MetadataImpl("access-request-ip", true, 20);
		
		LinkImpl l_a_ip = graph_20.connect(acc_req,ip_addr);
		l_a_ip.addMetadata(acc_req_ip);
		
		IdentifierImpl mac_addr = new IdentifierImpl("ip-address");
		mac_addr.addProperty("/mac-address[@value]", "ff-ff-ff-ff");
		graph_20.insert(mac_addr);
		
		MetadataImpl acc_req_mac = new MetadataImpl("access-request-mac", true, 20);
		
		LinkImpl l_a_mac = graph_20.connect(acc_req,mac_addr);
		l_a_mac.addMetadata(acc_req_mac);
	}
	
	/**
	 * @deprecated TestGraphBuilder should be used from now!
	 * @return
	 */
	@Deprecated
	public Collection<Identifier> getIdentifierTestGraphAt10() {
		return graph_10.getIdentifiers();
	}
	
	/**
	 * @deprecated TestGraphBuilder should be used from now!
	 * @return
	 */
	@Deprecated
	public List<IdentifierGraph> getIdentifierGraphsTestGraphAt10() {
		ArrayList<IdentifierGraph> graphs = new ArrayList<IdentifierGraph>();
		
		graphs.add(graph_10);
		
		return graphs;
	}
	
	/**
	 * @deprecated TestGraphBuilder should be used from now!
	 * @return
	 */
	@Deprecated
	public Collection<Identifier> getIdentifierTestGraphAt20() {
		return graph_20.getIdentifiers();
	}
	
	/**
	 * @deprecated TestGraphBuilder should be used from now!
	 * @return
	 */
	@Deprecated
	public List<IdentifierGraph> getIdentifierGraphsTestGraphAt20() {
		ArrayList<IdentifierGraph> graphs = new ArrayList<IdentifierGraph>();
		
		graphs.add(graph_20);
		
		return graphs;
	}
}
