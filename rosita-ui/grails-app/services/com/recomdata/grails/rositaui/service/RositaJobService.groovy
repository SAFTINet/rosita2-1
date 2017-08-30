package com.recomdata.grails.rositaui.service

import com.recomdata.grails.domain.RositaJob;

class RositaJobService {

    static transactional = true

    def getCurrentJob() {
		
		def jobs = RositaJob.createCriteria().list ([max: 10]){
			order('id', 'desc')
		}
		def latestWorkflowStep = null;
		def jobInProgress = false;
		if (jobs) {
			def firstJob = jobs.get(0);
			jobInProgress = firstJob.endDate ? false : true
			if (jobInProgress) {
				return firstJob
			}
			return null
		}

		return null
    }
}
