package foci.bu.outcalt.fociapp.timer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.creative.BrainstormTopicActivity;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.inspire.QuoteActivity;
import foci.bu.outcalt.fociapp.tab.TabLayoutActivity;
import foci.bu.outcalt.fociapp.timer.db.SessionContract;
import foci.bu.outcalt.fociapp.timer.db.SessionDbHelper;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;
import foci.bu.outcalt.fociapp.todo.db.TaskContract;

/**
 * Created by mark on 4/16/2017.
 */

public class TimerSessionActivity extends AppCompatActivity {

    //sessions
    private SessionDbHelper sessionDbHelper;
    private ArrayAdapter<String> sessionViewAdapter;
    private ListView sessionListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_sessions);

        sessionDbHelper = new SessionDbHelper(this);
        sessionListView = (ListView) findViewById(R.id.niceList);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        showSessions();
    }

    public void showSessions() {
        ArrayList<String> sessionList = new ArrayList<>();
        SQLiteDatabase db = sessionDbHelper.getReadableDatabase();
        String[] projection = {SessionContract.SessionEntry._ID, SessionContract.SessionEntry.COL_SESSION_TITLE, TaskContract.TaskEntry.COL_TASK_DATE_CREATED};

        Cursor cursor = db.query(
                SessionContract.SessionEntry.TABLE,  //table
                projection, //columns to return
                null, null, null, null, null);  //columns for where clause, values for where clause, row grouping, filter by group, sort order

        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(SessionContract.SessionEntry.COL_SESSION_TITLE);
            int blah = cursor.getColumnIndex(SessionContract.SessionEntry.COL_SESSION_DATE_CREATED);
            sessionList.add("Interrupts: " + cursor.getString(idx) + "       Date: " + cursor.getString(blah));
        }

        if (sessionViewAdapter == null) {
            sessionViewAdapter = new ArrayAdapter<>(this,
                    R.layout.timer_session_task,  //view to use for tasks
                    R.id.session_title,  //where to put the data
                    sessionList);  //get data from list of Tasks
            sessionListView.setAdapter(sessionViewAdapter);
        } else {
            sessionViewAdapter.clear();
            sessionViewAdapter.addAll(sessionList);
            sessionViewAdapter.notifyDataSetChanged();  //notify view that data has changed
        }
        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.foci_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_home:
                intent = new Intent(this, HomeActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_todo:
                intent = new Intent(this, ToDoActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_habit:
                intent = new Intent(this, HabitActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_timer:
                intent = new Intent(this, TimerActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_breathe:
                intent = new Intent(this, BreatheActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_info:
                intent = new Intent(this, TabLayoutActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_quote:
                intent = new Intent(this, QuoteActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_brainstorm:
                intent = new Intent(this, BrainstormTopicActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_session_history:
                intent = new Intent(this, TimerSessionActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
