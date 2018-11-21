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

<div class="seccion w3-light-grey">
<legend>
    <spring:message code="label.configuration"/>
</legend>
<form:form requestUri="configuration/administrator/edit.do"
           modelAttribute="configuration">

    <form:hidden path="id"/>
    <form:hidden path="version"/>

    <div class="row">
        <div class="w3-col m5 w3-padding">
            <acme:textbox code="configuration.companyName" path="systemTitle"/>
            <br/>
            <div class="w3-card">
                <img src="${configuration.logo}" alt="${alt}" class="w3-image">
                <div class="">
                    <acme:textbox code="label.none" path="logo" css="w3-border-0"/>
                </div>
            </div>


        </div>
        <div class="w3-col m7 w3-padding">
            <acme:textarea code="configuration.welcomeMessageEs"
                           path="welcomeMessageEs" css="formTextArea w3-text-black " />
            <acme:textarea code="configuration.welcomeMessageEn"
                           path="welcomeMessageEn" css="formTextArea w3-text-black"/>
            <acme:textarea code="label.tabooWords"
                           path="tabooWords" css="formTextArea w3-text-black"/>
            <div class="row">
                <div class="w3-col m7 w3-padding">
                    <acme:textarea code="label.audit.positive.words" path="positiveAuditWords"
                                   css="formTextArea w3-text-black"/>
                    <acme:textarea code="label.audit.negative.words" path="negativeAuditWords"
                                   css="formTextArea w3-text-black"/>

                </div>
                <div class="w3-col m5 w3-padding">
                    <acme:textarea code="label.audit.stopping.positive.words" path="stoppingPositiveAuditWords"
                                   css="formTextArea w3-text-black"/>
                    <acme:textarea code="label.audit.stopping.negative.words" path="stoppingNegativeAuditWords"
                                   css="formTextArea w3-text-black"/>
                </div>
            </div>

            <div class="row">

            </div>
        </div>
    </div>


    </div>


    <acme:submit code="configuration.save" name="save"
                 css="formButton toLeft"/>
    <br/>
</form:form>

<br/>
</div>