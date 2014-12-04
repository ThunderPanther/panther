package com.thunderpanther.panther;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ListActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.*;


public class DayViewActivity extends ListActivity implements ScheduleTaskDialogFragment.WorkSessionCreateListener {
    private static int HOURS_PER_DAY = 24;

    Context mContext = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        final int id = getIntent().getExtras().getInt("id");

        super.onCreate(savedInstanceState);
        //getListView().setBackgroundColor(Color.rgb(12, 12, 12));
        getListView().setDividerHeight(0);
        setListAdapter(new ListAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isEnabled(int arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return HOURS_PER_DAY;
            }

            @Override
            public Object getItem(int arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long getItemId(int arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getItemViewType(int arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public View getView(final int position, View arg1, ViewGroup arg2) {
                // TODO Auto-generated method stub
                LayoutInflater inflater = getLayoutInflater();
                View listItem = (View) inflater.inflate(R.layout.list_item, getListView(), false);
                TextView hourTV = (TextView) listItem.findViewById(R.id.hourTV);
                TextView amTV = (TextView) listItem.findViewById(R.id.amTV);
                hourTV.setTextColor(Color.BLUE);
                amTV.setTextColor(Color.BLUE);
                final LinearLayout eventsLL = (LinearLayout) listItem.findViewById(R.id.eventsLL);
                hourTV.setText(String.valueOf((position )));
                //I set am/pm for each entry ... you could specify which entries
                if (((position >= 0) && (position <= 2)) || ((position >= 15) && (position <= 23)))
                    amTV.setText("AM");
                else
                    amTV.setText("PM");
                eventsLL.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        /*AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                        alert.setTitle("New Event");
                        alert.setMessage("Event:");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(mContext);
                        alert.setView(input);

                        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                TextView A = new TextView(mContext);
                                A.setText(input.getText());
                                A.setTextColor(Color.BLACK);
                                eventsLL.addView(A);
                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                        alert.show();*/
                        if (id >= 0) {
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            int year = getIntent().getExtras().getInt("year");
                            int month = getIntent().getExtras().getInt("month");
                            int day = getIntent().getExtras().getInt("day");

                            cal.set(year, month + java.util.Calendar.JANUARY, day, position, 0, 0);
                            Date startDate = cal.getTime();

                            ScheduleTaskDialogFragment dialog = new ScheduleTaskDialogFragment();
                            Task target = User.getCurrentUser().getTask(id);
                            dialog.setTargetTask(target);
                            dialog.setStartTime(startDate);
                            dialog.show(getFragmentManager(), "schedule_worksession_dialog");
                        }
                    }

                });
                return listItem;
            }

            @Override
            public int getViewTypeCount() {
                // TODO Auto-generated method stub
                return 1;
            }

            @Override
            public boolean hasStableIds() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isEmpty() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver arg0) {
                // TODO Auto-generated method stub

            }

        });
    }

    @Override
    public void onCreateWorkSession(Date startTime, Date endTime, Task target) {
        // TODO: DB id!
        WorkSession w = new WorkSession(Application.getDB().getNextWorkSessionId(), startTime, endTime, target);
        User.getCurrentUser().scheduleWorkSession(w);
        storeWSinDB(w);
    }

    private void storeWSinDB(WorkSession w) {
        // TODO:
        Application.getDB().addWorkSessionToDB(w.getID(), w.getTarget().getID(), w.getStartTime(), w.getEndTime());
    }
}