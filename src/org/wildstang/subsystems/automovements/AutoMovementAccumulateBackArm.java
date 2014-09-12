/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.subsystems.automovements;

import org.wildstang.autonomous.steps.arms.AutonomousStepAccumulateBack;
import org.wildstang.autonomous.steps.arms.AutonomousStepSetArmPresets;
import org.wildstang.autonomous.steps.arms.AutonomousStepStopAccumulateBack;
import org.wildstang.autonomous.steps.control.AutonomousStepDelay;
import org.wildstang.config.IntegerConfigFileParameter;
import org.wildstang.subsystems.arm.ArmPreset;
import org.wildstang.subsystems.base.AutoMovement;

/**
 *
 * @author Alex
 */
public class AutoMovementAccumulateBackArm extends AutoMovement {
	protected IntegerConfigFileParameter delayTime = new IntegerConfigFileParameter(this.getClass().getName(), "AccumulateDelayTime", 2000);
	protected ArmPreset startPositionBackArmAccumulate = new ArmPreset(0, 90, "AutoMovementAccumulateBackArm.StartPosition");
	protected ArmPreset endPositionBackArmAccumulate = new ArmPreset(0, 0, "AutoMovementAccumulateBackArm.EndPosition");

	public void abort() {
		// not sure what to add here.
	}

	protected void defineSteps() {
		addStep(new AutonomousStepSetArmPresets(startPositionBackArmAccumulate));
		addStep(new AutonomousStepAccumulateBack());
		addStep(new AutonomousStepDelay(delayTime.getValue()));
		addStep(new AutonomousStepSetArmPresets(endPositionBackArmAccumulate));
		addStep(new AutonomousStepStopAccumulateBack());
	}

	public String toString() {
		return "Back Arm Accumulating";
	}

}
