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

package edu.ucdenver.rosita.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import edu.ucdenver.rosita.ArgHandler;

public class DataLinkageService {
	
	DataSource ds;
	
	public DataLinkageService(DataSource ds) {
		this.ds = ds;
	}
	
	public void link(List<MultiClinic> clinics) throws FileNotFoundException, Exception {
		Connection conn = ds.getConnection();
		PreparedStatement ps = null;
		
		try {
			
			boolean ranLinkage = false;
			
			ps = conn.prepareStatement("truncate table cz.linkage_log");
			ps.execute();
			ps.close();
			
			for (MultiClinic clinic : clinics) {
				
				if ("CLAIMS".equals(clinic.getDataSourceType())) {
					
					if (!(null == clinic.getLinkageType()) && !"".equals(clinic.getLinkageType()) && !"NONE".equals(clinic.getLinkageType())) {
						ranLinkage = true;
						
						ps = conn.prepareStatement("select * from cz.czx_linkage_preprocess(?, ?, ?)");
						ps.setLong(1, ArgHandler.getLong("stepid"));
						ps.setLong(2, clinic.getDataSourceId());
						ps.setString(3, clinic.getLinkageField());
						ps.execute();
						ps.close();
						
						if ("DETERMINISTIC".equals(clinic.getLinkageType())) {
							ps = conn.prepareStatement("select * from cz.czx_linkage_process(?, ?)");
							ps.setLong(1, ArgHandler.getLong("stepid"));
							ps.setString(2, clinic.getLinkageField());
							ps.execute();
							ps.close();
						}
						else if ("PROBABILISTIC".equals(clinic.getLinkageType())) {
							runLinkageProcess(clinic.getLinkageType(), ArgHandler.getLong("stepid"), clinic.getDataSourceId());
						}
						else if ("PPRL".equals(clinic.getLinkageType())) {
							runLinkageProcess(clinic.getLinkageType(), ArgHandler.getLong("stepid"), clinic.getDataSourceId());
						}
						
						ps = conn.prepareStatement("select * from cz.czx_linkage_postprocess(?)");
						ps.setLong(1, ArgHandler.getLong("stepid"));				
						ps.execute();
						ps.close();
					}
				}
			}
			
			if (!ranLinkage) {
				//Need to preprocess once for clinical data, if no linkage was run
				
				ps = conn.prepareStatement("select * from cz.czx_linkage_preprocess(?, ?, ?)");
				ps.setLong(1, ArgHandler.getLong("stepid"));
				ps.setLong(2, -1L);
				ps.setString(3, "");
				ps.execute();
				ps.close();
			}
			conn.close();
		}
		finally {
			ps.close();
			conn.close();
		}
	}
	
	private void runLinkageProcess(String linkageType, Long stepId, Long dataSourceId) {
		Process process = null;
		try {
			int exitCode = -1;
			String linkageScriptPath = ArgHandler.getArg("datalinkage." + linkageType.toLowerCase());
			String linkageScriptDir = linkageScriptPath.substring(0, linkageScriptPath.lastIndexOf("/")-1);
			String linkageScriptName = linkageScriptPath.substring(linkageScriptPath.lastIndexOf("/"), linkageScriptPath.length()-1);
			
			ProcessBuilder pb = new ProcessBuilder(linkageScriptName, String.valueOf(stepId), String.valueOf(dataSourceId));
			pb.directory(new File(linkageScriptDir));
			pb.redirectErrorStream(true);
			
			try {
				process = pb.start();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println("Linkage process was not started!");
				return;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			
			System.out.println("Started linkage process...");
			
			while ((line = r.readLine()) != null) {
				System.out.println("linkage:" + line);
			}
			System.out.println("...Finished reading. Process exit code was " + process != null ? process.waitFor() : "not found.");
			exitCode = process != null ? process.exitValue() : 1;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			process = null;
		}

	}

}
