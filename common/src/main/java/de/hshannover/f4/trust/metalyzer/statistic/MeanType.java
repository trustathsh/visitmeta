package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * 
 * @author hassannahle
 *a enum for selection of statistical average querys
 */

public enum MeanType {
	
	/**
	 * a attribute to evaluate links from identiier
	 */
	LFI,
	
	/**
	 * a attribute to evalute the mean of change roles of users
	 */
	CHANGEROLE,
	
	/**
	 * a attribute to evalute the mean of the user duration in a network
	 */
	USERDURATION
}
