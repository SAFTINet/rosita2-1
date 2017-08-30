package com.recomdata.grails.domain

class LinkageResultController {

      def index = {
        redirect(action: "list", params: params)
      }

	  def list = {

		  params.max = params.max ?: 100
		  
	      def linkageResults = LinkageResult.createCriteria().list([max: params.max, offset: params.offset]) {order('id', 'asc')}
	      [linkageResultDataSourceList: linkageResults ,linkageResultDataSourceTotal: linkageResults.getTotalCount()]
	}
}
