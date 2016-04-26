<%@include file="header.jsp" %>
<%@page import="java.time.format.DateTimeFormatter"%>
<c:set var="dateFormatter" value="<%=DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm\")%>"/>
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

        <div class="w3-container w3-half w3-margin-top">
            <form name="calculationForm" action="${calculatorUrl}" method="post" class="w3-container w3-card-4"accept-charset="utf-8">
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
                    <input type="number" step="0.01" class="w3-input" id="amount" name="amount" required/>
                    <label class="w3-label" for="amount"><fmt:message key="calculator.convert.amount"/>:</label>
                </p>
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
                <p>
                    <button type="submit" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="signup.button.submit"/></button>
                    <button type="reset" class="w3-btn w3-section w3-teal w3-ripple"><fmt:message key="signup.button.reset"/></button>
                </p>
            </form>
            <div class="w3-container w3-small w3-right">
                <a href="${logoutUrl}"><fmt:message key="msg.click.to.logout"/></a>
            </div>
            <c:if test="${queries != null && !queries.isEmpty()}">
                <h2><fmt:message key="calculator.history.title"/></h2>
                <table class="w3-table w3-bordered w3-striped" w3-card4>
                    <tr>
                        <th><fmt:message key="calculator.history.from"/></th>
                        <th><fmt:message key="calculator.history.amount"/></th>
                        <th><fmt:message key="calculator.history.to"/></th>
                        <th><fmt:message key="calculator.history.result"/></th>
                        <th><fmt:message key="calculator.history.date"/></th>
                    </tr>
                    <c:forEach items="${queries}" var="query">
                        <tr>
                            <td class="w3-tooltip">
                                ${query.from}
                                <span style="position:absolute;left:0;bottom:25px" class="w3-text w3-tag w3-round-xlarge">
                                    ${queriedCurrencies[query.from].description}
                                </span>
                            </td>
                            <td>${query.amount}</td>
                            <td class="w3-tooltip">
                                ${query.to}
                                <span style="position:absolute;left:0;bottom:25px" class="w3-text w3-tag w3-round-xlarge">
                                    ${queriedCurrencies[query.to].description}
                                </span>
                            </td>
                            <td>${query.result}</td>
                            <td>${query.timestamp.format( dateFormatter )}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </body>
</html>
