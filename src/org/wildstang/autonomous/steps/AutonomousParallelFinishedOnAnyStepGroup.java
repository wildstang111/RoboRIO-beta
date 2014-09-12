/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.steps;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joey
 */
public class AutonomousParallelFinishedOnAnyStepGroup extends AutonomousStep {

	private String name;
	private boolean initialized = false;
	private final List<AutonomousStep> steps = new ArrayList<>();

	public AutonomousParallelFinishedOnAnyStepGroup() {
		name = "";
	}

	public AutonomousParallelFinishedOnAnyStepGroup(String name) {
		this.name = name;
	}

	public void initialize() {
		initialized = true;
		for (AutonomousStep step : steps) {
			step.initialize();
		}
	}

	public void update() {
		for (AutonomousStep step : steps) {
			step.update();
			if (step.isFinished()) {
				steps.clear();
				break;
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
		return "Parallel finished on any step group: " + name;
	}
}
