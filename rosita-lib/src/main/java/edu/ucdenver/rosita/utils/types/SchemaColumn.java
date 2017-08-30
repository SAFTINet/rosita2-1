package edu.ucdenver.rosita.utils.types;

public class SchemaColumn {
	
	String tableName;
	String name;
	String type;
	String length;
	String precision;
	String scale;
	boolean required;
	boolean exists;
	boolean tableExists;
	
	public SchemaColumn(String tableName, String columnName, String type, String length, String precision, String scale, boolean required) {
		this.tableName = tableName;
		this.name = columnName;
		this.type = type;
		this.length = length;
		this.precision = precision;
		this.scale = scale;
		this.required = required;
	}

	public String getTableName() {
		return tableName;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getLength() {
		return length;
	}

	public String getPrecision() {
		return precision;
	}

	public String getScale() {
		return scale;
	}

	public boolean isRequired() {
		return required;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isExists() {
		return exists;
	}

	public boolean isTableExists() {
		return tableExists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public void setTableExists(boolean tableExists) {
		this.tableExists = tableExists;
	}
	
}
