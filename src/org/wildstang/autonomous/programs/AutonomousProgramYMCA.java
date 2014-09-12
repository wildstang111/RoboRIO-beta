/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.programs;

import org.wildstang.autonomous.AutonomousProgram;
import org.wildstang.autonomous.steps.arms.AutonomousStepSetArmPresets;
import org.wildstang.autonomous.steps.control.AutonomousStepDelay;
import org.wildstang.subsystems.BallHandler;
import org.wildstang.subsystems.arm.ArmPreset;

/**
 *
 * @author Joey
 */
public class AutonomousProgramYMCA extends AutonomousProgram {
	protected ArmPreset yPreset = new ArmPreset(180, 180, "yPreset");
	protected ArmPreset mPreset = new ArmPreset(30, 30, "mPreset");
	protected ArmPreset cPreset = new ArmPreset(150, 270, "cPreset");
	protected ArmPreset aPreset = new ArmPreset(200, 200, "aPreset");

	protected void defineSteps() {
		addStep(new AutonomousStepSetArmPresets(yPreset));
		addStep(new AutonomousStepDelay(1500));
		addStep(new AutonomousStepSetArmPresets(mPreset));
		addStep(new AutonomousStepDelay(1500));
		addStep(new AutonomousStepSetArmPresets(cPreset));
		addStep(new AutonomousStepDelay(1500));
		addStep(new AutonomousStepSetArmPresets(aPreset));
		addStep(new AutonomousStepDelay(3000));
		addStep(new AutonomousStepSetArmPresets(BallHandler.DEFAULT_POSITION));
	}

	public String toString() {
		return "YMCA";
	}

}
