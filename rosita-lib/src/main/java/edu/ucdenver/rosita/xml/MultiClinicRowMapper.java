package edu.ucdenver.rosita.xml;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class MultiClinicRowMapper implements RowMapper<MultiClinic> {

	@Override
	public MultiClinic mapRow(ResultSet rs, int rowNum) throws SQLException {
		MultiClinic mc = new MultiClinic();
		mc.setDataSourceId(rs.getLong("x_data_source_id"));
		mc.setDataSourceDirectory(rs.getString("data_source_directory"));
		mc.setFileType(rs.getString("file_type"));
		mc.setSchemaPath(rs.getString("schema_path"));
		mc.setDelimiter(rs.getString("delimiter"));
		mc.setFirstRowType(rs.getString("first_row_type"));
		mc.setQuoteCharacter(rs.getString("quote_character"));
		mc.setIncremental(rs.getBoolean("incremental"));
		mc.setEtlRulesFile(rs.getString("etl_rules_file"));
		mc.setDataSourceType(rs.getString("data_source_type"));
		mc.setLinkageField(rs.getString("linkage_field"));
		mc.setLinkageType(rs.getString("linkage_type"));
		return mc;
	}

}
