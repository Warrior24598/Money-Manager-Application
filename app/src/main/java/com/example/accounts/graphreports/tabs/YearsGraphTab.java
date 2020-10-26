package com.example.accounts.graphreports.tabs;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.accounts.Constants;
import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.graphreports.MonthlyEntryTypeGraph;
import com.example.accounts.graphreports.datasource.YearlyDataSource;
import com.example.accounts.models.EntryType;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class YearsGraphTab extends Fragment
{
    private static final String TAG = "YearsGraphTab";

    BarChart yearlyBarChart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_years_graph,container,false);

        yearlyBarChart = view.findViewById(R.id.yearlyBarChart);

        yearlyBarChart.getDescription().setEnabled(false);
        yearlyBarChart.setPinchZoom(false);
        yearlyBarChart.setScaleXEnabled(true);
        yearlyBarChart.setScaleYEnabled(false);

        yearlyBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {

                Intent monthlyEntryTypeGraph = new Intent(getContext(), MonthlyEntryTypeGraph.class);

                monthlyEntryTypeGraph.putExtra(Constants.YEAR,String.valueOf((int)h.getX()));

                startActivity(monthlyEntryTypeGraph);
            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        yearlyBarChart.setDrawBarShadow(false);
        yearlyBarChart.animateY(1000);
        yearlyBarChart.setDrawGridBackground(false);


        Legend l = yearlyBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(Typeface.DEFAULT);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = yearlyBarChart.getXAxis();
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        });

        YAxis leftAxis = yearlyBarChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        yearlyBarChart.getAxisRight().setEnabled(false);
        
        setData();

        return view;
    }
    
    private void setData()
    {
        SQLiteOpenHelper database = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(getContext());
        IEntryService entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createEntryService();

        YearlyDataSource dataSource = new YearlyDataSource(entryService);

        float groupSpace = 0.1f;
        float barSpace = 0.05f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        ArrayList<BarEntry> incomeData = dataSource.getData(EntryType.INCOME);
        ArrayList<BarEntry> expenseData = dataSource.getData(EntryType.EXPENSE);

        BarDataSet incomeSet, expenseSet;

        // create 4 DataSets
        incomeSet = new BarDataSet(incomeData, "INCOME");
        incomeSet.setColor(Color.rgb(104, 241, 175));
        expenseSet = new BarDataSet(expenseData, "EXPENSE");
        expenseSet.setColor(Color.rgb(255, 2, 102));

        BarData data = new BarData(expenseSet, incomeSet);

        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTypeface(Typeface.DEFAULT);

        yearlyBarChart.setData(data);

        // specify the width each bar should have
        yearlyBarChart.getBarData().setBarWidth(barWidth);
//        // restrict the x-axis range
        yearlyBarChart.getXAxis().setAxisMinimum(incomeData.get(0).getX());
        yearlyBarChart.getXAxis().setAxisMaximum(expenseData.get(expenseData.size()-1).getX()+1);
        yearlyBarChart.getXAxis().setLabelCount(incomeData.size()+1);
//        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
//        yearlyBarChart.getXAxis().setAxisMaximum(startYear + yearlyBarChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
//        yearlyBarChart.getXAxis().setLabelCount(incomeData.size());
        yearlyBarChart.groupBars(incomeData.get(0).getX(), groupSpace, barSpace);
        yearlyBarChart.invalidate();
    }
}
