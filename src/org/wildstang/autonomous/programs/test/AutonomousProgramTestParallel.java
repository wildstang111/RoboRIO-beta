/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.programs.test;

import org.wildstang.autonomous.AutonomousProgram;
import org.wildstang.autonomous.steps.AutonomousParallelStepGroup;
import org.wildstang.autonomous.steps.control.AutonomousStepDelay;
import org.wildstang.autonomous.steps.drivebase.AutonomousStepDriveManual;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
public class AutonomousProgramTestParallel extends AutonomousProgram {

	public void defineSteps() {
		AutonomousParallelStepGroup parallelGroup = new AutonomousParallelStepGroup("Test parallel step group");
		parallelGroup.addStep(new AutonomousStepDriveManual(AutonomousStepDriveManual.KEEP_PREVIOUS_STATE, 1.0));
		parallelGroup.addStep(new AutonomousStepDelay(250));
		parallelGroup.addStep(new AutonomousStepDriveManual(1.0, AutonomousStepDriveManual.KEEP_PREVIOUS_STATE));
		addStep(parallelGroup);
		addStep(new AutonomousStepDriveManual(0.0, 0.0));

	}

	public String toString() {
		return "Test Parallel Groups";
	}
}
