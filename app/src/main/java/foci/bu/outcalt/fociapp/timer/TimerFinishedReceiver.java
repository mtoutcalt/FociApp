package foci.bu.outcalt.fociapp.timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class TimerFinishedReceiver extends BroadcastReceiver {

    public TimerFinishedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);
        Toast.makeText(context, "VIBRATING", Toast.LENGTH_LONG).show();
    }
}
