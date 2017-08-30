/*
*   Copyright 2012-2013 The Regents of the University of Colorado
*
*   Licensed under the Apache License, Version 2.0 (the "License")
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package edu.ucdenver.rosita;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.ucdenver.rosita.utils.RositaLogger;

public class ArgHandler {
	
	private static Map<String, String> argmap = new HashMap<String, String>();
	private static String propertiesFilename = "/usr/share/rosita/rosita.properties";
	
	public static void initialize() throws IOException {
		initialize(new String[0]);
	}
	public static void initialize(String[] args) throws IOException {		
		//Load properties from the properties file first
		try {
			Properties props = new Properties();
			File propsFile = new File(propertiesFilename);
			props.load(new FileReader(propsFile));
			for (Object o : props.keySet()) {
				String propKey = (String) o;
				argmap.put(propKey, props.getProperty(propKey));
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("No properties file (/usr/share/rosita/rosita.properties) was found!");
			//Not a problem - continue without it
		}
		
		//Now add/override from the command line
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			String[] splitarg = arg.split("=", 2);
			if (splitarg.length == 1) {
				argmap.put(splitarg[0].toLowerCase(), "true");
			}
			else {
				//Special - if this value started with a " and doesn't end with one, keep adding args until we reach another "
				while (splitarg[1].startsWith("\"") && !splitarg[1].endsWith("\"")) {
					i++;
					splitarg[1] += args[i];
				}
				argmap.put(splitarg[0].toLowerCase(), splitarg[1]);
			}
		}
	}
	
	public static String getArg(String name) {
		String arg = argmap.get(name);
		return arg != null ? arg : "";
	}
	
	public static Integer getInt(String name) {
		String arg = getArg(name);
		try {
			return Integer.parseInt(arg);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public static Long getLong(String name) {
		String arg = getArg(name);
		try {
			return Long.parseLong(arg);
		}
		catch (Exception e) {
			return 0L;
		}
	}
	
	public static Long getLong(String name, Long defaultvalue) {
		String arg = getArg(name);
		try {
			return Long.parseLong(arg);
		}
		catch (Exception e) {
			return defaultvalue;
		}
	}
	
	public static boolean getBoolean(String name) {
		String arg = getArg(name);
		if (arg != null) {
			return Boolean.parseBoolean(arg);
		}
		return false;
	}
	
	public static String getArg(String name, String defaultString) {
		String propValue = argmap.get(name);
		String returnValue = (propValue == null || propValue == "") ? defaultString : propValue;
		return returnValue;
	}
	
	public static void mustExist(String name) {
		if (getArg(name).equals("")) {
			RositaLogger.log(false, "No " + name + " provided");
			RositaLogger.error("No " + name + " provided to rosita-lib", "ERROR");
			RositaLogger.log(true, "ERROR|||No " + name + " provided");
			System.exit(1);
		}
	}

}
