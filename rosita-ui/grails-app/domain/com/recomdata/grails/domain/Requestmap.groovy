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
 * $Id: $
 * @author $Author: $
 * @version $Revision: $
 */

/**
 * Request Map domain class.
 */
class Requestmap {
	
	Long id
	String url
	String configAttribute
	Long version
	
	static mapping ={
		table 'cz.au_request_map'
		//id generator:'sequence', params:[sequence:'SEQ_SEARCH_DATA_ID']
		id  params:[sequence:'cz.SEQ_AU_PK_ID'];
		columns {
			id column:'request_map_id'
		}
	}
	static constraints = {
		url(blank: false, unique: true)
		configAttribute(blank: false)
	}
}
