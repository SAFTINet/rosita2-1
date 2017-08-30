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
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import au.com.bytecode.opencsv.CSVWriter;

import edu.ucdenver.rosita.etl.ConceptMap;
import edu.ucdenver.rosita.etl.dao.rowmapper.ConceptMapRowMapper;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class ExportService {
	
	DataSource ds;
	JdbcTemplate jdbc;
	
	public ExportService(DataSource ds) {
		this.ds = ds;
		jdbc = new JdbcTemplate(ds);
	}
	
	public void export(boolean all) throws SQLException, IOException {
		String getRowsQuery = "SELECT * FROM cz.cz_concept_map;";
		if (!all) {
			getRowsQuery = "SELECT * FROM cz.cz_concept_map WHERE is_mapped='N';";
		}
		
		List<ConceptMap> items = jdbc.query(getRowsQuery, new Object[] {}, new ConceptMapRowMapper());
		
		RositaLogger.log(false, "Found " + items.size() + " rows to export");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		String lineSeparator = System.getProperty("line.separator");
		File file = new File("mappings-" + sdf.format(new Date()) + ".csv");
		CSVWriter csv = new CSVWriter(new FileWriter(file));
		String[] head = new String[] {"Target Table", "Target Column", "Source Value", "Source Vocabulary", "Source Desc"};
		csv.writeNext(head);
		
 		for (ConceptMap row : items) {
 			String[] vals = new String[] {row.getTargetTable(), row.getTargetColumn(), row.getSourceValue(), row.getSourceVocabulary(), row.getSourceDesc()};
 			csv.writeNext(vals);
 		}
 		csv.flush();
 		csv.close();
	}

}
