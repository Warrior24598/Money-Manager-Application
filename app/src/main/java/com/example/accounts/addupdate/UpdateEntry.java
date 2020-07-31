package com.example.accounts.addupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.Constants;
import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.data.Entry;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UpdateEntry extends AppCompatActivity
{
    TextView txtCategory, txtType;
    TextInputEditText inputDate, inputSource, inputAmount;
    MaterialButton btnUpdateEntry;
    AppCompatSpinner spinCategory;

    List<String> categories;

    int id;

    DatabaseHandler dbHandler;

    Entry e;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_entry);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtCategory = findViewById(R.id.txtCategory);
        txtType = findViewById(R.id.txtType);
        inputDate = findViewById(R.id.inputDate);
        inputSource = findViewById(R.id.inputSource);
        inputAmount = findViewById(R.id.inputAmount);
        spinCategory = findViewById(R.id.spinCategory);
        btnUpdateEntry= findViewById(R.id.btnUpdateEntry);

        dbHandler = DatabaseHandler.getHandler(this);




        id = getIntent().getIntExtra(Constants.ID,0);

        e = dbHandler.getEntry(id);

        //set Widget values
        txtType.setText(e.getType().toUpperCase());
        txtCategory.setText(e.getCategory());

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
                String category = spinCategory.getSelectedItem().toString();
                if(date.trim().equals("") || source.trim().equals("") ||amount.trim().equals("")||category.trim().equals(""))
                {
                    Toast.makeText(UpdateEntry.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                e.setDate(date);
                e.setSource(source);
                e.setAmount(Float.parseFloat(amount));
                e.setCategory(category);

                dbHandler.updateEntry(e);


                Toast.makeText(UpdateEntry.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();

                finish();

            }
        });
    }

    void setSpinner()
    {
        categories = dbHandler.getCategories(e.getType());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,categories);

        spinCategory.setAdapter(adapter);

        spinCategory.setSelection(categories.indexOf(e.getCategory()));
    }
}
