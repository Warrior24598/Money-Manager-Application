package com.example.accounts.listings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.addupdate.AddEntry;
import com.example.accounts.recyclerviewadapters.AdapterYears;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListYears extends AppCompatActivity
{
    TextView txtCategory, txtType;
    RecyclerView recyclerYears;
    FloatingActionButton fabAddEntry;

    String category, type;

    AdapterYears adapter;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_years);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtCategory = findViewById(R.id.txtCategory);
        txtType= findViewById(R.id.txtType);
        recyclerYears = findViewById(R.id.recyclerYears);
        fabAddEntry = findViewById(R.id.fabAddEntry);

        dbHandler = DatabaseHandler.getHandler(this);

        category = getIntent().getStringExtra(Constants.CATEGORY);
        type = getIntent().getStringExtra(Constants.TYPE);


        //setup apater and attach to recyclerview
        adapter = new AdapterYears(this,category,type);


        //-----------------------------------//
        //-------SET VALUES TO WIDGETS-------//
        //-----------------------------------//

        txtCategory.setText(category);
        txtType.setText(type.toUpperCase());

        recyclerYears.setAdapter(adapter);
        recyclerYears.setLayoutManager(new LinearLayoutManager(this));

        //set click listner to fab to add new entry

        fabAddEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent addEntry = new Intent(ListYears.this, AddEntry.class);

                addEntry.putExtra(Constants.CATEGORY,category);
                addEntry.putExtra(Constants.TYPE,type);

                startActivity(addEntry);
            }
        });


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        adapter.updateList();
    }
}
