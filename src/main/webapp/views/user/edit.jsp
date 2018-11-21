<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<div class="form">

	<form:form action="${requestUri}" modelAttribute="actorForm">
		<form:hidden path="id" />
		<br>
		<!-- Datos -->
		<div class="seccion w3-light-grey">

			<div class="row">
				<!-- Datos Personales-->
				<div class="col-60">

					<legend>
						<spring:message code="actor.personal.data" />
					</legend>

					<div class="row">
						<div class="col-50">
							<acme:textbox code="actor.name" path="name" readonly="${!edition}" />
						</div>
						<div class="col-50">
							<acme:textbox code="actor.surname" path="surname" readonly="${!edition}" />
						</div>
					</div>

					<div class="row">
						<div class="col-50">
							<acme:textbox code="actor.email" path="email" readonly="${!edition}" />
						</div>
						<div class="col-50">
							<acme:textbox code="label.phone" path="phone" readonly="${!edition}" />

						</div>
					</div>

					<div class="row">
						<div class="col-100">
							<acme:textbox code="actor.address" path="address" readonly="${!edition}" />
						</div>
					</div>
					<jstl:if test="${modelName eq 'userForm' or modelName = 'agentForm'}">
						<div id="userData">
							<div class="row">
								<div class="col-50">
									<acme:date code="label.birthdate" path="birthdate" readonly="${!edition}" id="birthdate" />
								</div>
								<div class="col-50">
									<acme:textbox code="label.genere" path="genere" readonly="${!edition}" id="genere" />
								</div>
							</div>
							<div class="row">
								<div class="col-100">
									<acme:textbox code="label.photo" path="photo" readonly="${!edition}" id="photo" />
								</div>
							</div>
						</div>
				</div>
				</jstl:if>
				<jstl:if test="${modelName eq 'actorForm' or modelName = 'auditorForm'}">
					<div class="row" id="companyData">
						<div class="col-100">
							<acme:textbox code="label.company" path="username" id="Company" />
						</div>
					</div>
				</jstl:if>
				<jstl:if test="${display}">
					<div class="col-40">
						<legend class="hideText">
							<spring:message code="label.photo" />
						</legend>
						<div class="w3-card-4" Style="margin-top: 2em;">
							<img src="${actorForm.photo}" alt="${actorForm.photo}" Style="width: 100%">
							<div class="w3-container">
								<h4>
									<b>${actorForm.userAccount.username}</b>
								</h4>
							</div>
						</div>
					</div>
				</jstl:if>
				<!-- Datos de la cuenta-->
				<jstl:if test="${edition}">
					<div class="col-40">
						<jstl:if test="${creation}">
							<legend>
								<spring:message code="label.userAccount" />
							</legend>
							<acme:textbox code="actor.username" path="username" />
							<acme:password code="label.userAccount.password" path="password" id="password" onkeyup="javascript: checkPassword();" />
							<acme:password code="label.userAccount.repeatPassword" path="confirmPassword" id="confirm_password" onkeyup="javascript: checkPassword();" />
							<form:label path="${path}">
								<spring:message code="label.authority" />
							</form:label>
							<select id="authority" name="authority" class="w3-text-black" onchange="selectedCheck(this)">
								<jstl:forEach items="${permisos}" var="permiso">
									<option value="${permiso}" id="${permiso}">
										<spring:message code="actor.authority.${permiso}" />
									</option>
								</jstl:forEach>
							</select>

						</jstl:if>
						<jstl:if test="${!creation}">
							<form:hidden path="authority" />
							<legend onclick="showUserAccount();" onmouseenter="overEffect(this);" onmouseleave="overEffect(this);"
							 class="iButton">
								<i class="fa fa-eye fa-fw"></i>�
								<spring:message code="actor.authority.${actorForm.account.authority}" />
							</legend>
							<div id="photoCard">
								<div class="col-40">
									<div class="w3-card-4" Style="margin-top: 2em;">
										<img src="${actorForm.photo}" alt="${actorForm.photo}" Style="width: 100%">
										<div class="w3-container">
											<h4>
												<b>${actorForm.username}</b>
											</h4>
										</div>
									</div>
								</div>
							</div>
							<div id="changePassword" style="display: none;">
								<div class="col-33">
									<acme:textbox code="actor.username" path="username" css="formInput" />
									<acme:password code="label.userAccount.oldPassword" path="password" css="formInput" id="password" onkeyup="javascript: checkEdition();" />
									<acme:password code="label.userAccount.newPassword" path="newPassword" css="formInput" id="new_password"
									 onkeyup="javascript: checkEdition();" />
									<acme:password code="label.userAccount.repeatPassword" path="confirmPassword" id="confirm_password" css="formInput"
									 onkeyup="javascript: checkEdition();" />
								</div>
							</div>
						</jstl:if>
					</div>
				</jstl:if>
			</div>
		</div>
		<jstl:if test="${display}">
			<!-- Showrooms -->

			<jstl:set value="${actorForm.id}" var="userId" />
			<%@ include file="/views/showroom/list.jsp"%>
		</jstl:if>

		<jstl:if test="${edition}">
			<div class="seccion w3-light-grey">
				<security:authorize access="isAnonymous()">
					<p class="terminos w3-text-purple">
						<acme:checkBox code="term.registration.acept" path="agree" css="w3-check" />
						(<a href="term/termsAndConditions.do">
							<spring:message code="term.terms" /></a> && <a href="term/cookies.do">
							<spring:message code="term.cookie" /></a>)
				</security:authorize>
				<div class="row">
					<div class="col-50">
						<input type="submit" name="save" id="save" value='<spring:message code="actor.save"/>' class="formButton toLeft" />&nbsp;
						<input type="button" name="cancel" value='<spring:message code="actor.cancel" />' onclick="relativeRedir('/');"
						 class="formButton toLeft" />
					</div>
				</div>
			</div>

		</jstl:if>

		<jstl:if test="${!edition}">
			<div class="seccion w3-light-grey">
				<div class="row">
					<div class="col-50">
						<acme:backButton text="actor.back" css="formButton toLeft" />
					</div>
				</div>
			</div>
		</jstl:if>


	</form:form>

</div>
<script>
	$(document).ready(function () {
		selectedCheck(document.getElementById("authority"));
	});
</script>