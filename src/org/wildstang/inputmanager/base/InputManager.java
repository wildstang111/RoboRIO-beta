package org.wildstang.inputmanager.base;

import java.util.ArrayList;
import java.util.List;

import org.wildstang.inputmanager.inputs.WsAnalogInput;
import org.wildstang.inputmanager.inputs.WsDigitalInput;
import org.wildstang.inputmanager.inputs.driverstation.WsDSAnalogInput;
import org.wildstang.inputmanager.inputs.driverstation.WsDSDigitalInput;
import org.wildstang.inputmanager.inputs.joystick.driver.DriverJoystick;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.inputmanager.inputs.joystick.manipulator.ManipulatorJoystick;
import org.wildstang.inputmanager.inputs.no.NoInput;
import org.wildstang.logger.Logger;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;

/**
 *
 * @author Nathan
 */
public class InputManager {

	private static InputManager instance = null;
	private static List<IInput> oiInputs = new ArrayList<>();
	private static List<IInput> sensorInputs = new ArrayList<>();

	/**
	 * Method to get the instance of this singleton object.
	 *
	 * @return The instance of InputManager
	 */
	public static InputManager getInstance() {
		if (instance == null) {
			instance = new InputManager();
		}
		return instance;
	}

	public void init() {
	}

	/**
	 * Method to trigger updates of all the sensor data input containers
	 */
	public void updateSensorData() {
		for (IInput sIn : sensorInputs) {
			if (sIn == null) {
				continue;
			}
			sIn.pullData();
			sIn.update();
		}
	}

	/**
	 * Method to trigger updates of all the oi data input containers.
	 */
	public void updateOiData() {
		for (IInput oiIn : oiInputs) {
			if (oiIn == null) {
				continue;
			}
			oiIn.pullData();
			oiIn.update();
		}
	}

	public void updateOiDataAutonomous() {
		for (IInput oiIn : oiInputs) {
			if (oiIn == null) {
				continue;
			}
			if (!(oiIn instanceof DriverJoystick || oiIn instanceof ManipulatorJoystick)) {
				oiIn.pullData();
			}
			oiIn.update();
		}
	}

	/**
	 * Method to notify all input containers that a config update occurred.
	 *
	 * Used by the ConfigFacade when the config is re-read.
	 */
	public void notifyConfigChange() {
		for (IInput sIn : sensorInputs) {
			if (sIn != null) {
				sIn.notifyConfigChange();
			}
		}
		for (IInput oiIn : oiInputs) {
			if (oiIn != null) {
				oiIn.notifyConfigChange();
			}
		}
	}

	/**
	 * Gets an OI container, based on a key.
	 *
	 * @param key
	 *            The key that represents the OI input container
	 * @return A WsInputInterface.
	 */
	public IInput getOiInput(int index) {
		if (index >= 0 && index < oiInputs.size()) {
			return (IInput) oiInputs.get(index);
		}
		return (IInput) oiInputs.get(UNKNOWN_INDEX);
	}

	/**
	 * Gets a sensor container, based on a key.
	 *
	 * @param key
	 *            The key that represents the sensor input container
	 * @return A WsInputInterface.
	 */
	public IInput getSensorInput(int index) {
		if (index >= 0 || index < sensorInputs.size()) {
			return (IInput) sensorInputs.get(index);
		}
		return (IInput) sensorInputs.get(UNKNOWN_INDEX);
	}

	final public void attachJoystickButton(IInputEnum button, IObserver observer) {
		if (button instanceof JoystickButtonEnum) {
			Subject subject = InputManager.getInstance().getOiInput(((JoystickButtonEnum) button).isDriver() ? InputManager.DRIVER_JOYSTICK_INDEX : InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(button);
			subject.attach(observer);
		} else {
			Logger.getLogger().debug(this.getClass().getName(), "attachJoystickButton", "Oops! Check that the inputs implement the required interfaces.");
		}
	}

	/**
	 * Keys to represent OI Inputs
	 */
	public static final int UNKNOWN_INDEX = 0;
	public static final int DRIVER_JOYSTICK_INDEX = 1;
	public static final int MANIPULATOR_JOYSTICK_INDEX = 2;
	public static final int AUTO_PROGRAM_SELECTOR_INDEX = 3;
	public static final int LOCK_IN_SWITCH_INDEX = 4;
	public static final int START_POSITION_SELECTOR_INDEX = 5;
	// Sensor Inputs
	public static final int PRESSURE_TRANSDUCER_INDEX = 1;
	public static final int FRONT_ARM_POT_INDEX = 2;
	public static final int BACK_ARM_POT_INDEX = 3;
	//public static final int LEFT_ENCODER_A_INDEX = 4;
	//public static final int LEFT_ENCODER_B_INDEX = 5;
	//public static final int RIGHT_ENCODER_A_INDEX = 6;
	//public static final int RIGHT_ENCODER_B_INDEX = 7;
	public static final int TENSION_LIMIT_SWITCH_INDEX = 4;
	public static final int BALL_DETECT_SWITCH_INDEX = 5;
	public static final int LATCH_POSITION_SWITCH_INDEX = 6;
	public static final int CATAPULT_DOWN_SWITCH_INDEX = 7;
	public static final int FRONT_ARM_CALIBRATION_SWITCH_INDEX = 8;
	public static final int BACK_ARM_CALIBRATION_SWITCH_INDEX = 9;

	/**
	 * Constructor for the InputManager.
	 *
	 * Each new data element to be added to the facade must be added here and
	 * have keys added above.
	 */
	protected InputManager() {
		// Add the facade data elements
		sensorInputs.add(UNKNOWN_INDEX, new NoInput());
		sensorInputs.add(PRESSURE_TRANSDUCER_INDEX, new WsAnalogInput(4));
		sensorInputs.add(FRONT_ARM_POT_INDEX, new WsAnalogInput(2));
		sensorInputs.add(BACK_ARM_POT_INDEX, new WsAnalogInput(3));
		 //sensorInputs.add(LEFT_ENCODER_A_INDEX, new WsDigitalInput(2));
		 //sensorInputs.add(LEFT_ENCODER_B_INDEX, new WsDigitalInput(3));
		 //sensorInputs.add(RIGHT_ENCODER_A_INDEX, new WsDigitalInput(4));
		 //sensorInputs.add(RIGHT_ENCODER_B_INDEX, new WsDigitalInput(5));
		sensorInputs.add(TENSION_LIMIT_SWITCH_INDEX, new WsDigitalInput(4));
		sensorInputs.add(BALL_DETECT_SWITCH_INDEX, new WsDigitalInput(10));
		sensorInputs.add(LATCH_POSITION_SWITCH_INDEX, new WsDigitalInput(6));
		sensorInputs.add(CATAPULT_DOWN_SWITCH_INDEX, new WsDigitalInput(7));
		sensorInputs.add(FRONT_ARM_CALIBRATION_SWITCH_INDEX, new WsDigitalInput(8));
		sensorInputs.add(BACK_ARM_CALIBRATION_SWITCH_INDEX, new WsDigitalInput(9));

		oiInputs.add(UNKNOWN_INDEX, new NoInput());
		oiInputs.add(DRIVER_JOYSTICK_INDEX, new DriverJoystick());
		oiInputs.add(MANIPULATOR_JOYSTICK_INDEX, new ManipulatorJoystick());
		oiInputs.add(AUTO_PROGRAM_SELECTOR_INDEX, new WsDSAnalogInput(1));
		oiInputs.add(LOCK_IN_SWITCH_INDEX, new WsDSDigitalInput(1));
		oiInputs.add(START_POSITION_SELECTOR_INDEX, new WsDSAnalogInput(2));

	}
}
