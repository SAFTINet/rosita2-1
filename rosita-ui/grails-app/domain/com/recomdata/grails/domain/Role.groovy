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
 * Authority domain class.
 * $Id: $
 * @author $Author: $jspencer
 * @version $Revision: $
 */
class Role {
	
	// role types
	static def ROLE_ADMIN = "ROLE_ADMIN"
	static def ROLE_USER = "ROLE_USER"
	
	static hasMany = [people: Person]
	
	Long id;
	/** ROLE String */
	String authority;
	String description;	
	String displayName;
	Integer securityLevel;
	
	static mapping = {
		table 'cz.au_role_descr'
		people joinTable:[name:'cz.au_person_role', key:'authority_id', column:'person_id']
		version false;
		id  params:[sequence:'cz.SEQ_AU_PK_ID'];
		columns {
			id column:'role_descr_id'
		}
	}
	
	static constraints = {
		authority(blank: false, unique: true, nullable:false)
		displayName(blank:false)
		description(nullable:true)
	}
}
