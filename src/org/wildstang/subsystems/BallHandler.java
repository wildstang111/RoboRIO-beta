package org.wildstang.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickAxisEnum;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subjects.debouncer.Debouncer;
import org.wildstang.subsystems.arm.Arm;
import org.wildstang.subsystems.arm.ArmPreset;
import org.wildstang.subsystems.arm.ArmRollerEnum;
import org.wildstang.subsystems.base.Subsystem;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author mail929
 */
public class BallHandler extends Subsystem implements IObserver {

	public static List<ArmPreset> presets = new ArrayList<>();
	public static final ArmPreset DEFAULT_POSITION = new ArmPreset(0, 0, "DefaultPosition");
	public static final ArmPreset CATAPULT_TENSION_PRESET = new ArmPreset(20, 20, "TensionPreset");

	public static final ArmPreset FRONT_ARM_ONLY_90 = new ArmPreset(90, ArmPreset.IGNORE_VALUE, "FrontArmOnly90");
	public static final ArmPreset FRONT_ARM_ONLY_45 = new ArmPreset(45, ArmPreset.IGNORE_VALUE, "FrontArmOnly45");
	public static final ArmPreset FRONT_ARM_ONLY_135 = new ArmPreset(135, ArmPreset.IGNORE_VALUE, "FrontArmOnly135");
	public static final ArmPreset FRONT_ARM_ONLY_NEG15 = new ArmPreset(-15, ArmPreset.IGNORE_VALUE, "FrontArmOnly-15");
	public static final ArmPreset FRONT_ARM_ONLY_0 = new ArmPreset(0, ArmPreset.IGNORE_VALUE, "FrontArmOnly0");
	public static final ArmPreset BACK_ARM_ONLY_45 = new ArmPreset(ArmPreset.IGNORE_VALUE, 45, "BackArmOnly45");
	public static final ArmPreset BACK_ARM_ONLY_90 = new ArmPreset(ArmPreset.IGNORE_VALUE, 90, "BackArmOnly90");
	public static final ArmPreset BACK_ARM_ONLY_135 = new ArmPreset(ArmPreset.IGNORE_VALUE, 135, "BackArmOnly135");
	public static final ArmPreset BACK_ARM_ONLY_NEG15 = new ArmPreset(ArmPreset.IGNORE_VALUE, -15, "BackArmOnly-15");
	public static final ArmPreset BACK_ARM_ONLY_0 = new ArmPreset(ArmPreset.IGNORE_VALUE, 0, "BackArmOnly0");

	protected Arm frontArm, backArm;
	protected boolean frontForwardButton = false, frontReverseButton = false;
	protected boolean backForwardButton = false, backReverseButton = false;
	protected double frontArmJoystickValue = 0.0, backArmJoystickValue = 0.0;
	protected double lastValueFront = 0.0, lastValueBack = 0.0;
	protected Debouncer frontArmCalibrationDebouncer, backArmCalibrationDebouncer;

	public BallHandler(String name) {
		super(name);

		this.frontArm = new Arm(OutputManager.FRONT_ARM_VICTOR_INDEX, OutputManager.FRONT_ARM_ROLLER_VICTOR_INDEX, InputManager.FRONT_ARM_POT_INDEX, true);
		this.backArm = new Arm(OutputManager.BACK_ARM_VICTOR_INDEX, OutputManager.BACK_ARM_ROLLER_VICTOR_INDEX, InputManager.BACK_ARM_POT_INDEX, false);

		this.frontArmCalibrationDebouncer = new Debouncer(60, new Boolean(true));
		this.backArmCalibrationDebouncer = new Debouncer(60, new Boolean(true));

		this.frontArmCalibrationDebouncer.attach(this);
		this.backArmCalibrationDebouncer.attach(this);

		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_5);
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_6);
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_7);
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_8);
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_1);
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_2);

		Subject subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_BACK_ARM_CONTROL);
		subject.attach(this);

		subject = InputManager.getInstance().getOiInput(InputManager.MANIPULATOR_JOYSTICK_INDEX).getSubject(JoystickAxisEnum.MANIPULATOR_FRONT_ARM_CONTROL);
		subject.attach(this);

		registerForSensorNotification(InputManager.FRONT_ARM_CALIBRATION_SWITCH_INDEX);
		registerForSensorNotification(InputManager.BACK_ARM_CALIBRATION_SWITCH_INDEX);
	}

	public void init() {
		frontForwardButton = false;
		frontReverseButton = false;
		backForwardButton = false;
		backReverseButton = false;
		frontArmJoystickValue = 0.0;
		backArmJoystickValue = 0.0;
		lastValueFront = 0.0;
		lastValueBack = 0.0;
		frontArm.init();
		backArm.init();
	}

	public void update() {
		// Both pressed or both are not pressed
		if (frontForwardButton == frontReverseButton) {
			frontArm.setRoller(ArmRollerEnum.OFF);
		} else if (frontForwardButton) {
			frontArm.setRoller(ArmRollerEnum.INTAKE);
		} else if (frontReverseButton) {
			frontArm.setRoller(ArmRollerEnum.OUTPUT);
		}

		// Both pressed or both are not pressed
		if (backForwardButton == backReverseButton) {
			backArm.setRoller(ArmRollerEnum.OFF);
		} else if (backForwardButton) {
			backArm.setRoller(ArmRollerEnum.INTAKE);
		} else if (backReverseButton) {
			backArm.setRoller(ArmRollerEnum.OUTPUT);
		}

		frontArm.setVictor(frontArmJoystickValue);
		backArm.setVictor(backArmJoystickValue);

		frontArm.update();
		backArm.update();

		ArmRollerEnum frontValue = frontArm.getRollerValue();
		ArmRollerEnum backValue = backArm.getRollerValue();
		String frontString = frontValue == ArmRollerEnum.INTAKE ? "Forward" : (frontValue == ArmRollerEnum.OUTPUT ? "Reverse" : "Off");
		String backString = backValue == ArmRollerEnum.INTAKE ? "Forward" : (backValue == ArmRollerEnum.OUTPUT ? "Reverse" : "Off");

		double voltageChangeFront = frontArm.getPotVoltage() - lastValueFront;
		double voltageChangeBack = backArm.getPotVoltage() - lastValueBack;

		lastValueFront = frontArm.getPotVoltage();
		lastValueBack = backArm.getPotVoltage();

		this.frontArmCalibrationDebouncer.update(InputManager.getInstance().getSensorInput(InputManager.FRONT_ARM_CALIBRATION_SWITCH_INDEX).get());
		this.backArmCalibrationDebouncer.update(InputManager.getInstance().getSensorInput(InputManager.BACK_ARM_CALIBRATION_SWITCH_INDEX).get());

		SmartDashboard.putNumber("Current Front Arm Angle", frontArm.getCurrentAngle());
		SmartDashboard.putNumber("Wanted Front Arm Angle", frontArm.getWantedAngle());
		SmartDashboard.putNumber("Current Back Arm Angle", backArm.getCurrentAngle());
		SmartDashboard.putNumber("Wanted Back Arm Angle", backArm.getWantedAngle());
		SmartDashboard.putString("Front Arm Roller", frontString);
		SmartDashboard.putString("Back Arm Roller", backString);
		SmartDashboard.putNumber("Front Arm Victor", frontArm.getVictorSpeed());
		SmartDashboard.putNumber("Back Arm Victor", backArm.getVictorSpeed());
		SmartDashboard.putNumber("Front Arm Joystick", frontArmJoystickValue);
		SmartDashboard.putNumber("Back Arm Joystick", backArmJoystickValue);
		SmartDashboard.putBoolean("Front Arm At Zero", !((Boolean) this.frontArmCalibrationDebouncer.getDebouncedValue()).booleanValue());
		SmartDashboard.putBoolean("Back Arm At Zero", !((Boolean) this.backArmCalibrationDebouncer.getDebouncedValue()).booleanValue());
	}

	public void notifyConfigChange() {
		Arm.notifyConfigChangeStatic();
		frontArm.notifyConfigChange();
		backArm.notifyConfigChange();
		for (ArmPreset preset : presets) {
			if (preset != null) {
				preset.notifyConfigChange();
			}
		}

	}

	public void acceptNotification(Subject subjectThatCaused) {
		if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_5) {
			this.frontForwardButton = ((BooleanSubject) subjectThatCaused).getValue();
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_6) {
			this.backForwardButton = ((BooleanSubject) subjectThatCaused).getValue();
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_7) {
			this.frontReverseButton = ((BooleanSubject) subjectThatCaused).getValue();
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_8) {
			this.backReverseButton = ((BooleanSubject) subjectThatCaused).getValue();
		} else if (subjectThatCaused.getType() == JoystickAxisEnum.MANIPULATOR_FRONT_ARM_CONTROL) {
			this.frontArmJoystickValue = ((DoubleSubject) subjectThatCaused).getValue() * -1;
		} else if (subjectThatCaused.getType() == JoystickAxisEnum.MANIPULATOR_BACK_ARM_CONTROL) {
			this.backArmJoystickValue = ((DoubleSubject) subjectThatCaused).getValue() * -1;
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_1) {
			if (((BooleanSubject) subjectThatCaused).getValue()) {
				setArmPreset(FRONT_ARM_ONLY_90);
			}
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_2) {
			if (((BooleanSubject) subjectThatCaused).getValue()) {
				setArmPreset(FRONT_ARM_ONLY_0);
			}
		} else if (subjectThatCaused == this.frontArmCalibrationDebouncer) {
			// The limit switches return false when pressed (Electrical)
			if (!((Boolean) this.frontArmCalibrationDebouncer.getDebouncedValue()).booleanValue()) {
				System.out.println("Calibrating Front Arm Now");
				frontArm.calibrate(true);
			}
		} else if (subjectThatCaused == this.backArmCalibrationDebouncer) {
			// The limit switches return false when pressed (Electrical)
			if (!((Boolean) this.backArmCalibrationDebouncer.getDebouncedValue()).booleanValue()) {
				System.out.println("Calibrating Back Arm Now");
				backArm.calibrate(true);
			}
		}
	}

	public void setArmPreset(ArmPreset preset) {
		int frontArmPreset = preset.getwantedAngleMeasureFront();
		int backArmPreset = preset.getwantedAngleMeasureBack();
		if (frontArmPreset <= frontArm.getHighBound() && frontArmPreset >= frontArm.getLowBound()) {
			this.frontArm.setToAngle(frontArmPreset);
		}
		if (backArmPreset <= backArm.getHighBound() && backArmPreset >= backArm.getLowBound()) {
			this.backArm.setToAngle(backArmPreset);
		}
	}

	public boolean areArmsUsingPidControl() {
		return (frontArm.isArmPidActive() || backArm.isArmPidActive());
	}

	public void setFrontArmAccumulator(ArmRollerEnum state) {
		frontArm.setRoller(state);
		if (state == ArmRollerEnum.INTAKE) {
			frontForwardButton = true;
			frontReverseButton = false;
		} else if (state == ArmRollerEnum.OUTPUT) {
			frontForwardButton = false;
			frontReverseButton = true;
		} else if (state == ArmRollerEnum.OFF) {
			frontForwardButton = false;
			frontReverseButton = false;
		}
	}

	public void setBackArmAccumulator(ArmRollerEnum state) {
		backArm.setRoller(state);
		if (state == ArmRollerEnum.INTAKE) {
			backForwardButton = true;
			backReverseButton = false;
		} else if (state == ArmRollerEnum.OUTPUT) {
			backForwardButton = false;
			backReverseButton = true;
		} else if (state == ArmRollerEnum.OFF) {
			backForwardButton = false;
			backReverseButton = false;
		}
	}
}
