/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.wildstang.autonomous.steps.arms;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.ISubjectEnum;

/**
 *
 * @author Nathan
 */
public class AutonomousStepWaitUntilBallInRobot extends AutonomousStep {

	public void initialize() {
		// Do nothing
	}

	public void update() {
		boolean ballInSwitchState = ((BooleanSubject) InputManager.getInstance().getSensorInput(InputManager.BALL_DETECT_SWITCH_INDEX).getSubject((ISubjectEnum) null)).getValue();
		if (true == ballInSwitchState) {
			finished = true;
		}
	}

	public String toString() {
		return "Wait for ball to be accumulated.";
	}

}
