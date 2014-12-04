package com.thunderpanther.panther;

import java.util.List;

import android.app.Activity;
import android.app.FragmentManager;
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
    // TODO: test!
    private FragmentManager fragmentManager;
    private List<Boolean> collapsed;

    public TaskListAdapter(Context context, int layoutResourceId, List<TaskPair> items,
                           FragmentManager fragmentManager, List<Boolean> collapsed) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
        this.fragmentManager = fragmentManager;
        this.collapsed = collapsed;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new TaskHolder();
        holder.task = items.get(position);
        holder.collapseTaskButton = (ImageButton)row.findViewById(R.id.collapse_tasks);
        holder.collapseTaskButton.setTag(holder.task);
        int iconID = collapsed.get(position)
                ? R.drawable.ic_action_folder_closed
                : R.drawable.ic_action_folder_open;

        holder.collapseTaskButton.setImageDrawable(context.getResources().getDrawable(iconID));

        holder.createSubtaskButton = (ImageButton)row.findViewById(R.id.create_subtask);
        holder.createSubtaskButton.setTag(holder.task);

        holder.name = (TextView)row.findViewById(R.id.task_name);

        TextView indent = (TextView)row.findViewById(R.id.indentation);
        StringBuilder iString = new StringBuilder();
        for (int i = 0; i < holder.task.depth; i++) {
            iString.append("   ");
        }
        indent.setText(iString.toString());

        View.OnClickListener clickTaskListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onclick", "clicked task");
                TaskPair taskPair = items.get(position);
                Task targetTask = User.getCurrentUser().getTask(taskPair.id);

                CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
                dialog.setExistingTask(targetTask);
                dialog.setParentTask(targetTask.getParent());
                dialog.show(fragmentManager, "create_task_dialog");
            }
        };

        holder.name.setOnClickListener(clickTaskListener);
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
        ImageButton collapseTaskButton;//removePaymentButton;
        ImageButton createSubtaskButton;
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
