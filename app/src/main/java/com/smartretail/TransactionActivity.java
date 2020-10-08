package com.smartretail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smartretail.transactions.RateFixingActivity;
import com.smartretail.transactions.SalesEntryActivity;

public class TransactionActivity extends AppCompatActivity {
Button purchase,sales,rateFixing,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        purchase = findViewById(R.id.btn_purchase);
        sales = findViewById(R.id.btn_sales);
        rateFixing = findViewById(R.id.btn_ratefixing);
        exit = findViewById(R.id.btn_exit);

        setTitle("Transaction");
    }

    public void  onPurchaseClick(View v){

    }

    public void onSalesClick(View v){
        Intent salesEntry = new Intent(TransactionActivity.this, SalesEntryActivity.class);
        startActivity(salesEntry);
    }

    public void onRateFixingClick(View v){
        Intent rateFixing = new Intent(TransactionActivity.this, RateFixingActivity.class);
        startActivity(rateFixing);

    }

    public void onExitClick(View v){
        finish();
    }
}
