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

class ClinicStatus {

    Long id
    RositaJob job
	WorkflowStepInstance workflowStep
    MultiClinicDataSource mcDataSource
    Boolean success

    static hasMany = [wfSteps : WorkflowStepInstance]

    static mapping = {
        table 'cz.cz_clinic_status'
        version false
		columns {
			id column: 'clinic_status_id', generator: 'sequence', params:[sequence:'cz.cz_sq']
			mcDataSource column: 'x_data_source_id'
		}
    }

    static constraints = {
    }
}
