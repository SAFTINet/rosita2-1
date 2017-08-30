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

public class OmopCareSite {

	Long id;
	Long locationId;
	Long organizationId;
	Long placeOfServiceConceptId;
	String careSiteSourceValue;
	String placeOfServiceSourceValue;
	String xDataSourceType;
	String xCareSiteName;
	Long xGridNodeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getPlaceOfServiceConceptId() {
		return placeOfServiceConceptId;
	}
	public void setPlaceOfServiceConceptId(Long placeOfServiceConceptId) {
		this.placeOfServiceConceptId = placeOfServiceConceptId;
	}
	public String getCareSiteSourceValue() {
		return careSiteSourceValue;
	}
	public void setCareSiteSourceValue(String careSiteSourceValue) {
		this.careSiteSourceValue = careSiteSourceValue;
	}
	public String getPlaceOfServiceSourceValue() {
		return placeOfServiceSourceValue;
	}
	public void setPlaceOfServiceSourceValue(String placeOfServiceSourceValue) {
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getxCareSiteName() {
		return xCareSiteName;
	}
	public void setxCareSiteName(String xCareSiteName) {
		this.xCareSiteName = xCareSiteName;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}

}
