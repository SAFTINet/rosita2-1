package edu.ucdenver.rosita.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucdenver.rosita.utils.types.SchemaColumn;
import edu.ucdenver.rosita.xml.RawTableService;

import au.com.bytecode.opencsv.CSVParser;

public class ParsingService {
	
	public static int NUMBER_OF_COLUMNS_IN_SCHEMA = 7;
	public static final List<String> xmlTables = Arrays.asList(new String[] { "organization", "care_site", "provider",
			"x_demographic", "visit_occurrence", "observation", "drug_exposure", "drug_cost", "condition_occurrence",
			"procedure_occurrence", "procedure_cost", "cohort", "death", "payer_plan_period" });
	
	public static boolean createTablesInSchema(String pathToSchema) throws FileNotFoundException, SQLException, Exception {
		RawTableService service = new RawTableService(null);
		Map<String, ArrayList<SchemaColumn>> schema = getSchemaLayoutFromPath(pathToSchema, false);
		service.dropTablesInSchema(schema);
		service.createTablesInSchema(schema);
		
		return true;
	}
	
	public static Map<String, ArrayList<SchemaColumn>> getSchemaLayoutFromPath(String pathToSchema, boolean checkDatabase) throws FileNotFoundException, Exception {
		Map<String, ArrayList<SchemaColumn>> tables = new HashMap<String, ArrayList<SchemaColumn>>();
		File schema = new File(pathToSchema);
		if (schema.exists() && schema.isFile() && schema.canRead()) {
			BufferedReader reader = null;
			try {
				//Attempt pipe - if wrong number of columns, attempt comma.
				char[] delimiters = {',', '|'};
				char selectedDelimiter = ',';
				for (char delimiter : delimiters) {
					CSVParser parser = new CSVParser(delimiter, CSVParser.DEFAULT_QUOTE_CHARACTER);           	
					reader = new BufferedReader(new FileReader(schema));
					String line = reader.readLine();
					String[] columns = parser.parseLine(line.trim());
					reader.close();
					if (columns.length == NUMBER_OF_COLUMNS_IN_SCHEMA) {
						selectedDelimiter = delimiter;
						break;
					}
				}
				
				CSVParser parser = new CSVParser(selectedDelimiter, CSVParser.DEFAULT_QUOTE_CHARACTER);
				String line;
	        	reader = new BufferedReader(new FileReader(schema));
				while ((line = reader.readLine()) != null) {
					String[] columns = parser.parseLine(line.trim());
					if (columns.length > 0 && columns[0] != null && !columns[0].trim().equals("") && !columns[0].trim().equals("TABLE")) {
						//"TABLE"|"COLUMN"|"TYPE"|"LENGTH"|"PRECISION"|"SCALE"|"REQUIRED"
						String tableName = columns[0];
						String columnName = columns[1];
						String type = columns[2];
						String length = columns[3];
						String precision = columns[4];
						String scale = columns[5];
						boolean required = Boolean.parseBoolean(columns[6].toLowerCase());
						
						ArrayList<SchemaColumn> table = tables.get(tableName);
						if (table == null) {
							table = new ArrayList<SchemaColumn>();
						}
						table.add(new SchemaColumn(tableName, columnName, type, length, precision, scale, required));
						tables.put(tableName, table);
					
					}
				}
				
				if (checkDatabase) {
					RawTableService service = new RawTableService(null);
					tables = service.getRawSchemaExistence(tables);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			finally {
				if (reader != null) {
					reader.close();
				}
			}
		}
		else {
			throw new FileNotFoundException("Schema file '" + schema.getPath() + "' is not readable");
		}
		
		return tables;
	}
	
	public static Map<String, ArrayList<SchemaColumn>> getXmlDefaultSchemaLayout() {
		Map<String, ArrayList<SchemaColumn>> tables = new HashMap<String, ArrayList<SchemaColumn>>();
		for (String table : xmlTables) {
			tables.put(table, null);
		}
		return tables;
	}
	
	public static String getDatabaseSafeName(String name) {
		name = name.toLowerCase();
		name = name.replace(' ', '_').replace('-','_');
		return name;
	}
	
	public static String maskData(String data) {
		String maskedData = data.replaceAll("[0-9]", "0")
				.replaceAll("[a-zA-Z]", "X")
				.replaceAll("[^0X ]", "*");
		
		return maskedData;
	}

}
