package edu.ucdenver.rosita.csv;

public class CsvValidationException extends Exception {
	
	public static String CATEGORY_TRUNCATE = "TRUNCATE";
	public static String CATEGORY_INVALID_DATE = "INVALIDDATE";
	public static String CATEGORY_NON_NUMERIC = "NONNUMERIC";
	public static String CATEGORY_MISSING_REQUIRED = "MISSING";
	public static String CATEGORY_MISSING_ATTRIBUTE = "MISSINGATTRIBUTE";
	public static String CATEGORY_UNKNOWN_COLUMN_TYPE = "UNKNOWNCOLUMNTYPE";
	public static String CATEGORY_COLUMN_NOT_IN_SCHEMA = "COLUMNNOTINSCHEMA";
	public static String CATEGORY_TOO_MANY_COLUMNS = "TOOMANYCOLUMNS";
	public static String CATEGORY_TOO_FEW_COLUMNS = "TOOFEWCOLUMNS";

	private String category = "VALIDATIONFAILED";
	private String column = "";
	
	public CsvValidationException(String message, String category) {
		super(message);
		this.category = category;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getColumn() {
		return column;
	}


	public void setColumn(String column) {
		this.column = column;
	}


	private static final long serialVersionUID = 851256248430865097L;

}
