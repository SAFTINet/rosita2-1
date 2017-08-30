

<%@ page import="com.recomdata.grails.domain.MultiClinicDataSource" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout_main" />
        <title>Edit Data Source</title>
    </head>
    <body>
    	<g:set var="page" value="multiClinic" scope="request"/>
        <div class="body">
            <h3>Edit Data Source</h3>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${multiClinicDataSourceInstance}">
            <div class="errors">
                <g:renderErrors bean="${multiClinicDataSourceInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${multiClinicDataSourceInstance?.id}" />
                <g:hiddenField name="version" value="${multiClinicDataSourceInstance?.version}" />
				
				<tmpl:form />
                
                <br/>
                <div id="schemaPreview">&nbsp;</div>
            
        </div>
        
        <div class="bottombar">
            <div class="bigbutton">
				<a href="${createLink([action:'list', id: multiClinicDataSourceInstance?.id])}" onclick="return confirm('Are you sure you want to cancel your changes to this data source?')"><div style="cursor: pointer;" class="stepicon back">Cancel</div></a>
			</div>
			
			<div class="bigbutton">
				<g:actionSubmit class="stepicon failed bigbuttoninput" id="deleteBtn" action="delete" onclick="return confirm('Are you sure you want to delete this data source?');"  value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
			</div>
			
			<div class="bigbutton" id="createTablesDiv" style="display:none;">
				<span onclick="createTables();"><div style="cursor: pointer;" class="stepicon createtable">Create tables</div></span>
			</div>
			
			<div class="bigbutton">
				<span onclick="checkForName();" class="save bigbuttoninput" >Update</span>
				<g:actionSubmit id="updateBtn" action="update" style="visibility:hidden;" value="${message(code: 'default.button.update.label', default: 'Update')}" />
			</div>
		</div>
		
		</g:form>
        <g:javascript>
        	$j(document).ready(function() {
		        $j('#schemaPath').bind('change', function() { 
					if ($j('#createTablesDiv:visible').size() > 0) { $j('#createTablesDiv').fadeOut(500); }
					if ($j('#schemaFileOK:visible').size() > 0) { $j('#schemaFileOK').fadeOut(500); }
				    updateSchemaPreview();
				});
				$j('#etlRulesFile').bind('change', function() {
					if ($j('#etlRulesFileOK:visible').size() > 0) { $j('#etlRulesFileOK').fadeOut(500); }
				    checkEtlFileOK();
				});
				checkEtlFileOK();
				updateSchemaPreview();
			});
			
			function createTables() {
				var conf = confirm('Are you sure? This will drop and recreate all tables in the raw schema for this file.');
				if (conf) {
					$j('#schemaPreview').hide();
					$j.ajax({
						url: '/rosita/multiClinicDataSource/recreateTables',
						type: 'GET',
						data: {path: $j('#schemaPath').val()},
						success: function(msg) {
							$j('#schemaPreview').html(msg);
							$j('#schemaPreview').fadeIn(500);
							alert('The tables for this schema have been created.');
						},
						error: function(xhr) {
							alert(xhr.responseText);
						}
					});
				}
			}
			
			function checkForName() {  
				var checkFlag = true; 
				var dataSrcName = $j('#dataSourceName').val(); 
				if($j.trim(dataSrcName) == ""){
					alert("Please enter a data source name.");
					checkFlag = false;
					return false;
				}
				
				if($j('#schemaPath').val() == ""){
				 	alert("Please select schema to use.");
				    checkFlag = false;
				    return false;
				}
				
				if($j('#etlRulesFile').val() == ""){
					alert("Please select ETL rule file to use.");
					checkFlag = false;
					return false;
				}
				
				if(checkForCSVFile())
				{
					if(checkFlag){
						$j('#updateBtn').css("visibility", "visible");
						$j("#updateBtn").trigger("click");
						$j('#updateBtn').css("visibility", "hidden");
					}
				}
			}
			
			function checkForCSVFile()
			{
				if(checkForDataSourceType())
				{
					var fileExtn =  $j('#schemaPath').val()
					var extension = fileExtn.substr( (fileExtn.lastIndexOf('.') +1) );
					
				 	if(extension === "csv" || extension === "CSV")
				 	{
						if($j('#delimiter').val() == " " || $j('#delimiter').val() == ""){
							alert("Please add delimiter.");
							checkFlag = false;
							return false;
						}
						
						if($j('#quoteCharacter').val() == " " || $j('#quoteCharacter').val() == "" ){
							alert("Please add quote character.");
							checkFlag = false;
							return false;
						}
					}
					return true;
				}
			}
			
			function checkForDataSourceType()
			{
				if($j('#dataSourceType').val() === "CLAIMS"){ 
				
					if($j('#linkageTypeId').is(':checked')){
					
						var linkageFieldvalue = $j('#linkageField').val();
						
						if($j.trim(linkageFieldvalue) == ""){
								alert("Please add deterministic linkage on field value.");
								return false;
						}
					}
				}
				return true;
			}
			
	    </g:javascript>
    </body>
</html>
