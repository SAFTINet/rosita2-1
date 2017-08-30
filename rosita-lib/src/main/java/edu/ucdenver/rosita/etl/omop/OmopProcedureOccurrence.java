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

public class OmopProcedureOccurrence {

	Long id;
	Long personId;
	Long procedureConceptId;
	Date procedureDate;
	Long procedureTypeConceptId;
	Long associatedProviderId;
	Long visitOccurrenceId;
	Long relevantConditionConceptId;
	String procedureSourceValue;
	String xDataSourceType;
	Long xGridNodeId;
	
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
	public Long getProcedureConceptId() {
		return procedureConceptId;
	}
	public void setProcedureConceptId(Long procedureConceptId) {
		this.procedureConceptId = procedureConceptId;
	}
	public Date getProcedureDate() {
		return procedureDate;
	}
	public void setProcedureDate(Date procedureDate) {
		this.procedureDate = procedureDate;
	}
	public Long getProcedureTypeConceptId() {
		return procedureTypeConceptId;
	}
	public void setProcedureTypeConceptId(Long procedureTypeConceptId) {
		this.procedureTypeConceptId = procedureTypeConceptId;
	}
	public Long getAssociatedProviderId() {
		return associatedProviderId;
	}
	public void setAssociatedProviderId(Long associatedProviderId) {
		this.associatedProviderId = associatedProviderId;
	}
	public Long getVisitOccurrenceId() {
		return visitOccurrenceId;
	}
	public void setVisitOccurrenceId(Long visitOccurrenceId) {
		this.visitOccurrenceId = visitOccurrenceId;
	}
	public Long getRelevantConditionConceptId() {
		return relevantConditionConceptId;
	}
	public void setRelevantConditionConceptId(Long relevantConditionConceptId) {
		this.relevantConditionConceptId = relevantConditionConceptId;
	}
	public String getProcedureSourceValue() {
		return procedureSourceValue;
	}
	public void setProcedureSourceValue(String procedureSourceValue) {
		this.procedureSourceValue = procedureSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	
}
