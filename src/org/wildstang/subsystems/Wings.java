package org.wildstang.subsystems;

import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickAxisEnum;
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
 * @author mail929
 */
public class Wings extends Subsystem implements IObserver {
	boolean currentState = false;

	public Wings(String name) {
		super(name);
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_1);
	}

	public void init() {
		currentState = false;
	}

	public void update() {
		int wingsValue = 0;
		if (currentState == true) {
			wingsValue = DoubleSolenoid.Value.kReverse_val;
		} else {
			wingsValue = DoubleSolenoid.Value.kForward_val;
		}
		(OutputManager.getInstance().getOutput(OutputManager.WINGS_SOLENOID_INDEX)).set(new Integer(wingsValue));
		SmartDashboard.putBoolean("Wing State", currentState);
	}

	public void notifyConfigChange() {
	}

	public void acceptNotification(Subject subjectThatCaused) {
		if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_1) {
			if (((BooleanSubject) subjectThatCaused).getValue()) {
				currentState = true;
			} else {
				currentState = false;
			}
		}
	}
}
