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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.RositaValidationErrorHandler;
import edu.ucdenver.rosita.utils.StackHandler;
import edu.ucdenver.rosita.utils.Stopwatch;
import edu.ucdenver.rosita.utils.ValidationErrorCache;

public class EtlValidatorService {
	
	Integer threshold = 5000;
	boolean saveErrors = false;
	ValidationErrorCache cache = null;
	Long dataSourceId = 1L;
	
	public EtlValidatorService(DataSource ds, Integer threshold, boolean saveErrors, Long dataSourceId) {
		this.dataSourceId = dataSourceId;
		this.cache = new ValidationErrorCache(ds, threshold, dataSourceId);
		if (threshold != null && threshold > 0) {
			this.threshold = threshold;
		}
		this.saveErrors = saveErrors;
	}
	
	public Long validate(String filename, String schema, Long maxErrors, Long stepId, Integer currentClinic, Integer maxClinics) throws SAXException, ParserConfigurationException, FileNotFoundException, IOException {
		
		File xmlFile = new File(filename);
		if (!xmlFile.exists()) {
			throw new FileNotFoundException("XML file does not exist: " + filename);
		}
		File schemaFile = new File(schema);
		if (!schemaFile.exists()) {
			throw new FileNotFoundException("Schema file does not exist: " + schema);
		}
		Stopwatch stopwatch = new Stopwatch();
		RositaLogger.log(false, "Validating...");
		stopwatch.start();
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		saxFactory.setValidating(true);
		saxFactory.setFeature("http://xml.org/sax/features/validation", false);
	    saxFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
	    saxFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		saxFactory.setNamespaceAware(true);
		saxFactory.setSchema(SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(schemaFile));

		SAXParser parser = saxFactory.newSAXParser();

		XMLReader saxReader = parser.getXMLReader();
		RositaValidationErrorHandler errorHandler = new RositaValidationErrorHandler(schemaFile.getName(), xmlFile.getName(), maxErrors, cache, saveErrors, stepId);
		saxReader.setErrorHandler(errorHandler);
		try {
			StackHandler stackHandler = StackHandler.getInstance();
			stackHandler.setDataSourceId(dataSourceId);
			stackHandler.setClinic(currentClinic);
			stackHandler.setMaxClinics(maxClinics);
			stackHandler.setOutputLimit(100000L);
			saxReader.setContentHandler(stackHandler);
			
			saxReader.parse(new InputSource(new FileReader(xmlFile)));
		}
		catch (SAXException e) {
			throw e;
		}
		
		cache.saveAll();
		stopwatch.stop();
		RositaLogger.log(false, "Validation of clinic took " + stopwatch.getElapsedTimeSecs() + "s. Errors: " + errorHandler.getErrorCount() + ", warnings: " + errorHandler.getWarningCount());
		return errorHandler.getErrorCount();
	}

}
