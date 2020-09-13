package com.example.accounts.models;

public class ExpenseConfig
{
    private Category category;
    private float limit;


    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public float getLimit()
    {
        return limit;
    }

    public void setLimit(float limit)
    {
        this.limit = limit;
    }

    @Override
    public String toString()
    {
        return "ExpenseConfig{" +
                "category=" + category +
                ", limit=" + limit +
                '}';
    }
}
