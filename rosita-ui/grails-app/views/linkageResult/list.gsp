<!DOCTYPE HTML5>
<%@ page import="com.recomdata.grails.domain.LinkageResult" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="layout_main" />
        <title>Linkage Result Details</title>
    </head>
    <body>
    		<g:set var="page" value="linkageResult" scope="request"/>
            <h3>Linkage Result Details</h3>

			<g:if test='${flash.message}'><div class='flashmessage'>${flash.message}</div></g:if>
			
            <div class="list">
                <table class="striped" style="min-width: 500px">
                	<thead>
                		<tr>
                			<th>Person Source A</th>
                			<th>Data Source A</th>
                			<th>Person Source B</th>
                			<th>Data Source B</th>
                			<th>Confidence</th>
                		</tr>
                	</thead>
                    <tbody>
                    <g:each in="${linkageResultDataSourceList}" status="i" var="linkageResultDataSource">
                        <tr>
                            <td>${fieldValue(bean: linkageResultDataSource, field: "personSourceValueAa")}</td>
                            <td>${fieldValue(bean: linkageResultDataSource, field: "xDataSourceIdAa")}</td>
                            <td>${fieldValue(bean: linkageResultDataSource, field: "personSourceValueBb")}</td>
                            <td>${fieldValue(bean: linkageResultDataSource, field: "xDataSourceIdBb")}</td>
                            <td>${fieldValue(bean: linkageResultDataSource, field: "confidence")}</td>
                          
                        </tr>
                    </g:each>
                    <g:if test="${!linkageResultDataSourceList}">
                    	<tr>
                    		<td colspan="5" style="text-align: center;"><i>There are no entries in the linkage results table. This table is truncated before each record linkage step.</i></td>
                    	</tr>
                    </g:if>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${linkageResultDataSourceTotal}" />
            </div>
    </body>
</html>
