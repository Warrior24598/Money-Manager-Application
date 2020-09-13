package com.example.accounts.databaseService;

import com.example.accounts.models.Category;
import com.example.accounts.models.ExpenseConfig;

import java.util.List;

public interface IExpenseLimitService
{
    public void addExpenseLimit(ExpenseConfig expenseConfig);
    public void updateExpenseLimit(ExpenseConfig expenseConfig);
    public void deleteExpenseLimit(ExpenseConfig expenseConfig);

    public ExpenseConfig getExpenseLimit(Category c);
    public List<ExpenseConfig> getExpenseLimit();
    public float getTotalLimit();
}
