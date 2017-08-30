<!DOCTYPE HTML5>
<%@ page import="com.recomdata.grails.domain.MultiClinicDataSource" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout_main" />
        <title>Data Sources</title>
    </head>
    <body>
    		<g:set var="page" value="multiClinic" scope="request"/>
            <h3>Data Sources</h3>

			<g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
			
            <div class="list">
                <table class="striped" style="min-width: 500px">
                	<thead>
                		<tr>
                			<th></th>
                			<th>Name</th>
                			<th>Directory</th>
                			<th>Type</th>
                			<th>Schema file</th>
                			<th>Etl file</th>
                			<th>Preserve Data</th>
                			<th>&nbsp;</th>
                		</tr>
                	</thead>
                    <tbody>
                    <g:each in="${multiClinicDataSourceInstanceList}" status="i" var="multiClinicDataSourceInstance">
                        <tr>
                            <g:if test="${multiClinicDataSourceInstance.active}">
                            	<td><div align="center"><img src="${resource(dir:'images/icons',file:'check.png')}" width="16" height="16" align="middle"  title="Active"/></div></td>
                            </g:if>
                            <g:else>
                          		<td><div align="center"><img src="${resource(dir:'images/icons',file:'close_small.png')}" width="16" height="16" align="middle"  title="Inactive"/></div></td>
                            </g:else>	
                                <td><g:link action="show" id="${multiClinicDataSourceInstance.id}">${multiClinicDataSourceInstance.dataSourceName}</g:link></td>
                                <td>${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceDirectory")}</td>
                                <td>${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceType")}</td>
                                <td>${fieldValue(bean: multiClinicDataSourceInstance, field: "schemaPath")}</td>
                                <td>${fieldValue(bean: multiClinicDataSourceInstance, field: "etlRulesFile")}</td>
                                <g:if test="${multiClinicDataSourceInstance.incremental}">
                            		<td><div align="center"><img src="${resource(dir:'images/icons',file:'check.png')}" width="16" height="16" align="middle"  title="Incremental load"/></div></td>
	                            </g:if>
	                            <g:else>
	                          		<td><div align="center"><img src="${resource(dir:'images/icons',file:'close_small.png')}" width="16" height="16" align="middle"  title="No incremental load"/></div></td>
	                            </g:else>	
                                <td style="width: 40px;"><a href="${createLink([action:'show',controller:'multiClinicDataSource',id:multiClinicDataSourceInstance.id])}"><img src="${resource(dir:'images/icons',file:'detail.png')}" width="16" height="16"/></a>
                                <a href="${createLink([action:'edit',controller:'multiClinicDataSource',id:multiClinicDataSourceInstance.id])}"><img src="${resource(dir:'images/icons',file:'pencil_edit.png')}" width="16" height="16"/></a></td>
                           
                        </tr>
                    </g:each>
                    <g:if test="${!multiClinicDataSourceInstanceList}">
                    	<tr>
                    		<td colspan="8" style="text-align: center;"><i>No data sources have been set up.</i></td>
                    	</tr>
                    </g:if>
                    <tr><td colspan="8" style="text-align: center;"><a class="anchor" href="${createLink([action:'create', controller:'multiClinicDataSource'])}">Add new</a></td></tr>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${multiClinicDataSourceInstanceTotal}" />
            </div>
        
        <div class="bottombar">
			<div class="bigbutton">
				<a href="${createLink([action:'index', controller:'rositaJob'])}"><div style="cursor: pointer;" class="stepicon back">Job list</div></a>
			</div>
			<div class="bigbutton" style="width: 250px">
				<a href="${createLink([action:'create', controller:'multiClinicDataSource'])}"><div style="cursor: pointer;" class="stepicon newdatasource">New data source</div></a>
			</div>
		</div>
    </body>
</html>
