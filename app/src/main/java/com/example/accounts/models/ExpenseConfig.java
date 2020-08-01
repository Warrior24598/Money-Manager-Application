package com.example.accounts.models;

public class ExpenseConfig
{
    private int id;
    private Category category;
    private float limit;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

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
}
