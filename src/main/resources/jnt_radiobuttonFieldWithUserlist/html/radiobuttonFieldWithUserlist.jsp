<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="user" uri="http://www.jahia.org/tags/user" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<template:addResources type="javascript" resources="jquery.min.js"/>
<template:addResources type="javascript" resources="jquery.filtertable.js" />

<script type='text/javascript'>
  //$(document).ready($('#table').filterTable());

  function onChangeToPrivateHandler() {
    
    if ($('#visibility:checked').val() == "private") {
     	$('#btnuserlist').fadeIn();
    } else {
        $('#btnuserlist').fadeOut();
    }
  }
</script>



<jcr:sql var="listQuerySql"
         sql="select * from [jnt:user] as result 
                  where result.[j:firstName] LIKE '_%'
              	  and   result.[j:lastName] LIKE '_%'
                  and	result.[j:publicProperties] LIKE '%j:firstName%'
                  and	result.[j:publicProperties] LIKE '%j:lastName%'
              order by lower(result.[j:lastName]) ASC"/>


<c:set var="required" value=""/>
<c:if test="${jcr:hasChildrenOfType(currentNode, 'jnt:required')}">
    <c:set var="required" value="required"/>
</c:if> 

<label class="left" for="${currentNode.name}">${currentNode.properties['jcr:title'].string}</label>
<div class="formMarginLeft">
  
    <c:set var="counter" value="0"/>
    <c:set var="defaultValue" value="${currentNode.properties['jcr:defaultValue'].long}"/>
  	<c:if test="${defaultValue gt (fn:length(jcr:getNodes(currentNode,'jnt:formListElement'))-1)}">
      <c:set var="defaultValue" value="0"/>
    </c:if>
  
    <c:forEach items="${jcr:getNodes(currentNode,'jnt:formListElement')}" var="option">
      <c:choose>
        <c:when test="${counter == defaultValue}">
          <input ${disabled} type="radio" onchange="onChangeToPrivateHandler();" ${required} class="${required}" name="${currentNode.name}" id="${currentNode.name}" value="${option.name}" checked="true" />
        </c:when>
        <c:otherwise>
           <input ${disabled} type="radio" onchange="onChangeToPrivateHandler();" ${required} class="${required}" name="${currentNode.name}" id="${currentNode.name}" value="${option.name}" <c:if test="${not empty sessionScope.formError and sessionScope.formDatas[currentNode.name][0] eq option.name}">checked="true"</c:if> />
        </c:otherwise>
      </c:choose>
      <c:set var="counter" value="${counter+1}"/>
      <label for="${currentNode.name}">${option.properties['jcr:title'].string}</label>
    </c:forEach> 
  
	<!--style="display: none;"-->
	<a href="#myModal" id="btnuserlist" role="button" class="btn" data-toggle="modal">Choose Users</a>
 
    <!-- Modal -->
    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">x</button>
        <h3 id="myModalLabel">Userlist</h3>
      </div>
      <div class="modal-body">
        
        <!--<form class="navbar-search pull-left">
          <input type="text" id="searchUsers" class="search-query" placeholder="Search">
        </form>-->
        <table id="table1" class="table table-condensed">
          <thead>
            <tr>
              <th scope="col">#</th>
              <th scope="col">First Name</th>
              <th scope="col">Last Name</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${listQuerySql.nodes}" var="user">
              <c:if test="${jcr:isNodeType(user, 'jnt:user')}">
                <tr>
                  <td>
                    <input ${disabled} type="checkbox" ${required} class="${required}" name="${currentNode.name}box" id="${currentNode.name}box" value="${user.path}" <c:if test="${isChecked eq 'true'}">checked="true"</c:if>
                      <c:if test="${required eq 'required'}">onclick='$("input:checkbox[name=${currentNode.name}box]:checked").size()==0?$("input:checkbox[name=${currentNode.name}box]").prop("required", true):$("input:checkbox[name=${currentNode.name}box]").removeAttr("required")'</c:if> />
                  </td>
                  <td>
                    <label>${user.properties['j:firstName'].string}</label> 
                  </td>
                  <td>
                    <label>${user.properties['j:lastName'].string}</label> 
                  </td>
                </tr>
              </c:if>
            </c:forEach>
          </tbody>  
        </table>
        <script>
          $(document).ready(function() {
              $('table').filterTable(); // apply filterTable to all tables on this page
          });
        </script> 
        
      </div>
      <div class="modal-footer">
        <button class="btn btn-primary"  data-dismiss="modal" >Save</button>
      </div>
    </div>
  
              
    <c:if test="${renderContext.editMode}">
        <p><fmt:message key="label.listOfOptions"/> </p>
        <ol>
            <c:forEach items="${jcr:getNodes(currentNode,'jnt:formListElement')}" var="option">
                <li><template:module node="${option}" view="default" editable="true"/></li>
            </c:forEach>
        </ol>
        <div class="addvalidation">
            <span><fmt:message key="label.addListOption"/> </span>
            <template:module path="*" nodeTypes="jnt:formListElement"/>
        </div>

        <p><fmt:message key="label.listOfValidation"/> </p>
        <ol>
            <c:forEach items="${jcr:getNodes(currentNode,'jnt:formElementValidation')}" var="formElement" varStatus="status">
                <li><template:module node="${formElement}" view="edit"/></li>
            </c:forEach>
        </ol>
        <div class="addvalidation">
            <span><fmt:message key="label.addValidation"/> </span>
            <template:module path="*" nodeTypes="jnt:formElementValidation"/>
        </div>
    </c:if>
</div>