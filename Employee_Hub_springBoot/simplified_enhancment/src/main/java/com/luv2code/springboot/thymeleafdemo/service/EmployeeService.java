package com.luv2code.springboot.thymeleafdemo.service;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(int theId);
    Employee save(Employee theEmployee);
    void deleteById(int theId);
    Employee findByUsername(String username);
    boolean updatePassword(String username, String email, String newPassword);

    void createPasswordResetTokenForUser(Employee employee, String token);
    String validatePasswordResetToken(String token);
    Employee getEmployeeByPasswordResetToken(String token);
    void changeEmployeePassword(Employee employee, String password);
}
