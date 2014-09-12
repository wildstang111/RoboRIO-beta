package org.wildstang.outputmanager.outputs;

import org.wildstang.config.BooleanConfigFileParameter;
import org.wildstang.outputmanager.base.IOutput;
import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.ISubjectEnum;
import org.wildstang.subjects.base.Subject;
import edu.wpi.first.wpilibj.DigitalOutput;

/**
 *
 * @author Nathan
 */
public class WsDigitalOutput implements IOutput {

	BooleanSubject digitalValue;
	DigitalOutput output;
	BooleanConfigFileParameter startState;

	// By giving the output number in the constructor we can make this generic
	// for all digital outputs

	public WsDigitalOutput(String name, int channel) {
		this.digitalValue = new BooleanSubject(name + ":DigitalOutput" + channel);
		startState = new BooleanConfigFileParameter(this.getClass().getName() + "." + name, "startState", false);

		this.output = new DigitalOutput(channel);

		digitalValue.setValue(startState.getValue());
	}

	public void set(IOutputEnum key, Object value) {
		digitalValue.setValue(((Boolean) value).booleanValue());

	}

	public Subject getSubject(ISubjectEnum subjectEnum) {
		return digitalValue;
	}

	public Object get(IOutputEnum key) {
		return digitalValue.getValueAsObject();
	}

	public void update() {
		digitalValue.updateValue();
		this.output.set(digitalValue.getValue());
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
