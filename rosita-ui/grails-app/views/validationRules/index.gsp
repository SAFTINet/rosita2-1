<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout_main" />
        <title>Edit Validation Rules</title>
    </head>
    
    <body>
    	<g:set var="page" value="validationRules" scope="request"/>
            <h3>Edit Validation Rules</h3>
            <g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
            <g:form method="post" >
                <div class="dialog">
                    <table class="striped" style="border-collapse: collapse;">
                    	<thead>
                    		<tr>
                    			<th>Validation error type</th>
                    			<th>Action to take</th>
                    		</tr>
                    	</thead>
                        <tbody>
                        	<g:each in="${rules}" var="rule">
								<tmpl:ruleSelectionRow rule="${rule}" />
							</g:each>
                        </tbody>
                    </table>
                </div>
                
                <br/>
                <div id="schemaPreview">&nbsp;</div>

        
        <div class="bottombar">
            <div class="bigbutton">
				<a href="${createLink([action:'index', controller: 'rositaJob'])}"><div style="cursor: pointer;" class="stepicon back">Job list</div></a>
			</div>
			<div class="bigbutton">
				<g:actionSubmit class="save bigbuttoninput" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
			</div>
		</div>
		
		</g:form>
    </body>
</html>
