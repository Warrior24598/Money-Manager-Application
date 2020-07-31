package com.example.accounts.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.recyclerviewadapters.AdapterCategories;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddCategory extends DialogFragment
{
    Context context;
    String type;
    AdapterCategories adapter;

    DatabaseHandler dbHandler;

    public AddCategory(Context context, String type, AdapterCategories adapter)
    {
        this.context = context;
        this.type = type;
        this.adapter = adapter;

        dbHandler = DatabaseHandler.getHandler(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_add_category,container,false);
        view.setMinimumWidth(900);

        final TextInputEditText inputCategory = view.findViewById(R.id.inputCategory);
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd);

        //on clicking on button check if input is empty or not and if not then add category to database
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String category = inputCategory.getText().toString();

                if(!category.trim().equals(""))
                {
                    dbHandler.addCategory(category,type);
                    adapter.categories.add(category);
                    adapter.notifyDataSetChanged();

                    getDialog().dismiss();
                }
            }
        });

        return view;
    }
}
