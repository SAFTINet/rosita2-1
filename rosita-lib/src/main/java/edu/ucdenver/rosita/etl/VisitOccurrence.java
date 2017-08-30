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
@XmlRootElement(name="visit_occurrence")
public class VisitOccurrence {
	
	String id;
	String demographicId;
	@XmlElement (name="x_visit_occurrence_source_identifier")
	String xVisitOccurrenceSourceIdentifier;
	@XmlElement (name="x_data_source_type")
	String xDataSourceType;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="visit_start_date")
	String visitStartDate;
	@XmlElement (name="visit_end_date")
	String visitEndDate;
	@XmlElement (name="place_of_service_source_value")
	String placeOfServiceSourceValue;
	@XmlElement (name="x_provider_source_value")
	String xProviderSourceValue;
	@XmlElement (name="care_site_source_value")
	String careSiteSourceValue;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.xVisitOccurrenceSourceIdentifier+"|")
                .append(this.xDataSourceType+"|")
                .append(this.personSourceValue+"|")
                .append(this.visitStartDate+"|")
                .append(this.visitEndDate+"|")
                .append(this.placeOfServiceSourceValue+"|")
                .append(this.xProviderSourceValue+"|")
                .append(this.careSiteSourceValue+"|\n");

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
	public String getxVisitOccurrenceSourceIdentifier() {
		return xVisitOccurrenceSourceIdentifier;
	}
	public void setxVisitOccurrenceSourceIdentifier(
			String xVisitOccurrenceSourceIdentifier) {
		this.xVisitOccurrenceSourceIdentifier = xVisitOccurrenceSourceIdentifier;
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
	public String getVisitStartDate() {
		return visitStartDate;
	}
	public void setVisitStartDate(String visitStartDate) {
		this.visitStartDate = visitStartDate;
	}
	public String getVisitEndDate() {
		return visitEndDate;
	}
	public void setVisitEndDate(String visitEndDate) {
		this.visitEndDate = visitEndDate;
	}
	public String getPlaceOfServiceSourceValue() {
		return placeOfServiceSourceValue;
	}
	public void setPlaceOfServiceSourceValue(String placeOfServiceSourceValue) {
		this.placeOfServiceSourceValue = placeOfServiceSourceValue;
	}
	public String getxProviderSourceValue() {
		return xProviderSourceValue;
	}
	public void setxProviderSourceValue(String xProviderSourceValue) {
		this.xProviderSourceValue = xProviderSourceValue;
	}
	public String getCareSiteSourceValue() {
		return careSiteSourceValue;
	}
	public void setCareSiteSourceValue(String careSiteSourceValue) {
		this.careSiteSourceValue = careSiteSourceValue;
	}

}
