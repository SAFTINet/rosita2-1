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

public class OmopObservationPeriod {

	Long id;
	Long personId;
	Date observationPeriodStartDate;
	Date observationPeriodEndDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Date getObservationPeriodStartDate() {
		return observationPeriodStartDate;
	}
	public void setObservationPeriodStartDate(Date observationPeriodStartDate) {
		this.observationPeriodStartDate = observationPeriodStartDate;
	}
	public Date getObservationPeriodEndDate() {
		return observationPeriodEndDate;
	}
	public void setObservationPeriodEndDate(Date observationPeriodEndDate) {
		this.observationPeriodEndDate = observationPeriodEndDate;
	}
	
}
