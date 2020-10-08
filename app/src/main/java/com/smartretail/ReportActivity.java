package com.smartretail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReportActivity extends AppCompatActivity {

    Button noWise,itemWise,dateWise,payment,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        noWise = findViewById(R.id.btn_nowise);
        itemWise = findViewById(R.id.btn_itemwise);
        dateWise = findViewById(R.id.btn_datawise);
        payment = findViewById(R.id.btn_pay);
        exit = findViewById(R.id.btn_exit);

        setTitle("Report");
    }

    public void onNoWiseReportClick(View v){

    }

    public void onItemWiseReportClick(View v){

    }

    public void onDateWiseReportClick(View v){

    }

    public void onPaymentReportClick(View v){

    }

    public void onExitClick(View v){
        finish();
    }
}
