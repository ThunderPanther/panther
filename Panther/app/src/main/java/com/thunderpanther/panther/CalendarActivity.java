package com.thunderpanther.panther;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

import java.util.Date;

public class CalendarActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
    CreateTaskDialogFragment.CreateTaskListener, ScheduleTaskDialogFragment.WorkSessionCreateListener {

    CalendarView calendar;
    TasksSQLiteHelper TDBHelper;

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
        TDBHelper = new TasksSQLiteHelper(this);
        User.setDB(TDBHelper);
        setContentView(R.layout.activity_calendar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
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
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);
        //sets the listener to be notified upon selected date change.
        calendar.setOnDateChangeListener(new OnDateChangeListener() {
            //show the selected date as a toast
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(this, DayViewActivity.class);
                startActivity(new Intent("com.thunderpanther.panther.DayViewActivity"));
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

    //TODO: this fucking function
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

    private int notificationID = 100;

    private void showNotification(String title, String text) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_action_clock)
                        .setContentTitle(title)
                        .setContentText(text);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, CalendarActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(CalendarActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());
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

        Task t = new Task(name, weight, timeEst, TDBHelper.getNextId());
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
        TDBHelper.addTaskToDB( t.getID(), t.getName(), t.getWeight(), t.getTimeEstimate(), pid);
        showNotification("New Task!","Estimated "+t.getTimeEstimate()+"Hours Remaining on " + t.getName());
    }

    @Override
    public void onDeleteTask(int id) {
        User.getCurrentUser().removeTask(id);
        // remove from db
        TDBHelper.deleteTask(id);
        // TODO: is this necessary?
        mNavigationDrawerFragment.refreshTaskList();
    }

    @Override
    public void onCreateWorkSession(Date startTime, Date endTime, Task target) {
        // TODO: DB id!
        WorkSession w = new WorkSession(TDBHelper.getNextWorkSessionId(), startTime, endTime, target);
        User.getCurrentUser().scheduleWorkSession(w);
        storeWSinDB(w);
    }

    private void storeWSinDB(WorkSession w) {
        // TODO:
        TDBHelper.addWorkSessionToDB(w.getID(), w.getTarget().getID(), w.getStartTime(), w.getEndTime());
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((CalendarActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
