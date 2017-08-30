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

import edu.ucdenver.rosita.RositaParser;

class UserController {

	def authenticateService;

	// the delete, save and update actions only accept POST requests
	static Map allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']

	def index = {
		redirect action: edit
	}
	
	def edit = {
		//Get the sole Person entry
		def person = Person.createCriteria().get {}
		
		if (!person) {
			flash.message = "User not found"
			redirect(controller: 'rositaJob', action: 'index')
			return
		}
		[person:person]
	}

    def importCohortData = {
      	def cohortImportResult = RositaParser.importCohortDataUI()
        if(cohortImportResult){
           flash.message = "Import Cohort data done"	
        }else{
           flash.message = "Import Cohort data failed"	
        }
        redirect(controller: 'utils',action: 'about')
    }

	def update = {
		def person = Person.get(params.id)
		if (!person) {
			redirect(controller: 'rositaJob', action: 'index')
			return
		}
				
		
		if (!params.password.equals(params.passwordconfirm)) {
			flash.message = "Passwords did not match!"
			render(view: 'edit', model: [person:person]);
			return;
		}
		if (!params.password) {
			flash.message = "No password provided"
			render(view: 'edit', model: [person:person]);
			return;
		}

		person.properties = params
		person.password = authenticateService.encodePassword(params.password)
		
		if (!person.hasErrors() && person.save()) {
			// sync up roles
			flash.message = "Updated login"
			redirect(controller: 'utils', action: 'about');
		} else {
			render(view: 'edit', model: [person:person]);
		}
	}
}
