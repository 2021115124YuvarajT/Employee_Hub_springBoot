package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.entity.Notification;
import com.luv2code.springboot.thymeleafdemo.service.NotificationService;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final NotificationService notificationService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, NotificationService notificationService) {
        this.employeeService = employeeService;
        this.notificationService = notificationService;
    }

    // Mapping to list all employees
    @GetMapping("/list")
    public String listEmployees(Model model, @RequestParam(required = false, defaultValue = "false") boolean isAdmin) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);
        model.addAttribute("isAdmin", isAdmin); // Pass isAdmin to the view
        return "employees/list-employees";
    }

    // Mapping to show the form for adding a new employee
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "employees/employee-form";
    }



    // Mapping to show the form for updating an existing employee
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int id, Model model,
                                    @RequestParam(required = false, defaultValue = "false") boolean isAdmin) {
        Employee employee = employeeService.findById(id);
        model.addAttribute("employee", employee);
        model.addAttribute("isAdmin", isAdmin); // Pass isAdmin to the view

        return "employees/employee-form";
    }

    // Mapping to save an employee (for both add and update operations)
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,
                               @RequestParam(value = "isAdmin", required = false, defaultValue = "false") boolean isAdmin) {
        employeeService.save(employee);

            return "redirect:/employees/list?isAdmin=true";

    }


    // Mapping to delete an employee
    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("employeeId") int id) {
        employeeService.deleteById(id);
        return "redirect:/employees/list?isAdmin=true";
    }

    // Mapping to show the sign-up form
    @GetMapping("/signup")
    public String showSignUpForm(Model model) {
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "employees/signup-form";
    }

    // Mapping to process sign-up form submission
    @PostMapping("/signup")
    public String processSignUpForm(@ModelAttribute("employee") Employee employee, Model model) {
        // Check if the username already exists in the database
        Employee existingEmployee = employeeService.findByUsername(employee.getUsername());
        if (existingEmployee != null) {
            model.addAttribute("message", "Username '" + employee.getUsername() + "' already has an account.");
            return "employees/signup-form";
        }

        // If username does not exist, proceed with saving the employee
        employeeService.save(employee);
        return "redirect:/employees/list"; // Redirect to employee list after sign-up
    }

    // Mapping to show the login form
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/login";
    }

    // Remove or comment out this method to resolve the conflict
    // @GetMapping("/forgot-password")
    // public String showForgotPasswordForm() {
    //     return "employees/forgot-password";
    // }

    // Mapping to process login form submission
    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute("employee") Employee employee, Model model) {
        // Find employee by username
        Employee existingEmployee = employeeService.findByUsername(employee.getUsername());

        // Check if employee exists and password matches
        if (existingEmployee == null || !existingEmployee.getPassword().equals(employee.getPassword())) {
            model.addAttribute("error", "Invalid username or password.");
            return "employees/login";
        }

        // Check if the user is an admin
        boolean isAdmin = "admin".equalsIgnoreCase(existingEmployee.getRole());
        if (isAdmin) {
            // Redirect to employee list with isAdmin=true
            return "redirect:/employees/list?isAdmin=true";
        } else {
            // Redirect to employee list with isAdmin=false
            return "redirect:/employees/list?isAdmin=false";
        }
    }
}
