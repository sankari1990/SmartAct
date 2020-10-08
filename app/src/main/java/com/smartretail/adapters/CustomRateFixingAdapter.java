package com.smartretail.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.smartretail.R;
import com.smartretail.database.ItemMasterTable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomRateFixingAdapter extends ArrayAdapter<ItemMasterTable>{

    private List<ItemMasterTable> dataSet;
    Context mContext;
    HashMap<Integer,Integer> updatedValues = new HashMap<>();

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        EditText amount;

    }

    public CustomRateFixingAdapter(List<ItemMasterTable> data, Context context) {
        super(context, R.layout.item_fixing_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    public int getItemCode(int position){
        return getItem(position).getIntCode();
    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ItemMasterTable dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_fixing_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.amount = (EditText) convertView.findViewById(R.id.amount);
            //viewHolder.txtCode = (TextView) convertView.findViewById(R.id.itemCode);


            result=convertView;

            convertView.setTag(viewHolder);

            viewHolder.txtName.setText(dataModel.getStrDesc());
            //viewHolder.txtCode.setText(""+dataModel.getIntCode());
            if(dataModel.getAmount()>0) {
                viewHolder.amount.setText("" + String.format("%.2f", dataModel.getAmount()));
            }



//            viewHolder.amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        EditText val = (EditText) v;
//                        if (val.getText().toString().length() > 0) {
//                            viewHolder.amount.setText("" + String.format("%.2f", Double.parseDouble(val.getText().toString())));
//                        }
//                    }
//                }
//            });
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