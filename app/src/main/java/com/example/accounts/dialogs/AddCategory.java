package com.example.accounts.dialogs;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.Category;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterCategories;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddCategory extends DialogFragment
{
    Context context;
    EntryType type;
    AdapterCategories adapter;

    SQLiteOpenHelper dbHandler;
    ICategoryService categoryService;
    IEntryService entryService;

    public AddCategory(Context context, EntryType type, AdapterCategories adapter)
    {
        this.context = context;
        this.type = type;
        this.adapter = adapter;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(context);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_add_category,container,false);
        view.setMinimumWidth(900);

        final TextInputEditText inputCategory = view.findViewById(R.id.inputCategory);
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd);
        inputCategory.setText("");

        //on clicking on button check if input is empty or not and if not then add category to database
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String categoryName = inputCategory.getText().toString();

                Category category = new Category();
                category.setName(categoryName);
                category.setType(type);

                if(!categoryName.trim().equals(""))
                {
                    categoryService.addCategory(category);
                    adapter.updateList();

                    getDialog().dismiss();
                }
            }
        });

        return view;
    }
}
