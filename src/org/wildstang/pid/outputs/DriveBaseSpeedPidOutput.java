/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.pid.outputs;

import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.pid.outputs.base.IPidOutput;
import org.wildstang.subsystems.DriveBase;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Nathan
 */
public class DriveBaseSpeedPidOutput implements IPidOutput {

	public DriveBaseSpeedPidOutput() {
		// Nothing to do here
	}

	public void pidWrite(double output) {
		((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).setPidSpeedValue(output);
	}
}
