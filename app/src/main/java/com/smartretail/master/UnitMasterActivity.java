package com.smartretail.master;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smartretail.R;
import com.smartretail.database.DatabaseClient;
import com.smartretail.database.ItemMasterDAO;
import com.smartretail.database.UnitMasterDAO;
import com.smartretail.database.UnitMasterTable;

import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UnitMasterActivity extends AppCompatActivity {

    EditText unitName;
    Button save,delete;
    ListView unitList;
    TextView txtLabel;
    private UnitMasterDAO unitMasterDAO;
    ArrayAdapter<String> arrayAdapter;
    String selectedName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_master);

        unitName = findViewById(R.id.txt_groupname);
        save = findViewById(R.id.btn_save);
        delete = findViewById(R.id.btn_delete);
        unitList = findViewById(R.id.groupList);
        txtLabel = findViewById(R.id.txt_label);
        txtLabel.setText("Unit Name");

        unitName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(unitName, InputMethodManager.SHOW_IMPLICIT);
        unitMasterDAO = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().unitMasterDAO();
        new populateList(unitMasterDAO).execute();
        delete.setVisibility(View.INVISIBLE);

        unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                for (int i = 0; i < unitList.getChildCount(); i++) {
//                    if(position == i ){
//                        unitList.getChildAt(i).setBackgroundColor(Color.LTGRAY);
//                    }else{
//                        unitList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }

                delete.setVisibility(View.VISIBLE);
                save.setText("Update");
                selectedName = parent.getAdapter().getItem(position).toString();
                unitName.setText(selectedName);

            }
        });

        setTitle("Unit Master");

        View headerView = ((LayoutInflater) UnitMasterActivity.this.getSystemService(UnitMasterActivity.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
        TextView txt = headerView.findViewById(R.id.textView);
        txt.setText("Unit Names");
        unitList.addHeaderView(headerView);
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



    private class populateList extends AsyncTask<String, Void, Void> {
        private UnitMasterDAO mAsyncTaskDao;
        populateList(UnitMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            List<String> list = unitMasterDAO.getAllUnitNames();
            Collections.sort(list);
            arrayAdapter = new ArrayAdapter(UnitMasterActivity.this, R.layout.item_row, R.id.name, list);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            unitList.setAdapter(arrayAdapter);
        }
    }


    public void onSave(View v){

        String value = unitName.getText().toString();
        if(value.length()>0){
            if(save.getText().toString().equalsIgnoreCase("save")) {
                UnitMasterTable unitMasterTable = new UnitMasterTable();
                unitMasterTable.setStrDesc(value);
                new saveThread(unitMasterDAO).execute(unitMasterTable);
            }else{
                new update(unitMasterDAO).execute(value);
            }
        }else{
            unitName.setError("Please Enter Value");
        }
    }

    public void onDelete(View v){
        String value = unitName.getText().toString();
        if(value.length()>=0){
            new deleteTask(unitMasterDAO).execute(value);
        }else{
            unitName.setError("Please Enter Value");
        }
    }



    private class saveThread extends AsyncTask<UnitMasterTable, Void, Void> {
        private UnitMasterDAO mAsyncTaskDao;
        private boolean insert = false;
        saveThread(UnitMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final UnitMasterTable... params) {
            if(mAsyncTaskDao.getItemByValue(params[0].getStrDesc()).size()<=0) {
                mAsyncTaskDao.insert(params[0]);
                insert = true;
            }else{
                insert = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!insert){
                final AlertDialog alertDialog = new AlertDialog.Builder(UnitMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Already Exist");
                alertDialog.setIcon(R.drawable.ic_error);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }else{
                new populateList(unitMasterDAO).execute();
                unitName.setText("");
            }
        }
    }

    private class update extends AsyncTask<String, Void, Void> {
        private UnitMasterDAO mAsyncTaskDao;
        private boolean insert = false;
        update(UnitMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            if(mAsyncTaskDao.getItemByValue(params[0]).size()<=0) {
                //mAsyncTaskDao.update(params[0]);
                mAsyncTaskDao.update(selectedName,params[0]);
                insert = true;
            }else{
                insert = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

                new populateList(unitMasterDAO).execute();
                unitName.setText("");
                unitName.setText("");
                delete.setVisibility(View.INVISIBLE);
                save.setText("Save");


        }
    }


    private class deleteTask extends AsyncTask<String, Void, Void> {
        private UnitMasterDAO mAsyncTaskDao;
        boolean isDeleted = false;
        private ItemMasterDAO itemMasterDAO;
        deleteTask(UnitMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            itemMasterDAO = DatabaseClient.getInstance(UnitMasterActivity.this).getAppDatabase().itemMasterDAO();
            int unitCode = mAsyncTaskDao.getId(params[0]);

            if(itemMasterDAO.getUnitCodeId(unitCode).size()>0){
                return null;
            }else {
                isDeleted = true;
                mAsyncTaskDao.deleteByValue(params[0]);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(isDeleted) {
                new populateList(unitMasterDAO).execute();
                unitName.setText("");
                unitName.setText("");
                delete.setVisibility(View.INVISIBLE);
                save.setText("Save");
            }else{
                final AlertDialog alertDialog = new AlertDialog.Builder(UnitMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Unit Name used in Item Master");
                alertDialog.setIcon(R.drawable.ic_error);
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }

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
