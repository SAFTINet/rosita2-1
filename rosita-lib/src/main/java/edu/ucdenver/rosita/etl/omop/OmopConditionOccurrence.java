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

public class OmopConditionOccurrence {

	Long id;
	Long personId;
	Long conditionConceptId;
	Date conditionStartDate;
	Date conditionEndDate;
	Long conditionTypeConceptId;
	String stopReason;
	Long associatedProviderId;
	Long visitOccurrenceId;
	String conditionSourceValue;
	String xDataSourceType;
	String xConditionSourceDesc;
	Date xConditionUpdateDate;
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
	public Long getConditionConceptId() {
		return conditionConceptId;
	}
	public void setConditionConceptId(Long conditionConceptId) {
		this.conditionConceptId = conditionConceptId;
	}
	public Date getConditionStartDate() {
		return conditionStartDate;
	}
	public void setConditionStartDate(Date conditionStartDate) {
		this.conditionStartDate = conditionStartDate;
	}
	public Date getConditionEndDate() {
		return conditionEndDate;
	}
	public void setConditionEndDate(Date conditionEndDate) {
		this.conditionEndDate = conditionEndDate;
	}
	public Long getConditionTypeConceptId() {
		return conditionTypeConceptId;
	}
	public void setConditionTypeConceptId(Long conditionTypeConceptId) {
		this.conditionTypeConceptId = conditionTypeConceptId;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
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
	public String getConditionSourceValue() {
		return conditionSourceValue;
	}
	public void setConditionSourceValue(String conditionSourceValue) {
		this.conditionSourceValue = conditionSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getxConditionSourceDesc() {
		return xConditionSourceDesc;
	}
	public void setxConditionSourceDesc(String xConditionSourceDesc) {
		this.xConditionSourceDesc = xConditionSourceDesc;
	}
	public Date getxConditionUpdateDate() {
		return xConditionUpdateDate;
	}
	public void setxConditionUpdateDate(Date xConditionUpdateDate) {
		this.xConditionUpdateDate = xConditionUpdateDate;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	


}
