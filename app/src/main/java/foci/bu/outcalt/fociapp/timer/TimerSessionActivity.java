package foci.bu.outcalt.fociapp.timer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.timer.db.SessionContract;
import foci.bu.outcalt.fociapp.timer.db.SessionDbHelper;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        showSessions();
    }

    public void showSessions() {
        ArrayList<String> sessionList = new ArrayList<>();
        ArrayList<String> createdList = new ArrayList<>();
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
//            createdList.add(cursor.getString(blah));
        }

        System.out.println("SESSIONS: " + sessionList.size());
        //TODO FIX THIS
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
}
