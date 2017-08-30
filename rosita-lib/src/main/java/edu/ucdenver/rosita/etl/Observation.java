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
@XmlRootElement(name="observation")
public class Observation {
	
	String id;
	String demographicId;
	@XmlElement (name="observation_source_identifier")
	String observationSourceIdentifier;
	@XmlElement (name="x_data_source_type")
	String xDataSourceType;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="observation_source_value")
	String observationSourceValue;
	@XmlElement (name="observation_source_value_vocabulary")
	String observationSourceValueVocabulary;
	@XmlElement (name="observation_date")
	String observationDate;
	@XmlElement (name="observation_time")
	String observationTime;
	@XmlElement (name="value_as_number")
	String valueAsNumber;
	@XmlElement (name="value_as_string")
	String valueAsString;
	@XmlElement (name="unit_source_value")
	String unitSourceValue;
	@XmlElement (name="range_low")
	String rangeLow;
	@XmlElement (name="range_high")
	String rangeHigh;
	@XmlElement (name="observation_type_source_value")
	String observationTypeSourceValue;
	@XmlElement (name="associated_provider_source_value")
	String associatedProviderSourceValue;
	@XmlElement (name="x_visit_occurrence_source_identifier")
	String xVisitOccurrenceSourceIdentifier;
	@XmlElement (name="relevant_condition_source_value")
	String relevantConditionSourceValue;
	@XmlElement (name="x_obs_comment")
	String xObsComment;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.observationSourceIdentifier+"|")
                .append(this.xDataSourceType+"|")
                .append(this.personSourceValue+"|")
                .append(this.observationSourceValue+"|")
                .append(this.observationSourceValueVocabulary+"|")
                .append(this.observationDate+"|")
                .append(this.observationTime+"|")
                .append(this.valueAsNumber+"|")
                .append(this.valueAsString+"|")
                .append(this.unitSourceValue+"|")
                .append(this.rangeLow+"|")
                .append(this.rangeHigh+"|")
                .append(this.observationTypeSourceValue+"|")
                .append(this.associatedProviderSourceValue+"|")
                .append(this.xVisitOccurrenceSourceIdentifier+"|")
                .append(this.relevantConditionSourceValue+"|")
                .append(this.xObsComment+"|\n");

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
	public String getObservationSourceIdentifier() {
		return observationSourceIdentifier;
	}
	public void setObservationSourceIdentifier(String observationSourceIdentifier) {
		this.observationSourceIdentifier = observationSourceIdentifier;
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
	public String getObservationSourceValue() {
		return observationSourceValue;
	}
	public void setObservationSourceValue(String observationSourceValue) {
		this.observationSourceValue = observationSourceValue;
	}
	public String getObservationSourceValueVocabulary() {
		return observationSourceValueVocabulary;
	}
	public void setObservationSourceValueVocabulary(
			String observationSourceValueVocabulary) {
		this.observationSourceValueVocabulary = observationSourceValueVocabulary;
	}
	public String getObservationDate() {
		return observationDate;
	}
	public void setObservationDate(String observationDate) {
		this.observationDate = observationDate;
	}
	public String getObservationTime() {
		return observationTime;
	}
	public void setObservationTime(String observationTime) {
		this.observationTime = observationTime;
	}
	public String getValueAsNumber() {
		return valueAsNumber;
	}
	public void setValueAsNumber(String valueAsNumber) {
		this.valueAsNumber = valueAsNumber;
	}
	public String getValueAsString() {
		return valueAsString;
	}
	public void setValueAsString(String valueAsString) {
		this.valueAsString = valueAsString;
	}
	public String getUnitSourceValue() {
		return unitSourceValue;
	}
	public void setUnitSourceValue(String unitSourceValue) {
		this.unitSourceValue = unitSourceValue;
	}
	public String getRangeLow() {
		return rangeLow;
	}
	public void setRangeLow(String rangeLow) {
		this.rangeLow = rangeLow;
	}
	public String getRangeHigh() {
		return rangeHigh;
	}
	public void setRangeHigh(String rangeHigh) {
		this.rangeHigh = rangeHigh;
	}
	public String getObservationTypeSourceValue() {
		return observationTypeSourceValue;
	}
	public void setObservationTypeSourceValue(String observationTypeSourceValue) {
		this.observationTypeSourceValue = observationTypeSourceValue;
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
	public String getRelevantConditionSourceValue() {
		return relevantConditionSourceValue;
	}
	public void setRelevantConditionSourceValue(String relevantConditionSourceValue) {
		this.relevantConditionSourceValue = relevantConditionSourceValue;
	}
	public String getxObsComment() {
		return xObsComment;
	}
	public void setxObsComment(String xObsComment) {
		this.xObsComment = xObsComment;
	}

}
