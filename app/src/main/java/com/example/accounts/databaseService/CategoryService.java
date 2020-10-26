package com.example.accounts.databaseService;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;

import java.util.ArrayList;
import java.util.List;

import static com.example.accounts.database.DatabaseConfig.*;

public class CategoryService implements ICategoryService
{
    private static final String TAG = "CategoryService";
    SQLiteOpenHelper helper;

    public CategoryService(SQLiteOpenHelper helper)
    {
        this.helper = helper;
    }

    @Override
    public void addCategory(Category category)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NAME,category.getName());
        values.put(COL_TYPE_ID,category.getType().id);

        database.insert(TABLE_CATEGORY,null,values);
        database.close();

        Log.e(TAG,"CATEGORY ADDED: \n"+category.getName()+"\n"+category.getType().toString()+")");
    }

    @Override
    public void deleteCategory(Category category)
    {
        SQLiteDatabase database = helper.getWritableDatabase();

        String delQuery = "DELETE FROM "+TABLE_ENTRY+" WHERE "+
                COL_CATEGORY_ID +"="+category.getId()+";";

        database.execSQL(delQuery);

        Log.e(TAG,"ALL ENTRIES DELETED: CATEGORY ID: "+category.getName());

        delQuery = "DELETE FROM "+TABLE_CONFIG+" WHERE "+
                COL_EXPENSE_CATEGORY_ID +"="+category.getId()+";";

        database.execSQL(delQuery);

        Log.e(TAG,"ALL EXPENSE CONFIG DELETED: CATEGORY ID: "+category.getName());

        String delQ = "DELETE FROM "+TABLE_CATEGORY+" WHERE " +
                COL_ID+"="+category.getId();

        database.execSQL(delQ);

        Log.e(TAG,"CATEGORY DELETED: CATEGORY/TYPE: "+category.getName());
        database.close();
    }

    @Override
    public List getCategories(EntryType type)
    {
        Log.e(TAG,"Finding Categories for type: "+type.toString());
        List<Category> categories = new ArrayList<>();

        SQLiteDatabase database = helper.getReadableDatabase();

        String getCategoriesQuery = "SELECT * FROM "+TABLE_CATEGORY+" WHERE " +
                COL_TYPE_ID +"="+type.id+";";

        Log.e(TAG,getCategoriesQuery);

        Cursor c = database.rawQuery(getCategoriesQuery,null);
        c.moveToFirst();

        while(!c.isAfterLast())
        {
            Category category = new Category();
            category.setType(type);
            category.setName(c.getString(c.getColumnIndex(COL_NAME)));
            category.setId(c.getInt(c.getColumnIndex(COL_ID)));
            categories.add(category);
            c.moveToNext();
        }

        Log.e(TAG,"Total Categories: "+categories.size());
        database.close();

        return categories;
    }

    @Override
    public List getCategories()
    {
    Log.e(TAG,"All Categories ");
    List<Category> categories = new ArrayList<>();

    SQLiteDatabase database = helper.getReadableDatabase();

    String getCategoriesQuery = "SELECT * FROM "+TABLE_CATEGORY;

    Log.e(TAG,getCategoriesQuery);

    Cursor c = database.rawQuery(getCategoriesQuery,null);
    c.moveToFirst();

    while(!c.isAfterLast())
    {
        Category category = new Category();
        category.setType(EntryType.find(c.getInt(c.getColumnIndex(COL_TYPE_ID))));
        category.setName(c.getString(c.getColumnIndex(COL_NAME)));
        category.setId(c.getInt(c.getColumnIndex(COL_ID)));
        categories.add(category);
        c.moveToNext();
    }

    Log.e(TAG,"Total Categories: "+categories.size());
    database.close();

    return categories;
}


    @Override
    public Category getCategory(int id)
    {
        Log.e(TAG,"Finding Category for id: "+id);

        SQLiteDatabase database = helper.getReadableDatabase();

        String getCategoriesQuery = "SELECT * FROM "+TABLE_CATEGORY+" WHERE " +
                COL_ID +"="+id+";";

        Log.e(TAG,getCategoriesQuery);

        return getCategoryFromQuery(getCategoriesQuery);
    }

    @Override
    public Category getCategory(String categoryName, EntryType type)
    {
        Log.e(TAG,"Finding Category for name: "+categoryName+", Type"+type.toString());

        String getCategoriesQuery = "SELECT * FROM "+TABLE_CATEGORY+" WHERE " +
                COL_NAME +"='"+categoryName+"' and "+COL_TYPE_ID+"="+type.id;

        Log.e(TAG,getCategoriesQuery);

        return getCategoryFromQuery(getCategoriesQuery);
    }

    @Override
    public void updateCategoryName(Category category)
    {

        SQLiteDatabase database = helper.getReadableDatabase();

        String updateCategoryQuery = "UPDATE "+TABLE_CATEGORY+" SET " +
                COL_NAME +"='"+category.getName()+"' "+
                "WHERE "+COL_ID+"="+category.getId();

        database.execSQL(updateCategoryQuery);
        database.close();
    }

    private Category getCategoryFromQuery(String query)
    {
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor c = database.rawQuery(query,null);
        if(false==c.moveToFirst())
        {
            Log.e(TAG, "Category Not Found");
            database.close();
            return null;
        }

        Category category = new Category();
        category.setType(EntryType.find(c.getInt(c.getColumnIndex(COL_TYPE_ID))));
        category.setName(c.getString(c.getColumnIndex(COL_NAME)));
        category.setId(c.getInt(c.getColumnIndex(COL_ID)));

        Log.e(TAG,"Category Found: "+category.toString());
        database.close();

        return category;
    }
}
