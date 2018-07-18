<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>

<html>

<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <title>Insert title here</title>
    <script type=""></script>
</head>

<body>

<form action="/zframe/login" method="post">
    <input name="account">
    <input name="password">
    <input type="checkbox" name="rememberMe">
    <input type="submit">
</form>

</body>

</html>