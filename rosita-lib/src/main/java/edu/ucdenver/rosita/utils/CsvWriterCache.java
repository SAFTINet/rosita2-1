package edu.ucdenver.rosita.utils;

import javax.sql.DataSource;

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
import edu.ucdenver.rosita.etl.dao.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Runtime;
import edu.ucdenver.rosita.utils.RositaLogger;
/*
import org.postgresql.Driver;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
*/

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

public class CsvWriterCache {
    int OBJECT_THRESHOLD = 5000;

    static Stopwatch stopwatch = new Stopwatch();

    static FileWriter fw_x_demographic = null;
    static FileWriter fw_visit_occurrence = null;
    static FileWriter fw_procedure_occurrence = null;
    static FileWriter fw_procedure_cost = null;
    static FileWriter fw_condition_occurrence = null;
    static FileWriter fw_observation = null;
    static FileWriter fw_drug_exposure = null;
    static FileWriter fw_drug_cost = null;
    static FileWriter fw_provider = null;
    static FileWriter fw_care_site = null;
    static FileWriter fw_organization = null;
    static FileWriter fw_payer_plan_period = null;
    static FileWriter fw_death = null;
    static FileWriter fw_cohort = null;


    final ArrayList<Organization> organizations;
    final ArrayList<CareSite> careSites;
    final ArrayList<Provider> providers;
    final ArrayList<Demographic> demographics;
    final ArrayList<VisitOccurrence> visitOccurrences;
    final ArrayList<Observation> observations;
    final ArrayList<DrugExposure> drugExposures;
    final ArrayList<DrugCost> drugCosts;
    final ArrayList<ConditionOccurrence> conditionOccurrences;
    final ArrayList<ProcedureOccurrence> procedureOccurrences;
    final ArrayList<ProcedureCost> procedureCosts;
    final ArrayList<Cohort> cohorts;
    final ArrayList<Death> deaths;
    final ArrayList<PayerPlanPeriod> payerPlanPeriods;

    public CsvWriterCache(Integer threshold, String directory) {
        if (threshold != null && threshold >= 0) {
            OBJECT_THRESHOLD = threshold;
        }

        RositaLogger.getInstance();
        try {
            fw_x_demographic = new FileWriter(new File(directory + "/x_demographic.csv"));
            fw_visit_occurrence = new FileWriter(new File(directory + "/visit_occurrence.csv"));
            fw_procedure_occurrence = new FileWriter(new File(directory + "/procedure_occurrence.csv"));
            fw_procedure_cost = new FileWriter(new File(directory + "/procedure_cost.csv"));
            fw_condition_occurrence = new FileWriter(new File(directory + "/condition_occurrence.csv"));
            fw_observation = new FileWriter(new File(directory + "/observation.csv"));
            fw_drug_exposure = new FileWriter(new File(directory + "/drug_exposure.csv"));
            fw_drug_cost = new FileWriter(new File(directory + "/drug_cost.csv"));
            fw_provider = new FileWriter(new File(directory + "/provider.csv"));
            fw_care_site = new FileWriter(new File(directory + "/care_site.csv"));
            fw_organization = new FileWriter(new File(directory + "/organization.csv"));
            fw_payer_plan_period = new FileWriter(new File(directory + "/payer_plan_period.csv"));
            fw_death = new FileWriter(new File(directory + "/death.csv"));
            fw_cohort = new FileWriter(new File(directory + "/cohort.csv"));
        } catch(Exception e) {
            RositaLogger.log(false,"IOException when instantiating the FileWriters");
            System.exit(1);
        }
        this.organizations = new ArrayList<Organization>();
        this.careSites = new ArrayList<CareSite>();
        this.providers = new ArrayList<Provider>();
        this.demographics = new ArrayList<Demographic>();
        this.visitOccurrences = new ArrayList<VisitOccurrence>();
        this.observations = new ArrayList<Observation>();
        this.drugExposures = new ArrayList<DrugExposure>();
        this.drugCosts = new ArrayList<DrugCost>();
        this.conditionOccurrences = new ArrayList<ConditionOccurrence>();
        this.procedureOccurrences = new ArrayList<ProcedureOccurrence>();
        this.procedureCosts = new ArrayList<ProcedureCost>();
        this.cohorts = new ArrayList<Cohort>();
        this.deaths = new ArrayList<Death>();
        this.payerPlanPeriods = new ArrayList<PayerPlanPeriod>();

        stopwatch.start();
    }

    public void add(Organization o) throws IOException {
        if(organizations.add(o)) {
            if(organizations.size() > OBJECT_THRESHOLD) {
                for(Organization org : organizations) {
                    fw_organization.write(org.dataToCsv());
                }
                RositaLogger.log(false, "Saved organizations - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                organizations.clear();
            }
        }
    }

    public void add(CareSite o) throws IOException {
        if (careSites.add(o)) {
            if(careSites.size() > OBJECT_THRESHOLD) {
                for(CareSite site : careSites) {
                    fw_care_site.write(site.dataToCsv());
                }
                RositaLogger.log(false, "Saved careSites - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                careSites.clear();
            }
        }
    }

    public void add(Provider o) throws IOException {
        if (providers.add(o)) {
            if(providers.size() > OBJECT_THRESHOLD) {
                for(Provider pro : providers) {
                    fw_provider.write(pro.dataToCsv());
                }
                RositaLogger.log(false, "Saved providers - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                providers.clear();
            }
        }
    }

    public void add(Demographic o) throws IOException {
        if (demographics.add(o)) {
            if(demographics.size() > OBJECT_THRESHOLD) {
                for(Demographic d : demographics) {
                    fw_x_demographic.write(d.dataToCsv());
                }
                RositaLogger.log(false, "Saved demographics - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                demographics.clear();
            }
        }
    }

    public void add(VisitOccurrence o) throws IOException {
        if (visitOccurrences.add(o)) {
            if(visitOccurrences.size() > OBJECT_THRESHOLD) {
                for(VisitOccurrence visit : visitOccurrences) {
                    fw_visit_occurrence.write(visit.dataToCsv());
                }
                RositaLogger.log(false, "Saved visitOccurrences - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                visitOccurrences.clear();
            }
        }
    }

    public void add(Observation o) throws IOException {
        if (observations.add(o)) {
            if(observations.size() > OBJECT_THRESHOLD) {
                for(Observation ob : observations) {
                    fw_observation.write(ob.dataToCsv());
                }
                RositaLogger.log(false, "Saved observations - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                observations.clear();
            }

        }
    }

    public void add(DrugExposure o) throws IOException {
        if (drugExposures.add(o)) {
            if(drugExposures.size() > OBJECT_THRESHOLD) {
                for(DrugExposure de : drugExposures) {
                    fw_drug_exposure.write(de.dataToCsv());
                }
                RositaLogger.log(false, "Saved drugExposures - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                drugExposures.clear();
            }

        }
    }

    public void add(DrugCost o) throws IOException {
        if (drugCosts.add(o)) {
            if(drugCosts.size() > OBJECT_THRESHOLD) {
                for(DrugCost cost : drugCosts) {
                    fw_drug_cost.write(cost.dataToCsv());
                }
                RositaLogger.log(false, "Saved drugCosts - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                drugCosts.clear();
            }

        }
    }

    public void add(ConditionOccurrence o) throws IOException {
        if (conditionOccurrences.add(o)) {
            if(conditionOccurrences.size() > OBJECT_THRESHOLD) {
                for(ConditionOccurrence cond : conditionOccurrences) {
                    fw_condition_occurrence.write(cond.dataToCsv());
                }
                RositaLogger.log(false, "Saved conditionOccurrences - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                conditionOccurrences.clear();
            }

        }
    }

    public void add(ProcedureOccurrence o) throws IOException {
        if (procedureOccurrences.add(o)) {
            if(procedureOccurrences.size() > OBJECT_THRESHOLD) {
                for(ProcedureOccurrence occ : procedureOccurrences) {
                    fw_procedure_occurrence.write(occ.dataToCsv());
                }
                RositaLogger.log(false, "Saved procedureOccurrences - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                procedureOccurrences.clear();
            }

        }
    }

    public void add(ProcedureCost o) throws IOException {
        if (procedureCosts.add(o)) {
            if(procedureCosts.size() > OBJECT_THRESHOLD) {
                for(ProcedureCost cost : procedureCosts) {
                    fw_procedure_cost.write(cost.dataToCsv());
                }
                RositaLogger.log(false, "Saved procedureCosts - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                procedureCosts.clear();
            }

        }
    }

    public void add(Cohort o) throws IOException {
        if (cohorts.add(o)) {
            if(cohorts.size() > OBJECT_THRESHOLD) {
                for(Cohort coh : cohorts) {
                    fw_cohort.write(coh.dataToCsv());
                }
                RositaLogger.log(false, "Saved cohorts - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                cohorts.clear();
            }

        }
    }

    public void add(Death o) throws IOException {
        if (deaths.add(o)) {
            if(deaths.size() > OBJECT_THRESHOLD) {
                for(Death death : deaths) {
                    fw_death.write(death.dataToCsv());
                }
                RositaLogger.log(false, "Saved deaths - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                deaths.clear();
            }

        }
    }

    public void add(PayerPlanPeriod o) throws IOException {
        if (payerPlanPeriods.add(o)) {
            if(payerPlanPeriods.size() > OBJECT_THRESHOLD) {
                for(PayerPlanPeriod plan : payerPlanPeriods) {
                    fw_payer_plan_period.write(plan.dataToCsv());
                }
                RositaLogger.log(false, "Saved payerPlanPeriods - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
                payerPlanPeriods.clear();
            }
        }
    }

    public void saveAll() throws IOException {
        for(Organization org : organizations) {
            fw_organization.write(org.dataToCsv());
        }
        for(CareSite site : careSites) {
            fw_care_site.write(site.dataToCsv());
        }
        for(Provider pro : providers) {
            fw_provider.write(pro.dataToCsv());
        }
        for(Demographic d : demographics) {
            fw_x_demographic.write(d.dataToCsv());
        }
        for(VisitOccurrence visit : visitOccurrences) {
            fw_visit_occurrence.write(visit.dataToCsv());
        }
        for(Observation ob : observations) {
            fw_observation.write(ob.dataToCsv());
        }
        for(DrugExposure de : drugExposures) {
            fw_drug_exposure.write(de.dataToCsv());
        }
        for(DrugCost cost : drugCosts) {
            fw_drug_cost.write(cost.dataToCsv());
        }
        for(ConditionOccurrence cond : conditionOccurrences) {
            fw_condition_occurrence.write(cond.dataToCsv());
        }
        for(ProcedureOccurrence occ : procedureOccurrences) {
            fw_procedure_occurrence.write(occ.dataToCsv());
        }
        for(ProcedureCost cost : procedureCosts) {
            fw_procedure_cost.write(cost.dataToCsv());
        }
        for(Cohort coh : cohorts) {
            fw_cohort.write(coh.dataToCsv());
        }
        for(Death death : deaths) {
            fw_death.write(death.dataToCsv());
        }
        for(PayerPlanPeriod plan : payerPlanPeriods) {
            fw_payer_plan_period.write(plan.dataToCsv());
        }

        //save anything left in the arraylists to file
        try {
            RositaLogger.log(false, "Saved everything - " + stopwatch.getElapsedTime() + "ms, free memory " + Runtime.getRuntime().freeMemory());
        } catch(Exception e) {

        } finally {
            try {
                fw_x_demographic.flush();
                fw_visit_occurrence.flush();
                fw_procedure_occurrence.flush();
                fw_procedure_cost.flush();
                fw_condition_occurrence.flush();
                fw_observation.flush();
                fw_drug_exposure.flush();
                fw_drug_cost.flush();
                fw_provider.flush();
                fw_care_site.flush();
                fw_organization.flush();
                fw_payer_plan_period.flush();
                fw_death.flush();
                fw_cohort.flush();

                fw_x_demographic.close();
                fw_visit_occurrence.close();
                fw_procedure_occurrence.close();
                fw_procedure_cost.close();
                fw_condition_occurrence.close();
                fw_observation.close();
                fw_drug_exposure.close();
                fw_drug_cost.close();
                fw_provider.close();
                fw_care_site.close();
                fw_organization.close();
                fw_payer_plan_period.close();
                fw_death.close();
                fw_cohort.close();
            }
            catch (Exception e) {
                //Not really much else we can do
            }
        }
    }
}
