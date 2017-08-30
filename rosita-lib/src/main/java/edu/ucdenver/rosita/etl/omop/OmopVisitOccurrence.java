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

public class OmopVisitOccurrence {

	Long id;
	Long personId;
	Date visitStartDate;
	Date visitEndDate;
	Long placeOfServiceConceptId;
	Long careSiteId;
	String placeOfServiceSourceValue;
	String xVisitOccurrenceSourceIdentifier;
	String xDataSourceType;
	Long xProviderId;
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
	public Date getVisitStartDate() {
		return visitStartDate;
	}
	public void setVisitStartDate(Date visitStartDate) {
		this.visitStartDate = visitStartDate;
	}
	public Date getVisitEndDate() {
		return visitEndDate;
	}
	public void setVisitEndDate(Date visitEndDate) {
		this.visitEndDate = visitEndDate;
	}
	public Long getPlaceOfServiceConceptId() {
		return placeOfServiceConceptId;
	}
	public void setPlaceOfServiceConceptId(Long placeOfServiceConceptId) {
		this.placeOfServiceConceptId = placeOfServiceConceptId;
	}
	public Long getCareSiteId() {
		return careSiteId;
	}
	public void setCareSiteId(Long careSiteId) {
		this.careSiteId = careSiteId;
	}
	public String getPlaceOfServiceSourceValue() {
		return placeOfServiceSourceValue;
	}
	public void setPlaceOfServiceSourceValue(String placeOfServiceSourceValue) {
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	public String getxVisitOccurrenceSourceIdentifier() {
		return xVisitOccurrenceSourceIdentifier;
	}
	public void setxVisitOccurrenceSourceIdentifier(
			String xVisitOccurrenceSourceIdentifier) {
		this.xVisitOccurrenceSourceIdentifier = xVisitOccurrenceSourceIdentifier;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public Long getxProviderId() {
		return xProviderId;
	}
	public void setxProviderId(Long xProviderId) {
		this.xProviderId = xProviderId;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	
	
}
