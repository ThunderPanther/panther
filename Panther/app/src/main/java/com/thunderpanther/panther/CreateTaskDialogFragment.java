package com.thunderpanther.panther;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Steven on 11/28/2014.
 */
public class CreateTaskDialogFragment extends DialogFragment {

    public interface CreateTaskListener {
        public void onCreateTaskConfirm(String name, Task parent, int weight, double timeEst);
        public void onDeleteTask(int id);
    }

    private String mTitle;
    private CreateTaskListener listener;

    private EditText mTaskName;
    private EditText mTaskWeight;
    private EditText mTaskTimeEstimate;
    private TextView mParentTaskDisplay;

    private Task mExistingTask = null;
    private Task mParentTask = null;

    public CreateTaskDialogFragment() {}
    public void setTitle(String title) {
        mTitle = title;
    }
    public void setExistingTask(Task task) { mExistingTask = task; }
    public void setParentTask(Task parent) { mParentTask = parent; }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (CreateTaskListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement CreateTaskListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_task_dialog, null);

        mTaskName = (EditText)dialogView.findViewById(R.id.task_name);
        mTaskWeight = (EditText)dialogView.findViewById(R.id.task_weight);
        mTaskTimeEstimate = (EditText)dialogView.findViewById(R.id.task_time_estimate);
        mParentTaskDisplay = (TextView)dialogView.findViewById(R.id.parent_task_display);

        final boolean editingTask = mExistingTask != null;

        if (editingTask) {
            mTaskName.setText(mExistingTask.getName());
            mTaskWeight.setText(mExistingTask.getWeight() + "");
            mTaskTimeEstimate.setText(mExistingTask.getTimeEstimate() + "");
        }

        if (mParentTask != null) {
            mParentTaskDisplay.setText(getString(R.string.dialogParent) + " " + mParentTask.getName());
        } else {
            mParentTaskDisplay.setVisibility(View.GONE);
        }

        builder.setView(dialogView)
                .setTitle(editingTask ? R.string.edit_task_title : R.string.create_task_title)
                .setPositiveButton(editingTask ? R.string.dialogConfirm : R.string.dialogCreateTask,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editingTask) {

                            try {
                                int weight = Integer.parseInt(mTaskWeight.getText().toString());
                                mExistingTask.setWeight(weight);
                            } catch (NumberFormatException e) {
                            }

                            try {
                                double timeEstimate = Double.parseDouble(mTaskTimeEstimate.getText().toString());
                                mExistingTask.setTimeEstimate(timeEstimate);
                            } catch (NumberFormatException e) {
                            }

                        } else if (listener != null) {

                            int weight, timeEst;
                            try {
                                weight = Integer.parseInt(mTaskWeight.getText().toString());
                            } catch (NumberFormatException e) {
                                weight = 0;
                            }

                            try {
                                timeEst = Integer.parseInt(mTaskTimeEstimate.getText().toString());
                            } catch (NumberFormatException e) {
                                timeEst = 0;
                            }
                            listener.onCreateTaskConfirm(mTaskName.getText().toString(), mParentTask, weight, timeEst);

                        }
                    }
                })
                .setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateTaskDialogFragment.this.getDialog().cancel();
                    }
                });

        if (editingTask) {
            builder.setNeutralButton(R.string.dialogDelete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // We need to go deeper!

                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(getActivity());

                    View deleteDialogView = inflater.inflate(R.layout.delete_task_confirmation_dialog, null);
                    TextView message = (TextView)deleteDialogView.findViewById(R.id.delete_task_confirmation_dialog_message);
                    message.setText(getString(R.string.confirmDeleteMessage)
                            + " \"" + mExistingTask.getName() + "\"? "
                            + getString(R.string.allSubtasksDeleted));

                    confirmBuilder.setTitle(R.string.confirmDelete)
                            .setView(deleteDialogView)
                            .setPositiveButton(R.string.dialogConfirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listener.onDeleteTask(mExistingTask.getID());
                                }
                            })
                            .setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO: is this the desired behavior?
                                    // CreateTaskDialogFragment.this.getDialog().cancel();
                                }
                            }).create().show();
                }
            });
        }
        return builder.create();
    }
}
