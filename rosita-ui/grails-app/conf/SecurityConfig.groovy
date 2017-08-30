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

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

security {

	// see DefaultSecurityConfig.groovy for all settable/overridable properties

	// enable spring security
	active = true
	
	/** login user class **/	
	loginUserDomainClass = "com.recomdata.grails.domain.Person"
	userName = 'username'
	password = 'password'
	enabled = 'enabled'
	relationalAuthorities = 'authorities'

	/** Authority/Role domain class **/
	authorityDomainClass = 'com.recomdata.grails.domain.Role'
	authorityField = 'authority'
	
	/** URL request map domain class **/
	useRequestMapDomainClass = true
	requestMapClass = "com.recomdata.grails.domain.Requestmap"
	requestMapPathField = 'url'
	requestMapConfigAttributeField = 'configAttribute'
		
	providerNames = ['daoAuthenticationProvider',
	                 'anonymousAuthenticationProvider',
	                 'rememberMeAuthenticationProvider']
	
	if (ConfigurationHolder.config.security.useLdap == true) {
		providerNames.push('ldapAuthProvider');
	}
	
	filterNames = ['httpSessionContextIntegrationFilter',
	               'logoutFilter',				
	               'authenticationProcessingFilter',
	               'rememberMeProcessingFilter',
	               'anonymousProcessingFilter',
	               'exceptionTranslationFilter',
	               'filterInvocationInterceptor']	

	/** authenticationEntryPoint */
	loginFormUrl = '/login/auth'
	forceHttps = 'false'
	ajaxLoginFormUrl = '/login/authAjax'	
		
	/** 
	 * accessDeniedHandler
	 * set errorPage to null, if you want to get error code 403 (FORBIDDEN).
	 */
	errorPage = '/login/denied'
	
	//passwordEncoder:
	//The digest algorithm to use.
	//Supports the named Message Digest Algorithms in the Java environment.
	//http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA
	algorithm = 'MD5' // Ex. MD5 SHA
	//use Base64 text ( true or false )
	encodeHashAsBase64 = false	
	
	afterLogoutUrl='/login/auth'
	
	// enable security events
	useSecurityEventListener = true
		
	// track login event
	onInteractiveAuthenticationSuccessEvent = { e, appCtx ->
		// delegate to SpringSecurityEventListener
	}
	
	onAuthenticationSuccessEvent = { e, appCtx ->
		// delegate to SpringSecurityEventListener
	}
	
   useLdap = (ConfigurationHolder.config.security.useLdap ?: false);
   ldapRetrieveDatabaseRoles = (ConfigurationHolder.config.security.ldapRetrieveDatabaseRoles ?: false);
   ldapRetrieveGroupRoles = (ConfigurationHolder.config.security.ldapRetrieveGroupRoles ?: false);
   ldapSearchSubtree = (ConfigurationHolder.config.security.ldapSearchSubtree ?: false);
   ldapServer = (ConfigurationHolder.config.security.ldapServer ?: '');
   ldapManagerDn = (ConfigurationHolder.config.security.ldapManagerDn ?: '');
   ldapManagerPassword = (ConfigurationHolder.config.security.ldapManagerPassword ?: '');
   ldapSearchBase = (ConfigurationHolder.config.security.ldapSearchBase ?: '');
   ldapSearchFilter = (ConfigurationHolder.config.security.ldapSearchFilter ?: '');
   ldapUsePassword = (ConfigurationHolder.config.security.ldapUsePassword ?: false);
   ldapGroupSearchBase = (ConfigurationHolder.config.security.ldapGroupSearchBase ?: '');
   ldapGroupSearchFilter = (ConfigurationHolder.config.security.ldapGroupSearchFilter ?: '');
   ldapGroupRoleAttribute = (ConfigurationHolder.config.security.ldapGroupRoleAttribute ?: '');
   ldapPasswordAttributeName = (ConfigurationHolder.config.security.ldapPasswordAttributeName ?: '');
}
