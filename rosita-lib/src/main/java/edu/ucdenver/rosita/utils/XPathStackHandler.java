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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XPathStackHandler {

	List<XPathStackEntry> entries = new ArrayList<XPathStackEntry>();

	private List<String> recognizedObjects = Arrays.asList(new String[] {"organization", "care_site", "provider", "x_demographic", "visit_occurrence", "observation", "drug_exposure", "condition_occurrence", "procedure_occurrence", "cohort", "death", "drug_cost", "payer_plan_period", "procedure_cost"});

	public void push(String entry) {
		XPathStackEntry current = getCurrentEntry();
		int newIndex = 1;
		if (current != null) {
			current.addToMap(entry);
			newIndex = current.getIndexForChild(entry);
		}
		entries.add(new XPathStackEntry(entry, newIndex));
	}
	
	public void pop() {
		//Discard the latest entry
		entries.remove(entries.size()-1);
	}
	
	public XPathStackEntry getCurrentEntry() {
		if (entries.size() > 0) {
			return entries.get(entries.size()-1);
		}
		else {
			return null;
		}
	}
	
	public String getCurrentState() {
		int i = 0; // A bit cheesy, but... don't output the first entry - this is root.
		StringBuilder sb = new StringBuilder();
		for (XPathStackEntry entry : entries) {
			if (i != 0) {
				sb.append("/" + entry.name + "[" + entry.index + "]");
			}
			i++;
		}
		return sb.toString();
	}
	
	public String getLatestObject() {
		for (int i = entries.size()-1; i >= 0; i--) {
			String name = entries.get(i).name;
			if (recognizedObjects.contains(name)) {
				return name;
			}
		}
		return "";
	}
}
