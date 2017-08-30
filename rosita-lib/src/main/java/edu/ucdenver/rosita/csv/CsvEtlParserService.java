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

package edu.ucdenver.rosita.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import edu.ucdenver.rosita.etl.CareSite;
import edu.ucdenver.rosita.etl.Cohort;
import edu.ucdenver.rosita.etl.ConditionOccurrence;
import edu.ucdenver.rosita.etl.Death;
import edu.ucdenver.rosita.etl.Demographic;
import edu.ucdenver.rosita.etl.DrugCost;
import edu.ucdenver.rosita.etl.DrugExposure;
import edu.ucdenver.rosita.etl.Observation;
import edu.ucdenver.rosita.etl.Organization;
import edu.ucdenver.rosita.etl.PayerPlanPeriod;
import edu.ucdenver.rosita.etl.ProcedureCost;
import edu.ucdenver.rosita.etl.ProcedureOccurrence;
import edu.ucdenver.rosita.etl.Provider;
import edu.ucdenver.rosita.etl.VisitOccurrence;
import edu.ucdenver.rosita.utils.EtlCache;
import edu.ucdenver.rosita.utils.RositaLogger;

import au.com.bytecode.opencsv.CSVParser;

public class CsvEtlParserService {

	Long id = 0l;
	Long elements;
	EtlCache objectCache;
	Long dataSourceId = 1L;
	Long outputThreshold = 10000L;
	Long totalLines = 0L;
	boolean trackParents = false;
	
	//Keep objects that need to be referred to as parents
	HashMap<String, Long> organizations = null;
	HashMap<String, Long> careSites = null;
	HashMap<String, Long> demographics = null;
	HashMap<String, Long> drugExposures = null;
	HashMap<String, Long> procedureOccurrences = null;
	
	public CsvEtlParserService(DataSource ds, Integer threshold, Long dataSourceId) {
		objectCache = new EtlCache(ds, threshold, dataSourceId);
		this.dataSourceId = dataSourceId;
	}
	
	public void setTrackParents(boolean track) {
		this.trackParents = track;
	}
	
	/**
	 * Parses files in specified directory
	 * @param directoryName
	 * @param suffix
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
   public Long parse(String directoryName, Integer clinic, Integer maxClinics)
            throws FileNotFoundException, IOException, ParserConfigurationException {

    	CsvFileHandler handler = new CsvFileHandler(directoryName, null);
    	
		//Keep objects that need to be referred to as parents
        organizations = new HashMap<String, Long>();
		careSites = new HashMap<String, Long>();
		demographics = new HashMap<String, Long>();
		drugExposures = new HashMap<String, Long>();
		procedureOccurrences = new HashMap<String, Long>();

        for (String table : CsvFileHandler.allTables) {
        	// Find first file that starts with table name
        	File file = handler.findFile(table);
        	if (file == null) {
        		continue;
        	}
        	
    		BufferedReader reader = null;
        	try {
        		RositaLogger.log(false, "Parsing " + file.getName());
           		CSVParser parser = new CSVParser('|', CSVParser.DEFAULT_QUOTE_CHARACTER);           	
 				String line;
 				
        		reader = new BufferedReader(new FileReader(file));
				id = 0l; // reset ID for each file
				while ((line = reader.readLine()) != null) {
					id++;
					totalLines++;
					if (totalLines > outputThreshold) {
						outputThreshold += 10000;
						RositaLogger.log(true, "STATUS|||LOAD|||" + totalLines + "|||" + dataSourceId + "|||" + clinic + "|||" + maxClinics);
					}
					
					String[] columns = parser.parseLine(line);
					if (parser.isPending()) {
						throw new IOException("Line " + id + " of " + file.getName() + " cannot be parsed due to invalid formatting");
					}
                    if("organization".equals(table)) {
                        //System.out.println("Parsing organization");
						Organization organization = parseOrganization(columns);
						objectCache.add(organization);
						if (trackParents) {
							if (!organizations.containsKey(organization.getOrganizationSourceValue())) {
								organizations.put(organization.getOrganizationSourceValue(), new Long(id));
							} else {
								RositaLogger.log(false, "Duplicate organization source value found: " + organization.getOrganizationSourceValue() + " at line " + id);
							}
						}
					}
					else if("care_site".equals(table)){
                        //System.out.println("Parsing care_site");
						CareSite obj = parseCareSite(columns);
						objectCache.add(obj);
						if (trackParents) {
							if (!careSites.containsKey(obj.getCareSiteSourceValue())) {
								careSites.put(obj.getCareSiteSourceValue(), new Long(id));
							} else {
								RositaLogger.log(false, "Duplicate careSite source value found: " + obj.getCareSiteSourceValue() + " at line " + id);
							}
						}
					}
					else if("provider".equals(table)){
                        //System.out.println("Parsing provider");
						Provider obj = parseProvider(columns);
						objectCache.add(obj);
					}
					else if("x_demographic".equals(table)){
                        //System.out.println("Parsing x_demographic");
						Demographic obj = parseDemographic(columns);
						objectCache.add(obj);
						if (trackParents) {
							if (!demographics.containsKey(obj.getPersonSourceValue())) {
								demographics.put(obj.getPersonSourceValue(), new Long(id));
							} else {
								RositaLogger.log(false, "Duplicate demographic source value found: " + obj.getPersonSourceValue() + " at line " + id);
							}
						}
					}
					else if("visit_occurrence".equals(table)){
                        //System.out.println("Parsing visit_occurrence");
						VisitOccurrence obj = parseVisitOccurrence(columns);
						objectCache.add(obj);
					}
					else if("observation".equals(table)){
                        //System.out.println("Parsing observation");
						Observation obj = parseObservation(columns);
						objectCache.add(obj);
					}
					else if("drug_exposure".equals(table)){
                        //System.out.println("Parsing drug_exposure");
						DrugExposure obj = parseDrugExposure(columns);
						objectCache.add(obj);
						if (trackParents) {
							if (!drugExposures.containsKey(obj.getDrugExposureSourceIdentifier())) {
								drugExposures.put(obj.getDrugExposureSourceIdentifier(), new Long(id));
							} else {
								RositaLogger.log(false, "Duplicate drugExposure source value found: " + obj.getDrugExposureSourceIdentifier() + " at line " + id);							
							}
						}
					}
					else if("drug_cost".equals(table)){
                        //System.out.println("Parsing drug_cost");
						DrugCost obj = parseDrugCost(columns);
						objectCache.add(obj);
					}
					else if("condition_occurrence".equals(table)){
                        //System.out.println("Parsing condition_occurrence");
						ConditionOccurrence obj = parseConditionOccurrence(columns);
						objectCache.add(obj);
					}
					else if("procedure_occurrence".equals(table)){
                        //System.out.println("Parsing procedure_occurrence");
						ProcedureOccurrence obj = parseProcedureOccurrence(columns);
						objectCache.add(obj);
						if (trackParents) {
							if (!procedureOccurrences.containsKey(obj.getProcedureOccurrenceSourceIdentifier())) {
								procedureOccurrences.put(obj.getProcedureOccurrenceSourceIdentifier(), new Long(id));
							} else {
								RositaLogger.log(false, "Duplicate procedureOccurrence source value found: " + obj.getProcedureOccurrenceSourceIdentifier() + " at line " + id);
							}
						}
					}
					else if("procedure_cost".equals(table)){
                        //System.out.println("Parsing procedure_cost");
						ProcedureCost obj = parseProcedureCost(columns);
						objectCache.add(obj);
					}
					else if("cohort".equals(table)){
                        //System.out.println("Parsing cohort");
						Cohort obj = parseCohort(columns);
						objectCache.add(obj);
					}
					else if("death".equals(table)){
                        //System.out.println("Parsing death");
						Death obj = parseDeath(columns);
						objectCache.add(obj);
					}
					else if("payer_plan_period".equals(table)){
                        //System.out.println("Parsing payer_plan_period");
						PayerPlanPeriod obj = parsePayerPlanPeriod(columns);
						objectCache.add(obj);
					}				
				}
			}
			finally {
				if (reader != null) {
					reader.close();
				}
			}		
		}
		
		objectCache.saveAll();
		
		//Free up ID maps for garbage collection
		organizations = null;
		careSites = null;
		demographics = null;
		drugExposures = null;
		procedureOccurrences = null;
		
		return totalLines;
    }

	private Organization parseOrganization(String[] row) {
		Organization org = new Organization();
		org.setOrganizationSourceValue(row[0]);
		org.setxDataSourceType(row[1]);
		org.setPlaceOfServiceSourceValue(row[2]);
		org.setOrganizationAddress1(row[3]);
		org.setOrganizationAddress2(row[4]);
		org.setOrganizationCity(row[5]);
		org.setOrganizationState(row[6]);
		org.setOrganizationZip(row[7]);
		org.setOrganizationCounty(row[8]);
		org.setId(String.valueOf(id));
		return org;
	}
	
	private CareSite parseCareSite(String[] row) {
		CareSite cs = new CareSite();
		cs.setCareSiteSourceValue(row[0]);
		cs.setxDataSourceType(row[1]);
		cs.setOrganizationSourceValue(row[2]);
		cs.setPlaceOfServiceSourceValue(row[3]);
		cs.setxCareSiteName(row[4]);
		cs.setCareSiteAddress1(row[5]);
		cs.setCareSiteAddress2(row[6]);
		cs.setCareSiteCity(row[7]);
		cs.setCareSiteState(row[8]);
		cs.setCareSiteZip(row[9]);
		cs.setCareSiteCounty(row[10]);
		cs.setId(String.valueOf(id));
		cs.setOrganizationId("0");
		if (trackParents) {
			cs.setOrganizationId(getOrganizationIdByOrganizationSourceValue(cs.getOrganizationSourceValue()));
		}
		return cs;
	}
	
	private Provider parseProvider(String[] row) {
		Provider prov = new Provider();
		prov.setProviderSourceValue(row[0]);
		prov.setxDataSourceType(row[1]);
		prov.setNpi(row[2]);
		prov.setDea(row[3]);
		prov.setSpecialtySourceValue(row[4]);
		prov.setxProviderFirst(row[5]);
		prov.setxProviderMiddle(row[6]);
		prov.setxProviderLast(row[7]);
		prov.setCareSiteSourceValue(row[8]);
		prov.setxOrganizationSourceValue(row[9]);
		prov.setCareSiteId("0");
		if (trackParents) {
			prov.setCareSiteId(getCareSiteIdByCareSiteSourceValue(prov.getCareSiteSourceValue()));
		}
		prov.setId(String.valueOf(id));		
		return prov;
	}
	
	private Demographic parseDemographic(String row[]){
		Demographic dem = new Demographic();
		dem.setPersonSourceValue(row[0]);
		dem.setxDataSourceType(row[1]);
		dem.setMedicaidIdNumber(row[2]);
		dem.setSsn(row[3]);
		dem.setLast(row[4]);
		dem.setMiddle(row[5]);
		dem.setFirst(row[6]);
		dem.setAddress1(row[7]);
		dem.setAddress2(row[8]);
		dem.setCity(row[9]);
		dem.setState(row[10]);
		dem.setZip(row[11]);
		dem.setCounty(row[12]);
		dem.setYearOfBirth(row[13]);
		dem.setMonthOfBirth(row[14]);
		dem.setDayOfBirth(row[15]);
		dem.setGenderSourceValue(row[16]);
		dem.setRaceSourceValue(row[17]);
		dem.setEthnicitySourceValue(row[18]);
		dem.setProviderSourceValue(row[19]);
		dem.setCareSiteSourceValue(row[20]);
		dem.setxOrganizationSourceValue(row[21]);
		dem.setOrganizationId("0");
		if (trackParents) {
			dem.setOrganizationId(getOrganizationIdByOrganizationSourceValue(dem.getxOrganizationSourceValue()));
		}
		dem.setId(String.valueOf(id));	
		return dem;
	}
	
	private VisitOccurrence parseVisitOccurrence(String row[]){
		VisitOccurrence visit = new VisitOccurrence();
		visit.setxVisitOccurrenceSourceIdentifier(row[0]);
		visit.setxDataSourceType(row[1]);
		visit.setPersonSourceValue(row[2]);
		visit.setVisitStartDate(row[3]);
		visit.setVisitEndDate(row[4]);
		visit.setPlaceOfServiceSourceValue(row[5]);
		visit.setxProviderSourceValue(row[6]);
		visit.setCareSiteSourceValue(row[7]);
		visit.setDemographicId("0");
		if (trackParents) {
			visit.setDemographicId(getDemographicIdByPersonSourceValue(visit.getPersonSourceValue()));
		}
		visit.setId(String.valueOf(id));
		return visit;	
	}
	
	private Observation parseObservation(String row[]){
		Observation obs = new Observation();
		obs.setObservationSourceIdentifier(row[0]);
		obs.setxDataSourceType(row[1]);
		obs.setPersonSourceValue(row[2]);
		obs.setObservationSourceValue(row[3]);
		obs.setObservationSourceValueVocabulary(row[4]);
		obs.setObservationDate(row[5]);
		obs.setObservationTime(row[6]);
		obs.setValueAsNumber(row[7]);
		obs.setValueAsString(row[8]);
		obs.setUnitSourceValue(row[9]);
		obs.setRangeLow(row[10]);
		obs.setRangeHigh(row[11]);
		obs.setObservationTypeSourceValue(row[12]);
		obs.setAssociatedProviderSourceValue(row[13]);
		obs.setxVisitOccurrenceSourceIdentifier(row[14]);
		obs.setRelevantConditionSourceValue(row[15]);
		obs.setxObsComment(row[16]);
		obs.setDemographicId("0");
		if (trackParents) {
			obs.setDemographicId(getDemographicIdByPersonSourceValue(obs.getPersonSourceValue()));
		}
		obs.setId(String.valueOf(id));
		return obs;
	}
	
	private DrugExposure parseDrugExposure(String row[]){
		DrugExposure drug = new DrugExposure();
		drug.setDrugExposureSourceIdentifier(row[0]);
		drug.setxDataSourceType(row[1]);
		drug.setPersonSourceValue(row[2]);
		drug.setDrugSourceValue(row[3]);
		drug.setDrugSourceValueVocabulary(row[4]);
		drug.setDrugExposureStartDate(row[5]);
		drug.setDrugExposureEndDate(row[6]);
		drug.setDrugTypeSourceValue(row[7]);
		drug.setStopReason(row[8]);
		drug.setRefills(row[9]);
		drug.setQuantity(row[10]);
		drug.setDaysSupply(row[11]);
		drug.setxDrugName(row[12]);
		drug.setxDrugStrength(row[13]);
		drug.setSig(row[14]);
		drug.setProviderSourceValue(row[15]);
		drug.setxVisitOccurrenceSourceIdentifier(row[16]);
		drug.setRelevantConditionSourceValue(row[17]);
		drug.setDemographicId("0");
		if (trackParents) {
			drug.setDemographicId(getDemographicIdByPersonSourceValue(drug.getPersonSourceValue()));
		}
		drug.setId(String.valueOf(id));
		return drug;
	}
	
	private DrugCost parseDrugCost(String row[]){
		DrugCost cost = new DrugCost();
		cost.setDrugCostSourceIdentifier(row[0]);
		cost.setDrugExposureSourceIdentifier(row[1]);
		cost.setPaidCopay(row[2]);
		cost.setPaidCoinsurance(row[3]);
		cost.setPaidTowardDeductible(row[4]);
		cost.setPaidByPayer(row[5]);
		cost.setPaidByCoordinationBenefits(row[6]);
		cost.setTotalOutOfPocket(row[7]);
		cost.setTotalPaid(row[8]);
		cost.setIngredientCost(row[9]);
		cost.setDispensingFee(row[10]);
		cost.setAverageWholesalePrice(row[11]);
		cost.setPayerPlanPeriodSourceIdentifier(row[12]);
		cost.setDrugExposureId("0");
		if (trackParents) {
			cost.setDrugExposureId(getDrugExposureIdByDrugExposureSourceIdentifier(cost.getDrugExposureSourceIdentifier()));
		}
		cost.setId(String.valueOf(id));
		return cost;
	}
	
	private ConditionOccurrence parseConditionOccurrence(String row[]){
		ConditionOccurrence co = new ConditionOccurrence();
		co.setConditionOccurrenceSourceIdentifier(row[0]);
		co.setxDataSourceType(row[1]);
		co.setPersonSourceValue(row[2]);
		co.setConditionSourceValue(row[3]);
		co.setConditionSourceValueVocabulary(row[4]);
		co.setxConditionSourceDesc(row[5]);
		co.setConditionStartDate(row[6]);
		co.setxConditionUpdateDate(row[7]);
		co.setConditionEndDate(row[8]);
		co.setConditionTypeSourceValue(row[9]);
		co.setStopReason(row[10]);
		co.setAssociatedProviderSourceValue(row[11]);
		co.setxVisitOccurrenceSourceIdentifier(row[12]);
		co.setDemographicId("0");
		if (trackParents) {
			co.setDemographicId(getDemographicIdByPersonSourceValue(co.getPersonSourceValue()));
		}
		co.setId(String.valueOf(id));
		return co;
	}
	
	private ProcedureOccurrence parseProcedureOccurrence(String row[]){
		ProcedureOccurrence po = new ProcedureOccurrence();
		po.setProcedureOccurrenceSourceIdentifier(row[0]);
		po.setxDataSourceType(row[1]);
		po.setPersonSourceValue(row[2]);
		po.setProcedureSourceValue(row[3]);
		po.setProcedureSourceValueVocabulary(row[4]);
		po.setProcedureDate(row[5]);
		po.setProcedureTypeSourceValue(row[6]);
		po.setProviderRecordSourceValue(row[7]);
		po.setxVisitOccurrenceSourceIdentifier(row[8]);
		po.setRelevantConditionSourceValue(row[9]);
		po.setDemographicId("0");
		if (trackParents) {
			po.setDemographicId(getDemographicIdByPersonSourceValue(po.getPersonSourceValue()));
		}
		po.setId(String.valueOf(id));
		return po;
	}
	
	private ProcedureCost parseProcedureCost(String row[]){
		ProcedureCost cost = new ProcedureCost();
		cost.setProcedureCostSourceIdentifier(row[0]);
		cost.setProcedureOccurrenceSourceIdentifier(row[1]);
		cost.setPaidCopay(Double.parseDouble(row[2]));
		cost.setPaidCoinsurance(Double.parseDouble(row[3]));
		cost.setPaidTowardDeductible(Double.parseDouble(row[4]));
		cost.setPaidByPayer(Double.parseDouble(row[5]));
		cost.setPaidByCoordinationBenefits(Double.parseDouble(row[6]));
		cost.setTotalOutOfPocket(Double.parseDouble(row[7]));
		cost.setTotalPaid(Double.parseDouble(row[8]));
		cost.setDiseaseClassConceptId(row[9]);
		cost.setDiseaseClassSourceValue(row[10]);
		cost.setRevenueCodeConceptId(row[11]);
		cost.setRevenueCodeSourceValue(row[12]);
		cost.setPayerPlanPeriodSourceIdentifier(row[13]);
		cost.setProcedureOccurrenceId("0");
		if (trackParents) {
			cost.setProcedureOccurrenceId(getProcedureOccurrenceIdByProcedureOccurrenceSourceIdentifier(cost.getProcedureOccurrenceSourceIdentifier()));
		}
		cost.setId(String.valueOf(id));
		return cost;
	}
	
	private Cohort parseCohort(String row[]){
		Cohort co = new Cohort();
		co.setCohortSourceIdentifier(row[0]);
		co.setCohortSourceValue(row[1]);
		co.setCohortStartDate(row[2]);
		co.setCohortEndDate(row[3]);
		co.setSubjectSourceIdentifier(row[4]);
		co.setStopReason(row[5]);
		co.setDemographicId("0");
		if (trackParents) {
			co.setDemographicId(getDemographicIdByPersonSourceValue(co.getSubjectSourceIdentifier()));
		}
		co.setId(String.valueOf(id));
		return co;	
	}
	
	private Death parseDeath(String row[]){
		Death death = new Death();
		death.setPersonSourceValue(row[0]);
		death.setDeathDate(row[1]);
		death.setDeathTypeConceptId(row[2]);
		death.setDeathTypeSourceValue(row[3]);
		death.setCauseOfDeathSourceValue(row[4]);
		death.setDemographicId("0");
		if (trackParents) {
			death.setDemographicId(getDemographicIdByPersonSourceValue(death.getPersonSourceValue()));
		}
		death.setId(String.valueOf(id));
		return death;	
	}
	
	private PayerPlanPeriod parsePayerPlanPeriod(String row[]){
		PayerPlanPeriod ppp = new PayerPlanPeriod();
		ppp.setPayerPlanPeriodSourceIdentifier(row[0]);
		ppp.setPersonSourceValue(row[1]);
		ppp.setPayerPlanPeriodStartDate(row[2]);
		ppp.setPayerPlanPeriodEndDate(row[3]);
		ppp.setPayerSourceValue(row[4]);
		ppp.setPlanSourceValue(row[5]);
		ppp.setFamilySourceValue(row[6]);
		ppp.setDemographicId("0");
		if (trackParents) {
			ppp.setDemographicId(getDemographicIdByPersonSourceValue(ppp.getPersonSourceValue()));
		}
		ppp.setId(String.valueOf(id));
		return ppp;			
	}
	
	private String getOrganizationIdByOrganizationSourceValue(String value){
		Long id = organizations.get(value);
		if (id == null) {
			return "";
		}
		return id.toString();
	}
	
	private String getCareSiteIdByCareSiteSourceValue(String value){
		Long careSiteId = careSites.get(value);
		if (careSiteId == null) {
			return "";
		}
		return careSiteId.toString();
	}
	
	private String getDemographicIdByPersonSourceValue(String value){
		Long demographicsId = demographics.get(value);
		if (demographicsId == null) {
			return "";
		}
		return demographicsId.toString();
	}
	
	private String getDrugExposureIdByDrugExposureSourceIdentifier(String value){
		Long drugExposureId = drugExposures.get(value);
		if (drugExposureId == null) {
			return "";
		}
		return drugExposureId.toString();
	}
	
	private String getProcedureOccurrenceIdByProcedureOccurrenceSourceIdentifier(String value){
		Long procedureOccurrenceId = procedureOccurrences.get(value);
		if (procedureOccurrenceId == null) {
			return "";
		}
		return procedureOccurrenceId.toString();
	}

	public Long getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(Long totalLines) {
		this.totalLines = totalLines;
		this.outputThreshold = totalLines;
	}
}
