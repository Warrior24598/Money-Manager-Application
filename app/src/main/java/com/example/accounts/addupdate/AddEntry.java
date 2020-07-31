package com.example.accounts.addupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.accounts.Constants;
import com.example.accounts.DatabaseHandler;
import com.example.accounts.R;
import com.example.accounts.data.Entry;
import com.example.accounts.dialogs.DatePickerFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddEntry extends AppCompatActivity
{

    TextView txtCategory, txtType;
    TextInputEditText inputDate, inputSource, inputAmount;
    MaterialButton btnAddEntry;

    String category, type, date, source, amount;

    DatabaseHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        //-----------------------------------------//
        //-------INITIALISATION OF WIDGETS---------//
        //-----------------------------------------//

        txtCategory = findViewById(R.id.txtCategory);
        txtType = findViewById(R.id.txtType);
        inputDate = findViewById(R.id.inputDate);
        inputSource = findViewById(R.id.inputSource);
        inputAmount = findViewById(R.id.inputAmount);
        btnAddEntry = findViewById(R.id.btnAddEntry);

        dbHandler = DatabaseHandler.getHandler(this);

        category = getIntent().getStringExtra(Constants.CATEGORY);
        type = getIntent().getStringExtra(Constants.TYPE);

        //set Widget values
        txtType.setText(type.toUpperCase());
        txtCategory.setText(category);

        Date date=new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
        inputDate.setText(format.format(date));



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


                Entry entry = new Entry(source,Float.parseFloat(amount),date,category,type);

                dbHandler.addEntry(entry);


                Toast.makeText(AddEntry.this, "Data Entered Successfully", Toast.LENGTH_SHORT).show();

                inputSource.setText("");
                inputAmount.setText("");

            }
        });
    }
}
