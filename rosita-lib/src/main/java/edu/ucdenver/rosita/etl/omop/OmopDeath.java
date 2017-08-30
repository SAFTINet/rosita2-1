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

public class OmopDeath {

	Long personId;
	Date deathDate;
	Long deathTypeConceptId;
	Long causeOfDeathConceptId;
	String causeOfDeathSourceValue;
	Long xGridNodeId;
	
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Date getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}
	public Long getDeathTypeConceptId() {
		return deathTypeConceptId;
	}
	public void setDeathTypeConceptId(Long deathTypeConceptId) {
		this.deathTypeConceptId = deathTypeConceptId;
	}
	public Long getCauseOfDeathConceptId() {
		return causeOfDeathConceptId;
	}
	public void setCauseOfDeathConceptId(Long causeOfDeathConceptId) {
		this.causeOfDeathConceptId = causeOfDeathConceptId;
	}
	public String getCauseOfDeathSourceValue() {
		return causeOfDeathSourceValue;
	}
	public void setCauseOfDeathSourceValue(String causeOfDeathSourceValue) {
		this.causeOfDeathSourceValue = causeOfDeathSourceValue;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	
}
