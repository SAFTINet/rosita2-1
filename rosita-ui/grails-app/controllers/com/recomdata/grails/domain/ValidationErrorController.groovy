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

import au.com.bytecode.opencsv.CSVWriter


class ValidationErrorController {
	
	static String FILTER_SESSION_KEY = "validation.error.filter"
	
	def index = {
		//Get place to return to
		def controllerReturn = params.from
		if (controllerReturn) {
			session.setAttribute(FILTER_SESSION_KEY, controllerReturn)
		}
		controllerReturn = session.getAttribute(FILTER_SESSION_KEY)
		
		def job = WorkflowStepInstance.get(params.id).job;
		
		def offset = (params.int('offset') ?: 0)
		def max = (params.int('max') ?: 50)
		def errors = ValidationError.createCriteria().list([max: max, offset: offset]) {
			eq('stepId', params.long('id'));
			
			and {
				order('datetime', 'asc');
				order('lineNumber', 'asc');
			}
		}
		
		[errors: errors, errorCount: errors.getTotalCount(), id: params.long('id'), max: max, offset: offset, from: controllerReturn, jobId: job.id]
	}
	
	def export = {
		def errors = ValidationError.createCriteria().list() {
			eq('stepId', params.long('id'));
			and {
				order('datetime', 'asc');
				order('lineNumber', 'asc');
			}
		}
		
		response.setHeader('Content-disposition', 'attachment; filename=validationerrors' + params.id + '.csv')
		response.contentType = 'text/plain'
		
		String lineSeparator = System.getProperty('line.separator')
		CSVWriter csv = new CSVWriter(response.writer)
		
		String[] head = ["Clinic ID", "Clinic Name", "Type", "Object", "Line", "Date", "Message"]
		csv.writeNext(head)
		for (error in errors) {
			String[] vals = [error.mcDataSource?.id, error.mcDataSource?.dataSourceName, error.errorType, error.sourceType, error.lineNumber, error.datetime, error.message]
			csv.writeNext(vals)
		}
		csv.close()
	}
}
