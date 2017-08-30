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

import edu.ucdenver.rosita.etl.omop.OmopLocation;

public class OmopLocationRowMapper implements RowMapper<OmopLocation> {

	@Override
	public OmopLocation mapRow(ResultSet rs, int rowNum) throws SQLException {
		OmopLocation x = new OmopLocation();
		x.setId(rs.getLong("location_id"));
		x.setAddress1(rs.getString("address_1"));
		x.setAddress2(rs.getString("address_2"));
		x.setCity(rs.getString("city"));
		x.setState(rs.getString("state"));
		x.setZip(rs.getString("zip"));
		x.setCounty(rs.getString("county"));
		x.setLocationSourceValue(rs.getString("location_source_value"));
		x.setxZipDeidentified(rs.getString("x_zip_deidentified"));
		x.setxLocationType(rs.getString("x_location_type"));
		x.setxDataSourceType(rs.getString("x_data_source_type"));
		x.setxGridNodeId(rs.getLong("x_grid_node_id"));
		return x;
	}

}
