/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.autonomous.parameters;

import org.wildstang.config.BooleanConfigFileParameter;

/**
 *
 * @author chadschmidt
 */
public class AutonomousBooleanConfigFileParameter extends BooleanConfigFileParameter {

	public AutonomousBooleanConfigFileParameter(String pName, boolean defValue) {
		super("org.wildstang.autonomous.parameters", pName, defValue);
	}

}
