package org.wildstang.pid.inputs;

import org.wildstang.pid.inputs.base.IPidInput;
import org.wildstang.subsystems.DriveBase;
import org.wildstang.subsystems.base.SubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class DriveBaseHeadingPidInput implements IPidInput {

	public DriveBaseHeadingPidInput() {
		// Nothing to do here
	}

	public double pidRead() {
		double gyro_angle;
		gyro_angle = ((DriveBase) SubsystemContainer.getInstance().getSubsystem(SubsystemContainer.DRIVE_BASE_INDEX)).getGyroAngle();
		SmartDashboard.putNumber("Gyro angle: ", gyro_angle);
		return gyro_angle;
	}
}
