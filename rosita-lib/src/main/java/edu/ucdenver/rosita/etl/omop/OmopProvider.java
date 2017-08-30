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

public class OmopProvider {

	Long id;
	String npi;
	String dea;
	Long specialtyConceptId;
	Long careSiteId;
	String providerSourceValue;
	String specialtySourceValue;
	String xDataSourceType;
	String xProviderFirst;
	String xProviderMiddle;
	String xProviderLast;
	Long xOrganizationId;
	Long xGridNodeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNpi() {
		return npi;
	}
	public void setNpi(String npi) {
		this.npi = npi;
	}
	public String getDea() {
		return dea;
	}
	public void setDea(String dea) {
		this.dea = dea;
	}
	public Long getSpecialtyConceptId() {
		return specialtyConceptId;
	}
	public void setSpecialtyConceptId(Long specialtyConceptId) {
		this.specialtyConceptId = specialtyConceptId;
	}
	public Long getCareSiteId() {
		return careSiteId;
	}
	public void setCareSiteId(Long careSiteId) {
		this.careSiteId = careSiteId;
	}
	public String getProviderSourceValue() {
		return providerSourceValue;
	}
	public void setProviderSourceValue(String providerSourceValue) {
		this.providerSourceValue = providerSourceValue;
	}
	public String getSpecialtySourceValue() {
		return specialtySourceValue;
	}
	public void setSpecialtySourceValue(String specialtySourceValue) {
		this.specialtySourceValue = specialtySourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getxProviderFirst() {
		return xProviderFirst;
	}
	public void setxProviderFirst(String xProviderFirst) {
		this.xProviderFirst = xProviderFirst;
	}
	public String getxProviderMiddle() {
		return xProviderMiddle;
	}
	public void setxProviderMiddle(String xProviderMiddle) {
		this.xProviderMiddle = xProviderMiddle;
	}
	public String getxProviderLast() {
		return xProviderLast;
	}
	public void setxProviderLast(String xProviderLast) {
		this.xProviderLast = xProviderLast;
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
