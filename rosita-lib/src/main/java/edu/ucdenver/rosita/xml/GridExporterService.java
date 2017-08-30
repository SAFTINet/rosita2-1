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

import com.mysql.jdbc.DatabaseMetaData;
import edu.ucdenver.rosita.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class GridExporterService  {

	private DataSource sourceDs = null;
	private DataSource targetDs = null;
	private JdbcTemplate sourceJdbc = null;
    private JdbcTemplate targetJdbc = null;
    private DatabaseMetaData dmd = null;
    private Long gridNodeId = 0L;
    private Integer threshold = 0;
	private Long objectsDone = 0L;
    private List<Map<String, Object>> rules = null;
    
    private Integer currentRuleIndex = 0;
    private Integer maxRuleIndex = 0;

	public GridExporterService(Integer threshold, DataSource sourceDs, DataSource targetDs, Long gridNodeId) {
        this.threshold = threshold;
		this.sourceDs = sourceDs;
		this.targetDs = targetDs;
		this.gridNodeId = gridNodeId;
        sourceJdbc = new JdbcTemplate(sourceDs);
        targetJdbc = new JdbcTemplate(targetDs);
	}
	
	public boolean export() throws SQLException {
		
		DifferenceStopwatch.getInstance().start();
		RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " Starting task");

		try {
            dmd = (DatabaseMetaData) targetDs.getConnection().getMetaData();
            selectETLRules();
            // Need to truncate all tables at once due to constraints.
            truncateTables();
            
            maxRuleIndex = rules.size();
            currentRuleIndex = 0;

            for (Map<String, Object> rule : rules) {
                Integer valueCount = (Integer) rule.get("value_count");
                String insert = (String) rule.get("insert_statement");
                String select = (String) rule.get("select_statement");
                String targetSchema = (String) rule.get("target_schema");
                String targetTable = (String) rule.get("target_table");

                currentRuleIndex++;
                if (doesTableExist(targetSchema, targetTable)) {
                    insert += " " + getValuesClause(valueCount);
//                    select += " limit ? offset ?";
                    GridCache cache = new GridCache(targetDs, insert, threshold, targetTable, gridNodeId, currentRuleIndex, maxRuleIndex, objectsDone);
                    transferData(cache, select, valueCount);
                }
                else {
                	RositaLogger.error("Table mentioned in rules file does not exist: " + targetSchema + "." + targetTable + " - skipped publishing to this table", RositaLogger.MSG_TYPE_WARNING);
                }

            }

			RositaLogger.log(true, "SUCCESS|||" + currentRuleIndex);
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return false;
		}
		return true;
	}

    private void selectETLRules() throws SQLException {

        String sql = "select * from cz.etl_rule where target_schema = 'saftinet' order by rule_order";
        rules = sourceJdbc.query(sql, new ColumnMapRowMapper());

    }

    private boolean doesTableExist(String tableSchema, String tableName) {

        boolean result = false;
        ResultSet rs = null;
        try {
            rs = dmd.getTables(null, tableSchema, tableName, null);
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            RositaLogger.error("Error checking for table mentioned in rules file: " + tableSchema + "." + tableName, RositaLogger.MSG_TYPE_WARNING);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e1) {
                    // do nothing
                }
            }
        }

        return result;
    }

    private String getValuesClause(int count) {

        StringBuilder clause = new StringBuilder();

        clause.append("values (");
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                clause.append(',');
            }
            clause.append("?");
        }
        clause.append(")");
//        // Add extra param for x_grid_node_id
//        clause.append("?)");

        return clause.toString();

    }

    private void truncateTables() throws SQLException {

        StringBuilder sql = new StringBuilder();
        HashSet<String> tables = new HashSet<String>();

        sql.append("SET FOREIGN_KEY_CHECKS=0; ");
        for (Map<String, Object> rule : rules) {
            String targetSchema = (String) rule.get("target_schema");
            String targetTable = (String) rule.get("target_table");

            // Skip table if we have already added it to the list of tables to truncate.
            if (tables.contains(targetSchema + "." + targetTable)) {
                continue;
            }
            tables.add(targetSchema + "." + targetTable);

            // Skip table if it does not exist on grid node.
            if (doesTableExist(targetSchema, targetTable)) {
                sql.append("truncate ").append(targetTable).append("; ");
            }
        }
        sql.append("SET FOREIGN_KEY_CHECKS=1; ");
        targetJdbc.execute(sql.toString());

    }

    private void transferData(GridCache cache, String select, int valueCount) throws SQLException {

        GridRowMapper mapper = new GridRowMapper(valueCount);
        GridRowCallbackHandler handler = new GridRowCallbackHandler(cache, valueCount);
        Object[] params = null;

        if (StringUtils.countMatches(select, "?") > 0) {
            params = new Object[] { gridNodeId };
        }

        GridStatementCreator creator = new GridStatementCreator(select, params, cache.getThreshold());
        sourceJdbc.query(creator, handler);
        objectsDone += cache.saveAndClearCache();
        RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + cache.getClassName() + " transferred, total records " + objectsDone);
        RositaLogger.log(true, "STATUS|||" + currentRuleIndex + "|||" + maxRuleIndex + "|||" + objectsDone);

    }

}
