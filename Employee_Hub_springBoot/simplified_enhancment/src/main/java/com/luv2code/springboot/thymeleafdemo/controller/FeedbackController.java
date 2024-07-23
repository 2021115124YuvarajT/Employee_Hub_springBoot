package com.luv2code.springboot.thymeleafdemo.controller;

import com.luv2code.springboot.thymeleafdemo.entity.Feedback;
import com.luv2code.springboot.thymeleafdemo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fillFeeds/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/showFeedbackForm")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "employees/feedback-form"; // Thymeleaf template name
    }

    @PostMapping("/save")
    public String submitFeedback(@ModelAttribute Feedback feedback, Model model) {
        feedbackService.save(feedback);
        model.addAttribute("message", "Feedback submitted successfully.");
        return "employees/feedback-form"; // Thymeleaf template name
    }
}
