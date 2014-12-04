package com.thunderpanther.panther;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Calendar;


public class DayViewActivity extends ListActivity implements ScheduleTaskDialogFragment.WorkSessionCreateListener {
    private static int HOURS_PER_DAY = 24;
    private static int id = -1;
    Context mContext = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Info", "onCreate");


        id = getIntent().getExtras().getInt("id");
        final int year = getIntent().getExtras().getInt("year");
        final int month = getIntent().getExtras().getInt("month");
        final int day = getIntent().getExtras().getInt("day");

        Log.d("info", year + " " + month + " " + day);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(year, month + java.util.Calendar.JANUARY, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        cal.set(year, month + java.util.Calendar.JANUARY, day, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 0);
        Date endDate = cal.getTime();

        final List<WorkSession> sessions = User.getCurrentUser().getCalendar().getWorkSessionsInRange(startDate, endDate);

        Log.d("Session length", sessions.size() + " ");
        for (WorkSession session : sessions) {
            Log.d("Session Info", session.getStartTime().toString() + " - " + session.getEndTime().toString());
        }

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
                final View listItem = (View) inflater.inflate(R.layout.list_item, getListView(), false);
                TextView hourTV = (TextView) listItem.findViewById(R.id.hourTV);
                TextView amTV = (TextView) listItem.findViewById(R.id.amTV);
                hourTV.setTextColor(Color.BLUE);
                amTV.setTextColor(Color.BLUE);
                final LinearLayout eventsLL = (LinearLayout) listItem.findViewById(R.id.eventsLL);
                hourTV.setText(String.valueOf((position )));
                //I set am/pm for each entry ... you could specify which entries
                /*
                if (((position >= 0) && (position <= 2)) || ((position >= 15) && (position <= 23)))
                    amTV.setText("AM");
                else
                    amTV.setText("PM");
                */

                amTV.setText(position < 12 ? "AM" : "PM");
                int newPosition = (position >= 12) ? position - 12 : position;
                hourTV.setText((newPosition != 0 ? newPosition : 12) + "");

                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(year, month + java.util.Calendar.JANUARY, day, position, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long rangeStart = cal.getTime().getTime();
                cal.set(year, month + java.util.Calendar.JANUARY, day, position, 59, 59);
                cal.set(Calendar.MILLISECOND, 0);
                long rangeEnd = cal.getTime().getTime();

                boolean hasWorkSession = false;
                WorkSession targetWS = null;
                for (WorkSession w : sessions) {
                    long wTime = w.getStartTime().getTime();
                    long wTime2 = w.getEndTime().getTime();
                    // Log.d("WS Info", rangeStart + " - " + rangeEnd + " :: " + wTime);

                    if (wTime >= rangeStart && wTime <= rangeEnd) {
                        hasWorkSession = true;
                        targetWS = w;
                        break;
                    } else if (wTime < rangeStart && wTime2 > rangeStart || wTime < rangeEnd && wTime2 > rangeEnd) {
                        hasWorkSession = true;
                    }
                }
                if (hasWorkSession) {
                    Log.d("info", "hasWorkSession");
                    // hourTV.setTextColor(Color.RED);
                    boolean isStart = targetWS != null;

                    eventsLL.setBackgroundColor(isStart ? Color.YELLOW : getResources().getColor(R.color.light_yellow));
                    if (isStart) {
                        TextView text = new TextView(mContext);
                        text.setText(targetWS.getTarget().getName());
                        text.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
                        eventsLL.addView(text);
                    }
                }

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

                            cal.set(year, month + java.util.Calendar.JANUARY, day, position, 0, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            Date startDate = cal.getTime();

                            ScheduleTaskDialogFragment dialog = new ScheduleTaskDialogFragment();
                            Task target = User.getCurrentUser().getTask(id);
                            dialog.setTargetTask(target);
                            dialog.setStartTime(startDate);
                            dialog.show(getFragmentManager(), "schedule_task_dialog");
                            id = -1;
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

        onCreate(null);
    }

    private void storeWSinDB(WorkSession w) {
        // TODO:
        Application.getDB().addWorkSessionToDB(w.getID(), w.getTarget().getID(), w.getStartTime(), w.getEndTime());
    }
}