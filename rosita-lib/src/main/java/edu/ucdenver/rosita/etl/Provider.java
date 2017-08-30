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
@XmlRootElement(name="provider")
public class Provider {

	String id;
	String careSiteId;
    @XmlElement(name="provider_source_value")
	String providerSourceValue;
    @XmlElement(name="x_data_source_type")
	String xDataSourceType;
    @XmlElement(name="npi")
	String npi;
    @XmlElement(name="dea")
	String dea;
    @XmlElement(name="specialty_source_value")
	String specialtySourceValue;
    @XmlElement(name="x_provider_first")
	String xProviderFirst;
    @XmlElement(name="x_provider_middle")
	String xProviderMiddle;
    @XmlElement(name="x_provider_last")
	String xProviderLast;
    @XmlElement(name="care_site_source_value")
	String careSiteSourceValue;
    @XmlElement(name="x_organization_source_value")
	String xOrganizationSourceValue;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.providerSourceValue+"|")
                .append(this.xDataSourceType+"|")
                .append(this.npi+"|")
                .append(this.dea+"|")
                .append(this.specialtySourceValue+"|")
                .append(this.xProviderFirst+"|")
                .append(this.xProviderMiddle+"|")
                .append(this.xProviderLast+"|")
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
	public String getCareSiteId() {
		return careSiteId;
	}
	public void setCareSiteId(String careSiteId) {
		this.careSiteId = careSiteId;
	}
	public String getProviderSourceValue() {
		return providerSourceValue;
	}
	public void setProviderSourceValue(String providerSourceValue) {
		this.providerSourceValue = providerSourceValue;
	}
	public String getxDataSourceType() {
		return xDataSourceType;
	}
	public void setxDataSourceType(String xDataSourceType) {
		this.xDataSourceType = xDataSourceType;
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
	public String getSpecialtySourceValue() {
		return specialtySourceValue;
	}
	public void setSpecialtySourceValue(String specialtySourceValue) {
		this.specialtySourceValue = specialtySourceValue;
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

}
