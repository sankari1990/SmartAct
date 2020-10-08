package com.smartretail.master;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartretail.R;
import com.smartretail.database.ItemMasterTable;

import java.util.List;


public class CustomItemAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<ItemMasterTable> items;

    public CustomItemAdapter(Context applicationContext, List<ItemMasterTable> items) {
        this.context = context;
        this.items = items;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ItemMasterTable getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_master_list, null);
        final TextView itemCode = view.findViewById(R.id.itemCode);
        TextView itemName = view.findViewById(R.id.itemName);

        final ItemMasterTable itemMasterTable = items.get(i);
        itemCode.setText(""+itemMasterTable.getIntCode());
        itemName.setText(itemMasterTable.getStrDesc());
//        AsyncTask.execute(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//
//                custText.setText(DatabaseClient.getInstance(context).getAppDatabase().customerDAO().findNameByCode(receipt.getStrCustCode()));
//
//            }
//        });


        return view;
    }


}