package com.project_calendar.Controller;

import com.project_calendar.DTO.ExpenseDTO;
import com.project_calendar.DTO.ExpenseSummaryDTO;
import com.project_calendar.DTO.LimitDTO;
import com.project_calendar.Entity.Category;
import com.project_calendar.Entity.Expense;
import com.project_calendar.Entity.Limit;
import com.project_calendar.Entity.User;
import com.project_calendar.Repository.ExpenseRepository;
import com.project_calendar.Service.CategoryService;
import com.project_calendar.Service.ExpenseService;
import com.project_calendar.Service.LimitService;
import com.project_calendar.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private LimitService limitService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    @GetMapping("total-expense.html")
    public String blank() {
        return "monthly-list.html";
    }

    @GetMapping("/add-expense.html")
    public String showExpenseForm(Model model) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        model.addAttribute("expenseDTO", expenseDTO);
        return "add-expense.html";
    }
    @PostMapping("/add-expense.html")
    public String createExpense(@ModelAttribute ExpenseDTO expenseDTO, Model model) {
        System.out.println(expenseDTO);
        expenseService.saveExpense(expenseDTO);
       return "add-expense.html";
    }
    @GetMapping("expense-limit.html")
    public String getLimit(Model model){
        LimitDTO limitDTO = new LimitDTO();
        model.addAttribute("limitDTO",limitDTO);
        return "expense-limit.html";
    }
    @PostMapping("expense-limit.html")
    public String postExpense(Model model, @ModelAttribute LimitDTO limitDTO) {
        System.out.println(limitDTO);
        limitService.saveLimit(limitDTO);
        return "expense-limit.html";
    }

    @PostMapping("/job/")
    public String limitJob(Model model,@ModelAttribute LimitDTO limitDTO){

        return "expense-limit.html";
    }
    @PostMapping("ajax/{month}/{year}")
    public ResponseEntity<ExpenseSummaryDTO> getMonth(@PathVariable int month, @PathVariable int year) {

        List<Expense> expenses = expenseRepository.findByMonthAndYear(month, year);
        List<ExpenseDTO> expenseDTOs = new ArrayList<>();

        double total = expenses.stream()
                .mapToDouble(Expense::getPrice)
                .sum();

        for (Expense expense : expenses) {
            ExpenseDTO expenseDTO = new ExpenseDTO();
            expenseDTO.setProduct(expense.getProduct());
            expenseDTO.setExpenseItem(expense.getExpenseItem());
            expenseDTO.setPrice(expense.getPrice());
            expenseDTOs.add(expenseDTO);
        }
        return ResponseEntity.ok(new ExpenseSummaryDTO(expenseDTOs, total));
    }
    @PostMapping("ajax/{day}/{month}/{year}")
    public ResponseEntity<List<ExpenseDTO>> getDate(@PathVariable int day,@PathVariable int month, @PathVariable int year) {
        List<Expense> expenses = expenseRepository.findByDayAndMonthAndYear(day,month, year);
        List<ExpenseDTO> expenseDTOs = new ArrayList<>();

        for (Expense expense : expenses) {
            ExpenseDTO expenseDTO = new ExpenseDTO();

            expenseDTO.setProduct(expense.getProduct());
            expenseDTO.setExpenseItem(expense.getExpenseItem());
            expenseDTO.setPrice(expense.getPrice());
            expenseDTO.setDate(expense.getDate());

            expenseDTOs.add(expenseDTO);
        }

        return ResponseEntity.ok(expenseDTOs);
    }
    @GetMapping("ajax/colors/{day}/{month}/{year}")
    public ResponseEntity<Double> getSumList(@PathVariable int day, @PathVariable int month, @PathVariable int year) {
        List<Expense> expenses = expenseRepository.findByDayAndMonthAndYear(day,month, year);
         double total=0;
         total = expenses.stream()
                .mapToDouble(Expense::getPrice)
                .sum();

        return ResponseEntity.ok(total);
    }


    @PostMapping({"/ajax/chart/{year}", "/ajax/chart"})
    public ResponseEntity<List<Map.Entry<String, Double>>> getChart(@PathVariable(value = "year", required = false) Integer year) {

        int actualYear = (year != null) ? year : Year.now().getValue();
        User user=userService.getAuthenticatedUser();

        List<Expense> expenses = expenseRepository.findByYearANDUserId(actualYear, Long.valueOf(user.getId()));
        Map<Month, Double> monthlyExpenses = new EnumMap<>(Month.class);

        for (Expense expense : expenses) {
            LocalDate date = expense.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            Month month = date.getMonth();
            double price = expense.getPrice();
            monthlyExpenses.put(Month.valueOf(month.toString()), monthlyExpenses.getOrDefault(month, 0.0) + price);
        }

        List<Map.Entry<String, Double>> expenseList = monthlyExpenses.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().name(), entry.getValue()))
                .collect(Collectors.toList());
        System.out.println(expenseList);

        return ResponseEntity.ok(expenseList);
    }
}
