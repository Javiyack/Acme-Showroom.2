<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security"
          uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<security:authorize access="isAuthenticated()">
    <security:authentication property="principal" var="logedActor"/>
    <security:authentication property="principal.authorities[0]"
                             var="permiso"/>
    <jstl:set var="rol" value="${fn:toLowerCase(permiso)}"/>
</security:authorize>
<jstl:if test="${pageSize == null}">
    <jstl:set value="20" var="pageSize"/>
</jstl:if>
<jstl:if test="${actorForm != null}">
    <jstl:set value="actor/actor/display.do" var="requestUri"/>
    <jstl:set value="true" var="included"/>
</jstl:if>

<div class="seccion w3-light-grey">
    <legend>
        <spring:message code="label.showrooms"/> <jstl:if test="${userId!=null}">
        <i class="fa fa-user font-awesome w3-padding"> <jstl:out value="${username}"/></i></jstl:if>
    </legend>

    <jstl:if test="${!showrooms.isEmpty()}">
        <jstl:if test="${!included}">
            <form:form action="${requestUri}" method="POST">
                <input name="userId" value="${userId}" type="hidden">
                <input name="username" value="${username}" type="hidden">

                <div class="row">
                    <div class="col-50">
                        <spring:message code="label.search" var="placeholder"/>
                        <input name="word" value="${word}" placeholder="&#xf002; ${placeholder}"
                               class="font-awesome">
                    </div>
                    <div class="col-50">
                        <p class="toRight">
                            <spring:message code="pagination.size"/>
                            <input type="number" name="pageSize" min="1" max="100"
                                   value="${pageSize}">
                            <input type="submit" value=">">

                        </p>

                    </div>
                </div>
            </form:form>
        </jstl:if>

        <jstl:if test="${included}">
            <a href="showroom/list.do?userId=${actorForm.id}&username=${actorForm.userAccount.username}"
               class="w3-xlarge w3-hover-text-orange w3-margin-right"> <i
                    class="fa fa-shopping-bag fa-fw"></i>� <spring:message
                    code="label.show.all" />
            </a>
        </jstl:if>
    </jstl:if>
    <div class="row">
        <div class="col-100">
            <div style="overflow-x:auto;">
                <display:table pagesize="${pageSize}"
                               class="flat-table0 flat-table-1 w3-light-grey" name="showrooms"
                               requestURI="${requestUri}" id="row">
                    <jstl:set var="owns" value="${logedActor.id == row.user.userAccount.id}"/>
                    <jstl:set value="showroom/display.do?showroomId=${row.id}" var="rowUrl"/>
                    <jstl:if test="${owns}">
                        <jstl:set value="showroom/user/edit.do?showroomId=${row.id}" var="rowUrl"/>
                    </jstl:if>
                    <acme:imgColumn src="${row.logo}" title="label.logo" href="${rowUrl}" css="tableImg iButton"/>
                    <acme:urlColumn value="${row.user.userAccount.username}" title="label.user" href="${rowUrl}" css="iButton" sortable="true"/>
                    <acme:urlColumn value="${row.name}" title="label.name" href="${rowUrl}" css="iButton" sortable="true"/>
                    <acme:urlColumn value="${row.description}" title="label.description"
                                    href="${rowUrl}" css="iButton"/>
                    <jstl:if test="${owns}">
                        <acme:urlColumn value=" " title="label.none" sortable="true"
                                     icon="fa fa-edit w3-xlarge" href="${rowUrl}" css="iButton"/>
                    </jstl:if>
                    <jstl:if test="${!owns}">
                        <acme:urlColumn value=" " title="label.none" sortable="true"
                                     icon="fa fa-eye w3-xlarge" href="${rowUrl}" css="iButton"/>
                    </jstl:if>

                </display:table>
            </div>
        </div>
    </div>
    <jstl:set var="owns" value="${actorForm!=null and logedActor.id == actorForm.userAccount.id}"/>

    <jstl:if test="${owns}">
        <jstl:if test="${rol == 'user'}">
            <spring:message code="label.new" var="newTitle"/>
            <spring:message code="label.showroom" var="showroomTitle"/>
            <p><i class="fa fa-plus-square w3-xxlarge w3-text-dark-grey w3-hover-text-orange iButton w3-padding w3-margin-right"
                       onclick="relativeRedir('showroom/user/create.do');" title="${newTitle} ${showroomTitle}"></i> </p>
        </jstl:if>
    </jstl:if>
    <jstl:if test="${actorForm==null and rol eq 'user'}">
        <hr>
        <spring:message code="label.new" var="newTitle"/>
        <spring:message code="label.showroom" var="showroomTitle"/>
        <div><i class="fa fa-plus-square w3-xxlarge w3-text-dark-grey w3-hover-text-orange iButton w3-padding w3-margin-right"
              onclick="relativeRedir('showroom/user/create.do');" title="${newTitle} ${showroomTitle}"></i>
        <jstl:if test="${userList}">
                <a href="showroom/list.do" >
                    <i class="fa fa fa-filter w3-xxlarge css-checked iButton w3-padding w3-margin-right"
                       title="<spring:message code="label.show.all"/>">
                    </i></a>
        </jstl:if>
        <jstl:if test="${userList==null}">
            <a href="showroom/user/list.do" >
                    <i class="fa fa fa-filter w3-xxlarge css-unchecked iButton w3-padding w3-margin-right"
                       title="<spring:message code="label.show.mine.only"/>"></i>
                    </a>

        </jstl:if>
        </div>
    </jstl:if>


</div>