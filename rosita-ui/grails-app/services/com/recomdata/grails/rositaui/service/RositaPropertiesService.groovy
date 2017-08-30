package com.recomdata.grails.rositaui.service

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class RositaPropertiesService {

    static transactional = false

	def props = null;
    
	def initialize() {
		if (props == null) {
			props = new Properties();
			props.load(new FileInputStream(ConfigurationHolder.config.rosita.jar.path + "/rosita.properties"))
		}
	}
	
	def get(String prop) {
		initialize();
		String result = props.get(prop);
		if (!result) {
			println("WARNING: Property " + prop + " is missing or blank")
		}
		return result
	}
	
	def getAll() {
		initialize();
		return props;
	}
	
	def refresh() {
		props = new Properties();
		props.load(new FileInputStream(ConfigurationHolder.config.rosita.jar.path + "/rosita.properties"))
	}
	
}
