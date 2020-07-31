package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.models.Conversion;
import com.example.accounts.recyclerviewadapters.AdapterDays;

public class ListDays extends AppCompatActivity
{

    TextView txtTypeCategory,txtMonthYear;
    RecyclerView recyclerDays;

    DatabaseHandler dbHandler;

    String category, type,month;

    AdapterDays adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_days);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtTypeCategory = findViewById(R.id.txtTypeCategory);
        txtMonthYear= findViewById(R.id.txtMonthYear);
        recyclerDays = findViewById(R.id.recyclerDays);

        dbHandler = DatabaseHandler.getHandler(this);

        category = getIntent().getStringExtra(Constants.CATEGORY);
        type = getIntent().getStringExtra(Constants.TYPE);
        month = getIntent().getStringExtra(Constants.MONTH);

        adapter = new AdapterDays(this,month,category,type);


        //set adapter to recyclerview
        recyclerDays.setAdapter(adapter);
        recyclerDays.setLayoutManager(new LinearLayoutManager(this));


        //set Widget values
        txtTypeCategory.setText(type.toUpperCase()+" - "+category);
        txtMonthYear.setText(Conversion.getMonthName(month.split("/")[0])+" "+month.split("/")[1]);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.updateList();
    }
}
