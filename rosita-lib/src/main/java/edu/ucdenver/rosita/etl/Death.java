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
@XmlRootElement(name="death")
public class Death {
	
	String id;
	String demographicId;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="death_date")
	String deathDate;
	@XmlElement (name="death_type_concept_id")
	String deathTypeConceptId;
	@XmlElement (name="death_type_source_value")
	String deathTypeSourceValue;
	@XmlElement (name="cause_of_death_source_value")
	String causeOfDeathSourceValue;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.personSourceValue+"|")
                .append(this.deathDate+"|")
                .append(this.deathTypeConceptId+"|")
                .append(this.deathTypeSourceValue+"|")
                .append(this.causeOfDeathSourceValue+"|\n");

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
	public String getPersonSourceValue() {
		return personSourceValue;
	}
	public void setPersonSourceValue(String personSourceValue) {
		this.personSourceValue = personSourceValue;
	}
	public String getDeathDate() {
		return deathDate;
	}
	public void setDeathDate(String deathDate) {
		this.deathDate = deathDate;
	}
	public String getDeathTypeConceptId() {
		return deathTypeConceptId;
	}
	public void setDeathTypeConceptId(String deathTypeConceptId) {
		this.deathTypeConceptId = deathTypeConceptId;
	}
	public String getDeathTypeSourceValue() {
		return deathTypeSourceValue;
	}
	public void setDeathTypeSourceValue(String deathTypeSourceValue) {
		this.deathTypeSourceValue = deathTypeSourceValue;
	}
	public String getCauseOfDeathSourceValue() {
		return causeOfDeathSourceValue;
	}
	public void setCauseOfDeathSourceValue(String causeOfDeathSourceValue) {
		this.causeOfDeathSourceValue = causeOfDeathSourceValue;
	}
	
}
