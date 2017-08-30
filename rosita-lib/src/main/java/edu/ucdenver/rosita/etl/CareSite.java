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
@XmlRootElement(name="care_site")
public class CareSite {

	String id;
	String organizationId;
	@XmlElement(name="care_site_source_value")
	String careSiteSourceValue;
    @XmlElement(name="x_data_source_type")
	String xDataSourceType;
    @XmlElement(name="organization_source_value")
	String organizationSourceValue;
    @XmlElement(name="place_of_service_source_value")
	String placeOfServiceSourceValue;
    @XmlElement(name="x_care_site_name")
	String xCareSiteName;
    @XmlElement(name="care_site_address_1")
	String careSiteAddress1;
    @XmlElement(name="care_site_address_2")
	String careSiteAddress2;
    @XmlElement(name="care_site_city")
	String careSiteCity;
    @XmlElement(name="care_site_state")
	String careSiteState;
    @XmlElement(name="care_site_zip")
	String careSiteZip;
    @XmlElement(name="care_site_county")
	String careSiteCounty;

	@XmlElement(name="provider")
	Collection<Provider> provider = new ArrayList<Provider>();

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.careSiteSourceValue+"|")
                .append(this.xDataSourceType+"|")
                .append(this.organizationSourceValue+"|")
                .append(this.placeOfServiceSourceValue+"|")
                .append(this.xCareSiteName+"|")
                .append(this.careSiteAddress1+"|")
                .append(this.careSiteAddress2+"|")
                .append(this.careSiteCity+"|")
                .append(this.careSiteState+"|")
                .append(this.careSiteZip+"|")
                .append(this.careSiteCounty+"|\n");

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
	public String getCareSiteSourceValue() {
		return careSiteSourceValue;
	}
	public void setCareSiteSourceValue(String careSiteSourceValue) {
		this.careSiteSourceValue = careSiteSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
	}
	public String getOrganizationSourceValue() {
		return organizationSourceValue;
	}
	public void setOrganizationSourceValue(String organizationSourceValue) {
		this.organizationSourceValue = organizationSourceValue;
	}
	public String getPlaceOfServiceSourceValue() {
		return placeOfServiceSourceValue;
	}
	public void setPlaceOfServiceSourceValue(String placeOfServiceSourceValue) {
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	public String getxCareSiteName() {
		return xCareSiteName;
	}
	public void setxCareSiteName(String xCareSiteName) {
		this.xCareSiteName = xCareSiteName;
	}
	public String getCareSiteAddress1() {
		return careSiteAddress1;
	}
	public void setCareSiteAddress1(String careSiteAddress1) {
		this.careSiteAddress1 = careSiteAddress1;
	}
	public String getCareSiteAddress2() {
		return careSiteAddress2;
	}
	public void setCareSiteAddress2(String careSiteAddress2) {
		this.careSiteAddress2 = careSiteAddress2;
	}
	public String getCareSiteCity() {
		return careSiteCity;
	}
	public void setCareSiteCity(String careSiteCity) {
		this.careSiteCity = careSiteCity;
	}
	public String getCareSiteState() {
		return careSiteState;
	}
	public void setCareSiteState(String careSiteState) {
		this.careSiteState = careSiteState;
	}
	public String getCareSiteZip() {
		return careSiteZip;
	}
	public void setCareSiteZip(String careSiteZip) {
		this.careSiteZip = careSiteZip;
	}
	public String getCareSiteCounty() {
		return careSiteCounty;
	}
	public void setCareSiteCounty(String careSiteCounty) {
		this.careSiteCounty = careSiteCounty;
	}
	public Collection<Provider> getProvider() {
		return provider;
	}
	public void setProvider(Collection<Provider> provider) {
		this.provider = provider;
	}

	
}

