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

package edu.ucdenver.rosita.etl.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.ucdenver.rosita.etl.omop.OmopObservationPeriod;

public class OmopObservationPeriodRowMapper implements RowMapper<OmopObservationPeriod> {

	@Override
	public OmopObservationPeriod mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopObservationPeriod x = new OmopObservationPeriod();
		x.setId(rs.getLong("observation_period_id"));
		x.setPersonId(rs.getLong("person_id"));
		x.setObservationPeriodStartDate(rs.getDate("observation_period_start_date"));
		x.setObservationPeriodEndDate(rs.getDate("observation_period_end_date"));
		return x;
	}

}
