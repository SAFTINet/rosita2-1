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

package com.recomdata.grails.rositaui.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class GenericFilenameFilter implements FilenameFilter {

	private ArrayList<String> suffixes = new ArrayList<String>();
	
	/**
	 * Constructs this object initializing suffixes using specified suffixes. 
	 * @param suffixes pipe delimited list of suffixes
	 */
	public GenericFilenameFilter(String suffixes) {
		
		String[] values = suffixes.split("\\|");
		for (String value : values) {
			this.suffixes.add(value.toLowerCase());
			System.out.println("suffix = " + value);
		}
		
		
	}
	
	@Override
	public boolean accept(File dir, String name) {
		
		for (String suffix : suffixes) {
			if (name.toLowerCase().endsWith(suffix)) {
//				System.out.println("name = " + name + ", accept = true");
				return true;
			}
		}
		return false;
		
	}

}
