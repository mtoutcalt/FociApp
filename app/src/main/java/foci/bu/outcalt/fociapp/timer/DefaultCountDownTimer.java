package foci.bu.outcalt.fociapp.timer;

import android.os.CountDownTimer;
import android.widget.TextView;

public class DefaultCountDownTimer extends CountDownTimer {

    TextView text;

    public DefaultCountDownTimer(long startTime, long interval, TextView text) {
        super(startTime, interval);
        this.text = text;
    }

    @Override
    public void onFinish() {
        text.setText("Time's up!");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        text.setText("" + millisUntilFinished / 1000);
    }
}