package com.example.accounts.models;

import java.util.HashMap;
import java.util.Map;

public class ExpenseConfig
{
    private static Map<Integer,Float> config = new HashMap<>();

    public static void addConfig(Category category, float limit)
    {
        config.put(category.getId(),limit);
    }

    public static float getConfig(Category category)
    {
        return config.get(category.getId());
    }
}
