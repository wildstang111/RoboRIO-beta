/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps.ears;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.subsystems.Ears;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Jason
 */
public class AutonomousStepSetEarState extends AutonomousStep {

	public void initialize() {

		((Ears) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.EAR_INDEX)).setEarState(earPreset);
		finished = true;
	}

	public void update() {

	}

	public String toString() {

		return "Setting Ear State";

	}

	public AutonomousStepSetEarState(boolean earPreset) {
		this.earPreset = earPreset;

	}

	boolean earPreset;

}
