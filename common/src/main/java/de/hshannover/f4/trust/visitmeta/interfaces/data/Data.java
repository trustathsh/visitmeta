package de.hshannover.f4.trust.visitmeta.interfaces.data;

import java.util.List;

public interface Data extends SubData {

	public List<Data> getSubData();

	/**
	 * Creates and returns a copy(1-1) of this object. The general intent is that, for any object {@code x}, the
	 * expression:
	 * <blockquote>
	 * 
	 * <pre>
	 * x.copy() != x
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.copy().getClass() == x.getClass()
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.copy().equals(x)
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * 
	 * @return a 1-1 copy of this instance.
	 */
	public Data copy();

	/**
	 * Creates and returns a clone of this object. The general intent is that, for any object {@code x}, the
	 * expression:
	 * <blockquote>
	 * 
	 * <pre>
	 * x.clone() != x
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.clone().getClass() == x.getClass()
	 * </pre>
	 * 
	 * </blockquote>
	 * will be true
	 * <blockquote>
	 * 
	 * <pre>
	 * x.clone().equals(x)
	 * </pre>
	 * 
	 * </blockquote>
	 * does <b>not</b> have to true!
	 * 
	 * @return a clone of this instance.
	 */
	public Data clone();

	public String getName();

	public void setName(String name);

	public Class<?> getDataTypeClass();
}
