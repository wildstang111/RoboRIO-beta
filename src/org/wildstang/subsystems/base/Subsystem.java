/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.subsystems.base;

import org.wildstang.inputmanager.base.IInputEnum;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.logger.Logger;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.ISubjectEnum;

/**
 *
 * @author Nathan
 */
public class Subsystem {

	static String subSystemName;

	/**
	 * Constructor for a subsystem.
	 *
	 * @param name
	 *            The name of the subsystem.
	 */
	public Subsystem(String name) {
		subSystemName = name;
	}

	public void init() {
	}

	/**
	 * Gets the name of the subsystem.
	 *
	 * @return The name of the subsystem.
	 */
	public String getName() {
		return subSystemName;
	}

	/**
	 * Method to allow the subsystem to update.
	 *
	 * Must be overridden when extending the base class.
	 */
	public void update() {
		// Override when extending base class.
	}

	/**
	 * Method to notify the subsystem of a config change.
	 *
	 * Override this method when extending the base class, if config params are
	 * required.
	 */
	public void notifyConfigChange() {
		// Override when extending base class if config is needed.
	}

	public void registerForJoystickButtonNotification(IInputEnum button) {
		try {
			InputManager.getInstance().attachJoystickButton(button, (IObserver) this);
		} catch (ClassCastException e) {
			Logger.getLogger().debug(this.getClass().getName(), "registerForJoystickButtonNotification", "This class must implement IObserver!");
		}
	}

	public void registerForSensorNotification(int sensorIndex) {
		try {
			InputManager.getInstance().getSensorInput(sensorIndex).getSubject().attach((IObserver) this);
		} catch (Exception e) {
			Logger.getLogger().debug(this.getClass().getName(), "registerForSensorNotification", "This class must implement IObserver!");
		}
	}
}
