/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.pid.outputs;

import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.pid.outputs.base.IPidOutput;
import org.wildstang.subsystems.arm.Arm;

/**
 *
 * @author Joey
 */
public class ArmVictorPidOutput implements IPidOutput {
	protected int victorIndex;

	public ArmVictorPidOutput(int victorIndex) {
		this.victorIndex = victorIndex;
	}

	public void pidWrite(double output) {
		OutputManager.getInstance().getOutput(victorIndex).set(new Double(output));
	}

}
