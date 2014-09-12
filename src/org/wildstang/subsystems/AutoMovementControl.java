package org.wildstang.subsystems;

import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import org.wildstang.subsystems.base.AutoMovement;
import org.wildstang.logger.Logger;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.IObserver;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subsystems.automovements.AutoMovementAccumulateBackArm;
import org.wildstang.subsystems.automovements.AutoMovementAccumulateFrontArm;
import org.wildstang.subsystems.automovements.AutoMovementArmsToTension;
import org.wildstang.subsystems.automovements.AutoMovementPassBackToFront;
import org.wildstang.subsystems.automovements.AutoMovementPassFrontToBack;
import org.wildstang.subsystems.base.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joey
 */
public class AutoMovementControl extends Subsystem implements IObserver {

	private AutoMovement runningProgram;
	private AutoMovement programToRun;
	private boolean programInProgress;

	public AutoMovementControl(String name) {
		super(name);
	}

	public void init() {
		runningProgram = null;
		programToRun = null;
		programInProgress = false;
	}

	public void update() {
		if (runningProgram != null) {
			runningProgram.update();
			if (runningProgram.isFinished()) {
				cleanUpRunningProgram();
			}
		}
	}

	protected void startProgram() {
		runningProgram = programToRun;
		Logger.getLogger().always("AutoMovementCtrl", "Running Auto Movement", runningProgram.toString());
		runningProgram.initialize();
		SmartDashboard.putString("Auto Movement:", runningProgram.toString());
	}

	public void notifyConfigChange() {
	}

	public void acceptNotification(Subject subjectThatCaused) {
	}

	public void requestMovement(AutoMovement movement) {
		programToRun = movement;
		if (programInProgress && runningProgram != null) {
			// terminate current program and start new one.
			cancelProgram();
		}
		startProgram();
		programInProgress = true;
	}

	public void cancelMovement() {
		if (runningProgram != null) {
			cancelProgram();
		}
	}

	private void cancelProgram() {
		Logger.getLogger().always("AutoMovementCtrl", "Abort Auto Movement", runningProgram.toString());
		runningProgram.abort();
		cleanUpRunningProgram();
	}

	protected void cleanUpRunningProgram() {
		runningProgram.cleanup();
		runningProgram = null;
		programInProgress = false;
		SmartDashboard.putString("Auto Movement:", "");
	}
}
