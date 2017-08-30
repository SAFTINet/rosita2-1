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

public class OmopCohort {
	Long id;
	String xDataSource;
	Long cohortConceptId;
	Date cohortStartDate;
	Date cohortEndDate;
	Long personId;
	String stopReason;
	Long xCohortMetadataId;
	Long xGridNodeId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getxDataSource() {
		return xDataSource;
	}
	public void setxDataSource(String xDataSource) {
		this.xDataSource = xDataSource;
	}
	public Long getCohortConceptId() {
		return cohortConceptId;
	}
	public void setCohortConceptId(Long cohortConceptId) {
		this.cohortConceptId = cohortConceptId;
	}
	public Date getCohortStartDate() {
		return cohortStartDate;
	}
	public void setCohortStartDate(Date cohortStartDate) {
		this.cohortStartDate = cohortStartDate;
	}
	public Date getCohortEndDate() {
		return cohortEndDate;
	}
	public void setCohortEndDate(Date cohortEndDate) {
		this.cohortEndDate = cohortEndDate;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Long getxCohortMetadataId() {
		return xCohortMetadataId;
	}
	public void setxCohortMetadataId(Long xCohortMetadataId) {
		this.xCohortMetadataId = xCohortMetadataId;
	}
	 
}
