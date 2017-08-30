package com.recomdata.grails.domain

class MultiClinicDataSource {
    Long    id
    String  dataSourceName
    String  dataSourceDirectory
    String  fileType
    Boolean active
	String  schemaPath
	boolean incremental
	String  delimiter
	String  firstRowType
	String  quoteCharacter
	String  etlRulesFile
	String  dataSourceType
	String  linkageField
	String  linkageType

    static mapping = {
        table 'cz.cz_data_source'
        version false
        id column:'x_data_source_id', generator: 'sequence', params:[sequence:'cz.cz_data_source_sq']

        sort id: 'asc'
    }

    static constraints = {
        dataSourceName      (nullable: false, maxSize: 200)
        dataSourceDirectory (nullable: false, maxSize: 200)
        active              (nullable: false)
        fileType            (nullable: true)
		schemaPath          (nullable: true)
		delimiter           (nullable: true)
		quoteCharacter      (nullable: true)
		etlRulesFile		(nullable: true)
		dataSourceType		(nullable: true)
		linkageField		(nullable: true)
		linkageType			(nullable: true)
		firstRowType		(nullable: true)
    }

    public String toString() {
        return dataSourceDirectory
    }
	
	public void setLinkageField(String field) {
		this.linkageField = field?.toLowerCase();
	}
}
