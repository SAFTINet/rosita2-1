package edu.ucdenver.rosita.xml;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import edu.ucdenver.rosita.ArgHandler;
import edu.ucdenver.rosita.etl.dao.rowmapper.OmopCohortRowMapper;
import edu.ucdenver.rosita.etl.dao.rowmapper.OmopXCohortMetadataRowMapper;
import edu.ucdenver.rosita.etl.omop.OmopCohort;
import edu.ucdenver.rosita.etl.omop.OmopXCohortMetadata;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SqlTemplates;

public class CohortImporterService    {

	private static String database ;
	private static String gridnodeId;
	
	private static String host ;
	private static String port ;
	private static String user ;
	private static String password ;
	
	public DataSource ds ;
	public static DataSource gridnodeDs = null;
	
	static JdbcTemplate jdbc = null;
	static Long gridNodeId = 0L;
	static SqlTemplates sqlTemplates = new SqlTemplates();

	public CohortImporterService(Integer threshold, DataSource ds) {
		this.ds = ds;
	}
	
	
	public boolean importCohortdata()
	{
		boolean returnVal = false;
		try
		{
			RositaLogger.initialize(true, ds);
			getGridnodeDatabaseConnection();
			List<OmopXCohortMetadata> cohortMetadataList = getCohortMetadataList();
			List<OmopCohort> cohortList = getCohortList();
			deleteOmopTablesData();
			saveCohortMetadataListToOmop(cohortMetadataList);
			saveCohortToOmop(cohortList);
			returnVal = true;
		}catch(Exception e){
			RositaLogger.log(true, "ERROR|||Errors");
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
			e.printStackTrace();
		}
		return returnVal;
	}
	
	private List<OmopXCohortMetadata> getCohortMetadataList()throws Exception
	{
		List<OmopXCohortMetadata> cohortMetadataList = new ArrayList<OmopXCohortMetadata>(); 
		
		String statement = getGridNodeTableQuery("xCohortMetadata");
		jdbc = new JdbcTemplate(gridnodeDs);
		
		try {
			cohortMetadataList = jdbc.query(statement, new Object[0], new OmopXCohortMetadataRowMapper());
		}
		catch (Exception e) {
			RositaLogger.log(true, "ERROR|||Errors");
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
			throw e;
		}
		return cohortMetadataList;
	}
	
	 
	private List<OmopCohort> getCohortList()throws Exception
	{
		List<OmopCohort> cohortList = new ArrayList<OmopCohort>(); 
		
		String statement = getGridNodeTableQuery("cohort"); 
		jdbc = new JdbcTemplate(gridnodeDs);
		
		try {
			cohortList = jdbc.query(statement, new Object[0], new OmopCohortRowMapper());
		}
		catch (Exception e) {
			RositaLogger.log(true, "ERROR|||Errors");
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
			throw e;
		}
		return cohortList;
	}
	 
	
	private void saveCohortToOmop(List<OmopCohort> cohortList)throws Exception
	{
		if(cohortList.size()>0)
		{
			PreparedStatement pStatement = null;
			try{
			    String query = sqlTemplates.get("OmopCohort");

			    pStatement = ds.getConnection().prepareStatement(query);
			    int batchSize = 10;
			  
			    for (int i = 0; i < cohortList.size(); i++) {
			    	pStatement.setLong(1,cohortList.get(i).getId());
			    	pStatement.setString(2, cohortList.get(i).getxDataSource());
			    	pStatement.setLong(3, cohortList.get(i).getCohortConceptId());
			    	pStatement.setDate(4, new java.sql.Date(cohortList.get(i).getCohortStartDate().getTime()));
			    	if(cohortList.get(i).getCohortEndDate() != null){
			    		pStatement.setDate(5, new java.sql.Date(cohortList.get(i).getCohortEndDate().getTime()));
			    	}else{
			    		pStatement.setDate(5,null);
			    	}
			    	pStatement.setLong(6, cohortList.get(i).getPersonId());
			    	pStatement.setString(7, cohortList.get(i).getStopReason());
			    	pStatement.setLong(8,cohortList.get(i).getxCohortMetadataId());
			    	if(cohortList.get(i).getxGridNodeId() == 0){
			    		pStatement.setLong(9,new Long (gridnodeId));
			    	}else{
			    		pStatement.setLong(9,cohortList.get(i).getxGridNodeId());
			    	}
			    	
			    	pStatement.addBatch();
				      
			        if (i % batchSize == 0) {
			            pStatement.executeBatch();
			        }
			    }
			    
			    pStatement.executeBatch() ;
				pStatement.close();
			    
			}catch(Exception e){
				RositaLogger.log(true, "ERROR|||Errors");
				RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
				throw e;
			}
			finally{
				try {
					pStatement.close();
				} catch (SQLException e) {
					RositaLogger.log(true, "ERROR|||Errors");
					RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
					throw e;
				}
			}
		}
	}

	private void saveCohortMetadataListToOmop(List<OmopXCohortMetadata> cohortMetadataList)throws Exception
	{
		if(cohortMetadataList.size()>0)
		{
			PreparedStatement pStatement = null;
			try{
			    String query = sqlTemplates.get("OmopXCohortMetada");
			    pStatement = ds.getConnection().prepareStatement(query);
			    int batchSize = 10;
			  
			    for (int i = 0; i < cohortMetadataList.size(); i++) {
			    		pStatement.setLong(1,cohortMetadataList.get(i).getId());
			    		pStatement.setString(2, cohortMetadataList.get(i).getCohortName());
			    		pStatement.setString(3, cohortMetadataList.get(i).getCohortDescription());
			    		pStatement.setString(4, cohortMetadataList.get(i).getCohortCreatorName());
			    		pStatement.setString(5, cohortMetadataList.get(i).getCohortCreatorContact());
			    		pStatement.setBoolean(6, cohortMetadataList.get(i).getIsCohortShared());
			    		pStatement.setString(7, cohortMetadataList.get(i).getCohortQuery());
			    		pStatement.setString(8, cohortMetadataList.get(i).getServiceUrl());
			    		pStatement.setLong(9, cohortMetadataList.get(i).getOriginalCohortCount());
			    		pStatement.setLong(10, cohortMetadataList.get(i).getLastCohortCount());
			    		if(cohortMetadataList.get(i).getOriginalCohortDate() != null){
			    			pStatement.setDate(11, new java.sql.Date(cohortMetadataList.get(i).getOriginalCohortDate().getTime()));
			    		}else{
			    			pStatement.setDate(11,null);
			    		}
			    		
			    		if(cohortMetadataList.get(i).getLastCohortDate() != null){
			    			pStatement.setDate(12, new java.sql.Date(cohortMetadataList.get(i).getLastCohortDate().getTime()));
			    		}else{
			    			pStatement.setDate(12,null);
			    		}
			    		pStatement.setString(13, cohortMetadataList.get(i).getCohortPhiNotes());
			    		pStatement.setString(14, cohortMetadataList.get(i).getCohortOtherNotes());
			    		if(cohortMetadataList.get(i).getCohortExpirationDate() != null){
			    			pStatement.setDate(15, new java.sql.Date(cohortMetadataList.get(i).getCohortExpirationDate().getTime()));
			    		}else{
			    			pStatement.setDate(15,null);
			    		}
			    		
			    		if(cohortMetadataList.get(i).getXgridNodeId() == 0){
			    			pStatement.setLong(16, new Long (gridnodeId));
			    		}else{
			    			pStatement.setLong(16, cohortMetadataList.get(i).getXgridNodeId());
			    		}
			    		
			        	pStatement.addBatch();
			      
			        if (i % batchSize == 0) {
			            pStatement.executeBatch();
			        }
			    }
		    
			   pStatement.executeBatch() ;
			   pStatement.close();
		   }catch(Exception e){
			   RositaLogger.log(true, "ERROR|||Errors");
			   RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
			   throw e;
		   }
		   finally{
			   try {
				   pStatement.close();
			   } catch (SQLException e) {
				   RositaLogger.log(true, "ERROR|||Errors");
				   RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
				   throw e;
			   }
		   }
		}
	
	}
	
	private void deleteOmopTablesData()throws Exception
	{
		List<String> deleteStatements ;
		PreparedStatement pStatement = null;
		try
		{ 
			deleteStatements = new SqlTemplates().getOmopTablesMapDelete();
		    
			for (String statement : deleteStatements) {
				pStatement = ds.getConnection().prepareStatement(statement);
				pStatement.execute();
			}
		
		}catch(Exception e){
			RositaLogger.log(true, "ERROR|||Errors");
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
			throw e;
		}finally{
			try{
				pStatement.close();
			}catch(Exception e){
				RositaLogger.log(true, "ERROR|||Errors");
				RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
				throw e;
			}
		}
	}
	
	
	private void getGridnodeDatabaseConnection()throws Exception
	{
		try
		{
			ArgHandler.initialize();
			host = ArgHandler.getArg("targethost");
			port = ArgHandler.getArg("targetport", "3306");
			user = ArgHandler.getArg("targetuser");
			password = ArgHandler.getArg("targetpassword");
			database = ArgHandler.getArg("targetdatabase");
			gridnodeId = ArgHandler.getArg("gridnodeid");
			
			
			gridnodeDs = new SimpleDriverDataSource(new com.mysql.jdbc.Driver(), "jdbc:mysql://"+host+":"+port+"/"+database,user,password);
		
		} catch (Exception e) {
			RositaLogger.log(true, "ERROR|||Errors");
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_ERROR, "APP");
			throw e;
		}
	}
	
	private String getGridNodeTableQuery(String tablename){
		String query = "";
		
		if(tablename.equals("xCohortMetadata")){
			query = "SELECT * FROM "+database+".x_cohort_metadata;";
		}
		else if(tablename.equals("cohort")){
			query = "SELECT * FROM "+database+".cohort;";
		}
		return query;
	}
		
	
	public boolean importCohortdataToOmop(){
		return importCohortdata();
	}
}
