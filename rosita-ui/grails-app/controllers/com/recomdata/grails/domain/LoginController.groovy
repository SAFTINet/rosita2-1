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

import org.springframework.security.AuthenticationTrustResolverImpl
import org.springframework.security.DisabledException
import org.springframework.security.context.SecurityContextHolder as SCH
import org.springframework.security.ui.AbstractProcessingFilter
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter

/**
 * Login Controller 
 */
class LoginController {

	/**
	 * Dependency injection for the authentication service.
	 */
	def authenticateService

	private final authenticationTrustResolver = new AuthenticationTrustResolverImpl()

	def index = {
		if (isLoggedIn()) {
			redirect(uri: '/home');
		} else {
			redirect action: auth, params: params
		}
	}

	/**
	 * Show the login page.
	 */
	def auth = {

		nocache response

		if (isLoggedIn()) {
			log.info("already auth, redirect url: "+request.getRequestURL())		
			return redirect(action: index, params: params);
		}

		// check if session is expired
		//if(session.getLastAccessedTime()){
			//flash.message="Your session is expired."
		//}
		log.info("login page requested")
				
		String view
		String postUrl
		def config = authenticateService.securityConfig.security
		if (config.useOpenId) {
			view = 'openIdAuth'
			postUrl = "${request.contextPath}/login/openIdAuthenticate"
		} else if (config.useFacebook) {
			view = 'facebookAuth'
			postUrl = "${request.contextPath}${config.facebook.filterProcessesUrl}"
		} else {
			view = 'auth'
			postUrl = "${request.contextPath}${config.filterProcessesUrl}"						
		}

		render view: view, model: [postUrl: postUrl]
	}

	// Login page (function|json) for Ajax access.
	def authAjax = {
		nocache(response)
		//this is example:
		render """
		<script type='text/javascript'>
		(function() {
			loginForm();
		})();
		</script>
		"""
	}

	/**
	 * The Ajax success redirect url.
	 */
	def ajaxSuccess = {
		nocache(response)
		render '{success: true}'
	}

	/**
	 * Show denied page.
	 */
	def denied = {
		if (isLoggedIn() && authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
			// have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
			redirect action: full, params: params
		}
	}

	/**
	 * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
	 */
	def full = {
		render view: 'auth', params: params,
			model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication)]
	}

	// Denial page (data|view|json) for Ajax access.
	def deniedAjax = {
		//this is example:
		render "{error: 'access denied'}"
	}

	/**
	 * login failed
	 */
	def authfail = {

		def username = session[AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
		def msg = ''
		def exception = session[AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY]
		if (exception) {
			if (exception instanceof DisabledException) {
				msg = "[$username] is disabled."
			}
			else {
				msg = "[$username] wrong username/password."
			}
		}

		if (isAjax()) {
			render "{error: '${msg}'}"
		}
		else {
			flash.message = msg
			redirect action: auth, params: params
		}
	}

	/**
	 * Check if logged in.
	 */
	private boolean isLoggedIn() {
		return authenticateService.isLoggedIn()
	}

	private boolean isAjax() {
		return authenticateService.isAjax(request)
	}

	/** cache controls */
	private void nocache(response) {
		response.setHeader('Cache-Control', 'no-cache') // HTTP 1.1
		response.addDateHeader('Expires', 0)
		response.setDateHeader('max-age', 0)
		response.setIntHeader ('Expires', -1) //prevents caching at the proxy server
		response.addHeader('cache-Control', 'private') //IE5.x only
	}
}
