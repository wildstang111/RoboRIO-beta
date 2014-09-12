/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.crio;

import org.wildstang.autonomous.AutonomousManager;
import org.wildstang.configmanager.ConfigManager;
import org.wildstang.configmanager.ConfigManagerException;
import org.wildstang.inputmanager.base.InputManager;
import org.wildstang.logger.Logger;
import org.wildstang.outputmanager.base.OutputManager;
import org.wildstang.subsystems.base.SubsystemContainer;

/**
 *
 * @author Alex
 */
public class FrameworkAbstraction {

	public static void autonomousInit() {
		SubsystemContainer.getInstance().init();
		Logger.getLogger().readConfig();
		AutonomousManager.getInstance().startCurrentProgram();
	}

	public static void teleopInit() {
		SubsystemContainer.getInstance().init();
		Logger.getLogger().readConfig();
	}

	public static void robotInit(String fileName) {
		try {
			ConfigManager.getInstance().setConfigFileName(fileName);
			ConfigManager.getInstance().readConfig();
		} catch (ConfigManagerException wscfe) {
			System.out.println(wscfe.toString());
		}

		InputManager.getInstance();
		OutputManager.getInstance();
		SubsystemContainer.getInstance().init();
		Logger.getLogger().readConfig();
		AutonomousManager.getInstance();
	}

	public static void teleopPeriodic() {
		InputManager.getInstance().updateOiData();
		InputManager.getInstance().updateSensorData();
		SubsystemContainer.getInstance().update();
		OutputManager.getInstance().update();
	}

	public static void autonomousPeriodic() {
		InputManager.getInstance().updateOiDataAutonomous();
		InputManager.getInstance().updateSensorData();
		AutonomousManager.getInstance().update();
		SubsystemContainer.getInstance().update();
		OutputManager.getInstance().update();
	}

	public static void disabledPeriodic() {
		InputManager.getInstance().updateOiData();
	}

	public static void disabledInit() {
		AutonomousManager.getInstance().clear();
		try {
			ConfigManager.getInstance().readConfig();
		} catch (ConfigManagerException e) {
			System.out.println(e.getMessage());
		}

		SubsystemContainer.getInstance().init();
		Logger.getLogger().readConfig();
	}

}
