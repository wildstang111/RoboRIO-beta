/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.outputmanager.outputs.no;

import org.wildstang.outputmanager.base.IServo;
import org.wildstang.outputmanager.outputs.*;
import org.wildstang.outputmanager.base.IOutput;
import org.wildstang.outputmanager.base.IOutputEnum;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.ISubjectEnum;
import org.wildstang.subjects.base.Subject;

/**
 *
 * @author Rick a.k.a. Batman
 */
public class NoServo implements IServo {

	DoubleSubject position;
	private boolean angleSet;

	public NoServo(String name, int channel) {
		this.position = new DoubleSubject(name);
		angleSet = false;
	}

	public void set(IOutputEnum key, Object value) {
		position.setValue(((Double) value).doubleValue());
		angleSet = false;
	}

	public void setAngle(IOutputEnum key, Object value) {
		position.setValue(((Double) value).doubleValue());
		angleSet = true;
	}

	public Object get(IOutputEnum key) {
		return this.position.getValueAsObject();
	}

	public Subject getSubject(ISubjectEnum subjectEnum) {
		return this.position;
	}

	public void update() {
		this.position.updateValue();
	}

	public void set(Object value) {
		this.set((IOutputEnum) null, value);
	}

	public Object get() {
		return this.get((IOutputEnum) null);
	}

	public void notifyConfigChange() {
	}
}
