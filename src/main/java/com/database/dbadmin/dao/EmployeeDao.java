package com.database.dbadmin.dao;

import com.database.dbadmin.database.EmployeePostgresSql;
import com.database.dbadmin.models.Employee;
import java.util.List;
import java.util.regex.Pattern;

public class EmployeeDao {
    private final EmployeePostgresSql postgresSqlConnect;

    public EmployeeDao(){
        postgresSqlConnect = EmployeePostgresSql.getInstance();
    }

    public boolean addEmployee(Employee employee){
        if (!checkEmployee(employee)){
            return false;
        }
        postgresSqlConnect.writeToDb(employee);
        return true;
    }

    public void deleteEmployee(Long id){
        postgresSqlConnect.delete(id);
    }

    public List<Employee> getAllEmployee(){
        return postgresSqlConnect.readFromDb();
    }

    public boolean updateEmployee(Employee employee, boolean flag){
        if (!checkEmployee(employee)) {
            return false;
        }
        postgresSqlConnect.updateEmployee(employee, flag);
        return true;
    }

    public Employee getEmployee(String login, String password){
        return postgresSqlConnect.getEmployeeFromDb(login, password);
    }

    public boolean checkEmployee(Employee employee){
        String regexPhoneNumber = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
        String regexEmail = "^[A-Za-z\\d.+]+@.+$";
        Pattern pattern = Pattern.compile(regexEmail);
        Pattern patternPhoneNumber = Pattern.compile(regexPhoneNumber);
        if (employee.getName().isEmpty() || employee.getPassword().isEmpty()
                || employee.getEmail().isEmpty() || employee.getLogin().isEmpty()
                || employee.getPhoneNumber().isEmpty()
                || !pattern.matcher(employee.getEmail()).matches()
                || !patternPhoneNumber.matcher(employee.getPhoneNumber()).matches()) {
            return false;
        }
        return true;
    }
}
