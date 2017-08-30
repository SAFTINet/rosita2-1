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

package edu.ucdenver.rosita.utils;

import edu.ucdenver.rosita.etl.*;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

public class XmlToCsvWriter {


    static List<String> demographicSubelementNames = Arrays.asList(new String[]{"visit_occurrence", "observation", "drug_exposure", "condition_occurrence", "procedure_occurrence", "cohort", "death", "payer_plan_period"});
    static Long id;
    static Long elements;
    static Long outputDelay = 0L;
    static Long outputLimit = null;
    static UnmarshallerPool unmarshallerPool = null;
    static CsvWriterCache objectCache;
    static XMLStreamReader reader = null;

    public static void main(String[] args) {
        try {
            generate(args);
        } catch(Exception e) {

        }
    }

    public static void generate(String[] args) {
        System.out.println("Give us a minute...");
        RositaLogger.getInstance();
        try {
            if (args.length < 2) {
                System.out.println("I need: 'target directory for CSV files' 'XML filename to convert'");
                System.exit(0);
            }

            unmarshallerPool = new UnmarshallerPool();
            objectCache = new CsvWriterCache(1000,args[0]);


            XMLInputFactory factory = XMLInputFactory.newInstance();

            try {
                reader = factory.createXMLStreamReader(new FileReader(args[1]));
            }
            catch (FileNotFoundException e) {
                RositaLogger.log(false, args[1] + "- File not found");
                RositaLogger.error(e);
                RositaLogger.log(false, "ERROR|||" + args[1] + "- File not found");
                System.exit(1);
            }

            parse(reader);

            System.out.println("All done - good luck!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOutputLimit(Long limit) {
        outputLimit = limit;
    }

    private static void parse(XMLStreamReader reader) throws XMLStreamException, JAXBException, FileNotFoundException, IOException, SAXException, ParserConfigurationException {

        //Keep objects that need to be referred to as parents
        Organization organization = null;
        CareSite careSite = null;
        Demographic demographic = null;
        DrugExposure drugExposure = null;
        ProcedureOccurrence procedureOccurrence = null;

        id = 0L;
        elements = 0L;

        Runtime runtime = Runtime.getRuntime();

        try {
            while (reader.hasNext()) {
                int type = reader.next();
                switch (type) {
                    case XMLStreamReader.START_ELEMENT:
                        elements++;
                        try {
                            if ("organization".equals(reader.getLocalName())) {
                                organization = parseOrganization(reader);
                                objectCache.add(organization);
                            }

                            // Don't use "if else" here because parseOrganization will return with a care_site element already loaded.
                            if ("care_site".equals(reader.getLocalName())) {
                                careSite = parseCareSite(reader);
                                careSite.setOrganizationId(organization.getId());
                                objectCache.add(careSite);
                            }

                            if ("provider".equals(reader.getLocalName())) {
                                Provider provider = (Provider) parseLeafElement(reader, "provider");
                                provider.setId(String.valueOf(id));
                                provider.setCareSiteId(careSite.getId());
                                objectCache.add(provider);
                            }

                            if ("x_demographic".equals(reader.getLocalName())) {
                                demographic = parseDemographic(reader);
                                demographic.setOrganizationId(organization.getId());
                                objectCache.add(demographic);
                            }

                            if ("visit_occurrence".equals(reader.getLocalName())) {
                                VisitOccurrence visitOccurrence = (VisitOccurrence) parseLeafElement(reader, "visit_occurrence");
                                visitOccurrence.setId(String.valueOf(id));
                                visitOccurrence.setDemographicId(demographic.getId());
                                objectCache.add(visitOccurrence);
                            }

                            if ("observation".equals(reader.getLocalName())) {
                                Observation observation = (Observation) parseLeafElement(reader, "observation");
                                observation.setId(String.valueOf(id));
                                observation.setDemographicId(demographic.getId());
                                objectCache.add(observation);
                            }

                            if ("drug_exposure".equals(reader.getLocalName())) {
                                drugExposure = parseDrugExposure(reader);
                                drugExposure.setDemographicId(demographic.getId());
                                objectCache.add(drugExposure);
                            }

                            if ("drug_cost".equals(reader.getLocalName())) {
                                DrugCost drugCost = (DrugCost) parseLeafElement(reader, "drug_cost");
                                drugCost.setId(String.valueOf(id));
                                drugCost.setDrugExposureId(drugCost.getId());
                                objectCache.add(drugCost);
                            }

                            if ("condition_occurrence".equals(reader.getLocalName())) {
                                ConditionOccurrence conditionOccurrence = (ConditionOccurrence) parseLeafElement(reader, "condition_occurrence");
                                conditionOccurrence.setId(String.valueOf(id));
                                conditionOccurrence.setDemographicId(demographic.getId());
                                objectCache.add(conditionOccurrence);
                            }

                            if ("procedure_occurrence".equals(reader.getLocalName())) {
                                procedureOccurrence = parseProcedureOccurrence(reader);
                                procedureOccurrence.setDemographicId(demographic.getId());
                                objectCache.add(procedureOccurrence);
                            }

                            if ("procedure_cost".equals(reader.getLocalName())) {
                                ProcedureCost procedureCost = (ProcedureCost) parseLeafElement(reader, "procedure_cost");
                                procedureCost.setId(String.valueOf(id));
                                procedureCost.setProcedureOccurrenceId(demographic.getId());
                                objectCache.add(procedureCost);
                            }

                            if ("cohort".equals(reader.getLocalName())) {
                                Cohort cohort = (Cohort) parseLeafElement(reader, "cohort");
                                cohort.setId(String.valueOf(id));
                                cohort.setDemographicId(demographic.getId());
                                objectCache.add(cohort);
                            }

                            if ("death".equals(reader.getLocalName())) {
                                Death death = (Death) parseLeafElement(reader, "death");
                                death.setId(String.valueOf(id));
                                death.setDemographicId(demographic.getId());
                                objectCache.add(death);
                            }

                            if ("payer_plan_period".equals(reader.getLocalName())) {
                                PayerPlanPeriod payerPlanPeriod = (PayerPlanPeriod) parseLeafElement(reader, "payer_plan_period");
                                payerPlanPeriod.setId(String.valueOf(id));
                                payerPlanPeriod.setDemographicId(demographic.getId());
                                objectCache.add(payerPlanPeriod);
                            }

                            outputDelay++;
                            if (outputLimit != null && outputDelay >= outputLimit) {
                                RositaLogger.log(true, "STATUS|||LOAD|||" + elements + "|||" + runtime.freeMemory() + "|||" + runtime.maxMemory());
                                outputDelay -= outputLimit;
                            }
                        }
                        catch (Exception e) {
                            //SQL errors are caught here - if this happens, something is probably wrong with the schema
                            RositaLogger.log(true, "ERROR|||" + e.getClass() + ": " + e.getCause().getMessage() + "|||" + reader.getLocation().getLineNumber() + "|||" + reader.getLocation().getColumnNumber() + "|||" + reader.getLocation().getCharacterOffset());
                            RositaLogger.error(e);
                            RositaLogger.log(false, e.getClass() + ": " + e.getCause().getMessage() + ". Line number " + reader.getLocation().getLineNumber() + ", column " + reader.getLocation().getColumnNumber() + ", offset " + reader.getLocation().getCharacterOffset());
                            System.exit(1);
                        }
                        break;
                    case XMLStreamReader.END_ELEMENT:
//            		logger.error("END_ELEMENT: " + reader.getLocalName());
                        break;
                    case XMLStreamReader.CHARACTERS:
//            		logger.error("CHARACTERS: " + reader.getText());
                        break;
                    case XMLStreamReader.END_DOCUMENT:
//          		logger.error("END_DOCUMENT");
                        break;
                    default:
//            		logger.error("OTHER: " + type);
                        break;
                }
            }
            objectCache.saveAll();
        } finally {
            reader.close();
            organization = null;
        }
    }

    private static Organization parseOrganization(XMLStreamReader reader) throws JAXBException, XMLStreamException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><organization>");
        boolean done = false;
        while (!done && reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    elements++;
                    if ("care_site".equals(reader.getLocalName())) {
                        xml.append("</organization>");
                        done = true;
                    } else {
                        xml.append("<").append(reader.getLocalName()).append(">");
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    xml.append("</").append(reader.getLocalName()).append(">");
                    if ("organization".equals(reader.getLocalName())) {
                        done = true;
                        break;
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    xml.append(getSafeXml(reader.getText()));
                    break;
                default:
                    break;
            }
        }

        Organization organization = (Organization) unmarshallerPool.organizationUnmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
        id++;
        organization.setId(String.valueOf(id));
        return organization;

    }

    private static CareSite parseCareSite(XMLStreamReader reader) throws JAXBException, XMLStreamException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><care_site>");
        boolean done = false;
        while (!done && reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    elements++;
                    if ("provider".equals(reader.getLocalName())) {
                        xml.append("</care_site>");
                        done = true;
                    } else {
                        xml.append("<").append(reader.getLocalName()).append(">");
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    xml.append("</").append(reader.getLocalName()).append(">");
                    if ("care_site".equals(reader.getLocalName())) {
                        done = true;
                        break;
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    xml.append(getSafeXml(reader.getText()));
                    break;
                default:
                    break;
            }
        }

        CareSite careSite = (CareSite) unmarshallerPool.careSiteUnmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
        id++;
        careSite.setId(String.valueOf(id));
        return careSite;

    }

    private static Demographic parseDemographic(XMLStreamReader reader) throws JAXBException, XMLStreamException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><x_demographic>");
        boolean done = false;
        while (!done && reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    elements++;
                    if (demographicSubelementNames.contains(reader.getLocalName())) {
                        xml.append("</x_demographic>");
                        done = true;
                    } else {
                        xml.append("<").append(reader.getLocalName()).append(">");
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    xml.append("</").append(reader.getLocalName()).append(">");
                    if ("x_demographic".equals(reader.getLocalName())) {
                        done = true;
                        break;
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    xml.append(getSafeXml(reader.getText()));
                    break;
                default:
                    break;
            }
        }

        Demographic demographic = (Demographic) unmarshallerPool.demographicUnmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
        id++;
        demographic.setId(String.valueOf(id));
        return demographic;

    }

    private static DrugExposure parseDrugExposure(XMLStreamReader reader) throws JAXBException, XMLStreamException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><drug_exposure>");
        boolean done = false;
        while (!done && reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    elements++;
                    if ("drug_cost".equals(reader.getLocalName())) {
                        xml.append("</drug_exposure>");
                        done = true;
                    } else {
                        xml.append("<").append(reader.getLocalName()).append(">");
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    xml.append("</").append(reader.getLocalName()).append(">");
                    if ("drug_exposure".equals(reader.getLocalName())) {
                        done = true;
                        break;
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    xml.append(getSafeXml(reader.getText()));
                    break;
                default:
                    break;
            }
        }

        DrugExposure drugExposure = (DrugExposure) unmarshallerPool.drugExposureUnmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
        id++;
        drugExposure.setId(String.valueOf(id));
        return drugExposure;

    }

    private static ProcedureOccurrence parseProcedureOccurrence(XMLStreamReader reader) throws JAXBException, XMLStreamException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><procedure_occurrence>");
        boolean done = false;
        while (!done && reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    elements++;
                    if ("procedure_cost".equals(reader.getLocalName())) {
                        xml.append("</procedure_occurrence>");
                        done = true;
                    } else {
                        xml.append("<").append(reader.getLocalName()).append(">");
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    xml.append("</").append(reader.getLocalName()).append(">");
                    if ("procedure_occurrence".equals(reader.getLocalName())) {
                        done = true;
                        break;
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    xml.append(getSafeXml(reader.getText()));
                    break;
                default:
                    break;
            }
        }

        ProcedureOccurrence procedureOccurrence = (ProcedureOccurrence) unmarshallerPool.procedureOccurrenceUnmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
        id++;
        procedureOccurrence.setId(String.valueOf(id));
        return procedureOccurrence;

    }

    private static Object parseLeafElement(XMLStreamReader reader, String tagName) throws JAXBException, XMLStreamException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?><" + tagName + ">");
        boolean done = false;
        while (!done && reader.hasNext()) {
            int type = reader.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    elements++;
                    xml.append("<").append(reader.getLocalName()).append(">");
                    break;
                case XMLStreamReader.END_ELEMENT:
                    xml.append("</").append(reader.getLocalName()).append(">");
                    if (tagName.equals(reader.getLocalName())) {
                        done = true;
                        break;
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                case XMLStreamReader.SPACE:
                    xml.append(getSafeXml(reader.getText()));
                    break;
                default:
                    break;
            }
        }

        Object parsedObject = null;
        Unmarshaller myUnmarshaller = unmarshallerPool.get(tagName);
        parsedObject = myUnmarshaller.unmarshal(new StreamSource(new StringReader(xml.toString())));
        id++;
        return parsedObject;

    }

    private static String getSafeXml(String string) {
        return StringEscapeUtils.escapeXml(string);
    }
}
