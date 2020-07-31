package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.R;
import com.example.accounts.recyclerviewadapters.AdapterDifference;

public class ListMonthDifference extends AppCompatActivity
{

    TextView txtYear;
    RecyclerView recyclerDifference;

    AdapterDifference adapter;

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
        recyclerDifference = findViewById(R.id.recyclerDifference);

        year = getIntent().getStringExtra(Constants.YEAR);

        adapter = new AdapterDifference(this,year);


        txtYear.setText(year);
        //set adapter to recycler view
        recyclerDifference.setAdapter(adapter);
        recyclerDifference.setLayoutManager(new LinearLayoutManager(this));


    }
}
