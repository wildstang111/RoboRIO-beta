package org.wildstang.autonomous;

import java.util.ArrayList;
import java.util.List;

import org.wildstang.autonomous.programs.AutonomousProgramSleeper;
import org.wildstang.autonomous.programs.AutonomousProgramYMCA;
import org.wildstang.autonomous.programs.test.AutonomousProgramDriveDistanceMotionProfile;
import org.wildstang.autonomous.programs.test.AutonomousProgramDrivePatterns;
import org.wildstang.autonomous.programs.test.AutonomousProgramTestParallel;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.logger.Logger;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author coder65535
 */
public class AutonomousManager implements IObserver {

	private List<AutonomousProgram> programs = new ArrayList<AutonomousProgram>();
	private int currentProgram, lockedProgram;
	private float selectorSwitch, positionSwitch;
	private AutonomousProgram runningProgram;
	private boolean programFinished, programRunning, lockInSwitch;
	private static AutonomousManager instance = null;
	private AutonomousStartPositionEnum currentPosition;

	private AutonomousManager() {
		definePrograms();
		InputManager.getInstance().getOiInput(InputManager.AUTO_PROGRAM_SELECTOR_INDEX).getSubject().attach(this);
		InputManager.getInstance().getOiInput(InputManager.LOCK_IN_SWITCH_INDEX).getSubject().attach(this);
		InputManager.getInstance().getOiInput(InputManager.START_POSITION_SELECTOR_INDEX).getSubject().attach(this);
		selectorSwitch = 0;
		lockInSwitch = false;
		positionSwitch = 0;
		currentPosition = AutonomousStartPositionEnum.UNKNOWN;
		clear();
	}

	public void update() {
		if (programFinished) {
			runningProgram.cleanup();
			programFinished = false;
			lockedProgram = 0;
			startCurrentProgram();
		}
		runningProgram.update();
		if (runningProgram.isFinished()) {
			programFinished = true;
		}
	}

	public void startCurrentProgram() {
		runningProgram = (AutonomousProgram) programs.get(lockedProgram);
		Logger.getLogger().always("Auton", "Running Autonomous Program", runningProgram.toString());
		runningProgram.initialize();
		SmartDashboard.putString("Running Autonomous Program", runningProgram.toString());
	}

	public void clear() {
		programFinished = false;
		programRunning = false;
		if (runningProgram != null) {
			runningProgram.cleanup();
		}
		runningProgram = (AutonomousProgram) programs.get(0);
		lockedProgram = 0;
		SmartDashboard.putString("Running Autonomous Program", "No Program Running");
		SmartDashboard.putString("Locked Autonomous Program", programs.get(lockedProgram).toString());
		SmartDashboard.putString("Current Autonomous Program", programs.get(currentProgram).toString());
		SmartDashboard.putString("Current Start Position", currentPosition.toString());
	}

	public AutonomousProgram getRunningProgram() {
		if (programRunning) {
			return runningProgram;
		} else {
			return (AutonomousProgram) null;
		}
	}

	public String getRunningProgramName() {
		return runningProgram.toString();
	}

	public String getSelectedProgramName() {
		return programs.get(currentProgram).toString();
	}

	public String getLockedProgramName() {
		return programs.get(lockedProgram).toString();
	}

	public AutonomousStartPositionEnum getStartPosition() {
		return currentPosition;
	}

	public void acceptNotification(Subject cause) {
		if (cause instanceof DoubleSubject) {
			if (cause == InputManager.getInstance().getOiInput(InputManager.START_POSITION_SELECTOR_INDEX).getSubject()) {
				positionSwitch = (float) ((DoubleSubject) cause).getValue();
				if (positionSwitch >= 3.3) {
					positionSwitch = 3.3f;
				}
				if (positionSwitch < 0) {
					positionSwitch = 0;
				}
				currentPosition = AutonomousStartPositionEnum.getEnumFromValue((int) (Math.floor((positionSwitch / 3.4) * AutonomousStartPositionEnum.POSITION_COUNT)));
				SmartDashboard.putString("Current Start Position", currentPosition.toString());
			} else if (cause == InputManager.getInstance().getOiInput(InputManager.AUTO_PROGRAM_SELECTOR_INDEX).getSubject()) {
				selectorSwitch = (float) ((DoubleSubject) cause).getValue();
				if (selectorSwitch >= 3.3) {
					selectorSwitch = 3.3f;
				}
				if (selectorSwitch < 0) {
					selectorSwitch = 0;
				}
				currentProgram = (int) (Math.floor((selectorSwitch / 3.4) * programs.size()));
				SmartDashboard.putString("Current Autonomous Program", programs.get(currentProgram).toString());
			}
		} else if (cause instanceof BooleanSubject) {
			lockInSwitch = ((BooleanSubject) cause).getValue();
			lockedProgram = !lockInSwitch ? currentProgram : 0;
			SmartDashboard.putString("Locked Autonomous Program", programs.get(lockedProgram).toString());
		}
	}

	public static AutonomousManager getInstance() {
		if (AutonomousManager.instance == null) {
			AutonomousManager.instance = new AutonomousManager();
		}
		return AutonomousManager.instance;
	}

	public void setProgram(int index) {
		if (index >= programs.size() || index < 0) {
			index = 0;
		}
		currentProgram = index;
		lockedProgram = currentProgram;
	}

	public void setPosition(int index) {
		if (index >= AutonomousStartPositionEnum.POSITION_COUNT) {
			index = 0;
		}
		currentPosition = AutonomousStartPositionEnum.getEnumFromValue(index);
	}

	private void definePrograms() {
		addProgram(new AutonomousProgramSleeper()); // Always leave Sleeper as
													// 0. Other parts of the
													// code assume 0 is Sleeper.
		addProgram(new AutonomousProgramDriveDistanceMotionProfile());
		addProgram(new AutonomousProgramDrivePatterns());
		addProgram(new AutonomousProgramTestParallel());
		addProgram(new AutonomousProgramYMCA());
	}

	private void addProgram(AutonomousProgram program) {
		programs.add(program);
	}
}
