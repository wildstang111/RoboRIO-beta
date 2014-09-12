/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.subsystems.automovements;

import org.wildstang.autonomous.steps.arms.AutonomousStepSetArmPresets;
import org.wildstang.autonomous.steps.control.AutonomousStepDelay;
import org.wildstang.config.IntegerConfigFileParameter;
import org.wildstang.subsystems.arm.ArmPreset;
import org.wildstang.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementPassBackToFront extends AutoMovement {
	protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "DelayTime", 500);
	protected ArmPreset startPositionForBackPasser = new ArmPreset(90, 20, "AutoMovementPassBackToFront.StartPosition");
	protected ArmPreset endPositionForBackPasser = new ArmPreset(90, -20, "AutoMovementPassBackToFront.EndPosition");

	public void abort() {
	}

	protected void defineSteps() {
		addStep(new AutonomousStepSetArmPresets(startPositionForBackPasser));
		addStep(new AutonomousStepDelay(delayTime.getValue()));
		addStep(new AutonomousStepSetArmPresets(endPositionForBackPasser));
	}

	public String toString() {
		return "Passing Back To Front";
	}
}
