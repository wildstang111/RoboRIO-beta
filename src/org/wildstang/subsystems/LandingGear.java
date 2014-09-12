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
 * @author mail929
 */
public class LandingGear extends Subsystem implements IObserver {
	boolean landingGearState = false;

	public LandingGear(String name) {
		super(name);
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_5);
	}

	public void init() {
		landingGearState = false;
	}

	public void update() {
		SmartDashboard.putBoolean("LandingGearState", landingGearState);

		int earValue = 0;
		if (landingGearState == true) {
			earValue = DoubleSolenoid.Value.kReverse_val;
		} else {
			earValue = DoubleSolenoid.Value.kForward_val;
		}
		(OutputManager.getInstance().getOutput(OutputManager.LANDING_GEAR_SOLENOID_INDEX)).set(new Integer(earValue));
	}

	public void acceptNotification(Subject subjectThatCaused) {
		if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_5) {
			if (((BooleanSubject) subjectThatCaused).getValue()) {
				landingGearState = true;
			} else {
				landingGearState = false;
			}
		}
	}

	public void notifyConfigChange() {

	}
}
