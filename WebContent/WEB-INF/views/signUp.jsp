<%@page import="net.jarl.kata.currencyzoo.model.COUNTRY"%>
<%@include file="header.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value="/resources/w3.css" />"/>
        <title><fmt:message key="signup.title"/></title>
    </head>

    <body>

        <c:url var="signUpUrl" value="/signUp"/>
        <c:url var="loginUrl" value="/login"/>
        <c:set var="countries" value="<%=COUNTRY.values()%>"/>

        <header class="w3-container w3-teal">
            <h1><fmt:message key="signup.title"/></h1>
        </header>
        <c:if test="${errors != null}">
            <div class="w3-container w3-yellow">
                <p><fmt:message key="alert.invalid.signup.data"/></p>
            </div>
        </c:if>

        <div class="w3-container w3-half w3-margin-top">
            <form name="userForm" action="${signUpUrl}" method="post" class="w3-container w3-card-4">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <p>
                    <input type="text" class="w3-input" id="username" name="username" value="${form.username}" required/>
                    <label class="w3-label w3-validate" for="username"><fmt:message key="signup.label.username"/></label>
                </p>
                <p>
                    <input type="password" class="w3-input" id="password" name="password" required/>
                    <label class="w3-label w3-validate" for="password"><fmt:message key="signup.label.password"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"password\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("password").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <input type="password" class="w3-input" id="passwordCopy" name="passwordCopy"/>
                    <label class="w3-label w3-validate" for="passwordCopy"><fmt:message key="signup.label.password.repeat"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"passwordCopy\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("passwordCopy").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label> 
                </p>
                <p>
                    <input type="text" class="w3-input" id="email" name="email" value="${form.email}" required/>
                    <label class="w3-label w3-validate" for="email"><fmt:message key="signup.label.email"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"email\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("email").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <input type="text" class="w3-input" id="dateOfBirth" name="dateOfBirth" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${form.dateOfBirth}"/>" required/>
                    <label class="w3-label w3-validate" for="dateOfBirth"><fmt:message key="signup.label.dateOfBirth"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"dateOfBirth\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("dateOfBirth").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <input type="text" class="w3-input" id="street" name="street" value="${form.street}" required/>
                    <label class="w3-label w3-validate" for="street"><fmt:message key="signup.label.street"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"street\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("street").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <input type="text" class="w3-input" id="zipCode" name="zipCode" value="${form.zipCode}" required/>
                    <label class="w3-label w3-validate" for="zipCode"><fmt:message key="signup.label.zipCode"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"zipCode\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("zipCode").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <input type="text" class="w3-input" id="city" name="city" value="${form.city}" required/>
                    <label class="w3-label w3-validate" for="city"><fmt:message key="signup.label.city"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"city\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("city").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <select class="w3-select" id="country" name="country" value="${form.country}" required>
                        <option value=""></option>
                    	<c:forEach items="${countries}" var="country">
                    		<option value="${country}"<c:if test="${country == form.country}">selected</c:if>><fmt:message key="country.${country}"/></option>
                    	</c:forEach>
                    </select>
                    <label class="w3-label w3-validate" for="country"><fmt:message key="signup.label.country"/>
                        <c:if test="${errors != null && errors.hasFieldErrors(\"country\")}">
                            <span class="w3-tiny w3-text-red"><b>${errors.getFieldErrors("country").get(0).getDefaultMessage()}</b></span>
                        </c:if>
                    </label>
                </p>
                <p>
                    <button type="submit" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="signup.button.submit"/></button>
                    <button type="reset" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="signup.button.reset"/></button>
                </p>
            </form>
            <div class="w3-container w3-small w3-right">
                <a href="${loginUrl}"><fmt:message key="msg.back.to.login"/></a>
            </div>
        </div>
    </body>
