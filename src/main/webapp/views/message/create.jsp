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

<div class="seccion w3-light-grey">
    <form:form action="${requestUri}" modelAttribute="modelMessage">
        <form:hidden path="id"/>
        <form:hidden path="version"/>
        <form:hidden path="moment"/>
        <form:hidden path="sender"/>


        <spring:message code="ms.priority.low" var="low"/>
        <spring:message code="ms.priority.neutral" var="neutral"/>
        <spring:message code="ms.priority.high" var="high"/>


        <div class="row">
            <div class="col-25">

                <acme:textbox code="ms.subject" path="subject"/>
                <form:label path="priority">
                    <spring:message code="ms.priority"/>
                </form:label>
                <form:select path="priority" class="formInput w3-text-black">
                    <form:option label="${low}" value="LOW"/>
                    <form:option label="${neutral}" value="NEUTRAL"/>
                    <form:option label="${high}" value="HIGH"/>
                </form:select>
                <form:errors cssClass="error" path="priority"/>

                <div class="w3-row">

                    <security:authorize access="hasRole('ADMINISTRATOR')">
                        <div class="w3-col m12">
                            <acme:checkBox code="label.notification" path="broadcast"
                                           css="w3-check"
                                           onclick="javascript:setVisibility('recipients', !this.checked);" id="notificationCheck"/>

                        </div>
                    </security:authorize>
                    <div class="w3-col w3-rest">
                        <div id="recipients" style="">
                            <jstl:if test="${requestUri eq 'message/edit.do'}">
                                <acme:select items="${actors}" itemLabel="userAccount.username"
                                             code="ms.recipient" path="recipient" css="formInput w3-text-black"/>
                            </jstl:if>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-75">
                <acme:textarea code="ms.body" path="body" css="msgTextArea w3-text-black"/>
            </div>

        </div>
        <div class="row w3-padding">
            <div class="col-25">
                <button type="submit" name="save" class="flatButton toRight">
                    <i class="fa fa-paper-plane w3-bar-item w3-xlarge w3-padding"
                       onmouseenter="overEffect(this);" onmouseleave="overEffect(this);"></i>
                </button>
                <button type="button" onclick="relativeRedir('/folder/list.do')"
                        class="flatButton toRight w3-padding">
                    <i class="fa fa-arrow-left w3-bar-item w3-xlarge"
                       onmouseenter="overEffect(this);" onmouseleave="overEffect(this);"></i>
                </button>
            </div>
        </div>
    </form:form>
</div>

<script>
    $(document).ready(function () {
        var checked = document.getElementById("notificationCheck").checked;
        setVisibility('recipients', !checked);
    });

</script>