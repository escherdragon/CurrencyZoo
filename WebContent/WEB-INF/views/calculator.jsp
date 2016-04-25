<%@include file="header.jsp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" href="<c:url value="/resources/w3.css" />"/>
        <title><fmt:message key="calculator.title"/></title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <c:url var="calculatorUrl" value="/calculator"/>

        <header class="w3-container w3-teal">
            <h1><fmt:message key="calculator.title"/></h1>
        </header>

        <c:if test="${error != null}">
            <div class="w3-container w3-yellow">
                <p><fmt:message key="${error}"/></p>
            </div>
        </c:if>
        <c:if test="${result != null}">
            <div class="w3-container w3-green">
                <p><b>${from} ${amount} = ${to} ${result}</b></p>
            </div>
        </c:if>

        <div class="w3-container w3-half w3-margin-top">
            <form name="calculationForm" action="${calculatorUrl}" method="post" class="w3-container w3-card-4">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <p>
                    <select class="w3-select" id="from" name="from" required>
                        <c:forEach items="${currencies}" var="currency">
                            <option value="${currency.symbol}"<c:if test="${from == currency.symbol}"> selected</c:if>>${currency.symbol} - ${currency.description}</option>
                        </c:forEach>
                    </select>
                    <label class="w3-label" for="from"><fmt:message key="calculator.convert.from"/></label>
                </p>
                <p>
                    <select class="w3-select" id="to" name="to" required>
                        <c:forEach items="${currencies}" var="currency">
                            <option value="${currency.symbol}"<c:if test="${to == currency.symbol}"> selected</c:if>>${currency.symbol} - ${currency.description}</option>
                        </c:forEach>
                    </select>
                    <label class="w3-label" for="to"><fmt:message key="calculator.convert.to"/></label>
                </p>
                <p>
                    <input type="number" class="w3-input" id="amount" name="amount" required/>
                    <label class="w3-label" for="amount"><fmt:message key="calculator.convert.amount"/>:</label>
                </p>
                <p>
                    <button type="submit" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="signup.button.submit"/></button>
                    <button type="reset" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="signup.button.reset"/></button>
                </p>
            </form>
            <div class="w3-container w3-small w3-right">
                <a href="${logoutUrl}"><fmt:message key="msg.click.to.logout"/></a>
            </div>
        </div>
    </body>
</html>
