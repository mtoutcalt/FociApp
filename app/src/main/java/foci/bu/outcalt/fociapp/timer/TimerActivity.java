package foci.bu.outcalt.fociapp.timer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import foci.bu.outcalt.fociapp.BaseActivity;
import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;


public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private CountDownTimer countDownTimer;
    public static final String EXTRA_MESSAGE = "cs683.bu.outcalt.foci.MESSAGE";
    private Button startButton;
    public TextView text;
    private boolean hasStarted = false;
    private final long startTime = 25 * 60 * 1000;

    private final long interval = 1 * 1000;

    TranslateAnimation sailboatAnimation;
    MediaPlayer mediaPlayer;
    private boolean musicStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        mediaPlayer = MediaPlayer.create(this, R.raw.ambient);
        mediaPlayer.setLooping(true);

        startButton = (Button) this.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        text = (TextView) this.findViewById(R.id.timerText);

        countDownTimer = new DefaultCountDownTimer(startTime, interval, text);

        text.setText(""+String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes( startTime),
                TimeUnit.MILLISECONDS.toSeconds(startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
            animateSailboat();
            countDownTimer.start();
            startButton.setText("PAUSE");
            hasStarted = true;
        } else {
            stopAnimateSailboat();
            countDownTimer.cancel();
            startButton.setText("GO");
            hasStarted = false;
        }
    }

    public void audioPlayer() {
        try {
//            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.ambient);
            mediaPlayer.start();
        } catch (Exception e) {
            System.out.println("AHHHH5: " + e);
        }
    }

    public void stopMusic() {
        mediaPlayer.stop();
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
            default: return super.onOptionsItemSelected(item);
        }
    }
}

