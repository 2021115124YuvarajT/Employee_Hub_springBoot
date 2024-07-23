package com.luv2code.springboot.thymeleafdemo.service;

import com.luv2code.springboot.thymeleafdemo.dao.EmployeeRepository;
import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private Map<String, String> passwordResetTokens = new HashMap<>();

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository) {
        employeeRepository = theEmployeeRepository;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Employee findById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);
        Employee theEmployee = null;
        if (result.isPresent()) {
            theEmployee = result.get();
        } else {
            throw new RuntimeException("Did not find employee id - " + theId);
        }
        return theEmployee;
    }

    @Override
    public Employee save(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    @Override
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    @Override
    public boolean updatePassword(String username, String email, String newPassword) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee != null && employee.getEmail().equals(email)) {
            employee.setPassword(newPassword);
            employeeRepository.save(employee);
            return true;
        }
        return false;
    }

    @Override
    public void createPasswordResetTokenForUser(Employee employee, String token) {
        passwordResetTokens.put(token, employee.getUsername());
    }

    @Override
    public String validatePasswordResetToken(String token) {
        return passwordResetTokens.containsKey(token) ? null : "invalidToken";
    }

    @Override
    public Employee getEmployeeByPasswordResetToken(String token) {
        String username = passwordResetTokens.get(token);
        return employeeRepository.findByUsername(username);
    }

    @Override
    public void changeEmployeePassword(Employee employee, String password) {
        employee.setPassword(password);
        employeeRepository.save(employee);
        passwordResetTokens.remove(employee.getUsername());
    }
}
