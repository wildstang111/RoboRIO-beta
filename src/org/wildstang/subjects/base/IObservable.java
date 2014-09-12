package org.wildstang.subjects.base;

/**
 *
 * @author Nathan
 */
public interface IObservable {

	public Subject getSubject(ISubjectEnum subjectEnum);

	public Subject getSubject();
}
