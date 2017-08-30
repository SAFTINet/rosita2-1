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

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.types.SchemaColumn;
import edu.ucdenver.rosita.xml.MultiClinic;
import edu.ucdenver.rosita.xml.RawTableService;

public class CsvFileHandler {
	
	public static final List<String> allTables = Arrays.asList(new String[] { "organization", "care_site", "provider",
			"x_demographic", "visit_occurrence", "observation", "drug_exposure", "drug_cost", "condition_occurrence",
			"procedure_occurrence", "procedure_cost", "cohort", "death", "payer_plan_period" });

	private String suffix;
	private File[] files;
	
	public CsvFileHandler(String directoryName, String fileSuffix) {
		
        File dir = new File(directoryName);
        files = dir.listFiles();
        if (files == null) {
        	RositaLogger.error("Error retrieving files in folder: " + directoryName, RositaLogger.MSG_TYPE_ERROR);
        }
        suffix = fileSuffix;
		
	}
	
	public String verifyRequiredFiles(MultiClinic clinic, Map<String, ArrayList<SchemaColumn>> schemaLayout) throws Exception {
		
		Long totalElements = 0L;
		
		StringBuilder result = new StringBuilder();
		String basePath = ArgHandler.getArg("folder.datasources") + File.separator + clinic.getDataSourceDirectory() + File.separator;
		List<String> missingFiles = new ArrayList<String>();
		
		for (String tableName : schemaLayout.keySet()) {
			
			File foundFile = findFile(tableName);
			
			if (foundFile == null) {
				missingFiles.add(tableName);
				continue;
			}
			
			LineNumberReader lnr = new LineNumberReader(new FileReader(foundFile));
			while (lnr.readLine() != null) {} //Do nothing!
			totalElements += lnr.getLineNumber();
			System.out.println("Counted " + lnr.getLineNumber() + " rows in file: " + tableName);
			lnr.close();
		}
		
		if (missingFiles.size() > 0) {
			result.append("There was no CSV or TXT file found for the following tables mentioned in the " + clinic.getSchemaPath() + " schema: ");
			result.append(missingFiles.get(0));
			for (int i = 1; i < missingFiles.size(); i++) {
				result.append(", " + missingFiles.get(i));
			}
			result.append(". These files will be skipped");
		}
		
		System.out.println("Counted " + totalElements + " rows in data source");
		
		RawTableService rts = new RawTableService(null);
		String rows = rts.getJobProperty("rows");
		long longRows = 0;
		if (rows != "") {
			longRows = Integer.parseInt(rows);
		}
		rts.setJobProperty("rows", String.valueOf(totalElements + longRows));
		return result.toString().trim();
		
	}
	
    /**
     * Finds first file from list of files which starts with table name and suffix.
     * @param table name of table to match
     * @param files list of files
     * @param suffix file suffix to match
     * @return first file with matches; otherwise null
     */
    public File findFile(String table) {
    	
    	File result = null;
    	
    	for (File file : files) {
    		String name = file.getName().toLowerCase();
    		int pos = name.lastIndexOf(".");
    		if (pos == -1) {
    			continue;
    		}
    		
    		if (suffix != null && !name.substring(pos).equals(suffix.toLowerCase())) {
    			continue;
    		}
    		
    		if (name.startsWith(table + ".")) {
    			result = file;
    			break;
    		}
    	}
    	
    	return result;
    	
    }	
	
}