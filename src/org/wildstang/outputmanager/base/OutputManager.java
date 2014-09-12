package org.wildstang.outputmanager.base;

import java.util.ArrayList;
import java.util.List;

import org.wildstang.outputmanager.outputs.WsDoubleSolenoid;
import org.wildstang.outputmanager.outputs.WsDriveSpeed;
import org.wildstang.outputmanager.outputs.WsRelay;
import org.wildstang.outputmanager.outputs.WsSolenoid;
import org.wildstang.outputmanager.outputs.WsVictor;
import org.wildstang.outputmanager.outputs.no.NoOutput;

import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Nathan
 */
public class OutputManager {

	private static OutputManager instance = null;
	private static List<IOutput> outputs = new ArrayList<>();

	/**
	 * Method to obtain the instance of the OutputManager singleton.
	 *
	 * @return the instance of the OutputManager.
	 */
	public static OutputManager getInstance() {
		if (OutputManager.instance == null) {
			OutputManager.instance = new OutputManager();
		}
		return OutputManager.instance;
	}

	/**
	 * Method to cause all output elements to update.
	 */
	public void init() {
	}

	public void update() {
		for (IOutput out : outputs) {
			if (out != null) {
				out.update();
			}
		}
	}

	/**
	 * Method to notify all output elements that a config change has occurred
	 * and config values need to be re-read.
	 */
	public void notifyConfigChange() {
		for (IOutput out : outputs) {
			if (out != null) {
				out.notifyConfigChange();
			}
		}
	}

	/**
	 * Gets an output element based on a key.
	 *
	 * @param index
	 *            A string representation of the output element.
	 *
	 * @return The output element.
	 */
	public IOutput getOutput(int index) {
		if (index >= 0 && index < outputs.size()) {
			return (IOutput) outputs.get(index);
		}
		return (IOutput) outputs.get(UNKNOWN_INDEX);
	}

	// Key Values - Need to update for each new output element.
	public static final int UNKNOWN_INDEX = 0;
	public static final int RIGHT_DRIVE_SPEED_INDEX = 1;
	public static final int LEFT_DRIVE_SPEED_INDEX = 2;
	public static final int SHIFTER_INDEX = 3;
	public static final int LIGHT_CANNON_RELAY_INDEX = 4;
	public static final int WINGS_SOLENOID_INDEX = 5;
	public static final int LANDING_GEAR_SOLENOID_INDEX = 6;
	public static final int CATAPAULT_SOLENOID_INDEX = 7;
	public static final int EARS_SOLENOID_INDEX = 8;
	public static final int FRONT_ARM_VICTOR_INDEX = 9;
	public static final int BACK_ARM_VICTOR_INDEX = 10;
	public static final int FRONT_ARM_ROLLER_VICTOR_INDEX = 11;
	public static final int BACK_ARM_ROLLER_VICTOR_INDEX = 12;
	public static final int LATCH_SOLENOID_INDEX = 13;

	/**
	 * Constructor for OutputManager.
	 *
	 * All new output elements need to be added in the constructor as well as
	 * having a key value added above.
	 */

	protected OutputManager() {
		// Add the facade data elements
		outputs.add(UNKNOWN_INDEX, new NoOutput());
		outputs.add(RIGHT_DRIVE_SPEED_INDEX, new WsDriveSpeed("Right Drive Speed", 3, 4));
		outputs.add(LEFT_DRIVE_SPEED_INDEX, new WsDriveSpeed("Left Drive Speed", 1, 2));
		outputs.add(SHIFTER_INDEX, new WsDoubleSolenoid("Shifter", 1, 1, 2));
		outputs.add(LIGHT_CANNON_RELAY_INDEX, new WsRelay(2, Relay.Direction.kForward));
		outputs.add(WINGS_SOLENOID_INDEX, new WsDoubleSolenoid("Wings Solenoid1", 1, 3, 4));
		outputs.add(LANDING_GEAR_SOLENOID_INDEX, new WsDoubleSolenoid("Landing Gear Solenoid", 1, 7, 8));
		outputs.add(CATAPAULT_SOLENOID_INDEX, new WsSolenoid("Arm Catapult Solenoid", 1, 5));
		 outputs.add(EARS_SOLENOID_INDEX, new WsDoubleSolenoid("Ears Double Solenoid", 2, 1, 2));
		outputs.add(FRONT_ARM_VICTOR_INDEX, new WsVictor("Front Arm Victor", 5));
		outputs.add(BACK_ARM_VICTOR_INDEX, new WsVictor("Back Arm Victor", 6));
		outputs.add(FRONT_ARM_ROLLER_VICTOR_INDEX, new WsVictor("Front Arm Roller Victor", 7));
		outputs.add(BACK_ARM_ROLLER_VICTOR_INDEX, new WsVictor("Back Arm Roller Victor", 8));
		outputs.add(LATCH_SOLENOID_INDEX, new WsSolenoid("Latch Solenoid", 1, 6));
	}
}
