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
@XmlRootElement(name="drug_exposure")
public class DrugExposure {
	
	String id;
	String demographicId;
	@XmlElement (name="drug_exposure_source_identifier")
	String drugExposureSourceIdentifier;
	@XmlElement (name="x_data_source_type")
	String xDataSourceType;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="drug_source_value")
	String drugSourceValue;
	@XmlElement (name="drug_source_value_vocabulary")
	String drugSourceValueVocabulary;
	@XmlElement (name="drug_exposure_start_date")
	String drugExposureStartDate;
	@XmlElement (name="drug_exposure_end_date")
	String drugExposureEndDate;
	@XmlElement (name="drug_type_source_value")
	String drugTypeSourceValue;
	@XmlElement (name="stop_reason")
	String stopReason;
	@XmlElement (name="refills")
	String refills;
	@XmlElement (name="quantity")
	String quantity;
	@XmlElement (name="days_supply")
	String daysSupply;
	@XmlElement (name="x_drug_name")
	String xDrugName;
	@XmlElement (name="x_drug_strength")
	String xDrugStrength;
	@XmlElement (name="sig")
	String sig;
	@XmlElement (name="provider_source_value")
	String providerSourceValue;
	@XmlElement (name="x_visit_occurrence_source_identifier")
	String xVisitOccurrenceSourceIdentifier;
	@XmlElement (name="relevant_condition_source_value")
	String relevantConditionSourceValue;
	@XmlElement (name="drug_cost")
	Collection<DrugCost> drugCost = new ArrayList<DrugCost>();

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.drugExposureSourceIdentifier+"|")
                .append(this.xDataSourceType+"|")
                .append(this.personSourceValue+"|")
                .append(this.drugSourceValue+"|")
                .append(this.drugSourceValueVocabulary+"|")
                .append(this.drugExposureStartDate+"|")
                .append(this.drugExposureEndDate+"|")
                .append(this.drugTypeSourceValue+"|")
                .append(this.stopReason+"|")
                .append(this.refills+"|")
                .append(this.quantity+"|")
                .append(this.daysSupply+"|")
                .append(this.xDrugName+"|")
                .append(this.xDrugStrength+"|")
                .append(this.sig+"|")
                .append(this.providerSourceValue+"|")
                .append(this.xVisitOccurrenceSourceIdentifier+"|")
                .append(this.relevantConditionSourceValue+"|")
                .append(this.drugCost+"|\n");

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
	public String getDrugExposureSourceIdentifier() {
		return drugExposureSourceIdentifier;
	}
	public void setDrugExposureSourceIdentifier(String drugExposureSourceIdentifier) {
		this.drugExposureSourceIdentifier = drugExposureSourceIdentifier;
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
	public String getDrugSourceValue() {
		return drugSourceValue;
	}
	public void setDrugSourceValue(String drugSourceValue) {
		this.drugSourceValue = drugSourceValue;
	}
	public String getDrugSourceValueVocabulary() {
		return drugSourceValueVocabulary;
	}
	public void setDrugSourceValueVocabulary(String drugSourceValueVocabulary) {
		this.drugSourceValueVocabulary = drugSourceValueVocabulary;
	}
	public String getDrugExposureStartDate() {
		return drugExposureStartDate;
	}
	public void setDrugExposureStartDate(String drugExposureStartDate) {
		this.drugExposureStartDate = drugExposureStartDate;
	}
	public String getDrugExposureEndDate() {
		return drugExposureEndDate;
	}
	public void setDrugExposureEndDate(String drugExposureEndDate) {
		this.drugExposureEndDate = drugExposureEndDate;
	}
	public String getDrugTypeSourceValue() {
		return drugTypeSourceValue;
	}
	public void setDrugTypeSourceValue(String drugTypeSourceValue) {
		this.drugTypeSourceValue = drugTypeSourceValue;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	public String getRefills() {
		return refills;
	}
	public void setRefills(String refills) {
		this.refills = refills;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getDaysSupply() {
		return daysSupply;
	}
	public void setDaysSupply(String daysSupply) {
		this.daysSupply = daysSupply;
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
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public String getProviderSourceValue() {
		return providerSourceValue;
	}
	public void setProviderSourceValue(String providerSourceValue) {
		this.providerSourceValue = providerSourceValue;
	}
	public String getxVisitOccurrenceSourceIdentifier() {
		return xVisitOccurrenceSourceIdentifier;
	}
	public void setxVisitOccurrenceSourceIdentifier(
			String xVisitOccurrenceSourceIdentifier) {
		this.xVisitOccurrenceSourceIdentifier = xVisitOccurrenceSourceIdentifier;
	}
	public String getRelevantConditionSourceValue() {
		return relevantConditionSourceValue;
	}
	public void setRelevantConditionSourceValue(String relevantConditionSourceValue) {
		this.relevantConditionSourceValue = relevantConditionSourceValue;
	}
	public Collection<DrugCost> getDrugCost() {
		return drugCost;
	}
	public void setDrugCost(Collection<DrugCost> drugCost) {
		this.drugCost = drugCost;
	}

}
