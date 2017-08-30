<g:if test="${workflowStep == '1'}">

	<g:if test="${messageType == 'SUCCESS'}">
		${latestOutput}
	</g:if>
	<g:else>
		Verifying files exist...<br/>
	</g:else>
	
	<g:if test="${clinicTable && clinicTable.size() > 1}"><tmpl:clinicTable clinicTable="${clinicTable}"/></g:if>

</g:if>
<g:elseif test="${workflowStep == '2'}">

	<g:if test="${messageType == 'COMPLETE'}">
		Validation complete, gathering results...
	</g:if>
	<g:elseif test="${messageType == 'STATUS'}">
		<g:if test="${maxClinics && (Integer.parseInt(maxClinics) > 1)}">
			<tmpl:bar numerator="${currentClinic}" denominator="${maxClinics}" color="blue" label="Validating data source"/>
		</g:if>
		<tmpl:bar numerator="${elements}" denominator="${totalElements}" color="green" label="Records validated"/>
	</g:elseif>
	
	<g:if test="${clinicTable && clinicTable.size() > 1}"><tmpl:clinicTable clinicTable="${clinicTable}"/></g:if>

</g:elseif>
<g:elseif test="${workflowStep == '3'}">

	<g:if test="${messageType == 'SUCCESS'}">
		Load complete, gathering results...
	</g:if>
	<g:elseif test="${taskType == 'CLEANUP'}">
		<tmpl:bar numerator="${currentClinic}" denominator="${maxClinics}" color="blue" label="Cleaning source tables"/>
	</g:elseif>
	<g:elseif test="${taskType == 'TRUNCATE'}">
		<g:if test="${maxClinics && (Integer.parseInt(maxClinics) > 1)}">
			<tmpl:bar numerator="${currentClinic}" denominator="${maxClinics}" color="blue" label="Deleting previous data"/>
		</g:if>
		<tmpl:bar numerator="${tables}" denominator="14" color="green" label="Deleted"/>
	</g:elseif>
	<g:elseif test="${taskType == 'VACCUUM'}">
		<tmpl:bar numerator="${currentTableNumber}" denominator="${maxTables}" color="green" label="Vaccuumed"/>
		<div class="stepstatistic">Vaccuuming table: ${currentTable}</div>
	</g:elseif>
	<g:elseif test="${messageType == 'STATUS'}">
		<g:if test="${maxClinics && (Integer.parseInt(maxClinics) > 1)}">
			<tmpl:bar numerator="${currentClinic}" denominator="${maxClinics}" color="blue" label="Loading data source"/>
		</g:if>
		<tmpl:bar numerator="${elements}" denominator="${totalElements}" color="green" label="Records loaded"/>
	</g:elseif>
	
	<g:if test="${clinicTable && clinicTable.size() > 1}"><tmpl:clinicTable clinicTable="${clinicTable}"/></g:if>

</g:elseif>
<g:elseif test="${workflowStep == '4'}">

	<g:if test="${messageType == 'COMPLETE'}">
		Profiling complete, gathering results...
	</g:if>
	<g:elseif test="${messageType == 'STATUS'}">
		<tmpl:bar numerator="${latestOutput}" denominator="15" color="green" label="Tables profiled"/>
	</g:elseif>

</g:elseif>
<g:elseif test="${workflowStep == '8'}">

	<g:if test="${messageType == 'COMPLETE'}">
		Processing complete, gathering results...
	</g:if>
	<g:elseif test="${messageType == 'STATUS'}">
		<tmpl:bar numerator="${latestOutput}" denominator="15" color="green" label="OMOP processing functions run"/>
	</g:elseif>

</g:elseif>
<g:elseif test="${workflowStep == '9'}">

	<g:if test="${messageType == 'COMPLETE'}">
		Profiling complete, gathering results...
	</g:if>
	<g:elseif test="${messageType == 'STATUS'}">
		<tmpl:bar numerator="${latestOutput}" denominator="18" color="green" label="OMOP tables profiled"/>
	</g:elseif>

</g:elseif>
<g:elseif test="${workflowStep == '11'}">

	<g:if test="${messageType == 'SUCCESS'}">
		Publish complete, gathering results...
	</g:if>
	<g:elseif test="${messageType == 'STATUS'}">
		<g:if test="${latestOutput == 'TRUNCATE'}">
			Truncating...
		</g:if>
		<g:else>
			<tmpl:bar numerator="${currentRule}" denominator="${maxRules}" color="blue" label="Running publish rule"/>
			<div class="stepstatistic">Objects published: ${objectsDone}</div>
		</g:else>
	</g:elseif>
	<g:elseif test="${messageType == 'WAITINGSTATUS' }">
		Waiting for the Grid database to indicate ${transitionState}. Will wait ${secondsLeft} more seconds...
		<br/>
	</g:elseif>

</g:elseif>
<g:elseif test="${workflowStep == '12'}">
	Backing up...
</g:elseif>
<g:elseif test="${workflowStep == '14'}">

	<g:if test="${taskType == 'VACCUUM'}">
		<tmpl:bar numerator="${currentTableNumber}" denominator="${maxTables}" color="green" label="Vaccuumed"/>
		<div class="stepstatistic">Vaccuuming table: ${currentTable}</div>
	</g:if>
	<g:else>
		Processing to standard tables - check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for progress.
	</g:else>

</g:elseif>

<g:else>
	Running... check the <g:link class="anchor" controller="rositaLog" action="list" id="${stepId}">message log</g:link> for progress.
</g:else>

<g:if test="${estimateMs}">
	<br/>
	<div class="texttiny"><b>Estimated time remaining:</b> <g:timeEstimateFromMs ms="${estimateMs}"/></div>
</g:if>