<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Изменить студента</title>
</head>
<body>
<% ResultSet student = (ResultSet) request.getAttribute("student"); %>
<form action="StudentServlet" method="post">
    <input type="hidden" name="id" value="<%= student.getInt("id") %>">
    Имя: <input type="text" name="name" value="<%= student.getString("name") %>"><br>
    Возраст: <input type="number" name="age" value="<%= student.getInt("age") %>"><br>
    Группа: <input type="text" name="group" value="<%= student.getString("group") %>"><br>
    Номер телефона: <input type="text" name="telephone_number" value="<%= student.getString("telephone_number") %>"><br>
    Электронная почта: <input type="text" name="email" value="<%= student.getString("email") %>"><br>
    <input type="hidden" name="action" value="update">
    <input type="submit" value="Сохранить">
</form>
<form action="index.jsp">
    <input type="submit" value="Обратно"/>
</form>
</body>
</html>
