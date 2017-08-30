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
import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.utils.GridRowMapper;
import edu.ucdenver.rosita.utils.RositaLogger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class to manage importing Profile Rules from CSV file.
 */
public class ProfileRuleImportService {

    private DataSource ds = null;
    private JdbcTemplate jdbc = null;
    private Long stepId = 0L;

    /**
     * Creates this object initializing its SQL statements.
     *
     * @param ds
     * @throws java.sql.SQLException
     */
    public ProfileRuleImportService(DataSource ds, Long stepId) throws SQLException {

        this.ds = ds;
        this.jdbc = new JdbcTemplate(ds);
        this.stepId = stepId;

    }

    public void loadRules(String rulesFileName) throws IOException, SQLException {

        if (rulesFileName == null || rulesFileName.equals("")) {
            rulesFileName =  ArgHandler.getArg("file.profilerules");
            if (rulesFileName == null || rulesFileName.equals("")) {
                RositaLogger.error("No Profile rules file was specified", RositaLogger.MSG_TYPE_ERROR);
                return;
            }
        }

        File rulesFile = new File(rulesFileName);
        if (!rulesFile.exists()) {
            RositaLogger.error("Profile rules file, " + rulesFile + ", does not exist.", RositaLogger.MSG_TYPE_ERROR);
            return;
        }
        if (!rulesFile.canRead()) {
            RositaLogger.error("Profile rules file, " + rulesFile + ", is not readable.", RositaLogger.MSG_TYPE_ERROR);
            return;
        }

        deleteRules();

        BufferedReader reader = new BufferedReader(new FileReader(rulesFile));
        CSVParser parser = new CSVParser();
        String line;
        int ruleCount = 0;

        reader.readLine(); // skip header
        while ((line = reader.readLine()) != null) {
            String[] fields = parser.parseLine(line);
            if (fields.length == 0) {
                continue;
            }
            HashMap<String, String> rule = parseFields(fields);
            saveRule(rule);
            ruleCount++;
//            RositaLogger.log(true, "STATUS|||" + String.valueOf(ruleCount));
        }
        reader.close();
        RositaLogger.log(true, "SUCCESS|||" + ruleCount);

    }

    private void deleteRules() {

        jdbc.update("delete from cz.oscar_rule");

    }

    /**
     * Parses rule fields in to HashMap
     *
     * @param fields
     * @return
     */
    private HashMap<String, String> parseFields(String[] fields) {

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("X_SOURCE_SCHEMA_NAME", fields[0]);
        map.put("SOURCE_TABLE", fields[1]);
        map.put("VARIABLE_NAME", fields[2]);
        map.put("VARIABLE_VALUE", fields[3]);
        map.put("X_STATISTIC_NAME", fields[4]);
        map.put("STATISTIC_VALUE", fields[5]);
        map.put("VARIABLE_TYPE", fields[6]);
        map.put("VARIABLE_DESCRIPTION_LEVEL_1", fields[7]);
        map.put("VARIABLE_VALUE_LEVEL_1", fields[8]);
        map.put("VARIABLE_DESCRIPTION_LEVEL_2", fields[9]);
        map.put("VARIABLE_VALUE_LEVEL_2", fields[10]);
        map.put("VARIABLE_DESCRIPTION_LEVEL_3", fields[11]);
        map.put("VARIABLE_VALUE_LEVEL_3", fields[12]);
        map.put("VARIABLE_DESCRIPTION_LEVEL_4", fields[13]);
        map.put("VARIABLE_VALUE_LEVEL_4", fields[14]);
        map.put("CUSTOM_QUERY", fields[15]);
        map.put("X_DNP", fields[16]);

        return map;
    }

    private Object getValue(String field) {

        Object value = null;

        if (field != null && field.length() > 0) {
            value = field;
        }

        return value;

    }

    /**
     * Saves oscar_rule record
     *
     * @param rule
     * @throws java.sql.SQLException
     */
    private void saveRule(HashMap<String, String> rule) throws SQLException {

        jdbc.update(
                "insert into cz.oscar_rule (x_source_schema_name,source_table_name,variable_name,variable_value," +
                        "x_statistic_name,statistic_value,variable_type,variable_description_level_1," +
                        "variable_value_level_1,variable_description_level_2,variable_value_level_2," +
                        "variable_description_level_3,variable_value_level_3,variable_description_level_4," +
                        "variable_value_level_4,custom_query,x_dnp) " +
                        "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new Object[] {
                        getValue(rule.get("X_SOURCE_SCHEMA_NAME")),
                        getValue(rule.get("SOURCE_TABLE")),
                        getValue(rule.get("VARIABLE_NAME")),
                        getValue(rule.get("VARIABLE_VALUE")),
                        getValue(rule.get("X_STATISTIC_NAME")),
                        getValue(rule.get("STATISTIC_VALUE")),
                        getValue(rule.get("VARIABLE_TYPE")),
                        getValue(rule.get("VARIABLE_DESCRIPTION_LEVEL_1")),
                        getValue(rule.get("VARIABLE_VALUE_LEVEL_1")),
                        getValue(rule.get("VARIABLE_DESCRIPTION_LEVEL_2")),
                        getValue(rule.get("VARIABLE_VALUE_LEVEL_2")),
                        getValue(rule.get("VARIABLE_DESCRIPTION_LEVEL_3")),
                        getValue(rule.get("VARIABLE_VALUE_LEVEL_3")),
                        getValue(rule.get("VARIABLE_DESCRIPTION_LEVEL_4")),
                        getValue(rule.get("VARIABLE_VALUE_LEVEL_4")),
                        getValue(rule.get("CUSTOM_QUERY")),
                        getValue(rule.get("X_DNP"))
                }
        );

    }

}