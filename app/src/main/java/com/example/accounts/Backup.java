package com.example.accounts;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.accounts.models.Entry;
import com.example.accounts.database.DatabaseHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Backup
{
    final static String ENTRIES = "entries";
    final static String CATEGORIES = "categories";
    final static String LASTBACKUP = "lastbak";

    final static String ID = "id";
    final static String SOURCE = "source";
    final static String AMOUNT = "amount";
    final static String DATE = "date";
    final static String CATEGORY = "category";
    final static String TYPE = "type";
    final static String NAME = "name";

    public static void Export(final Context context)
    {
        try
        {
            DatabaseHandler db = DatabaseHandler.getHandler(context);

//                    File file = new File(context.getExternalFilesDir(null),"accounts.exp");
            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/accounts.exp");

            JSONObject mainObj = new JSONObject();

            //current date
            Date date=new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");

            mainObj.put(LASTBACKUP,format.format(date));
            mainObj.put(ENTRIES,getEntries(db));
            mainObj.put(CATEGORIES,getCategories(db));

            fos.write(mainObj.toString().getBytes());
            fos.flush();
            fos.close();

            Toast.makeText(context, "Data Exported Successfully", Toast.LENGTH_SHORT).show();

        }
        catch (FileNotFoundException  e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void Import(final Context context, final String fileName)
    {

        try
        {
            DatabaseHandler db = DatabaseHandler.getHandler(context);

            FileReader fr = new FileReader(fileName);

            JSONParser parser = new JSONParser();
            JSONObject mainObj = (JSONObject) parser.parse(fr);

            //read values from the file
            String lb = (String)mainObj.get(LASTBACKUP);

            List<JSONObject> listEntries = (List<JSONObject>) mainObj.get(ENTRIES);

            List<JSONObject> listCategories = (List<JSONObject>) mainObj.get(CATEGORIES);

            //add all values to database

            //add entries
            for (JSONObject obj: listEntries)
            {

                //get values from object
                String source = (String) obj.get(SOURCE);
                String date = (String) obj.get(DATE);
                String category = (String) obj.get(CATEGORY);
                String type = (String) obj.get(TYPE);

                double  amount = (double) obj.get(AMOUNT);

                //create entry object
                Entry e = new Entry(source,Float.parseFloat(String.valueOf(amount)),date,category,type);

                //insert into database

                db.addEntry(e);


            }

            //add categories
            for(JSONObject obj:listCategories)
            {
                String name = (String) obj.get(NAME);
                String type = (String) obj.get(TYPE);

                //insert into database

                db.addCategory(name,type);
            }

            fr.close();


            Toast.makeText(context, "Data Imported Successfully", Toast.LENGTH_SHORT).show();


        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

    }

    //METHOD TO GET JSONOBJECT LIST OF ALL ENTRIES

    static List<JSONObject> getEntries(DatabaseHandler db)
    {
        List<JSONObject> entries = new ArrayList<>();

        List<Entry> listE = db.getAllEntries();

        for (Entry e: listE)
        {
            entries.add(createEnrty(e));
        }

        return entries;
    }

    static List<JSONObject> getCategories(DatabaseHandler db)
    {
        List<JSONObject> cats = new ArrayList<>();

        List<String> categories;

        categories = db.getCategories(Constants.TYPE_EXPENSE);

        for(String c : categories)
        {
            if(c.equals("Regular"))
                continue;
            cats.add(createCategory(c,Constants.TYPE_EXPENSE));
        }


        categories = db.getCategories(Constants.TYPE_INCOME);

        for(String c : categories)
        {
            if(c.equals("Regular"))
                continue;
            cats.add(createCategory(c,Constants.TYPE_INCOME));
        }

        return cats;
    }

    static JSONObject createEnrty(Entry e)
    {

        JSONObject entry = new JSONObject();
            entry.put(ID, e.getId());
            entry.put(SOURCE,e.getSource());
            entry.put(AMOUNT,e.getAmount());
            entry.put(DATE,e.getDate());
            entry.put(CATEGORY,e.getCategory());
            entry.put(TYPE,e.getType());
        return entry;

    }

    static JSONObject createCategory(String name, String type)
    {
        JSONObject category = new JSONObject();
            category.put(NAME,name);
            category.put(TYPE,type);
        return category;

    }
}
