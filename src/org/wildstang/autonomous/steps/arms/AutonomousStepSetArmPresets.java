/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps.arms;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.subsystems.BallHandler;
import org.wildstang.subsystems.arm.ArmPreset;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetArmPresets extends AutonomousStep {

	protected ArmPreset preset;

	public AutonomousStepSetArmPresets(ArmPreset preset) {
		this.preset = preset;
	}

	public void initialize() {
		((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).setArmPreset(preset);
	}

	public void update() {
		if (!((BallHandler) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.BALL_HANDLER_INDEX)).areArmsUsingPidControl()) {
			finished = true;
		}
	}

	public String toString() {
		return "Moving Arms to preset: " + preset.toString();
	}

}
