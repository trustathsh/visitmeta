package de.hshannover.f4.trust.metalyzer;

/**
 * Just an enum of all used Property-Key-Strings.
 * 
 * @author Johannes Busch
 *
 */
public enum PropertyStrings {
	// Identifier Property Strings
	AR_NAME("/access-request[@name]"),
	
	DEVICE_NAME("/device/name"),
	
	IP_VALUE("/ip-address[@value]"),
	IP_TYPE("/ip-address[@type]"),
	
	MAC_VALUE("/mac-address[@value]"),
	
	IDENT_NAME("/identity[@name]"),
	IDENT_TYPE("/identity[@type]"),
	//---------------------------------------------------------------------------
	
	// Metadata Property Strings
	CAP_NAME("/meta:capability/name"),
	
	ROLE_NAME("/meta:role/name"),
	
	DEV_ATTR("/meta:device-attribute/name"),
	
	LAY2_PORT("/meta:layer2-information/port"),
	LAY2_VLAN("/meta:layer2-information/vlan"),
	LAY2_VLAN_NAME("/meta:layer2-information/vlan-name");
	//----------------------------------------------------------------------
	
	private String desc;
	
	private PropertyStrings(String desc) {
		this.desc = desc;
	}
	
	public String get() {
		return desc;
	}
}
