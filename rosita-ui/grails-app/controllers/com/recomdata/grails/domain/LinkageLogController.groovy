package com.recomdata.grails.domain

class LinkageLogController {

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
		
		 params.max = params.max ?: 100		 
         def linkageLogList = LinkageLog.createCriteria().list([max: params.max, offset: params.offset]) {order('id', 'asc')}
         
         [linkageLogDataSourceList: linkageLogList, linkageLogDataSourceTotal: linkageLogList.getTotalCount()]
    }

}
