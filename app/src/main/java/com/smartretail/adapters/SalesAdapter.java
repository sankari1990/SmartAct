package com.smartretail.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.smartretail.R;
import com.smartretail.database.DatabaseClient;
import com.smartretail.database.ItemMasterTable;
import com.smartretail.database.SalesTable;
import com.smartretail.transactions.SalesEntryActivity;

import java.util.HashMap;
import java.util.List;

public class SalesAdapter extends ArrayAdapter<SalesTable>{

    private List<SalesTable> dataSet;
    Context mContext;
    HashMap<Integer,Integer> updatedValues = new HashMap<>();
    String value = "";

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView amount,qty,kgs;

    }

    public SalesAdapter(List<SalesTable> data, Context context) {
        super(context, R.layout.sales_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    public int getItemCode(int position){
        return getItem(position).getIntItCode();
    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final SalesTable dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.sales_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amt);
            viewHolder.qty = (TextView) convertView.findViewById(R.id.qty);
            viewHolder.kgs = (TextView) convertView.findViewById(R.id.kgs);

            //viewHolder.txtCode = (TextView) convertView.findViewById(R.id.itemCode);


            result=convertView;

            convertView.setTag(viewHolder);

            @SuppressLint("HandlerLeak")
            final Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    Bundle bundle = msg.getData();
                    String value = bundle.getString("Value");
                    viewHolder.txtName.setText(value);



                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    value = DatabaseClient.getInstance(mContext).getAppDatabase().itemMasterDAO().itemName(dataModel.getIntItCode());
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("Value", value);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }).start();
            viewHolder.amount.setText(""+dataModel.getSngAmt());
            viewHolder.kgs.setText(""+dataModel.getSngKgs());
            viewHolder.qty.setText(""+dataModel.getSngQty());

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
            convertView.setTag(viewHolder);

        }


        // Return the completed view to render on screen
        return convertView;
    }

    //public HashMap<Integer, Integer> getUpdatedValues() {
     //   return updatedValues;
    //}
}