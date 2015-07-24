package de.hshannover.f4.trust.visitmeta.persistence;

public class SubmitStatus {

	private int mMetadataNumber;

	private int mAddedMetadata;

	private int mUpdatedMetadata;

	private int mRemovedMetadata;

	/**
	 * Metadata SubmitStatus
	 * 
	 * @param max metadata number
	 * @param metadataNumber
	 */
	public SubmitStatus(int metadataNumber) {
		mMetadataNumber = metadataNumber;
		mAddedMetadata = 0;
		mUpdatedMetadata = 0;
		mRemovedMetadata = 0;
	}

	/**
	 * Increased the ADDED-counter by one
	 * 
	 * @return long The actual counter value
	 */
	public int added() {
		mAddedMetadata++;
		return mAddedMetadata;
	}

	/**
	 * Increased the UPDATED-counter by one
	 * 
	 * @return long The actual counter value
	 */
	public int updated() {
		mUpdatedMetadata++;
		return mUpdatedMetadata;
	}

	/**
	 * Increased the REMOVED-counter by one
	 * 
	 * @return long The actual counter value
	 */
	public int removed() {
		mRemovedMetadata++;
		return mRemovedMetadata;
	}

	/**
	 * Resets the ADDED-counter UPDATED-counter and IGNORED-counter to 0.
	 */
	public void resetCounter() {
		mAddedMetadata = 0;
		mUpdatedMetadata = 0;
		mRemovedMetadata = 0;
	}

	/**
	 * Get the actual SubmitStatus.
	 * 
	 * @return String with ADDED-counter UPDATED-counter and IGNORED-counter
	 */
	public String getStatus() {
		return "\t ADDED(" + mAddedMetadata + ") UPDATED(" + mUpdatedMetadata + ") REMOVED(" + mRemovedMetadata
				+ ") IGNORED(" + (mMetadataNumber - mAddedMetadata - mUpdatedMetadata - mRemovedMetadata)
				+ ") Metadata";
	}

	/**
	 * Same as getStatus()
	 */
	@Override
	public String toString() {
		return getStatus();
	}
}
