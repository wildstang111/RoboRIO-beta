/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wildstang.configmanager.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.wildstang.logger.Logger;
import org.wildstang.types.DataElement;

/**
 *
 * @author Nathan
 */
public final class ConfigManagerImpl {

	static String myClassName = "ConfigManagerimpl";

	private ConfigManagerImpl() {
	}

	/**
	 * Reads the config file /ws_config.txt unless setFileName has been used to
	 * change the filename. Supports comment lines using // delineator. Comments
	 * can be by themselves on a line or at the end of the line.
	 *
	 * The config file should be on the format key=value Example:
	 * org.wildstang.WsInputFacade.WsDriverJoystick.trim=0.1
	 *
	 * @throws ConfigManagerImplException
	 * @returns Hashtable of the config params.
	 */
	public static List<DataElement> readConfig(String fileName) throws ConfigManagerImplException {
		List<DataElement> config = new ArrayList<>();
		String line;
		StringTokenizer st;
		String configLine;
		byte[] b;
		int i;
		String path = "file://";
		path += fileName;
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
			while ((line = br.readLine()) != null) {
				// initialize configLine
				configLine = line;
				if (!(line.trim().startsWith("//")) && (line.trim().length() != 0)) {
					// This is not a comment line
					b = line.trim().getBytes();
					for (i = 0; i < (b.length - 1); i++) {
						if (b[i] == '/') {
							if (b[i + 1] == '/') {
								// We have a comment
								configLine = line.trim().substring(0, i - 1);
								break;
							}
						}
					}
					if (i == b.length - 1) {
						// we got to the end
						configLine = line.trim();
					}

					st = new StringTokenizer(configLine.trim(), "=");
					if (st.countTokens() >= 2) {
						config.add(new DataElement(st.nextToken(), st.nextToken()));
					} else {
						br.close();
						throw new ConfigManagerImplException("Bad line in config file " + line);
					}
				}
			}
		} catch (Throwable ioe) {
			throw new ConfigManagerImplException(ioe.toString());
		}
		return config;
	}

	public static boolean checkCreateFile(String filename) {

		try {
			File file = new File(filename);
			if (file.exists()) {
				return true;
			} else {
				file.createNewFile();
				return true;
			}
		} catch (Throwable e) {
			Logger.getLogger().error(myClassName, "checkCreateFile", "Open Config File exception " + e.toString());
			return false;
		}
	}

	public static String getConfigItemName(String configItem) {
		StringTokenizer st;
		String configName = null;
		if (configItem != null) {
			st = new StringTokenizer(configItem.trim(), ".");
			while (st.hasMoreTokens()) {
				configName = st.nextToken();
			}
		}
		return configName;
	}

	public static boolean isUpdateAvailable(String filename) {
		File file;
		String path = "file://";
		path += filename;

		try {
			file = new File(path);
			return file.exists();
		} catch (Throwable e) {
			Logger.getLogger().error(myClassName, "isUpdateAvailable", "Check for update exception " + e.toString());
			return false;
		}
	}

	public static void deleteUpdateFile(String filename) {
		File file;
		String path = "file://";
		path += filename;

		try {
			file = new File(path);
			file.delete();
		} catch (Throwable e) {
			Logger.getLogger().error(myClassName, "isUpdateAvailable", "Update file not deleted " + e.toString());
		}
	}
}
