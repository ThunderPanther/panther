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

        // private CreateTaskListener listener;

        private Task targetTask;
        private Date startTime;
        private ProgressBar progressBar;

        public TaskProgressDialogFragment() {}
        public void setTargetTask(Task target) {
            targetTask = target;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.task_progress_dialog, null);

            progressBar = (ProgressBar)dialogView.findViewById(R.id.task_progress_bar);
            progressBar.setProgress((int)(targetTask.getCompletePercentage() * 100));

            builder.setView(dialogView)
                    .setTitle(targetTask.getName() + " " + getString(R.string.task_progress_title))
                    .setPositiveButton(R.string.dialog_OK,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TaskProgressDialogFragment.this.getDialog().cancel();
                                }
                            });

            return builder.create();
        }
    }

