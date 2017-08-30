package edu.ucdenver.rosita.utils;

public class ClinicResult {
	
	boolean success = true;
	Long errorCount = 0L;
	Long elements = 0L;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Long getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Long errorCount) {
		this.errorCount = errorCount;
	}
	public Long getElements() {
		return elements;
	}
	public void setElements(Long elements) {
		this.elements = elements;
	}

}
