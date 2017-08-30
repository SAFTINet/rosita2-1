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
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import au.com.bytecode.opencsv.CSVParser;
import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.etl.DelimitedColumn;
import edu.ucdenver.rosita.etl.DelimitedObject;
import edu.ucdenver.rosita.utils.DelimitedCache;
import edu.ucdenver.rosita.utils.ParsingService;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.types.SchemaColumn;
import edu.ucdenver.rosita.xml.MultiClinic;
import edu.ucdenver.rosita.xml.VaccuumService;

public class DelimitedEtlParserService {

	Long id = 0l;
	Long elements;
	DelimitedCache objectCache;
	Long dataSourceId = 1L;
	Long outputThreshold = 10000L;
	Long totalLines = 0L;
	
	private Map<Integer, Integer> columnMap = new HashMap<Integer, Integer>();
	private Map<Integer, Integer> fileToSchemaMap = new HashMap<Integer, Integer>();
	private Map<String, ArrayList<SchemaColumn>> schema;
	private ArrayList<SchemaColumn> currentTableColumnList;
	
	public DelimitedEtlParserService(DataSource ds, Integer threshold, Long dataSourceId) {
		objectCache = new DelimitedCache(ds, threshold, dataSourceId);
		this.dataSourceId = dataSourceId;
	}
	
	/**
	 * Parses files in specified directory
	 * @param directoryName
	 * @param suffix
	 * @throws Exception 
	 */
   public Long parse(MultiClinic clinic, Integer clinicNum, Integer maxClinics) throws Exception {
	   
	    //Get schema and list of files to load
	    schema = ParsingService.getSchemaLayoutFromPath(ArgHandler.getArg("folder.schemas") + clinic.getSchemaPath(), false);
	    CsvFileHandler handler = new CsvFileHandler(ArgHandler.getArg("folder.datasources") + File.separatorChar + clinic.getDataSourceDirectory(), null);
	    
	    //Ensure CsvFileSpec is initialized
	    new CsvFileSpec("");

        for (String table : schema.keySet()) {
        	// Find first file that starts with table name
        	File file = handler.findFile(table);
        	if (file == null) {
        		continue;
        	}
        	
        	Character delimiter = clinic.getDelimiter() == null || clinic.getDelimiter().equals("") ? ',' : clinic.getDelimiter().charAt(0);
        	Character quoteCharacter = clinic.getQuoteCharacter() == null || clinic.getQuoteCharacter().equals("") ? '"' : clinic.getQuoteCharacter().charAt(0);
        	
    		RositaLogger.log(false, "Parsing " + file.getName());
       		CSVParser parser = new CSVParser(delimiter, quoteCharacter);
    		BufferedReader reader = null;
    		reader = new BufferedReader(new FileReader(file));
    		
    		columnMap = new HashMap<Integer, Integer>();
    		fileToSchemaMap = new HashMap<Integer, Integer>();
    		currentTableColumnList = schema.get(table);
    		List<String> columnNames = null;
       		
			String line;
			//Build up our column map if we're mapping - if not, assume in order in the CSV specification
			if (clinic.getFirstRowType().equals("MAP")) {
				line = reader.readLine();
				columnNames = Arrays.asList(parser.parseLine(line.trim()));
			}
			else if (clinic.getFirstRowType().equals("IGNORE")) {
				line = reader.readLine(); //Skip over first row
			}
			
			Integer defaultIndex = 0;
			Integer schemaIndex = 0;
			for (SchemaColumn column : schema.get(table)) {
				Integer index = -1;
				if (columnNames != null) {
					index = columnNames.indexOf(column.getName());
					if (index == -1) {
						System.out.println("Found no index for column: " + column.getName() + " in table: " + table);
					}
				}
				else {
					index = defaultIndex++; //Assign then increment!
				}

				columnMap.put(schemaIndex, index);
				fileToSchemaMap.put(index, schemaIndex);
				schemaIndex++;
			}
        	
        	objectCache.setupCache(table, getColumnData(schema.get(table)), columnMap);

        	try {
 				
				id = 0l; // reset ID for each file
				while ((line = reader.readLine()) != null) {
					id++;
					totalLines++;
					if (totalLines > outputThreshold) {
						outputThreshold += 10000;
						RositaLogger.log(true, "STATUS|||LOAD|||" + totalLines + "|||" + dataSourceId + "|||" + clinicNum + "|||" + maxClinics);
					}
					
					//If this line is blank, skip loading it
					if (line.trim().equals("")) {
						continue;
					}

					String[] columns = parser.parseLine(line);
					if (parser.isPending()) {
						throw new IOException("Line " + id + " of " + file.getName() + " cannot be parsed due to invalid formatting");
					}
					else {
						if (columns.length < fileToSchemaMap.size()) {
							RositaLogger.error("Line " + id + " of " + table + " has too few columns! " + fileToSchemaMap.size() + " expected, " + columns.length + " found", RositaLogger.MSG_TYPE_WARNING);
						}
						DelimitedObject obj = parseDelimitedObject(columns);
						objectCache.add(obj);
					}			
				}
			}
			finally {
				if (reader != null) {
					reader.close();
				}
			}
        	//Save all from the object cache before changing files
        	objectCache.saveAll();
		}
		
		return totalLines;
    }

	private DelimitedObject parseDelimitedObject(String[] row) {
		DelimitedObject d = new DelimitedObject();
		for (int i = 0; i < row.length; i++) {
			String data = row[i];
			//Here, adapt the data in any way necessary - truncate if overflow.
			//TODO Take this and the validation methods out of CsvFileSpec and unify them
			
			Integer columnInSchema = fileToSchemaMap.get(i);
			if (columnInSchema == null) {
				d.add(null);
				continue;
			}
			SchemaColumn currentColumn = currentTableColumnList.get(columnInSchema);
			if (currentColumn.getType().equals("decimal")) {
				try {
					Double.parseDouble(data);
				}
				catch (Exception e) {
					if (ArgHandler.getBoolean("adjustbadvalues") && data != null && data != "") {
						data = "0";
					}
				}
			}
			else if (currentColumn.getType().equals("integer")) {
				try {
					Double dd = Double.parseDouble(data);
					data = String.valueOf(Math.round(dd));
				}
				catch(Exception e) {
					if (ArgHandler.getBoolean("adjustbadvalues") && data != null && data != "") {
						data = "0";
					}
				}
			}
			else if (currentColumn.getType().equals("text")) {
				int maxLength = CsvFileSpec.TEXT_LENGTH;
				if (data.length() > maxLength) {
					data = data.substring(0, maxLength);
				}
			}
			else if (currentColumn.getType().equals("timestamp")) {
		        try {
		            if (data.length() > CsvFileSpec.TIMESTAMP_LENGTH) {
		            	data = data.substring(0, CsvFileSpec.TIMESTAMP_LENGTH);
		            }
		            CsvFileSpec.datetimeFormat.parse(data);
		        } catch (ParseException e) {
					if (ArgHandler.getBoolean("adjustbadvalues") && data != null && data != "") {
						data = "1800-01-01T00:00";
					}
		        }
			}
			else if (currentColumn.getType().equals("date")) {
		        try {
		            if (data.length() > CsvFileSpec.DATE_LENGTH) {
		            	data = data.substring(0, CsvFileSpec.DATE_LENGTH);
		            }
		            CsvFileSpec.dateFormat.parse(data);
		        } catch (ParseException e) {
		        	if (ArgHandler.getBoolean("adjustbadvalues") && data != null && data != "") {
		        		data = "1800-01-01";
		        	}
		        }
			}
			else {
				//Assume varchar
				int maxLength = 0;
				try {
					maxLength = Integer.parseInt(currentColumn.getLength());
					if (maxLength == 0) { maxLength = 500; }
				}
				catch (Exception e) {
					maxLength = 500;
				}
				if (data.length() > maxLength) {
					data = data.substring(0, maxLength);
				}
			}
			d.add(data);
		}
		return d;
	}

	public Long getTotalLines() {
		return totalLines;
	}

	public void setTotalLines(Long totalLines) {
		this.totalLines = totalLines;
		this.outputThreshold = totalLines;
	}
	
	private List<DelimitedColumn> getColumnData(ArrayList<SchemaColumn> schemaColumns) {
		List<DelimitedColumn> columnData = new ArrayList<DelimitedColumn>();
		for (SchemaColumn column : schemaColumns) {
			columnData.add(new DelimitedColumn(column.getName(), column.getType()));
		}
		return columnData;
	}
}
