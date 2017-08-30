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

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType( XmlAccessType.NONE )
@XmlRootElement(name="procedure_occurrence")
public class ProcedureOccurrence {
	
	String id;
	String demographicId;
	@XmlElement (name="procedure_occurrence_source_identifier")
	String procedureOccurrenceSourceIdentifier;
	@XmlElement (name="x_data_source_type")
	String xDataSourceType;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="procedure_source_value")
	String procedureSourceValue;
	@XmlElement (name="procedure_source_value_vocabulary")
	String procedureSourceValueVocabulary;
	@XmlElement (name="procedure_date")
	String procedureDate;
	@XmlElement (name="procedure_type_source_value")
	String procedureTypeSourceValue;
	@XmlElement (name="provider_record_source_value")
	String providerRecordSourceValue;
	@XmlElement (name="x_visit_occurrence_source_identifier")
	String xVisitOccurrenceSourceIdentifier;
	@XmlElement (name="relevant_condition_source_value")
	String relevantConditionSourceValue;
	@XmlElement (name="procedure_cost")
	Collection<ProcedureCost> procedureCost = new ArrayList<ProcedureCost>();

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.procedureOccurrenceSourceIdentifier+"|")
                .append(this.xDataSourceType+"|")
                .append(this.personSourceValue+"|")
                .append(this.procedureSourceValue+"|")
                .append(this.procedureSourceValueVocabulary+"|")
                .append(this.procedureDate+"|")
                .append(this.procedureTypeSourceValue+"|")
                .append(this.providerRecordSourceValue+"|")
                .append(this.xVisitOccurrenceSourceIdentifier+"|")
                .append(this.relevantConditionSourceValue+"|\n");

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
	public String getProcedureOccurrenceSourceIdentifier() {
		return procedureOccurrenceSourceIdentifier;
	}
	public void setProcedureOccurrenceSourceIdentifier(
			String procedureOccurrenceSourceIdentifier) {
		this.procedureOccurrenceSourceIdentifier = procedureOccurrenceSourceIdentifier;
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
	public String getProcedureSourceValue() {
		return procedureSourceValue;
	}
	public void setProcedureSourceValue(String procedureSourceValue) {
		this.procedureSourceValue = procedureSourceValue;
	}
	public String getProcedureSourceValueVocabulary() {
		return procedureSourceValueVocabulary;
	}
	public void setProcedureSourceValueVocabulary(
			String procedureSourceValueVocabulary) {
		this.procedureSourceValueVocabulary = procedureSourceValueVocabulary;
	}
	public String getProcedureDate() {
		return procedureDate;
	}
	public void setProcedureDate(String procedureDate) {
		this.procedureDate = procedureDate;
	}
	public String getProcedureTypeSourceValue() {
		return procedureTypeSourceValue;
	}
	public void setProcedureTypeSourceValue(String procedureTypeSourceValue) {
		this.procedureTypeSourceValue = procedureTypeSourceValue;
	}
	public String getProviderRecordSourceValue() {
		return providerRecordSourceValue;
	}
	public void setProviderRecordSourceValue(String providerRecordSourceValue) {
		this.providerRecordSourceValue = providerRecordSourceValue;
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
	public Collection<ProcedureCost> getProcedureCost() {
		return procedureCost;
	}
	public void setProcedureCost(Collection<ProcedureCost> procedureCost) {
		this.procedureCost = procedureCost;
	}

}
