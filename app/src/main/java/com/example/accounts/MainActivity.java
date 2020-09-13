package com.example.accounts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionMenuView;
import androidx.viewpager.widget.ViewPager;

import com.example.accounts.tabfragments.DifferenceTab;
import com.example.accounts.tabfragments.ExpenseTab;
import com.example.accounts.tabfragments.IncomeTab;
import com.google.android.material.tabs.TabLayout;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";

    ActionMenuView optionMenu;
    ViewPager view_pager;
    SectionPageAdapter sectionPageAdapter;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //------------INITIALIZATION-------------//

        optionMenu = findViewById(R.id.option_menu);
        view_pager = findViewById(R.id.view_pager);
        sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        tabLayout = findViewById(R.id.tabs);

        Log.e(TAG,"DONE INTIALIZATION");

        //-----------SETTING UP VIEW PAGER--------//
        sectionPageAdapter.addPage(new ExpenseTab(),"EXPENSE");
        sectionPageAdapter.addPage(new IncomeTab(),"INCOME");
        sectionPageAdapter.addPage(new DifferenceTab(),"DIFFERENCE");

        view_pager.setAdapter(sectionPageAdapter);

        tabLayout.setupWithViewPager(view_pager);

        Log.e(TAG,"DONE SETTING VIEW PAGER");

        setOptionMenu();

        Log.e(TAG,"DONE SETTING OPTIONS");

        checkWritePermission();

    }

    public void setOptionMenu()
    {
        final PopupMenu menu = new PopupMenu(this,optionMenu);

        menu.inflate(R.menu.main_menu);

        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.option_import:

                        new MaterialFilePicker()
                                .withActivity(MainActivity.this)
                                .withRequestCode(1)
                                .withFilter(Pattern.compile(".*\\.exp$")) // Filtering files and directories by file name using regexp
                                .withHiddenFiles(false) // Show hidden files and folders
                                .start();
                        return true;

                    case R.id.option_export:

                        return true;
                    case R.id.option_expenseLimitConfig:
                        Intent expenseConfigIntent = new Intent(MainActivity.this,ExpenseLimitConfiguration.class);
                        startActivity(expenseConfigIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        optionMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                menu.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            String fileName = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
        }
    }
    private boolean checkWritePermission()
    {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1001);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case 1001:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Request Granted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "Request Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }
}