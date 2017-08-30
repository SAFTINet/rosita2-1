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

package edu.ucdenver.rosita.utils;

import java.util.HashMap;
import java.util.Map;

public class XPathStackEntry {

	String name;
	Integer index;
	Map<String, Integer> childMap = new HashMap<String, Integer>();
	
	public XPathStackEntry(String name, Integer index) {
		this.name = name;
		this.index = index;
		this.childMap = new HashMap<String, Integer>();
	}
	
	public void addToMap(String elementName) {
		Integer i = childMap.get(elementName);
		if (i == null) {
			childMap.put(elementName, 1);
		}
		else {
			childMap.put(elementName, i+1);
		}
	}
	
	public Integer getIndexForChild(String elementName) {
		return childMap.get(elementName);
	}

}
