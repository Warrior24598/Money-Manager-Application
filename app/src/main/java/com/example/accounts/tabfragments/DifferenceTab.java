package com.example.accounts.tabfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.accounts.Constants;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.recyclerviewadapters.AdapterDifferenceYear;

public class DifferenceTab extends Fragment
{

    RecyclerView recyclerYears;
    TextView txtGrandTotal;

    DatabaseHandler dbHandler;

    AdapterDifferenceYear adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_difference,container,false);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        recyclerYears = view.findViewById(R.id.recyclerYears);
        txtGrandTotal = view.findViewById(R.id.txtGrandTotal);

        dbHandler = DatabaseHandler.getHandler(getContext());

        adapter = new AdapterDifferenceYear(getContext());


        //set Adapter to recyclerview
        recyclerYears.setAdapter(adapter);
        recyclerYears.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        float totalIncome = dbHandler.getGrandTotal(Constants.ALLCATS,Constants.TYPE_INCOME);
        float totalExpense = dbHandler.getGrandTotal(Constants.ALLCATS,Constants.TYPE_EXPENSE);
        float total = totalIncome - totalExpense;

        txtGrandTotal.setText("Grand Total: "+total);
    }
}
