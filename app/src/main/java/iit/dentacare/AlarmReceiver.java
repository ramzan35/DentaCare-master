package iit.dentacare;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "ALARM ON", Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(context, RingtonePlayingService.class);
        context.startService(intent1);

        createNotification(context);
    }

    private void createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_action_call)
                .setContentTitle("It's time to brush for tonight!")
                .setContentText("Pickup your brush and start brushing to cancel the alarm.")
                .setSubText("Tap to Cancel")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        Intent intent = new Intent(context, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Intent dismissIntent = new Intent(context, RingtonePlayingService.class);
        dismissIntent.setAction(RingtonePlayingService.ACTION_DISMISS);
        PendingIntent pendingIntent1 = PendingIntent.getService(context, 123, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action action = new NotificationCompat
                .Action(android.R.drawable.ic_lock_idle_alarm, "DISMISS", pendingIntent1);

        builder.addAction(action);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Notification notification = builder.build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(321, notification);


    }
}