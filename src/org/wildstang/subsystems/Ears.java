package org.wildstang.subsystems;

import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Sahil
 */
public class Ears extends Subsystem implements IObserver {

	boolean currentEarState = false;

	public Ears(String name) {
		super(name);
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_3);
	}

	public void init() {
		currentEarState = false;
	}

	public void update() {
		SmartDashboard.putBoolean("Ears are up", currentEarState);
		int earValue = 0;
		if (currentEarState == true) {
			earValue = DoubleSolenoid.Value.kReverse_val;
		} else {
			earValue = DoubleSolenoid.Value.kForward_val;
		}
		(OutputManager.getInstance().getOutput(OutputManager.EARS_SOLENOID_INDEX)).set(new Integer(earValue));
	}

	public void notifyConfigChange() {
	}

	public void acceptNotification(Subject subjectThatCaused) {

		if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_3) {
			if (((BooleanSubject) subjectThatCaused).getValue()) {
				currentEarState = true;
			}

			else {
				currentEarState = false;
			}
		}
	}

	public void setEarState(boolean currentEarState) {
		this.currentEarState = currentEarState;
	}

}