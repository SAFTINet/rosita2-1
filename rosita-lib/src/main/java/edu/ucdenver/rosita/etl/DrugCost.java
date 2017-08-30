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
@XmlRootElement(name="drug_cost")
public class DrugCost {

	String id;
	String drugExposureId;
	@XmlElement (name="drug_cost_source_identifier")
	String drugCostSourceIdentifier;
	@XmlElement (name="drug_exposure_source_identifier")
	String drugExposureSourceIdentifier;
	@XmlElement (name="paid_copay")
	String paidCopay;
	@XmlElement (name="paid_coinsurance")
	String paidCoinsurance;
	@XmlElement (name="paid_toward_deductible")
	String paidTowardDeductible;
	@XmlElement (name="paid_by_payer")
	String paidByPayer;
	@XmlElement (name="paid_by_coordination_benefits")
	String paidByCoordinationBenefits;
	@XmlElement (name="total_out_of_pocket")
	String totalOutOfPocket;
	@XmlElement (name="total_paid")
	String totalPaid;
	@XmlElement (name="ingredient_cost")
	String ingredientCost;
	@XmlElement (name="dispensing_fee")
	String dispensingFee;
	@XmlElement (name="average_wholesale_price")
	String averageWholesalePrice;
	@XmlElement (name="payer_plan_period_source_identifier")
	String payerPlanPeriodSourceIdentifier;

    public String dataToCsv() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append(this.drugCostSourceIdentifier+"|")
                .append(this.drugExposureSourceIdentifier+"|")
                .append(this.paidCopay+"|")
                .append(this.paidCoinsurance+"|")
                .append(this.paidTowardDeductible+"|")
                .append(this.paidByPayer+"|")
                .append(this.paidByCoordinationBenefits+"|")
                .append(this.totalOutOfPocket+"|")
                .append(this.totalPaid+"|")
                .append(this.ingredientCost+"|")
                .append(this.dispensingFee+"|")
                .append(this.averageWholesalePrice+"|")
                .append(this.payerPlanPeriodSourceIdentifier+"|\n");

        return strbuf.toString();
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDrugExposureId() {
		return drugExposureId;
	}
	public void setDrugExposureId(String drugExposureId) {
		this.drugExposureId = drugExposureId;
	}
	public String getDrugCostSourceIdentifier() {
		return drugCostSourceIdentifier;
	}
	public void setDrugCostSourceIdentifier(String drugCostSourceIdentifier) {
		this.drugCostSourceIdentifier = drugCostSourceIdentifier;
	}
	public String getDrugExposureSourceIdentifier() {
		return drugExposureSourceIdentifier;
	}
	public void setDrugExposureSourceIdentifier(String drugExposureSourceIdentifier) {
		this.drugExposureSourceIdentifier = drugExposureSourceIdentifier;
	}
	public String getPaidCopay() {
		return paidCopay;
	}
	public void setPaidCopay(String paidCopay) {
		this.paidCopay = paidCopay;
	}
	public String getPaidCoinsurance() {
		return paidCoinsurance;
	}
	public void setPaidCoinsurance(String paidCoinsurance) {
		this.paidCoinsurance = paidCoinsurance;
	}
	public String getPaidTowardDeductible() {
		return paidTowardDeductible;
	}
	public void setPaidTowardDeductible(String paidTowardDeductible) {
		this.paidTowardDeductible = paidTowardDeductible;
	}
	public String getPaidByPayer() {
		return paidByPayer;
	}
	public void setPaidByPayer(String paidByPayer) {
		this.paidByPayer = paidByPayer;
	}
	public String getPaidByCoordinationBenefits() {
		return paidByCoordinationBenefits;
	}
	public void setPaidByCoordinationBenefits(String paidByCoordinationBenefits) {
		this.paidByCoordinationBenefits = paidByCoordinationBenefits;
	}
	public String getTotalOutOfPocket() {
		return totalOutOfPocket;
	}
	public void setTotalOutOfPocket(String totalOutOfPocket) {
		this.totalOutOfPocket = totalOutOfPocket;
	}
	public String getTotalPaid() {
		return totalPaid;
	}
	public void setTotalPaid(String totalPaid) {
		this.totalPaid = totalPaid;
	}
	public String getIngredientCost() {
		return ingredientCost;
	}
	public void setIngredientCost(String ingredientCost) {
		this.ingredientCost = ingredientCost;
	}
	public String getDispensingFee() {
		return dispensingFee;
	}
	public void setDispensingFee(String dispensingFee) {
		this.dispensingFee = dispensingFee;
	}
	public String getAverageWholesalePrice() {
		return averageWholesalePrice;
	}
	public void setAverageWholesalePrice(String averageWholesalePrice) {
		this.averageWholesalePrice = averageWholesalePrice;
	}
	public String getPayerPlanPeriodSourceIdentifier() {
		return payerPlanPeriodSourceIdentifier;
	}
	public void setPayerPlanPeriodSourceIdentifier(
			String payerPlanPeriodSourceIdentifier) {
		this.payerPlanPeriodSourceIdentifier = payerPlanPeriodSourceIdentifier;
	}

}
