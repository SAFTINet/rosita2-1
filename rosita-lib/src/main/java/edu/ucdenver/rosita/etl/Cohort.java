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
@XmlRootElement(name="cohort")
public class Cohort {

	String id;
	String demographicId;
	@XmlElement (name="cohort_source_identifier")
	String cohortSourceIdentifier;
	@XmlElement (name="cohort_source_value")
	String cohortSourceValue;
	@XmlElement (name="cohort_start_date")
	String cohortStartDate;
	@XmlElement (name="cohort_end_date")
	String cohortEndDate;
	@XmlElement (name="subject_source_identifier")
	String subjectSourceIdentifier;
	@XmlElement (name="stop_reason")
	String stopReason;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.cohortSourceIdentifier+"|")
                .append(this.cohortSourceValue+"|")
                .append(this.cohortStartDate+"|")
                .append(this.cohortEndDate+"|")
                .append(this.subjectSourceIdentifier+"|")
                .append(this.stopReason+"|\n");

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
	public String getCohortSourceIdentifier() {
		return cohortSourceIdentifier;
	}
	public void setCohortSourceIdentifier(String cohortSourceIdentifier) {
		this.cohortSourceIdentifier = cohortSourceIdentifier;
	}
	public String getCohortSourceValue() {
		return cohortSourceValue;
	}
	public void setCohortSourceValue(String cohortSourceValue) {
		this.cohortSourceValue = cohortSourceValue;
	}
	public String getCohortStartDate() {
		return cohortStartDate;
	}
	public void setCohortStartDate(String cohortStartDate) {
		this.cohortStartDate = cohortStartDate;
	}
	public String getCohortEndDate() {
		return cohortEndDate;
	}
	public void setCohortEndDate(String cohortEndDate) {
		this.cohortEndDate = cohortEndDate;
	}
	public String getSubjectSourceIdentifier() {
		return subjectSourceIdentifier;
	}
	public void setSubjectSourceIdentifier(String subjectSourceIdentifier) {
		this.subjectSourceIdentifier = subjectSourceIdentifier;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

}
