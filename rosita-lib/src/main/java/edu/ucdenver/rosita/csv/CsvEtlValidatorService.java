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

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import au.com.bytecode.opencsv.CSVParser;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.ParsingService;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.ValidationErrorCache;
import edu.ucdenver.rosita.utils.types.SchemaColumn;
import edu.ucdenver.rosita.xml.MultiClinic;
import edu.ucdenver.rosita.xml.ValidationRulesService;
import edu.ucdenver.rosita.etl.ValidationError;

public class CsvEtlValidatorService {
	
	private boolean saveErrors = false;
	private String schemaName = "";
	private String directoryName = "";
	private String fileSuffix = "";
	private long maxErrors = 0l;
	private long stepId = 0l;
	private long outputThreshold = 5000l;
	private MultiClinic dataSource = null;
	private long totalLines = 0L;

	private ValidationErrorCache cache = null;
	private HashMap<String, CsvFileSpec> specs = new HashMap<String, CsvFileSpec>();
	private long errorCount = 0l;
	private long warningCount = 0l;

	
	public CsvEtlValidatorService(DataSource ds, Integer threshold, boolean saveErrors, String schemaName, String directoryName,
			String fileSuffix, Long maxErrors, Long stepId, MultiClinic dataSource) {
		
		this.cache = new ValidationErrorCache(ds, threshold, dataSource.getDataSourceId());
		this.saveErrors = saveErrors;
		this.schemaName = schemaName;
		this.directoryName = directoryName;
		this.fileSuffix = fileSuffix;
		this.maxErrors = maxErrors;
		this.stepId = stepId;
		this.dataSource = dataSource;
	}
	
	public Long validate(Integer clinic, Integer maxClinics) throws Exception {
		
        long totalLines = this.totalLines;
		try {
			RositaLogger.log(false, "Validating schema...");
			RositaLogger.log(true, "Validating schema...");
			try {
				validateSchema();
			}
			catch (Exception e) {
				RositaLogger.log(true, "ERROR|||" + e.getMessage());
				RositaLogger.error(e);
				RositaLogger.log(false, "Encountered unrecoverable error: " + e.getMessage());
				throw e;
			}
			
			CsvFileHandler handler = new CsvFileHandler(directoryName, fileSuffix);
			Map<String, ArrayList<SchemaColumn>> schema = ParsingService.getSchemaLayoutFromPath(schemaName, false);
			
	        for (String table : schema.keySet()) {
	        	File file = handler.findFile(table);
	        	
	        	if (file == null) {
	        		continue;
	        	}
	        	
	    		RositaLogger.log(false, "Validating " + file.getName() + "...");
	    		totalLines += validateFile(table, file);
	    		if (maxErrors > 0 && errorCount > maxErrors) {
	    			break;
	    		}
	    		if (totalLines > outputThreshold) {
	    			outputThreshold += 5000;
	    			RositaLogger.log(true, "STATUS|||" + totalLines + "|||" + "csv" + "|||" + errorCount + "|||" + dataSource.getDataSourceId() + "|||" + clinic + "|||" + maxClinics);
	    		}
			}
		} catch (IOException e) {
			RositaLogger.log(true, "ERROR|||" + e.getMessage());
			RositaLogger.error(e);
			RositaLogger.log(false, "Encountered unrecoverable error: " + e.getMessage());
			throw e;
		}
		finally {
			cache.saveAll();
		}
		RositaLogger.log(false, "Validation lines: " + totalLines + ", errors: " + errorCount + ", warnings: " + warningCount);
		this.totalLines = totalLines;
		
		return errorCount;
		
	}
	
	private void validateSchema() throws FileNotFoundException, IOException {
		
    	BufferedReader reader = null;
    	int lineNumber = 0;
    	boolean errorsParsingSchema = false;
    	try {
	    	File file = new File(schemaName);
	    	if (!file.exists()) {
	    		throw new FileNotFoundException("Schema file does not exist: " + schemaName);
	    	}
	    	reader = new BufferedReader(new FileReader(schemaName));
	    	
	    	CSVParser parser = new CSVParser('|', CSVParser.DEFAULT_QUOTE_CHARACTER);

			String line;
			while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line != null) {
                	line = line.trim();
                }
                try {
	                String[] columns = parser.parseLine(line);
					if (parser.isPending()) {
						throw new IOException("Unable to parse due to invalid formatting (unterminated quote character?)");
					}
	                if (columns.length > 0 && !"".equals(columns[0].trim()) && !"TABLE".equals(columns[0]))	{
	                	if (!specs.containsKey(columns[0])) {
	                		specs.put(columns[0], new CsvFileSpec(columns[0]));
	                	}
	                	CsvFileSpec spec = specs.get(columns[0]);
	                    spec.addColumn(columns);
	                }
                }
            	catch (IOException e) {
            		RositaLogger.error(schemaName + " line " + lineNumber + ": " + e.getMessage(), RositaLogger.MSG_TYPE_ERROR);
            		errorsParsingSchema = true;
            	}
			}
    	}

    	finally {
    		if (reader != null) {
    			reader.close();
    		}
		}
    	
    	//Stop here if we don't have a valid schema.
    	if (errorsParsingSchema) {
    		RositaLogger.error("Schema is not valid", RositaLogger.MSG_TYPE_ERROR);
    		System.exit(1);
    	}

	}
	
	private long validateFile(String table, File file) throws FileNotFoundException, IOException {

		BufferedReader reader = null;
		long lineNumber = 0l;
		try {	
       		CsvFileSpec spec = specs.get(table);
       		if (spec == null) {
       			throw new IOException("Unable to find file specification for type " + table);
       		}
       		spec.finalize();
       		
			char delimiter = (dataSource.getDelimiter() == null || dataSource.getDelimiter().equals("")) ? ',' : dataSource.getDelimiter().charAt(0);
			char quoteCharacter = dataSource.getQuoteCharacter() == null || dataSource.getQuoteCharacter().equals("") ? '"' : dataSource.getQuoteCharacter().charAt(0);
			
       		CSVParser parser = new CSVParser(delimiter, quoteCharacter);           	
       		String line;
			reader = new BufferedReader(new FileReader(file));

			//If we're not mapping the column names, we don't need to retrieve them - set gotColumnNames to true.
			boolean gotColumnNames = true;
			if (dataSource.getFirstRowType().equals("MAP")) { 
				gotColumnNames = false;
			}
			if (dataSource.getFirstRowType().equals("IGNORE")) {
				line = reader.readLine();
			}
            while ((line = reader.readLine()) != null) {
                lineNumber++;
            	if (line == null || line.trim().equals("")) {
            		continue;
            	}
            	String[] columns = null;
            	
            	try {
	                columns = parser.parseLine(line);
	                if (parser.isPending()) {
	                	throw new IOException("Line cannot be parsed due to invalid formatting");
	                }
            	} catch (IOException e) {
                	if (saveErrors) {
    			        errorCount++;
    			        ValidationError error = new ValidationError("Fatal", lineNumber, e.getMessage(),
                				new Date(), schemaName, directoryName, table, file.getName(), stepId, true);
                		RositaLogger.log(false, error.toString());
                		cache.add(error);
                	}
                	continue;
            	}
            	
            	//If the first row contains column names and we don't already have them, get them and form a map
            	if (!gotColumnNames) {
            		Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();
            		//For each column name in the file, map to a schema column
            		CsvFileSpec.ColumnSpec[] columnSpecs = spec.getColumns();
            		
            		for (int fc = 0; fc < columns.length; fc++) {
            			boolean columnFoundInSchema = false;
            			String fileColumnName = columns[fc].toLowerCase();
                		for (int sc = 0; sc < columnSpecs.length; sc++) {
                			CsvFileSpec.ColumnSpec columnSpec = columnSpecs[sc];
                			String schemaColumnName = columnSpec.column.toLowerCase();
                			if (fileColumnName.equals(schemaColumnName)) {
                				columnMap.put(fc, sc);
                				columnFoundInSchema = true;
                				break;
                			}
                		}
                		if (!columnFoundInSchema) {
                			boolean wasAllowed = RositaLogger.validationError("Column in " + spec.getTableName() + " does not appear in schema and will not be mapped: " + fileColumnName, CsvValidationException.CATEGORY_COLUMN_NOT_IN_SCHEMA);
                			if (!wasAllowed) { errorCount++; }
                		}
            		}
            		spec.setColumnMap(columnMap);
            		gotColumnNames = true;
            		//Continue - don't try to validate this row
            		continue;
            	}
                
                List<CsvValidationException> validationExceptions = spec.validateRow(columns);
                if (!validationExceptions.isEmpty()) {
			        for (CsvValidationException e : validationExceptions) {
			        	if (saveErrors) {
			        		boolean allowed = ValidationRulesService.isValidationErrorAllowed(e.getCategory());
			        		if (!allowed) { errorCount++; }
			        		ValidationError error = new ValidationError(e.getCategory(), lineNumber, e.getColumn() + ": " + e.getMessage(),
			        				new Date(), schemaName, directoryName, table, file.getName(), stepId, allowed);
			        		//RositaLogger.log(false, error.toString());
			        		cache.add(error);
			        	}
			        	else {
			        		errorCount++;
			        	}
			        }
			    }
                if (maxErrors > 0 && errorCount > maxErrors) {
                	ValidationError error = new ValidationError("Fatal", lineNumber, "Maximum number of errors reached",
	        				new Date(), schemaName, directoryName, table, file.getName(), stepId, false);
	        		//RositaLogger.log(false, error.toString());
	        		cache.add(error);
	        		break;
                }
            }
		} finally {
			if (reader != null) {
				reader.close();
			}
		}		

		return lineNumber;
		
	}

	public long getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(long totalLines) {
		this.totalLines = totalLines;
		this.outputThreshold = totalLines;
	}
}
