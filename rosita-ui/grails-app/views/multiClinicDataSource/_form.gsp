<%@ page import="com.recomdata.grails.rositaui.service.RositaPropertiesService" %>
<%
    def rositaPropertiesService = grailsApplication.classLoader.loadClass('com.recomdata.grails.rositaui.service.RositaPropertiesService').newInstance()
%>
                <div class="dialog">
                    <table class="striped" style="width: 710px">
                    	<thead>
                            <tr><th colspan="2">Data Source Information</th></tr>
                        </thead>
                        <tbody>

                            <tr>
                                <td valign="top">
                                  <label for="dataSourceName">Name</label>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'dataSourceName', 'errors')}>
                                    <g:textField style="width: 400px" name="dataSourceName" value="${multiClinicDataSourceInstance?.dataSourceName}" class="inputWidth"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="dataSourceName">Enabled</label>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'active', 'errors')}>
                                	<div class="active"><input type="radio" ${multiClinicDataSourceInstance?.active ? 'checked="checked"' : ''} name="active" value="true"/> <tmpl:/ui/checkmark test="${true}"/> Enabled</div>
                                	<div class="active"><input type="radio" ${!multiClinicDataSourceInstance?.active ? 'checked="checked"' : ''} name="active" value="false"/> <tmpl:/ui/checkmark test="${false}" showFail="true"/> Disabled</div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="dataSourceType">Data Source Type</label>                                  
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'dataSourceDirectory', 'errors')} >
                                	<g:select name="dataSourceType" from="${[CLINICAL:'Clinical', CLAIMS:'Claims']}" optionKey="key" optionValue="value" value="${multiClinicDataSourceInstance?.dataSourceType}" class="selectWidth"/>
                                </td> 
                            </tr>
                            
                            <tr id="dataLinkageRow" style="display: none;">
                                <td valign="top">
                                  <label for="linkageField">Data Linkage</label>                                  
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'linkageField', 'errors')} >
                                	<div class="linkageType"><input type="radio" ${!(multiClinicDataSourceInstance?.linkageType) ? 'checked="checked"' : ''} name="linkageType" value=""/> No linkage</div>
                                	<div class="linkageType"><input type="radio" ${multiClinicDataSourceInstance?.linkageType.equals('DETERMINISTIC') ? 'checked="checked"' : ''} name="linkageType" value="DETERMINISTIC"  id="linkageTypeId" /> Deterministic linkage on field: <g:textField name="linkageField" value="${multiClinicDataSourceInstance?.linkageField}" /></div>
                                	<div class="linkageType"><input type="radio" ${multiClinicDataSourceInstance?.linkageType.equals('PROBABILISTIC') ? 'checked="checked"' : ''} name="linkageType" value="PROBABILISTIC"/> Probabilistic linkage</div>
                                	<div class="linkageType"><input type="radio" ${multiClinicDataSourceInstance?.linkageType.equals('PPRL') ? 'checked="checked"' : ''} name="linkageType" value="PPRL"/> PPRL linkage</div>
                                </td> 
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="dataSourceDirectory">Data Directory</label>
                                  <div class="texttiny">Looking in folder: ${rositaPropertiesService.get('folder.datasources')}</div>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'dataSourceDirectory', 'errors')} >
                                	<g:select name="dataSourceDirectory" from="${thedirs}" value="${multiClinicDataSourceInstance?.dataSourceDirectory}" class="selectWidth"/>
                                </td> 
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="incremental">Preserve data from previous load</label>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'incremental', 'errors')}>
                                    <g:checkBox name="incremental" value="${multiClinicDataSourceInstance?.incremental}"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="schemaPath">Schema to use</label>
                                  <div style="text-align: center; padding: 8px; border-top: 1px solid #DDD"><img src="${resource([file:'csv_to_raw.png', dir:'images'])}"/></div>
                                  <div class="texttiny">A schema that describes the tables and columns in the source files, used for loading the files to the raw database schema.</div>
                                  <br/>
                                  <div class="texttiny">Looking in folder: ${rositaPropertiesService.get('folder.schemas')}</div>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'schemaPath', 'errors')}>
                                	<g:select name="schemaPath" from="${schemaFiles}" noSelection="${['':'(Select...)']}" value="${multiClinicDataSourceInstance?.schemaPath}" class="selectWidth"/><span id="schemaFileOK" style="display: none"><tmpl:/ui/checkmark test="${true}"/></span>
                                </td>
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="schemaPath">ETL rules file to use</label>
                                  <div style="text-align: center; padding: 8px; border-top: 1px solid #DDD"><img src="${resource([file:'raw_to_std.png', dir:'images'])}"/></div>
                                  <div class="texttiny">A rules file that describes how to process the raw data into the standard tables.</div>
                                  <br/>
                                  <div class="texttiny">Looking in folder: ${rositaPropertiesService.get('folder.etlrules')}</div>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'etlRulesFile', 'errors')}>
                                	<g:select name="etlRulesFile" from="${rulesFiles}" noSelection="${['':'(Select...)']}" value="${multiClinicDataSourceInstance?.etlRulesFile}" class="selectWidth"/><span id="etlRulesFileOK" style="display: none"><tmpl:/ui/checkmark test="${true}"/></span>
                                </td>
                            </tr>
                            
                            <tr><th colspan="2">CSV Settings</th></tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="delimiter">Delimiter in data files</label>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'delimiter', 'errors')}>
                                    <g:textField name="delimiter" value="${multiClinicDataSourceInstance?.delimiter}" style="width: 20px;"  maxlength="1" />
                                </td>
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="quoteCharacter">Quote character in data files</label>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'quoteCharacter', 'errors')}>
                                    <g:textField name="quoteCharacter" value="${multiClinicDataSourceInstance?.quoteCharacter}" style="width: 20px;"  maxlength="1"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td valign="top">
                                  <label for="firstRowType">Column names in data files</label>
                                </td>
                                <td valign="top" ${hasErrors(bean: multiClinicDataSourceInstance, field: 'firstRowType', 'errors')} >
                                	<div class="firstRowType"><input type="radio" ${!(multiClinicDataSourceInstance?.firstRowType) ? 'checked="checked"' : ''} name="firstRowType" value=""/> No column names in data files</div>
                                	<div class="firstRowType"><input type="radio" ${multiClinicDataSourceInstance?.firstRowType.equals('MAP') ? 'checked="checked"' : ''} name="firstRowType" value="MAP"/> Data files contain column names - use these to map to columns in the schema</div>
                                	<div class="firstRowType"><input type="radio" ${multiClinicDataSourceInstance?.firstRowType.equals('IGNORE') ? 'checked="checked"' : ''} name="firstRowType" value="IGNORE"/> Data files contain column names - ignore them and load the schema columns in order from left to right</div>
                                </td> 
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                
                <g:javascript>
		        	$j(document).ready(function() {
				        $j('#dataSourceType').bind('change', function() {
				        	showClaimsPanel(false);
				        });
				        showClaimsPanel(true);
				    });
				    
				    var showClaimsPanel = function(noFade) {
				        
					    if ($j('#dataSourceType').val() == 'CLAIMS') {
					    	if (noFade) {
				        		$j('#dataLinkageRow').show();
				        	}
				        	else {
				        		$j('#dataLinkageRow').fadeIn(500);
				        	}
			        	}
			        	else {
				        	$j('#dataLinkageRow').fadeOut(500);
			        	}
		        	}
                </g:javascript>