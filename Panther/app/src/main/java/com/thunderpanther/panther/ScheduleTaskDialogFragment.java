package com.thunderpanther.panther;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.*;
import java.util.Calendar;

/**
 * Created by Steven on 12/4/2014.
 */
public class ScheduleTaskDialogFragment extends DialogFragment {

    // private CreateTaskListener listener;

    private Task targetTask;
    private Date startTime;
    private WorkSessionCreateListener listener;
    private EditText duration;

    public ScheduleTaskDialogFragment() {}
    public void setTargetTask(Task target) {
        targetTask = target;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (WorkSessionCreateListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement WorkSessionCreateListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.schedule_task_dialog, null);

        duration = (EditText)dialogView.findViewById(R.id.duration_input);

        builder.setView(dialogView)
                .setTitle(getString(R.string.scheduleTaskTitle) + " " + targetTask.getName())
                .setPositiveButton(R.string.dialogConfirm,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                java.util.Calendar calendar = java.util.Calendar.getInstance();
                                calendar.setTime(startTime);
                                Date endTime;
                                try {
                                    calendar.add(java.util.Calendar.HOUR, Integer.parseInt(duration.getText().toString()));
                                    endTime = calendar.getTime();
                                } catch (NumberFormatException e) {
                                    endTime = startTime;
                                }
                                listener.onCreateWorkSession(startTime, endTime, targetTask);
                            }
                        })
                .setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScheduleTaskDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public interface WorkSessionCreateListener {
        public void onCreateWorkSession(Date startTime, Date endTime, Task target);
    }
}
