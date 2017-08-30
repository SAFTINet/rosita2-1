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
@XmlRootElement(name="x_demographic")
public class Demographic {

	String id;
	String organizationId;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="x_data_source_type")
	String xDataSourceType;
	@XmlElement (name="medicaid_id_number")
	String medicaidIdNumber;
	@XmlElement (name="ssn")
	String ssn;
	@XmlElement (name="last")
	String last;
	@XmlElement (name="middle")
	String middle;
	@XmlElement (name="first")
	String first;
	@XmlElement (name="address_1")
	String address1;
	@XmlElement (name="address_2")
	String address2;
	@XmlElement (name="city")
	String city;
	@XmlElement (name="state")
	String state;
	@XmlElement (name="zip")
	String zip;
	@XmlElement (name="county")
	String county;
	@XmlElement (name="year_of_birth")
	String yearOfBirth;
	@XmlElement (name="month_of_birth")
	String monthOfBirth;
	@XmlElement (name="day_of_birth")
	String dayOfBirth;
	@XmlElement (name="gender_source_value")
	String genderSourceValue;
	@XmlElement (name="race_source_value")
	String raceSourceValue;
	@XmlElement (name="ethnicity_source_value")
	String ethnicitySourceValue;
	@XmlElement (name="provider_source_value")
	String providerSourceValue;
	@XmlElement (name="care_site_source_value")
	String careSiteSourceValue;
	@XmlElement (name="x_organization_source_value")
	String xOrganizationSourceValue;
	@XmlElement (name="visit_occurrence")
	Collection<VisitOccurrence> visitOccurrence = new ArrayList<VisitOccurrence>();
	@XmlElement (name="observation")
	Collection<Observation> observation = new ArrayList<Observation>();
	@XmlElement (name="drug_exposure")
	Collection<DrugExposure> drugExposure = new ArrayList<DrugExposure>();
	@XmlElement (name="condition_occurrence")
	Collection<ConditionOccurrence> conditionOccurrence = new ArrayList<ConditionOccurrence>();
	@XmlElement (name="procedure_occurrence")
	Collection<ProcedureOccurrence> procedureOccurrence = new ArrayList<ProcedureOccurrence>();
	@XmlElement (name="cohort")
	Collection<Cohort> cohort = new ArrayList<Cohort>();
	@XmlElement (name="death")
	Collection<Death> death = new ArrayList<Death>();
	@XmlElement (name="payer_plan_period")
	Collection<PayerPlanPeriod> payerPlanPeriod = new ArrayList<PayerPlanPeriod>();

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.personSourceValue+"|")
                .append(this.xDataSourceType+"|")
                .append(this.medicaidIdNumber+"|")
                .append(this.ssn+"|")
                .append(this.last+"|")
                .append(this.middle+"|")
                .append(this.first+"|")
                .append(this.address1+"|")
                .append(this.address2+"|")
                .append(this.city+"|")
                .append(this.state+"|")
                .append(this.zip+"|")
                .append(this.county+"|")
                .append(this.yearOfBirth+"|")
                .append(this.monthOfBirth+"|")
                .append(this.dayOfBirth+"|")
                .append(this.genderSourceValue+"|")
                .append(this.raceSourceValue+"|")
                .append(this.ethnicitySourceValue+"|")
                .append(this.providerSourceValue+"|")
                .append(this.careSiteSourceValue+"|")
                .append(this.xOrganizationSourceValue+"|\n");

        return strbuf.toString();
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getPersonSourceValue() {
		return personSourceValue;
	}
	public void setPersonSourceValue(String personSourceValue) {
		this.personSourceValue = personSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getMedicaidIdNumber() {
		return medicaidIdNumber;
	}
	public void setMedicaidIdNumber(String medicaidIdNumber) {
		this.medicaidIdNumber = medicaidIdNumber;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getLast() {
		return last;
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getMiddle() {
		return middle;
	}
	public void setMiddle(String middle) {
		this.middle = middle;
	}
	public String getFirst() {
		return first;
	}
	public void setFirst(String first) {
		this.first = first;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getYearOfBirth() {
		return yearOfBirth;
	}
	public void setYearOfBirth(String yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	public String getMonthOfBirth() {
		return monthOfBirth;
	}
	public void setMonthOfBirth(String monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}
	public String getDayOfBirth() {
		return dayOfBirth;
	}
	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}
	public String getGenderSourceValue() {
		return genderSourceValue;
	}
	public void setGenderSourceValue(String genderSourceValue) {
		this.genderSourceValue = genderSourceValue;
	}
	public String getRaceSourceValue() {
		return raceSourceValue;
	}
	public void setRaceSourceValue(String raceSourceValue) {
		this.raceSourceValue = raceSourceValue;
	}
	public String getEthnicitySourceValue() {
		return ethnicitySourceValue;
	}
	public void setEthnicitySourceValue(String ethnicitySourceValue) {
		this.ethnicitySourceValue = ethnicitySourceValue;
	}
	public String getProviderSourceValue() {
		return providerSourceValue;
	}
	public void setProviderSourceValue(String providerSourceValue) {
		this.providerSourceValue = providerSourceValue;
	}
	public String getCareSiteSourceValue() {
		return careSiteSourceValue;
	}
	public void setCareSiteSourceValue(String careSiteSourceValue) {
		this.careSiteSourceValue = careSiteSourceValue;
	}
	public String getxOrganizationSourceValue() {
		return xOrganizationSourceValue;
	}
	public void setxOrganizationSourceValue(String xOrganizationSourceValue) {
		this.xOrganizationSourceValue = xOrganizationSourceValue;
	}
	public Collection<VisitOccurrence> getVisitOccurrence() {
		return visitOccurrence;
	}
	public void setVisitOccurrence(Collection<VisitOccurrence> visitOccurrence) {
		this.visitOccurrence = visitOccurrence;
	}
	public Collection<Observation> getObservation() {
		return observation;
	}
	public void setObservation(Collection<Observation> observation) {
		this.observation = observation;
	}
	public Collection<DrugExposure> getDrugExposure() {
		return drugExposure;
	}
	public void setDrugExposure(Collection<DrugExposure> drugExposure) {
		this.drugExposure = drugExposure;
	}
	public Collection<ConditionOccurrence> getConditionOccurrence() {
		return conditionOccurrence;
	}
	public void setConditionOccurrence(
			Collection<ConditionOccurrence> conditionOccurrence) {
		this.conditionOccurrence = conditionOccurrence;
	}
	public Collection<ProcedureOccurrence> getProcedureOccurrence() {
		return procedureOccurrence;
	}
	public void setProcedureOccurrence(
			Collection<ProcedureOccurrence> procedureOccurrence) {
		this.procedureOccurrence = procedureOccurrence;
	}
	public Collection<Cohort> getCohort() {
		return cohort;
	}
	public void setCohort(Collection<Cohort> cohort) {
		this.cohort = cohort;
	}
	public Collection<Death> getDeath() {
		return death;
	}
	public void setDeath(Collection<Death> death) {
		this.death = death;
	}
	public Collection<PayerPlanPeriod> getPayerPlanPeriod() {
		return payerPlanPeriod;
	}
	public void setPayerPlanPeriod(Collection<PayerPlanPeriod> payerPlanPeriod) {
		this.payerPlanPeriod = payerPlanPeriod;
	}
	
}

