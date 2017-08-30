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
 * Copyright(c) 2010-2011 Recombinant Data Corp., All rights Reserved
 ******************************************************************************/

package com.recomdata.grails.domain

public class Permission implements Serializable {
	
	Long id;
	String name;
	String description;
	String uid;
	
	static mapping  = {
		table  'cz.au_permission'
		version false
		id  params:[sequence:'cz.SEQ_AU_PK_ID'];
		columns {
			id column: "id"
			name column: "name", nullable:false, length:50
			description column:"description", nullable:true, length:500
			uid column:"unique_id"
		}
	}
	
	static constraints = {
		name(maxSize:50, nullable: false)
		description(maxSize:500, nullable: true)
	}
}
