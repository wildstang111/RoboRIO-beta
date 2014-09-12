package org.wildstang.subsystems;

import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickAxisEnum;
import org.wildstang.inputmanager.inputs.joystick.manipulator.ManipulatorJoystick;
import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Joey
 */
public class LightCannon extends Subsystem implements IObserver {

	private Relay.Value relayState = Relay.Value.kOff;

	public LightCannon(String name) {
		super(name);

		InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_DPAD_Y).attach(this);
	}

	public void init() {
		relayState = Relay.Value.kOff;
	}

	public void acceptNotification(Subject subjectThatCaused) {
		double value = ((DoubleSubject) subjectThatCaused).getValue();
		if (value > 0.5 || value < -0.5) {
			relayState = Relay.Value.kOn;
		} else {
			relayState = Relay.Value.kOff;
		}
		OutputManager.getInstance().getOutput(OutputManager.LIGHT_CANNON_RELAY_INDEX).set(relayState);
	}
}
