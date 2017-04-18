package foci.bu.outcalt.fociapp.timer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import foci.bu.outcalt.fociapp.BaseActivity;
import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.calm.BreatheActivity;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.inspire.QuoteActivity;
import foci.bu.outcalt.fociapp.tab.TabLayoutActivity;
import foci.bu.outcalt.fociapp.timer.db.SessionContract;
import foci.bu.outcalt.fociapp.timer.db.SessionDbHelper;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;
import foci.bu.outcalt.fociapp.todo.db.TaskContract;
import foci.bu.outcalt.fociapp.todo.db.TaskDbHelper;


public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button startButton;
    public TextView text;
    private boolean hasStarted = false;
    private long startTime = 5*1000; //25 * 60 * 1000
    private final long interval = 1 * 1000;
    private int interruptCounter = 0;

    TranslateAnimation sailboatAnimation;
    MediaPlayer mediaPlayer;
    private boolean musicStarted = false;
    private CountDownTimer myTimer;
    long timeLeft;
    //sessions
    private SessionDbHelper sessionDbHelper;
    private ArrayAdapter<String> sessionViewAdapter;
    private ListView sessionListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        mediaPlayer = MediaPlayer.create(this, R.raw.ambient);
        mediaPlayer.setLooping(true);

        startButton = (Button) this.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        text = (TextView) this.findViewById(R.id.timerText);

        sessionDbHelper = new SessionDbHelper(this);
        sessionListView = (ListView) findViewById(R.id.niceList);


        text.setText(""+String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes( startTime),
                TimeUnit.MILLISECONDS.toSeconds(startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Button musicB = (Button) findViewById(R.id.musicB);
        musicB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!musicStarted) {
                    audioPlayer();
                    musicStarted = true;
                } else {
                    stopMusic();
                    musicStarted = false;
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addSessionToDB(int interruptNum) {
        Format formatter = new SimpleDateFormat("MM-dd");
        String todaysDate = formatter.format(new Date());

        SQLiteDatabase db = sessionDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SessionContract.SessionEntry.COL_SESSION_TITLE, Integer.toString(interruptNum));
        values.put(SessionContract.SessionEntry.COL_SESSION_DATE_CREATED, todaysDate);
        db.insertWithOnConflict(SessionContract.SessionEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Toast.makeText(this, "You had " + interruptCounter + " interrupts", Toast.LENGTH_LONG).show();
        interruptCounter = 0;
        TextView counter = (TextView) findViewById(R.id.interCount);
        counter.setText("0");
    }

    public void broadcastIntent() {
        Intent intent = new Intent();
        //broadcast receiver intent filter must use this action string
        intent.setAction("com.outcalt.sendbroadcast.vibrate");
        //include intent filters of stopped applications too in the list of potential targets to resolve against
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        this.sendBroadcast(intent);
    }

    private void animateSailboat() {
        final ImageView image =(ImageView) this.findViewById(R.id.sailboat);

        sailboatAnimation = new TranslateAnimation(0, 1200, 0, 0);
        sailboatAnimation.setDuration(6000);
        sailboatAnimation.setFillAfter(true);
        sailboatAnimation.setRepeatCount(3);
        sailboatAnimation.setRepeatMode(2);

        image.startAnimation(sailboatAnimation);
    }

    private void stopAnimateSailboat() {
        sailboatAnimation.cancel();
    }

    @Override
    public void onClick(View v) {
        if (!hasStarted) {
            //creating new timer with new startTime so timeleft is persisted
            myTimer = new CountDownTimer(startTime, interval) {
                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    text.setText(""+String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }
                public void onFinish() {
                    text.setText("Time's up!");
                    broadcastIntent();
                    hasStarted = false;
                    startTime = 5 * 1000; //TODO 25 * 60 * 1000
                    startButton.setText("START AGAIN");
                    addSessionToDB(interruptCounter);
                }
            };
            animateSailboat();
            myTimer.start();
            startButton.setText("PAUSE");
            hasStarted = true;
        } else {
            stopAnimateSailboat();
            myTimer.cancel();
            System.out.println("SAVED: " + timeLeft);
            startTime = timeLeft;
            startButton.setText("GO");
            hasStarted = false;
        }
    }

    public void audioPlayer() {
        try {
            mediaPlayer.start();
        } catch (Exception e) {
            System.out.println("Something bad happened: " + e);
        }
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    public void addInterrupt(View view) {
        TextView counter = (TextView) findViewById(R.id.interCount);
        ++interruptCounter;
        counter.setText(Integer.toString(interruptCounter));
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
            case R.id.menu_session_history:
                intent = new Intent(this, TimerSessionActivity.class);
                this.startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}

