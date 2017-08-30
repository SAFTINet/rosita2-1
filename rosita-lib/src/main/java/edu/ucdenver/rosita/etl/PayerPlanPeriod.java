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
@XmlRootElement(name="payer_plan_period")
public class PayerPlanPeriod {

	String id;
	String demographicId;
	@XmlElement (name="payer_plan_period_source_identifier")
	String payerPlanPeriodSourceIdentifier;
	@XmlElement (name="person_source_value")
	String personSourceValue;
	@XmlElement (name="payer_plan_period_start_date")
	String payerPlanPeriodStartDate;
	@XmlElement (name="payer_plan_period_end_date")
	String payerPlanPeriodEndDate;
	@XmlElement (name="payer_source_value")
	String payerSourceValue;
	@XmlElement (name="plan_source_value")
	String planSourceValue;
	@XmlElement (name="family_source_value")
	String familySourceValue;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.payerPlanPeriodSourceIdentifier+"|")
                .append(this.personSourceValue+"|")
                .append(this.payerPlanPeriodStartDate+"|")
                .append(this.payerPlanPeriodEndDate+"|")
                .append(this.payerSourceValue+"|")
                .append(this.planSourceValue+"|")
                .append(this.familySourceValue+"\n");

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
	public String getPayerPlanPeriodSourceIdentifier() {
		return payerPlanPeriodSourceIdentifier;
	}
	public void setPayerPlanPeriodSourceIdentifier(
			String payerPlanPeriodSourceIdentifier) {
		this.payerPlanPeriodSourceIdentifier = payerPlanPeriodSourceIdentifier;
	}
	public String getPersonSourceValue() {
		return personSourceValue;
	}
	public void setPersonSourceValue(String personSourceValue) {
		this.personSourceValue = personSourceValue;
	}
	public String getPayerPlanPeriodStartDate() {
		return payerPlanPeriodStartDate;
	}
	public void setPayerPlanPeriodStartDate(String payerPlanPeriodStartDate) {
		this.payerPlanPeriodStartDate = payerPlanPeriodStartDate;
	}
	public String getPayerPlanPeriodEndDate() {
		return payerPlanPeriodEndDate;
	}
	public void setPayerPlanPeriodEndDate(String payerPlanPeriodEndDate) {
		this.payerPlanPeriodEndDate = payerPlanPeriodEndDate;
	}
	public String getPayerSourceValue() {
		return payerSourceValue;
	}
	public void setPayerSourceValue(String payerSourceValue) {
		this.payerSourceValue = payerSourceValue;
	}
	public String getPlanSourceValue() {
		return planSourceValue;
	}
	public void setPlanSourceValue(String planSourceValue) {
		this.planSourceValue = planSourceValue;
	}
	public String getFamilySourceValue() {
		return familySourceValue;
	}
	public void setFamilySourceValue(String familySourceValue) {
		this.familySourceValue = familySourceValue;
	}
	
}
