package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Employee;
import com.luv2code.springboot.thymeleafdemo.service.EmailService;
import com.luv2code.springboot.thymeleafdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/employees")
public class PasswordResetController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "employees/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("username") String username, Model model) {
        Employee employee = employeeService.findByUsername(username);
        if (employee != null) {
            String token = UUID.randomUUID().toString();
            employeeService.createPasswordResetTokenForUser(employee, token);

            String resetUrl = "http://localhost:8080/employees/change-password?token=" + token;
            emailService.sendNewPasswordEmail(employee.getEmail(), resetUrl);

            model.addAttribute("message", "A password reset link has been sent to your email.");
        } else {
            model.addAttribute("error", "No account found with that username.");
        }
        return "employees/forgot-password";
    }

    @GetMapping("/change-password")
    public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
        String result = employeeService.validatePasswordResetToken(token);
        if (result != null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "employees/forgot-password";
        }
        model.addAttribute("token", token);
        return "employees/change-password";
    }

    @PostMapping("/change-password")
    public String savePassword(@RequestParam("token") String token,
                               @RequestParam("password") String password,
                               Model model) {
        String result = employeeService.validatePasswordResetToken(token);
        if (result != null) {
            model.addAttribute("error", "Invalid or expired token.");
            return "employees/change-password";
        }

        Employee employee = employeeService.getEmployeeByPasswordResetToken(token);
        if (employee != null) {
            employeeService.changeEmployeePassword(employee, password);
            model.addAttribute("message", "Password successfully updated.");
        } else {
            model.addAttribute("error", "An error occurred while updating the password.");
        }

        return "employees/change-password";
    }
}
