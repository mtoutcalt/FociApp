package foci.bu.outcalt.fociapp.calm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import foci.bu.outcalt.fociapp.R;
import foci.bu.outcalt.fociapp.habit.HabitActivity;
import foci.bu.outcalt.fociapp.home.HomeActivity;
import foci.bu.outcalt.fociapp.timer.TimerActivity;
import foci.bu.outcalt.fociapp.todo.ToDoActivity;

public class BreathActivity extends AppCompatActivity {

    int counter = 1;
    TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.breathe_animate);
        textView = (TextView) findViewById(R.id.breathText);

        ImageView mImageViewFilling = (ImageView) findViewById(R.id.animate_breathe);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();

        textView.postDelayed(updateText, 3800);
    }

    Runnable updateText = new Runnable () {
        public void run() {
            determineTextChange();
            textView.postDelayed(updateText, 3800);
        }
    };

    private void determineTextChange() {
        switch (counter) {
            case 1: textView.setText("Hold");
                    counter++;
                    break;
            case 2: textView.setText("BREATHE OUT");
                    counter++;
                    break;
            case 3: textView.setText("HOLD");
                     counter++;
                    break;
            case 4: textView.setText("BREATHE");
                counter = 1;
                break;
            default: textView.setText("ERROR");
        }
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
