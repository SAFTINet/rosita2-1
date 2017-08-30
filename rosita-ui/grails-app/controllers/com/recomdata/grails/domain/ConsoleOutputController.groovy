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

package com.recomdata.grails.domain

import grails.converters.JSON

class ConsoleOutputController {
	
	def index = {
		def lines = ConsoleOutput.createCriteria().list() {
			eq('step.id', params.long('id'));
			order('date', 'asc');
		}
		
		if (!lines) {
			render(contentType:'text/plain',text:"No output for this step");
			return
		}
		
		def format = "%-80s | %s"
		
		for (line in lines) {
			render(contentType:'text/plain',text:String.format(format, line.message, line.date) + "\n");
		}
	}
}
