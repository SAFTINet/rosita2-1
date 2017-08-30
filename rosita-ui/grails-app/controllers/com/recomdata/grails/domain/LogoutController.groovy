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
 * Copyright(c)  2009-2010 Recombinant Data Corp., All rights Reserved
 ******************************************************************************/
package com.recomdata.grails.domain;

/**
 * Logout Controller
 */
class LogoutController {

	/**
	 * Index action. Redirects to the Spring security logout uri.
	 */
	def index = {
			
		// add your custom logout logic here ...
		
		redirect(uri: '/j_spring_security_logout')
	}
}
