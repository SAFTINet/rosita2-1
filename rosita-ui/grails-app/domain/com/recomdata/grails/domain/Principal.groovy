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

class Principal implements Serializable {
	
	Long id
	//	String type='BASE'
	String securityIdentifier
	String category
	Date dateCreated
	Date lastUpdated
	String name
	String username
	
	//Set<AssignedPermission> permissions = new HashSet<AssignedPermission>()
	//static hasMany = [permissions:AssignedPermission]
	
	static mapping  = {
		table  'cz.au_principal'
		tablePerHierarchy true
		version false
		//  discriminator column:'PRINCIPAL_TYPE', sqlType:'VARCHAR',length:100, value:"BASE"
		id  params:[sequence:'cz.SEQ_AU_PK_ID'];
		columns {
			id column:'principal_id'
		}
	}
	static constraints = {
		securityIdentifier(nullable:true, maxSize:255)
		category(nullable:false, maxSize:255)
		name(nullable:false) // need unique here...
		username(nullable:true, maxSize:255)
		//principalClass(nullable:true, maxSize:255)
	}
	
	def afterInsert ={
		//if(this.securityIdentifier==null)
		//{
		//	this.securityIdentifier=+":"+this.id; //generate a uid
		//	this.save();
		//}
	}
}
