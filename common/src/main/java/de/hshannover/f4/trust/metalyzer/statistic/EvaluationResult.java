package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * @author hassannahle
 * a result object, which represents various information about a timestamp
 */
public class EvaluationResult {

	private long timestamp;
	private double deviation;
	
	/**
	 * 
	 * @param timestamp a timestamp from the timeinterval
	 * @param deviation deviation from timestamp to mean
	 */
	public EvaluationResult(long timestamp, double deviation){
		this.timestamp = timestamp;
		this.deviation = deviation;
	}
	
	public EvaluationResult() {	
		
	}
	
	/**
	 * @return returns the selected timestamp
	 */
	public long getTimestamp(){
		return timestamp;
	}

	/**
	 * @return returns the deviation from timestamp to the mean value
	 */
	public double getDeviation(){
		return deviation;
	}
}
