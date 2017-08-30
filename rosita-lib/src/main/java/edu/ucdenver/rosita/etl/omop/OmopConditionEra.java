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

package edu.ucdenver.rosita.etl.omop;

import java.util.Date;

public class OmopConditionEra {

	Long id;
	Long personId;
	Long conditionConceptId;
	Date conditionEraStartDate;
	Date conditionEraEndDate;
	Long conditionTypeConceptId;
	Long conditionOccurrenceCount;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Long getConditionConceptId() {
		return conditionConceptId;
	}
	public void setConditionConceptId(Long conditionConceptId) {
		this.conditionConceptId = conditionConceptId;
	}
	public Date getConditionEraStartDate() {
		return conditionEraStartDate;
	}
	public void setConditionEraStartDate(Date conditionEraStartDate) {
		this.conditionEraStartDate = conditionEraStartDate;
	}
	public Date getConditionEraEndDate() {
		return conditionEraEndDate;
	}
	public void setConditionEraEndDate(Date conditionEraEndDate) {
		this.conditionEraEndDate = conditionEraEndDate;
	}
	public Long getConditionTypeConceptId() {
		return conditionTypeConceptId;
	}
	public void setConditionTypeConceptId(Long conditionTypeConceptId) {
		this.conditionTypeConceptId = conditionTypeConceptId;
	}
	public Long getConditionOccurrenceCount() {
		return conditionOccurrenceCount;
	}
	public void setConditionOccurrenceCount(Long conditionOccurrenceCount) {
		this.conditionOccurrenceCount = conditionOccurrenceCount;
	}
	

}
