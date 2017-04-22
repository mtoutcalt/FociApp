package foci.bu.outcalt.fociapp.todo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import foci.bu.outcalt.fociapp.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mark on 4/22/2017.
 */

public class TodoAlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Repeating Alarm worked.", Toast.LENGTH_LONG).show();

        intent = new Intent(context, ToDoActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder  = new NotificationCompat.Builder(context)
                .setContentTitle("Reminder")
                .setContentText("Have you completed your ToDo list for today?")
                .setSmallIcon(R.drawable.ic_done_all_black_24dp)
                .setColor(Color.RED)
                .setSound(defaultSoundUri)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder
                (android.R.drawable.sym_action_chat,
                        "GO GET'EM",
                        pIntent).build();

        builder.addAction(action);
        NotificationManager notifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(0, builder.build());
    }

}
