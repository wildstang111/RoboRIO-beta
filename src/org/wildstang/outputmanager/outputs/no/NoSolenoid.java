package org.wildstang.outputmanager.outputs.no;

import org.wildstang.outputmanager.outputs.*;
import org.wildstang.config.BooleanConfigFileParameter;
import org.wildstang.outputmanager.base.IOutput;
import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.ISubjectEnum;
import org.wildstang.subjects.base.Subject;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Nathan
 */
public class NoSolenoid implements IOutput {

	BooleanSubject subject;

	public NoSolenoid(String name, int module, int channel1) {
		this.subject = new BooleanSubject(name);
		subject.setValue(false);

	}

	public NoSolenoid(String name, int channel1) {
		this(name, 1, channel1);
	}

	public void set(IOutputEnum key, Object value) {
		subject.setValue(((Boolean) value).booleanValue());

	}

	public Subject getSubject(ISubjectEnum subjectEnum) {
		return subject;
	}

	public Object get(IOutputEnum key) {
		return subject.getValueAsObject();
	}

	public void update() {
		subject.updateValue();
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
