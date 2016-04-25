<%@include file="header.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="<c:url value="/resources/w3.css" />"/>
        <title><fmt:message key="login.title"/></title>
    </head>
 
    <body>
        <c:url var="loginUrl" value="/login"/>
        <c:url var="signUpUrl" value="/signUp"/>

        <header class="w3-container w3-teal">
            <h1><fmt:message key="login.title"/></h1>
        </header>

        <c:if test="${param.error != null}">
            <div class="w3-container w3-yellow">
                <p><fmt:message key="alert.invalid.credentials"/></p>
            </div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="w3-container w3-green">
                <p><fmt:message key="alert.successful.logoff"/></p>
            </div>
        </c:if>
        <c:if test="${param.signed_up != null}">
            <div class="w3-container w3-green">
                <p><fmt:message key="alert.successful.signup"/></p>
            </div>
        </c:if>

        <div class="w3-container w3-half w3-margin-top">
            <form action="${loginUrl}" method="post" class="w3-container w3-card-4" accept-charset="utf-8">
                <p>
                    <input type="text" class="w3-input" id="username" name="username" placeholder="<fmt:message key="login.placeholder.username"/>" required>
                    <label class="w3-label w3-validate" for="username"><fmt:message key="login.label.username"/></label>
                </p>
                <p>
                    <input type="password" class="w3-input" id="password" name="password" placeholder="<fmt:message key="login.paceholder.password"/>" required>
                    <label class="w3-label w3-validate" for="password"><fmt:message key="login.label.password"/></label> 
                </p>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <p>
                    <button type="submit" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="login.button.submit"/></button>
                </p>
            </form>
            <div class="w3-container">
                <fmt:message key="msg.dont.have.account.yet"/> <b><a href="${signUpUrl}"><fmt:message key="msg.sign.up.here"/></a></b>
            </div>
        </div>
    </body>
</html>
