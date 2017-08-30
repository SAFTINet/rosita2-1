package edu.ucdenver.rosita.xml;

public class MultiClinic {
	
	private Long dataSourceId;
	private String dataSourceDirectory;
	private String fileType;
	private String schemaPath;
	private String firstRowType;
	private String delimiter;
	private String quoteCharacter;
	private boolean incremental;
	private String etlRulesFile;
	private String dataSourceType;
	private String linkageField;
	private String linkageType;
	
	public String getEtlRulesFile() {
		return etlRulesFile;
	}
	public void setEtlRulesFile(String etlRulesFile) {
		this.etlRulesFile = etlRulesFile;
	}
	public Long getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(Long dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getDataSourceDirectory() {
		return dataSourceDirectory;
	}
	public void setDataSourceDirectory(String dataSourceDirectory) {
		this.dataSourceDirectory = dataSourceDirectory;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getSchemaPath() {
		return schemaPath;
	}
	public void setSchemaPath(String schemaPath) {
		this.schemaPath = schemaPath;
	}
	public String getFirstRowType() {
		return firstRowType == null ? "" : firstRowType;
	}
	public void setFirstRowType(String firstRowType) {
		this.firstRowType = firstRowType;
	}
	public String getDelimiter() {
		return delimiter;
	}
	public String getQuoteCharacter() {
		return quoteCharacter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public void setQuoteCharacter(String quoteCharacter) {
		this.quoteCharacter = quoteCharacter;
	}
	public boolean isIncremental() {
		return incremental;
	}
	public void setIncremental(boolean incremental) {
		this.incremental = incremental;
	}
	public String getDataSourceType() {
		return dataSourceType;
	}
	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	public String getLinkageField() {
		return linkageField;
	}
	public void setLinkageField(String linkageField) {
		this.linkageField = linkageField;
	}
	public String getLinkageType() {
		return linkageType;
	}
	public void setLinkageType(String linkageType) {
		this.linkageType = linkageType;
	}

}
