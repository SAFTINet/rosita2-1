package com.recomdata.grails.domain

class ValidationErrorHandler {

    String name
	boolean allow
	String parameters
	String description
	String allowMessage
	
    static mapping = {
        table 'cz.validation_error_handler'
        version false
		columns {
			id column: 'validation_error_handler_id', generator: 'sequence', params:[sequence:'cz.cz_sq']
		}
    }
	
}
