package com.thunderpanther.panther;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.Toast;
import android.content.Intent;

import java.util.*;

public class CalendarActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
    CreateTaskDialogFragment.CreateTaskListener, ScheduleTaskDialogFragment.WorkSessionCreateListener,
    TaskListAdapter.OnLongClickerListener {

    CalendarView calendar;
    // TasksSQLiteHelper TDBHelper;

    boolean taskSelected = false;
    TaskPair selectedTask;
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Application.initDB(this);
        User.setDB(Application.getDB());
        setContentView(R.layout.activity_calendar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setCalendarActivity(this);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        initializeCalendar();
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendar);

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Sunday as the first day of the Calendar
        calendar.setFirstDayOfWeek(1);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.light_yellow));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.light_yellow);
        calendar.setClickable(true);
        /*
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Info", "CLICKCKCKC");
                Date date = new Date(calendar.getDate());
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
                int month = cal.get(java.util.Calendar.MONTH);
                int year = cal.get(java.util.Calendar.YEAR);
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.thunderpanther.panther.DayViewActivity");
                if(taskSelected == true) {
                    intent.putExtra("id", selectedTask.id);
                    intent.putExtra("name", selectedTask.name);
                    intent.putExtra("depth", selectedTask.depth);
                    Log.d("cal", "taskSelected: " + selectedTask.name);
                    selectedTask = null;
                    taskSelected = false;

                } else {
                    intent.putExtra("id", -1);
                }
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);

                startActivity(intent);
            }
        });
        */

        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.thunderpanther.panther.DayViewActivity");
                if(taskSelected == true) {
                    intent.putExtra("id", selectedTask.id);
                    intent.putExtra("name", selectedTask.name);
                    intent.putExtra("depth", selectedTask.depth);
                    Log.d("cal", "taskSelected: " + selectedTask.name);
                    selectedTask = null;
                    taskSelected = false;

                } else {
                    intent.putExtra("id", -1);
                }
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        // FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction()
         //       .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
         //       .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public void collapseTaskToggle(View v) {
        //Task itemToRemove = (AtomPayment)v.getTag();
        // adapter.remove(itemToRemove);
        TaskPair p = (TaskPair)v.getTag();
        int position = mNavigationDrawerFragment.getTaskListPosition(p.id);
        mNavigationDrawerFragment.toggleCollapsed(position);
    }


    public void createSubtask(View v) {
        TaskPair p = (TaskPair)v.getTag();
        Log.i("This", p.id + ": " + p.name);
        Task parent = User.getCurrentUser().getTask(p.id);

        CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
        dialog.setParentTask(parent);
        dialog.show(getFragmentManager(), "create_task_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.calendar, menu);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.calendar, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void createTask() {
        CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
        // dialog.setTitle(taskName);
        dialog.show(getFragmentManager(), "create_task_dialog");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
       switch(item.getItemId()) {
           case R.id.action_settings:
               return true;
           case R.id.create_task:
               createTask();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    @Override
    public void onCreateTaskConfirm(String name, Task parent, int weight, double timeEst) {
        Log.d("info", "Task created! (really)");
        Log.d("info", name + " " + weight);

        Task t = new Task(name, weight, timeEst, Application.getDB().getNextId());
        Log.d("info", "It wont get here");
        User.getCurrentUser().addTask(t, parent);
        storeTaskInDB(t);
        mNavigationDrawerFragment.refreshTaskList();
    }

    public void storeTaskInDB(Task t){
        Log.d("info", "Storing Task in DB");
        int pid;
        if(t.getParent() == null){
            pid = -1;
        }else{
            pid = t.getParent().getID();
        }
        Application.getDB().addTaskToDB(t.getID(), t.getName(), t.getWeight(), t.getTimeEstimate(), pid, t.isCompleted());
    }

    @Override
    public void onDeleteTask(int id) {
        User.getCurrentUser().removeTask(id);
        // remove from db
        Application.getDB().deleteTask(id);
        // TODO: is this necessary?
        mNavigationDrawerFragment.refreshTaskList();
    }

    @Override
    public void updateTask(Task t) {
        Application.getDB().updateTaskInDB(t.getID(), t.getName(), t.getWeight(), t.getTimeEstimate());
        User.getCurrentUser().invalidateTaskList();
        mNavigationDrawerFragment.refreshTaskList();
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

    @Override
    public void onTaskLongClick(TaskPair taskPair) {
        mNavigationDrawerFragment.minimize();
        taskSelected = true;
        selectedTask = taskPair;
        Log.d("lol", "selectedTask.name: " + selectedTask.name);
    }
}
