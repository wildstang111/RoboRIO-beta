/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps.drivebase;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.pid.controller.base.PidStateType;
import org.wildstang.subsystems.DriveBase;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class AutonomousStepStopDriveUsingMotionProfile extends AutonomousStep {

	public AutonomousStepStopDriveUsingMotionProfile() {
	}

	public void initialize() {
	}

	public void update() {
		((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).stopStraightMoveWithMotionProfile();
		finished = true;
	}

	public String toString() {
		return "Stop the drive using motion profile";
	}
}
