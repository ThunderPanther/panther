package com.thunderpanther.panther;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Conner on 12/3/14.
 */
public class TaskListAdapter extends ArrayAdapter<TaskPair> {

    private List<TaskPair> items;
    private int layoutResourceId;
    private Context context;

    public TaskListAdapter(Context context, int layoutResourceId, List<TaskPair> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new TaskHolder();
        holder.task = items.get(position);
        holder.removePaymentButton = (ImageButton)row.findViewById(R.id.atomPay_removePay);
        holder.removePaymentButton.setTag(holder.task);

        holder.name = (TextView)row.findViewById(R.id.atomPay_name);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onclick", "clicked task");
            }
        });
        //setNameTextChangeListener(holder);
        //holder.value = (TextView)row.findViewById(R.id.atomPay_value);
        //setValueTextListeners(holder);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }



    private void setupItem(TaskHolder holder) {
        holder.name.setText(holder.task.name);
        //holder.value.setText(String.valueOf(holder.atomPayment.getValue()));
    }

    public static class TaskHolder {
        TaskPair task;
        TextView name;
        //TextView value;
        ImageButton removePaymentButton;
    }

    private void setNameTextChangeListener(final TaskHolder holder) {
        holder.name.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.task.name = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}
