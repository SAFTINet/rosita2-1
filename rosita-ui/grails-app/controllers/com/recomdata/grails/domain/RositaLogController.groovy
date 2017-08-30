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

import java.util.Date;

import grails.converters.JSON
import au.com.bytecode.opencsv.CSVWriter

class RositaLogController {
	
	def index = {
		def lines = RositaLog.createCriteria().list() {
			eq('step.id', params.long('id'));
			order('id', 'asc');
		}
		
		if (!lines) {
			render(contentType:'text/plain',text:"No output for this step");
			return
		}
		
		def format = "%-80s | %s"
		
		for (line in lines) {
			render(contentType:'text/plain',text:String.format(format, line.message, line.logDate) + "\n");
		}
	}
	
	def list = {
		render(view: 'list', model: [stepId: params.long('id'), step: WorkflowStepInstance.get(params.long('id')), level: params.int('level')])
	}
	
	def listAjax = {
		def offset = params.int('offset') ?: 0
		def lines = RositaLog.createCriteria().list([max: 100, offset: offset]) {
			eq('step.id', params.long('id'))
			if (params.int('level') == 1) {
				'in'('messageType', ['WARNING'])
			}
			else if (params.int('level') == 2) {
				eq('messageType', 'ERROR')
			}
			order('logDate', 'asc')
		}
		
		render(template: 'logTable', model: [lines: lines, totalLines: lines.totalCount, offset: offset, level: params.int('level')])
	}
	
	def export = {
		def lines = RositaLog.createCriteria().list() {
			eq('step.id', params.long('id'));
			and {
				order('logDate', 'asc');
			}
		}
		
		response.setHeader('Content-disposition', 'attachment; filename=rositalogmessages' + params.id + '.csv')
		response.contentType = 'text/plain'
		
		String lineSeparator = System.getProperty('line.separator')
		CSVWriter csv = new CSVWriter(response.writer)
		
		String[] head = ["Date", "Message Type", "Error Code", "Message", "Schema Name", "Table Name", "Function Name", "Subtask Number", "Records Manipulated", "Filename", "Record Number", "Time Elapsed"]
		csv.writeNext(head)
		for (line in lines) {
			String[] vals = [
				line.logDate,
				line.messageType,
				line.errorCode,
				line.message,
				line.schemaName,
				line.tableName,
				line.functionName,
				line.subtaskNum,
				line.recordsManipulated,
				line.fileName,
				line.recordNum,
				line.timeElapsedSecs
			]
			csv.writeNext(vals)
		}
		csv.close()
	}
}
