<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<security:authorize access="isAuthenticated()">
	<jstl:set var="colom" value=", " />
	<security:authentication property="principal.username" var="username" />
	<security:authentication property="principal" var="logedActor" />
	<security:authentication property="principal.authorities[0]"
		var="permiso" />
	<jstl:set var="rol" value="${fn:toLowerCase(permiso)}/" />
</security:authorize>
<jstl:set var="rol" value="${fn:toLowerCase(permiso)}" />
<jstl:if test="${pageSize == null}">
	<jstl:set value="5" var="pageSize" />
</jstl:if>

<jstl:if test="${showroom != null}">
	<jstl:set value="showroom/display.do" var="requestUri" />
	<jstl:set value="true" var="included" />
</jstl:if>
<jstl:if test="${item != null}">
	<jstl:set value="item/display.do" var="requestUri" />
	<jstl:set value="true" var="included" />
</jstl:if>
<div class="seccion w3-light-grey">
	<legend>
		<spring:message code="label.comments" />
		<jstl:if test="${commented != null and !included}">
			<spring:message code="label.about" />
			<jstl:out value="${commented}" />
		</jstl:if>
	</legend>
	<jstl:if test="${pageSize == null}">
		<jstl:set value="20" var="pageSize" />
	</jstl:if>
	<jstl:if test="${!included}">
		<form:form action="${requestUri}" method="GET">
			<spring:message code="pagination.size" />
			<input hidden="true" name="word" value="${word}">
			<input type="number" name="pageSize" min="1" max="100"
				value="${pageSize}">
			<input type="submit" value=">">
		</form:form>
	</jstl:if>

	<div style="overflow-x: auto;">
		<display:table pagesize="${pageSize}"
			class="flat-table0 flat-table-1 w3-light-grey" name="comments"
			requestURI="${requestUri}" id="row">
			<jstl:set var="url"
				value="comment/actor/display.do?commentId=${row.id}" />

			<acme:urlColumn value="${row.actor.userAccount.username}"
				title="label.user" href="actor/display.do?actorId=${row.actor.id}"
				css="iButton" />
			<acme:urlColumn value="${row.title}" title="label.title"
				sortable="true" href="${url}" css="iButton" />
			<acme:urlColumn value="${row.text}" title="label.description"
							href="${url}" css="iButton" />
			<acme:imgColumn src="images/rating${row.rating}.png" title="label.stars"
							href="${url}" css="iButton tableRatting" />
			<spring:message code="moment.pattern" var="intercionalizedPattern" />
			<fmt:formatDate value="${row.moment}"
				pattern="${intercionalizedPattern}" var="intercionalizedMoment" />
			<acme:urlColumn value="${intercionalizedMoment}" title="label.moment"
				href="${url}" css="iButton" />
			<acme:urlColumn value="" title="label.none" href="${url}"
				css="iButton" icon="fa fa-eye w3-xlarge" />
		</display:table>
	</div>
	<br />
</div>