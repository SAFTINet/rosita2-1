package edu.ucdenver.rosita.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucdenver.rosita.utils.ParsingService;
import edu.ucdenver.rosita.utils.RositaLogger;

public class CsvFileSpec{
	static enum Types {VARCHAR, INTEGER, DATE, DECIMAL, TEXT, TIMESTAMP, UNKNOWN};
    public static int TEXT_LENGTH = 500;
    public static int DATE_LENGTH = 10;
    public static int TIMESTAMP_LENGTH = 16;
	private String table;
	public int noOfColumns = 0;
	private List<ColumnSpec> columnsList;
	private ColumnSpec[] columns;
	private Map<Integer, Integer> columnMap = null;
	
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
	
	public CsvFileSpec(String table){
		this.table = table;
		columnsList = new ArrayList<ColumnSpec>();
		dateFormat.setLenient(false);
		//datetimeFormat.setLenient(false);
	}
	
	public void addColumn(String[] column)
	{
		noOfColumns++;
		columnsList.add(new ColumnSpec(column));
	}
	
	public boolean tableNameEquals(String table){
		return this.table.equals(table);
	}
	
	public String getTableName(){
		return table;
	}
	
	public void setColumnMap(Map<Integer, Integer> columnMap) {
		this.columnMap = columnMap;
	}
	
	public ColumnSpec[] getColumns() {
		return columns;
	}
	
	public void finalize(){
		columns = new ColumnSpec[0];
		columns = columnsList.toArray(columns);
	}
	
	public List<CsvValidationException> validateRow(String[] row)
	{

		List<CsvValidationException> validationErrors = new ArrayList<CsvValidationException>();
				
		if (row.length > noOfColumns) {
			validationErrors.add(new CsvValidationException("Too many columns! Expected " + noOfColumns + ", found " + row.length, CsvValidationException.CATEGORY_TOO_MANY_COLUMNS));
		}
		if (row.length < noOfColumns) {
			validationErrors.add(new CsvValidationException("Too few columns! Expected " + noOfColumns + ", found " + row.length, CsvValidationException.CATEGORY_TOO_FEW_COLUMNS));
			
			//Fill the remainder of the columns with blanks in order to validate
			String[] newRow = new String[noOfColumns];
			for (int i = 0; i < newRow.length; i++) {
				if (i < row.length) {
					newRow[i] = row[i];
				}
				else {
					newRow[i] = "";
				}
			}
			
			row = newRow;
		}

		for(int i = 0; i < row.length; i++){
			try {
				validateCell(row[i], i);
			}
			catch (CsvValidationException e) {
				int columnIndex = i;
				if (columnMap != null && columnMap.get(columnIndex) != null) {
					columnIndex = columnMap.get(columnIndex);
				}
				e.setColumn(columns[columnIndex].column);
                validationErrors.add(e);
				RositaLogger.log(false, e.getColumn() + ": " + e.getMessage());
			}
		}
		return validationErrors;
	}
	
	private void validateCell(String cell, int columnNo) throws CsvValidationException
	{
		//If we have a column map, translate the file column number into the schema column number.
		//If not, just assume the columns in the schema and in the file are in the same order.
		//If this is out of range of the number of columns, we're ignoring this column
		if (columnMap != null) {
			if (columnMap.get(columnNo) != null) {
				columnNo = columnMap.get(columnNo);
			}
			else {
				//Not mapping this column - skip it
				return;
			}
		}
		if (columnNo >= columns.length) {
			return;
		}
		if(!columns[columnNo].required && "".equals(cell)) return;
		if(columns[columnNo].required && "".equals(cell)) { throw new CsvValidationException("Data for required column is missing", CsvValidationException.CATEGORY_MISSING_REQUIRED); }
		
		switch (columns[columnNo].type){
			case VARCHAR:
				validateVarchar(cell, columns[columnNo].length);
				break;
			case INTEGER:
				validateInteger(cell);
				break;
			case DATE:
				validateDate(cell);
				break;
			case DECIMAL:
				validateDecimal(cell, columns[columnNo].precision, columns[columnNo].scale);
				break;
			case TEXT:
				validateText(cell);
				break;
			case TIMESTAMP:
				validateTimestamp(cell);
				break;
			default:
		}
	}

	private boolean validateVarchar(String text, int length) throws CsvValidationException {
		if (text.length() > length) {
			throw new CsvValidationException("Data is too long for varchar column (length " + length + "): " + ParsingService.maskData(text), CsvValidationException.CATEGORY_TRUNCATE);
		}
		return true;
	}
	
	private void validateInteger(String cell) throws CsvValidationException {
		try{
			Integer.parseInt(cell);
		} catch (Exception e){
			int index = cell.indexOf('.');  
			if(index == -1){
				throw new CsvValidationException("Cannot parse integer: " + ParsingService.maskData(cell), CsvValidationException.CATEGORY_NON_NUMERIC);
			}else{
				throw new CsvValidationException("Cannot parse decimal: " + ParsingService.maskData(cell), CsvValidationException.CATEGORY_NON_NUMERIC);
			}
		}
	}
	
	private void validateDate(String date) throws CsvValidationException {
		try {
            // omx_load_xxx procedure truncate date values to 10 characters before attempting to parse them, so we
            // can do the same here to test if it is a valid format.
            if (date.length() > DATE_LENGTH) {
                date = date.substring(0, DATE_LENGTH);
            }
            dateFormat.parse(date);
		} catch (ParseException e) {
            throw new CsvValidationException("Value does not have a valid date format: " + ParsingService.maskData(date), CsvValidationException.CATEGORY_INVALID_DATE);
        }
	}
	
	private void validateDecimal(String cell, int precision, int scale) throws CsvValidationException {
		try{
			Float.parseFloat(cell);
		} catch (Exception e){
			throw new CsvValidationException("Cannot parse decimal: " + ParsingService.maskData(cell), CsvValidationException.CATEGORY_NON_NUMERIC);
		}
	}
	
	private void validateText(String cell) throws CsvValidationException {
		if (cell.length() > TEXT_LENGTH) {
			throw new CsvValidationException("Data is too long for text column (length " + TEXT_LENGTH + "): " + ParsingService.maskData(cell), CsvValidationException.CATEGORY_TRUNCATE);
		}
	}

	//2012-01-09T12:00:00Z
	private void validateTimestamp(String timestamp) throws CsvValidationException {
        try {
            // omx_load_observation function will ignore timestamp values less that 16 characters in length, so we
            // can do the same here to test if it is a valid format.
            if (timestamp.length() > TIMESTAMP_LENGTH) {
                timestamp = timestamp.substring(0, TIMESTAMP_LENGTH);
            }
            datetimeFormat.parse(timestamp);
        } catch (ParseException e) {
            throw new CsvValidationException("Value does not have a valid timestamp format: " + ParsingService.maskData(timestamp), CsvValidationException.CATEGORY_INVALID_DATE);
        }

	}
	
	public class ColumnSpec{
		String column;
		Types type;
		int length = 0;
		int precision = 0;
		int scale = 0;
		boolean required = false;
		
		ColumnSpec(String[] columnSpec){
			
			column = columnSpec[1];
			//System.out.print(columnSpec[2]);
			if("varchar".equals(columnSpec[2])) type = Types.VARCHAR;
			else if("integer".equals(columnSpec[2])) type = Types.INTEGER;
			else if("date".equals(columnSpec[2])) type = Types.DATE;
			else if("decimal".equals(columnSpec[2])) type = Types.DECIMAL;
			else if("text".equals(columnSpec[2])) type = Types.TEXT;
			else if("timestamp".equals(columnSpec[2])) type = Types.TIMESTAMP;
			else {
				RositaLogger.log(false, "Unknown column type in: " + columnSpec[0] + ", " + columnSpec[2]);
				RositaLogger.validationError("Unknown column type in " + columnSpec[0] + "." + columnSpec[1] + ": " + columnSpec[2] + ".", CsvValidationException.CATEGORY_UNKNOWN_COLUMN_TYPE);
				type = Types.VARCHAR;
			}
			
			if (type.equals(Types.VARCHAR)) {
				try{
					length = Integer.parseInt(columnSpec[3]);
				} catch (Exception e){
					RositaLogger.validationError("Could not get length for " + columnSpec[0] + "." + columnSpec[1] + ".", CsvValidationException.CATEGORY_MISSING_ATTRIBUTE);
				}
			}
			
			if (type.equals(Types.DECIMAL) || type.equals(Types.INTEGER)) {
				try{
					precision = Integer.parseInt(columnSpec[4]);
				} catch (Exception e){
					RositaLogger.validationError("Could not get precision for " + columnSpec[0] + "." + columnSpec[1] + ".", CsvValidationException.CATEGORY_MISSING_ATTRIBUTE);
				}
			}
			
			if (type.equals(Types.DECIMAL)) { //Integers always have a scale of zero
				try{
					scale = Integer.parseInt(columnSpec[5]);
				} catch (Exception e){
					RositaLogger.validationError("Could not get scale for " + columnSpec[0] + "." + columnSpec[1] + ".", CsvValidationException.CATEGORY_MISSING_ATTRIBUTE);
				}
			}
			
			required = false;
			try {
				String requiredString = columnSpec[6].toLowerCase();
				if (requiredString == null || (!"true".equals(requiredString.toLowerCase()) && !"false".equals(requiredString.toLowerCase()))) {
					RositaLogger.validationError("'Required' value for " + columnSpec[0] + "." + columnSpec[1] + " is not true or false - '" + requiredString + "'.", CsvValidationException.CATEGORY_MISSING_ATTRIBUTE);
				}
				required = Boolean.parseBoolean(requiredString);	
			}
			catch (ArrayIndexOutOfBoundsException e) {
				RositaLogger.validationError("'Required' value for " + columnSpec[0] + "." + columnSpec[1] + " is missing.", CsvValidationException.CATEGORY_MISSING_ATTRIBUTE);
			}
			

		}
	}
}