package ru.istu.employmenthistoryapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
    String url = "jdbc:mysql://localhost:3306/employee";
    String username = "root";
    String password = "123456";

    public void addEmployee(Employee employee) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                String query = "INSERT INTO employee (id, surname, name, patronymic, dateofbirth, education, profession) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, employee.getId());
                pstmt.setString(2, employee.getSurName());
                pstmt.setString(3, employee.getName());
                pstmt.setString(4, employee.getPatronymic());
                pstmt.setString(5, employee.getDateBirth());
                pstmt.setString(6, employee.getEducation());
                pstmt.setString(7, employee.getProfession());
                pstmt.executeUpdate();

                javax.swing.JOptionPane.showMessageDialog(null, "Данные успешно добавлены!");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
    }

    public List<Employee> getAllEmployee() {
        List<Employee> employeeList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id, surname, name, patronymic, dateofbirth, education, profession FROM employee");

                while (resultSet.next()) {
                    Integer id = resultSet.getInt(1);
                    String surname = resultSet.getString(2);
                    String name = resultSet.getString(3);
                    String patronymic = resultSet.getString(4);
                    String dateofbirth = resultSet.getString(5);
                    String education = resultSet.getString(6);
                    String profession = resultSet.getString(7);

                    employeeList.add(new Employee(id, surname, name, patronymic, dateofbirth, education, profession));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
        return employeeList;
    }

    public void addJobHistory(Integer employeeId, List<Job> jobList) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                for (Job el : jobList) {
                    String query = "INSERT INTO job (id, info, date, grounds) VALUES(?,?,?,?)";
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    pstmt.setInt(1, employeeId);
                    pstmt.setString(2, el.getInfo());
                    pstmt.setString(3, el.getDate());
                    pstmt.setString(4, el.getGrounds());
                    pstmt.executeUpdate();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
    }

    public List<Job> getJobHistory(Integer employeeId) {
        List<Job> jobList = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT date, info, grounds FROM job WHERE id = " + employeeId);

                while (resultSet.next()) {
                    String date = resultSet.getString(1);
                    String inf = resultSet.getString(2);
                    String grounds = resultSet.getString(3);
                    jobList.add(new Job(date, inf, grounds));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
        return jobList;
    }

    public Integer getLastEmployeeId() {
        Integer lastId = 1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MAX(id) FROM employee");

                if (resultSet.next()) {
                    lastId = resultSet.getInt(1);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.printf("Ошибка подключения к базе данных %s!", url);
            System.out.println(ex);
        }
        return lastId;
    }
}
