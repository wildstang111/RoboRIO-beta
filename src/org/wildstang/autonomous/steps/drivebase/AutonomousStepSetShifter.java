/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps.drivebase;

import org.wildstang.autonomous.steps.AutonomousStep;
import org.wildstang.subsystems.DriveBase;
import org.wildstang.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Joey
 */
public class AutonomousStepSetShifter extends AutonomousStep {
	protected DoubleSolenoid.Value state;

	public AutonomousStepSetShifter(DoubleSolenoid.Value state) {
		this.state = state;
	}

	public void initialize() {
		((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).setShifter(state);
		this.finished = true;
	}

	public void update() {
	}

	public String toString() {
		return "Set Shifter State";
	}

}
