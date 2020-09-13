package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.EntryType;
import com.example.accounts.recyclerviewadapters.AdapterDifference;

public class ListMonthDifference extends AppCompatActivity
{

    TextView txtYear, txtTotalIncome, txtTotalExpense, txtTotalSaving;
    RecyclerView recyclerDifference;
    ImageButton backButton;

    AdapterDifference adapter;

    SQLiteOpenHelper dbHandler;
    IEntryService entryService;

    String year;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_month_difference);


        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtYear = findViewById(R.id.txtYear);
        txtTotalExpense = findViewById(R.id.txtTotalExpense);
        txtTotalIncome = findViewById(R.id.txtTotalIncome);
        txtTotalSaving = findViewById(R.id.txtTotalSaving);
        recyclerDifference = findViewById(R.id.recyclerDifference);
        backButton = findViewById(R.id.backButton);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();

        year = getIntent().getStringExtra(Constants.YEAR);

        adapter = new AdapterDifference(this,year);

        float totalExpense = entryService.getGrandTotal(EntryType.EXPENSE);
        float totalIncome = entryService.getGrandTotal(EntryType.INCOME);
        float totalSaving = totalIncome - totalExpense;

        txtYear.setText(year);
        txtTotalIncome.setText(String.valueOf(totalIncome));
        txtTotalExpense.setText(String.valueOf(totalExpense));
        txtTotalSaving.setText(String.valueOf(totalSaving));
        if(totalSaving>0)
        {
            txtTotalSaving.setBackgroundColor(Color.GREEN);
        }
        //set adapter to recycler view
        recyclerDifference.setAdapter(adapter);
        recyclerDifference.setLayoutManager(new LinearLayoutManager(this));

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
