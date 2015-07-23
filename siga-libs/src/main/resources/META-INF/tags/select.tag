<%@ tag body-content="empty"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="ww" uri="/webwork"%>
<%@ attribute name="id" required="false"%>
<%@ attribute name="label" required="false"%>
<%@ attribute name="name" required="false"%>
<%@ attribute name="list" required="false"%>
<%@ attribute name="theme" required="false"%>
<%@ attribute name="listValue" required="false"%>
<%@ attribute name="listKey" required="false"%>
<%@ attribute name="value" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="multiple" required="false"%>
<%@ attribute name="headerValue" required="false"%>
<%@ attribute name="headerKey" required="false"%>

<!-- wwselect -->
<c:if test="${empty value}">
	<c:set var="value" value="${requestScope[name]}"/>
</c:if>
<c:if test="${theme != 'simple'}">
<tr><td>${label}</td><td>
</c:if>
<select id="${id}" name="${name}" <c:if test="${not empty onchange}">onchange="${onchange}"</c:if>>
<c:if test="${not empty headerKey}">
	<option value="${headerKey}"
			${headerKey == value ? 'selected' : ''}
			>${headerValue}</option>  
</c:if>
	<c:forEach items="${requestScope[list]}" var="item">
	<option value="${item[listKey]}"
			${item[listKey] == value ? 'selected' : ''}
			>${item[listValue]}</option>  
	</c:forEach>
</select>
<c:if test="${theme != 'simple'}">
</td></tr>
</c:if>
