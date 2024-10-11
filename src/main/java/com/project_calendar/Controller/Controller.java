package com.project_calendar.Controller;

import com.project_calendar.DTO.UserDTO;
import com.project_calendar.Entity.User;
import com.project_calendar.Service.UserDetailsServiceImpl;
import com.project_calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/page-register.html")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "page-register";
    }

    @PostMapping("/page-register.html")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO) {
        userService.registerNewUser(userDTO);
        return "redirect:/page-login.html";
    }

    @GetMapping("/page-login.html")
    public String showLoginForm() {
        return "page-login";
    }

    @PostMapping("/page-login.html")
    public String processLogin(@RequestParam String email, @RequestParam String password, Model model) {
        System.out.println(email);
        System.out.println(password);

        boolean loginSuccess = authenticateUser(email, password);
        if (loginSuccess) {
            return "redirect:/app-calender.html";
        } else {
            model.addAttribute("error", true);
            return "page-login";
        }
    }
    private boolean authenticateUser(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, userDetails.getPassword());
    }
    @GetMapping("/app-calender.html")
    public String getCalendar() {
        return "app-calender.html";
    }
    @GetMapping("/chart-line.html")
    public String getChart() {
        return "chart-line.html";
    }
    @GetMapping("/monthly-list.html")
    public String getLock() {
        return "monthly-list.html";
    }

    @GetMapping("/email-inbox.html")
    public String getEmail() {
        return "email-inbox.html";
    }
    @GetMapping("/app-profile.html")
    public String getProfile(Model model) {
        User user = userService.getAuthenticatedUser();
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("email", user.getEmail());
        return "app-profile.html";
    }
}

