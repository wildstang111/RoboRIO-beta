package org.wildstang.inputmanager.inputs;

import org.wildstang.config.DoubleConfigFileParameter;
import org.wildstang.inputmanager.base.IInput;
import org.wildstang.inputmanager.base.IInputEnum;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.ISubjectEnum;
import org.wildstang.subjects.base.Subject;
import edu.wpi.first.wpilibj.AnalogInput;

;

/**
 *
 * @author Nathan
 */
public class WsAnalogInput implements IInput {

	DoubleSubject analogValue;
	AnalogInput input;

	// By giving the input number in the constructor we can make this generic
	// for all digital inputs
	public WsAnalogInput(int channel) {
		this.analogValue = new DoubleSubject("AnalogInput" + channel);
		this.input = new AnalogInput(channel);

		analogValue.setValue(0.0);
	}

	public void set(IInputEnum key, Object value) {
		boolean b = ((Boolean) value).booleanValue();
		double d = 0;
		if (b) {
			d = 1;
		}
		analogValue.setValue(d);

	}

	public Subject getSubject(ISubjectEnum subjectEnum) {
		return analogValue;
	}

	public Object get(IInputEnum key) {
		return analogValue.getValueAsObject();
	}

	public void update() {
		analogValue.updateValue();
	}

	public void pullData() {
		analogValue.setValue(input.getAverageVoltage());
	}

	public void set(Object value) {
		this.set((IInputEnum) null, value);
	}

	public Subject getSubject() {
		return this.getSubject((ISubjectEnum) null);
	}

	public Object get() {
		return this.get((IInputEnum) null);
	}

	public void notifyConfigChange() {
		// Nothing to update here, since the config value only affect the
		// start state.
	}
}
