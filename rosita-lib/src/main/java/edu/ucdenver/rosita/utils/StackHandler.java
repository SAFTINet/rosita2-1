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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class StackHandler extends DefaultHandler {
	
	private static StackHandler me = null;
	
	public static StackHandler getInstance() {
		if (me == null) {
			me = new StackHandler();
		}
		return me;
	}
	
	Long elements = 0L;
	Long outputDelay = 0L;
	Long outputLimit = null;
	Integer clinic = 1;
	Integer maxClinics = 1;
	Long dataSourceId = 1L;
	
	public void setOutputLimit(Long limit) {
		outputLimit = limit;
	}
	
	public void setClinic(Integer clinic) {
		this.clinic = clinic;
	}
	
	public void setMaxClinics(Integer max) {
		this.maxClinics = max;
	}
	
	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	
	public void setElements(Long elements) {
		this.elements = elements;
		this.outputDelay = elements;
	}
		
	private StackHandler() {}
	
	private XPathStackHandler xpathStack = new XPathStackHandler();
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		//stack.push(localName);
		xpathStack.push(localName);
		elements++;
		outputDelay++;
		if (outputLimit != null && outputDelay >= outputLimit) {
			RositaValidationErrorHandler errorHandler = RositaValidationErrorHandler.getInstance();
			RositaLogger.log(true, "STATUS|||" + elements + "|||" + xpathStack.getCurrentState() + "|||" + errorHandler.errors + "|||" + dataSourceId + "|||" + clinic + "|||" + maxClinics);
			outputDelay = 0L;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) {
		//stack.pop();
		xpathStack.pop();
	}
	
	public String getStack() {
		return xpathStack.getCurrentState();
	}
	
	public String getObject() {
		return xpathStack.getLatestObject();
	}
	
	public Long getElements() {
		return elements;
	}

}
