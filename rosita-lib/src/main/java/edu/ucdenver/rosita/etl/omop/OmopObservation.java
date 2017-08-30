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

public class OmopObservation {

	Long id;
	Long personId;
	Long observationConceptId;
	Date observationDate;
	Date observationTime;
	Double valueAsNumber;
	String valueAsString;
	Long valueAsConceptId;
	Long unitConceptId;
	Double rangeLow;
	Double rangeHigh;
	Long observationTypeConceptId;
	Long associatedProviderId;
	Long visitOccurrenceId;
	Long relevantConditionConceptId;
	String observationSourceValue;
	String unitSourceValue;
	String xDataSourceType;
	String xObsComment;	
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
	public Long getObservationConceptId() {
		return observationConceptId;
	}
	public void setObservationConceptId(Long observationConceptId) {
		this.observationConceptId = observationConceptId;
	}
	public Date getObservationDate() {
		return observationDate;
	}
	public void setObservationDate(Date observationDate) {
		this.observationDate = observationDate;
	}
	public Date getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(Date observationTime) {
		this.observationTime = observationTime;
	}
	public Double getValueAsNumber() {
		return valueAsNumber;
	}
	public void setValueAsNumber(Double valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}
	public String getValueAsString() {
		return valueAsString;
	}
	public void setValueAsString(String valueAsString) {
		this.valueAsString = valueAsString;
	}
	public Long getValueAsConceptId() {
		return valueAsConceptId;
	}
	public void setValueAsConceptId(Long valueAsConceptId) {
		this.valueAsConceptId = valueAsConceptId;
	}
	public Long getUnitConceptId() {
		return unitConceptId;
	}
	public void setUnitConceptId(Long unitConceptId) {
		this.unitConceptId = unitConceptId;
	}
	public Double getRangeLow() {
		return rangeLow;
	}
	public void setRangeLow(Double rangeLow) {
		this.rangeLow = rangeLow;
	}
	public Double getRangeHigh() {
		return rangeHigh;
	}
	public void setRangeHigh(Double rangeHigh) {
		this.rangeHigh = rangeHigh;
	}
	public Long getObservationTypeConceptId() {
		return observationTypeConceptId;
	}
	public void setObservationTypeConceptId(Long observationTypeConceptId) {
		this.observationTypeConceptId = observationTypeConceptId;
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
	public String getObservationSourceValue() {
		// NOTE: The observation source value field in the GRID model is limited to 20 characters.
		if (observationSourceValue != null && observationSourceValue.length() > 20) {
			return observationSourceValue.substring(0, 20);
		}
		return observationSourceValue;
	}
	public void setObservationSourceValue(String observationSourceValue) {
		this.observationSourceValue = observationSourceValue;
	}
	public String getUnitSourceValue() {
		return unitSourceValue;
	}
	public void setUnitSourceValue(String unitSourceValue) {
		this.unitSourceValue = unitSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getxObsComment() {
		return xObsComment;
	}
	public void setxObsComment(String xObsComment) {
		this.xObsComment = xObsComment;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}

	
	
	
}
