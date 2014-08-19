package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * @author hassannahle
 * a result object for node queries, which extends from the EvaluationResult class.
 * This result object is used to retrieve information about node queries.
 */
public class NodeResult extends EvaluationResult {
	
	private long numbersOfIdentifer;

	/**
	 * @param timestamp a timestamp from the timeinterval
	 * @param deviation deviation from the numbersOfIdentifer value (of timestamp) to the mean value (of the timeinterval)
	 * @param numbersOfIdentifier number of nodes in the timestamp
	 */
	public NodeResult(long timestamp, double deviation, long numbersOfIdentifier) {
		super(timestamp, deviation);
		this.numbersOfIdentifer = numbersOfIdentifier;
		
	}
	
	public NodeResult() {
		
	}
	
	/**
	 * @return returns the number of nodes in the timestamp
	 */
	public long getNumbersOfIdentifer(){
		return numbersOfIdentifer;
	}
}
