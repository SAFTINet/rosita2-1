package com.recomdata.grails.domain

class ValidationRulesController {

    def index = {
		def validationRules = ValidationErrorHandler.listOrderByName();
		
		return [rules: validationRules]
	}
	
	def update = {
		def paramMap = params
		def validationRules = ValidationErrorHandler.listOrderByName();
		
		for (rule in validationRules) {
			rule.allow = Boolean.parseBoolean(paramMap."${rule.name}")
			rule.save()
		}
		
		flash.message = "Validation rules have been saved"
		redirect(action: 'index')
	}
}
