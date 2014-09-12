/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author coder65535
 */
public class AutonomousParallelStepGroup extends AutonomousStep {
	// Parallel groups execute all contained steps in the same frame. Be
	// careful!
	// Note: a finished step is immediately removed from the list. update() is
	// not called on any step that finishes.

	final List<AutonomousStep> steps = new ArrayList<>();
	boolean initialized = false;
	String name = "";

	public AutonomousParallelStepGroup() {
		name = "";
	}

	public AutonomousParallelStepGroup(String name) {
		this.name = name;
	}

	public void initialize() {
		for (AutonomousStep step : steps) {
			step.initialize();
		}
		initialized = true;
	}

	public void update() {
		for (AutonomousStep step : steps) {
			step.update();
			if (step.isFinished()) {
				steps.remove(step);
			}
		}
		if (steps.isEmpty()) {
			finished = true;
		}
	}

	public void addStep(AutonomousStep step) {
		if (!initialized) {
			steps.add(step);
		}
	}

	public String toString() {
		return "Parallel step group: " + name;
	}
}
