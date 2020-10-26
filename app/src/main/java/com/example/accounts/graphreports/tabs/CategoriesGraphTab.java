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
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.graphreports.MonthlyCategoryGraph;
import com.example.accounts.graphreports.MonthlyEntryTypeGraph;
import com.example.accounts.graphreports.datasource.CategoryDataSource;
import com.example.accounts.models.EntryType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CategoriesGraphTab extends Fragment
{
    private static final String TAG = "CategoriesGraphTab";
    LineChart incomeCategoryLineChart,expenseCategoryLineChart;
    SQLiteOpenHelper database;
    ICategoryService categoryService;
    IEntryService entryService;

    Random random = new Random();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_categories_graph,container,false);

        database = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(getContext());
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createEntryService();

        incomeCategoryLineChart = view.findViewById(R.id.incomeCategoryLineChart);
        expenseCategoryLineChart = view.findViewById(R.id.expenseCategoryLineChart);

        setData(incomeCategoryLineChart, EntryType.INCOME);
        setData(expenseCategoryLineChart, EntryType.EXPENSE);

        return view;
    }

    private void setData(LineChart chart,EntryType type)
    {
        setGraph(chart,type);

        CategoryDataSource dataSource = new CategoryDataSource(entryService,categoryService);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        Map<String, List<Entry>> categoryData = dataSource.getCategoryData(type);

        for(String category: categoryData.keySet())
        {
            LineDataSet dataSet = new LineDataSet(categoryData.get(category),category);
            dataSet.setLineWidth(2f);
            dataSet.setCircleRadius(4f);

            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

            dataSet.setColor(color);
            dataSet.setCircleColor(color);
            dataSets.add(dataSet);
        }

        LineData graphData = new LineData(dataSets);
        chart.setData(graphData);
    }


    private void setGraph(final LineChart chart, final EntryType type)
    {
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                Log.e(TAG,"HighLight: "+h);
                String category = chart.getLineData().getDataSetByIndex(h.getDataSetIndex()).getLabel();

                Intent monthlyCategoryGraph = new Intent(getContext(), MonthlyCategoryGraph.class);

                monthlyCategoryGraph.putExtra(Constants.YEAR,String.valueOf((int)h.getX()));
                monthlyCategoryGraph.putExtra(Constants.TYPE,type.id);
                monthlyCategoryGraph.putExtra(Constants.CATEGORY, category);

                startActivity(monthlyCategoryGraph);
            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setScaleYEnabled(true);

        chart.animateX(1000);
        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(Typeface.DEFAULT);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int)value);
            }
        });


        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
//        chart.setDrawBorders(true);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLines(false);
    }

}
