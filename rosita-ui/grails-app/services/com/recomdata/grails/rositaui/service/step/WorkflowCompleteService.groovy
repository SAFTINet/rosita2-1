package com.recomdata.grails.rositaui.service.step

import com.recomdata.grails.domain.RositaJob;
import com.recomdata.grails.domain.WorkflowStepInstance;
import com.recomdata.grails.rositaui.thread.VerifierRunnable
import org.codehaus.groovy.grails.commons.ConfigurationHolder;

class WorkflowCompleteService {

    static transactional = true

	def getStatus = {
		return [:]
	}
	
	def start(paramMap) {
		RositaJob j = paramMap.job;
		WorkflowStepInstance wf = paramMap.step;
		j.endDate = new Date()
		j.save(flush: true)
		
		wf.state = 'completed';
		wf.save(flush: true)
	}
	
	def cancel() {
	}
}
