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

class WorkflowTitle {
	
	Long id
	String title
	Integer stepNumber
	String handler
	String initialState
	Integer nextStep

	static mapping = {
		table 'cz.cz_workflow_title'
		version false
		columns {
			id column: 'title_id', generator: 'sequence', params:[sequence:'cz.cz_sq']
		}
	}
	
    static constraints = {
		handler(nullable: true)
		nextStep(nullable: true)
    }
}
