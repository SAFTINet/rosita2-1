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


/**
 * Class to manage importing ETL Rules from CSV file.
 */
package edu.ucdenver.rosita.xml;

import au.com.bytecode.opencsv.CSVParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.GridRowMapper;
import edu.ucdenver.rosita.utils.RositaLogger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Class to manage importing ETL Rules from CSV file.
 */
public class EtlRuleImportService {

    private DataSource ds = null;
    private JdbcTemplate jdbc = null;
    private Long stepId = 0L;
    private int maxRuleOrder = 0;

    /**
     * Creates this object initializing its SQL statements.
     *
     * @param ds
     * @throws SQLException
     */
    public EtlRuleImportService(DataSource ds, Long stepId) throws SQLException {

        this.ds = ds;
        this.jdbc = new JdbcTemplate(ds);
        this.stepId = stepId;

    }

    /**
     * Loads STD ETL Rules, Tables, and Maps from CSV file.
     *
     * @throws IOException
     * @throws SQLException
     */
    public void loadRules(MultiClinic c, String etlRuleType) throws IOException, SQLException {

    	String rulesFileName = c.getEtlRulesFile();
    	if (rulesFileName == null || rulesFileName.equals("")) {
            RositaLogger.error("No ETL rules file was specified for data source " + c.getDataSourceDirectory(), RositaLogger.MSG_TYPE_ERROR);
            return;
    	}
        String rulesPath =  ArgHandler.getArg("folder.etlrules");
        if (rulesPath == null || rulesPath.equals("")) {
            RositaLogger.error("No ETL rules path was specified", RositaLogger.MSG_TYPE_ERROR);
            return;
        }
        if (!rulesPath.endsWith(File.separator)) {
            rulesPath += File.separator;
        }

        File rulesFile = new File(rulesPath + rulesFileName);
        if (!rulesFile.exists()) {
            RositaLogger.error("ETL rules file, " + rulesFile + ", does not exist.", RositaLogger.MSG_TYPE_ERROR);
            return;
        }
        if (!rulesFile.canRead()) {
            RositaLogger.error("ETL rules file, " + rulesFile + ", is not readable.", RositaLogger.MSG_TYPE_ERROR);
            return;
        }

        deleteETLRules(c.getDataSourceId(), etlRuleType);

        BufferedReader reader = new BufferedReader(new FileReader(rulesFile));
        CSVParser parser = new CSVParser();
        String line;
        String ruleOrder = "";
        long etlRuleId = -1;
        ArrayList<HashMap<String, String>> tables = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        //Newly added for custom rules
        ArrayList<HashMap<String, String>> customs = new ArrayList<HashMap<String, String>>();
        //a flag to identify custom rule
        boolean isCustomRule = false;
        
        reader.readLine(); // skip header
        while ((line = reader.readLine()) != null) {
            String[] fields = parser.parseLine(line);
            if (fields.length == 0) {
                continue;
            }
            HashMap<String, String> rule = parseFields(fields);
            rule.put("DATA_SOURCE_ID", String.valueOf(c.getDataSourceId()));
            
            
            // If RULE_ORDER is new number, then save existing tables and maps and create new rule.
            if (!ruleOrder.equals(rule.get("RULE_ORDER"))) {
            	 
	                
	                saveIndexes(tables);
	                replaceTableNames(tables, maps);
	                if (tables.size() > 0) {
	                    saveTables(tables);
	                    tables = new ArrayList<HashMap<String, String>>();
	                }
	                if (maps.size() > 0) {
	                    saveMaps(maps);
	                    maps = new ArrayList<HashMap<String, String>>();
	                }
	                if(customs.size()>0){
            			saveCustomRules(customs);
            			customs = new ArrayList<HashMap<String, String>>();
            		}
	            if(!rule.get("MAP_TYPE").equals("CUSTOM")){
	              isCustomRule = false;
//                deleteETLRule(findRuleByDescription(rule.get("RULE_DESCRIPTION")));
	              etlRuleId = saveRule(rule, etlRuleType);
	                //Check if a rule is custom rule
	               
            	}else{
            		isCustomRule = true;
            	}
            	
            	ruleOrder = rule.get("RULE_ORDER");
            }
            rule.put("ETL_RULE_ID", Long.toString(etlRuleId));

            // If this is a VALUE record, then add it to maps
            if ("CUSTOM".equals(rule.get("MAP_TYPE"))){
            	customs.add(rule);
            }
            else if ("VALUE".equals(rule.get("MAP_TYPE"))) {
                maps.add(rule);
                // Otherwise, add it to tables.
            } else {
                tables.add(rule);
            }
        }

        // If there are unsaved tables/maps, then replace table names in source_value fields with aliases and then save
        // them.
        
        if(isCustomRule == false){
	        saveIndexes(tables);
	        replaceTableNames(tables, maps);
	        if (tables.size() > 0) {
	            saveTables(tables);
	        }
	        if (maps.size() > 0) {
	            saveMaps(maps);
	        }
        }else{
        	if(customs.size()>0){
        		saveCustomRules(customs);
        	}
        }
        reader.close();

    }

    /**
     * Loads Publish Rules, Tables, and Maps from CSV file.
     *

     * @throws IOException
     * @throws SQLException
     */
    public void loadRules(String rulesFileName, String etlRuleType) throws IOException, SQLException {

        if (rulesFileName == null || rulesFileName.equals("")) {
            rulesFileName =  ArgHandler.getArg("file.publishrules");
            if (rulesFileName == null || rulesFileName.equals("")) {
                RositaLogger.error("No publish rules file was specified", RositaLogger.MSG_TYPE_ERROR);
                return;
            }
        }

        File rulesFile = new File(rulesFileName);
        if (!rulesFile.exists()) {
            RositaLogger.error("Publish rules file, " + rulesFile + ", does not exist.", RositaLogger.MSG_TYPE_ERROR);
            return;
        }
        if (!rulesFile.canRead()) {
            RositaLogger.error("Publish rules file, " + rulesFile + ", is not readable.", RositaLogger.MSG_TYPE_ERROR);
            return;
        }

        deleteETLRules(-1L, etlRuleType);

        BufferedReader reader = new BufferedReader(new FileReader(rulesFile));
        CSVParser parser = new CSVParser();
        String line;
        String ruleOrder = "";
        long etlRuleId = -1;
        ArrayList<HashMap<String, String>> tables = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        int ruleCount = 0;

        reader.readLine(); // skip header
        while ((line = reader.readLine()) != null) {
            String[] fields = parser.parseLine(line);
            if (fields.length == 0) {
                continue;
            }
            HashMap<String, String> rule = parseFields(fields);
            rule.put("DATA_SOURCE_ID", String.valueOf(-1));

            // If RULE_ORDER is new number, then save existing tables and maps and create new rule.
            if (!ruleOrder.equals(rule.get("RULE_ORDER"))) {
                replaceTableNames(tables, maps);
                if (tables.size() > 0) {
                    saveTables(tables);
                    tables = new ArrayList<HashMap<String, String>>();
                }
                if (maps.size() > 0) {
                    saveMaps(maps);
                    maps = new ArrayList<HashMap<String, String>>();
                }

//                deleteETLRule(findRuleByDescription(rule.get("RULE_DESCRIPTION")));
                etlRuleId = saveRule(rule, etlRuleType);
                ruleOrder = rule.get("RULE_ORDER");
                ruleCount++;
//                RositaLogger.log(true, "STATUS|||" + String.valueOf(ruleCount));
            }
            rule.put("ETL_RULE_ID", Long.toString(etlRuleId));

            // If this is a VALUE record, then add it to maps
            if ("VALUE".equals(rule.get("MAP_TYPE"))) {
                maps.add(rule);
                // Otherwise, add it to tables.
            } else {
                tables.add(rule);
            }
        }

        // If there are unsaved tables/maps, then replace table names in source_value fields with aliases and then save
        // them.
        replaceTableNames(tables, maps);
        if (tables.size() > 0) {
            saveTables(tables);
        }
        if (maps.size() > 0) {
            saveMaps(maps);
        }
        reader.close();
        RositaLogger.log(true, "SUCCESS|||" + ruleCount);

    }

    /**
     * Execute cz.czx_etl_rule_update function to update insert and select statements in cz.etl_rule table.
     *
     * @throws SQLException
     */
    public void updateRules() throws SQLException {

        GridRowMapper mapper = new GridRowMapper(1);
        List<ArrayList<Object>> records = jdbc.query("select cz.czx_etl_rule_update(?)", new Object[]{stepId}, mapper);

        Long result = 0L;
        if (records.size() > 0) {
            ArrayList<Object> record = records.get(0);
            result = (Long) record.get(0);
        }

        // TODO: handle result

    }

    public void deleteETLRules(Long dataSourceId, String etlRuleType) {

    	if (dataSourceId > -1) {
	        jdbc.update(
	                "delete from cz.etl_map where etl_rule_id in (select etl_rule_id from cz.etl_rule where x_data_source_id = ? and rule_type = ?)",
	                new Object[] { dataSourceId, etlRuleType });
	        jdbc.update(
	                "delete from cz.etl_table where etl_rule_id in (select etl_rule_id from cz.etl_rule where x_data_source_id = ? and rule_type = ?)",
	                new Object[] { dataSourceId, etlRuleType });
	        jdbc.update(
	                "delete from cz.etl_rule where x_data_source_id = ? and rule_type = ?",
	                new Object[] { dataSourceId, etlRuleType });
	        //Delete custom rule table
	        jdbc.update("delete from cz.etl_custom_rules");
	    	}
    	else {
            jdbc.update(
                    "delete from cz.etl_map where etl_rule_id in (select etl_rule_id from cz.etl_rule where rule_type = ?)",
                    new Object[] { etlRuleType });
            jdbc.update(
                    "delete from cz.etl_table where etl_rule_id in (select etl_rule_id from cz.etl_rule where rule_type = ?)",
                    new Object[] { etlRuleType });
            jdbc.update(
                    "delete from cz.etl_rule where rule_type = ?",
                    new Object[] { etlRuleType });
        }    	
    }

    public void deleteIndexes(String ruleType) {

        String schemaName;

        if ("STD".equals(ruleType)) {
            schemaName = "RAW";
        } else {
            schemaName = "SAFTINET";
        }
        Object[] params = new Object[] {
                schemaName
        };
        String sql = "DELETE FROM cz.table_index WHERE upper(schema_name) = ?";
        jdbc.update(sql, params);

    }

    /**
     * Parses rule fields in to HashMap
     *
     * @param fields
     * @return
     */
    private HashMap<String, String> parseFields(String[] fields) {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("RULE_ORDER", fields[0]);
        map.put("RULE_DESCRIPTION", fields[1]);
        map.put("TARGET_SCHEMA", fields[2]);
        map.put("TARGET_TABLE", fields[3]);
        map.put("TARGET_COLUMN", fields[4]);
        map.put("MAP_TYPE", fields[5]);
        map.put("MAP_ORDER", fields[6]);
        map.put("SOURCE_SCHEMA", fields[7]);
        map.put("SOURCE_TABLE", fields[8]);
        map.put("SOURCE_VALUE", fields[9]);

        return map;
    }

    /**
     * Gets maximum rule_order value from database.
     *
     * @return maximum rule_order; 0, if no rules exist.
     * @throws SQLException
     */
    private int setMaxRuleOrder() throws SQLException {

        int maxRuleOrder = 0;
        GridRowMapper mapper = new GridRowMapper(1);
        List<ArrayList<Object>> records = jdbc.query("select max(rule_order) from cz.etl_rule", mapper);

        if (records.size() > 0) {
            ArrayList<Object> record = records.get(0);
            maxRuleOrder = (Integer) record.get(0);
        }

        return maxRuleOrder;

    }

    /**
     * Finds etl_rule_id of rule which matches specified description.
     *
     * @param description
     * @return etl_rule_id of first record which matches description; -1, otherwise
     * @throws SQLException
     */
    private long findRuleByDescription(String description) throws SQLException {

        long etlRuleId = -1;
        GridRowMapper mapper = new GridRowMapper(1);
        List<ArrayList<Object>> records = jdbc.query(
                "select etl_rule_id from cz.etl_rule where upper(rule_description) = upper(?)",
                new Object[] { description }, mapper);

        if (records.size() > 0) {
            ArrayList<Object> record = records.get(0);
            etlRuleId = (Long) record.get(0);
        }

        return etlRuleId;

    }

    /**
     * Deletes ETL rule/table/map with specified etl_rule_id.
     *
     * @param etlRuleId
     * @throws SQLException
     */
    private void deleteETLRule(long etlRuleId) throws SQLException {

        jdbc.update("delete from cz.etl_rule where etl_rule_id = ?", new Object[] { etlRuleId });
        jdbc.update("delete from cz.etl_table where etl_rule_id = ?", new Object[] { etlRuleId });
        jdbc.update("delete from cz.etl_rule where etl_rule_id = ?", new Object[] { etlRuleId });

    }

    /**
     * Saves etl_rule record returning its etl_rule_id
     *
     * @param rule
     * @return
     * @throws SQLException
     */
    private long saveRule(HashMap<String, String> rule, String etlRuleType) throws SQLException {

        long etlRuleId = -1;
        long dataSourceId = 0;
        try {
        	dataSourceId = Integer.parseInt(rule.get("DATA_SOURCE_ID"));
        }
        catch (Exception e) {
        	RositaLogger.error("Error parsing data source ID: " + rule.get("DATA_SOURCE_ID"), RositaLogger.MSG_TYPE_WARNING);
        }

        GridRowMapper mapper = new GridRowMapper(1);
        List<ArrayList<Object>> records = jdbc.query(
                "insert into cz.etl_rule (rule_type, rule_order, rule_description, target_schema, target_table, x_data_source_id) values (?, ?, ?, ?, ?, ?) returning etl_rule_id",
                new Object[] {
                        etlRuleType,
                        Integer.parseInt(rule.get("RULE_ORDER")) + maxRuleOrder,
                        rule.get("RULE_DESCRIPTION"),
                        rule.get("TARGET_SCHEMA"),
                        rule.get("TARGET_TABLE"),
                        dataSourceId
                },
                mapper
        );

        if (records.size() > 0) {
            ArrayList<Object> record = records.get(0);
            etlRuleId = (Long) record.get(0);
        }

        return etlRuleId;

    }

    /**
     * Saves etl_table records
     *
     * @param tables
     * @throws SQLException
     */
    private void saveTables(ArrayList<HashMap<String, String>> tables) throws SQLException {

        for (HashMap<String, String> table : tables) {
            jdbc.update(
                    "insert into cz.etl_table (etl_rule_id, table_type, table_order, source_schema, source_table, source_value) values (?, ?, ?, ?, ?, ?)",
                    new Object[] {
                            Long.parseLong(table.get("ETL_RULE_ID")),
                            table.get("MAP_TYPE"),
                            Integer.parseInt(table.get("MAP_ORDER")),
                            table.get("SOURCE_SCHEMA"),
                            table.get("SOURCE_TABLE"),
                            table.get("SOURCE_VALUE")
                    });
        }

    }
    
    /**
     * Saves etl_custom_rule records
     *
     * @param customs
     * @throws SQLException
     */
    private void saveCustomRules(ArrayList<HashMap<String, String>> customs) throws SQLException {
    	
    	for (HashMap<String, String> custom : customs) {
    		long dataSourceId = 0;
            try {
            	dataSourceId = Integer.parseInt(custom.get("DATA_SOURCE_ID"));
            }
            catch (Exception e) {
            	RositaLogger.error("Error parsing data source ID: " + custom.get("DATA_SOURCE_ID"), RositaLogger.MSG_TYPE_WARNING);
            }
            jdbc.update(
                    "insert into cz.etl_custom_rules (rule_description, x_data_source_id, select_statement) values (?, ?, ?)",
                    new Object[] {
                    		custom.get("RULE_DESCRIPTION"),
                    		dataSourceId,
                    		custom.get("SOURCE_VALUE")
                    });
        }

    }

    /**
     * Saves etl_map records
     *
     * @param maps
     * @throws SQLException
     */
    private void saveMaps(ArrayList<HashMap<String, String>> maps) throws SQLException {

        for (HashMap<String, String> map : maps) {
            jdbc.update(
                    "insert into cz.etl_map (etl_rule_id, map_order, target_column, source_value) values (?, ?, ?, ?)",
                    new Object[] {
                            Long.parseLong(map.get("ETL_RULE_ID")),
                            Integer.parseInt(map.get("MAP_ORDER")),
                            map.get("TARGET_COLUMN"),
                            map.get("SOURCE_VALUE")
                    });
        }

    }

    /**
     * Replaces table names in source_value field with alias.
     *
     * @param tables
     * @param maps
     */
    private void replaceTableNames(ArrayList<HashMap<String, String>> tables, ArrayList<HashMap<String, String>> maps) {

        int i = 0;
        for (HashMap<String, String> alias : tables) {
            i++;
            String sourceTable = alias.get("SOURCE_TABLE");
            if (sourceTable != null && sourceTable.length() > 0)   {
                for (HashMap<String, String> table : tables) {
                    String sourceValue = table.get("SOURCE_VALUE");
                    sourceValue = sourceValue.replaceAll(sourceTable + "\\.", "@" + i + ".");
                    table.put("SOURCE_VALUE", sourceValue);
                }

                for (HashMap<String, String> map : maps) {
                    String sourceValue = map.get("SOURCE_VALUE");
                    sourceValue = sourceValue.replaceAll(sourceTable + "\\.", "@" + i + ".");
                    map.put("SOURCE_VALUE", sourceValue);
                }
            }
        }

    }

    // Determines unique
    private void saveIndexes(ArrayList<HashMap<String, String>> tables) {

        // PRIMARY  raw     vt_medical_claimd   vt_medical_claims	vt_medical_claims.billing_provider_id,vt_provider.provider_type_code,vt_provider.provider_organization_type
        // JOIN     raw     vt_provider         vt_provider         vt_medical_claims.billing_provider = vt_provider.provider_id
        // WHERE                                                   vt_provider.provider_organization_type in ('1', '2')
        //
        // vt_medical_claims    billing_provider_id
        // vt_provider          provider_type_code
        // vt_provider          provider_organization_type
        //
        // vt_medical_claims    billing_provider_id
        // vt_provider          provider_id
        //
        // vt_provider          provider_organization_type


        // Get list of tables in RAW schema from tables
        HashSet<String> tablesInSchema = new HashSet<String>();
        for (HashMap<String, String> table : tables) {
            String sourceSchema = table.get("SOURCE_SCHEMA");
            String sourceTable = table.get("SOURCE_TABLE");
            if (sourceTable != null && sourceTable.length() > 0
                    && sourceSchema != null && sourceSchema.toUpperCase().equals("RAW")) {
                tablesInSchema.add(sourceTable);
            }
        }

        // Get list of indexable values from source vales for tables
        HashSet<String> indexes = new HashSet<String>();
        for (HashMap<String, String> table : tables) {
            String sourceValue = table.get("SOURCE_VALUE");
            ArrayList<String> values = parseSourceValue(sourceValue);
            for (String value : values) {
                String[] components = value.split("\\.");
                String tableName = components[0];
                if (tablesInSchema.contains(tableName)) {
                    indexes.add(value);
                }
            }
        }

        // Add default indexes to indexable tables for x_data_source_id and x_etl_date
        for (String table : tablesInSchema) {
            indexes.add(table + "." + "x_data_source_id");
            indexes.add(table + "." + "x_etl_date");
        }

        // Add index records to table_index for tables/columns which where indexable
        for (String index : indexes) {
            String[] components = index.split("\\.");
            String schemaName = "raw";
            String tableName = components[0];
            String columnName = components[1];
            String indexName = tableName + "_" + columnName + "_idx";

            Object[] params = new Object[] { schemaName, tableName, indexName };
            String sql = "SELECT count(*) FROM cz.table_index WHERE schema_name = ? AND table_name = ? AND index_name = ?";
            int count = jdbc.queryForInt(sql, params);

            if (count == 0) {
                StringBuilder indexSql = new StringBuilder();
                indexSql.append("CREATE INDEX ").append(indexName)
                        .append(" ON ").append(schemaName).append(".").append(tableName)
                        .append(" (").append(columnName).append(")")
                        .append(" TABLESPACE rosita_indx");
                params = new Object[] { schemaName, tableName, indexName, indexSql };
                sql = "INSERT INTO cz.table_index (schema_name, table_name, index_name, index_sql) values (?,?,?,?)";
                jdbc.update(sql, params);
            }
        }

    }

    /**
     * Parses table name "." column name pairs from table source value.
     * @param s
     * @return
     */
    private ArrayList<String> parseSourceValue(String s) {

        ArrayList<String> values = new ArrayList<String>();
        Pattern p = Pattern.compile("([a-z][a-z0-9_]*\\.[a-z][a-z0-9_]*)[^a-z0-9_]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(s);
        while (m.find()) {
            if (m.groupCount() > 0) {
                String value = m.group(1);
                values.add(value);
            }
        }

        return values;
    }

}