package foci.bu.outcalt.fociapp.timer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import foci.bu.outcalt.fociapp.BaseActivity;
import foci.bu.outcalt.fociapp.R;


public class TimerActivity extends AppCompatActivity implements View.OnClickListener {

    private CountDownTimer countDownTimer;
    public static final String EXTRA_MESSAGE = "cs683.bu.outcalt.foci.MESSAGE";
    private Button startButton;
    public TextView text;
    private boolean hasStarted = false;
    private final long startTime = 25 * 60 * 1000;

    private final long interval = 1 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        startButton = (Button) this.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        text = (TextView) this.findViewById(R.id.timerText);

        countDownTimer = new DefaultCountDownTimer(startTime, interval, text);

        text.setText(text.getText() + String.valueOf(startTime / (1000)));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        animateSailboat();
    }

    private void animateSailboat() {
        final ImageView image =(ImageView) this.findViewById(R.id.sailboat);

        final TranslateAnimation sailboatAnimation = new TranslateAnimation(0, 1200, 0, 0);
        sailboatAnimation.setDuration(6000);
        sailboatAnimation.setFillAfter(true);
        sailboatAnimation.setRepeatCount(3);
        sailboatAnimation.setRepeatMode(2);

        Button button = (Button) this.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                image.startAnimation(sailboatAnimation);
            }

        });
    }

    @Override
    public void onClick(View v) {
        if (!hasStarted) {
            countDownTimer.start();
            startButton.setText("PAUSE");
            hasStarted = true;
        } else {
            countDownTimer.cancel();
            startButton.setText("GO");
            hasStarted = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_red:
                Intent intent = new Intent(this, BaseActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.menu_green:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                return true;
            case R.id.menu_yellow:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                return true;
            case R.id.menu_blue:
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user taps the Send button
     * https://developer.android.com/training/basics/firstapp/starting-activity.html#BuildIntent*/
//    public void sendMessage(View view) {
//        Intent intent = new Intent(this, FragmentExampleActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
//    }
}

