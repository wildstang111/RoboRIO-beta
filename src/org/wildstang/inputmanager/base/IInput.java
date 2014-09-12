package org.wildstang.inputmanager.base;

import org.wildstang.subjects.base.IObservable;

/**
 *
 * @author Nathan
 */
public interface IInput extends IObservable {

	/**
	 * Method to set values in the data element.
	 *
	 * @param key
	 *            An enumerated key that to get to a specific value.
	 * @param value
	 *            The value to set to.
	 */
	public void set(IInputEnum key, Object value);

	public void set(Object value);

	/**
	 * Method to get a value from the data element.
	 *
	 * @param key
	 *            An enumerated key that represents the value wanted.
	 * @return An Object value of the element.
	 */
	public Object get(IInputEnum key);

	public Object get();

	/**
	 * Method to force a periodic update of the data element.
	 */
	public void update();

	/**
	 * Method to pull input info for the data element.
	 */
	public void pullData();

	/**
	 * Method to notify the data element that config data has changed and needs
	 * to be re-read.
	 */
	public void notifyConfigChange();
}
