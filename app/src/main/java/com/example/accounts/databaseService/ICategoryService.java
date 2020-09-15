package com.example.accounts.databaseService;

import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;

import java.util.List;

public interface ICategoryService
{

    void addCategory(Category category);

    void deleteCategory(Category category);

    List getCategories(EntryType type);

    List getCategories();

    Category getCategory(int id);

    void updateCategoryName(Category category);
}
