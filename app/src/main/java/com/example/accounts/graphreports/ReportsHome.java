package com.example.accounts.graphreports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.accounts.R;
import com.example.accounts.SectionPageAdapter;
import com.example.accounts.graphreports.tabs.CategoriesGraphTab;
import com.example.accounts.graphreports.tabs.YearsGraphTab;
import com.google.android.material.tabs.TabLayout;

public class ReportsHome extends AppCompatActivity
{

    ImageButton backButton;
    ViewPager viewPager;
    TabLayout tabs;
    SectionPageAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_home);

        backButton = findViewById(R.id.backButton);
        viewPager = findViewById(R.id.view_pager);
        tabs = findViewById(R.id.tabs);

        pageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        pageAdapter.addPage(new YearsGraphTab(),"Years");
        pageAdapter.addPage(new CategoriesGraphTab(),"Categories");

        viewPager.setAdapter(pageAdapter);

        tabs.setupWithViewPager(viewPager);

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