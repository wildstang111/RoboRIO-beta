package org.wildstang.subsystems;

import org.wildstang.autonomous.parameters.AutonomousBooleanConfigFileParameter;
import org.wildstang.autonomous.parameters.AutonomousDoubleConfigFileParameter;
import org.wildstang.config.BooleanConfigFileParameter;
import org.wildstang.config.DoubleConfigFileParameter;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickAxisEnum;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.logger.Logger;
import org.wildstang.motionprofile.ContinuousAccelFilter;
import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.pid.controller.base.SpeedPidController;
import org.wildstang.pid.inputs.DriveBaseSpeedPidInput;
import org.wildstang.pid.outputs.DriveBaseSpeedPidOutput;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subsystems.base.Subsystem;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Smitty
 */
public class DriveBase extends Subsystem implements IObserver {

	private static final double MAX_INPUT_THROTTLE_VALUE = 1.0;
	private static final double MAX_INPUT_HEADING_VALUE = 1.0;
	private static final double HEADING_SENSITIVITY = 1.8;
	private static final double MAX_MOTOR_OUTPUT = 1.0;
	private static final double ANTI_TURBO_MAX_DEFLECTION = 0.500;
	private static double THROTTLE_LOW_GEAR_ACCEL_FACTOR = 0.250;
	private static double HEADING_LOW_GEAR_ACCEL_FACTOR = 0.500;
	private static double THROTTLE_HIGH_GEAR_ACCEL_FACTOR = 0.125;
	private static double HEADING_HIGH_GEAR_ACCEL_FACTOR = 0.250;
	private static double TICKS_PER_ROTATION = 360.0;
	private static double WHEEL_DIAMETER = 6;
	private static double MAX_HIGH_GEAR_PERCENT = 0.80;
	private static double ENCODER_GEAR_RATIO = 7.5;
	private static double DEADBAND = 0.05;
	private static double SLOW_TURN_FORWARD_SPEED;
	private static double SLOW_TURN_BACKWARD_SPEED;
	private static double MAX_ACCELERATION_DRIVE_PROFILE = 600.0;
	private static double STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR = 10.0;
	private static double QUICK_TURN_CAP;
	private static double QUICK_TURN_ANTITURBO;
	private static double driveBaseThrottleValue = 0.0;
	private static double driveBaseHeadingValue = 0.0;
	private static boolean antiTurboFlag = false;
	private static boolean turboFlag = false;
	private static DoubleSolenoid.Value shifterFlag = DoubleSolenoid.Value.kForward; // Default
																						// to
																						// low
																						// gear
	private static boolean quickTurnFlag = false;
	private static Encoder leftDriveEncoder;
	private static Encoder rightDriveEncoder;
	private static Gyro driveHeadingGyro;
	private static ContinuousAccelFilter continuousAccelerationFilter;
	// Set low gear top speed to 8.5 ft/ second = 102 inches / second = 2.04
	// inches/ 20 ms
	private static double MAX_SPEED_INCHES_LOWGEAR = 90.0;
	private double goal_velocity = 0.0;
	private double distance_to_move = 0.0;
	private double distance_moved = 0.0;
	private double distance_remaining = 0.0;
	private boolean motionProfileActive = false;
	private double currentProfileX = 0.0;
	private double currentProfileV = 0.0;
	private double currentProfileA = 0.0;
	private static double FEED_FORWARD_VELOCITY_CONSTANT = 1.00;
	private static double FEED_FORWARD_ACCELERATION_CONSTANT = 0.00018;
	private double totalPosition = 0.0;
	private double previousPositionSinceLastReset = 0.0;
	private double previousRightPositionSinceLastReset = 0.0;
	private double previousLeftPositionSinceLastReset = 0.0;
	private static double pidSpeedValue = 0.0;
	private static SpeedPidController driveSpeedPid;
	private static DriveBaseSpeedPidInput driveSpeedPidInput;
	private static DriveBaseSpeedPidOutput driveSpeedPidOutput;
	private double deltaPosition = 0.0;
	private double deltaTime = 0.0;
	private double deltaPosError = 0.0;
	private double deltaProfilePosition = 0.0;
	private double previousTime = 0.0;
	private double previousVelocity = 0.0;
	private double currentVelocity = 0.0;
	private double currentAcceleration = 0.0;
	private double DECELERATION_VELOCITY_THRESHOLD = 48; // Velocity in in/sec
	private double DECELERATION_MOTOR_SPEED = 0.3;
	private static boolean driveDistancePidEnabled = false;
	private static DoubleConfigFileParameter WHEEL_DIAMETER_config;
	private static DoubleConfigFileParameter TICKS_PER_ROTATION_config;
	private static DoubleConfigFileParameter THROTTLE_LOW_GEAR_ACCEL_FACTOR_config;
	private static DoubleConfigFileParameter HEADING_LOW_GEAR_ACCEL_FACTOR_config;
	private static DoubleConfigFileParameter THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config;
	private static DoubleConfigFileParameter HEADING_HIGH_GEAR_ACCEL_FACTOR_config;
	private static DoubleConfigFileParameter MAX_HIGH_GEAR_PERCENT_config;
	private static DoubleConfigFileParameter ENCODER_GEAR_RATIO_config;
	private static DoubleConfigFileParameter DEADBAND_config;
	private static DoubleConfigFileParameter SLOW_TURN_FORWARD_SPEED_config;
	private static DoubleConfigFileParameter SLOW_TURN_BACKWARD_SPEED_config;
	private static DoubleConfigFileParameter FEED_FORWARD_VELOCITY_CONSTANT_config;
	private static DoubleConfigFileParameter FEED_FORWARD_ACCELERATION_CONSTANT_config;
	private static DoubleConfigFileParameter MAX_ACCELERATION_DRIVE_PROFILE_config;
	private static DoubleConfigFileParameter MAX_SPEED_INCHES_LOWGEAR_config;
	private static DoubleConfigFileParameter DECELERATION_VELOCITY_THRESHOLD_config;
	private static DoubleConfigFileParameter DECELERATION_MOTOR_SPEED_config;
	private static DoubleConfigFileParameter STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR_config;
	private static DoubleConfigFileParameter DRIVE_OFFSET_config;
	private static DoubleConfigFileParameter QUICK_TURN_CAP_config;
	private static DoubleConfigFileParameter QUICK_TURN_ANTITURBO_config;
	private static BooleanConfigFileParameter USE_LEFT_SIDE_FOR_OFFSET_config;

	public DriveBase(String name) {
		super(name);

		WHEEL_DIAMETER_config = new DoubleConfigFileParameter(this.getClass().getName(), "wheel_diameter", 6);
		TICKS_PER_ROTATION_config = new DoubleConfigFileParameter(this.getClass().getName(), "ticks_per_rotation", 360.0);
		THROTTLE_LOW_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "throttle_low_gear_accel_factor", 0.250);
		HEADING_LOW_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "heading_low_gear_accel_factor", 0.500);
		THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "throttle_high_gear_accel_factor", 0.125);
		HEADING_HIGH_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "heading_high_gear_accel_factor", 0.250);
		MAX_HIGH_GEAR_PERCENT_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_high_gear_percent", 0.80);
		ENCODER_GEAR_RATIO_config = new DoubleConfigFileParameter(this.getClass().getName(), "encoder_gear_ratio", 7.5);
		DEADBAND_config = new DoubleConfigFileParameter(this.getClass().getName(), "deadband", 0.05);
		SLOW_TURN_FORWARD_SPEED_config = new DoubleConfigFileParameter(this.getClass().getName(), "slow_turn_forward_speed", 0.16);
		SLOW_TURN_BACKWARD_SPEED_config = new DoubleConfigFileParameter(this.getClass().getName(), "slow_turn_backward_speed", -0.19);
		FEED_FORWARD_VELOCITY_CONSTANT_config = new DoubleConfigFileParameter(this.getClass().getName(), "feed_forward_velocity_constant", 1.00);
		FEED_FORWARD_ACCELERATION_CONSTANT_config = new DoubleConfigFileParameter(this.getClass().getName(), "feed_forward_acceleration_constant", 0.00018);
		MAX_ACCELERATION_DRIVE_PROFILE_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_acceleration_drive_profile", 600.0);
		MAX_SPEED_INCHES_LOWGEAR_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_speed_inches_lowgear", 90.0);
		DECELERATION_VELOCITY_THRESHOLD_config = new DoubleConfigFileParameter(this.getClass().getName(), "deceleration_velocity_threshold", 48.0);
		DECELERATION_MOTOR_SPEED_config = new DoubleConfigFileParameter(this.getClass().getName(), "deceleration_motor_speed", 0.3);
		STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR_config = new DoubleConfigFileParameter(this.getClass().getName(), "stopping_distance_at_max_speed_lowgear", 10.0);
		DRIVE_OFFSET_config = new AutonomousDoubleConfigFileParameter("DriveOffset", 1.00);
		USE_LEFT_SIDE_FOR_OFFSET_config = new AutonomousBooleanConfigFileParameter("UseLeftDriveForOffset", true);
		QUICK_TURN_CAP_config = new DoubleConfigFileParameter(this.getClass().getName(), "quick_turn_cap", 10.0);
		QUICK_TURN_ANTITURBO_config = new DoubleConfigFileParameter(this.getClass().getName(), "quick_turn_antiturbo", 10.0);

		// Anti-Turbo button
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_8);
		// Turbo button
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_7);
		// Shifter Button
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_6);

		// Initialize the drive base encoders
		leftDriveEncoder = new Encoder(0, 1, true, EncodingType.k4X);
		leftDriveEncoder.reset();
		rightDriveEncoder = new Encoder(2, 3, false, EncodingType.k4X);
		rightDriveEncoder.reset();

		// Initialize the gyro
		// @TODO: Get the correct port
		driveHeadingGyro = new Gyro(1);

		continuousAccelerationFilter = new ContinuousAccelFilter(0, 0, 0);
		driveSpeedPidInput = new DriveBaseSpeedPidInput();
		driveSpeedPidOutput = new DriveBaseSpeedPidOutput();
		driveSpeedPid = new SpeedPidController(driveSpeedPidInput, driveSpeedPidOutput, "DriveBaseSpeedPid");
		init();
	}

	public void init() {
		driveBaseThrottleValue = 0.0;
		driveBaseHeadingValue = 0.0;
		antiTurboFlag = false;
		turboFlag = false;
		shifterFlag = DoubleSolenoid.Value.kForward;
		quickTurnFlag = false;
		motionProfileActive = false;
		previousTime = Timer.getFPGATimestamp();
		currentProfileX = 0.0;
		continuousAccelerationFilter = new ContinuousAccelFilter(0, 0, 0);
		// Zero out all motor values left over from autonomous
		OutputManager.getInstance().getOutput(OutputManager.LEFT_DRIVE_SPEED_INDEX).set(new Double(0.0));
		OutputManager.getInstance().getOutput(OutputManager.RIGHT_DRIVE_SPEED_INDEX).set(new Double(0.0));
		OutputManager.getInstance().getOutput(OutputManager.LEFT_DRIVE_SPEED_INDEX).update();
		OutputManager.getInstance().getOutput(OutputManager.RIGHT_DRIVE_SPEED_INDEX).update();
		InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).set(JoystickAxisEnum.DRIVER_THROTTLE, new Double(0.0));
		InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).set(JoystickAxisEnum.DRIVER_HEADING, new Double(0.0));
		InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX).update();
		// Clear encoders
		resetLeftEncoder();
		resetRightEncoder();
	}

	public void update() {
		updateSpeedAndAccelerationCalculations();
		if (true == motionProfileActive) {

			// Update PID using profile velocity as setpoint and measured
			// velocity as PID input
			enableSpeedPidControl();
			setDriveSpeedPidSetpoint(continuousAccelerationFilter.getCurrVel());
			// Update system to get feed forward terms
			deltaPosError = this.deltaPosition - (deltaProfilePosition);
			distance_moved += this.deltaPosition;
			// distance_remaining = this.distance_to_move - currentProfileX;
			distance_remaining = this.distance_to_move - distance_moved;
			// Logger.getLogger().debug(this.getClass().getName(),
			// "AccelFilter", "distance_left: " + distance_remaining + " p: " +
			// continuousAccelerationFilter.getCurrPos()+ " v: " +
			// continuousAccelerationFilter.getCurrVel() + " a: " +
			// continuousAccelerationFilter.getCurrAcc() );
			continuousAccelerationFilter.calculateSystem(distance_remaining, currentProfileV, goal_velocity, MAX_ACCELERATION_DRIVE_PROFILE, MAX_SPEED_INCHES_LOWGEAR, deltaTime);
			deltaProfilePosition = continuousAccelerationFilter.getCurrPos() - currentProfileX;
			currentProfileX = continuousAccelerationFilter.getCurrPos();
			currentProfileV = continuousAccelerationFilter.getCurrVel();
			currentProfileA = continuousAccelerationFilter.getCurrAcc();
			SmartDashboard.putNumber("Speed PID Error", driveSpeedPid.getError());
			SmartDashboard.putNumber("Speed PID Output", this.pidSpeedValue);
			SmartDashboard.putNumber("Distance Error", this.deltaPosError);
			// Update motor output with PID output and feed forward velocity and
			// acceleration
			double throttleValue = this.pidSpeedValue + FEED_FORWARD_VELOCITY_CONSTANT * (continuousAccelerationFilter.getCurrVel() / MAX_SPEED_INCHES_LOWGEAR) + FEED_FORWARD_ACCELERATION_CONSTANT * continuousAccelerationFilter.getCurrAcc();

			if (((distance_remaining < getStoppingDistanceFromDistanceToMove(distance_to_move)) && (currentProfileV > 0) && (currentProfileA < 0)) || ((distance_remaining > -getStoppingDistanceFromDistanceToMove(distance_to_move)) && (currentProfileV < 0) && (currentProfileA > 0))) {
				throttleValue = 0.0;
			}

			// Update the throttle value outside the function so that the
			// acceleration factor is not applied.
			driveBaseThrottleValue = throttleValue;
			if (driveBaseThrottleValue > MAX_INPUT_THROTTLE_VALUE) {
				driveBaseThrottleValue = MAX_INPUT_THROTTLE_VALUE;
			} else if (driveBaseThrottleValue < -MAX_INPUT_THROTTLE_VALUE) {
				driveBaseThrottleValue = -MAX_INPUT_THROTTLE_VALUE;
			}
			SmartDashboard.putNumber("Motion Profile Throttle", driveBaseThrottleValue);
			updateDriveMotors(true);
		} else {
			// We are in manual control
			// Get the inputs for heading and throttle
			// Set heading and throttle values
			double throttleValue = 0.0;
			double headingValue = 0.0;

			throttleValue = ((Double) ((InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX))).get(JoystickAxisEnum.DRIVER_THROTTLE)).doubleValue();
			headingValue = ((Double) ((InputManager.getInstance().getOiInput(InputManager.DRIVER_JOYSTICK_INDEX))).get(JoystickAxisEnum.DRIVER_HEADING)).doubleValue();

			SmartDashboard.putNumber("Heading Joystick Value", headingValue);

			setThrottleValue(throttleValue);
			setHeadingValue(headingValue);

			// Use updated values to update the quickTurnFlag
			checkAutoQuickTurn();

			// Set the drive motor outputs
			updateDriveMotors(false);

			SmartDashboard.putNumber("Throttle Value", driveBaseThrottleValue);
			SmartDashboard.putNumber("Heading Value", driveBaseHeadingValue);
			SmartDashboard.putBoolean("Shifter State", shifterFlag.equals(DoubleSolenoid.Value.kReverse));
			SmartDashboard.putBoolean("Anti-Turbo Flag", antiTurboFlag);
			SmartDashboard.putBoolean("Quickturn", quickTurnFlag);

			// Set gear shift output
			OutputManager.getInstance().getOutput(OutputManager.SHIFTER_INDEX).set(new Integer(shifterFlag.value));
		}

		SmartDashboard.putNumber("Left encoder count: ", this.getLeftEncoderValue());
		SmartDashboard.putNumber("Right encoder count: ", this.getRightEncoderValue());
		SmartDashboard.putNumber("Right Distance: ", this.getRightDistance());
		SmartDashboard.putNumber("Left Distance: ", this.getLeftDistance());
		SmartDashboard.putNumber("Gyro angle", this.getGyroAngle());
	}

	private void updateSpeedAndAccelerationCalculations() {
		double newTime = Timer.getFPGATimestamp();
		double leftDistance = this.getLeftDistance();
		double rightDistance = this.getRightDistance();
		double rightDelta = (rightDistance - previousRightPositionSinceLastReset);
		double leftDelta = (leftDistance - previousLeftPositionSinceLastReset);
		this.deltaPosition = (Math.abs(rightDelta) > Math.abs(leftDelta) ? rightDelta : leftDelta);
		this.deltaTime = (newTime - previousTime);
		if (this.deltaTime > 0.060) {
			this.deltaTime = 0.060;
		}
		// Do velocity internally in in/sec
		currentVelocity = (deltaPosition / deltaTime);
		currentAcceleration = ((currentVelocity - previousVelocity) / deltaTime);

		// Output velocity in ft/sec
		SmartDashboard.putNumber("Velocity: ", this.currentVelocity / 12.0);
		SmartDashboard.putNumber("Accel: ", this.currentAcceleration / 144.0);

		totalPosition += deltaPosition;
		if (Math.abs(deltaPosition) > 0.005) {
			// Logger.getLogger().debug(this.getClass().getName(), "Kinematics",
			// "tP: "+ totalPosition + " dP: " + deltaPosition + "dpp:" +
			// deltaProfilePosition + " dt: " + deltaTime + " cv: " +
			// currentVelocity + " pv: " + previousVelocity + " ca: " +
			// currentAcceleration);
		}
		previousPositionSinceLastReset += deltaPosition;
		previousRightPositionSinceLastReset += rightDelta;
		previousLeftPositionSinceLastReset += leftDelta;
		previousTime = newTime;
		previousVelocity = currentVelocity;

	}

	public void resetKinematics() {
		previousPositionSinceLastReset = 0.0;
		previousVelocity = 0.0;
		currentVelocity = 0.0;
		currentAcceleration = 0.0;
	}

	public double getDistanceRemaining() {
		return this.distance_remaining;
	}

	public double getAcceleration() {
		return currentAcceleration;
	}

	public double getVelocity() {
		return currentVelocity;
	}

	public void setThrottleValue(double tValue) {

		// Taking into account Anti-Turbo
		double new_throttle = tValue;
		if (true == antiTurboFlag) {
			new_throttle *= ANTI_TURBO_MAX_DEFLECTION;

			// Cap the throttle at the maximum deflection allowed for anti-turbo
			if (new_throttle > ANTI_TURBO_MAX_DEFLECTION) {
				new_throttle = ANTI_TURBO_MAX_DEFLECTION;
			}
			if (new_throttle < -ANTI_TURBO_MAX_DEFLECTION) {
				new_throttle = -ANTI_TURBO_MAX_DEFLECTION;
			}
		}

		if (shifterFlag == DoubleSolenoid.Value.kReverse) {
			// We are in high gear, see if the turbo button is pressed
			if (turboFlag == true) {
				// We are in turbo mode, don't cap the output
			} else {
				// We aren't in turbo mode, cap the output at the max percent
				// for high gear
				if (new_throttle > MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT) {
					new_throttle = MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT;
				}
				if (new_throttle < -MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT) {
					new_throttle = -MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT;
				}
			}

		} else {
			// We are in low gear, don't modify the throttle
		}

		// Use the acceleration factor based on the current shifter state
		if (shifterFlag == DoubleSolenoid.Value.kForward) {
			// We are in low gear, use that acceleration factor
			driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * THROTTLE_LOW_GEAR_ACCEL_FACTOR;
		} else if (shifterFlag == DoubleSolenoid.Value.kReverse) {
			// We are in high gear, use that acceleration factor
			driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * THROTTLE_HIGH_GEAR_ACCEL_FACTOR;
		} else {
			// This is bad...
			// If we get here we have a problem
		}

		if (driveBaseThrottleValue > MAX_INPUT_THROTTLE_VALUE) {
			driveBaseThrottleValue = MAX_INPUT_THROTTLE_VALUE;
		} else if (driveBaseThrottleValue < -MAX_INPUT_THROTTLE_VALUE) {
			driveBaseThrottleValue = -MAX_INPUT_THROTTLE_VALUE;
		}
	}

	public void setHeadingValue(double hValue) {

		// Taking into account anti-turbo
		double new_heading = hValue;
		if (true == antiTurboFlag) {
			new_heading *= ANTI_TURBO_MAX_DEFLECTION;

			// Cap the heading at the maximum deflection allowed for anti-turbo
			if (new_heading > ANTI_TURBO_MAX_DEFLECTION) {
				new_heading = ANTI_TURBO_MAX_DEFLECTION;
			}
			if (new_heading < -ANTI_TURBO_MAX_DEFLECTION) {
				new_heading = -ANTI_TURBO_MAX_DEFLECTION;
			}
		}

		// Use the acceleration factor based on the current shifter state
		if (shifterFlag == DoubleSolenoid.Value.kForward) {
			// We are in low gear, use that acceleration factor
			driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * HEADING_LOW_GEAR_ACCEL_FACTOR;
		} else if (shifterFlag == DoubleSolenoid.Value.kReverse) {
			// We are in high gear, use that acceleration factor
			driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * HEADING_HIGH_GEAR_ACCEL_FACTOR;
		} else {
			// This is bad...
			// If we get here we have a problem
		}

		if (driveBaseHeadingValue > MAX_INPUT_HEADING_VALUE) {
			driveBaseHeadingValue = MAX_INPUT_HEADING_VALUE;
		} else if (driveBaseHeadingValue < -MAX_INPUT_HEADING_VALUE) {
			driveBaseHeadingValue = -MAX_INPUT_HEADING_VALUE;
		}
	}

	public void overrideHeadingValue(double newHeading) {
		driveBaseHeadingValue = newHeading;
		if (driveBaseHeadingValue > MAX_INPUT_HEADING_VALUE) {
			driveBaseHeadingValue = MAX_INPUT_HEADING_VALUE;
		} else if (driveBaseHeadingValue < -MAX_INPUT_HEADING_VALUE) {
			driveBaseHeadingValue = -MAX_INPUT_HEADING_VALUE;
		}
	}

	public void updateDriveMotors(boolean useDriveOffset) {
		double rightMotorSpeed = 0;
		double leftMotorSpeed = 0;
		double angularPower = 0.0;
		if (Math.abs(driveBaseHeadingValue) > 0.05) {
			// Absolute value on driveBaseThrottleValue, creates a S curve, none
			// is banana
			angularPower = driveBaseThrottleValue * driveBaseHeadingValue * HEADING_SENSITIVITY;
		}

		rightMotorSpeed = driveBaseThrottleValue - angularPower;
		leftMotorSpeed = driveBaseThrottleValue + angularPower;

		if (true == quickTurnFlag) {
			rightMotorSpeed = 0.0f;
			leftMotorSpeed = 0.0f;
			driveBaseThrottleValue = 0.0f;

			// Quick turn does not take throttle into account
			leftMotorSpeed += driveBaseHeadingValue;
			rightMotorSpeed -= driveBaseHeadingValue;

			/*
			 * if(true == antiTurboFlag) { leftMotorSpeed /=
			 * ANTI_TURBO_MAX_DEFLECTION; leftMotorSpeed *=
			 * QUICK_TURN_ANTITURBO; rightMotorSpeed /=
			 * ANTI_TURBO_MAX_DEFLECTION; rightMotorSpeed *=
			 * QUICK_TURN_ANTITURBO; }
			 * 
			 * if(false == turboFlag) { if (leftMotorSpeed > QUICK_TURN_CAP) {
			 * leftMotorSpeed = QUICK_TURN_CAP; } else if (leftMotorSpeed <
			 * -QUICK_TURN_CAP) { leftMotorSpeed = -QUICK_TURN_CAP; } if
			 * (rightMotorSpeed > QUICK_TURN_CAP) { rightMotorSpeed =
			 * QUICK_TURN_CAP; } else if (rightMotorSpeed < -QUICK_TURN_CAP) {
			 * rightMotorSpeed = -QUICK_TURN_CAP; } }
			 */
		} else {
			if (driveBaseThrottleValue >= 0) {
				if (rightMotorSpeed < 0) {
					rightMotorSpeed = 0;
				}
				if (leftMotorSpeed < 0) {
					leftMotorSpeed = 0;
				}
			} else {
				if (rightMotorSpeed >= 0) {
					rightMotorSpeed = 0;
				}
				if (leftMotorSpeed >= 0) {
					leftMotorSpeed = 0;
				}
			}

			if (rightMotorSpeed > MAX_MOTOR_OUTPUT) {
				rightMotorSpeed = MAX_MOTOR_OUTPUT;
			}
			if (leftMotorSpeed > MAX_MOTOR_OUTPUT) {
				leftMotorSpeed = MAX_MOTOR_OUTPUT;
			}
			if (rightMotorSpeed < -MAX_MOTOR_OUTPUT) {
				rightMotorSpeed = -MAX_MOTOR_OUTPUT;
			}
			if (leftMotorSpeed < -MAX_MOTOR_OUTPUT) {
				leftMotorSpeed = -MAX_MOTOR_OUTPUT;
			}
		}

		// If our throttle is within the zero deadband and our velocity is above
		// the threshold,
		// use deceleration to slow us down
		if ((Math.abs(driveBaseThrottleValue) < DEADBAND) && (Math.abs(currentVelocity) > DECELERATION_VELOCITY_THRESHOLD) && !quickTurnFlag) {
			// We are above the velocity threshold, apply a small inverse motor
			// speed to decelerate
			if (currentVelocity > 0) {
				// We are moving forward, apply a negative motor value
				rightMotorSpeed = -DECELERATION_MOTOR_SPEED;
				leftMotorSpeed = -DECELERATION_MOTOR_SPEED;
			} else {
				// We are moving backward, apply a positive motor value
				rightMotorSpeed = DECELERATION_MOTOR_SPEED;
				leftMotorSpeed = DECELERATION_MOTOR_SPEED;
			}
		} else if ((Math.abs(driveBaseThrottleValue) < DEADBAND) && (Math.abs(currentVelocity) <= DECELERATION_VELOCITY_THRESHOLD) && !quickTurnFlag) {
			// We are below the velocity threshold, zero the motor values to
			// brake
			rightMotorSpeed = 0.0;
			leftMotorSpeed = 0.0;
		}

		// If we're within the deadband, zero out the throttle and heading
		if ((leftMotorSpeed < DEADBAND && leftMotorSpeed > -DEADBAND) && (rightMotorSpeed < DEADBAND && rightMotorSpeed > -DEADBAND)) {
			this.setHeadingValue(0.0);
			this.setThrottleValue(0.0);
			rightMotorSpeed = 0.0;
			leftMotorSpeed = 0.0;
		}

		SmartDashboard.putNumber("LeftDriveSpeed", leftMotorSpeed);
		SmartDashboard.putNumber("RightDriveSpeed", rightMotorSpeed);

		// Update Output Facade.
		OutputManager.getInstance().getOutput(OutputManager.LEFT_DRIVE_SPEED_INDEX).set(new Double(leftMotorSpeed));
		OutputManager.getInstance().getOutput(OutputManager.RIGHT_DRIVE_SPEED_INDEX).set(new Double(rightMotorSpeed));
	}

	public void checkAutoQuickTurn() {
		double throttle = driveBaseThrottleValue;
		double heading = driveBaseHeadingValue;

		throttle = Math.abs(throttle);
		heading = Math.abs(heading);

		if ((throttle < 0.1) && (heading > 0.1)) {
			quickTurnFlag = true;
		} else {
			quickTurnFlag = false;
		}
	}

	/*
	 * ENCODER/GYRO STUFF
	 */
	public Encoder getLeftEncoder() {
		return leftDriveEncoder;
	}

	public Encoder getRightEncoder() {
		return rightDriveEncoder;
	}

	public double getLeftEncoderValue() {
		return leftDriveEncoder.get();
	}

	public double getRightEncoderValue() {
		return rightDriveEncoder.get();
	}

	public double getLeftDistance() {
		double distance = 0.0;
		distance = (leftDriveEncoder.get() / (TICKS_PER_ROTATION * ENCODER_GEAR_RATIO)) * 2.0 * Math.PI * (WHEEL_DIAMETER / 2.0);
		return distance;
	}

	public double getRightDistance() {
		double distance = 0.0;
		distance = (rightDriveEncoder.get() / (TICKS_PER_ROTATION * ENCODER_GEAR_RATIO)) * 2.0 * Math.PI * (WHEEL_DIAMETER / 2.0);
		return distance;
	}

	public void resetLeftEncoder() {
		leftDriveEncoder.reset();
	}

	public void resetRightEncoder() {
		rightDriveEncoder.reset();
	}

	public Gyro getGyro() {
		return driveHeadingGyro;
	}

	public double getGyroAngle() {
		return driveHeadingGyro.getAngle();
	}

	public void resetGyro() {
		driveHeadingGyro.reset();
	}

	public void setPidSpeedValue(double pidSpeed) {
		// Add the feed forward velocity and acceleration

		pidSpeedValue = pidSpeed;
	}

	public double getPidSpeedValue() {
		// Add the feed forward velocity and acceleration

		return pidSpeedValue;
	}

	public void enableSpeedPidControl() {
		driveSpeedPid.enable();
	}

	public void setDriveSpeedPidSetpoint(double speed) {
		driveSpeedPid.setSetPoint(speed);
		driveSpeedPid.calcPid();
	}

	public void startStraightMoveWithMotionProfile(double distance, double goal_velocity) {
		startMoveWithHeadingAndMotionProfile(distance, goal_velocity, 0.0);

	}

	public void startMoveWithHeadingAndMotionProfile(double distance, double goal_velocity, double heading) {
		this.distance_to_move = distance;
		this.distance_moved = 0.0;
		this.distance_remaining = distance;
		this.goal_velocity = goal_velocity;
		motionProfileActive = true;
		overrideHeadingValue(heading);
	}

	public void stopStraightMoveWithMotionProfile() {
		disableSpeedPidControl();
		continuousAccelerationFilter = new ContinuousAccelFilter(0, 0, 0);
		currentProfileX = 0;
		this.distance_to_move = 0.0;
		this.distance_remaining = 0.0;
		this.goal_velocity = 0.0;
		motionProfileActive = false;
		overrideHeadingValue(0.0);
	}

	public void disableSpeedPidControl() {
		driveSpeedPid.disable();
		resetSpeedPid();
		Logger.getLogger().debug(this.getClass().getName(), "disableSpeedPidControl", "Speed PID is disabled");
	}

	public void resetSpeedPid() {
		driveSpeedPid.reset();
	}

	public boolean getQuickTurnFlag() {
		return quickTurnFlag;
	}

	public void notifyConfigChange() {
		WHEEL_DIAMETER = WHEEL_DIAMETER_config.getValue();
		TICKS_PER_ROTATION = TICKS_PER_ROTATION_config.getValue();
		THROTTLE_LOW_GEAR_ACCEL_FACTOR = THROTTLE_LOW_GEAR_ACCEL_FACTOR_config.getValue();
		HEADING_LOW_GEAR_ACCEL_FACTOR = HEADING_LOW_GEAR_ACCEL_FACTOR_config.getValue();
		THROTTLE_HIGH_GEAR_ACCEL_FACTOR = THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config.getValue();
		HEADING_HIGH_GEAR_ACCEL_FACTOR = HEADING_HIGH_GEAR_ACCEL_FACTOR_config.getValue();
		MAX_HIGH_GEAR_PERCENT = MAX_HIGH_GEAR_PERCENT_config.getValue();
		ENCODER_GEAR_RATIO = ENCODER_GEAR_RATIO_config.getValue();
		SLOW_TURN_FORWARD_SPEED = SLOW_TURN_FORWARD_SPEED_config.getValue();
		SLOW_TURN_BACKWARD_SPEED = SLOW_TURN_BACKWARD_SPEED_config.getValue();
		FEED_FORWARD_VELOCITY_CONSTANT = FEED_FORWARD_VELOCITY_CONSTANT_config.getValue();
		FEED_FORWARD_ACCELERATION_CONSTANT = FEED_FORWARD_ACCELERATION_CONSTANT_config.getValue();
		MAX_ACCELERATION_DRIVE_PROFILE = MAX_ACCELERATION_DRIVE_PROFILE_config.getValue();
		MAX_SPEED_INCHES_LOWGEAR = MAX_SPEED_INCHES_LOWGEAR_config.getValue();
		DEADBAND = DEADBAND_config.getValue();
		DECELERATION_VELOCITY_THRESHOLD = DECELERATION_VELOCITY_THRESHOLD_config.getValue();
		DECELERATION_MOTOR_SPEED = DECELERATION_MOTOR_SPEED_config.getValue();
		STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR = STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR_config.getValue();
		QUICK_TURN_CAP = QUICK_TURN_CAP_config.getValue();
		QUICK_TURN_ANTITURBO = QUICK_TURN_ANTITURBO_config.getValue();
		driveSpeedPid.notifyConfigChange();
	}

	public void acceptNotification(Subject subjectThatCaused) {
		if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_8) {
			antiTurboFlag = ((BooleanSubject) subjectThatCaused).getValue();
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_6) {
			if (((BooleanSubject) subjectThatCaused).getValue() == true) {
				shifterFlag = shifterFlag.equals(DoubleSolenoid.Value.kForward) ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward;
			}
		} else if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_7) {
			turboFlag = ((BooleanSubject) subjectThatCaused).getValue();
		}
	}

	public double getSpeedError() {
		return driveSpeedPid.getError();
	}

	public double getDeltaPosError() {
		return deltaPosError;
	}

	private double getStoppingDistanceFromDistanceToMove(double distance) {
		if (Math.abs(distance) > 40.0) {
			return STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR;
		} else {
			return (STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR - ((3.0f / 15.0f) * (40 - Math.abs(distance))));
		}
	}

	public void setShifter(DoubleSolenoid.Value state) {
		this.shifterFlag = state;
	}
}
