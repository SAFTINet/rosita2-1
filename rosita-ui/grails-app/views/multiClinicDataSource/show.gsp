
<%@ page import="com.recomdata.grails.domain.MultiClinicDataSource" %>
<html xmlns="http://www.w3.org/1999/html">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout_main" />
        <title>${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceName")}</title>
    </head>
    <body>
    	<g:set var="page" value="multiClinic" scope="request"/>
        <div class="body">
            <h3>${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceName")}</h3>

            <div class="dialog">
                <table class="striped">
                    <tbody>                    
                        <tr class="prop">
                            <td valign="top">Name</td>
                            
                            <td valign="top">${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceName")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top">Data Directory</td>
                            
                            <td valign="top">${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceDirectory")}</td>
                            
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top">Incremental load</td>
                            <td valign="top">
                            	<g:if test="${multiClinicDataSourceInstance.incremental}">
                            		<div align="left"><img src="${resource(dir:'images/icons',file:'check.png')}" width="16" height="16" align="middle"  title="Incremental load"/></div>
	                            </g:if>
	                            <g:else>
	                          		<div align="left"><img src="${resource(dir:'images/icons',file:'close_small.png')}" width="16" height="16" align="middle"  title="No incremental load"/></div>
	                            </g:else>
							</td>
                        </tr>
                       
                        <tr class="prop">
                          	<td valign="top">Data Source Type</td>
                          	<td valign="top">${fieldValue(bean: multiClinicDataSourceInstance, field: "dataSourceType")}</td>
                       	 </tr>
                     
                        <g:if test="${multiClinicDataSourceInstance.dataSourceType == 'CLAIMS'}">
	                         <tr class="prop">
	                            <td valign="top">Data Linkage</td>
	                            <td valign="top">${fieldValue(bean: multiClinicDataSourceInstance, field: "linkageType")}</td>
	                         </tr>
                        </g:if>
                     	<tr class="prop">
                            <td valign="top">Schema file to use</td>
                            <td valign="top"><span id="schemaPathDisplay">${fieldValue(bean: multiClinicDataSourceInstance, field: "schemaPath")}</span><span id="schemaFileOK" style="display: none"><tmpl:/ui/checkmark test="${true}"/></span></td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top">ETL rules file to use</td>
                            <td valign="top"><span id="etlRulesFilePathDisplay">${fieldValue(bean: multiClinicDataSourceInstance, field: "etlRulesFile")}</span><span id="etlRulesFileOK" style="display: none"><tmpl:/ui/checkmark test="${true}"/></span></td>
                        </tr>
                     
                        <tr class="prop">
                            <td valign="top">Delimiter in data files</td>
                            
                            <td valign="top">${fieldValue(bean: multiClinicDataSourceInstance, field: "delimiter")}</td>
                            
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top">Quote character in data files</td>
                            
                            <td valign="top">${fieldValue(bean: multiClinicDataSourceInstance, field: "quoteCharacter")}</td>
                            
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top">Column names in data files</td>
                            <g:set var="columnNames">${fieldValue(bean: multiClinicDataSourceInstance, field: "firstRowType")}</g:set>
                            <g:if test="${!columnNames}"><g:set var="columnNames">No column names</g:set></g:if>
                            <g:elseif test="${columnNames.equals('MAP')}"><g:set var="columnNames">Map column names to schema</g:set></g:elseif>
                            <g:elseif test="${columnNames.equals('IGNORE')}"><g:set var="columnNames">Ignore column names</g:set></g:elseif>
                            
                            <td valign="top">${columnNames}</td>
                        </tr>
                        
                        <tr class="prop">
                            <td valign="top">Enabled</td>
                            <td valign="top">
	                           	<g:if test="${multiClinicDataSourceInstance.active}">
	                           		<div align="left"><img src="${resource(dir:'images/icons',file:'check.png')}" width="16" height="16" align="middle"  title="Enabled"/></div>
	                           	</g:if>
	                           	<g:else>
	                           		<div align="left"><img src="${resource(dir:'images/icons',file:'close_small.png')}" width="16" height="16" align="middle"  title="Disabled"/></div>
	                           	</g:else>	
	                         </td>  	
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            
            <br />
            
            <div id="schemaPreview">&nbsp;</div>
            
            <div class="bottombar">
	            <div class="bigbutton">
					<a href="${createLink([action:'list'])}"><div style="cursor: pointer;" class="stepicon back">Data source list</div></a>
				</div>
				<div class="bigbutton">
					<a href="${createLink([action:'edit', id: multiClinicDataSourceInstance?.id])}"><div style="cursor: pointer;" class="stepicon editdatasource">Edit</div></a>
				</div>
				<g:if test="${multiClinicDataSourceInstance.active}">
					<div class="bigbutton">
						<a href="${createLink([action:'deactivate', id: multiClinicDataSourceInstance?.id])}" onclick="return confirm('Are you sure?');"><div style="cursor: pointer;" class="stepicon failed">Disable</div></a>
					</div>
				</g:if>
                <g:else>
					<div class="bigbutton">
						<a href="${createLink([action:'activate', id: multiClinicDataSourceInstance?.id])}" ><div style="cursor: pointer;" class="save bigbuttoninput">Enable</div></a>
					</div>
				 </g:else>	
			</div>
	
	        <g:javascript>
	        	$j(document).ready(function() {
					updateSchemaPreview();
					checkEtlFileOK();
				});
		    </g:javascript>
        </div>
    </body>
</html>
