package com.database.dbadmin.database;

import com.database.dbadmin.models.Employee;
import com.database.dbadmin.models.Role;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class EmployeePostgresSql {
    private final PostgresSqlConnect postgresSqlConnect;

    private EmployeePostgresSql() {
        postgresSqlConnect = PostgresSqlConnect.getInstance();
    }

    private static EmployeePostgresSql instance;

    public static EmployeePostgresSql getInstance(){
        return instance == null ? new EmployeePostgresSql() : instance;
    }

    public void writeToDb(@NotNull Employee employee) {
        String query = "INSERT INTO employee(login, password, name, email, \"phoneNumber\", age, role_id) " +
                "VALUES(?, ?, ?, ?, ?, ?, (SELECT role.role_id FROM role WHERE role_name=?))";

        try (PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)) {

            preparedStatement.setString(1, employee.getLogin());
            preparedStatement.setString(2, getPasswordHash(employee.getPassword()));
            preparedStatement.setString(3, employee.getName());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setInt(6, employee.getAge());
            preparedStatement.setString(7, employee.getRole_id().toString());
            preparedStatement.executeUpdate();

            System.out.println("Successfully created");
        } catch (SQLException e) {
            System.err.println("Error in writeToDb");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Employee> readFromDb() {
        String query = "SELECT * FROM employee";

        List<Employee> employeeList = new ArrayList<>();
        try (Statement statement = postgresSqlConnect.connection.createStatement()){
            ResultSet set = statement.executeQuery(query);

            while (set.next()){
                Employee employee = getEmployee(set);
                assert false;
                employeeList.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error in readFromDb");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return employeeList;
    }

    public void delete(long id) {
        String query = "DELETE FROM employee WHERE employee_id=?";
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)){
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex){
            System.out.println("err in deleteEmployee");
        }
    }

    public Employee getEmployeeFromDb(String login, String password) {
        Employee employee = null;
        String query = "SELECT * FROM employee WHERE login=? AND password=?";
        try (PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, getPasswordHash(password).toUpperCase());
            ResultSet set = preparedStatement.executeQuery();
            if (set.next()) {
                employee = getEmployee(set);
            }
        } catch (SQLException e) {
            System.err.println("Error in getEmployee");
        } catch (NoSuchAlgorithmException exception){
            System.out.println("no such algorithm");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return employee;
    }

    public void updateEmployee(Employee employee, boolean flag){
        String query = "UPDATE employee SET login=?, password=?, name=?, email=?, \"phoneNumber\"=?, age=? WHERE ID=?";
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)){
            String password;
            if (flag){
                password = employee.getPassword();
            } else {
                password = getPasswordHash(employee.getPassword());
            }
            preparedStatement.setString(1, employee.getLogin());
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, employee.getName());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setString(5, employee.getPhoneNumber());
            preparedStatement.setInt(6, employee.getAge());
            preparedStatement.setLong(7, employee.getEmployee_id());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error in update");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPasswordHash(@NotNull String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] bytes = messageDigest.digest(str.getBytes());
        StringBuilder strBuild = new StringBuilder();
        for (byte b : bytes) {
            strBuild.append(String.format("%02X", b));
        }

        return strBuild.toString();
    }

    private Employee getEmployee(ResultSet set) throws SQLException, NoSuchFieldException, IllegalAccessException {
        Employee employee = new Employee();
        ResultSetMetaData resultSetMetaData = set.getMetaData();
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnName(i);
            Object object = set.getObject(columnName);
            Field field = Employee.class.getDeclaredField(columnName);
            field.setAccessible(true);
            if (Objects.equals(columnName, "role_id")){
                object = getRole(Integer.parseInt(object.toString()));
            }
            field.set(employee, object);
        }
        return employee;
    }

    public Set<String> getRoles(){
        String query = "SELECT role_name FROM role";
        Set<String> roles = new HashSet<>();
        try(PreparedStatement preparedStatement = postgresSqlConnect.connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                roles.add(resultSet.getString("role_name"));
            }
            return roles;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Role getRole(int id){
        if (id == 1){
            return Role.ADMIN;
        } else if (id == 2) {
            return Role.USER;
        } else {
            return Role.TRAVEL_AGENT;
        }
    }
}
