<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="org.jahia.services.usermanager.*" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>

<%@ taglib prefix="query" uri="http://www.jahia.org/tags/queryLib" %>

<jcr:jqom var="usermap">
  <query:selector nodeTypeName="jnt:user"/>
</jcr:jqom>

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
          <input ${disabled} type="radio" ${required} class="${required}" name="${currentNode.name}" id="${currentNode.name}" value="${option.name}" checked="true" />
        </c:when>
        <c:otherwise>
           <input ${disabled} type="radio" ${required} class="${required}" name="${currentNode.name}" id="${currentNode.name}" value="${option.name}" <c:if test="${not empty sessionScope.formError and sessionScope.formDatas[currentNode.name][0] eq option.name}">checked="true"</c:if> />
        </c:otherwise>
      </c:choose>
      <c:set var="counter" value="${counter+1}"/>
      <label for="${currentNode.name}">${option.properties['jcr:title'].string}</label>
    </c:forEach>
	
  
  	



	<c:forEach items="${usermap.nodes}" var="user">
      <h4>Query test: ${user.properties['j:firstName'].string} , ${user.properties['j:lastName'].string}</h4>
      ${user.propertiesAsString}</h4>
      
      <label>
        <input ${disabled} type="checkbox" ${required} class="${required}" name="${currentNode.name}box" <c:if test="${isChecked eq 'true'}">checked="true"</c:if>
                           <c:if test="${required eq 'required'}">onclick='$("input:checkbox[name=${currentNode.name}box]:checked").size()==0?$("input:checkbox[name=${currentNode.name}box]").prop("required", true):$("input:checkbox[name=${currentNode.name}box]").removeAttr("required")'</c:if> />
        ${user.properties['j:lastName'].string} , ${user.properties['j:firstName'].string}</label> 	
	</c:forEach>  
  
  
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