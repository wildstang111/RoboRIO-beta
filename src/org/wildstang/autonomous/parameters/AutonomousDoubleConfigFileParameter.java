/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.parameters;

import org.wildstang.config.DoubleConfigFileParameter;

/**
 *
 * @author chadschmidt
 */
public class AutonomousDoubleConfigFileParameter extends DoubleConfigFileParameter {

	public AutonomousDoubleConfigFileParameter(String pName, double defValue) {
		super("org.wildstang.autonomous.parameters", pName, defValue);
	}

}
