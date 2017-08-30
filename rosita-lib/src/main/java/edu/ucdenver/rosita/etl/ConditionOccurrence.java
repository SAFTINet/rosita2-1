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

package edu.ucdenver.rosita.etl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( XmlAccessType.NONE )
@XmlRootElement(name="condition_occurrence")
public class ConditionOccurrence {
	
	String id;
	String demographicId;
	@XmlElement (name="condition_occurrence_source_identifier")
	String conditionOccurrenceSourceIdentifier;
	@XmlElement (name="x_data_source_type")
	String xDataSourceType;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="condition_source_value")
	String conditionSourceValue;
	@XmlElement (name="condition_source_value_vocabulary")
	String conditionSourceValueVocabulary;
	@XmlElement (name="x_condition_source_desc")
	String xConditionSourceDesc;
	@XmlElement (name="condition_start_date")
	String conditionStartDate;
	@XmlElement (name="x_condition_update_date")
	String xConditionUpdateDate;
	@XmlElement (name="condition_end_date")
	String conditionEndDate;
	@XmlElement (name="condition_type_source_value")
	String conditionTypeSourceValue;
	@XmlElement (name="stop_reason")
	String stopReason;
	@XmlElement (name="associated_provider_source_value")
	String associatedProviderSourceValue;
	@XmlElement (name="x_visit_occurrence_source_identifier")
	String xVisitOccurrenceSourceIdentifier;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.conditionOccurrenceSourceIdentifier+"|")
                .append(this.xDataSourceType+"|")
                .append(this.personSourceValue+"|")
                .append(this.conditionSourceValue+"|")
                .append(this.conditionSourceValueVocabulary+"|")
                .append(this.xConditionSourceDesc+"|")
                .append(this.conditionStartDate+"|")
                .append(this.xConditionUpdateDate+"|")
                .append(this.conditionEndDate+"|")
                .append(this.conditionTypeSourceValue+"|")
                .append(this.stopReason+"|")
                .append(this.associatedProviderSourceValue+"|")
                .append(this.xVisitOccurrenceSourceIdentifier+"|\n");

        return strbuf.toString();
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDemographicId() {
		return demographicId;
	}
	public void setDemographicId(String demographicId) {
		this.demographicId = demographicId;
	}
	public String getConditionOccurrenceSourceIdentifier() {
		return conditionOccurrenceSourceIdentifier;
	}
	public void setConditionOccurrenceSourceIdentifier(
			String conditionOccurrenceSourceIdentifier) {
		this.conditionOccurrenceSourceIdentifier = conditionOccurrenceSourceIdentifier;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getPersonSourceValue() {
		return personSourceValue;
	}
	public void setPersonSourceValue(String personSourceValue) {
		this.personSourceValue = personSourceValue;
	}
	public String getConditionSourceValue() {
		return conditionSourceValue;
	}
	public void setConditionSourceValue(String conditionSourceValue) {
		this.conditionSourceValue = conditionSourceValue;
	}
	public String getConditionSourceValueVocabulary() {
		return conditionSourceValueVocabulary;
	}
	public void setConditionSourceValueVocabulary(
			String conditionSourceValueVocabulary) {
		this.conditionSourceValueVocabulary = conditionSourceValueVocabulary;
	}
	public String getxConditionSourceDesc() {
		return xConditionSourceDesc;
	}
	public void setxConditionSourceDesc(String xConditionSourceDesc) {
		this.xConditionSourceDesc = xConditionSourceDesc;
	}
	public String getConditionStartDate() {
		return conditionStartDate;
	}
	public void setConditionStartDate(String conditionStartDate) {
		this.conditionStartDate = conditionStartDate;
	}
	public String getxConditionUpdateDate() {
		return xConditionUpdateDate;
	}
	public void setxConditionUpdateDate(String xConditionUpdateDate) {
		this.xConditionUpdateDate = xConditionUpdateDate;
	}
	public String getConditionEndDate() {
		return conditionEndDate;
	}
	public void setConditionEndDate(String conditionEndDate) {
		this.conditionEndDate = conditionEndDate;
	}
	public String getConditionTypeSourceValue() {
		return conditionTypeSourceValue;
	}
	public void setConditionTypeSourceValue(String conditionTypeSourceValue) {
		this.conditionTypeSourceValue = conditionTypeSourceValue;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	public String getAssociatedProviderSourceValue() {
		return associatedProviderSourceValue;
	}
	public void setAssociatedProviderSourceValue(
			String associatedProviderSourceValue) {
		this.associatedProviderSourceValue = associatedProviderSourceValue;
	}
	public String getxVisitOccurrenceSourceIdentifier() {
		return xVisitOccurrenceSourceIdentifier;
	}
	public void setxVisitOccurrenceSourceIdentifier(String xVisitOccurrenceSourceIdentifier) {
		this.xVisitOccurrenceSourceIdentifier = xVisitOccurrenceSourceIdentifier;
	}

}
