package com.example.accounts.graphreports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.accounts.Constants;
import com.example.accounts.R;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.graphreports.datasource.CategoryDataSource;
import com.example.accounts.listings.ListDays;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MonthlyCategoryGraph extends AppCompatActivity
{
    private static final String TAG = "MonthlyCategoryGraph";
    ImageButton backButton;
    TextView txtTitle;

    LineChart monthlyCategoryGraph;

    String year, category;
    EntryType type;

    SQLiteOpenHelper database;
    IEntryService entryService;
    ICategoryService categoryService;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_category_graph);

        backButton = findViewById(R.id.backButton);
        txtTitle = findViewById(R.id.txtTitle);
        monthlyCategoryGraph = findViewById(R.id.monthlyCategoryGraph);

        year = getIntent().getStringExtra(Constants.YEAR);
        category = getIntent().getStringExtra(Constants.CATEGORY);
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,0));

        database = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createEntryService();

        txtTitle.setText(year+" > "+type.toString()+" > "+category);

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        setData(monthlyCategoryGraph,type);
    }

    private void setData(LineChart chart,EntryType type)
    {
        setGraph(chart,type);

        CategoryDataSource dataSource = new CategoryDataSource(entryService,categoryService);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        LineDataSet dataSet = dataSource.getMonthData(category,type,year);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);

        dataSets.add(dataSet);

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
            String date = (h.getX()<10) ? "0"+(int)h.getX() : String.valueOf((int)h.getX());
            date+="/"+year;
            Log.e(TAG,"Category: "+category);
            Log.e(TAG,"Date: "+date);

            Intent listDays = new Intent(MonthlyCategoryGraph.this, ListDays.class);

            listDays.putExtra(Constants.MONTH,date);
            listDays.putExtra(Constants.TYPE,type.id);
            listDays.putExtra(Constants.CATEGORY, categoryService.getCategory(category,type).getId());

            startActivity(listDays);
        }

        @Override
        public void onNothingSelected()
        {

        }
    });
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

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setDrawGridLines(false);
    }
}