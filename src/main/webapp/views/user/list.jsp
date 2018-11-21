<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:message code="msg.delete.confirmation" var="deleteConfirmation"/>
<jstl:if test="${actors!=null}">
    <div class="seccion w3-light-grey ">

        <legend>
            <spring:message code="label.users"/>
        </legend>
        <div style="overflow-x:auto;">
            <jstl:if test="${!actors.isEmpty()}">
                <form:form action="${requestUri}" method="GET">
                    <spring:message code="pagination.size"/>
                    <input hidden="true" name="word" value="${word}">
                    <input hidden="true" name="actorId" value="${showroom.id}">
                    <input type="number" name="pageSize" min="1" max="100"
                           value="${pageSize}">
                    <input type="submit" value=">">
                </form:form>
            </jstl:if>
            <display:table pagesize="${pageSize}"
                           class="flat-table flat-table-1 w3-light-grey" name="actors"
                           requestURI="${requestUri}" id="row">
                <jstl:set var="activateUrl" value="actor/administrator/activate.do?actorId=${row.id}&pageSize=${pageSize}"/>
                <jstl:set var="url" value="actor/display.do?actorId=${row.id}"/>
                <acme:column property="${row.userAccount.username}" title="label.user" sortable="true"
                             rowUrl="${url}"/>
                <acme:column property="${row.surname}, ${row.name}" title="label.name" sortable="true" rowUrl="${url}"/>
                <acme:column property="birthdate" title="label.birthdate" sortable="true" format="date.format"/>
                <security:authorize access="hasRole('USER')">
                    <jstl:if test="${!userIsFollowedMap[row]}">
                        <acme:column property=" " title="label.follow" sortable="true"
                                     rowUrl="follow/user/follow.do?userId=${row.id}"
                                     icon="fa fa-check w3-xlarge w3-text-gray css-uncheck"/>
                    </jstl:if>
                    <jstl:if test="${userIsFollowedMap[row]}">
                        <acme:column property=" " title="label.follow" sortable="true"
                                     rowUrl="follow/user/follow.do?userId=${row.id}"
                                     icon="fa fa-check w3-xlarge w3-text-green"/>
                    </jstl:if>
                </security:authorize>

                <security:authorize access="hasRole('ADMINISTRATOR')">
                    <jstl:if test="${!row.userAccount.active}">
                        <acme:column property=" " title="label.activation" sortable="true"
                                     rowUrl="${activateUrl}"
                                     icon="fa fa-toggle-off w3-xlarge w3-text-light-gray css-uncheck"/>
                    </jstl:if>
                    <jstl:if test="${row.userAccount.active}">
                        <acme:column property=" " title="label.activation" sortable="true"
                                     rowUrl="${activateUrl}"
                                     icon="fa fa-toggle-on w3-xlarge w3-text-green"/>
                    </jstl:if>
                </security:authorize>
            </display:table>
        </div>
    </div>
</jstl:if>
<security:authorize access="hasRole('USER')">
    <jstl:if test="${userIsFollowedMap!=null && userIsFollowedMap.size!=0}">
<spring:message code="date.pattern" var="datePattern"/>

        <div class="seccion w3-light-grey ">
            <legend><spring:message code="label.followeds"/></legend>
            <div style="overflow-x:auto;">


                <table class="flat-table flat-table-1 w3-light-grey">
                    <tr>
                        <th><spring:message code="label.user"/></th>
                        <th><spring:message code="label.name"/></th>
                        <th><spring:message code="label.date"/></th>
                        <th><spring:message code="label.follow"/></th>
                    </tr>

                    <jstl:forEach items="${userIsFollowedMap}" var="entry">
                        <jstl:if test="${entry.value}">
                            <tr>
                                <td>${entry.key.userAccount.username}</td>
                                <td>${entry.key.surname}, ${entry.key.name}</td>
                                <td><fmt:formatDate value="${entry.key.birthdate}" pattern="${datePattern}"/> </td>
                                <td><a href="follow/user/follow.do?userId=${entry.key.id}">
                                    <i class="fa fa-check w3-xlarge w3-text-green"></i>
                                </a></td>
                            </tr>
                        </jstl:if>
                    </jstl:forEach>
                </table>
            </div>
        </div>
    </jstl:if>
</security:authorize>
<jstl:out value="${userIsFollowerMap}"/>

<security:authorize access="hasRole('USER')">
    <jstl:if test="${followers!=null}">
        <div class="seccion w3-light-grey ">
            <legend><spring:message code="label.followers"/></legend>
            <div style="overflow-x:auto;">
                <jstl:if test="${!followers.isEmpty()}">
                    <form:form action="${requestUri}" method="GET">
                        <spring:message code="pagination.size"/>
                        <input hidden="true" name="word" value="${word}">
                        <input hidden="true" name="actorId" value="${showroom.id}">
                        <input type="number" name="pageSize" min="1" max="100"
                               value="${pageSize}">
                        <input type="submit" value=">">
                    </form:form>
                </jstl:if>
                <display:table pagesize="${pageSize}"
                               class="flat-table flat-table-1 w3-light-grey" name="followers"
                               requestURI="${requestUri}" id="row">
                    <jstl:set var="activateUrl" value="actor/administrator/activate.do?actorId=${row.id}&pageSize=${pageSize}"/>
                    <jstl:set var="url" value="actor/display.do?actorId=${row.id}"/>
                    <acme:column property="${row.userAccount.username}" title="label.user" sortable="true"
                                 rowUrl="${url}"/>
                    <acme:column property="${row.surname}, ${row.name}" title="label.name" sortable="true" rowUrl="${url}"/>
                    <acme:column property=" " title="label.view" sortable="true"
                                 rowUrl="${url}"
                                 icon="fa fa-eye w3-xlarge"/>
                </display:table>

            </div>
        </div>
    </jstl:if>
</security:authorize>


<div class="seccion w3-light-grey">
    <div class="row">
        <div class="col-50">
            <security:authorize access="hasRole('USER')">
                <acme:button url="/follow/user/followers.do" text="label.followers"
                             css="formButton toLeft w3-padding"/>
                <acme:button url="/follow/user/followeds.do" text="label.followeds"
                             css="formButton toLeft w3-padding"/>
            </security:authorize>
            <acme:backButton text="actor.back" css="formButton toLeft w3-padding"/>
        </div>
    </div>
</div>
