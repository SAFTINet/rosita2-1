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

class User extends Principal {
	
	String providerCode
	
	static belongsTo = UserGroup
	static hasMany = [groups:UserGroup]
	                  
	static mapping  = {
		groups joinTable: [name: 'cz.au_user_group_member', column: 'au_group_id', key: 'au_user_id' ], lazy:false
		//	discriminator column:'PRINCIPAL_TYPE', sqlType:'VARCHAR',length:100, value:'USER'
		discriminator 'USER'		
	}
	
	static constraints = {
		providerCode(nullable:true, maxSize:255)
	}
	
	public User() {
		this.category='USER'
	}
	
	public IsAdmin() {
		//log.info("Trying to deterimine if admin user attached: "+this.isAttached())
		def a=this.groups.findAll{it.name.equals("Administrators")}
		return(a.size()>0)
	}
}
