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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;
import javax.xml.bind.JAXBException;

import org.springframework.jdbc.BadSqlGrammarException;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import edu.ucdenver.rosita.etl.VocabularyRow;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;
import edu.ucdenver.rosita.utils.VocabularyCache;

public class VocabularyParserService {

	VocabularyCache objectCache;
	DataSource ds;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	private boolean errors = false;
	
	public VocabularyParserService(DataSource ds, Integer threshold) throws JAXBException {
		objectCache = new VocabularyCache(ds, threshold);
		this.ds = ds;
	}
	
	Long lineNumber = 2L;
	
	int id = 0;
	
    public void parse(String filename) throws IOException, Exception {
    	
		List<String> truncateStatements = new SqlTemplates().getConceptMapDelete();
		
		int statements = 0;
		for (String statement : truncateStatements) {
			PreparedStatement ps = ds.getConnection().prepareStatement(statement);
			ps.execute();
			statements++;
			RositaLogger.log(true, "STATUS|||TRUNCATE|||" + String.valueOf(statements));
		}

    	CSVReader fr = null;
    	try {
	    	File file = new File(filename);
	    	if (!file.exists()) {
	    		throw new FileNotFoundException("Vocabulary file does not exist: " + filename);
	    	}
//			fr = new CSVReader(new FileReader(file));
	    	// Switched escape character from backslash to escape because the vocabulary file includes backslash characters.
			fr = new CSVReader(new FileReader(file), CSVParser.DEFAULT_SEPARATOR, CSVParser.DEFAULT_QUOTE_CHARACTER, '\034');
			
			//Skip the first line
			fr.readNext();
			
			String[] line;
			
			while ((line = fr.readNext()) != null) {
				lineNumber++;
				VocabularyRow vocabularyRow = null;
				try {
					vocabularyRow = parseVocabularyRow(line);
					objectCache.add(vocabularyRow);
				}
				catch (Exception e) {
					handleException(e);
				}
			}
			
			try {
				objectCache.saveAll();
			}
			catch (Exception e) {
				handleException(e);
			}
			
			if (errors) {
				RositaLogger.log(true, "ERROR|||Errors");
				System.exit(1);
			}
			else {
				RositaLogger.log(true, "SUCCESS|||Success");
			}
    	}
    	finally {
    		if (fr != null) {
    			fr.close();
    		}
		}		
    }
    
    private void handleException(Exception e) {
    	RositaLogger.log(false, "Could not parse row " + lineNumber + ": " + e.getClass() + " " + e.getMessage());
		RositaLogger.log(true, "ERROR|||" + e.getMessage());
		errors = true;
		if (e instanceof BadSqlGrammarException) {
			BadSqlGrammarException bsqle = (BadSqlGrammarException) e;
			Exception ex = bsqle.getSQLException().getNextException();
			if (!RositaLogger.forUi()) {
				ex.printStackTrace(System.out);
			}
			else {
				RositaLogger.log(true, "ERROR|||" + ex.getMessage());
				System.exit(1);
			}
		}
		else if (e.getMessage() != null) {
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
		}
		
    }
    
    private VocabularyRow parseVocabularyRow(String[] line) throws Exception {
    	
    	//TODO NB. This is a complete mess
    	String sourceCode = line[0];
    	if (sourceCode == null) {
    		sourceCode = "";
    	}
    	
    	int sourceVocabularyId = 0;
    	try {
    		sourceVocabularyId = Integer.parseInt(line[1]);
    	}
    	catch (Exception e) {
    		throw new Exception("Could not parse int: " + line[1] + ". Line is " + getArrayString(line));
    	}
    	
    	String sourceCodeDesc = line[2];
	    
    	int targetConceptId = 0;
    	try {
	    	targetConceptId = Integer.parseInt(line[3]);
		}
		catch (Exception e) {
			throw new Exception("Could not parse int: " + line[3] + ". Line is " + getArrayString(line));
		}
		
		int targetVocabularyId = 0;
		try {
	    	targetVocabularyId = Integer.parseInt(line[4]);
		}
		catch (Exception e) {
			throw new Exception("Could not parse int: " + line[4] + ". Line is " + getArrayString(line));
		}
		
    	String mappingType = line[5];
    	
    	String primaryMap = line[6];
    	if (primaryMap.length() == 0) {
    		primaryMap = null;
    	}
    	
    	Date validStartDate = null;
    	try {
    		validStartDate = sdf.parse(line[7]);
    	}
    	catch (Exception e) {
    		throw new Exception("Could not parse date: " + line[7] + ". Line is " + getArrayString(line));
    	}
    	
    	Date validEndDate = null;
    	try {
    		validEndDate = sdf.parse(line[8]);
    	}
    	catch (Exception e) {
    		throw new Exception("Could not parse date: " + line[8] + ". Line is " + getArrayString(line));
    	}
    	
    	String invalidReason = line[9];
    	if (invalidReason.length() == 0) {
    		invalidReason = null;
    	}
    	
    	String mapSource = line[10];
    	
    	Long dataSourceId = -1L;
    	try {
    		dataSourceId = Long.parseLong(line[11]);
    	}
    	catch (Exception e) {
    		//No data source ID, leave it at -1
    	}
    	
    	return new VocabularyRow(sourceCode, sourceVocabularyId, sourceCodeDesc, targetConceptId, targetVocabularyId, mappingType, primaryMap, validStartDate, validEndDate, invalidReason, mapSource, dataSourceId);
    }
    
    private String getArrayString(String[] line) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("[");
	    	if (line.length > 0) {
	    	sb.append("\"" + line[0] + "\"");
	    	for (int i = 1; i < line.length; i++) {
	    		sb.append(", " + "\"" + line[i] + "\"");
	    	}
    	}
    	sb.append("]");
    	return sb.toString();
    }
}
