/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wildstang.autonomous.steps.arms;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.Subject;

/**
 *
 * @author Nathan
 */
public class AutonomousStepAccumulateBack extends AutonomousStep {

	public void initialize() {
		Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickButtonEnum.MANIPULATOR_BUTTON_6);
		((BooleanSubject) subject).setValue(true);
		finished = true;
	}

	public void update() {

	}

	public String toString() {
		return "Accumulate with the front roller";
	}

}
