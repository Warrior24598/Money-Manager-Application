package com.example.accounts.graphreports.datasource;

import android.util.Log;

import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.models.EntryType;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class YearlyDataSource
{
    private static final String TAG = "YearlyDataSource";
    IEntryService entryService;

    public YearlyDataSource(IEntryService entryService)
    {
        this.entryService = entryService;
    }

    public ArrayList<BarEntry> getData(EntryType type)
    {
        ArrayList<BarEntry> data = new ArrayList<>();
        List<String> years = entryService.getYears();

        Log.e(TAG,"List of Years for "+type.toString()+": "+years);

        for(String year: years)
        {
            Log.e(TAG,"Adding Bar Entry for year: "+year);
            BarEntry entry = new BarEntry(Float.parseFloat(year),entryService.getYearTotal(year,type));
            data.add(entry);
        }

        return data;
    }

    public LineDataSet getMonthlyData(String year, EntryType type)
    {
        List<Entry> entries = new ArrayList<>();

        for(int month = 1;month<=12;month++)
        {
            String sMonth = (month<10) ? "0"+month : String.valueOf(month);

            float monthTotal = entryService.getMonthTotal(sMonth+"/"+year,type);

            entries.add(new Entry((float)month,monthTotal));
        }

        LineDataSet dataSet = new LineDataSet(entries,type.toString());

        return dataSet;
    }
}
