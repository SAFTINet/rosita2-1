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

public class OmopDrugExposure {

	Long id;
	Long personId;
	Long drugConceptId;
	Date drugExposureStartDate;
	Date drugExposureEndDate;
	Long drugTypeConceptId;
	String stopReason;
	Long refills;
	Double quantity;
	Long daysSupply;
	String sig;
	Long prescribingProviderId;
	Long visitOccurrenceId;
	Long relevantConditionConceptId;
	String xDataSourceType;
	String xDrugName;
	String xDrugStrength;
	String drugSourceValue;
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
	public Long getDrugConceptId() {
		return drugConceptId;
	}
	public void setDrugConceptId(Long drugConceptId) {
		this.drugConceptId = drugConceptId;
	}
	public Date getDrugExposureStartDate() {
		return drugExposureStartDate;
	}
	public void setDrugExposureStartDate(Date drugExposureStartDate) {
		this.drugExposureStartDate = drugExposureStartDate;
	}
	public Date getDrugExposureEndDate() {
		return drugExposureEndDate;
	}
	public void setDrugExposureEndDate(Date drugExposureEndDate) {
		this.drugExposureEndDate = drugExposureEndDate;
	}
	public Long getDrugTypeConceptId() {
		return drugTypeConceptId;
	}
	public void setDrugTypeConceptId(Long drugTypeConceptId) {
		this.drugTypeConceptId = drugTypeConceptId;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	public Long getRefills() {
		return refills;
	}
	public void setRefills(Long refills) {
		this.refills = refills;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Long getDaysSupply() {
		return daysSupply;
	}
	public void setDaysSupply(Long daysSupply) {
		this.daysSupply = daysSupply;
	}
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public Long getPrescribingProviderId() {
		return prescribingProviderId;
	}
	public void setPrescribingProviderId(Long prescribingProviderId) {
		this.prescribingProviderId = prescribingProviderId;
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
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getDrugSourceValue() {
		return drugSourceValue;
	}
	public void setDrugSourceValue(String drugSourceValue) {
		this.drugSourceValue = drugSourceValue;
	}
	public String getxDrugName() {
		return xDrugName;
	}
	public void setxDrugName(String xDrugName) {
		this.xDrugName = xDrugName;
	}
	public String getxDrugStrength() {
		return xDrugStrength;
	}
	public void setxDrugStrength(String xDrugStrength) {
		this.xDrugStrength = xDrugStrength;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	
	
	
}
