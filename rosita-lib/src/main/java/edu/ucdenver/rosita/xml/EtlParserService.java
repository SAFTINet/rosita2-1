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

package edu.ucdenver.rosita.xml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.SAXException;

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
import edu.ucdenver.rosita.utils.UnmarshallerPool;

public class EtlParserService {

	List<String> demographicSubelementNames = Arrays.asList(new String[] {"visit_occurrence", "observation", "drug_exposure", "condition_occurrence", "procedure_occurrence", "cohort", "death", "payer_plan_period"});
	Long id;
	Long elements = 0L;
	Long outputDelay = 0L;
	Long outputLimit = null;
	UnmarshallerPool unmarshallerPool = null;
	EtlCache objectCache;
	Long dataSourceId;
	
	public EtlParserService(DataSource ds, Integer threshold, Long dataSourceId) throws JAXBException {
		unmarshallerPool = new UnmarshallerPool();
		objectCache = new EtlCache(ds, threshold, dataSourceId);
		this.dataSourceId = dataSourceId;
	}
	
	public void setOutputLimit(Long limit) {
		outputLimit = limit;
	}
	
    public Long parse(String filename, Integer currentClinic, Integer maxClinics) throws Exception {
    	
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader reader = null;
		try {
			reader = factory.createXMLStreamReader(new FileReader(filename));
		}
		catch (FileNotFoundException e) {
			RositaLogger.log(false, filename + "- File not found");
			RositaLogger.error(e);
			RositaLogger.log(true, "ERROR|||" + filename + "- File not found");
			throw new Exception("File not found");
		}

		//Keep objects that need to be referred to as parents
		Organization organization = null;
		CareSite careSite = null;
		Demographic demographic = null;
		DrugExposure drugExposure = null;
		ProcedureOccurrence procedureOccurrence = null;
		
		id = 0L;
		
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
							RositaLogger.log(true, "STATUS|||LOAD|||" + elements + "|||" + dataSourceId + "|||" + currentClinic + "|||" + maxClinics);
							outputDelay -= outputLimit;
						}
					}
					catch (Exception e) {
						//SQL errors are caught here - if this happens, something is probably wrong with the schema
						RositaLogger.log(true, "ERROR|||" + e.getClass() + ": " + e.getCause().getMessage() + "|||" + reader.getLocation().getLineNumber() + "|||" + reader.getLocation().getColumnNumber() + "|||" + reader.getLocation().getCharacterOffset());
						RositaLogger.error(e);
						RositaLogger.log(false, e.getClass() + ": " + e.getCause().getMessage() + ". Line number " + reader.getLocation().getLineNumber() + ", column " + reader.getLocation().getColumnNumber() + ", offset " + reader.getLocation().getCharacterOffset());
						throw new Exception(e.getCause().getMessage());
					}
					break;
				case XMLStreamReader.END_ELEMENT:
					break;
				case XMLStreamReader.CHARACTERS:
					break;
				case XMLStreamReader.END_DOCUMENT:
					break;
				default:
					break;
				}
			}
			objectCache.saveAll();
			return elements;
		}
		catch (Exception e) {
			//This section catches unexpected SQL exceptions - we want to just halt on these
			RositaLogger.error(e);
			e.printStackTrace();
			System.exit(1);
		}
		finally {
			reader.close();
			organization = null;
		}
		return 0L;
    }

	private Organization parseOrganization(XMLStreamReader reader) throws JAXBException, XMLStreamException {
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
	
	private CareSite parseCareSite(XMLStreamReader reader) throws JAXBException, XMLStreamException {
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
	
	private Demographic parseDemographic(XMLStreamReader reader) throws JAXBException, XMLStreamException {
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
	
	private DrugExposure parseDrugExposure(XMLStreamReader reader) throws JAXBException, XMLStreamException {
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
	
	private ProcedureOccurrence parseProcedureOccurrence(XMLStreamReader reader) throws JAXBException, XMLStreamException {
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
	
	private Object parseLeafElement(XMLStreamReader reader, String tagName) throws JAXBException, XMLStreamException {
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
	
	private String getSafeXml(String string) {
		return StringEscapeUtils.escapeXml(string);
	}

	public Long getElements() {
		return elements;
	}

	public void setElements(Long elements) {
		this.elements = elements;
		this.outputDelay = 0L;
	}
		
}
