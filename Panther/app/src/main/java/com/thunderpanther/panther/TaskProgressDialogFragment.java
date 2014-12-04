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
import android.widget.ProgressBar;

import java.util.Date;

/**
 * Created by Steven on 12/4/2014.
 */
public class TaskProgressDialogFragment extends DialogFragment {

        private Task targetTask;
        private Date startTime;
        private ProgressBar progressBar;

        private TaskListAdapter.OnLongClickerListener listener;

        public TaskProgressDialogFragment() {}
        public void setTargetTask(Task target) {
            targetTask = target;
        }
        public void setListener(TaskListAdapter.OnLongClickerListener listener) {
            this.listener = listener;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            setRetainInstance(true);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.task_progress_dialog, null);

            progressBar = (ProgressBar)dialogView.findViewById(R.id.task_progress_bar);
            progressBar.setProgress((int)(targetTask.getCompletePercentage() * 100));

            int neutralString = targetTask.isCompleted() ? R.string.dialog_undo_complete : R.string.dialog_complete;

            builder.setView(dialogView)
                    .setTitle(targetTask.getName() + " " + getString(R.string.task_progress_title))
                    .setPositiveButton(R.string.dialog_schedule, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (listener != null) {
                                TaskPair pair = new TaskPair(targetTask.getID(), targetTask.getName(), 0);
                                listener.onTaskLongClick(pair);
                            }
                        }
                    })
                    .setNeutralButton(neutralString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            targetTask.setComplete(!targetTask.isCompleted());
                        }
                    })
                    .setNegativeButton(R.string.dialogCancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TaskProgressDialogFragment.this.getDialog().cancel();
                                }
                            });

            return builder.create();
        }
    }

