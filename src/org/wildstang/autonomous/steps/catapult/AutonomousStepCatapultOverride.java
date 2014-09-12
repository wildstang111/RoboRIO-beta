/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps.catapult;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subsystems.Catapult;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Alex
 */
public class AutonomousStepCatapultOverride extends AutonomousStep {
	BooleanSubject button = null;

	public void initialize() {
		Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickButtonEnum.MANIPULATOR_BUTTON_10);
		button = (BooleanSubject) subject;
		button.setValue(true);
		this.finished = true;
	}

	public void update() {
	}

	public String toString() {
		return "Set Override";
	}

}
