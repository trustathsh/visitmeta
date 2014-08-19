package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * 
 * @author hassannahle
 * a enum for selection of statistical frequency querys
 */
public enum FrequencyType {
	/**
	 * a attribite to evaluate the frequency of identifiers
	 */
	IDENTIFIER,
	/**
	 * a attribite to evaluate the frequency of metadata
	 */
	METADATA,
	/**
	 * a attribite to evaluate the frequency of devices
	 */
	DEVICES,
	/**
	 * a attribite to evaluate the frequency of roles (a metadata)
	 */
	ROLES
}
