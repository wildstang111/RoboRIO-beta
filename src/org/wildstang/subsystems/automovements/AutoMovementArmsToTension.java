/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.subsystems.automovements;

import org.wildstang.autonomous.steps.arms.AutonomousStepSetArmPresets;
import org.wildstang.subsystems.BallHandler;
import org.wildstang.subsystems.arm.ArmPreset;
import org.wildstang.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementArmsToTension extends AutoMovement {
	public void abort() {
		// nothing needed here. PID handles it.
	}

	protected void defineSteps() {
		addStep(new AutonomousStepSetArmPresets(BallHandler.CATAPULT_TENSION_PRESET));
	}

	public String toString() {
		return "Arms to Tension";
	}

}
