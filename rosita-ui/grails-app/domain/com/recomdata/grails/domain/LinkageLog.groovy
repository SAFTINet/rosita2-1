package com.recomdata.grails.domain

class LinkageLog {

     Long id    
     String personSourceValue
     Long xDataSourceId
     Date xEtlDate
     Long xRecordNum
     String logType
     String logMessage

     static mapping = {
        table 'cz.linkage_log'
        version false
        columns {
			id column: 'linkage_id'
		}
    }

    static constraints = {
		 personSourceValue (nullable: false, maxSize: 100 )
    }


}
