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
@XmlRootElement(name="procedure_cost")
public class ProcedureCost {
	
	String id;
	String procedureOccurrenceId;
	@XmlElement (name="procedure_cost_source_identifier")
	String procedureCostSourceIdentifier;
	@XmlElement (name="procedure_occurrence_source_identifier")
	String procedureOccurrenceSourceIdentifier;
	@XmlElement (name="paid_copay")
	Double paidCopay;
	@XmlElement (name="paid_coinsurance")
	Double paidCoinsurance;
	@XmlElement (name="paid_toward_deductible")
	Double paidTowardDeductible;
	@XmlElement (name="paid_by_payer")
	Double paidByPayer;
	@XmlElement (name="paid_by_coordination_benefits")
	Double paidByCoordinationBenefits;
	@XmlElement (name="total_out_of_pocket")
	Double totalOutOfPocket;
	@XmlElement (name="total_paid")
	Double totalPaid;
	@XmlElement (name="disease_class_concept_id")
	String diseaseClassConceptId;
	@XmlElement (name="disease_class_source_value")
	String diseaseClassSourceValue;
	@XmlElement (name="revenue_code_concept_id")
	String revenueCodeConceptId;
	@XmlElement (name="revenue_code_source_value")
	String revenueCodeSourceValue;
	@XmlElement (name="payer_plan_period_source_identifier")
	String payerPlanPeriodSourceIdentifier;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.procedureCostSourceIdentifier+"|")
                .append(this.procedureOccurrenceSourceIdentifier+"|")
                .append(this.paidCopay+"|")
                .append(this.paidCoinsurance+"|")
                .append(this.paidTowardDeductible+"|")
                .append(this.paidByPayer+"|")
                .append(this.paidByCoordinationBenefits+"|")
                .append(this.totalOutOfPocket+"|")
                .append(this.totalPaid+"|")
                .append(this.diseaseClassConceptId+"|")
                .append(this.diseaseClassSourceValue+"|")
                .append(this.revenueCodeConceptId+"|")
                .append(this.revenueCodeSourceValue+"|")
                .append(this.payerPlanPeriodSourceIdentifier+"|\n");

        return strbuf.toString();
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProcedureOccurrenceId() {
		return procedureOccurrenceId;
	}
	public void setProcedureOccurrenceId(String procedureOccurrenceId) {
		this.procedureOccurrenceId = procedureOccurrenceId;
	}
	public String getProcedureCostSourceIdentifier() {
		return procedureCostSourceIdentifier;
	}
	public void setProcedureCostSourceIdentifier(
			String procedureCostSourceIdentifier) {
		this.procedureCostSourceIdentifier = procedureCostSourceIdentifier;
	}
	public String getProcedureOccurrenceSourceIdentifier() {
		return procedureOccurrenceSourceIdentifier;
	}
	public void setProcedureOccurrenceSourceIdentifier(
			String procedureOccurrenceSourceIdentifier) {
		this.procedureOccurrenceSourceIdentifier = procedureOccurrenceSourceIdentifier;
	}
	public Double getPaidCopay() {
		return paidCopay;
	}
	public void setPaidCopay(Double paidCopay) {
		this.paidCopay = paidCopay;
	}
	public Double getPaidCoinsurance() {
		return paidCoinsurance;
	}
	public void setPaidCoinsurance(Double paidCoinsurance) {
		this.paidCoinsurance = paidCoinsurance;
	}
	public Double getPaidTowardDeductible() {
		return paidTowardDeductible;
	}
	public void setPaidTowardDeductible(Double paidTowardDeductible) {
		this.paidTowardDeductible = paidTowardDeductible;
	}
	public Double getPaidByPayer() {
		return paidByPayer;
	}
	public void setPaidByPayer(Double paidByPayer) {
		this.paidByPayer = paidByPayer;
	}
	public Double getPaidByCoordinationBenefits() {
		return paidByCoordinationBenefits;
	}
	public void setPaidByCoordinationBenefits(Double paidByCoordinationBenefits) {
		this.paidByCoordinationBenefits = paidByCoordinationBenefits;
	}
	public Double getTotalOutOfPocket() {
		return totalOutOfPocket;
	}
	public void setTotalOutOfPocket(Double totalOutOfPocket) {
		this.totalOutOfPocket = totalOutOfPocket;
	}
	public Double getTotalPaid() {
		return totalPaid;
	}
	public void setTotalPaid(Double totalPaid) {
		this.totalPaid = totalPaid;
	}
	public String getDiseaseClassConceptId() {
		return diseaseClassConceptId;
	}
	public void setDiseaseClassConceptId(String diseaseClassConceptId) {
		this.diseaseClassConceptId = diseaseClassConceptId;
	}
	public String getDiseaseClassSourceValue() {
		return diseaseClassSourceValue;
	}
	public void setDiseaseClassSourceValue(String diseaseClassSourceValue) {
		this.diseaseClassSourceValue = diseaseClassSourceValue;
	}
	public String getRevenueCodeConceptId() {
		return revenueCodeConceptId;
	}
	public void setRevenueCodeConceptId(String revenueCodeConceptId) {
		this.revenueCodeConceptId = revenueCodeConceptId;
	}
	public String getRevenueCodeSourceValue() {
		return revenueCodeSourceValue;
	}
	public void setRevenueCodeSourceValue(String revenueCodeSourceValue) {
		this.revenueCodeSourceValue = revenueCodeSourceValue;
	}
	public String getPayerPlanPeriodSourceIdentifier() {
		return payerPlanPeriodSourceIdentifier;
	}
	public void setPayerPlanPeriodSourceIdentifier(
			String payerPlanPeriodSourceIdentifier) {
		this.payerPlanPeriodSourceIdentifier = payerPlanPeriodSourceIdentifier;
	}

}
