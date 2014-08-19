package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * @author hassannahle
 * a result object for edge queries, which extends from the EvaluationResult class.
 * This result object is used to retrieve information about edge queries.
 */
public class EdgeResult extends EvaluationResult {
	
	private long numbersOfEgde;
	
	/**
	 * @param timestamp a timestamp from the timeinterval
	 * @param deviation deviation from the numbersOfEdge value (of timestamp) to the mean value (of the timeinterval)
	 * @param numbersOfEdge number of edges in the timestamp
	 */
	public EdgeResult(long timestamp, double deviation, long numbersOfEdge) {
		super(timestamp, deviation);
		this.numbersOfEgde = numbersOfEdge;
	}

	public EdgeResult() {
	}

	/**
	 * @return return the number of edges in the timestamp
	 */
	public long getNumbersOfEgde() {
		return numbersOfEgde;
	}



}
