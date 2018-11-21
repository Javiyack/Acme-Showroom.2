<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>
<jstl:set var="color" value="red" />
<jstl:set var="url" value="security/login.do" />
<security:authorize access="isAuthenticated()">
	<jstl:set var="color" value="green" />
	<jstl:set var="url" value="j_spring_security_logout" />
</security:authorize>
<!-- Top container -->
<div class="w3-bar w3-top w3-black"
	style="z-index: 4; opacity: .9; height: 52px;">
	<button
		class="w3-bar-item w3-button w3-hide-large w3-hover-none w3-hover-text-light-grey"
		onclick="w3_open();">
		<i class="fa fa-bars"></i> �Menu
	</button>
	<i class="w3-bar-item w3-button"><img src="images/spain.ico"
		class="iconoenlace" style="margin-bottom: 22px;"
		title="Cambiar a espa�ol" onclick="changeLang('es');" /></i> <i
		class="w3-bar-item w3-button"><img src="images/uk.ico" class="iconoenlace"
		style="margin-bottom: 22px;" title="Change to english"
		onclick="changeLang('en')" /></i> <span class="w3-bar-item w3-right">

		<security:authorize access="isAnonymous()">
			<a href="user/create.do"><i class="fa fa-user-plus w3-xxlarge"
				style="margin-left: 1em; margin-right: 1em;"></i></a>
		</security:authorize> <a href="${url}" style="color:${color};"> <i
			class="fa fa-power-off w3-xxlarge"> </i></a>

	</span>
</div>
