<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en"><head>
    <%@include file="head.jsp" %>
</head>

<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <c:import url="menu.jsp"/>
        </div>
    </div>
</div>

<div class="container">
    <h1>All data</h1>
    <form method="POST">
        <c:import url="struct.jsp"/>
    </form>
</div>
<br>
<br>
<br>
<br>

<%--<div id="infoWindow" class="modal hide fade in" style="display: none;">--%>
    <%--<div class="modal-header">--%>
        <%--<a class="close" data-dismiss="modal">×</a>--%>
        <%--<h3></h3>--%>
    <%--</div>--%>
    <%--<div class="modal-footer">--%>
        <%--<a href="#" class="btn" data-dismiss="modal">Close</a>--%>
    <%--</div>--%>
<%--</div>--%>


<script src="<c:url value='/static/js/jquery-1.9.1.min.js' />"></script>
<script src="<c:url value='/static/bootstrap/js/bootstrap.min.js' />"></script>
<script src="<c:url value='/static/js/redis-loader.js' />"></script>
<script src="<c:url value='/static/js/index.js' />"></script>

</body>
</html>