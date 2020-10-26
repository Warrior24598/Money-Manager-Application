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
import com.example.accounts.graphreports.datasource.YearlyDataSource;
import com.example.accounts.listings.ListDays;
import com.example.accounts.models.Conversion;
import com.example.accounts.models.EntryType;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MonthlyEntryTypeGraph extends AppCompatActivity
{
    private static final String TAG = "YearlyEntryTypeGraph";

    ImageButton backButton;
    TextView txtTitle;

    String year;

    SQLiteOpenHelper database;
    IEntryService entryService;
    ICategoryService categoryService;

    YearlyDataSource yearlyDataSource;
    CategoryDataSource categoryDataSource;

    LineChart entryComparisonLineGraph, incomeCategoriesLineChart, expenseCategoriesLineChart;
    PieChart incomePieGraph, expensePieGraph;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_entry_type_graph);

        backButton = findViewById(R.id.backButton);
        txtTitle = findViewById(R.id.txtTitle);

        entryComparisonLineGraph = findViewById(R.id.entryComparisonLineGraph);
        incomeCategoriesLineChart = findViewById(R.id.incomeCategoriesLineGraph);
        expenseCategoriesLineChart = findViewById(R.id.expenseCategoriesLineGraph);
        incomePieGraph = findViewById(R.id.incomePieGraph);
        expensePieGraph = findViewById(R.id.expensePieGraph);

        database = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createCategoryService();
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(database).createEntryService();

        yearlyDataSource = new YearlyDataSource(entryService);
        categoryDataSource = new CategoryDataSource(entryService,categoryService);

        year = getIntent().getStringExtra(Constants.YEAR);

        txtTitle.setText(year);

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });

        setEntryComparisonGraph();

        setEntryCategoriesGraph(incomeCategoriesLineChart,EntryType.INCOME);
        setEntryCategoriesGraph(expenseCategoriesLineChart,EntryType.EXPENSE);

        setEntryCategoriesPieGraph(incomePieGraph,EntryType.INCOME);
        setEntryCategoriesPieGraph(expensePieGraph,EntryType.EXPENSE);
    }

    private void setEntryComparisonGraph()
    {
        setLineGraph(entryComparisonLineGraph);

        entryComparisonLineGraph.setOnChartValueSelectedListener(new OnChartValueSelectedListener()
        {
            @Override
            public void onValueSelected(Entry e, Highlight h)
            {
                Log.e(TAG,"HighLight: "+h);
                String sType = entryComparisonLineGraph.getLineData().getDataSetByIndex(h.getDataSetIndex()).getLabel();
                String date = (h.getX()<10) ? "0"+(int)h.getX() : String.valueOf((int)h.getX());
                date+="/"+year;
                Log.e(TAG,"Type: "+sType);
                Log.e(TAG,"Date: "+date);

                EntryType type = (sType.equals(EntryType.INCOME.toString())) ? EntryType.INCOME : EntryType.EXPENSE;

                Intent listDays = new Intent(MonthlyEntryTypeGraph.this, ListDays.class);

                listDays.putExtra(Constants.MONTH,date);
                listDays.putExtra(Constants.TYPE,type.id);

                startActivity(listDays);
            }

            @Override
            public void onNothingSelected()
            {

            }
        });

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet incomeDataSet = yearlyDataSource.getMonthlyData(year,EntryType.INCOME);
        incomeDataSet.setColor(Color.rgb(104, 241, 175));
        incomeDataSet.setCircleColor(Color.rgb(104, 241, 175));

        LineDataSet expenseDataSet = yearlyDataSource.getMonthlyData(year,EntryType.EXPENSE);
        expenseDataSet.setColor(Color.rgb(255, 2, 102));
        expenseDataSet.setCircleColor(Color.rgb(255, 2, 102));

        dataSets.add(incomeDataSet);
        dataSets.add(expenseDataSet);

        LineData graphData = new LineData(dataSets);
        entryComparisonLineGraph.setData(graphData);
    }

    private void setEntryCategoriesGraph(final LineChart chart,final EntryType type)
    {
        setLineGraph(chart);

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

                Intent listDays = new Intent(MonthlyEntryTypeGraph.this, ListDays.class);

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

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        Map<String, List<Entry>> categoryData = categoryDataSource.getMonthData(type,year);

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
    private void setEntryCategoriesPieGraph(PieChart chart, EntryType type)
    {
        setPieGraph(chart);

        List<PieEntry> entries = categoryDataSource.getYearlyTotal(year,type);

        PieDataSet dataSet = new PieDataSet(entries,year+" > "+type.toString());

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
////
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());

        for(int temp=0;temp<entries.size();temp++)
        {
            colors.add(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
        }

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT);
        chart.setData(data);
    }

    private void setLineGraph(LineChart chart)
    {

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
        xAxis.setLabelRotationAngle(90);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return Conversion.getMonthName(String.valueOf((int)value));
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
        chart.getXAxis().setDrawGridLines(true);
    }

    private void setPieGraph(PieChart chart)
    {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);
//
//        chart.setCenterTextTypeface(Typeface.DEFAULT);
//        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);



        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTypeface(Typeface.DEFAULT);
        chart.setEntryLabelTextSize(12f);
    }
}