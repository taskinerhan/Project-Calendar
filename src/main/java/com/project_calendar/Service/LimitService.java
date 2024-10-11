package com.project_calendar.Service;

import com.project_calendar.Component.EmailProducer;
import com.project_calendar.DTO.LimitDTO;
import com.project_calendar.Entity.Expense;
import com.project_calendar.Entity.Limit;
import com.project_calendar.Entity.User;
import com.project_calendar.Repository.ExpenseRepository;
import com.project_calendar.Repository.LimitRepository;
import com.project_calendar.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LimitService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LimitRepository limitRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailProducer emailProducer;
    private static final Logger logger = LoggerFactory.getLogger(LimitService.class);

    public void saveLimit(LimitDTO limitDTO) {
        User user = userService.getAuthenticatedUser();
        String email = user.getEmail();
        user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (limitRepository.existsByUserIdAndMonthAndYear(Long.valueOf(user.getId()), limitDTO.getMonth(), limitDTO.getYear())) {
            throw new RuntimeException("There is already a limit record for this user : " + limitDTO.getMonth() + "/" + limitDTO.getYear());
        }

        Limit limit = new Limit();
        limit.setLimitAmount(limitDTO.getLimitAmount());
        limit.setMonth(limitDTO.getMonth());
        limit.setYear(limitDTO.getYear());
        limit.setUser(user);

        limitRepository.save(limit);
    }

    public void limitCalculate() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            List<Limit> limits = limitRepository.findByUserId(Long.valueOf(user.getId()));

            if (!limits.isEmpty()) {
                for (Limit limit : limits) {
                    List<Expense> expenses = expenseRepository.findByYearAndMonthAndUserId(
                            limit.getYear(), limit.getMonth(), Long.valueOf(user.getId()));

                    double totalPrice = expenses.stream().mapToDouble(Expense::getPrice).sum();

                    if (totalPrice >= limit.getLimitAmount()) {
                        String content = "Limit Amount Exceeded: " + limit.getLimitAmount() + " Total Price: " + totalPrice
                                + " Date: " + limit.getMonth() + "-" + limit.getYear();

                        String emailContent = user.getEmail() + ";Limit Exceeded Notification;" + content;

                        emailProducer.sendEmail(emailContent);
                    }
                }
            }
        }
    }
}
