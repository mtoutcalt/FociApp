package foci.bu.outcalt.fociapp.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.creative.BrainstormTopicActivity;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.inspire.QuoteActivity;
import foci.bu.outcalt.fociapp.tab.TabLayoutActivity;
import foci.bu.outcalt.fociapp.timer.TimerActivity;
import foci.bu.outcalt.fociapp.timer.TimerSessionActivity;
import foci.bu.outcalt.fociapp.todo.db.TaskContract;
import foci.bu.outcalt.fociapp.todo.db.TaskDbHelper;

/*
    code adapted from examples from https://www.sitepoint.com/starting-android-development-creating-todo-app/
    and Android Studio 2.2 Development Essentials by Neil Smyth
 */
public class ToDoActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private static final int REQUEST_CODE = 101;
    private static final String TAG = "StateChange";
    private TaskDbHelper taskDbHelper;
    private ListView taskListView;
    private ArrayAdapter<String> taskViewAdapter;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    View snackView;

    private TextView gestureText;
    private GestureDetectorCompat gDetector;

    public ToDoActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setAlarm();

//        //gesture
//        gestureText = (TextView) findViewById(R.id.gestureStatusText2);
//        this.gDetector = new GestureDetectorCompat(this, this);
//        gDetector.setOnDoubleTapListener(this);

        //intialize the dbHelper
        taskDbHelper = new TaskDbHelper(this);
        taskListView = (ListView) findViewById(R.id.list_tasks);
        updateUI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addListItem();
                snackView = view;
            }
        });
    }

    private void setAlarm(){
        Intent intent = new Intent(this, TodoAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES , pendingIntent);  //set repeating every 24 hours
    }

    private void addListItem() {
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Add Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = taskDbHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
                                values.put(TaskContract.TaskEntry.COL_TASK_DATE_CREATED, createCurrentTime());
                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                                        null,
                                        values,
                                        SQLiteDatabase.CONFLICT_REPLACE);
                                updateUI();
                                db.close();
                                Log.d(TAG, "Task to add: " + task);
                                callSnackBar();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
    }

    private String createCurrentTime() {
        Format formatter = new SimpleDateFormat("MM-dd");
        return formatter.format(new Date());
    }

    private void callSnackBar() {
        Snackbar.make(snackView, "Item added to list", Snackbar.LENGTH_LONG)
                .setAction("Undo", undoOnClickListener).show();
    }

    View.OnClickListener undoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            listItems.remove(listItems.size()-1);
//            adapter.notifyDataSetChanged();
            View deleteView = findViewById(R.id.task_title);
            deleteTask(deleteView);
            Snackbar.make(view, "Item removed", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = taskDbHelper.getReadableDatabase();
        String[] projection = {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DATE_CREATED};

        Cursor cursor = db.query(
                TaskContract.TaskEntry.TABLE,  //table
                projection, //columns to return
                null, null, null, null, null);  //columns for where clause, values for where clause, row grouping, filter by group, sort order

        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (taskViewAdapter == null) {
            taskViewAdapter = new ArrayAdapter<>(this,
                    R.layout.todo_dialog,  //view to use for tasks
                    R.id.task_title,  //where to put the data
                    taskList);  //get data from list of Tasks
            taskListView.setAdapter(taskViewAdapter);
        } else {
            taskViewAdapter.clear();
            taskViewAdapter.addAll(taskList);
            taskViewAdapter.notifyDataSetChanged();  //notify view that data has changed
        }
        cursor.close();
        db.close();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");

//        final EditText textBox = (EditText) findViewById(R.id.editText2);
//        CharSequence userText = textBox.getText();
//        Log.i(TAG, "savedText is: " + userText);
//        outState.putCharSequence("savedText", userText);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState");

//        final EditText textBox = (EditText) findViewById(R.id.editText2);
//        CharSequence userText = savedInstanceState.getCharSequence("savedText");
//        Log.i(TAG, "restore Saved Text: " + userText);
//        textBox.setText(userText);
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

    public void deleteTask(View view) {
        Log.i(TAG, "TaskView: " + view);
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        Log.i(TAG, "Task to delete: " + task);
        SQLiteDatabase db = taskDbHelper.getWritableDatabase();
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

    //gestures
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent");
        this.gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        Log.i(TAG, "onDown");
        gestureText.setText("onDown");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        Log.i(TAG, "onFling");
        gestureText.setText("onFling");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        gestureText.setText("onLongPress");
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        gestureText.setText("onScroll");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        gestureText.setText("onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        gestureText.setText("onSingleTapUp");
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        gestureText.setText("onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        gestureText.setText("onDoubleTapEvent");
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        gestureText.setText("onSingleTapConfirmed");
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
