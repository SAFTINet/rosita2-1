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

import edu.ucdenver.rosita.etl.omop.OmopCareSite;

public class OmopCareSiteRowMapper implements RowMapper<OmopCareSite> {

	@Override
	public OmopCareSite mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopCareSite x = new OmopCareSite();
		x.setId(rs.getLong("care_site_id"));
		x.setLocationId(rs.getLong("location_id"));
		x.setOrganizationId(rs.getLong("organization_id"));
		x.setPlaceOfServiceConceptId(rs.getLong("place_of_service_concept_id"));
		x.setCareSiteSourceValue(rs.getString("care_site_source_value"));
		x.setPlaceOfServiceSourceValue(rs.getString("place_of_service_source_value"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxCareSiteName(rs.getString("x_care_site_name"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
