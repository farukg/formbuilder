<%@ taglib prefix="jcr" uri="http://www.jahia.org/tags/jcr" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="utility" uri="http://www.jahia.org/tags/utilityLib" %>
<%@ taglib prefix="template" uri="http://www.jahia.org/tags/templateLib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="formbuilder" uri="http://www.jahia.org/tags/formbuilder" %>
<%--@elvariable id="currentNode" type="org.jahia.services.content.JCRNodeWrapper"--%>
<%--@elvariable id="out" type="java.io.PrintWriter"--%>
<%--@elvariable id="script" type="org.jahia.services.render.scripting.Script"--%>
<%--@elvariable id="scriptInfo" type="java.lang.String"--%>
<%--@elvariable id="workspace" type="java.lang.String"--%>
<%--@elvariable id="renderContext" type="org.jahia.services.render.RenderContext"--%>
<%--@elvariable id="currentResource" type="org.jahia.services.render.Resource"--%>
<%--@elvariable id="url" type="org.jahia.services.render.URLGenerator"--%>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<c:set target="${renderContext}" property="contentType" value="text/csv;charset=UTF-8" /><c:set value="${formbuilder:getFormFields(currentNode.parent)}" var="formFields" scope="request"/>date,user,url<c:forEach items="${formFields}" var="formField"><c:choose><c:when test="${formField.value eq 'file'}"><c:set var="hasFile" value="true"/></c:when><c:otherwise>,${formField.key}</c:otherwise></c:choose></c:forEach><c:if test="${hasFile}">,files</c:if>${fn:escapeXml(newLineChar)}
<c:forEach items="${jcr:getDescendantNodes(currentNode,'jnt:responseToForm')}" var="subResponseNode"><template:module node="${subResponseNode}" view="default"><template:param name="hasFile" value="${hasFile}"/></template:module>${fn:escapeXml(newLineChar)}
</c:forEach>