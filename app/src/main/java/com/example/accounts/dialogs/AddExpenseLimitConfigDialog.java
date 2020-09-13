package com.example.accounts.dialogs;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;
import com.example.accounts.models.Category;
import com.example.accounts.models.ExpenseConfig;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddExpenseLimitConfigDialog extends DialogFragment
{
    private Category c;
    OnActionCompletedListner listner;

    TextView txtCategory;
    TextInputEditText inputLimit;
    MaterialButton btnSet;

    SQLiteOpenHelper dbHandler;
    IExpenseLimitService expenseLimitService;

    public AddExpenseLimitConfigDialog(Category c, OnActionCompletedListner listner)
    {
        this.c = c;
        this.listner = listner;

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(getContext());
        expenseLimitService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createExpenseLimitService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_add_expenes_limit_config,container,false);
        view.setMinimumWidth(900);

        txtCategory = view.findViewById(R.id.txtCategory);
        inputLimit = view.findViewById(R.id.inputLimit);
        btnSet = view.findViewById(R.id.btnSet);

        txtCategory.setText(c.getName());

        btnSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String limit = inputLimit.getText().toString();

                if(!limit.equals(""))
                {
                    float l = Float.parseFloat(limit);

                    ExpenseConfig config = new ExpenseConfig();
                    config.setLimit(l);
                    config.setCategory(c);

                    expenseLimitService.addExpenseLimit(config);

                    listner.OnActionComplete(config);

                    dismiss();
                }
            }
        });

        return view;
    }
}

