package com.example.accounts.models;

public class Entry
{
    private int id;
    private float amount;
    private String source,date;
    private Category category;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    @Override
    public String toString()
    {
        return "Entry{" +
                "id=" + id +
                ", amount=" + amount +
                ", source='" + source + '\'' +
                ", date='" + date + '\'' +
                ", category=" + category +
                '}';
    }
}

