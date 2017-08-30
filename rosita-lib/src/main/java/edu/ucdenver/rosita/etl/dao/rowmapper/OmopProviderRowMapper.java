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

import edu.ucdenver.rosita.etl.omop.OmopProvider;

public class OmopProviderRowMapper implements RowMapper<OmopProvider> {

	@Override
	public OmopProvider mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopProvider x = new OmopProvider();
		x.setId(rs.getLong("provider_id"));
		x.setNpi(rs.getString("npi"));
		x.setDea(rs.getString("dea"));
		x.setSpecialtyConceptId(rs.getLong("specialty_concept_id"));
		x.setCareSiteId(rs.getLong("care_site_id"));
		x.setProviderSourceValue(rs.getString("provider_source_value"));
		x.setSpecialtySourceValue(rs.getString("specialty_source_value"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxProviderFirst(rs.getString("x_provider_first"));
		x.setxProviderMiddle(rs.getString("x_provider_middle"));
		x.setxProviderLast(rs.getString("x_provider_last"));
		x.setxOrganizationId(rs.getLong("x_organization_id"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
