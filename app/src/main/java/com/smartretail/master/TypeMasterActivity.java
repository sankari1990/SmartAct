package com.smartretail.master;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.smartretail.database.PaymentMasterDAO;
import com.smartretail.database.PaymentMasterTable;
import com.smartretail.database.TypeMasterDAO;
import com.smartretail.database.TypeMasterTable;

import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TypeMasterActivity extends AppCompatActivity {

    EditText unitName;
    Button save,delete;
    ListView typeList;
    TextView txtLabel;
    private TypeMasterDAO typeMasterDAO;
    ArrayAdapter<String> arrayAdapter;
    String selectedName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_master);

        unitName = findViewById(R.id.txt_groupname);
        save = findViewById(R.id.btn_save);
        delete = findViewById(R.id.btn_delete);
        typeList = findViewById(R.id.groupList);
        txtLabel = findViewById(R.id.txt_label);
        txtLabel.setText("SaleType\nMaster");

        unitName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(unitName, InputMethodManager.SHOW_IMPLICIT);
        typeMasterDAO = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().typeMasterDAO();
        new populateList(typeMasterDAO).execute();
        delete.setVisibility(View.INVISIBLE);

        typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete.setVisibility(View.VISIBLE);
                save.setText("Update");
                selectedName = parent.getAdapter().getItem(position).toString();
                unitName.setText(selectedName);
            }
        });

        setTitle("SaleType Master");

        View headerView = ((LayoutInflater) TypeMasterActivity.this.getSystemService(TypeMasterActivity.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
        TextView txt = headerView.findViewById(R.id.textView);
        txt.setText("SaleType Names");
        typeList.addHeaderView(headerView);
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
        private TypeMasterDAO mAsyncTaskDao;
        populateList(TypeMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {
            List<String> list = mAsyncTaskDao.getAllPayNames();
            Collections.sort(list);
            arrayAdapter = new ArrayAdapter(TypeMasterActivity.this, R.layout.item_row, R.id.name, list);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            typeList.setAdapter(arrayAdapter);
        }
    }


    public void onSave(View v){

        String value = unitName.getText().toString();
        if(value.length()>0){
            if(save.getText().toString().equalsIgnoreCase("save")) {
                TypeMasterTable paymentMasterTable = new TypeMasterTable();
                paymentMasterTable.setStrDesc(value);
                new saveThread(typeMasterDAO).execute(paymentMasterTable);
            }else{
                new update(typeMasterDAO).execute(value);
            }
        }else{
            unitName.setError("Please Enter Value");
        }
    }

    public void onDelete(View v){
        String value = unitName.getText().toString();
        if(value.length()>=0){
            new deleteTask(typeMasterDAO).execute(value);
        }else{
            unitName.setError("Please Enter Value");
        }
    }



    private class saveThread extends AsyncTask<TypeMasterTable, Void, Void> {
        private TypeMasterDAO mAsyncTaskDao;
        private boolean insert = false;
        saveThread(TypeMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TypeMasterTable... params) {
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
                final AlertDialog alertDialog = new AlertDialog.Builder(TypeMasterActivity.this).create();
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
                new populateList(typeMasterDAO).execute();
                unitName.setText("");
            }
        }
    }

    private class update extends AsyncTask<String, Void, Void> {
        private TypeMasterDAO mAsyncTaskDao;
        private boolean insert = false;
        update(TypeMasterDAO dao) {
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

                new populateList(typeMasterDAO).execute();
                unitName.setText("");
                unitName.setText("");
                delete.setVisibility(View.INVISIBLE);
                save.setText("Save");


        }
    }


    private class deleteTask extends AsyncTask<String, Void, Void> {
        private TypeMasterDAO mAsyncTaskDao;
        deleteTask(TypeMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {

                mAsyncTaskDao.deleteByValue(params[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

                new populateList(typeMasterDAO).execute();
                unitName.setText("");
                unitName.setText("");
                delete.setVisibility(View.INVISIBLE);
                save.setText("Save");

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
