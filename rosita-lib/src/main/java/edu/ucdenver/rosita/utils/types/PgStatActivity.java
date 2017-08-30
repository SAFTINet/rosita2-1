package edu.ucdenver.rosita.utils.types;

import java.util.Date;

public class PgStatActivity {
	
	Integer pid;
	String datname;
	Date queryStart;
	Boolean waiting;
	String state;
	String query;
	
	public PgStatActivity() {
	}
	
	public PgStatActivity(Integer pid, String datname, Date queryStart, Boolean waiting, String state, String query) {
		this.pid = pid;
		this.datname = datname;
		this.queryStart = queryStart;
		this.waiting = waiting;
		this.state = state;
		this.query = query;
	}
	
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getDatname() {
		return datname;
	}
	public void setDatname(String datname) {
		this.datname = datname;
	}
	public Date getQueryStart() {
		return queryStart;
	}
	public void setQueryStart(Date queryStart) {
		this.queryStart = queryStart;
	}
	public Boolean getWaiting() {
		return waiting;
	}
	public void setWaiting(Boolean waiting) {
		this.waiting = waiting;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
}
