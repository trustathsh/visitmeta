/**
 * Project: Metalyzer 
 * 
 * Auhtor: Johannes Busch
 * Last Change: 
 * 		by: 	$Author: $
 * 		date: 	$Date: $ 
 * Copyright (c): Hochschule Hannover
 */
package de.hshannover.f4.trust.visitmeta.interfaces;

/**
 * Enum to distinguish between identifier, metadata and links
 * in graph database.
 * 
 * @author Johannes Busch
 *
 */
public enum GraphType {
	IDENTIFIER,
	METADATA,
	LINK;
}
