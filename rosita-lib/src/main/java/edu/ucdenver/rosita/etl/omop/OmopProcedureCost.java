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

public class OmopProcedureCost {

	Long id;
	Long procedureOccurrenceId;
	Double paidCopay;
	Double paidCoinsurance;
	Double paidTowardDeductible;
	Double paidByPayer;
	Double paidByCoordinationBenefits;
	Double totalOutOfPocket;
	Double totalPaid;
	Long diseaseClassConceptId;
	Long revenueCodeConceptId;
	Long payerPlanPeriodId;
	String diseaseClassSourceValue;
	String revenueCodeSourceValue;
	Long xGridNodeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProcedureOccurrenceId() {
		return procedureOccurrenceId;
	}
	public void setProcedureOccurrenceId(Long procedureOccurrenceId) {
		this.procedureOccurrenceId = procedureOccurrenceId;
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
	public Long getDiseaseClassConceptId() {
		return diseaseClassConceptId;
	}
	public void setDiseaseClassConceptId(Long diseaseClassConceptId) {
		this.diseaseClassConceptId = diseaseClassConceptId;
	}
	public Long getRevenueCodeConceptId() {
		return revenueCodeConceptId;
	}
	public void setRevenueCodeConceptId(Long revenueCodeConceptId) {
		this.revenueCodeConceptId = revenueCodeConceptId;
	}
	public Long getPayerPlanPeriodId() {
		return payerPlanPeriodId;
	}
	public void setPayerPlanPeriodId(Long payerPlanPeriodId) {
		this.payerPlanPeriodId = payerPlanPeriodId;
	}
	public String getDiseaseClassSourceValue() {
		return diseaseClassSourceValue;
	}
	public void setDiseaseClassSourceValue(String diseaseClassSourceValue) {
		this.diseaseClassSourceValue = diseaseClassSourceValue;
	}
	public String getRevenueCodeSourceValue() {
		return revenueCodeSourceValue;
	}
	public void setRevenueCodeSourceValue(String revenueCodeSourceValue) {
		this.revenueCodeSourceValue = revenueCodeSourceValue;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	
}
