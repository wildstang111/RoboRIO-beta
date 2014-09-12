package org.wildstang.subsystems;

import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subsystems.base.Subsystem;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

/**
 *
 * @author John
 */
public class LED extends Subsystem implements IObserver {

	MessageHandler messageSender;
	// Sent states
	boolean autoDataSent = false;
	boolean disableDataSent = false;
	boolean sendData = false;

	public static class LedCmd {

		byte[] dataBytes = new byte[5];

		public LedCmd(int command, int payloadByteOne, int payloadByteTwo) {

			dataBytes[0] = (byte) command;
			dataBytes[1] = (byte) payloadByteOne;
			dataBytes[2] = (byte) payloadByteTwo;
			dataBytes[3] = 0;
			dataBytes[4] = 0;
		}

		byte[] getBytes() {
			return dataBytes;
		}
	}

	/*
	 * --------------------------------------------------------- | Function |
	 * Cmd | PL 1 | PL 2 |
	 * --------------------------------------------------------- | Shoot | 0x05
	 * | 0x13 | 0x14 | | Climb | 0x06 | 0x11 | 0x12 | | Autonomous | 0x02 | 0x11
	 * | 0x12 | | Red Alliance | 0x04 | 0x52 | 0x01 | | Blue Alliance | 0x04 |
	 * 0x47 | 0x01 | ---------------------------------------------------------
	 * 
	 * Send sequence once, no spamming the Arduino.
	 */

	// Reused commands from year to year
	LedCmd autoCmd = new LedCmd(0x02, 0x11, 0x12);
	LedCmd redCmd = new LedCmd(0x04, 0x52, 0x01);
	LedCmd blueCmd = new LedCmd(0x04, 0x47, 0x01);

	// New commands each year
	LedCmd shootCmd = new LedCmd(0x05, 0x13, 0x14);
	LedCmd climbCmd = new LedCmd(0x06, 0x11, 0x12);
	LedCmd intakeCmd = new LedCmd(0x07, 0x11, 0x12);

	public LED(String name) {
		super(name);
		// Fire up the message sender thread.
		Thread t = new Thread(messageSender = new MessageHandler());
		// This is safe because there is only one instance of the subsystem in
		// the subsystem container.
		t.start();

		// Kicker
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_6);
		// Intake
		registerForJoystickButtonNotification(JoystickButtonEnum.MANIPULATOR_BUTTON_5);
		// Climb
		registerForJoystickButtonNotification(JoystickButtonEnum.DRIVER_BUTTON_2);
	}

	public void init() {
		// Nothing to do anymore, I'm bored.
		autoDataSent = false;
		disableDataSent = false;
		sendData = false;
	}

	public void update() {
		// Get all inputs relevant to the LEDs
		boolean isRobotEnabled = DriverStation.getInstance().isEnabled();
		boolean isRobotTeleop = DriverStation.getInstance().isOperatorControl();
		boolean isRobotAuton = DriverStation.getInstance().isAutonomous();
		DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();

		if (isRobotEnabled) {
			if (isRobotTeleop) {
				// Do nothing, handled in acceptNotification
			} else if (isRobotAuton) {
				// --------------------------------------------------------------
				// Handle Autonomous signalling here
				// --------------------------------------------------------------
				// One send and one send only.
				// Don't take time in auto sending LED cmds.
				if (!autoDataSent) {
					autoDataSent = sendData(autoCmd);
				}
			}
		} else {
			// ------------------------------------------------------------------
			// Handle Disabled signalling here
			// ------------------------------------------------------------------
			switch (alliance) {
			case Red: {
				if (!disableDataSent) {
					disableDataSent = sendData(redCmd);
				}
			}
				break;

			case Blue: {
				if (!disableDataSent) {
					disableDataSent = sendData(blueCmd);
				}
			}
				break;
			default: {
				disableDataSent = false;
			}
				break;
			}
		}
	}

	public void acceptNotification(Subject subjectThatCaused) {
		boolean buttonState = ((BooleanSubject) subjectThatCaused).getValue();
		if (DriverStation.getInstance().isOperatorControl()) {
			if (buttonState) {
				if (subjectThatCaused.getType() == JoystickButtonEnum.MANIPULATOR_BUTTON_6) {
					if (buttonState) {
						sendData(shootCmd);
					}
				}
				if (subjectThatCaused.getType() == JoystickButtonEnum.DRIVER_BUTTON_2) {
					if (buttonState) {
						sendData(climbCmd);
					}
				}
			} else {
				// send cmds on buttone release
			}
		}

	}

	private boolean sendData(LedCmd ledCmd) {
		byte[] dataBytes = ledCmd.getBytes();

		synchronized (messageSender) {
			messageSender.setSendData(dataBytes, dataBytes.length);
			messageSender.notify();
		}

		return true;
	}

	private static class MessageHandler implements Runnable {
		// Designed to only have one single threaded controller. (LED)
		// Offload to a thread avoid blocking main thread with LED sends.

		static byte[] rcvBytes;
		byte[] sendData;
		int sendSize = 0;
		I2C i2c;
		boolean running = true;
		boolean dataToSend = false;

		public MessageHandler() {
			// Get ourselves an i2c instance to send out some data.
			i2c = new I2C(Port.kOnboard, 0x52 << 1);
		}

		public void run() {
			while (running) {
				synchronized (this) {
					try {
						// blocking sleep until someone calls notify.
						this.wait();
						// Need at least 5 bytes and someone has to have called
						// setSendData.
						if (sendSize >= 5 && dataToSend) {
							// Extremely fast and cheap data confirmation
							// algorithm
							sendData[3] = (byte) (~sendData[1]);
							sendData[4] = (byte) (~sendData[2]);
							// byte[] dataToSend, int sendSize, byte[]
							// dataReceived, int receiveSize
							// set receive size to 0 to avoid sending an i2c
							// read request.
							// System.out.println("Cmd: " +
							// Integer.toHexString(sendData[0]));
							i2c.transaction(sendData, sendSize, rcvBytes, 0);
							dataToSend = false;
						}
					} catch (InterruptedException e) {
					}
				}
			}
		}

		public void setSendData(byte[] data, int size) {
			sendData = data;
			sendSize = size;
			dataToSend = true;
		}

		public void stop() {
			running = false;
		}
	}
}
