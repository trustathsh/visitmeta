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
package de.hshannover.f4.trust.metalyzer;

import static de.hshannover.f4.trust.metalyzer.PropertyStrings.AR_NAME;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.DEVICE_NAME;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.IDENT_NAME;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.IDENT_TYPE;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.IP_TYPE;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.IP_VALUE;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.LAY2_PORT;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.LAY2_VLAN;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.LAY2_VLAN_NAME;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.MAC_VALUE;
import static de.hshannover.f4.trust.metalyzer.PropertyStrings.ROLE_NAME;

import java.util.ArrayList;

import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.IdentifierImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.LinkImpl;
import de.hshannover.f4.trust.visitmeta.dataservice.graphservice.MetadataImpl;
import de.hshannover.f4.trust.visitmeta.interfaces.Identifier;
import de.hshannover.f4.trust.visitmeta.interfaces.Metadata;

public class GraphBuilderHelper {
	private ArrayList<Identifier> idents;
	private ArrayList<Metadata> metas;

	public GraphBuilderHelper(ArrayList<Identifier> identifiers, ArrayList<Metadata> metas) {
		this.idents = identifiers;
		this.metas = metas;
	}

	/**
	 *  Creates a Device with the given name.
	 * @param name
	 */
	public IdentifierImpl createDevice(String name) {
		IdentifierImpl ident = new IdentifierImpl("device");
		ident.addProperty(DEVICE_NAME.get(), name);

		idents.add(ident);

		return ident;
	}

	/** Creates a IP with the given type and address.
	 * 
	 * @param type
	 * @param address
	 * @return
	 */
	public IdentifierImpl createIP(String type, String address) {
		IdentifierImpl ip = new IdentifierImpl("ip-address");
		ip.addProperty(IP_VALUE.get(), address);
		ip.addProperty(IP_TYPE.get(), type);

		idents.add(ip);

		return ip;
	}

	/** Creates a MAC with the given address.
	 * 
	 * @param address
	 * @return
	 */
	public IdentifierImpl createMAC(String address) {
		IdentifierImpl mac = new IdentifierImpl("mac-address");
		mac.addProperty(MAC_VALUE.get(), address);

		idents.add(mac);

		return mac;
	}

	/** Creates a Identity with the given type and name.
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public IdentifierImpl createIdentity(String type, String name) {
		IdentifierImpl ident = new IdentifierImpl("identity");

		ident.addProperty(IDENT_TYPE.get(), type);
		ident.addProperty(IDENT_NAME.get(), name);

		idents.add(ident);

		return ident;
	}

	/**
	 *  Creates a AR with the given name.
	 * @param name
	 * @return
	 */
	public IdentifierImpl createAR(String name) {
		IdentifierImpl ar = new IdentifierImpl("access-request");

		ar.addProperty(AR_NAME.get(), name);

		idents.add(ar);

		return ar;
	}

	/**
	 *  Creates a link with Device IP Metadata between to Identifiers.
	 * @param device
	 * @param ip
	 * @param ts
	 * @return
	 */
	public LinkImpl buildDeviceIPLink(IdentifierImpl device, IdentifierImpl ip, long ts) {
		MetadataImpl metaDeviceIP = new MetadataImpl("device-ip", true, ts);
		metas.add(metaDeviceIP);

		LinkImpl link = buildLink(device, ip);
		link.addMetadata(metaDeviceIP);

		return link;
	}

	/**
	 *  Creates a link between ip and mac with an ip-mac metadata.
	 * @param ip
	 * @param mac
	 * @param ts
	 * @return
	 */
	public LinkImpl buildIPMACLink(IdentifierImpl ip, IdentifierImpl mac, long ts) {
		MetadataImpl metaIPMAC = new MetadataImpl("ip-mac", false, ts);
		metas.add(metaIPMAC);

		LinkImpl link = buildLink(ip, mac);
		link.addMetadata(metaIPMAC);

		return link;
	}
	
	/**
	 *  Creates a auth-by-link between a pdp and ar.
	 * @param pdp
	 * @param ar
	 * @param ts
	 * @return
	 */
	public LinkImpl buildAuthByLink(IdentifierImpl pdp, IdentifierImpl ar, long ts) {
		MetadataImpl authBy = new MetadataImpl("authenticated-by", true, ts);
		metas.add(authBy);
		
		LinkImpl link = buildLink(pdp, ar);
		link.addMetadata(authBy);
		
		return link;
	}
	
	/**
	 *  Creates a layer2 link between a ar and switch-device.
	 * @param ar
	 * @param sw
	 * @param ts
	 * @param port
	 * @param vlan
	 * @param name
	 * @return
	 */
	public LinkImpl buildLayer2Link(IdentifierImpl ar, IdentifierImpl sw, long ts, String port, String vlan, String name) {
		MetadataImpl layer2 = new MetadataImpl("layer2-information",false,ts);
		metas.add(layer2);
		
		layer2.addProperty(LAY2_PORT.get(), port);
		layer2.addProperty(LAY2_VLAN.get(), vlan);
		layer2.addProperty(LAY2_VLAN_NAME.get(), name);
		
		LinkImpl link = buildLink(ar, sw);
		link.addMetadata(layer2);
		
		return link;
	}
	
	/**
	 * Creates a ar-ip link.
	 * @param ar
	 * @param ip
	 * @return
	 */
	public LinkImpl buildARIPLink(IdentifierImpl ar, IdentifierImpl ip, long ts) {
		MetadataImpl arIP = new MetadataImpl("access-request-ip",true,ts);
		metas.add(arIP);
		
		LinkImpl link = buildLink(ar, ip);
		link.addMetadata(arIP);
		
		return link;
	}
	
	/**
	 * Creates a ar-mac link.
	 * @param ar
	 * @param ip
	 * @return
	 */
	public LinkImpl buildARMACLink(IdentifierImpl ar, IdentifierImpl mac, long ts) {
		MetadataImpl arMAC = new MetadataImpl("access-request-mac",true,ts);
		metas.add(arMAC);
		
		LinkImpl link = buildLink(ar, mac);
		link.addMetadata(arMAC);
		
		return link;
	}
	
	/**
	 * Creates a link between ar and identity with a role metadata.
	 * @param ar
	 * @param identity
	 * @param role
	 * @return
	 */
	public LinkImpl buildRoleLink(IdentifierImpl ar, IdentifierImpl identity, long ts, String role) {
		MetadataImpl mRole = new MetadataImpl("role",true,ts);
		mRole.addProperty(ROLE_NAME.get(), role);
		metas.add(mRole);
		
		LinkImpl link = buildLink(ar, identity);
		link.addMetadata(mRole);
		
		return link;
	}
	
	/**
	 * Builds an ar-dev link.
	 * @param ar
	 * @param dev
	 * @return
	 */
	public LinkImpl buildARDevLink(IdentifierImpl ar, IdentifierImpl dev, long ts) {
		MetadataImpl arDev = new MetadataImpl("access-request-device",true,ts);
		metas.add(arDev);
		
		LinkImpl link = buildLink(ar, dev);
		link.addMetadata(arDev);
		
		return link;
	}

	/**
	 *  Creates just a link between to Identifier.
	 * @param ident1
	 * @param ident2
	 * @return
	 */
	public LinkImpl buildLink(IdentifierImpl ident1, IdentifierImpl ident2) {
		LinkImpl link = new LinkImpl(ident1, ident2);

		ident1.addLink(link);
		ident2.addLink(link);

		return link;
	}
}
