package org.wildstang.subsystems;

import org.wildstang.config.DoubleConfigFileParameter;
import org.wildstang.inputmanager.base.IInput;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Liam
 */
public class WsCompressor extends Subsystem {
	protected final DoubleConfigFileParameter LOW_VOLTAGE_CONFIG, HIGH_VOLTAGE_CONFIG, MAX_PSI_CONFIG;
	protected double lowVoltage, highVoltage, maxPSI;

	protected Compressor compressor;

	public WsCompressor(String name, int pressureSwitchSlot, int pressureSwitchChannel, int compresssorRelaySlot, int compressorRelayChannel) {
		super(name);
		compressor = new Compressor();
		compressor.start();

		LOW_VOLTAGE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "LowVoltage", 0.5);
		HIGH_VOLTAGE_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "HighVoltage", 4.0);
		MAX_PSI_CONFIG = new DoubleConfigFileParameter(this.getClass().getName(), "MaxPSI", 115);

		lowVoltage = LOW_VOLTAGE_CONFIG.getValue();
		highVoltage = HIGH_VOLTAGE_CONFIG.getValue();
		maxPSI = MAX_PSI_CONFIG.getValue();
	}

	public void init() {
	}

	public void update() {
		IInput pressureTransducer = InputManager.getInstance().getSensorInput(InputManager.PRESSURE_TRANSDUCER_INDEX);

		double voltage = ((Double) pressureTransducer.get()).doubleValue();

		SmartDashboard.putNumber("Pressure Transducer Voltage", voltage);

		double psi = maxPSI * ((voltage - lowVoltage) / (highVoltage - lowVoltage));

		SmartDashboard.putNumber("PSI", psi);
	}

	public void notifyConfigChange() {
		lowVoltage = LOW_VOLTAGE_CONFIG.getValue();
		highVoltage = HIGH_VOLTAGE_CONFIG.getValue();
		maxPSI = MAX_PSI_CONFIG.getValue();
	}
}
