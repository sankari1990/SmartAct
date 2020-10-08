package com.smartretail.transactions;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartretail.PreferenceUtils;
import com.smartretail.R;
import com.smartretail.adapters.CustomRateFixingAdapter;
import com.smartretail.adapters.SalesAdapter;
import com.smartretail.database.DatabaseClient;
import com.smartretail.database.SalesTable;
import com.smartretail.master.ItemMasterActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class SalesEntryActivity extends AppCompatActivity {

    EditText no,date,itemName,txt_rate,txt_amt,txt_qty,txt_kgs;
    Button add,save;
    PreferenceUtils preferenceUtils;
    Spinner sale_type_unit,itemCodeSpin,payment_spin;
    String itemUnit="";
    double itemRate=0;
    ArrayList<SalesTable> salesTables ;
    int saleTypeCode,payCode;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_entry);
        salesTables = new ArrayList<>();
        listView = findViewById(R.id.list);
        no = (EditText) findViewById(R.id.txt_num);
        date = findViewById(R.id.txt_date);
        sale_type_unit = findViewById(R.id.sale_type_unit);
        itemCodeSpin = findViewById(R.id.item_code_spin);
        payment_spin = findViewById(R.id.spin_payment);
        itemName = findViewById(R.id.itemName);
        txt_rate = findViewById(R.id.txt_rate);
        txt_amt = findViewById(R.id.txt_amt);
        txt_qty = findViewById(R.id.txt_qty);
        txt_kgs = findViewById(R.id.txt_kgs);

        preferenceUtils = new PreferenceUtils(this);

        add = findViewById(R.id.btn_add);
        save = findViewById(R.id.btn_save);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAdd();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceUtils.incrementSalesEntryNumber();
                onSave();
            }
        });

        txt_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int qty = 0;

                try{
                     qty = Integer.parseInt(text);
                }catch (Exception e){
                }
                //int qty = getvaluefromED(txt_qty);
                int kgs = getvaluefromED(txt_kgs);


                if(itemUnit.equalsIgnoreCase("kgs")){
                    double result = itemRate * kgs;
                    Toast.makeText(SalesEntryActivity.this,"kgs",Toast.LENGTH_LONG).show();
                    txt_amt.setText(""+result);
                }else{
                    Toast.makeText(SalesEntryActivity.this,"qty "+qty+"rate "+itemRate,Toast.LENGTH_LONG).show();
                    double result = itemRate * qty;
                    txt_amt.setText(""+result);
                }
            }
        });

        txt_kgs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int qty = getvaluefromED(txt_qty);
                String text = s.toString();
                int kgs = 0;

                try{
                    kgs = Integer.parseInt(text);
                }catch (Exception e){
                }
                //

                if(itemUnit.equalsIgnoreCase("kgs")){
                    double result = itemRate * kgs;
                    Toast.makeText(SalesEntryActivity.this,"kgs",Toast.LENGTH_LONG).show();
                    txt_amt.setText(""+result);
                }else{
                    Toast.makeText(SalesEntryActivity.this,"qty "+itemRate,Toast.LENGTH_LONG).show();
                    double result = itemRate * qty;
                    txt_amt.setText(""+result);
                }
            }
        });

        populateValues();
    }

    private int getvaluefromED(EditText et){
        String text = et.getText().toString();

        try{
            return Integer.parseInt(text);
        }catch (Exception e){
            return 0;
        }
    }

    private double getDoublevaluefromED(EditText et){
        String text = et.getText().toString();

        try{
            return Double.parseDouble(text);
        }catch (Exception e){
            return 0;
        }
    }

    public void populateValues(){
        no.setText(""+preferenceUtils.getSalesEntryNumber());
        no.setEnabled(false);
        date.setText(preferenceUtils.getDate());
        date.setEnabled(false);
        new populateList().execute();
    }



    private class populateList extends AsyncTask<Void, Void, Void> {
        List<String> saleTypeList;
        List<Integer> itemCodeList;
        List<String> paymentList;


        @Override
        protected Void doInBackground(Void... params) {
            saleTypeList = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase().typeMasterDAO().getAllPayNames();
            itemCodeList = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase().itemMasterDAO().getAllIntCodes();
            paymentList  = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase().paymentMasterDAO().getAllPayNames();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            ArrayAdapter stockAdadpter =  new ArrayAdapter(SalesEntryActivity.this, R.layout.spinner_item, saleTypeList);
            stockAdadpter.setDropDownViewResource(R.layout.spinner_item);
            sale_type_unit.setAdapter(stockAdadpter);

            final ArrayAdapter itemCodeAdapter =  new ArrayAdapter(SalesEntryActivity.this, R.layout.spinner_item, itemCodeList);
            itemCodeAdapter.setDropDownViewResource(R.layout.spinner_item);
            itemCodeSpin.setAdapter(itemCodeAdapter);

            final ArrayAdapter payAdapter =  new ArrayAdapter(SalesEntryActivity.this, R.layout.spinner_item, paymentList);
            payAdapter.setDropDownViewResource(R.layout.spinner_item);
            payment_spin.setAdapter(payAdapter);

            sale_type_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saleTypeCode = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase().typeMasterDAO().id(saleTypeList.get(position));
                        }}).start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            payment_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            payCode = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase().paymentMasterDAO().id(paymentList.get(position));
                        }
                    }).start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            @SuppressLint("HandlerLeak")
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    Bundle bundle = msg.getData();
                    String value = bundle.getString("Value");
                    String rate = bundle.getString("Rate");

                    itemName.setText(value);
                    txt_rate.setText(rate);
                    txt_qty.setText("");
                    txt_kgs.setText("");
                }
            };

            itemCodeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String value = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase()
                                    .itemMasterDAO().itemName((Integer) itemCodeAdapter.getItem(position));
                            itemRate = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase()
                                    .itemMasterDAO().getAmount((Integer) itemCodeAdapter.getItem(position));
                            int unitCode = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase()
                                    .itemMasterDAO().getUnitCode((Integer) itemCodeAdapter.getItem(position));
                            itemUnit = DatabaseClient.getInstance(SalesEntryActivity.this).getAppDatabase()
                                    .unitMasterDAO().getUnitName(unitCode);
                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();
                            bundle.putString("Value", value);
                            bundle.putString("Rate", ""+itemRate);
                            bundle.putString("unit",itemUnit);
                            msg.setData(bundle);
                            handler.sendMessage(msg);                        }
                    }).start();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void onAdd() {
        Toast.makeText(getApplicationContext(),"Add",Toast.LENGTH_LONG).show();
        SalesTable salesTable = new SalesTable();
        salesTable.setIntNo(getvaluefromED(no));
        salesTable.setDtmDt(date.getText().toString());
        salesTable.setIntSaleType(saleTypeCode);
        salesTable.setIntItCode(Integer.parseInt(itemCodeSpin.getSelectedItem().toString()));
        salesTable.setSngQty(getvaluefromED(txt_qty));
        salesTable.setSngKgs(getDoublevaluefromED(txt_kgs));
        salesTable.setSngAmt(getDoublevaluefromED(txt_amt));
        salesTable.setSngRate(getDoublevaluefromED(txt_rate));
        salesTable.setSngRoff(Math.round(getDoublevaluefromED(txt_amt) * 100.0) / 100.0);
        salesTable.setIntPayCode(payCode);
        salesTables.add(salesTable);
        Toast.makeText(getApplicationContext(),"SIZE"+salesTables.size(),Toast.LENGTH_LONG).show();

        SalesAdapter salesAdapter = new SalesAdapter(salesTables,getApplicationContext());
        listView.setAdapter(salesAdapter);
        salesAdapter.notifyDataSetChanged();


    }

    public void onSave() {

    }

}