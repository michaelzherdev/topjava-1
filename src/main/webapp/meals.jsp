<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://javawebinar.ru/functions" prefix="format" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<form method="post">
    <input type="submit" formaction="meals?action=new" value="New"/>
    <table>
        <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <tr style="${meal.exceed ? 'color:red' : 'color:green'}">
                <td>${meal.id}</td>
                <td>${format:formatLocalDateTime(meal.dateTime)}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><input type="submit" formaction="meals?action=edit&id=<c:out value='${meal.id}'/>" value="Edit"/></td>
                <td><input type="submit" formaction="meals?action=delete&id=<c:out value='${meal.id}'/>" value="Delete"/></td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>
</html>