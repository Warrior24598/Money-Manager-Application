package com.example.accounts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.accounts.recyclerviewadapters.AdapterExpenseLimitConfig;
import com.google.android.material.button.MaterialButton;

public class ExpenseLimitConfiguration extends AppCompatActivity
{

    RecyclerView recyclerExpenseLimit;
    MaterialButton btnEdit;
    ImageButton backButton;

    Boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_limit_configuration);

        recyclerExpenseLimit = findViewById(R.id.recyclerExpenseLimit);
        btnEdit = findViewById(R.id.btnEdit);
        backButton = findViewById(R.id.backButton);

        final AdapterExpenseLimitConfig adapter = new AdapterExpenseLimitConfig(this);

        recyclerExpenseLimit.setAdapter(adapter);
        recyclerExpenseLimit.setLayoutManager(new LinearLayoutManager(this));

        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!edit)
                {
                    btnEdit.setText("Save");
                }
                else
                {
                    btnEdit.setText("Edit");
                    adapter.save();
                }

                edit = !edit;
                adapter.setEditMode(edit);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }
}