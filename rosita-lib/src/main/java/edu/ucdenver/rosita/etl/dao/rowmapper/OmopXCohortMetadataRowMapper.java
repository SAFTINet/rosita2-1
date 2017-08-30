package edu.ucdenver.rosita.etl.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.ucdenver.rosita.etl.omop.OmopXCohortMetadata;

public class OmopXCohortMetadataRowMapper implements RowMapper<OmopXCohortMetadata> {

	@Override
	public OmopXCohortMetadata mapRow(ResultSet rs, int rowNum)throws SQLException {
		OmopXCohortMetadata cmd = new OmopXCohortMetadata();
		cmd.setId(rs.getLong("cohort_metadata_id"));
		cmd.setCohortName(rs.getString("cohort_name"));
		cmd.setCohortDescription(rs.getString("cohort_description"));
		cmd.setCohortCreatorName(rs.getString("cohort_creator_name"));
		cmd.setCohortCreatorContact(rs.getString("cohort_creator_contact"));
		cmd.setIsCohortShared(rs.getBoolean("is_cohort_shared"));
		cmd.setCohortQuery(rs.getString("cohort_query"));
		cmd.setServiceUrl(rs.getString("service_url"));
		cmd.setOriginalCohortCount(rs.getLong("original_cohort_count"));
		cmd.setLastCohortCount(rs.getLong("last_cohort_count"));
		cmd.setOriginalCohortDate(rs.getDate("original_cohort_date"));
		cmd.setLastCohortDate(rs.getDate("last_cohort_date"));
		cmd.setCohortPhiNotes(rs.getString("cohort_phi_notes"));
		cmd.setCohortOtherNotes(rs.getString("cohort_other_notes"));
		cmd.setCohortExpirationDate(rs.getDate("cohort_expiration_date"));
		cmd.setXgridNodeId(rs.getLong("grid_node_id"));
		return cmd;
	}
	 
}
