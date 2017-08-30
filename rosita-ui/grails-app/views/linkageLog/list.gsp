<!DOCTYPE HTML5>
<%@ page import="com.recomdata.grails.domain.LinkageLog" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout_main" />
        <title>Linkage Log Details</title>
    </head>
    <body>
    		<g:set var="page" value="linkageLog" scope="request"/>
            <h3>Linkage Log Details</h3>

			<g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
			
            <div class="list">
                <table class="striped" style="min-width: 500px">
                	<thead>
                		<tr>
                			<th></th>
                			<th>Log Type</th>
                			<th>Date</th>
                			<th>Data Source ID</th>
                			<th>Person Source Value</th>
                			<th>Log Message</th>
                		</tr>
                	</thead>
                    <tbody>
                    <g:each in="${linkageLogDataSourceList}" status="i" var="linkageLogDataSource">
                        <tr>
                       			<td><tmpl:/ui/statusicon status="${linkageLogDataSource.logType}"/></td>
                               <td>${fieldValue(bean: linkageLogDataSource, field: "logType")}</td>
                               <td>${fieldValue(bean: linkageLogDataSource, field: "xEtlDate")}</td>
                               <td>${fieldValue(bean: linkageLogDataSource, field: "xDataSourceId")}</td>
                               <td>${fieldValue(bean: linkageLogDataSource, field: "personSourceValue")}</td>
                               <td>${fieldValue(bean: linkageLogDataSource, field: "logMessage")}</td>
                        </tr>
                    </g:each>
                    <g:if test="${!linkageLogDataSourceList}">
                    	<tr>
                    		<td colspan="6" style="text-align: center;"><i>There are no entries in the linkage log table.</i></td>
                    	</tr>
                    </g:if>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${linkageLogDataSourceTotal}" />
            </div>
    </body>
</html>
