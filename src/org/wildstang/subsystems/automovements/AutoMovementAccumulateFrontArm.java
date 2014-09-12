/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.subsystems.automovements;

import org.wildstang.autonomous.steps.arms.AutonomousStepAccumulateFront;
import org.wildstang.autonomous.steps.arms.AutonomousStepSetArmPresets;
import org.wildstang.autonomous.steps.arms.AutonomousStepStopAccumulateFront;
import org.wildstang.autonomous.steps.control.AutonomousStepDelay;
import org.wildstang.config.IntegerConfigFileParameter;
import org.wildstang.subsystems.arm.ArmPreset;
import org.wildstang.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementAccumulateFrontArm extends AutoMovement {
	protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "AccumulateDelayTime", 2000);
	protected ArmPreset startPositionFrontAccumulate = new ArmPreset(90, 0, "AutoMovementAccumulateFrontArm.StartPosition");
	protected ArmPreset endPositionFrontAccumulate = new ArmPreset(0, 0, "AutoMovementAccumulateFrontArm.EndPosition");

	public void abort() {
		// not sure what to put here.
	}

	protected void defineSteps() {
		addStep(new AutonomousStepSetArmPresets(startPositionFrontAccumulate));
		addStep(new AutonomousStepAccumulateFront());
		addStep(new AutonomousStepDelay(delayTime.getValue()));
		// addStep(new AutonomousStepSetArmPresets(endPositionFrontAccumulate));
		addStep(new AutonomousStepStopAccumulateFront());
	}

	public String toString() {
		return "Front Arm Accumulating";
	}

}
