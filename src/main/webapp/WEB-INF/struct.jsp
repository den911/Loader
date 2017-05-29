<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table class="table table-hover jobs-table">
    <thead>
    <tr>
        <th>Structure</th>
        <th>Number</th>
        <th>Status</th>
        <th style="width: 180px;">Edit</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="struct" items="${structs}">
        <tr data-job-name="${struct.name}" id="struct-${struct.name}">
            <td>
                <c:out value="${struct.name}" />
                <%--<img class="hide progress-indicator" src="<c:url value='/static/img/progress.gif' />" alt="Job is running!" height="16" width="16" />--%>
                <%--<a href="#" class="infoLink"> <img src="<c:url value='/static/img/help.gif' />"></a>--%>
                <%--<input type="hidden" class="desc" value="${job.description}"/>--%>
                <%--<input type="hidden" class="sql" value="${job.query}"/>--%>
            </td>
            <td class="records-number"><fmt:formatNumber value="${struct.recordsNumber}" /></td>
            <td class="status">
                <c:out value="${struct.status}" />
                <%--<span class="time"><fmt:formatDate value="${job.startTime}" pattern="HH:mm:ss" /></span>--%>
                <%--<span class="date"><fmt:formatDate value="${job.startTime}" pattern="dd.MM.yyyy" /></span>--%>
            </td>
            <td class="edit">
                <a href="<c:url value="/edit?name=${struct.name}"/>">edit</a>
                <%--<span class="time"><fmt:formatDate value="${job.endTime}" pattern="HH:mm:ss" /></span>--%>
                <%--<span class="date"><fmt:formatDate value="${job.endTime}" pattern="dd.MM.yyyy" /></span>--%>
            </td>
        </tr>

    </c:forEach>
    </tbody>
</table>