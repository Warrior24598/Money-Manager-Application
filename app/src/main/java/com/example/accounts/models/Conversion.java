package com.example.accounts.models;

public class Conversion
{
    final static String[] months = {
                            "January",
                            "February",
                            "March",
                            "April",
                            "May",
                            "June",
                            "July",
                            "August",
                            "September",
                            "October",
                            "November",
                            "December"
                    };


    public static String getMonthName(String monthIndex)
    {
        int month = Integer.parseInt(monthIndex)-1;

        return months[month];

    }
}
