package com.example.accounts.addupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.Constants;
import com.example.accounts.SystemSingleTon;
import com.example.accounts.database.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.databaseService.ICategoryService;
import com.example.accounts.databaseService.IEntryService;
import com.example.accounts.databaseService.IExpenseLimitService;
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.example.accounts.models.EntryType;
import com.example.accounts.models.ExpenseConfig;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEntry extends AppCompatActivity
{

    TextView txtType;
    TextInputEditText inputDate, inputSource, inputAmount;
    MaterialButton btnAddEntry;
    ImageButton backButton;
    AppCompatSpinner spinCategory;

    Category category;
    EntryType type;

    SQLiteOpenHelper dbHandler;
    IEntryService entryService;
    ICategoryService categoryService;
    IExpenseLimitService expenseLimitService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtType = findViewById(R.id.txtType);
        inputDate = findViewById(R.id.inputDate);
        inputSource = findViewById(R.id.inputSource);
        inputAmount = findViewById(R.id.inputAmount);
        btnAddEntry = findViewById(R.id.btnAddEntry);
        backButton = findViewById(R.id.backButton);
        spinCategory = findViewById(R.id.spinCategory);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();
        expenseLimitService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createExpenseLimitService();

        category = categoryService.getCategory(getIntent().getIntExtra(Constants.CATEGORY,-1));
        type = EntryType.find(getIntent().getIntExtra(Constants.TYPE,-1));

        //set Widget values
        txtType.setText(type.toString());

        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        inputDate.setText(format.format(date));

        setSpinner();

        //set click listener for input date

        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Calendar c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEntry.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                    {
                        String m = String.valueOf(month + 1), d = String.valueOf(dayOfMonth);
                        if (month + 1 <= 9)
                            m = "0" + m;
                        if (dayOfMonth <= 9)
                            d = "0" + d;

                        String date = year+"-"+m+"-"+d;

                        inputDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });


        //check all the data and insert entry into database on clicking button

        btnAddEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //validate data

                String date = inputDate.getText().toString();
                String source = inputSource.getText().toString();
                String amount = inputAmount.getText().toString();
                if(date.trim().equals("") || source.trim().equals("") ||amount.trim().equals(""))
                {
                    Toast.makeText(AddEntry.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                Entry entry = new Entry();
                entry.setCategory(category);
                entry.setDate(date);
                entry.setSource(source);
                entry.setAmount(Float.parseFloat(amount));

                entryService.addEntry(entry);

                Toast.makeText(AddEntry.this, "Data Entered Successfully", Toast.LENGTH_SHORT).show();

                checkCategoryLimit(category);

                inputSource.setText("");
                inputAmount.setText("");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });
    }

    void setSpinner()
    {
        List<Category> categories = categoryService.getCategories(category.getType());

        ArrayAdapter<Category> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,categories);

        spinCategory.setAdapter(adapter);

        spinCategory.setSelection(getIndexOf(category,categories));
    }

    private int getIndexOf (Category object, List<Category> objectList)
    {
        for(Category o : objectList)
        {
            if(o.getId()==object.getId())
            {
                return objectList.indexOf(o);
            }
        }
        return -1;
    }

    void checkCategoryLimit(Category category)
    {
        if(category.getType()==EntryType.INCOME)
        {
            return;
        }
        ExpenseConfig config = expenseLimitService.getExpenseLimit(category);

        if(config==null)
        {
            return;
        }

        float categoryTotal = entryService.getGrandTotal(category,category.getType());
        float categoryLimit = config.getLimit();

        if(categoryTotal>=categoryLimit)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Limit Exceeded");
            builder.setMessage("Expense Limit Exceeded for Category: "+category.getName()+"\nLimit: "+categoryLimit+"\nExpense: "+categoryTotal);

            builder.setNegativeButton("Okay", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    }
}
