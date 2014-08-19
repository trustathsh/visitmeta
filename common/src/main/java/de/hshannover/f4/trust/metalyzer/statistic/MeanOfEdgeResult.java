package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * @author hassannahle
 * a result object for edge per nodes queries, which extends from the EvaluationResult class.
 * This result object is used to retrieve information about edge per nodes queries.
 */
public class MeanOfEdgeResult extends EvaluationResult {

	private double meanOfEdges;
	
	/**
	 * @param timestamp a timestamp from the timeinterval
	 * @param deviation deviation from the meanOfEdges value of a timestamp to the mean of edge value of the time interval
	 * @param meanOfEdges number of edge per nodes in the timestamp
	 */
	public MeanOfEdgeResult(long timestamp, double deviation, double meanOfEdges) {
		super(timestamp, deviation);
		this.meanOfEdges = meanOfEdges;
	}

	public MeanOfEdgeResult() {
		
	}
	
	/**
	 * @return returns the edge per nodes value of the timestamp
	 */
	public double getMeanOfEdges() {
		return meanOfEdges;
	}

	

}
