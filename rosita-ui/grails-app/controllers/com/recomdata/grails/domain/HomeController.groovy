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

/*******************************************************************************
 * Copyright(c)  2009-2011 Recombinant Data Corp., All rights Reserved
 ******************************************************************************/

package com.recomdata.grails.domain;

public class HomeController {
	
	// injections
	def authenticateService
	
	def index = {
	
		def user = authenticateService.userDomain();

		// if not logged in, boot to login page
		if(!authenticateService.isLoggedIn()) {
			return redirect(controller: 'login', params: params);
		}

		// go to user event log page
		log.info("authenicated user: ${user}")				
		redirect(controller:'rositaJob', action:'index');
	}
	
}
