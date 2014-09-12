package org.wildstang.subjects.base;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nathan
 */
abstract public class Subject {

	protected String name;
	protected List<IObserver> observers;
	protected ISubjectEnum type;

	protected Subject(String name) {
		this(name, null);
	}

	protected Subject(String name, ISubjectEnum type) {
		this.name = name;
		observers = new ArrayList<>();
		this.type = type;
	}

	protected Subject() {
		this("Subject");
	}

	public void attach(IObserver observer) {
		observers.add(observer);
	}

	public void detach(IObserver observer) {
		if (observers.contains(observer)) {
			observers.remove(observer);
		}
	}

	protected void notifyObservers() {
		for (IObserver observer : observers) {
			observer.acceptNotification(this);
		}
	}

	public String getName() {
		return name;
	}

	public ISubjectEnum getType() {
		return type;
	}

	public void setValue(Object value) {
		System.out.println(name + " does not currently support this object for use with setValue.");
	}

	abstract public Object getValueAsObject();
}
