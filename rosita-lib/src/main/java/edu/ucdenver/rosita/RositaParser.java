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

package edu.ucdenver.rosita;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import edu.ucdenver.rosita.xml.*;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import edu.ucdenver.rosita.csv.CsvEtlValidatorService;
import edu.ucdenver.rosita.csv.CsvFileHandler;
import edu.ucdenver.rosita.csv.DelimitedEtlParserService;
import edu.ucdenver.rosita.utils.ClinicResult;
import edu.ucdenver.rosita.utils.DataPublisherStateMachine;
import edu.ucdenver.rosita.utils.ParsingService;
import edu.ucdenver.rosita.utils.RositaLogger;
import edu.ucdenver.rosita.utils.SourceCSVGenerator;
import edu.ucdenver.rosita.utils.SourceXmlGenerator;
import edu.ucdenver.rosita.utils.StackHandler;
import edu.ucdenver.rosita.utils.XmlToCsvWriter;
import edu.ucdenver.rosita.utils.types.SchemaColumn;

public class RositaParser {
	
	static DataSource ds = null;
	static DataSource targetDs = null;
	static Long stepId = 0L;
	static Long jobId = 0L;
	static boolean all = false;
	static MultiClinicSqlService multiClinicSqlService = null;
	//static boolean multiclinicMode = true;
	static String fileMode = null;
	static boolean trackParentsCsv = false;
	
	public static void main(String[] args) throws FileNotFoundException, Exception {
		
		ArgHandler.initialize(args);

		RositaParser parser = new RositaParser();
		String filename = ArgHandler.getArg("file");
		
		String db = ArgHandler.getArg("database");
		String host = ArgHandler.getArg("host", "localhost");
		String port = ArgHandler.getArg("port", "5432");
		String user = ArgHandler.getArg("user");
		String password = ArgHandler.getArg("password");
		
		String targetdb = ArgHandler.getArg("targetdatabase");
		String targethost = ArgHandler.getArg("targethost", "localhost");
		String targetport = ArgHandler.getArg("targetport", "3306");
		String targetuser = ArgHandler.getArg("targetuser");
		String targetpassword = ArgHandler.getArg("targetpassword");
		
		Integer threshold = ArgHandler.getInt("threshold");
		
		Long maxValidationErrors = ArgHandler.getLong("maxerrors");
		boolean saveErrors = ArgHandler.getBoolean("saveerrors");
		boolean forUi = ArgHandler.getBoolean("forui");
		Long gridNodeId = ArgHandler.getLong("gridnodeid");
		Long dataSourceId = ArgHandler.getLong("datasourceid", 1L);
		
		boolean load = ArgHandler.getBoolean("load");
		boolean loadvocabulary = ArgHandler.getBoolean("loadvocabulary");
        boolean loadrules = ArgHandler.getBoolean("loadrules");
        boolean loadpublishrules = ArgHandler.getBoolean("loadpublishrules");
        boolean loadprofilerules = ArgHandler.getBoolean("loadprofilerules");
        boolean validate = ArgHandler.getBoolean("validate");
		boolean exportOMOP = ArgHandler.getBoolean("publish");
		boolean verify = ArgHandler.getBoolean("verify");
		boolean truncatelz = ArgHandler.getBoolean("truncatelz");
		boolean profilelz = ArgHandler.getBoolean("profilelz");
		boolean profileomop = ArgHandler.getBoolean("profileomop");
		boolean process = ArgHandler.getBoolean("process");
		boolean processStd = ArgHandler.getBoolean("processstd");
		boolean export = ArgHandler.getBoolean("export");
		boolean createtables = ArgHandler.getBoolean("createtables");
		boolean linkdata = ArgHandler.getBoolean("linkdata");
		boolean importcohorts = ArgHandler.getBoolean("importcohorts");
		
		//SPECIAL: Generate
		if (ArgHandler.getBoolean("generatexml")) {
			SourceXmlGenerator.generate(args);
			System.exit(0);
		}
        if (ArgHandler.getBoolean("generatecsv"))  {
            SourceCSVGenerator.generate(args);
            System.exit(0);
        }

        if (ArgHandler.getBoolean("xmlconvert"))  {
            XmlToCsvWriter.generate(args);
            System.exit(0);
        }

		stepId = ArgHandler.getLong("stepid");
		jobId = ArgHandler.getLong("jobid");
		all = ArgHandler.getBoolean("all");
		trackParentsCsv = ArgHandler.getBoolean("trackparents");
		
		//File must exist for most operations...
		if (verify || validate || load) {
			ArgHandler.mustExist("file");
		}
		
		//multiclinicMode = true;
		
		//Checking these arguments for all operations now - they should be in the properties file regardless of whether they're actually used
		ArgHandler.mustExist("database");
		ArgHandler.mustExist("password");
		ArgHandler.mustExist("user");
		ds = new DriverManagerDataSource("jdbc:postgresql://" + host + ":" + port + "/" + db, user, password);
		multiClinicSqlService = new MultiClinicSqlService(ds);
		
		RositaLogger.initialize(forUi, ds);
		
		if (validate) {
			parser.validate();
		}
		else if (linkdata) {
			parser.linkData();
		}
		else if (load) {
			parser.truncateAndLoad(filename, threshold);
		}
		else if (loadvocabulary) {
			parser.parseVocabulary(filename, threshold);
		}
		else if (exportOMOP) {
			ArgHandler.mustExist("targetdatabase");
			ArgHandler.mustExist("targetpassword");
			ArgHandler.mustExist("targetuser");
			ArgHandler.mustExist("gridnodeid");
            DataPublisherStateMachine stateMachine = null;
			try {
				targetDs = new SimpleDriverDataSource(new com.mysql.jdbc.Driver(), "jdbc:mysql://" + targethost + ":" + targetport + "/" + targetdb + "?allowMultiQueries=true", targetuser, targetpassword);
                //this is where we want to access the grid node's state table to see if we can proceed according to our
                //state definitions
                stateMachine = new DataPublisherStateMachine(targetDs);
                String stateResult = stateMachine.canProceed();
                if(stateResult == null) {
                    RositaLogger.log(RositaLogger.forUi(),"state machine returned null indicating system is in unknown state");
                    System.exit(1);
                }
                if(stateResult.equals("LOAD")) {
                	
                	//Import records from OMOP first.
            		CohortImporterService cohortImportService = new CohortImporterService(ArgHandler.getInt("threshold"), ds);
            		cohortImportService.importCohortdataToOmop();
            		
                    boolean result = parser.exportOMOP(threshold, gridNodeId);
                    if(result) {
                        stateMachine.setSuccess();
                        RositaLogger.log(RositaLogger.forUi(),"RositaParser.exportOMOP returned Success");
                        System.exit(0);
                    } else {
                        stateMachine.setFailure("RositaParser.exportOMOP returned Fail");
                        RositaLogger.log(RositaLogger.forUi(),"RositaParser.exportOMOP returned Fail");
                        System.exit(1);
                    }
                } else if(stateResult.equals("TIMEOUT")) {
                    RositaLogger.log(RositaLogger.forUi(),"DataPublisherStateMachine returned TimeOut");
                    System.exit(1);
                }
            } catch (InterruptedException e) {
                RositaLogger.error(e);
                RositaLogger.log(RositaLogger.forUi(),"Caught InterruptedException, reason = " + e.getMessage());
                stateMachine.setFailure("Caught InterruptedException, reason = " + e.getMessage());
                System.exit(1);
			} catch (SQLException e) {
                RositaLogger.error(e);
				e.getNextException().printStackTrace(System.out);
				e.printStackTrace(System.out);
                try {
                    stateMachine.setFailure("Caught SQLException, reason = " + e.getMessage());
                } catch(SQLException e1) {
                    System.out.println("SQLException in StateMachine.setFailure method...hence we're unable to set correct state");
                }
				System.exit(1);
			} catch (Exception e) {
				RositaLogger.error(e);
                e.printStackTrace(System.out);
                try {
                    stateMachine.setFailure("Caught Exception, reason = "+ e.getMessage());
                } catch(SQLException e1) {
                    System.out.println("SQLException in StateMachine.setFailure method...hence we're unable to set correct state");
                }
                System.exit(1);
            }
		}
		else if (verify) {
			parser.verify();
		}
		else if (truncatelz) {
			parser.truncatelz();
		}
		else if (profilelz) {
			parser.profilelz();
		}
		else if (profileomop) {
			parser.profileOmop();
		}
		else if (process) {
			parser.processAll();
		}
		else if (processStd) {
			parser.processToStandard();
		}
		else if (export) {
			parser.export();
		}
		else if (createtables) {
			parser.createTables(dataSourceId);
		}
		else if (loadrules) {
            parser.loadrules();
        }
        else if (loadpublishrules) {
            parser.loadpublishrules(filename);
        }
        else if (loadprofilerules) {
            parser.loadprofilerules(filename);
        }
		else if (importcohorts) {
			parser.importCohortData();
		}
		
		else {
			System.out.println("Specify command:");
			System.out.println("\t\trositaparser load");
			System.out.println("\t\trositaparser loadvocabulary");
			System.out.println("\t\trositaparser publish");
			System.out.println("\t\trositaparser validate");
			System.out.println("\t\trositaparser verify");
			System.out.println("\t\trositaparser truncatelz");
			System.out.println("\t\trositaparser profilelz");
			System.out.println("\t\trositaparser process");
			System.out.println("\t\trositaparser profileomop");
			System.out.println("\t\trositaparser backup");
			System.out.println("\t\trositaparser export");
			System.exit(1); //If the UI call gets here, something's missing!
		}
	}
	
	public void importCohortData() {
		CohortImporterService cohortImportService = new CohortImporterService(ArgHandler.getInt("threshold"), ds);
		cohortImportService.importCohortdataToOmop();
	}
	
	
	public static boolean importCohortDataUI()
	{
		CohortImporterService cohortImportService = null;
		try {
				ArgHandler.initialize( );
				String db = ArgHandler.getArg("database");
				String host = ArgHandler.getArg("host", "localhost");
				String port = ArgHandler.getArg("port", "5432");
				String user = ArgHandler.getArg("user");
				String password = ArgHandler.getArg("password");
				
				ArgHandler.mustExist("database");
				ArgHandler.mustExist("password");
				ArgHandler.mustExist("user");
				ds = new DriverManagerDataSource("jdbc:postgresql://" + host + ":" + port + "/" + db, user, password);
				
				cohortImportService = new CohortImporterService(ArgHandler.getInt("threshold"), ds);
			
		} catch (IOException e) {
			processException(e);
		}
		
		return cohortImportService.importCohortdataToOmop();
	}
	
	public void createTables(Long dataSourceId) {
		MultiClinic clinic = multiClinicSqlService.getMultiClinic(dataSourceId);
		try {
			RawTableService service = new RawTableService(ds);
			Map<String, ArrayList<SchemaColumn>> schema = ParsingService.getSchemaLayoutFromPath(ArgHandler.getArg("folder.schemas") + File.separator + clinic.getSchemaPath(), false);
			//Drop all mentioned tables from RAW then recreate
			service.dropTablesInSchema(schema);
			service.createTablesInSchema(schema);
		}
		catch (Exception e) {
			processException(e);
		}
	}
	
	public void validate() {
		List<MultiClinic> clinics = multiClinicSqlService.getSuccessfulClinics(jobId);
		boolean success = true;
		Long errorCount = 0L;
		Long elements = 0L;
		for (int i = 0; i < clinics.size(); i++) {
			MultiClinic clinic = clinics.get(i);
			fileMode = clinic.getFileType();
			String dataSourceDirectory = ArgHandler.getArg("file") + "/" + clinic.getDataSourceDirectory();
			ClinicResult vr = validateInternal(dataSourceDirectory, clinic.getDataSourceId(), i+1, clinics.size(), elements);
			if (!vr.isSuccess()) {
				success = false;
			}
			errorCount += vr.getErrorCount();
			elements = vr.getElements();
		}
		
		if (success) {
			RositaLogger.log(true, "COMPLETE|||" + errorCount + "|||" + elements);
			System.exit(0);
		}
		else {
			RositaLogger.log(true, "ERROR|||Some clinics had validation errors - see the error log for more information.");
			RositaLogger.log(false, "Some clinics had validation errors - see the error log for more information.");
			System.exit(1);
		}
	}
	
	private ClinicResult validateInternal(String dataSourceDirectory, Long dataSourceId, Integer clinic, Integer maxClinics, Long elementsSoFar) {
		
		ClinicResult vr = new ClinicResult();
		String[] fileAndMode = getFileAndMode(dataSourceDirectory);
		String checkedFile = fileAndMode[0];
		String mode = fileAndMode[1];
		if (mode.equals("csv") || mode.equals("txt")) {
			try {
				MultiClinic mc = multiClinicSqlService.getMultiClinic(dataSourceId);
				CsvEtlValidatorService validatorService = new CsvEtlValidatorService(ds, ArgHandler.getInt("threshold"), ArgHandler.getBoolean("saveerrors"), ArgHandler.getArg("folder.schemas") + File.separator + mc.getSchemaPath(), checkedFile, null, ArgHandler.getLong("maxerrors"), stepId, mc);
				validatorService.setTotalLines(elementsSoFar);
	 			Long errorsThisClinic = validatorService.validate(clinic, maxClinics);
	 			vr.setErrorCount(errorsThisClinic);
	 			vr.setElements(validatorService.getTotalLines());
				RositaLogger.log(false, "Validation complete");
				multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, dataSourceId, errorsThisClinic == 0L);
			}
			//Any unexpected errors (not validation errors)
			catch (Exception e) {
				e.printStackTrace(System.out);
				RositaLogger.error(e);
				multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, dataSourceId, false);
				vr.setSuccess(false);
			}
		}
		else {
			try {
				MultiClinic mc = multiClinicSqlService.getMultiClinic(dataSourceId);
				EtlValidatorService validatorService = new EtlValidatorService(ds, ArgHandler.getInt("threshold"), ArgHandler.getBoolean("saveerrors"), dataSourceId);
				StackHandler.getInstance().setElements(elementsSoFar);
				Long errorsThisClinic = validatorService.validate(checkedFile, ArgHandler.getArg("folder.schemas") + File.separator + mc.getSchemaPath(), ArgHandler.getLong("maxerrors"), stepId, clinic, maxClinics);
				vr.setErrorCount(errorsThisClinic);
				vr.setElements(StackHandler.getInstance().getElements());
				multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, dataSourceId, errorsThisClinic == 0L);
			}
			//Any unexpected errors (not validation errors)
			catch (Exception e) {
				e.printStackTrace(System.out);
				RositaLogger.error(e);
				multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, dataSourceId, false);
				vr.setSuccess(false);
			}
		}
		
		return vr;
	}
	
	public void truncateAndLoad(String filename, Integer threshold) {
		if (filename == null) {
			return;
		}

		//No longer deleting data from disabled clinics
//		List<MultiClinic> clinicsToClean = multiClinicSqlService.getDisabledClinics();
//		for (int i = 0; i < clinicsToClean.size(); i++) {
//			MultiClinic clinic = clinicsToClean.get(i);
//			EtlTruncatorService trunc = new EtlTruncatorService(ds);
//			try {
//				String mode = clinic.getFileType();
//				trunc.truncate(clinic, i+1, clinicsToClean.size(), true);
//			}
//			catch (Exception e) {
//				processException(e);
//				System.exit(1);
//			}
//		}
		
		TableIndexService indexService = new TableIndexService(ds, stepId);
        int result = 0;
        result = indexService.dropIndexes("raw");
        if (result != 0) {
            RositaLogger.error("A SQL error occurred while attempting to drop indexes from the RAW schema.", RositaLogger.MSG_TYPE_WARNING);
        }

		List<MultiClinic> clinics = multiClinicSqlService.getSuccessfulClinics(jobId);
		boolean success = true;
		Long totalElements = 0L;
		for (int i = 0; i < clinics.size(); i++) {
			MultiClinic clinic = clinics.get(i);
			fileMode = clinic.getFileType();
			String dataSourceDirectory = filename + "/" + clinic.getDataSourceDirectory();
			ClinicResult lr = truncateAndLoadInternal(dataSourceDirectory, threshold, clinic, i+1, clinics.size(), totalElements);
			if (!lr.isSuccess()) {
				success = false;
			}
			totalElements = lr.getElements();
		}

		try {
	        VaccuumService dyson = new VaccuumService(ds);
	        dyson.vaccuumSchema("raw");
		}
		catch (Exception e) {
			//Failing to vaccuum isn't a disaster (just look at my house)
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_WARNING);
		}
		
		if (success) {
			RositaLogger.log(true, "SUCCESS");
			System.exit(0);
		}
		else {
			RositaLogger.log(true, "ERROR|||Some clinics had parsing errors - see the error log for more information.");
			RositaLogger.log(false, "Some clinics had parsing errors - see the error log for more information.");
			System.exit(1);
		}
	}
	
	private ClinicResult truncateAndLoadInternal(String filename, Integer threshold, MultiClinic clinic, Integer clinicNum, Integer maxClinics, Long elementsSoFar) {
		
		ClinicResult lr = new ClinicResult();
		
		try {
			
			String[] fileAndMode = getFileAndMode(filename);
			String checkedFile = fileAndMode[0];
			String mode = fileAndMode[1];
			
			if (!clinic.isIncremental()) {
				EtlTruncatorService trunc = new EtlTruncatorService(ds);
				trunc.truncate(clinic, clinicNum, maxClinics, false);
			}
			
			if (mode.equals("csv") || mode.equals("txt")) {
				try {
					DelimitedEtlParserService etlParserService = new DelimitedEtlParserService(ds, threshold, clinic.getDataSourceId());
					etlParserService.setTotalLines(elementsSoFar); //Start counting where we last left off
					etlParserService.parse(clinic, clinicNum, maxClinics);
					lr.setElements(etlParserService.getTotalLines());
					multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), true);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					RositaLogger.error(e);
					lr.setSuccess(false);
					multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), false);
				}
			}
			else {
				try {
					EtlParserService etlParserService = new EtlParserService(ds, threshold, clinic.getDataSourceId());
					etlParserService.setOutputLimit(10000L);
					etlParserService.setElements(elementsSoFar); //Start counting where we last left off
					etlParserService.parse(checkedFile, clinicNum, maxClinics);
					lr.setElements(etlParserService.getElements());
					multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), true);
				}
				catch (Exception e) {
					e.printStackTrace(System.out);
					lr.setSuccess(false);
					multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), false);
				}
			}
		}
		catch (Exception e) {
			processException(e);
			lr.setSuccess(false);
			multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), false);
		}
		
		return lr;
	}

	public void parseVocabulary(String filename, Integer threshold) {
		if (filename == null) {
			return;
		}
		try {
			VocabularyParserService vocabularyParserService = new VocabularyParserService(ds, threshold);
			vocabularyParserService.parse(filename);
			RositaLogger.log(false, "Vocabulary load complete");
			RositaLogger.log(true, "SUCCESS|||Vocabulary load complete");
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
	}
	
    public void loadrules() {

		EtlRuleImportService service = null;
		try {
	        service = new EtlRuleImportService(ds, stepId);

	        service.deleteETLRules(-1L, "STD");
            service.deleteIndexes("STD");
	        
	        //Run this for clinics that have passed the load step
            List<MultiClinic> clinics = multiClinicSqlService.getSuccessfulClinics(jobId, 3L);

            for (int i = 0; i < clinics.size(); i++) {
                MultiClinic c = clinics.get(i);
                service.loadRules(c, "STD");
            }
            service.updateRules();

            TableIndexService indexService = new TableIndexService(ds, stepId);
            int result = 0;
            result = indexService.addIndexes("raw");
            if (result != 0) {
                RositaLogger.error("A SQL error occurred while attempting to add indexes from the RAW schema.", RositaLogger.MSG_TYPE_WARNING);
            }

            RositaLogger.log(true, "SUCCESS|||ETL Rules load complete");
	    } catch (SQLException e) {
        	RositaLogger.error(e);
        } catch (IOException e) {
        	RositaLogger.error(e);
        }

    }

    public void loadpublishrules(String filename) {

        EtlRuleImportService service = null;
        try {
            service = new EtlRuleImportService(ds, stepId);
            service.loadRules(filename, "GRID");
            RositaLogger.log(true, "SUCCESS|||Publish Rules load complete");
            service.updateRules();
            System.exit(0);
        } catch (SQLException e) {
            RositaLogger.error(e);
        } catch (IOException e) {
            RositaLogger.error(e);
        }
        System.exit(1);

    }

    public void loadprofilerules(String filename) {

        ProfileRuleImportService service = null;
        try {
            service = new ProfileRuleImportService(ds, stepId);
            service.loadRules(filename);
            RositaLogger.log(true, "SUCCESS|||Profile Rules load complete");
            System.exit(0);
        } catch (SQLException e) {
            RositaLogger.error(e);
        } catch (IOException e) {
            RositaLogger.error(e);
        }
        System.exit(1);
    }

    public boolean exportOMOP(Integer threshold, Long gridNodeId) {
		try {
//			GridTruncatorService truncator = new GridTruncatorService(targetDs);
//			truncator.truncate();
//			RositaLogger.log(false, "Truncation complete");
//			OmopExporterService omopExporterService = new OmopExporterService(threshold, ds, targetDs, gridNodeId);
//			boolean success = omopExporterService.export();
//			RositaLogger.log(false, success ? "OMOP export complete" : "OMOP export encountered errors");
//            return success;
            GridExporterService gridExporterService = new GridExporterService(threshold, ds, targetDs, gridNodeId);
            boolean success = gridExporterService.export();
            RositaLogger.log(false, success ? "Publish to Grid complete" : "Publish to Grid encountered errors");
            return success;
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
        return false;
	}

    public void verify() {
		
		List<MultiClinic> clinics = multiClinicSqlService.getActiveMultiClinics();
		boolean success = true;
		
		for (MultiClinic clinic : clinics) {
			fileMode = clinic.getFileType();
			try {
				verifyClinic(clinic);
				multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), true);
			}
			catch (Exception e) {
				success = false;
				processException(e);
				multiClinicSqlService.saveMultiClinicStatus(jobId, stepId, clinic.getDataSourceId(), false);
			}
		}
		
		if (success) {
			RositaLogger.log(true, "SUCCESS|||All clinics verified");
			RositaLogger.log(false, "All clinics verified");
			System.exit(0);
		}
		else {
			RositaLogger.log(true, "ERROR|||Some clinics had errors - see the error log for more information.");
			RositaLogger.log(false, "Some clinics had errors - see the error log for more information.");
			System.exit(1);
		}
	}
		
	public void verifyClinic(MultiClinic clinic) throws Exception {
		//Detect if directory, and if XML or CSV
		String filename = ArgHandler.getArg("file") + "/" + clinic.getDataSourceDirectory();
		File file = new File(filename);
		if (!file.exists()) {
			throw new Exception("File or folder " + filename + " does not exist");
		}
		String[] fileAndMode = getFileAndMode(filename);
		String checkedFile = fileAndMode[0];
		String mode = fileAndMode[1];
		if (mode.equals("csv") || mode.equals("txt")) {
			verifyCsv(checkedFile, clinic);
		}
		else {
			verifyXml(checkedFile, clinic);
		}
	}
	
	public String[] getFileAndMode(String filename) {
		
		//If we have no file mode (command line), detect it
		if (fileMode == null || fileMode.equals("")) {
			File file = new File(filename);
			if (file.isDirectory()) {
				String xmlFileName = getFirstXmlFileInDirectory(file);
				if (xmlFileName != null) {
					return new String[] {filename + "/" + xmlFileName, "xml"};
				}
				else {
					return new String[] {filename, "csv"};
				}
			}
			else {
				return new String[] {filename, "xml"};
			}
		}
		//If we have a file mode, check for XML directories - otherwise just return the file and mode.
		else {
			//If mode is XML and we have a directory, get the first XML file in that directory
			if (fileMode.equalsIgnoreCase("xml")) {
				File file = new File(filename);
				if (file.isDirectory()) {
					String xmlFileName = getFirstXmlFileInDirectory(file);
					return new String[] {filename + "/" + xmlFileName, "xml"};
				}
				else {
					return new String[] {filename, "xml"};
				}
			}
			else {
				return new String[] {filename, fileMode};
			}
		}

	}
	
	private String getFirstXmlFileInDirectory(File dir) {
		String[] filesInDirectory = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.endsWith(".xml")) return true;
				return false;
			}
		});
		//If at least one XML file, operate on this as XML
		String xmlFileName = null;
		for (String fileInDirectory : filesInDirectory) {
			if (fileInDirectory.endsWith(".xml")) {
				xmlFileName = fileInDirectory;
				break;
			}
		}
		
		return xmlFileName;
	}
	
	public void verifyXml(String filename, MultiClinic clinic) throws Exception {
		File file = new File(filename);
		File schema = new File(ArgHandler.getArg("folder.schemas") + File.separator + clinic.getSchemaPath());
		
		if (RositaLogger.forUi()) {
			if (!file.exists()) {
				throw new Exception("XML file " + filename + " does not exist");
			}
			if (!schema.exists()) {
				throw new Exception("Schema file " + schema.getCanonicalPath() + " does not exist");
			}
		}
	}
	
	public void verifyCsv(String directoryName, MultiClinic clinic) throws Exception {
		
		StringBuilder errors = new StringBuilder();
		File schema = new File(ArgHandler.getArg("folder.schemas") + File.separator + clinic.getSchemaPath());
		
		if (!schema.exists()) {
			throw new Exception("Schema file " + schema.getCanonicalPath() + " does not exist. ");
		}

		CsvFileHandler handler = new CsvFileHandler(directoryName, null);
		errors.append(handler.verifyRequiredFiles(clinic, ParsingService.getSchemaLayoutFromPath(schema.getCanonicalPath(), false)));

		if (errors.length() > 0) {
			RositaLogger.log(false, errors.toString().trim());
			RositaLogger.error(errors.toString().trim(), RositaLogger.MSG_TYPE_WARNING);
		} else {
			RositaLogger.log(false, "Files verified");
			RositaLogger.log(true, "SUCCESS|||Files verified.");
		}
		
	}
	
	public void truncatelz() {

		EtlTruncatorService trunc = new EtlTruncatorService(ds);
		List<MultiClinic> clinics = multiClinicSqlService.getSuccessfulClinics(jobId);
		for (int i = 0; i < clinics.size(); i++) {
			try {
				MultiClinic clinic = clinics.get(i);
				trunc.truncate(clinic, i+1, clinics.size(), false);
			}
			catch (Exception e) {
				processException(e);
			}
		}
	}
	
	public void profilelz() {
		EtlProfilerService prof = new EtlProfilerService(ds);
		try {
			prof.profile(stepId);
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
		System.exit(0);
	}
	
	public void processAll() {
		EtlProcessService proc = new EtlProcessService(ds);
		try {
			proc.process(stepId);
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
		
		/*try {
	        VaccuumService dyson = new VaccuumService(ds);
	        dyson.vaccuumSchema("omop");
		}
		catch (Exception e) {
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_WARNING);
		}*/
		
		System.exit(0);
	}
	
	public void processToStandard() {

        TableIndexService indexService = new TableIndexService(ds, stepId);
        int result = 0;
        result = indexService.saveIndexes("std");
        if (result != 0) {
            RositaLogger.error("A SQL error occurred while attempting to save indexes from the STD schema.", RositaLogger.MSG_TYPE_WARNING);
            result = indexService.dropIndexes("std");
            if (result != 0) {
                RositaLogger.error("A SQL error occurred while attempting to drop indexes from the STD schema.", RositaLogger.MSG_TYPE_WARNING);
            }
        }

        EtlProcessStandardService proc = new EtlProcessStandardService(ds);

        result = indexService.addIndexes("std");
        if (result != 0) {
            RositaLogger.error("A SQL error occurred while attempting to add indexes from the STD schema.", RositaLogger.MSG_TYPE_WARNING);
        }

		try {
			proc.process(stepId);
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
		
		try {
	        VaccuumService dyson = new VaccuumService(ds);
	        dyson.vaccuumSchema("std");
		}
		catch (Exception e) {
			RositaLogger.error(e.getMessage(), RositaLogger.MSG_TYPE_WARNING);
		}
		
		System.exit(0);
	}

	public void profileOmop() {
		OmopProfilerService prof = new OmopProfilerService(ds);
		try {
			prof.profile(stepId);
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
		System.exit(0);
	}
	
	private void export() {
		ExportService exporter = new ExportService(ds);
		try {
			exporter.export(all);
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
		System.exit(0);
	}
	
	private void linkData() {
		DataLinkageService dls = new DataLinkageService(ds);
		List<MultiClinic> clinics = multiClinicSqlService.getSuccessfulClinics(jobId, 3L);
		try {
			dls.link(clinics);
		}
		catch (Exception e) {
			processException(e);
			System.exit(1);
		}
		System.exit(0);
	}

	
	private static void processException(Exception e) {
		RositaLogger.error(e);
	}

}
