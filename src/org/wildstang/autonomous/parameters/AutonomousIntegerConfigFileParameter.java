/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.parameters;

import org.wildstang.config.IntegerConfigFileParameter;

/**
 *
 * @author chadschmidt
 */
public class AutonomousIntegerConfigFileParameter extends IntegerConfigFileParameter {

	public AutonomousIntegerConfigFileParameter(String pName, int defValue) {
		super("org.wildstang.autonomous.parameters", pName, defValue);
	}

}
