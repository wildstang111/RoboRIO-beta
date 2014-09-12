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
import org.wildstang.subsystems.BallHandler;
import org.wildstang.subsystems.arm.ArmRollerEnum;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class AutonomousStepStopAccumulateFront extends AutonomousStep {

	public void initialize() {
		((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setFrontArmAccumulator(ArmRollerEnum.OFF);
		finished = true;
	}

	public void update() {

	}

	public String toString() {
		return "Stop Accumulate with the front roller";
	}

}
