package iit.dentacare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmActivity extends AppCompatActivity {

    @BindView(R.id.timePicker)

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
    }

    public void startAlarm(View view) {
        Toast.makeText(this, "ALARM IS ON", Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();

        if(Build.VERSION.SDK_INT >= 23) {
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getMinute());
        }else {
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        }

        intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP
                , calendar.getTimeInMillis(), 1000 * 60, pendingIntent);
    }

    public void cancelAlarm(View view) {
        Intent i = new Intent(this, RingtonePlayingService.class);
        stopService(i);

        intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}