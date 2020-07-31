package com.example.accounts.data;

public class Entry
{
    private int id;
    float amount;
    private String source,date,category,type;

    public Entry(int id, String source, float amount, String date, String category, String type)
    {
        this.id = id;
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.type = type;
    }

    public Entry(String source, float amount, String date, String category, String type)
    {
        this.id = 0;
        this.source = source;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.type = type;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getId()
    {
        return id;
    }

    public float getAmount()
    {
        return amount;
    }

    public String getSource()
    {
        return source;
    }

    public String getDate()
    {
        return date;
    }

    public String getCategory()
    {
        return category;
    }

    public String getType()
    {
        return type;
    }

    public String print()
    {
        String ret = "ID: "+id+"\n" +
                "SOURCE: "+source+"\n" +
                "AMOUNT: "+amount+"\n" +
                "DATE: "+date+"\n" +
                "CATEGORY: "+category+"\n" +
                "TYPE: "+type;

        return ret;
    }
}

