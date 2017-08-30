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

import edu.ucdenver.rosita.etl.omop.OmopPerson;

public class OmopPersonRowMapper implements RowMapper<OmopPerson> {

	@Override
	public OmopPerson mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopPerson x = new OmopPerson();
		x.setId(rs.getLong("person_id"));
		x.setGenderConceptId(rs.getLong("gender_concept_id"));
		x.setYearOfBirth(rs.getInt("year_of_birth"));
		x.setMonthOfBirth(rs.getInt("month_of_birth"));
		x.setDayOfBirth(rs.getInt("day_of_birth"));
		x.setRaceConceptId(rs.getLong("race_concept_id"));
		x.setEthnicityConceptId(rs.getLong("ethnicity_concept_id"));
		x.setLocationId(rs.getLong("location_id"));
		x.setProviderId(rs.getLong("provider_id"));
		x.setCareSiteId(rs.getLong("care_site_id"));
		x.setPersonSourceValue(rs.getString("person_source_value"));
		x.setGenderSourceValue(rs.getString("gender_source_value"));
		x.setRaceSourceValue(rs.getString("race_source_value"));
		x.setEthnicitySourceValue(rs.getString("ethnicity_source_value"));
		x.setxOrganizationId(rs.getLong("x_organization_id"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
