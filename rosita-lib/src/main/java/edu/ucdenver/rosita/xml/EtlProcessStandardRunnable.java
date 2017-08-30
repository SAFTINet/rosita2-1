package edu.ucdenver.rosita.xml;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import edu.ucdenver.rosita.utils.RositaLogger;

public class EtlProcessStandardRunnable implements Runnable{
	
	private DataSource ds;
	private int rule_order;
	private String processStatement;
	private int jobId;
	
	public EtlProcessStandardRunnable(DataSource ds, int jobId, int rule_order){
		this.ds = ds;
		this.rule_order = rule_order;
		this.jobId = jobId;
	}
	
	private void loadData(){
		try {

			//Establish a new connection to the database
			//Every new worker will establish a different database connection
			Connection conn = ds.getConnection();
			processStatement = "select std.stdx_load_std_by_rule(-1,"+rule_order+");".replace("-1", String.valueOf(jobId));
			PreparedStatement ps = conn.prepareStatement(processStatement);
			ps.execute();
			ps.close();
			conn.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		loadData();
	}

}
