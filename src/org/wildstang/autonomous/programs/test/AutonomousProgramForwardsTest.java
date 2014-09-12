/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.programs.test;

import org.wildstang.autonomous.AutonomousProgram;
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
public class AutonomousProgramForwardsTest extends AutonomousProgram {

	public void defineSteps() {
		addStep(new AutonomousStepDriveManual(1.0, 0.0));
		addStep(new AutonomousStepDelay(500));
		addStep(new AutonomousStepDriveManual(0.0, 0.0));
	}

	public String toString() {
		return "Test by driving forwards for 10 seconds";
	}
}
