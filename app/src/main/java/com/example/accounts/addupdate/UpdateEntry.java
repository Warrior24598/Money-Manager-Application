package com.example.accounts.addupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
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
import com.example.accounts.models.Category;
import com.example.accounts.models.Entry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;

public class UpdateEntry extends AppCompatActivity
{
    TextView txtType;
    TextInputEditText inputDate, inputSource, inputAmount;
    MaterialButton btnUpdateEntry;
    AppCompatSpinner spinCategory;
    ImageButton backButton;

    List<Category> categories;

    int id;

    SQLiteOpenHelper dbHandler;
    IEntryService entryService;
    ICategoryService categoryService;

    Entry e;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtType = findViewById(R.id.txtType);
        inputDate = findViewById(R.id.inputDate);
        inputSource = findViewById(R.id.inputSource);
        inputAmount = findViewById(R.id.inputAmount);
        spinCategory = findViewById(R.id.spinCategory);
        btnUpdateEntry= findViewById(R.id.btnUpdateEntry);
        backButton = findViewById(R.id.backButton);

        dbHandler = SystemSingleTon.instance().getDatabaseAbstractFactory().createDatabaseHandler(this);
        entryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createEntryService();
        categoryService = SystemSingleTon.instance().getDatabaseServiceAbstractFactory(dbHandler).createCategoryService();

        id = getIntent().getIntExtra(Constants.ID,0);

        e = entryService.getEntry(id);
        e.setDate(formatDate(e.getDate()));

        //set Widget values
        txtType.setText(e.getCategory().getType().toString());

        inputDate.setText(e.getDate());
        inputSource.setText(e.getSource());
        inputAmount.setText(String.valueOf(e.getAmount()));

        //set spinner
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEntry.this, new DatePickerDialog.OnDateSetListener()
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

        btnUpdateEntry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //validate data

                String date = inputDate.getText().toString();
                String source = inputSource.getText().toString();
                String amount = inputAmount.getText().toString();
                Category category = (Category) spinCategory.getSelectedItem();
                if(date.trim().equals("") || source.trim().equals("") ||amount.trim().equals("")||category==null)
                {
                    Toast.makeText(UpdateEntry.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                e.setDate(date);
                e.setSource(source);
                e.setAmount(Float.parseFloat(amount));
                e.setCategory(category);

                entryService.updateEntry(e);


                Toast.makeText(UpdateEntry.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                finish();

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
        categories = categoryService.getCategories(e.getCategory().getType());

        ArrayAdapter<Category> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,categories);

        spinCategory.setAdapter(adapter);

        spinCategory.setSelection(getIndexOf(e.getCategory(),categories));
    }

    private int getIndexOf (Category object,List<Category> objectList)
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
    
    private String formatDate(String date)
    {
        String formattedDate = "";
        String[] parts = date.split("/");

        formattedDate+=parts[2];
        formattedDate+="-";
        formattedDate+=parts[1];
        formattedDate+="-";
        formattedDate+=parts[0];

        return formattedDate;
    }
}
