package com.smartretail.master;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.smartretail.R;
import com.smartretail.database.DatabaseClient;
import com.smartretail.database.ItemMasterDAO;
import com.smartretail.database.ItemMasterTable;
import com.smartretail.database.UnitMasterDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemMasterActivity extends AppCompatActivity {

    EditText itemCode,itemName;
    Spinner groupSpin,unitSpin,stockSpin;

    Button save,delete;

    ListView listView;

    int initialItemCodeValue;
    String initialItemNameValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_master);

        itemCode = findViewById(R.id.txt_itemcode);
        itemName = findViewById(R.id.txt_itemname);
        groupSpin = findViewById(R.id.spin_group);
        unitSpin = findViewById(R.id.spin_unit);
        stockSpin = findViewById(R.id.spin_stock);
        save = findViewById(R.id.btn_save);
        delete = findViewById(R.id.btn_delete);

        delete.setVisibility(View.INVISIBLE);

        listView = findViewById(R.id.list);


        new populateList().execute();
        new getItems().execute();

        groupSpin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        unitSpin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        stockSpin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomItemAdapter customItemAdapter = (CustomItemAdapter) listView.getAdapter();
                ItemMasterTable itemMasterTable = customItemAdapter.getItem(position);

                itemCode.setText(""+itemMasterTable.getIntCode());
                itemName.setText(""+itemMasterTable.getStrDesc());
                setGroupSpinSelection(itemMasterTable.getIntGrpCode());
                setUnitSpinSelection(itemMasterTable.getIntUnitCode());
                setStockSpinSelection(itemMasterTable.isStock());
                save.setText("Update");
                delete.setVisibility(View.VISIBLE);

                initialItemCodeValue = itemMasterTable.getIntCode();
                initialItemNameValue = itemMasterTable.getStrDesc();
            }
        });

        setTitle("Item Master");

        itemCode.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(itemCode, InputMethodManager.SHOW_IMPLICIT);
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

    private void setGroupSpinSelection(final int groupId){
            AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                final String groupName = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().groupMasterDAO().getGroupName(groupId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        groupSpin.setSelection(getIndex(groupSpin, groupName));
                    }});
            }
        });
    }

    private void setUnitSpinSelection(final int unitId){
            AsyncTask.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    final String unitName = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().unitMasterDAO().getUnitName(unitId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            unitSpin.setSelection(getIndex(unitSpin, unitName));
                        }});
                }
            });
        }

    private void setStockSpinSelection(boolean isStock){
        if(isStock)
            stockSpin.setSelection(1);
        else
            stockSpin.setSelection(0);
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void onSave(View v){

        if(itemCode.getText().toString().length()<=0){
            Toast.makeText(getApplicationContext(),"Item Code should not be empty",Toast.LENGTH_LONG).show();
        }else if(itemName.getText().toString().length()<=0){
            Toast.makeText(getApplicationContext(),"Item Name should not be empty",Toast.LENGTH_LONG).show();
        }else if(groupSpin.getSelectedItem().toString().length()<=0 || groupSpin.getSelectedItem().toString().equalsIgnoreCase("-Select-")){
            Toast.makeText(getApplicationContext(),"Select Group Name",Toast.LENGTH_LONG).show();
        }else if(unitSpin.getSelectedItem().toString().length()<=0|| unitSpin.getSelectedItem().toString().equalsIgnoreCase("-Select-")){
            Toast.makeText(getApplicationContext(),"Select Unit Name",Toast.LENGTH_LONG).show();
        }else{
            if(save.getText().toString().equalsIgnoreCase("save"))
                new save().execute();
            else
                new update().execute();
        }
    }

    public void onDelete(View v){
        new delete().execute();
    }

    private class save extends AsyncTask<Void, Void, Void> {

        ItemMasterDAO itemMasterDAO;
        boolean itemCodeExist = false;
        boolean itemNameExist = false;
        @Override
        protected Void doInBackground(Void... params) {
            itemMasterDAO = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().itemMasterDAO();

            if(itemMasterDAO.getItemCodeByValue(Integer.parseInt(itemCode.getText().toString())).size()>0){
                itemCodeExist = true;
                return null;
            }else if(itemMasterDAO.getItemNameByValue(itemName.getText().toString()).size()>0){
                itemNameExist = true;
                return null;
            }else {


                int groupId = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().groupMasterDAO().getId(groupSpin.getSelectedItem().toString());
                int unitId = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().unitMasterDAO().getId(unitSpin.getSelectedItem().toString());

                ItemMasterTable itemMasterTable = new ItemMasterTable();
                itemMasterTable.setIntGrpCode(groupId);
                itemMasterTable.setIntUnitCode(unitId);
                if(stockSpin.getSelectedItemPosition() == 0)
                    itemMasterTable.setStock(false);
                else
                    itemMasterTable.setStock(true);
                itemMasterTable.setIntCode(Integer.parseInt(itemCode.getText().toString()));
                itemMasterTable.setStrDesc(itemName.getText().toString());

                itemMasterDAO.insert(itemMasterTable);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(itemCodeExist){
                final AlertDialog alertDialog = new AlertDialog.Builder(ItemMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Item Code Already Exist");
                alertDialog.setIcon(R.drawable.ic_error);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        itemCode.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(itemCode, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                alertDialog.show();

            }
            else if(itemNameExist){
                final AlertDialog alertDialog = new AlertDialog.Builder(ItemMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Item Name Already Exist");
                alertDialog.setIcon(R.drawable.ic_error);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        itemName.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(itemName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                alertDialog.show();
            }else {
                itemCode.setText("");
                itemName.setText("");
                groupSpin.setSelection(0);
                unitSpin.setSelection(0);
                stockSpin.setSelection(0);
                itemCode.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(itemCode, InputMethodManager.SHOW_IMPLICIT);

                new getItems().execute();
            }
        }
    }


    private class update extends AsyncTask<Void, Void, Void> {

        ItemMasterDAO itemMasterDAO;
        boolean itemCodeExist = false;
        boolean itemNameExist = false;


        @Override
        protected Void doInBackground(Void... params) {
            itemMasterDAO = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().itemMasterDAO();

            int intCode = Integer.parseInt(itemCode.getText().toString());
            String item_name = itemName.getText().toString();

            if(initialItemCodeValue != intCode && itemMasterDAO.getItemCodeByValue(intCode).size()>0){
                itemCodeExist = true;
                return null;
            }else if(!initialItemNameValue.equals(item_name) && itemMasterDAO.getItemNameByValue(item_name).size()>0){
                itemNameExist = true;
                return null;
            }else {
                int groupId = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().groupMasterDAO().getId(groupSpin.getSelectedItem().toString());
                int unitId = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().unitMasterDAO().getId(unitSpin.getSelectedItem().toString());

                ItemMasterTable itemMasterTable = new ItemMasterTable();
                itemMasterTable.setIntGrpCode(groupId);
                itemMasterTable.setIntUnitCode(unitId);
                if(stockSpin.getSelectedItemPosition() == 0)
                    itemMasterTable.setStock(false);
                else
                    itemMasterTable.setStock(true);
                itemMasterTable.setIntCode(intCode);
                itemMasterTable.setStrDesc(item_name);

                itemMasterDAO.update(itemMasterTable);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(itemCodeExist){
                final AlertDialog alertDialog = new AlertDialog.Builder(ItemMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Item Code Already Exist");
                alertDialog.setIcon(R.drawable.ic_error);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        itemCode.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(itemCode, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                alertDialog.show();

            }
            else if(itemNameExist){
                final AlertDialog alertDialog = new AlertDialog.Builder(ItemMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Item Name Already Exist");
                alertDialog.setIcon(R.drawable.ic_error);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        itemName.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(itemName, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                alertDialog.show();
            }else {
                itemCode.setText("");
                itemName.setText("");
                groupSpin.setSelection(0);
                unitSpin.setSelection(0);
                stockSpin.setSelection(0);
                itemCode.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(itemCode, InputMethodManager.SHOW_IMPLICIT);

                save.setText("Save");
                delete.setVisibility(View.INVISIBLE);
                new getItems().execute();
            }
        }
    }

    private class delete extends AsyncTask<Void, Void, Void> {
        ItemMasterDAO itemMasterDAO;

        @Override
        protected Void doInBackground(Void... params) {
            itemMasterDAO = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().itemMasterDAO();
            int groupId = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().groupMasterDAO().getId(groupSpin.getSelectedItem().toString());
            int unitId = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().unitMasterDAO().getId(unitSpin.getSelectedItem().toString());

            ItemMasterTable itemMasterTable = new ItemMasterTable();
            itemMasterTable.setIntGrpCode(groupId);
            itemMasterTable.setIntUnitCode(unitId);
            itemMasterTable.setIntCode(Integer.parseInt(itemCode.getText().toString()));
            itemMasterTable.setStrDesc(itemName.getText().toString());

            itemMasterDAO.delete(itemMasterTable);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            itemCode.setText("");
            itemName.setText("");
            groupSpin.setSelection(0);
            unitSpin.setSelection(0);
            stockSpin.setSelection(0);
            itemCode.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(itemCode, InputMethodManager.SHOW_IMPLICIT);

            save.setText("Save");
            delete.setVisibility(View.INVISIBLE);
            new getItems().execute();
        }
    }


    private class populateList extends AsyncTask<Void, Void, Void> {
        List<String> groupList,unitList;

        @Override
        protected Void doInBackground(Void... params) {
            groupList = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().groupMasterDAO().getAllGroupNames();
            unitList = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().unitMasterDAO().getAllUnitNames();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Collections.sort(groupList);
            Collections.sort(unitList);
            groupList.add(0,"-Select-");
            unitList.add(0,"-Select-");
            ArrayAdapter adapter =  new ArrayAdapter(ItemMasterActivity.this, R.layout.spinner_item, groupList);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            groupSpin.setAdapter(adapter);

            ArrayAdapter unitAdapter =  new ArrayAdapter(ItemMasterActivity.this, R.layout.spinner_item, unitList);
            unitAdapter.setDropDownViewResource(R.layout.spinner_item);
            unitSpin.setAdapter(unitAdapter);

            ArrayAdapter stockAdadpter =  new ArrayAdapter(ItemMasterActivity.this, R.layout.spinner_item, new String[]{"No","Yes"});
            unitAdapter.setDropDownViewResource(R.layout.spinner_item);
            stockSpin.setAdapter(stockAdadpter);
        }
    }

    private class getItems extends AsyncTask<Void, Void, Void> {

        List<ItemMasterTable> items;
        @Override
        protected Void doInBackground(Void... params) {

            items = DatabaseClient.getInstance(ItemMasterActivity.this).getAppDatabase().itemMasterDAO().getAllItems();



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CustomItemAdapter customAdapter = new CustomItemAdapter(getApplicationContext(),items);
            listView.setAdapter(customAdapter);


        }
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view =

                getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
