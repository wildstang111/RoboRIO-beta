package org.wildstang.outputmanager.outputs.no;

import org.wildstang.outputmanager.outputs.*;
import org.wildstang.outputmanager.base.IOutput;
import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.ISubjectEnum;
import org.wildstang.subjects.base.Subject;
import edu.wpi.first.wpilibj.Victor;

/**
 *
 * @author Nathan
 */
public class NoVictor implements IOutput {

	DoubleSubject motorSpeed;

	// By giving the victor1 number in the constructor we can make this generic
	// for all digital victor1s
	public NoVictor(String name, int channel) {
		this.motorSpeed = new DoubleSubject(name);

		motorSpeed.setValue(0.0);
	}

	public void set(IOutputEnum key, Object value) {
		motorSpeed.setValue(((Double) value).doubleValue());

	}

	public Subject getSubject(ISubjectEnum subjectEnum) {
		return motorSpeed;
	}

	public Object get(IOutputEnum key) {
		return motorSpeed.getValueAsObject();
	}

	public void update() {
		motorSpeed.updateValue();
	}

	public void set(Object value) {
		this.set((IOutputEnum) null, value);
	}

	public Object get() {
		return this.get((IOutputEnum) null);
	}

	public void notifyConfigChange() {
		// Nothing to update here, since the config value only affect the
		// start state.
	}
}
