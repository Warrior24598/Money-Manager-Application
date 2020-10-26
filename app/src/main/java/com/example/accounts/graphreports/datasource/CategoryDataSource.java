package com.example.accounts.graphreports.datasource;

import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDataSource
{
    IEntryService entryService;
    ICategoryService categoryService;

    public CategoryDataSource(IEntryService entryService, ICategoryService categoryService)
    {
        this.entryService = entryService;
        this.categoryService = categoryService;
    }

    public Map<String,List<Entry>> getCategoryData(EntryType type)
    {
        Map<String,List<Entry>> lineData = new HashMap<>();

        List<String> years = entryService.getYears(type);
        List<Category> categories = categoryService.getCategories(type);

        for(Category category: categories)
        {
            ArrayList<Entry> categoriesData = new ArrayList<>();
            for(String year: years)
            {
                Entry e = new Entry(Float.parseFloat(year),entryService.getYearTotal(year,category,type));
                categoriesData.add(e);
            }

            lineData.put(category.getName(),categoriesData);
        }

        return lineData;
    }

    public LineDataSet getMonthData(String categoryName, EntryType type, String year)
    {
        Category category = categoryService.getCategory(categoryName,type);

        LineDataSet dataSet = new LineDataSet(getMonthlyData(year,category,type),categoryName);

        return dataSet;
    }

    public Map<String,List<Entry>> getMonthData(EntryType type,String year)
    {
        {
            Map<String,List<Entry>> lineData = new HashMap<>();

            List<Category> categories = categoryService.getCategories(type);

            for(Category category: categories)
            {
                List<Entry> categoriesData = getMonthlyData(year,category,type);

                lineData.put(category.getName(),categoriesData);
            }

            return lineData;
        }
    }

    public List<PieEntry> getYearlyTotal(String year, EntryType type)
    {
        List<PieEntry> entries = new ArrayList<>();

        List<Category> categories = categoryService.getCategories(type);

        for(Category category: categories)
        {
            PieEntry entry = new PieEntry(entryService.getYearTotal(year,category,type),category.getName());
            entries.add(entry);
        }

        return entries;
    }

    private List<Entry> getMonthlyData(String year, Category category, EntryType type)
    {
        List<Entry> entries = new ArrayList<>();

        for(int month = 1;month<=12;month++)
        {
            String sMonth = (month<10) ? "0"+month : String.valueOf(month);

            float monthTotal = entryService.getMonthTotal(sMonth+"/"+year,category,type);

            entries.add(new Entry((float)month,monthTotal));
        }

        return entries;
    }
}
