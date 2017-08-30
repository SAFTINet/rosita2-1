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

package edu.ucdenver.rosita.utils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GridCache {

	private int threshold = 0;
    private String className = null;
	private final ArrayList<ArrayList<Object>> records;
	private PreparedStatement ps = null;
    private String sql = null;
	private Long recordCount = 0L;
	private Long gridNodeId = 0L;
    private int ruleIndex = 0;
    private int maxRules = 0;
    private Long objectsDone = 0L;

	SqlTemplates sqlTemplates = new SqlTemplates();

	private DataSource dataSource;

	public GridCache(DataSource ds, String sql, Integer threshold, String className, Long gridNodeId, int ruleIndex, int maxRules, Long objectsDone) throws SQLException {

		this.dataSource = ds;
        this.sql = sql;
		if (dataSource != null) {
			ps = dataSource.getConnection().prepareStatement(sql);
		}
		if (threshold != null && threshold > 0) {
			this.threshold = threshold;
		}
        this.className = className;
		this.records = new ArrayList<ArrayList<Object>>(threshold);
		this.gridNodeId = gridNodeId;
        this.ruleIndex = ruleIndex;
        this.maxRules = maxRules;
        this.objectsDone = objectsDone;
	}

    public int getThreshold() {
        return this.threshold;
    }
	public void add(ArrayList<Object> o) throws SQLException {
		
		records.add(o);
		
		if (records.size() >= this.threshold) {
            saveAndClearCache();
		}

	}
	
	public Long saveAndClearCache() throws SQLException {
		RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + className + " about to custom batch update!");

		try {
			ps.executeUpdate("SET autocommit=0;");
			ps.executeUpdate("START TRANSACTION;");
			for (ArrayList<Object> record : records) {
				setArguments(record);
				ps.execute();
			}
			ps.executeUpdate("COMMIT;");
	        recordCount += records.size();
			records.clear();
			RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + className + " updated and cleared. Record count is " + recordCount);
            RositaLogger.log(true, "STATUS|||" + ruleIndex + "|||" + maxRules + "|||" + (objectsDone + recordCount));
			return recordCount;
		}
		catch (Exception e) {
			SQLException sqle = new SQLException(this.className + " " + recordCount, e);
			throw sqle;
		}
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public void setArguments(ArrayList<Object> record) throws SQLException {

        boolean hasGridNodeIdParam = (sql.toLowerCase().contains("grid_node"));
        for (int i = 0; i < record.size(); i++) {
            Object value = record.get(i);
            // Add grid node ID as last parameter for inserts which include a grid node ID
            if (hasGridNodeIdParam && i == record.size() - 1) {
                ps.setLong(i + 1, gridNodeId);
            } else  {
                ps.setObject(i + 1, record.get(i));
            }
        }

	}

}
