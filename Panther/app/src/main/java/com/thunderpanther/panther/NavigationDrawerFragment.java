package com.thunderpanther.panther;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ActionBar;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.thunderpanther.panther.R;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

//<<<<<<< Updated upstream
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@a link NavigationDrawerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@a link NavigationDrawerFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NavigationDrawerFragment extends Fragment {
    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private TaskListAdapter adapter;
    List<TaskPair> taskList;
    boolean[] isCollapsed;

    private CalendarActivity calendarActivity;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        //setUpListViewAdapter();
        //setupAddPaymentButton();
        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);



    }





    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    public void refreshTaskList() {
        User currentUser = User.getCurrentUser();
        if (taskList == null) {
            taskList = currentUser.getTaskList();
            // Defaults to false
            isCollapsed = new boolean[taskList.size()];
        } else if (currentUser.isTaskListModified()) {
            List<TaskPair> newTaskList = currentUser.getTaskList();
            boolean[] newCollapsed = new boolean[newTaskList.size()];

            int j = 0;
            for (int i = 0; i < newCollapsed.length && j < isCollapsed.length; i++) {
                if (taskList != null && newTaskList.get(i).id == taskList.get(j).id) {
                    newCollapsed[i] = isCollapsed[j++];
                } else {
                    newCollapsed[i] = false;
                }
            }

            taskList = newTaskList;
            isCollapsed = newCollapsed;
        }


        // List<String> tasks = new ArrayList<String>();
        List<TaskPair> tasks = new ArrayList<TaskPair>();
        List<Boolean> collapsed = new ArrayList<Boolean>();

        for (int i = 0; i < taskList.size(); i++) {
            StringBuilder indentBuilder = new StringBuilder();
            int depth = taskList.get(i).depth;

            for (int j = 0; j < depth; j++) {
                indentBuilder.append("    ");
            }
            // tasks.add(indentBuilder.append(taskList.get(i).name).toString());
            tasks.add(taskList.get(i));
            collapsed.add(isCollapsed[i]);

            if (isCollapsed[i]) {
                // Skip past tasks with greater depth
                int j;
                for (j = i + 1; j < taskList.size() && taskList.get(j).depth > depth; j++);
                i = j - 1;
            }
        }

        mDrawerListView.setAdapter(new TaskListAdapter(
                getActivity(),
                R.layout.task_list_item,
                // taskList,
                tasks,
                getFragmentManager(),
                collapsed,
                calendarActivity));

    }

    public void setCalendarActivity(CalendarActivity cal) {
        this.calendarActivity = cal;
    }

    public void minimize() {
        mDrawerLayout.closeDrawer(mDrawerListView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDrawerListView = (ListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
                //Toast.makeText(getActivity(), "Clicked a Task.", Toast.LENGTH_SHORT).show();  DOESNT DO ANYTHING
            }
        });

        mDrawerListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
        /*mDrawerListView.setOnItemClickListener(
                new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        //Take action here.


                    }
                }
        );*/
        refreshTaskList();

        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
        return mDrawerListView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                refreshTaskList();

                super.onDrawerOpened(drawerView);

                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public int getTaskListID(int position) {
        for (int i = 0, k = 0; i < taskList.size(); i++, k++) {
            if (k == position) {
                return taskList.get(k).id;
            }

            int depth = taskList.get(i).depth;

            if (isCollapsed[i]) {
                // Skip past tasks with greater depth
                int j;
                for (j = i + 1; j < taskList.size() && taskList.get(j).depth > depth; j++);
                i = j - 1;
            }
        }
        // Error, not found (will this ever happen)
        return -1;
    }

    public int getTaskListPosition(int id) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    public void toggleCollapsed(int position) {
        isCollapsed[position] ^= true;
        refreshTaskList();
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);

            // TODO: get task ID
            String taskName = ((String)mDrawerListView.getAdapter().getItem(position)).trim();
            // TODO: pull up edit task

            if (taskList != null) {
                CreateTaskDialogFragment dialog = new CreateTaskDialogFragment();
                int taskID = getTaskListID(position);
                dialog.setExistingTask(User.getCurrentUser().getTask(taskID));
                dialog.show(getFragmentManager(), "create_task_dialog");
            }
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(getActivity(), "Collapse/Expand.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {

        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
