package com.smartretail.master;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.Toast;

import com.smartretail.R;
import com.smartretail.database.DatabaseClient;
import com.smartretail.database.GroupMasterDAO;
import com.smartretail.database.GroupMasterTable;
import com.smartretail.database.ItemMasterDAO;

import java.util.Collections;
import java.util.List;

public class GroupMasterActivity extends AppCompatActivity {

    EditText groupName;
    Button save,delete;
    ListView groupList;
    private GroupMasterDAO groupMasterDAO;
    ArrayAdapter<String> arrayAdapter;
    String selectedName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_master);

        groupName = findViewById(R.id.txt_groupname);
        save = findViewById(R.id.btn_save);
        delete = findViewById(R.id.btn_delete);
        groupList = findViewById(R.id.groupList);

        groupName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(groupName, InputMethodManager.SHOW_IMPLICIT);
        groupMasterDAO = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().groupMasterDAO();
        new populateList(groupMasterDAO).execute();
        delete.setVisibility(View.INVISIBLE);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete.setVisibility(View.VISIBLE);
                save.setText("Update");
                selectedName = parent.getAdapter().getItem(position).toString();
                groupName.setText(selectedName);
            }
        });

        setTitle("Group Master");

        View headerView = ((LayoutInflater)GroupMasterActivity.this.getSystemService(GroupMasterActivity.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.header, null, false);
        groupList.addHeaderView(headerView);
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
        private GroupMasterDAO mAsyncTaskDao;
        populateList(GroupMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(String... params) {

            List<String> list = groupMasterDAO.getAllGroupNames();
            Collections.sort(list);
            arrayAdapter = new ArrayAdapter(GroupMasterActivity.this, R.layout.item_row, R.id.name, list);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            groupList.setAdapter(arrayAdapter);
        }
    }


    public void onSave(View v){

        String value = groupName.getText().toString();
        if(value.length()>0){
            if(save.getText().toString().equalsIgnoreCase("save")) {
                GroupMasterTable groupMasterTable = new GroupMasterTable();
                groupMasterTable.setStrDesc(value);
                new saveThread(groupMasterDAO).execute(groupMasterTable);
            }else{
                new update(groupMasterDAO).execute(value);
            }
        }else{
            groupName.setError("Please Enter Value");
        }
    }

    public void onDelete(View v){
        String value = groupName.getText().toString();
        if(value.length()>=0){
            new deleteTask(groupMasterDAO).execute(value);
        }else{
            groupName.setError("Please Enter Value");
        }
    }



    private class saveThread extends AsyncTask<GroupMasterTable, Void, Void> {
        private GroupMasterDAO mAsyncTaskDao;
        private boolean insert = false;
        saveThread(GroupMasterDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GroupMasterTable... params) {
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
                final AlertDialog alertDialog = new AlertDialog.Builder(GroupMasterActivity.this).create();
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
                new populateList(groupMasterDAO).execute();
                groupName.setText("");
            }
        }
    }

    private class update extends AsyncTask<String, Void, Void> {
        private GroupMasterDAO mAsyncTaskDao;
        private boolean insert = false;
        update(GroupMasterDAO dao) {
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

                new populateList(groupMasterDAO).execute();
                groupName.setText("");
                groupName.setText("");
                delete.setVisibility(View.INVISIBLE);
                save.setText("Save");


        }
    }


    private class deleteTask extends AsyncTask<String, Void, Void> {
        private GroupMasterDAO mAsyncTaskDao;
        deleteTask(GroupMasterDAO dao) {
            mAsyncTaskDao = dao;
        }
        boolean isDeleted = false;
        private ItemMasterDAO itemMasterDAO;

        @Override
        protected Void doInBackground(final String... params) {
                itemMasterDAO = DatabaseClient.getInstance(GroupMasterActivity.this).getAppDatabase().itemMasterDAO();
                int groupCode = mAsyncTaskDao.getId(params[0]);

                if(itemMasterDAO.getGroupCodeId(groupCode).size()>0){
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
                new populateList(groupMasterDAO).execute();
                groupName.setText("");
                groupName.setText("");
                delete.setVisibility(View.INVISIBLE);
                save.setText("Save");
            }else{
                final AlertDialog alertDialog = new AlertDialog.Builder(GroupMasterActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Group Name used in Item Master");
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
