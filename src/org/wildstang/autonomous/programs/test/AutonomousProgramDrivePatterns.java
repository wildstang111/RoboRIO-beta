/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.programs.test;

import org.wildstang.autonomous.AutonomousManager;
import org.wildstang.autonomous.AutonomousProgram;
import org.wildstang.autonomous.steps.drivebase.AutonomousStepQuickTurn;
import org.wildstang.autonomous.steps.drivebase.AutonomousStepStartDriveUsingMotionProfile;
import org.wildstang.autonomous.steps.drivebase.AutonomousStepStopDriveUsingMotionProfile;
import org.wildstang.autonomous.steps.drivebase.AutonomousStepWaitForDriveMotionProfile;
import org.wildstang.config.DoubleConfigFileParameter;

/**
 *
 * @author Joey
 */
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
public class AutonomousProgramDrivePatterns extends AutonomousProgram {

	private DoubleConfigFileParameter firstAngle, secondAngle, firstDriveDistance, firstDriveVelocity, secondDriveDistance, secondDriveVelocity;

	public void defineSteps() {
		firstAngle = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstRelativeAngle", 45);
		secondAngle = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondRelativeAngle", 45);
		firstDriveDistance = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDriveDistance", -100);
		firstDriveVelocity = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDriveVelocity", 0.0);
		secondDriveDistance = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDriveDistance", -30);
		secondDriveVelocity = new DoubleConfigFileParameter(this.getClass().getName(), AutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDriveVelocity", 0.0);

		addStep(new AutonomousStepStartDriveUsingMotionProfile(firstDriveDistance.getValue(), firstDriveVelocity.getValue()));
		addStep(new AutonomousStepWaitForDriveMotionProfile());
		addStep(new AutonomousStepStopDriveUsingMotionProfile());
		addStep(new AutonomousStepQuickTurn(firstAngle.getValue()));
		addStep(new AutonomousStepStartDriveUsingMotionProfile(secondDriveDistance.getValue(), secondDriveVelocity.getValue()));
		addStep(new AutonomousStepWaitForDriveMotionProfile());
		addStep(new AutonomousStepStopDriveUsingMotionProfile());
		addStep(new AutonomousStepQuickTurn(secondAngle.getValue()));
	}

	public String toString() {
		return "Testing drive patterns for after shoot 5";
	}
}
