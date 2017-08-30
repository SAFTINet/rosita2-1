package com.recomdata.grails.domain

class LinkageResult {

       Long id
       String personSourceValueAa
       Long xDataSourceIdAa
       String personSourceValueBb
       Long xDataSourceIdBb
       Long confidence


      static mapping  = {
		table  'cz.linkage_result'
		version false
		columns {
			id column: 'linkage_id'
			personSourceValueAa column: 'person_source_value_a'
			personSourceValueBb column: 'person_source_value_b'
			xDataSourceIdAa column: 'x_data_source_id_a'
			xDataSourceIdBb column: 'x_data_source_id_b'
		}
	  }
}
