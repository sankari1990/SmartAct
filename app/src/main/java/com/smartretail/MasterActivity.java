package com.smartretail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.smartretail.database.PaymentMasterTable;
import com.smartretail.database.UnitMasterTable;
import com.smartretail.master.GroupMasterActivity;
import com.smartretail.master.ItemMasterActivity;
import com.smartretail.master.PaymentMasterActivity;
import com.smartretail.master.TypeMasterActivity;
import com.smartretail.master.UnitMasterActivity;

public class MasterActivity extends AppCompatActivity {

    Button groupMaster,unitMaster,itemMaster,payMaster,exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        groupMaster = findViewById(R.id.btn_group);
        unitMaster = findViewById(R.id.btn_unit);
        itemMaster = findViewById(R.id.btn_item);
        payMaster = findViewById(R.id.btn_pay);
        exit = findViewById(R.id.btn_exit);

        setTitle("Masters");
    }

    public void onGroupMasterClick(View v){
        Intent groupMaster = new Intent(MasterActivity.this, GroupMasterActivity.class);
        startActivity(groupMaster);
    }

    public void onUnitMasterClick(View v){
        Intent groupMaster = new Intent(MasterActivity.this, UnitMasterActivity.class);
        startActivity(groupMaster);
    }

    public void onItemMasterClick(View v){
        Intent groupMaster = new Intent(MasterActivity.this, ItemMasterActivity.class);
        startActivity(groupMaster);
    }

    public void onPayMasterClick(View v){
        Intent groupMaster = new Intent(MasterActivity.this, PaymentMasterActivity.class);
        startActivity(groupMaster);
    }

    public void onTypeMasterClick(View v){
        Intent groupMaster = new Intent(MasterActivity.this, TypeMasterActivity.class);
        startActivity(groupMaster);
    }

    public void onExitClick(View v){
        finish();
    }
}
