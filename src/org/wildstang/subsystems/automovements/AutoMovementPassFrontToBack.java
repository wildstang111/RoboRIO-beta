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
public class AutoMovementPassFrontToBack extends AutoMovement {
	protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "DelayTime", 500);
	protected ArmPreset startPositionForFrontPasser = new ArmPreset(20, 90, "AutoMovementPassFrontToBack.StartPosition");
	protected ArmPreset endPositionForFrontPasser = new ArmPreset(-20, 90, "AutoMovementPassFrontToBack.EndPosition");

	public void abort() {
	}

	protected void defineSteps() {
		addStep(new AutonomousStepSetArmPresets(startPositionForFrontPasser));
		addStep(new AutonomousStepDelay(delayTime.getValue()));
		addStep(new AutonomousStepSetArmPresets(endPositionForFrontPasser));
	}

	public String toString() {
		return "Passing Front To Back";
	}

}
