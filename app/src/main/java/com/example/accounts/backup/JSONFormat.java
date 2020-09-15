package com.example.accounts.backup;


import android.util.Log;

import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;
import com.example.accounts.models.ExpenseConfig;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONFormat implements IBackupFormat
{
    static final String CATEGORIES = "categories";
    static final String ENTRIES = "entries";
    static final String CONFIGS = "configs";
    private static final String TAG = "JSONFormat";
    @Override
    public String convertToString(List<Category> categories, List<Entry> entries, List<ExpenseConfig> configs)
    {
        JSONObject root = new JSONObject();

        root.put(CATEGORIES,writeCategories(categories));
        root.put(ENTRIES,writeEntries(entries));
        root.put(CONFIGS,writeExpenseLimitConfig(configs));

        Log.e(TAG,"Converting Root Json object to String");

        return root.toJSONString();
    }

    @Override
    public JSONArray writeCategories(List<Category> categories)
    {
        Log.e(TAG,"Converting Categories to JSONArray");

        JSONArray jsonArray = new JSONArray();

        for(Category c:categories)
        {
            JSONObject categoryJson = convertJson(c);

            jsonArray.add(categoryJson);
        }

        return jsonArray;
    }

    @Override
    public JSONArray writeEntries(List<Entry> entries)
    {
        Log.e(TAG,"Converting Entries to JSONArray");

        JSONArray jsonArray = new JSONArray();

        for(Entry e:entries)
        {
            JSONObject entryJson = convertJson(e);

            jsonArray.add(entryJson);
        }

        return jsonArray;
    }

    @Override
    public JSONArray writeExpenseLimitConfig(List<ExpenseConfig> configs)
    {
        Log.e(TAG,"Converting ExpenseLimitConfigs to JSONArray");

        JSONArray jsonArray = new JSONArray();

        for(ExpenseConfig e:configs)
        {
            JSONObject expenseConfigJson = convertJson(e);

            jsonArray.add(expenseConfigJson);
        }

        return jsonArray;
    }

    @Override
    public List getCategories(String fileContent)
    {
        JSONParser parser = new JSONParser();
        List<Category> categoryList = new ArrayList<>();

        try
        {
            JSONObject root = (JSONObject)parser.parse(fileContent);
            JSONArray categories = (JSONArray) root.get(CATEGORIES);

            Iterator iterator = categories.iterator();

            while(iterator.hasNext())
            {
                JSONObject object = (JSONObject) iterator.next();

                categoryList.add(getCategoryFromJson(object));
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return categoryList;
    }

    @Override
    public List getEntries(String fileContent)
    {
        JSONParser parser = new JSONParser();
        List<Entry> entryList = new ArrayList<>();

        try
        {
            JSONObject root = (JSONObject)parser.parse(fileContent);
            JSONArray entries = (JSONArray) root.get(ENTRIES);

            Iterator iterator = entries.iterator();

            while(iterator.hasNext())
            {
                JSONObject object = (JSONObject) iterator.next();
                entryList.add(getEntryFromJson(object));
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return entryList;
    }

    @Override
    public List getExpenseLimitConfig(String fileContent)
    {

        JSONParser parser = new JSONParser();
        List<ExpenseConfig> expenseConfigList = new ArrayList<>();

        try
        {
            JSONObject root = (JSONObject)parser.parse(fileContent);
            JSONArray expenseConfigs = (JSONArray) root.get(CONFIGS);

            Iterator iterator = expenseConfigs.iterator();

            while(iterator.hasNext())
            {
                JSONObject object = (JSONObject) iterator.next();
                expenseConfigList.add(getExpenseLimitConfigFromJson(object));
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return expenseConfigList;
    }



    private JSONObject convertJson(Category c)
    {
        JSONObject categoryJson = new JSONObject();

        categoryJson.put("id",c.getId());
        categoryJson.put("name",c.getName());
        categoryJson.put("type",c.getType().toString());

        return categoryJson;
    }

    private JSONObject convertJson(Entry e)
    {
        JSONObject entryJson = new JSONObject();

        entryJson.put("id",e.getId());
        entryJson.put("source",e.getSource());
        entryJson.put("amount",e.getAmount());
        entryJson.put("category",convertJson(e.getCategory()));
        entryJson.put("date",e.getDate());

        return entryJson;
    }

    private JSONObject convertJson(ExpenseConfig e)
    {
        JSONObject expenseConfigJson = new JSONObject();

        expenseConfigJson.put("limit",e.getLimit());
        expenseConfigJson.put("category",convertJson(e.getCategory()));

        return expenseConfigJson;
    }

    private Category getCategoryFromJson(JSONObject object)
    {
        Category category = new Category();

        category.setId((Integer.parseInt(String.valueOf(object.get("id")))));
        category.setType((((String)object.get("type")).equals(EntryType.INCOME.toString()))?EntryType.INCOME:EntryType.EXPENSE);
        category.setName((String)object.get("name"));
        return category;
    }

    private Entry getEntryFromJson(JSONObject object)
    {

        Entry entry = new Entry();

        entry.setId((Integer.parseInt(String.valueOf(object.get("id")))));
        entry.setSource((String)object.get("source"));
        entry.setAmount((Float.parseFloat(String.valueOf(object.get("amount")))));
        entry.setCategory(getCategoryFromJson((JSONObject) object.get("category")));
        return entry;
    }

    private ExpenseConfig getExpenseLimitConfigFromJson(JSONObject object)
    {
        ExpenseConfig config = new ExpenseConfig();

        config.setLimit(Float.parseFloat(String.valueOf(object.get("limit"))));
        config.setCategory(getCategoryFromJson((JSONObject) object.get("category")));

        return config;
    }
}
