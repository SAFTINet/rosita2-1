package edu.ucdenver.rosita.etl.dao.omop;

import java.sql.SQLException;

import javax.sql.DataSource;

import edu.ucdenver.rosita.etl.omop.OmopXCohortMetadata;

public class OmopXCohortMetadataCache extends OmopCache<OmopXCohortMetadata> {

	public OmopXCohortMetadataCache(DataSource ds, Integer threshold,Long gridNodeId) throws SQLException {
		super(ds, threshold, "OmopXCohortMetadata", gridNodeId);
	}
 
	public void setArguments(OmopXCohortMetadata x) throws SQLException {
		lastId = x.getId();
		ps.setLong(1, x.getId());
		ps.setString(2, x.getCohortName());
		ps.setString(3, x.getCohortDescription());
		ps.setString(4,x.getCohortCreatorName());
		ps.setString(5,x.getCohortCreatorContact());
		ps.setBoolean(6, x.getIsCohortShared());
		ps.setString(7, x.getCohortQuery());
		ps.setString(8, x.getServiceUrl());
		ps.setLong(9, x.getOriginalCohortCount());
		ps.setLong(10, x.getLastCohortCount());
		ps.setDate(11, new java.sql.Date(x.getOriginalCohortDate().getTime()));
		ps.setDate(12 , new java.sql.Date(x.getLastCohortDate().getTime()));
		ps.setString(13, x.getCohortPhiNotes());
		ps.setString(14, x.getCohortOtherNotes());
		ps.setDate(15, new java.sql.Date(x.getCohortExpirationDate().getTime()));
		ps.setLong(16, gridNodeId);
	}
}
