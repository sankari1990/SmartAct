package com.smartretail.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smartretail.R;
import com.smartretail.adapters.CustomRateFixingAdapter;
import com.smartretail.database.DatabaseClient;
import com.smartretail.database.ItemMasterDAO;
import com.smartretail.database.ItemMasterTable;
import com.smartretail.master.CustomItemAdapter;
import com.smartretail.master.ItemMasterActivity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class RateFixingActivity extends AppCompatActivity {

    private ListView itemList;
    private CustomRateFixingAdapter customRateFixingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_fixing);
        itemList = (ListView) findViewById(R.id.itemList);
        new populateList().execute();

    }

    private class populateList extends AsyncTask<Void, Void, Void> {
        List<ItemMasterTable> itemMasterTables;

        @Override
        protected Void doInBackground(Void... params) {
            itemMasterTables = DatabaseClient.getInstance(RateFixingActivity.this).getAppDatabase().itemMasterDAO().getAllItemsBYName();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            customRateFixingAdapter = new CustomRateFixingAdapter(itemMasterTables,getApplicationContext());
            itemList.setAdapter(customRateFixingAdapter);
        }
    }

    private class updateDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //HashMap<Integer,Integer> map = customRateFixingAdapter.getUpdatedValues();
            ItemMasterDAO itemMasterDAO = DatabaseClient.getInstance(RateFixingActivity.this).getAppDatabase().itemMasterDAO();

            HashMap<Integer,Double> map = new HashMap<>();
            for(int i=0;i<customRateFixingAdapter.getCount();i++){
                View view = itemList.getChildAt(i);
                //TextView itemCode = (TextView) view.findViewById(R.id.itemCode);
                EditText amount = (EditText) view.findViewById(R.id.amount);
                int item = customRateFixingAdapter.getItemCode(i);
                if(amount.getText().toString().length()>0) {
                    Double amt = Double.parseDouble(amount.getText().toString());
                    map.put(item, amt);
                }
            }
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                itemMasterDAO.updateAmount((double)pair.getValue(),(int)pair.getKey());
                it.remove(); // avoids a ConcurrentModificationException
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            finish();
        }
    }

    public void onSave(View v){
            new updateDb().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.menu_item:   //this item has your app icon
                finish();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }
}