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

public class OmopDrugCost {

	Long id;
	Long drugExposureId;
	Double paidCopay;
	Double paidCoinsurance;
	Double paidTowardDeductible;
	Double paidByPayer;
	Double paidByCoordinationBenefits;
	Double totalOutOfPocket;
	Double totalPaid;
	Double ingredientCost;
	Double dispensingFee;
	Double averageWholesalePrice;
	Long payerPlanPeriodId;
	Long xGridNodeId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDrugExposureId() {
		return drugExposureId;
	}
	public void setDrugExposureId(Long drugExposureId) {
		this.drugExposureId = drugExposureId;
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
	public Double getIngredientCost() {
		return ingredientCost;
	}
	public void setIngredientCost(Double ingredientCost) {
		this.ingredientCost = ingredientCost;
	}
	public Double getDispensingFee() {
		return dispensingFee;
	}
	public void setDispensingFee(Double dispensingFee) {
		this.dispensingFee = dispensingFee;
	}
	public Double getAverageWholesalePrice() {
		return averageWholesalePrice;
	}
	public void setAverageWholesalePrice(Double averageWholesalePrice) {
		this.averageWholesalePrice = averageWholesalePrice;
	}
	public Long getPayerPlanPeriodId() {
		return payerPlanPeriodId;
	}
	public void setPayerPlanPeriodId(Long payerPlanPeriodId) {
		this.payerPlanPeriodId = payerPlanPeriodId;
	}
	public Long getxGridNodeId() {
		return xGridNodeId;
	}
	public void setxGridNodeId(Long xGridNodeId) {
		this.xGridNodeId = xGridNodeId;
	}
	
}
