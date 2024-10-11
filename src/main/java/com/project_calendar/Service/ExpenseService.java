package com.project_calendar.Service;

import com.project_calendar.DTO.ExpenseDTO;
import com.project_calendar.Entity.Category;
import com.project_calendar.Entity.Expense;
import com.project_calendar.Entity.User;
import com.project_calendar.Repository.CategoryRepository;
import com.project_calendar.Repository.ExpenseRepository;
import com.project_calendar.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(ExpenseService.class);


    public void saveExpense(ExpenseDTO expenseDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        User user = null;
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            System.out.println("-" + user);
        }
        Expense expense = new Expense();
        expense.setExpenseItem(expenseDTO.getExpenseItem());
        expense.setProduct(expenseDTO.getProduct());
        expense.setPrice(expenseDTO.getPrice());
        expense.setDate(expenseDTO.getDate());
        expense.setUser(user);

        Optional<Category> category = categoryRepository.findByName( expenseDTO.getCategoryName());

        if (category.isPresent()) {
            Long categoryId = category.get().getId();
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (categoryOpt.isPresent()) {
                expense.setCategory(categoryOpt.get());
            } else if (category.get().getName() != null && !category.get().getName().isEmpty()) {
                Category newCategory = new Category();
                newCategory.setName(category.get().getName());
                categoryRepository.save(newCategory);
                expense.setCategory(newCategory);
            }
        } else {
            Category newCategory = new Category();
            newCategory.setName(expenseDTO.getCategoryName());
            categoryRepository.save(newCategory);
            expense.setCategory(newCategory);
        }

        expenseRepository.save(expense);
    }




}
