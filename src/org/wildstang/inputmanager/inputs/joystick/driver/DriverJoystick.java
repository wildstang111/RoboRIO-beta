package org.wildstang.inputmanager.inputs.joystick.driver;

import org.wildstang.inputmanager.base.IInput;
import org.wildstang.inputmanager.base.IInputEnum;
import org.wildstang.inputmanager.inputs.joystick.IHardwareJoystick;
import org.wildstang.inputmanager.inputs.joystick.JoystickAxisEnum;
import org.wildstang.inputmanager.inputs.joystick.JoystickButtonEnum;
import org.wildstang.inputmanager.inputs.joystick.JoystickDPadButtonEnum;
import org.wildstang.subjects.base.BooleanSubject;
import org.wildstang.subjects.base.DoubleSubject;
import org.wildstang.subjects.base.ISubjectEnum;
import org.wildstang.subjects.base.Subject;
import org.wildstang.subjects.debouncer.Debouncer;
import org.wildstang.types.DataElement;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class DriverJoystick implements IInput {

	protected final static int numberOfAxes = 6;
	protected DoubleSubject[] axes;
	protected final static int numberOfButtons = 12;
	protected BooleanSubject[] buttons;
	protected final static int numberOfDPadButtons = 4;
	protected BooleanSubject[] dPadButtons;
	protected final static int neededCyclesForChange = 8;
	protected Debouncer[] dPadDebouncers;
	protected Joystick driverJoystick = null;

	public DriverJoystick() {
		driverJoystick = (Joystick) new Joystick(1);

		axes = new DoubleSubject[numberOfAxes];
		for (int i = 0; i < axes.length; i++) {
			if (JoystickAxisEnum.getEnumFromIndex(true, i) != null) {
				axes[i] = new DoubleSubject(JoystickAxisEnum.getEnumFromIndex(true, i));
			} else {
				axes[i] = new DoubleSubject("DriverSubject" + i);
			}
		}

		buttons = new BooleanSubject[numberOfButtons];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new BooleanSubject(JoystickButtonEnum.getEnumFromIndex(true, i));
		}

		dPadButtons = new BooleanSubject[numberOfDPadButtons];
		dPadDebouncers = new Debouncer[numberOfDPadButtons];
		for (int i = 0; i < dPadButtons.length; i++) {
			dPadButtons[i] = new BooleanSubject(JoystickDPadButtonEnum.getEnumFromIndex(true, i));
			dPadDebouncers[i] = new Debouncer(20, new Boolean(false));
		}
	}

	public Subject getSubject(ISubjectEnum subjectEnum) {
		if (subjectEnum instanceof JoystickAxisEnum && ((JoystickAxisEnum) subjectEnum).isDriver() == true) {
			return axes[((JoystickAxisEnum) subjectEnum).toValue()];
		} else if (subjectEnum instanceof JoystickButtonEnum && ((JoystickButtonEnum) subjectEnum).isDriver() == true) {
			return buttons[((JoystickButtonEnum) subjectEnum).toValue()];
		} else if (subjectEnum instanceof JoystickDPadButtonEnum && ((JoystickDPadButtonEnum) subjectEnum).isDriver()) {
			return dPadButtons[((JoystickDPadButtonEnum) subjectEnum).toValue()];
		} else {
			System.out.println("Subject not supported or incorrect.");
			return null;
		}
	}

	public void set(IInputEnum key, Object value) {
		if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == true) {
			axes[((JoystickAxisEnum) key).toValue()].setValue(value);
		} else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == true) {
			buttons[((JoystickButtonEnum) key).toValue()].setValue(value);
		} else if (key instanceof JoystickDPadButtonEnum && ((JoystickDPadButtonEnum) key).isDriver()) {
			dPadButtons[((JoystickDPadButtonEnum) key).toValue()].setValue(value);
		} else {
			System.out.println("key not supported or incorrect.");
		}
	}

	public Object get(IInputEnum key) {
		if (key instanceof JoystickAxisEnum && ((JoystickAxisEnum) key).isDriver() == true) {
			return axes[((JoystickAxisEnum) key).toValue()].getValueAsObject();
		} else if (key instanceof JoystickButtonEnum && ((JoystickButtonEnum) key).isDriver() == true) {
			return buttons[((JoystickButtonEnum) key).toValue()].getValueAsObject();
		} else if (key instanceof JoystickDPadButtonEnum && ((JoystickDPadButtonEnum) key).isDriver()) {
			return dPadButtons[((JoystickDPadButtonEnum) key).toValue()].getValueAsObject();
		} else {
			return new Double(-100);
		}
	}

	public void update() {
		for (int i = 0; i < axes.length; i++) {
			axes[i].updateValue();
		}
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].updateValue();
		}
		for (int i = 0; i < dPadButtons.length; i++) {
			dPadButtons[i].updateValue();
		}
	}

	public void pullData() {
		if (driverJoystick instanceof IHardwareJoystick) {
			((IHardwareJoystick) driverJoystick).pullData();
		}
		for (int i = 0; i < axes.length; i++) {
			// Invert the vertical axes so that full up is 1
			if (i % 2 != 0) {
				axes[i].setValue(driverJoystick.getRawAxis(i + 1) * -1);
			} else {
				axes[i].setValue(driverJoystick.getRawAxis(i + 1));
			}
		}
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setValue(driverJoystick.getRawButton(i + 1));
		}

		// DPad Button Logic
		dPadDebouncers[JoystickDPadButtonEnum.DPAD_DOWN].update(new Boolean(axes[JoystickAxisEnum.DPAD_Y].getValue() < 0));
		dPadDebouncers[JoystickDPadButtonEnum.DPAD_UP].update(new Boolean(axes[JoystickAxisEnum.DPAD_Y].getValue() > 0));
		dPadDebouncers[JoystickDPadButtonEnum.DPAD_LEFT].update(new Boolean(axes[JoystickAxisEnum.DPAD_X].getValue() < 0));
		dPadDebouncers[JoystickDPadButtonEnum.DPAD_RIGHT].update(new Boolean(axes[JoystickAxisEnum.DPAD_X].getValue() > 0));

		dPadButtons[JoystickDPadButtonEnum.DPAD_DOWN].setValue(dPadDebouncers[JoystickDPadButtonEnum.DPAD_DOWN].getDebouncedValue());
		dPadButtons[JoystickDPadButtonEnum.DPAD_UP].setValue(dPadDebouncers[JoystickDPadButtonEnum.DPAD_UP].getDebouncedValue());
		dPadButtons[JoystickDPadButtonEnum.DPAD_LEFT].setValue(dPadDebouncers[JoystickDPadButtonEnum.DPAD_LEFT].getDebouncedValue());
		dPadButtons[JoystickDPadButtonEnum.DPAD_RIGHT].setValue(dPadDebouncers[JoystickDPadButtonEnum.DPAD_RIGHT].getDebouncedValue());
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
	}
}
