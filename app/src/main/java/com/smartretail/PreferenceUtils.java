package com.smartretail;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PreferenceUtils {
    SharedPreferences sharedpreferences;
    public static final String SMART_ACT = "SmartAct" ;


    public PreferenceUtils(Context context){
        sharedpreferences = context.getSharedPreferences(SMART_ACT, Context.MODE_PRIVATE);

    }

    public int getSalesEntryNumber(){

        int value = sharedpreferences.getInt("SalesEntryNumber",1);

        return value;
    }

    public void incrementSalesEntryNumber(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt("SalesEntryNumber",getSalesEntryNumber() + 1);
        editor.commit();
    }

    public String getDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

}
