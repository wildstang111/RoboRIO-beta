package org.wildstang.subjects.base;

/**
 *
 * @author Nathan
 */
public interface IObserver {

	public void acceptNotification(Subject subjectThatCaused);
}
