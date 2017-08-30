package edu.ucdenver.rosita.etl.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import edu.ucdenver.rosita.etl.XCohortMetadata;


public class XCohortMetadataBatchPreparedStatementSetter  extends EtlBatchPreparedStatementSetter<XCohortMetadata> implements BatchPreparedStatementSetter {

	public XCohortMetadataBatchPreparedStatementSetter(Integer threshold) {
		super(threshold);
	}
	
	public XCohortMetadataBatchPreparedStatementSetter(Integer threshold, Long dataSourceId, Timestamp timestamp) {
		super(threshold, dataSourceId, timestamp);
	}
	
	@Override
	public void setValues(PreparedStatement ps, int i) throws SQLException {
		XCohortMetadata cm = items.get(i);
		ps.setString(1,cm.getCohortName());
		ps.setString(2, cm.getCohortDescription());
		ps.setString(3,cm.getCohortCreatorName());
		ps.setString(4,cm.getCohortCreatorContact());
		ps.setBoolean(5, cm.getIsCohortShared());
		ps.setString(6, cm.getCohortQuery());
		ps.setString(7, cm.getServiceUrl());
		ps.setString(8, cm.getOriginalCohortCount());
		ps.setString(9, cm.getLastCohortCount());
		ps.setString(10,  cm.getOriginalCohortDate() );
		ps.setString(11 ,  cm.getLastCohortDate() );
		ps.setString(12, cm.getCohortPhiNotes());
		ps.setString(13, cm.getCohortOtherNotes());
		ps.setString(14,  cm.getCohortExpirationDate());
		ps.setString(15, cm.getXgridNodeId());
	}
	
	@Override
	public int getBatchSize() {
		return items.size();
	}
}







