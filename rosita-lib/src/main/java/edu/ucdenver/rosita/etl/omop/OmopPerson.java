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


public class OmopPerson {

	Long id;
	Long genderConceptId;
	Integer yearOfBirth;
	Integer monthOfBirth;
	Integer dayOfBirth;
	Long raceConceptId;
	Long ethnicityConceptId;
	Long locationId;
	Long providerId;
	Long careSiteId;
	String personSourceValue;
	String genderSourceValue;
	String raceSourceValue;
	String ethnicitySourceValue;
	Long xOrganizationId;
	Long xGridNodeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGenderConceptId() {
		return genderConceptId;
	}
	public void setGenderConceptId(Long genderConceptId) {
		this.genderConceptId = genderConceptId;
	}
	public Integer getYearOfBirth() {
		return yearOfBirth;
	}
	public void setYearOfBirth(Integer yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}
	public Integer getMonthOfBirth() {
		return monthOfBirth;
	}
	public void setMonthOfBirth(Integer monthOfBirth) {
		this.monthOfBirth = monthOfBirth;
	}
	public Integer getDayOfBirth() {
		return dayOfBirth;
	}
	public void setDayOfBirth(Integer dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}
	public Long getRaceConceptId() {
		return raceConceptId;
	}
	public void setRaceConceptId(Long raceConceptId) {
		this.raceConceptId = raceConceptId;
	}
	public Long getEthnicityConceptId() {
		return ethnicityConceptId;
	}
	public void setEthnicityConceptId(Long ethnicityConceptId) {
		this.ethnicityConceptId = ethnicityConceptId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public Long getProviderId() {
		return providerId;
	}
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	public Long getCareSiteId() {
		return careSiteId;
	}
	public void setCareSiteId(Long careSiteId) {
		this.careSiteId = careSiteId;
	}
	public String getPersonSourceValue() {
		return "0"; //personSourceValue; REMOVING PHI!!!
	}
	public void setPersonSourceValue(String personSourceValue) {
		this.personSourceValue = personSourceValue;
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
	public Long getxOrganizationId() {
		return xOrganizationId;
	}
	public void setxOrganizationId(Long xOrganizationId) {
		this.xOrganizationId = xOrganizationId;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}

	
}
