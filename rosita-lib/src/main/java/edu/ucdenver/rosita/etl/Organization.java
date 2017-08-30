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
@XmlRootElement
public class Organization {

	String id;
    @XmlElement(name="organization_source_value")
	String organizationSourceValue;
    @XmlElement(name="x_data_source_type")
	String xDataSourceType;
    @XmlElement(name="place_of_service_source_value")
	String placeOfServiceSourceValue;
    @XmlElement(name="organization_address_1")
	String organizationAddress1;
    @XmlElement(name="organization_address_2")
	String organizationAddress2;
    @XmlElement(name="organization_city")
	String organizationCity;
    @XmlElement(name="organization_state")
	String organizationState;
    @XmlElement(name="organization_zip")
	String organizationZip;
    @XmlElement(name="organization_county")
	String organizationCounty;
	@XmlElement(name="care_site")
	Collection<CareSite> careSite = new ArrayList<CareSite>();
	@XmlElement(name="x_demographic")
	Collection<Demographic> x_demographic = new ArrayList<Demographic>();

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.organizationSourceValue+"|")
                .append(this.xDataSourceType+"|")
                .append(this.placeOfServiceSourceValue+"|")
                .append(this.organizationAddress1+"|")
                .append(this.organizationAddress2+"|")
                .append(this.organizationCity+"|")
                .append(this.organizationState+"|")
                .append(this.organizationZip+"|")
                .append(this.organizationCounty+"|\n");
        return strbuf.toString();
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrganizationSourceValue() {
		return organizationSourceValue;
	}
	public void setOrganizationSourceValue(String organizationSourceValue) {
		this.organizationSourceValue = organizationSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getPlaceOfServiceSourceValue() {
		return placeOfServiceSourceValue;
	}
	public void setPlaceOfServiceSourceValue(String placeOfServiceSourceValue) {
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	public String getOrganizationAddress1() {
		return organizationAddress1;
	}
	public void setOrganizationAddress1(String organizationAddress1) {
		this.organizationAddress1 = organizationAddress1;
	}
	public String getOrganizationAddress2() {
		return organizationAddress2;
	}
	public void setOrganizationAddress2(String organizationAddress2) {
		this.organizationAddress2 = organizationAddress2;
	}
	public String getOrganizationCity() {
		return organizationCity;
	}
	public void setOrganizationCity(String organizationCity) {
		this.organizationCity = organizationCity;
	}
	public String getOrganizationState() {
		return organizationState;
	}
	public void setOrganizationState(String organizationState) {
		this.organizationState = organizationState;
	}
	public String getOrganizationZip() {
		return organizationZip;
	}
	public void setOrganizationZip(String organizationZip) {
		this.organizationZip = organizationZip;
	}
	public String getOrganizationCounty() {
		return organizationCounty;
	}
	public void setOrganizationCounty(String organizationCounty) {
		this.organizationCounty = organizationCounty;
	}
	public Collection<CareSite> getCareSite() {
		return careSite;
	}
	public void setCareSite(Collection<CareSite> careSite) {
		this.careSite = careSite;
	}
	public Collection<Demographic> getDemographic() {
		return x_demographic;
	}
	public void setDemographic(Collection<Demographic> x_demographic) {
		this.x_demographic = x_demographic;
	}

}
