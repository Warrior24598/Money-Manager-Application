package com.example.accounts.models;

public class Category
{
    private String name;
    private int id;
    private EntryType type;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public EntryType getType()
    {
        return type;
    }

    public void setType(EntryType type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Category{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", type=" + type +
                '}';
    }
}
