package com.example.lr5;

import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/students";
    private static final String jdbcUsername = "root";
    private static final String jdbcPassword = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            createStudent(request, response);
        } else if ("update".equals(action)) {
            updateStudent(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("list".equals(action)) {
            listStudents(request, response);
        } else if ("edit".equals(action)) {
            showEditForm(request, response);
        } else if ("delete".equals(action)) {
            deleteStudent(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    private void createStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String group = request.getParameter("group");
        String telephone_number = request.getParameter("telephone_number");
        String email = request.getParameter("email");

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO students_data (name, age, `group`, telephone_number, email) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, group);
            preparedStatement.setString(4, telephone_number);
            preparedStatement.setString(5, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("StudentServlet?action=list");
    }

    private void listStudents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students_data")) {

            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            out.println("<html><body><h2>Данные о студентах</h2><table border='1'><tr><th>ID</th><th>Имя</th><th>Возраст</th><th>Группа</th><th>Номер телефона</th><th>Электронная почта</th><th>Действия</th></tr>");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String group = resultSet.getString("group");
                String telephone_number = resultSet.getString("telephone_number");
                String email = resultSet.getString("email");

                out.println("<tr>" +
                        "<td>" + id + "</td>" +
                        "<td>" + name + "</td>" +
                        "<td>" + age + "</td>" +
                        "<td>" + group + "</td>" +
                        "<td>" + telephone_number + "</td>" +
                        "<td>" + email + "</td>" +
                        "<td><a href='StudentServlet?action=edit&id=" + id + "'>Изменить</a> | <a href='StudentServlet?action=delete&id=" + id + "'>Удалить</a ></td>" +
                        "</tr>");
            }

            out.println("</table></body></html>");
            out.println("<form action=\"index.jsp\">\n" +
                    "    <input type=\"submit\" value=\"Обратно\" />\n" +
                    "</form>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM students_data WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                request.setAttribute("student", resultSet);
                request.getRequestDispatcher("edit_student.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String group = request.getParameter("group");
        String telephone_number = request.getParameter("telephone_number");
        String email = request.getParameter("email");

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE students_data SET name = ?, age = ?, `group` = ?, telephone_number = ?, email = ? WHERE id = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, group);
            preparedStatement.setString(4, telephone_number);
            preparedStatement.setString(5, email);
            preparedStatement.setInt(6, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("StudentServlet?action=list");
    }

    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM students_data WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        response.sendRedirect("StudentServlet?action=list");
    }
}
