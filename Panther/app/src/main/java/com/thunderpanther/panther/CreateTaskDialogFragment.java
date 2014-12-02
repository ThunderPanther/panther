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

/**
 * Created by Steven on 11/28/2014.
 */
public class CreateTaskDialogFragment extends DialogFragment {

    public interface CreateTaskListener {
        public void onCreateTaskConfirm(String name, int weight);
    }

    private String mTitle;
    private CreateTaskListener listener;
    private EditText mTaskName;
    private EditText mTaskWeight;

    public CreateTaskDialogFragment() {}
    public void setTitle(String title) {
        mTitle = title;
    }

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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_task_dialog, null);

        mTaskName = (EditText)dialogView.findViewById(R.id.task_name);
        mTaskWeight = (EditText)dialogView.findViewById(R.id.task_weight);

        builder.setView(dialogView)
                .setTitle(R.string.create_task_title)
                .setPositiveButton(R.string.dialogOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onCreateTaskConfirm(mTaskName.getText().toString(),
                                    Integer.parseInt(mTaskWeight.getText().toString()));
                        }
                    }
                })
                .setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateTaskDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
