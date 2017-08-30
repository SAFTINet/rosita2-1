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

import com.rdc.domain.DomainPermissionDetails

/**
 * Domain class for Spring user for authentication
 * @author jspencer
 */
public class Person implements Serializable {
	// internal system admin
	static def SYSTEM_ADMIN_USER = "sadmin"
	
	// injections
	def authenticateService;
	def userService;
	
	static transients = ['pass']
	
	Long id
	String username
	/** MD5 Password */
	String password
	/** plain password to create a MD5 password */
	String pass = '[secret]'
		
	boolean enabled = true
	String email
	boolean emailShow = true
	
	// permissioned principal
	User principal	
	
	static hasMany = [authorities: Role]
	static belongsTo = Role	
	
	static mapping = {
		table 'cz.au_person'
		version false
		authorities joinTable:[name:'cz.au_person_role', key:'person_id', column:'authority_id']
		id  params:[sequence:'cz.SEQ_AU_PK_ID'];
		columns {
			id column:'person_id'
			principal column:'principal_id',lazy:false
		}
	}
	
	static constraints = {
		username(blank: false, unique: true)
		password(blank: false, minSize:7,
			validator: { val, obj ->
				if(!(val=~ /(\w)/)){
					return false
				}
				
				if(!(val=~ /(\d)/)){
					return false
				}
			}
		)
		
		email(maxSize:255, blank:false, email:true)
		principal(nullable:true)
		//authorities(nullable:false, minSize:1)
	}
	
	def beforeInsert = {
		if(this.principal==null) this.principal = new User();
		synchonizePrincipal();		
	}
	
	def beforeUpdate = {		
		// make sure principal exists
		if(this.principal==null) {
			this.principal = new User();		
		} else {
			this.principal = principal.merge();		
		}			
		synchonizePrincipal();
	}

	/** 
	 * on insert and updates, synchronize attributes
	 * with principal
	 */
	private void synchonizePrincipal() {
		log.info("principal: "+principal)
		this.principal.username = username;
		this.principal.name = username;
		this.principal.category = "USER";
		
		if( !this.principal.validate() ) {
			this.principal.errors.each {
				println it
			}
		}
		this.principal.save()		
	}
	
	/**
	 * useful helper function
	 */
	def boolean hasRole(String role) {
		boolean bMatch = false;
		authorities.each {
			if(it.authority.equals(role)) bMatch = true; 
		}		
		return bMatch;	
	}
		
	/**
	* convenience method
	*/
   def getDisplayName(){
	   StringBuilder s = new StringBuilder();
	   if(lastName!=null) s.append(lastName).append(", ");
	   if(firstName!=null) s.append(firstName);
	   if(title!=null) s.append(" [ ").append(title).append(" ]");
	   return s.toString();
   }
   
	/**
	 * override for equals comparison
	 */
	public int hashCode() {
		return id?.intValue()
	}
	
	/**
	 * override for equals comparisons
	 */
	public boolean equals(Object obj) {
		if(obj instanceof Person) {
			Person user = (Person) obj;
			//log.info "user equals "+this.id.equals(user.id);
			return this.id.equals(user.id);
		}
		return false;
	}   
   
	/**
	 * determine if user has permission to edit the instance
	 */
	def boolean canEdit() {
		if(id==null) return false;
		
		return userService.canCreate();
	}
	
	/**
	 * only allow user to edit a role if a different user
	 * @return
	 */
	def boolean canEditRole() {
		if(id==null) return true;
		
		// admin granted permission
		if(authenticateService.ifAnyGranted(Role.ROLE_ADMIN)) return true;
				
		// same user?
		Person user = authenticateService.userDomain();
		if(user.id==id) return false;
		
		return true;
	}
	
	/**
	 * determine if user has permission to delete the instance
	 * @return
	 */
	def boolean canDelete() {
		
		// cannot delete self
		Person user = authenticateService.userDomain();
		if(user.id==id) return false;
		
		// restrict regular staff users
		return authenticateService.ifAnyGranted(Role.ROLE_ADMIN);
	}
	
	/**
	 * get standard CRUD permissions for authenticated user
	 * @return
	 */
	def DomainPermissionDetails evalPermissions() {
		DomainPermissionDetails perm = new DomainPermissionDetails();
		perm.canCreate = true //userService.canCreate();
		perm.canUpdate = true //canEdit();
		perm.canDelete = true //canDelete();
		//perm.canSpecialUpdate = canEditRestrictedFields();
		return perm;
	}
		
	/**
	 * @autogenerated by CodeHaggis (http://sourceforge.net/projects/haggis)
	 * @overwrite toString()
	 * @return String returns this object in a String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Person::[");
		sb.append(" id:=");
		sb.append(id);
		sb.append(" username:=");
		sb.append(username);
		sb.append(" password:=");
		sb.append(password);
		sb.append(" enabled:=");
		sb.append(enabled);
		sb.append(" email:=");
		sb.append(email);
		sb.append(" emailShow:=");
		sb.append(emailShow);
		sb.append(" principal:=");
		sb.append(principal);
		sb.append(']');
		return sb.toString();
	}	
}
