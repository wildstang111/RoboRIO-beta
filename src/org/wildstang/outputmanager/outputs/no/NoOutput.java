/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.outputmanager.outputs.no;

import org.wildstang.outputmanager.base.IOutput;
import org.wildstang.outputmanager.base.IOutputEnum;

/**
 *
 * @author Joey
 */
public class NoOutput implements IOutput {
	protected Object object = new Object();

	public void set(IOutputEnum key, Object value) {
	}

	public Object get(IOutputEnum key) {
		return object;
	}

	public void set(Object value) {
		this.set((IOutputEnum) null, value);
	}

	public Object get() {
		return this.get((IOutputEnum) null);
	}

	public void update() {
	}

	public void notifyConfigChange() {
	}

}
