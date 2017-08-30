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

package edu.ucdenver.rosita.etl.dao.omop;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import edu.ucdenver.rosita.utils.DifferenceStopwatch;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class OmopCache<T> {
	
	int threshold = 5000;
	final List<T> omopItems;
	PreparedStatement ps = null;
	Long lastId = null;
	String className;
	Long gridNodeId = 0L;
	
	SqlTemplates sqlTemplates = new SqlTemplates();
	
	private DataSource dataSource;
	
	public OmopCache(DataSource ds, Integer threshold, String className, Long gridNodeId) throws SQLException {
		this.className = className;
		this.dataSource = ds;
		if (dataSource != null) {
			ps = dataSource.getConnection().prepareStatement(sqlTemplates.get(className));
		}
		if (threshold != null && threshold > 0) {
			this.threshold = threshold;
		}
		this.omopItems = new ArrayList<T>(threshold);
		this.gridNodeId = gridNodeId;
	}
	
	public void add(T o) {
		
		omopItems.add(o);
		
		//if (omopItems.size() >= this.threshold) {
		//	saveAndClearCache();
		//}
		//return lastId;
	}
	
	public Long saveAndClearCache() throws SQLException {
		RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + className + " about to custom batch update!");

		try {
			ps.executeUpdate("SET autocommit=0;");
			ps.executeUpdate("START TRANSACTION;");
			for (T i : omopItems) {
				setArguments(i);
				ps.execute();
			}
			ps.executeUpdate("COMMIT;");
	
			omopItems.clear();
			RositaLogger.log(false, DifferenceStopwatch.getInstance().getTimeAndDiff() + " " + className + " updated and cleared. Last ID is " + lastId);
			return lastId;
		}
		catch (Exception e) {
			SQLException sqle = new SQLException(this.className + " " + lastId, e);
			throw sqle;
		}
	}
	
	public String getClassName() {
		return this.className;
	}
	
	public void setArguments(T i) throws SQLException {
		//Overridden by extending classes - do nothing here
	}

}
