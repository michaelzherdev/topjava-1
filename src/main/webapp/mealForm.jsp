<%--
  Created by IntelliJ IDEA.
  User: mikhail
  Date: 08.11.17
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meal Form</title>
</head>
<body>
<div align="center">
    <form action="meals?action=${meal == null ? 'insert' : 'update'}" method="post">
        <table border="1">
            <caption>
                <h2>
                    ${meal == null ? 'Add New Meal' : 'Update Meal'}
                </h2>
            </caption>
            <c:if test="${meal != null}">
                <input type="hidden" name="id" value="${meal.id}"/>
            </c:if>
            <tr>
                <th>Date:</th>
                <td><input type="datetime-local" min="2000-01-01T00:00" max="2100-01-01T00:00" name="datetime" value="${meal.dateTime}"/></td>
            </tr>
            <tr>
                <th>Decription:</th>
                <td><input type="text" name="description" size="40" value="${meal.description}"/></td>
            </tr>
            <tr>
                <th>Calories:</th>
                <td><input type="number" name="calories" size="5" value="${meal.calories}"/></td>
            </tr>
            <tr>
                <td colspan="2" align="center"><input type="submit" value="Save"/></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
