package de.hshannover.f4.trust.metalyzer.statistic;

import java.util.ArrayList;

/**
 * @author hassannahle
 * @param <T> mean the resulttype of a query (for example: NodeResult)
 * The ResultObject includes information about a query in a given timeinterval.
 * It includes general statistical information about the timeinterval (mean, standardDeviation) and some informations about the 
 * timestamp*/

public class ResultObject<T> {

	private ArrayList<T> resultList;
	private double mean;
	private double standardDeviation;
	
	/**
	 * 
	 * @param mean describes the global mean over a timeinterval
	 * @param standardDeviation describes the global standardDeviation over a timeinterval
	 * @param evaluationResult a list of results for various timestamps in a timeinterval (for example: NodeResult)
	 */
	public ResultObject(double mean, double standardDeviation, ArrayList<T> evaluationResult){
		this.mean = mean;
		this.resultList = evaluationResult;
		this.standardDeviation = standardDeviation;
	}
	
	public ResultObject(){
		
	}
	
	/**
	 * @return returns the global mean value of a timeinterval
	 */
	public double getMean(){
		return mean;
	}
	
	/**
	 * @return returns the global standard deviation value of a timeinterval
	 */
	public double getStandardDeviation(){
		return standardDeviation;
	}
	
	/**
	 * @return returns a list of results for various timestamps in a timeinterval (for example: NodeResult)
	 */
	public ArrayList<T> getResultList(){
		return resultList;
	}
}
