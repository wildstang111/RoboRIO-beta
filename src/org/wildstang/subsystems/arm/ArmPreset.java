/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.subsystems.arm;

import org.wildstang.config.IntegerConfigFileParameter;
import org.wildstang.subsystems.BallHandler;

/**
 *
 * @author Jason
 */
public class ArmPreset {

	public static int IGNORE_VALUE = 1000;
	protected IntegerConfigFileParameter wantedAngleMeasureFront_config, wantedAngleMeasureBack_config;
	protected int wantedAngleMeasureFront, wantedAngleMeasureBack;

	public ArmPreset(int wantedAngleMeasureFrontDefault, int wantedAngleMeasureBackDefault, String presetName) {

		this.wantedAngleMeasureBack_config = new IntegerConfigFileParameter(this.getClass().getName() + "." + presetName, "WantedBackArmAngle", wantedAngleMeasureBackDefault);
		this.wantedAngleMeasureFront_config = new IntegerConfigFileParameter(this.getClass().getName() + "." + presetName, "WantedFrontArmAngle", wantedAngleMeasureFrontDefault);

		this.wantedAngleMeasureBack = wantedAngleMeasureBack_config.getValue();
		this.wantedAngleMeasureFront = wantedAngleMeasureFront_config.getValue();
		BallHandler.presets.add(this);
	}

	public int getwantedAngleMeasureFront() {
		return wantedAngleMeasureFront;
	}

	public int getwantedAngleMeasureBack() {
		return wantedAngleMeasureBack;
	}

	public void notifyConfigChange() {
		this.wantedAngleMeasureBack = this.wantedAngleMeasureBack_config.getValue();
		this.wantedAngleMeasureFront = this.wantedAngleMeasureFront_config.getValue();
	}

	public String toString() {
		return "Wanted Front Arm Angle: " + this.wantedAngleMeasureFront + ", Wanted Back Arm Angle: " + this.wantedAngleMeasureBack;
	}

}