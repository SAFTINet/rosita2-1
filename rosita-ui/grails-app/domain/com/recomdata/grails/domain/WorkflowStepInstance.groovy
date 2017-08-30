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

class WorkflowStepInstance {
	
	Long id
	RositaJob job
	WorkflowStepDescription stepDescription
	Date startDate
	Date endDate
	String state
	//Integer procId
	//String message

	static mapping = {
		table 'cz.step'
		version false
		columns {
			id column: 'step_id'
			message type: 'text'
		}
	}
	
    static constraints = {
		startDate (nullable: true)
		endDate (nullable: true)
		//procId (nullable: true)
		//message (nullable: true)
		job (nullable: true)
		stepDescription (nullable: true)
    }
		
	static transients = ['hasStepErrors', 'hasStepWarnings']
	
	public boolean getHasStepErrors() {
		def error = RositaLog.executeQuery("SELECT id FROM RositaLog WHERE step = ? AND messageType='ERROR'", [this], [max:1])
		return error ? true : false
	}
	
	public boolean getHasStepWarnings() {
		def error = RositaLog.executeQuery("SELECT id FROM RositaLog WHERE step = ? AND messageType='WARNING'", [this], [max:1])
		return error ? true : false
	}
}
