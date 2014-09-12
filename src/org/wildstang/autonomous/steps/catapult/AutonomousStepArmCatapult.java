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
public class AutonomousStepArmCatapult extends AutonomousStep {
	BooleanSubject button = null;

	public void initialize() {
		Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickButtonEnum.MANIPULATOR_BUTTON_2);
		button = (BooleanSubject) subject;
		button.setValue(true);
	}

	public void update() {
		Catapult catapult = (Catapult) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.CATAPULT_INDEX);
		boolean tensionState = catapult.isTension();
		if (tensionState == true) {
			button.setValue(false);
			this.finished = true;
		}
	}

	public String toString() {
		return "moving catapult to ready to fire position.";
	}

}
