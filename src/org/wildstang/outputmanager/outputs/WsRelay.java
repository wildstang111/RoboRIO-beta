package org.wildstang.outputmanager.outputs;

import org.wildstang.outputmanager.base.IOutput;
import org.wildstang.outputmanager.base.IOutputEnum;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;

/**
 *
 * @author Joey
 */
public class WsRelay implements IOutput {
	private Relay relay;

	public WsRelay(int channel, Direction direction) {
		relay = new Relay(channel, direction);
	}

	public void set(IOutputEnum key, Object value) {
		if (value instanceof Value) {
			relay.set((Value) value);
		}
	}

	public Object get(IOutputEnum key) {
		return relay;
	}

	public void update() {
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
