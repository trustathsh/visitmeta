package de.hshannover.f4.trust.metalyzer.statistic;

/**
 * @author Juri Seewald
 */

public class RateOfChangeResult {
	
	private long timestamp;
	private long previous;
	private long next;
	private long current;
	
	public RateOfChangeResult(long timestamp, long current, long previous, long next){
		this.timestamp = timestamp;
		this.previous = previous;
		this.next = next;
		this.current = current;
	}
	
	public RateOfChangeResult(){
		
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public long getPrevios(){
		return previous;
	}
	
	public long getNext(){
		return next;
	}
	
	public long getCurrent(){
		return current;
	}
	
}
